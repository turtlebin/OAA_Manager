package gri.engine.core;


public class UpdateSyncConfigHelper {

	public UpdateSyncConfigHelper() {
		// TODO Auto-generated constructor stub
	}

	public boolean updateSyncTaskManager() {
		try {
			DataSyncTaskManager dataSyncTaskManager = new DataSyncTaskManager();
			dataSyncTaskManager.init();
			dataSyncTaskManager.shutdown();
			dataSyncTaskManager.addAllTask();
			dataSyncTaskManager.addAllTask2();
			dataSyncTaskManager.addAllTask3();
		    for(int i=0;i<100;i++) {
		    	System.out.println("修改同步配置成功"+i);
		    }
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
