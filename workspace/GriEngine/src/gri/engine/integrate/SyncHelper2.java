package gri.engine.integrate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gir.engine.monitor.Message3;
import gir.engine.monitor.MessageInitiator;
import gir.engine.monitor.SendHelper;
import gir.engine.monitor.TimerHelper;
import gri.engine.dest.Column;
import gri.engine.util.Constant;
import gri.engine.util.DBHelper;
import gri.engine.util.DateUtil;

public class SyncHelper2 implements sync {

	public static int a = 1;
	private JoinHelper joinHelper;
	private String stage = null;

	public String getStage() {
		return stage;
	}

	public int getProcess() {
		if (this.stage.equalsIgnoreCase("joining")) {
			return joinHelper.getProcess();
		} else {
			return MultiSourceSchemaTransformation.getProcess();
		}
	}

	public boolean joinSync(String paragraphName) throws IOException {
		MessageInitiator init = new MessageInitiator();
		Message3 messageState = null;
		List<String> file = FileUtil.findFilebyName(Constant.propertiesFolder, paragraphName);
		if (file.size() > 0) {
			TimerHelper timerHelper = null;
			try {
//				IntegrateMain main = FileUtil.ReadMain(Constant.propertiesFolder + file.get(0));
				IntegrateMain main = FileUtil.ReadMainFromJson(Constant.propertiesFolder + file.get(0));
				List<DataJoin> dataJoinList = main.getDataJoinList();
				DBHelper2 helper = new DBHelper2(main.getTableDest());
				FileUtil.clearAllFiles(Constant.ObjectFolder, paragraphName);
				synchronized (SyncHelper.class) {// 接下来这部分比较消耗内存，因此使用类锁进行同步限制

					List<Map<String, Map<String, Column>>> sourceList = main.getList();
					String sourcePath = getSourcePath(sourceList);
					String destPath = main.getTableDest();

					messageState = (Message3) init.InitMessage3("Paragraph3 Started", 1, sourcePath, destPath,
							paragraphName);
					SendHelper.send("Message3", messageState);
					DBDao.dataSources.clear();
					DBDao dao = new DBDao(main.getNeededList());
					List<DataSource> dataSources = dao.getDataSources();
					joinHelper = new JoinHelper(dataSources, dataJoinList, messageState);
					Connection conn = helper.getConnection();
					// int i = 10;
					// while( i-- > 0)
					// {
					// System.out.println(paragraphName + " : " + i);
					// try
					// {
					// Thread.sleep(500);
					// }
					// catch (InterruptedException ie)
					// {
					// return false;
					// }
					// }
					Message3 messageProcess = (Message3) init.InitMessage3("Paragraph3 Running", 2, sourcePath,
							destPath, paragraphName);
					if (!Constant.data_fragment) {// 不使用数据分片的情况
						this.stage = "joining";
						timerHelper = new TimerHelper("Message3", messageProcess, this);
						timerHelper.send();
						if (joinHelper.JoinCore()) {
							closeConnection(dao.getConnections());
							System.out.println(paragraphName);
							try {
								this.stage = "integrating";
								MultiSourceSchemaTransformation.insertRecordsToTable2(joinHelper.getResults(), conn,
										main.getTableDest(), main.getInsertMap());
								init.UpdateMessage3(messageProcess, "Paragraph3 Integrate Finished", 100);
								SendHelper.send("Message3", messageProcess);
								init.UpdateMessage3(messageState, "Paragraph3 Integrate Finished", 0);
								SendHelper.send("Message3", messageState);
								UpdateSyncTime(paragraphName);
								timerHelper.getTimer().cancel();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								init.UpdateMessage2(messageState, "Paragraph3 Terminated Abnormally", 0);
								SendHelper.send("Message3", messageState);
								SendHelper.sendError(paragraphName, e1);
								e1.printStackTrace();
								return false;
							}finally {
								if (conn != null) {
									if (!conn.isClosed())
										conn.close();
								}
							}
						}
					} else {
						this.stage = "joining";
						timerHelper = new TimerHelper("Message3", messageProcess, this);
						timerHelper.send();
						if (joinHelper.JoinCore2(paragraphName)) {
							closeConnection(dao.getConnections());
							System.out.println(a);
							a++;
							try {
								this.stage = "integrating";
								MultiSourceSchemaTransformation.insertRecordsWithFragment(conn, main.getTableDest(),
										main.getInsertMap(), paragraphName);
								init.UpdateMessage3(messageProcess, "Paragraph3 Integrate Finished", 100);
								SendHelper.send("Message3", messageProcess);
								init.UpdateMessage3(messageState, "Paragraph3 Integrate Finished", 0);
								SendHelper.send("Message3", messageState);
								System.out.println("paragraph3插入连接已关闭");
								UpdateSyncTime(paragraphName);
								timerHelper.getTimer().cancel();
							} catch (Exception e2) {
								init.UpdateMessage2(messageState, "Paragraph3 Terminated Abnormally", 0);
								SendHelper.send("Message3", messageState);
								SendHelper.sendError(paragraphName, e2);
								e2.printStackTrace();
								return false;
							} finally {
								if (conn != null) {
									if (!conn.isClosed())
										conn.close();
								}
							}
						}
					}
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				SendHelper.sendError(paragraphName, e);
				return false;
			}
		}
		return false;
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

	private String getSourcePath(List<Map<String, Map<String, Column>>> list) {
		if (list.size() <= 0) {
			return null;
		}
		Set<String> set = new HashSet<String>();
		for (Map<String, Map<String, Column>> map : list) {
			set.addAll(map.keySet());
		}
		if (set.size() <= 0) {
			return null;
		}
		String s = "[";
		for (String source : set) {
			s += source;
			s += ",";
		}
		s = s.substring(0, s.length() - 1);
		s += "]";
		return s;
	}

	private void UpdateSyncTime(String paragraphName) {
		String updateSql = "update paragraph set last_sync_time=? where name=?";
		DBHelper.executeNonQuery(updateSql, DateUtil.getCurrentDate(), paragraphName);
	}

	public static void main(String[] args) {
		try {
			new SyncHelper2().joinSync("tsloan_general");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
