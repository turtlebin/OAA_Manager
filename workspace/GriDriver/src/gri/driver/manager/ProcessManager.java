package gri.driver.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gri.driver.model.Connection;
import gri.driver.model.process.Process;
import gri.driver.model.process.Container;
import gri.driver.model.process.Processor;
import gri.driver.model.process.StatsConf;
import gri.driver.model.process.StatsConfData2;
import gri.driver.model.process.StatsConfDto1;
import gri.driver.model.process.StatsResult;
import gri.driver.model.process.View;
import gri.driver.util.DriverConstant;

public class ProcessManager {
	private Connection connection;

	public ProcessManager(Connection connection) {
		super();
		this.connection = connection;
	}

	@SuppressWarnings("unchecked")
	public List<Processor> listProcessor() {
		Object result = connection.execute_sync(DriverConstant.OPERATE_LIST_PROCESSOR, "");
		if (result != null)
			return (List<Processor>) result;
		else
			return new ArrayList<Processor>();
	}

	public boolean addProcessor(Processor processor) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_ADD_PROCESSOR, processor);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}


	public boolean updateProcessor(Processor processor){
		Object result = connection.execute_sync(DriverConstant.OPERATE_UPDATE_PROCESSOR, processor);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}
	
	public boolean delProcessor(Integer id) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_DEL_PROCESSOR, id);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	@SuppressWarnings("unchecked")
	public List<Container> listContainer(String account) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_LIST_CONTAINER, account);
		if (result != null)
			return (List<Container>) result;
		else
			return new ArrayList<Container>();
	}
	
	@SuppressWarnings("unchecked")
	public List<Container> listContianerByProcessor(String account, Integer processorId){
		Object result = connection.execute_sync(DriverConstant.OPERATE_LIST_CONTAINER_BY_PROCESSOR, new Object[]{account , processorId});
		if (result != null)
			return (List<Container>) result;
		else
			return new ArrayList<Container>();
	}

	public boolean addContainer(Container container) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_ADD_CONTAINER, container);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}


	public boolean updateContainer(Container container){
		Object result = connection.execute_sync(DriverConstant.OPERATE_UPDATE_CONTAINER, container);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}
	
	public boolean delContainer(Integer id) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_DEL_CONTAINER, id);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<View> listView() {
		Object result = connection.execute_sync(DriverConstant.OPERATE_LIST_VIEW, "");
		if (result != null)
			return (List<View>) result;
		else
			return new ArrayList<View>();
	}
	
	@SuppressWarnings("unchecked")
	public List<View> listViewByProcessor(Integer processorId){
		Object result = connection.execute_sync(DriverConstant.OPERATE_LIST_VIEW_BY_PROCESSOR, processorId);
		if (result != null)
			return (List<View>) result;
		else
			return new ArrayList<View>();
	}

	public boolean addView(View view) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_ADD_VIEW, view);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	public boolean updateView(View view){
		Object result = connection.execute_sync(DriverConstant.OPERATE_UPDATE_VIEW, view);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}
	
	public boolean delView(Integer id) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_DEL_VIEW, id);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<View> listViewByContainer(Integer containerId) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_LIST_VIEWS_BY_CONTAINER, containerId);
		if (result != null)
			return (List<View>) result;
		else
			return new ArrayList<View>();
	}
	
	@SuppressWarnings("unchecked")
	public List<Process> listProcess(Integer paragraphId) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_LIST_PROCESS, paragraphId);
		if (result != null)
			return (List<Process>) result;
		else
			return new ArrayList<Process>();
	}

	public View getView(String containerName, String viewName) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_GET_VIEW_BY_NAME, new String[]{containerName, viewName});
		if (result != null)
			return (View) result;
		else
			return null;
	}
	
	public boolean addProcess(Process process) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_ADD_PROCESS, process);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}


	public boolean updateProcess(Process process){
		Object result = connection.execute_sync(DriverConstant.OPERATE_UPDATE_PROCESS, process);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}
	
	public boolean delProcess(Integer id) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_DEL_PROCESS, id);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}
	
	public Object readView(String containerName, String viewName) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_READ_VIEW, new String[]{containerName, viewName});
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StatsConfDto1> listStatsConf(int paragraphId){
		Object result = connection.execute_sync(DriverConstant.OPERATE_LIST_STATS, paragraphId);
		if (result != null)
			return (List<StatsConfDto1>) result;
		else
			return new ArrayList<StatsConfDto1>();
	}

	public boolean addStatsConf(StatsConfDto1 statsConfDto1){
		Object result = connection.execute_sync(DriverConstant.OPERATE_ADD_STATS, statsConfDto1);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}


	public boolean updateStatsConf(StatsConfDto1 statsConfDto1){
		Object result = connection.execute_sync(DriverConstant.OPERATE_UPDATE_STATS, statsConfDto1);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}
	
	public boolean delStatsConf(Integer processId) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_DEL_STATS, processId);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}
	
	//stats
	public StatsResult calculate(Integer paragraphID,StatsConf statsConf) {
		StatsConfData2 data = new StatsConfData2();
		data.setParagraphID(paragraphID);
		data.setStatsConf(statsConf);
		Object result = connection.execute_sync(DriverConstant.OPERATE_STATS_CALCULATE, data);
		if (result != null)
			return (StatsResult) result;
		else
			return null;
	}
	
	public String[] colList(Integer paragraphID) {
		Object result = connection.execute_sync(DriverConstant.OPERATE_COL_LIST, paragraphID);
		if (result != null)
			return (String[]) result;
		else
			return null;
	}
	
	public String getBlockName(Integer paragraphId){
		Object result = connection.execute_sync(DriverConstant.OPERATE_GET_BLOCK_NAME, paragraphId);
		if (result != null)
			return (String) result;
		else
			return null;
	}
	
	public void setBlockName(Integer paragraphId, String blockName){
		connection.execute_sync(DriverConstant.OPERATE_SET_BLOCK_NAME, new Object[]{paragraphId, blockName});	
	}
	
	public String readBlock(String blockName){
		Object result = connection.execute_sync(DriverConstant.OPERATE_READ_BLOCK, blockName);
		if (result != null)
			return (String) result;
		else
			return null;
	}
}
