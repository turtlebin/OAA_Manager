package gri.engine.core;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.kafka.clients.consumer.Consumer;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import csAsc.EIB.Engine.Authentication;
import csAsc.EIB.Engine.EITPLocation;
import csAsc.EIB.Engine.Engine;
import csAsc.EIB.Engine.IOrderHandler;
import csAsc.EIB.Engine.IServerHandler;
import csAsc.EIB.Engine.Order;
import csAsc.EIB.Engine.StatusCodeUtil;
import csAsc.EIB.Engine.ServerHandler.ServerHandlerTypes;
import csAsc.EIB.ServerEngine.ServerEngine;
import gir.engine.monitor.Message2;
import gir.engine.monitor.MessageEngine;
import gir.engine.monitor.MessageGenerator;
import gir.engine.monitor.MessageInitiator;
import gir.engine.monitor.OKHttpUtil;
import gir.engine.monitor.SendHelper;
import gir.engine.monitor.aMessage;
import gri.driver.model.AccessPermission;
import gri.driver.model.DataChangeMessage;
import gri.driver.model.GriDoc;
import gri.driver.model.GriElement;
import gri.driver.model.GriElementData2;
import gri.driver.model.GriElementData3;
import gri.driver.model.GriElementData4;
//import gri.driver.model.Paragraph;
import gri.driver.model.process.Paragraph;
import gri.driver.model.ParagraphDataReadRequest;
import gri.driver.model.ParagraphDataReadResponse;
import gri.driver.model.ParagraphDataWriteRequest;
import gri.driver.model.Section;
import gri.driver.model.process.ProcessDto1;
import gri.driver.model.process.Processor;
import gri.driver.model.process.StatsConfData2;
import gri.driver.model.process.StatsConfDto1;
import gri.driver.model.process.StatsResult;
import gri.driver.model.User;
import gri.driver.util.DriverConstant;
import gri.engine.model.dao.DocDao;
import gri.engine.model.dao.ProcessDao;
import gri.engine.model.dao.StatsDao;
import gri.engine.process.KafkaPipeConsumer;
import gri.driver.model.process.Container;
import gri.driver.model.process.Process;
import gri.driver.model.process.View;
import gri.engine.service.BlockService;
import gri.engine.service.CacheService;
import gri.engine.service.GriEngineNamingService;
import gri.engine.service.GriEngineService;
import gri.engine.service.IndexService;
import gri.engine.service.ParseService;
import gri.engine.service.ProcessService;
import gri.engine.service.RedisContainerService;
import gri.engine.service.StatsService;
import gri.engine.util.Constant;
import gri.engine.util.DBHelper;
import gri.engine.util.DataSourceConnectTool;
import gri.engine.util.DateUtil;
import gri.engine.util.EITPSender;
import gri.engine.util.PathHelper;
import gri.engine.util.SQLParser;
import gri.engine.service.GriEngineDataService;

public class GriEngine {
	private static final Logger LOGGER = LoggerFactory.getLogger(GriEngine.class);

	public static GriEngine getGriEngine() {
		if (griEngine == null)
			griEngine = new GriEngine();
		return griEngine;
	}

	private GriEngine() {
	}

	private static GriEngine griEngine; // THIS

	private DataSyncTaskManager dataSyncTaskManager;// 管理所有段数据的定时/周期同步

	private CacheManager cacheManager;
	private ServerEngine serverEngine; // EITP消息路由
	private GriEngineNamingService griEngineNamingService;// 通信权限
	private Engine eitp_engine; // EITP消息引擎
	private StringBuilder serverHandlerKey;// 服务器句柄标识，通信隧道

	private Map<Integer, String> pMap = new HashMap<Integer, String>();

