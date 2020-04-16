package gri.engine.integrate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import gir.engine.monitor.Message1;
import gir.engine.monitor.Message2;
import gir.engine.monitor.MessageInitiator;
import gir.engine.monitor.RowGetter;
import gir.engine.monitor.SendHelper;
import gir.engine.monitor.TimerHelper;
import gri.engine.dest.Column;
import gri.engine.dest.DestDatabase;
import gri.engine.service.CacheService;
import gri.engine.util.DBHelper;
import gri.engine.util.DateUtil;

public class SyncHelper implements sync{//段1的同步
	private DestDatabase des=null;
	
	public int getProcess() {
		return (des==null)?-1:des.getProcess();
	}

	public boolean joinSync(String paragraphName, String sourcePath, String destPath, String cacheUUID) throws IOException {
		CacheService cs = new CacheService(cacheUUID);
		InputStream input = cs.getInputStream();
		InputStream input2 = cs.getInputStream();
		InputStream input3 = cs.getInputStream();

		des = new DestDatabase(sourcePath, destPath);
		int columnCount = des.getColumnCount(input);
		List<Column> columns = des.initColumn(input2, columnCount);
		String destBaseType = destPath.split("###")[0];
		boolean create = false;
		boolean insert = false;
		synchronized (SyncHelper.class) {// 接下来这部分比较消耗内存，因此使用类锁进行同步限制
//			int i =10;
//			while (i-- > 0) {
//				System.out.println(paragraphName + " : " + i);
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException ie) {
//					return false;
//				}
//			}

			MessageInitiator init=new MessageInitiator();
			Message1 messageState=(Message1)init.InitMessage1("Paragraph1 Started",1,sourcePath,destPath,paragraphName);//此处创建的是Paragraph1的运行状态消息
			SendHelper.send("Message1", messageState);
			RowGetter getter=new RowGetter(sourcePath,"Paragraph1");
			getter.start();
			Message1 messageProcess=(Message1)init.InitMessage1("Paragraph1 Running", 2,sourcePath,destPath,paragraphName);//此处创建的是Paragraph1的运行进度消息
			TimerHelper timerHelper=new TimerHelper("Message1",messageProcess,getter,this);
			timerHelper.send();
			if (destBaseType.equalsIgnoreCase("mysql")) {
				create = des.createTable(columns, columnCount, paragraphName, false);
				insert = des.insertMySql4(input3, paragraphName);
			} else if (destBaseType.equalsIgnoreCase("Oracle")) {
				create = des.createOracleTable(columns, columnCount, paragraphName, false);
				insert = des.insertOracle(input3, paragraphName);
			}
			timerHelper.getTimer().cancel();
			if (create && insert) {
				init.UpdateMessage1(messageProcess, "Paragraph1 Integrate Finished",100);
				init.UpdateMessage1(messageState, "Paragraph1 Termanited Normally",0);
				SendHelper.send("Message1", messageProcess);
				SendHelper.send("Message1", messageState);
				UpdateSyncTime(paragraphName);
				return true;
			} else {
				init.UpdateMessage1(messageState, "Paragraph1 Terminated Abnormally",0);
				SendHelper.send("Message1", messageState);
				SendHelper.sendError(paragraphName, null);
				return false;
			}
		}
	}

	private void UpdateSyncTime(String paragraphName) {
		String updateSql="update paragraph set last_sync_time=? where name=?";
		DBHelper.executeNonQuery(updateSql, DateUtil.getCurrentDate(),paragraphName);
	}
}
