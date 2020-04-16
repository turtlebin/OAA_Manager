package gri.engine.integrate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gir.engine.monitor.Message2;
import gir.engine.monitor.MessageInitiator;
import gir.engine.monitor.RowGetter;
import gir.engine.monitor.SendHelper;
import gir.engine.monitor.TimerHelper;
import gri.engine.reflect.MethodMapper;
import gri.engine.reflect.MethodReflection;
import gri.engine.util.Constant;
import gri.engine.util.DBHelper;
import gri.engine.util.DateUtil;

public class SyncHelper3 implements sync {

	public int process = 0;


	public boolean joinSync(String paragraphName) throws IOException {
		MessageInitiator init=new MessageInitiator();
		Message2 messageState=null;
		List<String> file = FileUtil.findFilebyName(Constant.propertiesFolder, paragraphName);
		if (file.size() > 0) {
			TimerHelper timerHelper = null;
			Connection conn=null;
			Connection sourceConn=null;
			DBDao2 dao=null;
			try {
//				IntegrateMain main = FileUtil.ReadMain(Constant.propertiesFolder + file.get(0));
				IntegrateMain main = FileUtil.ReadMainFromJson(Constant.propertiesFolder + file.get(0));

				DBHelper2 helper = new DBHelper2(main.getTableDest());
				Map<String, String> insertMap = main.getInsertMap();// insertMap.put(destColumn,
																	// dataSource+"@@@"+sourceColumn);
				String table = main.getTableDest().split("###")[6];
				String insertSql = getInsertSql(insertMap, table);
				synchronized (SyncHelper.class) {// 接下来这部分比较消耗内存，因此使用类锁进行同步限制
//					int i = 10;
//					while (i-- > 0) {
//						System.out.println(paragraphName + " : " + i);
//						try {
//							Thread.sleep(500);
//						} catch (InterruptedException ie) {
//							return false;
//						}
//					}
					
					Set<String> sourceSet=main.getList().get(0).keySet();
					String sourcePath=getSourcePath(sourceSet);
					String destPath=main.getTableDest();
					messageState=(Message2)init.InitMessage2("Paragraph2 Started",1,sourcePath,destPath,paragraphName);
					
					SendHelper.send("Message2", messageState);
					DBDao2.dataSources.clear();
					dao = new DBDao2(main.getNeededList());
					conn = helper.getConnection();//这个是目标数据库的连接
					PreparedStatement ps = conn.prepareStatement(insertSql);
					String dataSource=main.getNeededList().get(0).getDataSource();
					RowGetter getter=new RowGetter(dataSource,table,"Paragraph2");
					getter.start();
					Message2 messageProcess=(Message2)init.InitMessage2("Paragraph2 Running",2,sourcePath,destPath,paragraphName);
					timerHelper=new TimerHelper("Message2",messageProcess,getter,this);
					timerHelper.send();
					List<DataSource> dataSources = dao.getDataSources2();
					ResultSet rs = dataSources.get(0).getResult();
					insertToMysql(rs, conn, ps, insertMap,main.getTableDest());
					rs.close();
					ps.close();
					init.UpdateMessage2(messageProcess, "Paragraph2 Integrate Finished",100);
					SendHelper.send("Message2", messageProcess);
				}
				init.UpdateMessage2(messageState, "Paragraph2 Terminated Normally",0);
				SendHelper.send("Message2", messageState);
				UpdateSyncTime(paragraphName);
				timerHelper.getTimer().cancel();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				init.UpdateMessage2(messageState, "Paragraph2 Terminated Abnormally",0);
				SendHelper.send("Message2", messageState);
				SendHelper.sendError(paragraphName, e);
				return false;
			}finally {
				if(conn!=null) {
					try {
						if(!conn.isClosed())
						conn.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						SendHelper.sendError(paragraphName, e1);
					}
				}
				if(dao!=null)
				closeConnection(dao.getConnections());
			}
		}
		return false;
	}

	private boolean insertToMysql(ResultSet rs, Connection conn, PreparedStatement ps, Map<String, String> insertMap,String destPath) {
		try {
			String tableName=destPath.split("###")[6];
			String deleteSql="truncate table `"+tableName+"`";
			boolean deleteSuccess=conn.createStatement().execute(deleteSql);
			if(deleteSuccess) {
				System.out.println("delete Success");
			}
			else {
				System.out.println("delete failed");
			}
			Map<String,String> methodMap=null;
			MethodReflection methodReflect=new MethodReflection();
			conn.setAutoCommit(false);
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = 1;
			while (rs.next()) {
				int i = 1;
				for (Map.Entry<String, String> entry : insertMap.entrySet()) {
					String type = null;
					String colDestPath=destPath+"@@@"+entry.getKey();
					String colSourcePath=entry.getValue();
					String sourceColName = entry.getValue().split("@@@")[1];
					if ((type = rsmd.getColumnTypeName(i)).indexOf("TIME") == -1 && (type.indexOf("DATETIME") == -1)) {
						Object temp=rs.getObject(sourceColName);
						String key=colSourcePath+"!!!"+colDestPath;
						if((methodMap=MethodMapper.getMethodMapper()).containsKey(key)) {
							ps.setObject(i,methodReflect.load(methodMap.get(key).split("!!!")[0], methodMap.get(key).split("!!!")[1], temp));
						}
						else {
						ps.setObject(i, temp);
						}
					} else {// 对时间数据类型的特殊处理
						String time = rs.getString(sourceColName);
						if (time != null) {
							// time = time.split("\\.")[0];
							ps.setObject(i, time);
						} else {
							ps.setObject(i, null);
						}
					}
					i++;
				}
				i = 1;
				count++;
				process++;
				ps.addBatch();
				if (count >= 1000000) {
					ps.executeBatch();
					ps.clearBatch();
					count = 1;
					conn.commit();
				}
			}
			ps.executeBatch();
			ps.clearBatch();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private String getInsertSql(Map<String, String> insertMap, String table) {
		if (insertMap.size() == 0) {
			return null;
		}
		int columnCount = insertMap.size();
		StringBuilder insertSql = new StringBuilder("INSERT INTO `").append(table).append("`(");
		int a = 0;
		for (String key : insertMap.keySet()) {
			insertSql.append((a == columnCount - 1) ? ("`" + key + "`") : ("`" + key + "`,"));
			a++;
		}
		insertSql.append(") values(");
		for (int i = 0; i < columnCount; i++) {
			insertSql.append((i == columnCount - 1) ? "?" : "?,");
		}
		insertSql.append(")");
		String sql = insertSql.toString();
		return sql;
	}

	private void closeConnection(List<Connection> conns) {
		for (Connection conn : conns) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("查询连接已关闭");
		}
	}
	
	private String getSourcePath(Set<String> set) {
		if(set.size()<=0) {
			return null;
		}
		String s="[";
		for(String source:set) {
			s+=source;
			s+=",";
		}
		s=s.substring(0, s.length()-1);
		s+="]";
		return s;
	}

	private void UpdateSyncTime(String paragraphName) {
		String updateSql="update paragraph set last_sync_time=? where name=?";
		DBHelper.executeNonQuery(updateSql, DateUtil.getCurrentDate(),paragraphName);
	}
	
	public static void main(String[] args) {
		try {
			new SyncHelper3().joinSync("ta_cggl_planproject");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