	public void start() throws IOException {
		// EITP消息路由
		this.griEngineNamingService = new GriEngineNamingService();// 权限
		this.serverEngine = new ServerEngine(this.griEngineNamingService);
		this.serverEngine.setPort(Constant.eitpServerPort);
		this.serverEngine.start();
		// 初始化所有缓存数据（不存在时创建）
		this.cacheManager = new CacheManager();
		this.cacheManager.initialAllCache();
		// 启动数据同步定时任务
		this.dataSyncTaskManager = new DataSyncTaskManager();
		this.dataSyncTaskManager.init();
		this.dataSyncTaskManager.addAllTask();
		this.dataSyncTaskManager.addAllTask2();
		this.dataSyncTaskManager.addAllTask3();
		// 在调试模式下开启所有拓扑
		if(Constant.run_process) new ProcessService().initAllTopology();
		
		// 启动通信引擎
		this.eitp_engine = new Engine();
		eitp_engine.start();
		this.serverHandlerKey = new StringBuilder();// 挂接指令处理器
		Authentication authentication = new Authentication(Authentication.AT_BASIC,
				new String[] { Constant.local_eitp_user, Constant.local_eitp_password });
		// 建立连接
		int sc = eitp_engine.connect(new EITPLocation("EITP://localhost:" + String.valueOf(Constant.eitpServerPort)),
				ServerHandlerTypes.DUPLEX, serverHandlerKey, authentication);
		if (sc != 0) {
			LOGGER.info("连接失败：[code:{}, meesage:{}]", sc, StatusCodeUtil.getMessage(sc));
			eitp_engine.stop();
		} else {
			eitp_engine.registerOrderHandler(DriverConstant.shutEngine, new IOrderHandler(){
				@Override
				public void handleOrder(Order order,IServerHandler server) {
					shutdown();
				}
			});
			eitp_engine.registerOrderHandler("close", new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					if (order.from.equals("0")) {
						server.close();
					}
				}
			});
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_TEST, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					SendOrder(order.from, DriverConstant.OPERATE_TEST + "_RESULT", "ok, I am received");// 发送响应结果
				}
			});

			/**************************************************************/
			/************ ↓↓↓↓↓↓↓↓↓↓↓****User *****↓↓↓↓↓↓↓↓↓↓↓ ************/
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_ADD_USER, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					if (!order.from.equals("root")) {
						SendOrder(order.from, DriverConstant.OPERATE_ADD_USER + "_RESULT", null);// 发送响应结果
						return;
					}
					User user = (User) order.data;
					User newUser = new DocDao().addUser(user);
					SendOrder(order.from, DriverConstant.OPERATE_ADD_USER + "_RESULT", newUser);// 发送响应结果
				}
			});

			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_DELETE_USER, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					if (!order.from.equals("root")) {
						SendOrder(order.from, DriverConstant.OPERATE_DELETE_USER + "_RESULT", false);// 发送响应结果
						return;
					}
					User user = (User) order.data;
					boolean success = new DocDao().delUser(user);
					SendOrder(order.from, DriverConstant.OPERATE_DELETE_USER + "_RESULT", success);// 发送响应结果
				}
			});
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_GET_ALL_USER, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					List<User> users =new DocDao().getAllUser();
					SendOrder(order.from, DriverConstant.OPERATE_GET_ALL_USER + "_RESULT", users);// 发送响应结果
				}
			});

			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_UPDATE_USER, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					if (!order.from.equals("root")) {
						SendOrder(order.from, DriverConstant.OPERATE_UPDATE_USER + "_RESULT", false);// 发送响应结果
						return;
					}
					User user = (User) order.data;
					boolean success = new DocDao().updateUser(user);
					SendOrder(order.from, DriverConstant.OPERATE_UPDATE_USER + "_RESULT", success);// 发送响应结果
				}
			});

			/**************************************************************/
			/*********** ↓↓↓↓↓↓↓↓↓↓↓****GriDoc *****↓↓↓↓↓↓↓↓↓↓↓ ***********/

			eitp_engine.registerOrderHandler(DriverConstant.Update_Sync, new IOrderHandler() {
				@Override
				public void handleOrder(Order order,IServerHandler server) {
					boolean result=new UpdateSyncConfigHelper().updateSyncTaskManager();
				    SendOrder(order.from,DriverConstant.Update_Sync + "_RESULT", result);
				}
			});
			
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_ADD_GRIDOC, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					DocDao docDao=new DocDao();
					int userId = docDao.getIdByAccount(order.from);
					GriDoc gridoc = (GriDoc) order.data;
					GriDoc newGridoc =null;
					if(userId != -1) newGridoc=docDao.addGriDoc(gridoc, userId);
					SendOrder(order.from, DriverConstant.OPERATE_ADD_GRIDOC + "_RESULT", newGridoc);// 发送响应结果
				}
			});

			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_DELETE_GRIDOC, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {							
					boolean success = false;
					DocDao docDao=new DocDao();
					int userId = docDao.getIdByAccount(order.from);
					GriDoc gridoc = (GriDoc) order.data;
					if(userId != -1) success = docDao.delGriDoc(gridoc, userId);
					SendOrder(order.from, DriverConstant.OPERATE_DELETE_GRIDOC + "_RESULT", success);// 发送响应结果
				}
			});

			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_GET_ALL_GRIDOC, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					int userId = new DocDao().getIdByAccount(order.from);
					List<GriDoc> gridocs = new DocDao().getAllGriDoc(userId);
					SendOrder(order.from, DriverConstant.OPERATE_GET_ALL_GRIDOC + "_RESULT", gridocs);// 发送响应结果
				}
			});

			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_GET_CHILDREN_OF_GRIDOC, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					GriDoc gridoc = (GriDoc) order.data;
					List<GriElement> children = new DocDao().getChildrenOfGriDoc(gridoc);
					SendOrder(order.from, DriverConstant.OPERATE_GET_CHILDREN_OF_GRIDOC + "_RESULT", children);// 发送响应结果
				}
			});

			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_UPDATE_GRIDOC, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					boolean success = false;
					DocDao docDao=new DocDao();
					GriDoc gridoc = (GriDoc) order.data;
					int userId = docDao.getIdByAccount(order.from);
					if(userId != -1) success = docDao.updateGriDoc(gridoc, userId);
					SendOrder(order.from, DriverConstant.OPERATE_UPDATE_GRIDOC + "_RESULT", success);// 发送响应结果
				}
			});

			/***************************************************************/
			/*********** ↓↓↓↓↓↓↓↓↓↓↓****Section *****↓↓↓↓↓↓↓↓↓↓↓ ***********/
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_GET_CHILDREN_OF_SECTION, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Section section = (Section) order.data;
					List<GriElement> children = new DocDao().getChildrenOfSection(section);
					SendOrder(order.from, DriverConstant.OPERATE_GET_CHILDREN_OF_SECTION + "_RESULT", children);// 发送响应结果
				}
			});

			/******************************************************************/
			/*********** ↓↓↓↓↓↓↓↓↓↓↓****GriElement *****↓↓↓↓↓↓↓↓↓↓↓ ***********/

			// 增加格文档元素
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_ADD_GRI_ELEMENT, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					DocDao docDao=new DocDao();
					GriElementData3 griElementData3 = (GriElementData3) order.data;
					int userId = docDao.getIdByAccount(order.from);//order.from应该指代用户账号
					GriElement result = null;
					if(userId!= -1) result = new DocDao().addGriElement(griElementData3,userId);
					SendOrder(order.from, DriverConstant.OPERATE_ADD_GRI_ELEMENT + "_RESULT", result);// 发送响应结果
					return;
				}
			});
			
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_ADD_GRI_ELEMENT2, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					DocDao docDao=new DocDao();
					GriElementData4 griElementData4 = (GriElementData4) order.data;
					int userId = docDao.getIdByAccount(order.from);//order.from应该指代用户账号
					GriElement result = null;
					if(userId!= -1) result = new DocDao().addGriElement(griElementData4,userId,griElementData4.isAddTableName());
					SendOrder(order.from, DriverConstant.OPERATE_ADD_GRI_ELEMENT2 + "_RESULT", result);// 发送响应结果
					return;
				}
			});


			// 删除格文档元素
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_DELETE_GRI_ELEMENT, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					DocDao docDao=new DocDao();
					GriElementData2 griElementData2 = (GriElementData2) order.data;
					int userId = docDao.getIdByAccount(order.from);
					boolean success = false;
					if(userId!= -1) success = new DocDao().delGriElement(griElementData2,userId);
					SendOrder(order.from, DriverConstant.OPERATE_DELETE_GRI_ELEMENT + "_RESULT", success);// 发送响应结果
					return;
				}
			});

			// 清理删除未引用格文档元素
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_DELETE_UNREFERENCE_GRI_ELEMENT,
					new IOrderHandler() {
						@Override
						public void handleOrder(Order order, IServerHandler server) {
							new DocDao().removeUnreferenceGriElement();
							SendOrder(order.from, DriverConstant.OPERATE_DELETE_UNREFERENCE_GRI_ELEMENT + "_RESULT",
									"收到清理未引用格文档元素指令，后台处理中...");// 发送响应结果
						}
					});

			// 移除格文档元素
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_REMOVE_GRI_ELEMENT, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					DocDao docDao=new DocDao();
					GriElementData2 griElementData2 = (GriElementData2) order.data;
					int userId = docDao.getIdByAccount(order.from);
					boolean success = false;
					if(userId!= -1) success = new DocDao().remGriElement(griElementData2,userId);
				SendOrder(order.from, DriverConstant.OPERATE_REMOVE_GRI_ELEMENT + "_RESULT", success);// 发送响应结果
				}
			});

			// 查询格文档元素下的所有段
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_GET_ALL_PARAGRAPH_OF_GRI_ELEMTNT,
					new IOrderHandler() {
						@Override
						public void handleOrder(Order order, IServerHandler server) {
							GriElement griElement = (GriElement) order.data;
							List<Paragraph> paras = new DocDao().getParagraphOfGriElement(griElement);
							SendOrder(order.from, DriverConstant.OPERATE_GET_ALL_PARAGRAPH_OF_GRI_ELEMTNT + "_RESULT",
									paras);// 发送响应结果
						}
					});

			// 修改格文档元素
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_UPDATE_GRI_ELEMTNT, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					DocDao docDao=new DocDao();
					GriElementData2 griElementData2 = (GriElementData2) order.data;
					int userId = docDao.getIdByAccount(order.from);
					boolean success = false;
					if(userId!= -1) success = new DocDao().updateGriElement(griElementData2,userId);
					SendOrder(order.from, DriverConstant.OPERATE_UPDATE_GRI_ELEMTNT + "_RESULT", success);// 发送响应结果
				}
			});

			// 段数据字节大小
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_READ_PARAGRAPH_SIZE, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer paragraphID = (Integer) order.data;
					int size = new GriEngineService().readParagraphSize(paragraphID);
					SendOrder(order.from, DriverConstant.OPERATE_READ_PARAGRAPH_SIZE + "_RESULT", size);// 发送响应结果
				}
			});

			// 读取段数据
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_READ_PARAGRAPH, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					ParagraphDataReadRequest request = (ParagraphDataReadRequest) order.data;
					ParagraphDataReadResponse response = new GriEngineService().readParagraphByByte(request);
					SendOrder(order.from, DriverConstant.OPERATE_READ_PARAGRAPH + "_RESULT", response);// 发送响应结果
				}
			});

			// 写段数据
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_WRITE_PARAGRAPH, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					// TODO GriDoc权限
					ParagraphDataWriteRequest request = (ParagraphDataWriteRequest) order.data;
					Integer paragraphID = request.paragraphID;
					String cacheUUID = "";
					Object uuid = pMap.get(paragraphID);
					if (uuid == null) {
						String sql = "select * from paragraph where id = ?";
						Object[] obj = new Object[] { request.paragraphID };
						Connection conn=DBHelper.getConnection();
						PreparedStatement ps=null;
						try {
							ps = conn.prepareStatement(sql);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ResultSet rs = DBHelper.executeQuery(conn,ps,sql, obj);
						try {
							while (rs.next()) {
								cacheUUID = rs.getString("cache_file");
								pMap.put(paragraphID, cacheUUID);// 缓存ID-CacheUUID
							}
						} catch (SQLException e) {
							e.printStackTrace();
						} finally {
							DBHelper.free(rs,ps,conn);
						}
					} else
						cacheUUID = (String) uuid;
					if (cacheUUID.equals("")) {
						SendOrder(order.from, DriverConstant.OPERATE_WRITE_PARAGRAPH + "_RESULT", false);// 发送响应结果
						return;
					}
					CacheService cacheService = new CacheService(cacheUUID, request.paragraphID);
					cacheService.addListenner(new DataChangeListener());// 监听用户对缓存数据的更改
					boolean success = cacheService.writeCache(request.bytes, request.append);// 写缓存数据
					SendOrder(order.from, DriverConstant.OPERATE_WRITE_PARAGRAPH + "_RESULT", success);// 发送响应结果
				}
			});

			// 读取段预览数据
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_READ_PARAGRAPH_PREVIEW, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer paragraphID = (Integer) order.data;
					String text = new GriEngineService().readParagraphPreview(paragraphID);
					SendOrder(order.from, DriverConstant.OPERATE_READ_PARAGRAPH_PREVIEW + "_RESULT", text);// 发送响应结果
				}
			});

			// 刷新段预览数据,已过时，现在直接在同步里刷新段预览
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_REFRESH_PARAGRAPH_PREVIEW, new IOrderHandler() {
				String cacheUUID = "";
				String dataSourceType = "";
				String dataSourcePath = "";
				String extension = "";

				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer paragraphID = (Integer) order.data;

					String sql = "select * from paragraph where id = ?";
					Object[] obj = new Object[] { paragraphID };
					Connection conn=DBHelper.getConnection();
					PreparedStatement ps=null;
					try {
						ps = conn.prepareStatement(sql);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ResultSet rs = DBHelper.executeQuery(conn,ps,sql, obj);
					try {
						while (rs.next()) {
							cacheUUID = rs.getString("cache");
							pMap.put(paragraphID, cacheUUID);// 缓存ID-CacheUUID
							dataSourceType = rs.getString("data_source_type");
							dataSourcePath = rs.getString("data_source_path");

							if (dataSourceType.equals(DriverConstant.DataSourceType_File))
								extension = PathHelper.getExtension(dataSourcePath);
							else if (dataSourceType.equals(DriverConstant.DataSourceType_Database))
								extension = "db";
							else if (dataSourceType.equals(DriverConstant.DataSourceType_WebService))
								extension = "html";
							else if (dataSourceType.equals(DriverConstant.DataSourceType_ParagraphEngine)) {
								extension = "";
								String host = dataSourcePath.split("###")[0];
								String user = dataSourcePath.split("###")[1];
								String password = dataSourcePath.split("###")[2];
								// String name = dataSourcePath.split("###")[3];
								String id = dataSourcePath.split("###")[4];
								Object result = EITPSender.sendRequest(host, user, password, "publisher",
										"get paragraph extension name", id);
								if (result != null) {
									extension = (String) result;
								}
							}
							else if (dataSourceType.equals(DriverConstant.DataSourceType_GriEngine)) {
								extension = "txt";
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						DBHelper.free(rs,ps,conn);
					}

					new Thread() {
						public void run() {
							ParseService ps = new ParseService(cacheUUID, extension);
							ps.parse();// 更新预览文件

							IndexService is = new IndexService();
							is.delelteIndex(cacheUUID);
							is.createIndex(cacheUUID); // 更新索引
						};
					}.start();

					SendOrder(order.from, DriverConstant.OPERATE_REFRESH_PARAGRAPH_PREVIEW + "_RESULT",
							"收到刷新Paragraph" + paragraphID + "预览文件指令，后台处理中...");// 发送响应结果
				}
			});

			// 强制同步数据（缓存-数据源）
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_SYNC_DATA, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer paragraphId = (Integer) order.data;
					boolean result = new GriEngineService().dataSync(paragraphId);
					SendOrder(order.from, DriverConstant.OPERATE_SYNC_DATA + "_RESULT",result);// 发送响应结果
				}
			});

			// 验证CronExpression时间表达式格式是否合法
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_CHECK_CRONEXPRESSION, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					String cronExpressionStr = (String) order.data;
					boolean isValid = false;
					try {
						new CronExpression(cronExpressionStr);
						isValid = true;
					} catch (ParseException e) {
						e.printStackTrace();
					}
					SendOrder(order.from, DriverConstant.OPERATE_CHECK_CRONEXPRESSION + "_RESULT", isValid);// 发送响应结果
				}
			});

			// 测试数据源连接
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_TEST_DATA_SOURCE_CONNECT, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					String dataSourceConnectPath = (String) order.data;
					boolean succeed = new DataSourceConnectTool().canConnect(dataSourceConnectPath);
					SendOrder(order.from, DriverConstant.OPERATE_CHECK_CRONEXPRESSION + "_RESULT", succeed);// 发送响应结果
				}
			});

			// 查询指定格文档访问权限
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_QUERY_ACCESS_PERMISSION, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					List<AccessPermission> result = new ArrayList<AccessPermission>();
					GriDoc gridoc = (GriDoc) order.data;
					String sql = "select * from gridoc_user left join user on gridoc_user.user = user.id where gridoc = ?";
					Object[] obj = new Object[] { gridoc.getId() };
					Connection conn=DBHelper.getConnection();
					PreparedStatement ps=null;
					try {
						ps = conn.prepareStatement(sql);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ResultSet rs = DBHelper.executeQuery(conn,ps,sql, obj);
					try {
						while (rs.next()) {
							result.add(new AccessPermission(rs.getInt("gridoc"), rs.getInt("user"),
									rs.getString("user.account"), rs.getString("read_only").equals("Y")));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						DBHelper.free(rs,ps,conn);
					}
					SendOrder(order.from, DriverConstant.OPERATE_CHECK_CRONEXPRESSION + "_RESULT", result);// 发送响应结果
				}
			});

			// 删除指定格文档访问权限
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_DELETE_ACCESS_PERMISSION, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					AccessPermission accessPermission = (AccessPermission) order.data;
					DocDao docDao=new DocDao();
					boolean readOnly = docDao.docReadOnly(accessPermission.getGridocID(),
							docDao.getIdByAccount(order.from));
					if (readOnly) {
						LOGGER.info("格文档为只读：[user:{}, gridoc id:{}]", order.from, accessPermission.getGridocID());
						SendOrder(order.from, DriverConstant.OPERATE_DELETE_ACCESS_PERMISSION + "_RESULT", false);// 发送响应结果
						return;
					}
					String sql = "delete from gridoc_user where gridoc = ? and user = ?";
					Object[] obj = new Object[] { accessPermission.getGridocID(), accessPermission.getUserID() };
					int size = DBHelper.executeNonQuery(sql, obj);
					SendOrder(order.from, DriverConstant.OPERATE_DELETE_ACCESS_PERMISSION + "_RESULT", size > 0);// 发送响应结果
				}
			});

			// 修改指定格文档访问权限
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_UPDATE_ACCESS_PERMISSION, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					AccessPermission accessPermission = (AccessPermission) order.data;
					DocDao docDao=new DocDao();
					boolean readOnly = docDao.docReadOnly(accessPermission.getGridocID(),
							docDao.getIdByAccount(order.from));
					if (readOnly) {
						LOGGER.info("格文档为只读：[user:{}, gridoc id:{}]", order.from, accessPermission.getGridocID());
						SendOrder(order.from, DriverConstant.OPERATE_UPDATE_ACCESS_PERMISSION + "_RESULT", false);// 发送响应结果
						return;
					}
					String sql = "update gridoc_user set read_only = ? where gridoc = ? and user = ?";
					Object[] obj = new Object[] { accessPermission.isReadOnly() ? "Y" : "N",
							accessPermission.getGridocID(), accessPermission.getUserID() };
					int size = DBHelper.executeNonQuery(sql, obj);
					SendOrder(order.from, DriverConstant.OPERATE_UPDATE_ACCESS_PERMISSION + "_RESULT", size > 0);// 发送响应结果
				}
			});
			// 添加指定格文档访问权限
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_ADD_ACCESS_PERMISSION, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					AccessPermission accessPermission = (AccessPermission) order.data;
					DocDao docDao=new DocDao();
					boolean readOnly = docDao.docReadOnly(accessPermission.getGridocID(),
							docDao.getIdByAccount(order.from));
					if (readOnly) {
						LOGGER.info("格文档为只读：[user:{}, gridoc id:{}]", order.from, accessPermission.getGridocID());
						SendOrder(order.from, DriverConstant.OPERATE_ADD_ACCESS_PERMISSION + "_RESULT", false);// 发送响应结果
						return;
					}
					String sql = "insert into gridoc_user(gridoc, user, read_only) values (?,?,?)";
					Object[] obj = new Object[] { accessPermission.getGridocID(), accessPermission.getUserID(),
							accessPermission.isReadOnly() ? "Y" : "N" };
					int size = DBHelper.executeNonQuery(sql, obj);
					SendOrder(order.from, DriverConstant.OPERATE_ADD_ACCESS_PERMISSION + "_RESULT", size > 0);// 发送响应结果
				}
			});

			// 格文档 按属性查询
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_QUERY_GRIELEMENT, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					DocDao docDao= new DocDao();
					String account = order.from;
					String attr_value = (String) order.data;
					int spitIndex = attr_value.indexOf("###");
					String attr = attr_value.substring(0, spitIndex);
					String value = attr_value.substring(spitIndex + 3, attr_value.length());

					List<GriElement> result = new ArrayList<GriElement>();
					
					int userId=docDao.getIdByAccount(account);
					if(userId!= -1){											
						if (attr.equals("名称")) {
							for (GriDoc gridoc : docDao.getAllGriDoc(userId)) {
								if (gridoc.getName().toLowerCase().contains(value.toLowerCase()))
									result.add(gridoc);
								for (GriElement child : docDao.getChildrenOfGriDoc(gridoc))
									queryByName(child, value, result);
							}
						} else if (attr.equals("关键词")) {
							for (GriDoc gridoc : docDao.getAllGriDoc(userId)) {
								queryByKeywords(gridoc, value, result);
							}
						} else if (attr.equals("描述")) {
							for (GriDoc gridoc : docDao.getAllGriDoc(userId)) {
								queryByDescription(gridoc, value, result);
							}
						} else if (attr.equals("全文检索")) {
							IndexService indexService = new IndexService();
							List<String> cacheUUIDs = indexService.search(value);
							for (GriDoc gridoc : docDao.getAllGriDoc(userId)) {
								queryByCacheUUID(gridoc, cacheUUIDs, result);
							}
						}
					}
					SendOrder(order.from, DriverConstant.OPERATE_QUERY_GRIELEMENT + "_RESULT", result);// 发送响应结果
				}
			});

			// 数据源更改通知受理
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_DATA_CHANGE, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					DataChangeMessage dataChangeMessage = (DataChangeMessage) order.data;
					String dataSourceType = dataChangeMessage.getDataSourceType();
					String dataSourcePath = dataChangeMessage.getDataSourcePath();
					LOGGER.info(dataSourceType);
					LOGGER.info(dataSourcePath);
					new GriEngineService().handleDataChange(dataSourceType,dataSourcePath);
					SendOrder(order.from, DriverConstant.OPERATE_DATA_CHANGE + "_RESULT", "数格引擎已对数据源数据更改通知进行处理");// 发送响应结果
				}
			});
			
			// 统计计算
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_STATS_CALCULATE, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					StatsConfData2 statsConfData2 = (StatsConfData2) order.data;
					String text = getTextByParagraphID(statsConfData2.getParagraphID());				
					StatsService statsService = new StatsService(text,statsConfData2.getStatsConf());
					StatsResult statsResult=statsService.calculate();
					
					SendOrder(order.from, DriverConstant.OPERATE_STATS_CALCULATE + "_RESULT", statsResult);// 发送响应结果
				}
			});
			
			// 列出段数据中的列
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_COL_LIST, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer paragraphID = (Integer) order.data;
					String text = getTextByParagraphID(paragraphID);
					text=(String) text.subSequence(text.indexOf("["), text.length()-1);
					JSONArray array = JSONArray.fromObject(text);
					String[] ls; 
					if(array.size()>0){
						JSONObject obj= array.getJSONObject(0);
						ls = new String[obj.size()];
						Iterator iter = obj.keys();
						int i =0;
						while(iter.hasNext()){
							ls[i]=(String) iter.next();
							i++;
						}
					}
					else ls = new String[0];
					
					SendOrder(order.from, DriverConstant.OPERATE_COL_LIST + "_RESULT", ls);// 发送响应结果
				}
			});
			
			//列出处理器
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_LIST_STATS, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer paragraphId = (Integer) order.data;
					List<StatsConfDto1> ls = new StatsDao().listStatsConf(paragraphId);		
					SendOrder(order.from, DriverConstant.OPERATE_LIST_STATS + "_RESULT", ls);// 发送响应结果
				}
			});
			
			//新建处理器
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_ADD_STATS, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					StatsConfDto1 statsConfDto1 = (StatsConfDto1) order.data;
					boolean result= new StatsDao().addStatsConf(statsConfDto1);				
					SendOrder(order.from, DriverConstant.OPERATE_ADD_STATS + "_RESULT", result);// 发送响应结果
				}
			});
			
			//修改处理器
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_UPDATE_STATS, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					StatsConfDto1 statsConfDto1 = (StatsConfDto1) order.data;
					boolean result= new StatsDao().updateStatsConf(statsConfDto1);				
					SendOrder(order.from, DriverConstant.OPERATE_UPDATE_STATS + "_RESULT", result);// 发送响应结果
				}
			});
			
			//删除处理器
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_DEL_STATS, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer processId = (Integer) order.data;
					boolean result= new StatsDao().delStatsConf(processId);				
					SendOrder(order.from, DriverConstant.OPERATE_DEL_STATS + "_RESULT", result);// 发送响应结果
				}
			});
			
			// 根据id查找段
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_GET_PARAGRAPH_BY_ID, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer paragraphID = (Integer) order.data;
					Paragraph paragraph = new ProcessDao().getParagraphById(paragraphID);
					
					SendOrder(order.from, DriverConstant.OPERATE_GET_PARAGRAPH_BY_ID + "_RESULT", paragraph);// 发送响应结果
				}
			});
			
			// 根据name查找段
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_GET_PARAGRAPH_BY_NAME, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					String name = (String) order.data;
					gri.driver.model.process.Paragraph paragraph = new ProcessDao().getParagraphByName(name);
					
					SendOrder(order.from, DriverConstant.OPERATE_GET_PARAGRAPH_BY_NAME + "_RESULT", paragraph);// 发送响应结果
				}
			});
			
			// 当数据类型是数格引擎时获取最终数据源的类型，用于生成预览
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_GET_REAL_PARAGRAPH, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					String dataSourcePath = (String) order.data;
					gri.driver.model.process.Paragraph result = new GriEngineDataService(dataSourcePath).realParagraph();
					
					SendOrder(order.from, DriverConstant.OPERATE_GET_REAL_PARAGRAPH + "_RESULT", result);// 发送响应结果
				}
			});
			
			//列出处理器
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_LIST_PROCESSOR, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					List<Processor> ls = new ProcessDao().listProcessor();		
					SendOrder(order.from, DriverConstant.OPERATE_LIST_PROCESSOR + "_RESULT", ls);// 发送响应结果
				}
			});
			
			//新建处理器
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_ADD_PROCESSOR, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Processor processor = (Processor) order.data;
					boolean result= new ProcessDao().addProcessor(processor);
					SendOrder(order.from, DriverConstant.OPERATE_ADD_PROCESSOR + "_RESULT", result);// 发送响应结果
				}
			});
			
			//修改处理器
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_UPDATE_PROCESSOR, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Processor processor = (Processor) order.data;
					boolean result= new ProcessDao().updateProcessor(processor);				
					SendOrder(order.from, DriverConstant.OPERATE_UPDATE_PROCESSOR + "_RESULT", result);// 发送响应结果
				}
			});
			
			//删除处理器
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_DEL_PROCESSOR, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer id = (Integer) order.data;
					boolean result= new ProcessDao().delProcessor(id);				
					SendOrder(order.from, DriverConstant.OPERATE_DEL_PROCESSOR + "_RESULT", result);// 发送响应结果
				}
			});
			
			//列出容器
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_LIST_CONTAINER, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					String account = (String) order.data;
					List<Container> ls = new ProcessDao().listContainer(account);		
					SendOrder(order.from, DriverConstant.OPERATE_LIST_CONTAINER + "_RESULT", ls);// 发送响应结果
				}
			});
			
			//根据处理器列出容器
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_LIST_CONTAINER_BY_PROCESSOR, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Object[] objs = (Object[]) order.data;
					List<Container> ls = new ProcessDao().listContianerByProcessor((String)objs[0],(Integer)objs[1]);		
					SendOrder(order.from, DriverConstant.OPERATE_LIST_CONTAINER_BY_PROCESSOR + "_RESULT", ls);// 发送响应结果
				}
			});
			
			//新建容器
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_ADD_CONTAINER, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Container container = (Container) order.data;
					boolean result= new ProcessDao().addContainer(container);				
					SendOrder(order.from, DriverConstant.OPERATE_ADD_CONTAINER + "_RESULT", result);// 发送响应结果
				}
			});
			
			//修改容器
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_UPDATE_CONTAINER, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Container container = (Container) order.data;
					boolean result= new ProcessDao().updateContainer(container);				
					SendOrder(order.from, DriverConstant.OPERATE_UPDATE_CONTAINER + "_RESULT", result);// 发送响应结果
				}
			});
			
			//删除容器
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_DEL_CONTAINER, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer id = (Integer) order.data;
					boolean result= new ProcessDao().delContainer(id);				
					SendOrder(order.from, DriverConstant.OPERATE_DEL_CONTAINER + "_RESULT", result);// 发送响应结果
				}
			});
			
			//列出视图
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_LIST_VIEW, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					List<View> ls = new ProcessDao().listView();		
					SendOrder(order.from, DriverConstant.OPERATE_LIST_VIEW + "_RESULT", ls);// 发送响应结果
				}
			});
			
			//根据处理器列出视图
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_LIST_VIEW_BY_PROCESSOR, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer processorId = (Integer) order.data;
					List<View> ls = new ProcessDao().listViewByProcessor(processorId);		
					SendOrder(order.from, DriverConstant.OPERATE_LIST_VIEW_BY_PROCESSOR + "_RESULT", ls);// 发送响应结果
				}
			});
			
			//根据容器列出视图
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_LIST_VIEWS_BY_CONTAINER, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer containerId = (Integer) order.data;
					List<View> ls = new GriEngineService().listView(containerId);		
					SendOrder(order.from, DriverConstant.OPERATE_LIST_VIEWS_BY_CONTAINER + "_RESULT", ls);// 发送响应结果
				}
			});
			
			//新建视图
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_ADD_VIEW, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					View view = (View) order.data;
					boolean result= new ProcessDao().addView(view);				
					SendOrder(order.from, DriverConstant.OPERATE_ADD_VIEW + "_RESULT", result);// 发送响应结果
				}
			});
			
			//修改视图
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_UPDATE_VIEW, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					View view = (View) order.data;
					boolean result= new ProcessDao().updateView(view);				
					SendOrder(order.from, DriverConstant.OPERATE_UPDATE_VIEW + "_RESULT", result);// 发送响应结果
				}
			});
			
			//删除视图
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_DEL_VIEW, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer id = (Integer) order.data;
					boolean result= new ProcessDao().delView(id);				
					SendOrder(order.from, DriverConstant.OPERATE_DEL_VIEW + "_RESULT", result);// 发送响应结果
				}
			});
			
			//列出处理
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_LIST_PROCESS, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer paragraphId = (Integer) order.data;
					List<Process> ls = new ProcessDao().listProcess(paragraphId);		
					SendOrder(order.from, DriverConstant.OPERATE_LIST_PROCESS + "_RESULT", ls);// 发送响应结果
				}
			});
			
			//根据名字读视图
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_GET_VIEW_BY_NAME, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					String[] viewNames = (String[]) order.data;
					View view = new GriEngineService().getView(viewNames[0], viewNames[1]);		
					SendOrder(order.from, DriverConstant.OPERATE_GET_VIEW_BY_NAME + "_RESULT", view);// 发送响应结果
				}
			});
			
			//新建处理
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_ADD_PROCESS, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Process process = (Process) order.data;
					ProcessDao processDao= new ProcessDao();
					boolean result= processDao.addProcess(process);	
					//!!!暂时先这么写，分布式不行的,要重启topology
					if(result){
						Container container = processDao.getContianerById(process.getContainerId());
						if(container != null){
							List<Integer> ls= processDao.listParagraphIdByContianerName(container.getName());
							Consumer<String,String> consumer = KafkaPipeConsumer.getConsumer(container.getName());
							List<String> lss= new ArrayList<String>();
							for(int i = 0 ;i<ls.size();i++) lss.add(Integer.toString(ls.get(i)));
							consumer.subscribe(lss);
						}
					}
						
					SendOrder(order.from, DriverConstant.OPERATE_ADD_PROCESS + "_RESULT", result);// 发送响应结果
				}
			});
			
			//修改处理
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_UPDATE_PROCESS, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Process process = (Process) order.data;
					boolean result= new ProcessDao().updateProcess(process);				
					SendOrder(order.from, DriverConstant.OPERATE_UPDATE_PROCESS + "_RESULT", result);// 发送响应结果
				}
			});
			
			//删除处理
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_DEL_PROCESS, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer id = (Integer) order.data;
					boolean result= new ProcessDao().delProcess(id);				
					SendOrder(order.from, DriverConstant.OPERATE_DEL_PROCESS + "_RESULT", result);// 发送响应结果
				}
			});
			
			// 查看视图数据
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_READ_VIEW, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					String[] viewNames = (String[]) order.data;
					Object result = new GriEngineService().readView(viewNames[0], viewNames[1]);
					SendOrder(order.from, DriverConstant.OPERATE_READ_VIEW + "_RESULT", result);// 发送响应结果
				}
			});
			
			// 设置所属区块链
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_SET_BLOCK_NAME, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Object[] params = (Object[]) order.data;
					new BlockService().setBlockName((Integer)params[0], (String)params[1]);
					SendOrder(order.from, DriverConstant.OPERATE_SET_BLOCK_NAME + "_RESULT", null);// 发送响应结果
				}
			});
			
			// 读取所属区块链
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_GET_BLOCK_NAME, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer paragraphId = (Integer) order.data;
					Object result = new BlockService().getBlockName(paragraphId);
					SendOrder(order.from, DriverConstant.OPERATE_GET_BLOCK_NAME + "_RESULT", result);// 发送响应结果
				}
			});
			
			// 读取区块链内容
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_READ_BLOCK, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					String blockNames = (String) order.data;
					Object result = new BlockService().readBlock(blockNames);
					SendOrder(order.from, DriverConstant.OPERATE_READ_BLOCK + "_RESULT", result);// 发送响应结果
				}
			});
			
			// 分页读取数据
			eitp_engine.registerOrderHandler(DriverConstant.OPERATE_READ_DB_PAGE, new IOrderHandler() {
				@Override
				public void handleOrder(Order order, IServerHandler server) {
					Integer[] pages = (Integer[]) order.data;
					Object result = new GriEngineService().readDbPage(pages[0],pages[1],pages[2]);
					SendOrder(order.from, DriverConstant.OPERATE_READ_DB_PAGE + "_RESULT", result);// 发送响应结果
				}
			});
			
			System.out.println("started listening service<<<<<<<<<<<<");
			
			MessageInitiator init=new MessageInitiator();
			MessageEngine message=(MessageEngine)init.InitMessageEngine("Engine Started");
			SendHelper.send("MessageEngine", message);//向服務器發送引擎已啟動消息
			
		} // 结束指令监听

	}// end start()

	// 全文检索
	private void queryByCacheUUID(GriElement root, List<String> cacheUUIDs, List<GriElement> results) {
		DocDao docDao = new DocDao();
		if (root instanceof GriDoc)
			for (GriElement child : docDao.getChildrenOfGriDoc((GriDoc) root))
				queryByCacheUUID(child, cacheUUIDs, results);
		else if (root instanceof Section)
			for (GriElement child : docDao.getChildrenOfSection((Section) root))
				queryByCacheUUID(child, cacheUUIDs, results);
		else if (root instanceof Paragraph && cacheUUIDs.contains(((Paragraph) root).getCache()))
			results.add(root);
	}

	private void queryByName(GriElement root, String name, List<GriElement> results) {
		DocDao docDao = new DocDao();
		if (root.getName().toLowerCase().contains(name.toLowerCase()))
			results.add(root);
		if (root instanceof GriDoc)
			for (GriElement child : docDao.getChildrenOfGriDoc((GriDoc) root))
				queryByName(child, name, results);
		else if (root instanceof Section)
			for (GriElement child : docDao.getChildrenOfSection((Section) root))
				queryByName(child, name, results);
	}

	private void queryByKeywords(GriElement root, String keyword, List<GriElement> results) {
		DocDao docDao = new DocDao();
		if (root instanceof GriDoc)
			for (GriElement child : docDao.getChildrenOfGriDoc((GriDoc) root))
				queryByKeywords(child, keyword, results);
		else if (root instanceof Section)
			for (GriElement child : docDao.getChildrenOfSection((Section) root))
				queryByKeywords(child, keyword, results);
		else if (root instanceof Paragraph
				&& ((Paragraph) root).getKeywords().toLowerCase().contains(keyword.toLowerCase()))
			results.add(root);
	}

	private void queryByDescription(GriElement root, String desciption, List<GriElement> results) {
		DocDao docDao = new DocDao();
		if (root instanceof GriDoc)
			for (GriElement child : docDao.getChildrenOfGriDoc((GriDoc) root))
				queryByKeywords(child, desciption, results);
		else if (root instanceof Section)
			for (GriElement child : docDao.getChildrenOfSection((Section) root))
				queryByDescription(child, desciption, results);
		else if (root instanceof Paragraph
				&& ((Paragraph) root).getDescription().toLowerCase().contains(desciption.toLowerCase()))
			results.add(root);
	}

	private String getTextByParagraphID(Integer paragraphID){
		String cacheUUID = "";
		Object uuid = pMap.get(paragraphID);
		if (uuid == null) {
			String sql = "select * from paragraph where id = ?";
			Object[] obj = new Object[] { paragraphID };
			Connection conn=DBHelper.getConnection();
			PreparedStatement ps=null;
			try {
				ps = conn.prepareStatement(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultSet rs = DBHelper.executeQuery(conn,ps,sql, obj);
			try {
				while (rs.next()) {
					cacheUUID = rs.getString("cache");
					pMap.put(paragraphID, cacheUUID);// 缓存ID-CacheUUID
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBHelper.free(rs,ps,conn);
			}
		} else {
			cacheUUID = (String) uuid;
		}
		
		String text;
		if(cacheUUID.equals("")) text = "[]";
		else text= new CacheService(cacheUUID).readParagraphPreview();
		return text;
	}
	
	// 有问题 from=null?
	private void SendOrder(String to, String operate, Object data) {
		System.out.println("serverHandlekey:"+serverHandlerKey.toString());
		eitp_engine.write(serverHandlerKey.toString(), new Order(to, null, operate, data));
	}

	public void shutdown() {
		if (this.dataSyncTaskManager != null)
			this.dataSyncTaskManager.shutdown();
		if (this.eitp_engine != null)
			this.eitp_engine.stop();
		System.exit(0);
	}

	public void shutdown(boolean wait) {
		if (this.dataSyncTaskManager != null)
			this.dataSyncTaskManager.shutdown(wait);
		if (this.eitp_engine != null)
			this.eitp_engine.stop();
	}

}
