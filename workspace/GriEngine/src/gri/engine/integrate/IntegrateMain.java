package gri.engine.integrate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gri.engine.dest.Column;
import gri.engine.dest.Pair;

public class IntegrateMain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7477950271452533058L;
	private  List<Map<String, Map<String, Column>>> list = new ArrayList<Map<String, Map<String, Column>>>();// list存放格式为Map<数据源，Map<目标表字段名称，来源表字段column>>
	private  List<Pair<String, String>> wholePairList = new ArrayList<Pair<String, String>>();// wholePairList存放格式为Pair<数据源1!!!列名,数据源2!!!列名>
	private  List<DataSourceNeededColumn> neededList = new ArrayList<DataSourceNeededColumn>();
	private  List<DataJoin> dataJoinList=new ArrayList<DataJoin>();
	private  Map<String,String> insertMap=new LinkedHashMap<String,String>();
	private  String tableDest=null;

	public List<Map<String, Map<String, Column>>> getList() {
		return list;
	}

	public void setList(List<Map<String, Map<String, Column>>> list) {
		this.list = list;
	}

	public List<Pair<String, String>> getWholePairList() {
		return wholePairList;
	}

	public void setWholePairList(List<Pair<String, String>> wholePairList) {
		this.wholePairList = wholePairList;
	}
	public  String getTableDest() {
		return tableDest;
	}

	public  void setTableDest(String tableDest) {
		this.tableDest = tableDest;
	}

	public  Map<String, String> getInsertMap() {
		return insertMap;
	}

	public  void setInsertMap(Map<String, String> insertMap) {
		this.insertMap = insertMap;
	}

	public  List<DataJoin> getDataJoinList() {
		return dataJoinList;
	}

	public void setDataJoinList(List<DataJoin> dataJoinList) {
		this.dataJoinList = dataJoinList;
	}

	public List<DataSourceNeededColumn> getNeededList() {
		return neededList;
	}

	public void setNeededList(List<DataSourceNeededColumn> neededList) {
		this.neededList = neededList;
	}

	public IntegrateMain(List<Map<String, Map<String, Column>>> list, List<Pair<String, String>> wholePairList,String tableDest) {
		this.list = list;
		this.wholePairList = wholePairList;
		this.tableDest=tableDest;
		initNeededColumnInfo(true);//初始化需要各数据源对应列的信息
		initDataSourcePair();//初始化各数据源间的连接信息
		initInsertMap();
	}
	public IntegrateMain(List<Map<String, Map<String, Column>>> list,String tableDest) {
		this.list = list;
		this.tableDest=tableDest;
		initNeededColumnInfo(false);//初始化需要各数据源对应列的信息
		initInsertMap();
	}

	private void initNeededColumnInfo(boolean joined) {
		for (Map<String, Map<String, Column>> map : list) {
			for (Map.Entry<String, Map<String, Column>> entry : map.entrySet()) {
				String dataSource = entry.getKey();
				DataSourceNeededColumn neededInit = new DataSourceNeededColumn(dataSource);
				neededInit.addColumns(entry.getValue().values());// 先从list初始化各数据源所需要的列，先不考虑join的额外字段
				if(joined) {
				addJoinNeededColumn(neededInit, dataSource);// 添加由于join所额外需要的字段
				}
				neededList.add(neededInit);
			}
		}
	}
	
	private void initInsertMap() {
		for(Map<String,Map<String,Column>> map:list)
		{
			for(Map.Entry<String, Map<String,Column>> entry:map.entrySet())
			{
				String dataSource=entry.getKey();
				for(Map.Entry<String, Column> entry2:entry.getValue().entrySet())
				{
					String destColumn=entry2.getKey();
					String sourceColumn=entry2.getValue().getColumnName();
					insertMap.put(destColumn, dataSource+"@@@"+sourceColumn);
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initDataSourcePair()
	{
		if(wholePairList==null) {return;}
		for(Pair<String,String> pair:wholePairList)
		{
			String[] pairLeft=pair.getFirst().split("@@@");
			String[] pairRight = pair.getSecond().split("@@@");
			if(dataJoinList.size()==0)//若某一对数据源的连接列表为空，则直接添加
			{
				List<Pair<String,String>> list=new ArrayList<Pair<String,String>>();
				list.add(new Pair(pairLeft[1],pairRight[1]));
				DataJoin dataJoin=new DataJoin(pairLeft[0],pairRight[0],list);
				dataJoinList.add(dataJoin);
			}
			else
			{
				List<DataJoin> temp=new ArrayList<DataJoin>();
				boolean existed=false;
				for(DataJoin join:dataJoinList)
				{
					if(isExistedDataJoin(pairLeft[0],pairRight[0],join))//若已经存在相同的连接对，则说明是多值连接
					{
						join.getList().add(new Pair(pairLeft[1],pairRight[1]));
						existed=true;
					}
				}
				if(!existed)
				{
					List<Pair<String,String>> list=new ArrayList<Pair<String,String>>();
					list.add(new Pair(pairLeft[1],pairRight[1]));
					DataJoin dataJoin=new DataJoin(pairLeft[0],pairRight[0],list);
					temp.add(dataJoin);
				}
				if(temp.size()>0) 
				{
				dataJoinList.addAll(temp);
				}
			}
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	private void addJoinNeededColumn(DataSourceNeededColumn neededInit, String dataSource) {
		for (Pair<String, String> pair : wholePairList) {
			String[] pairLeft = pair.getFirst().split("@@@");
			String[] pairRight = pair.getSecond().split("@@@");
			if (pairLeft[0].equals(dataSource)) {
				if (!neededInit.isExisted(pairLeft[1])) {
					neededInit.addColumn(pairLeft[1]);
				}
			} else if (pairRight[0].equals(dataSource)) {
				if (!neededInit.isExisted(pairRight[1])) {
					neededInit.addColumn(pairRight[1]);
				}
			} else {
				continue;
			}
		}
	}
	
	private boolean isExistedDataJoin(String first,String second,DataJoin dataJoin)
	{
		if(first.equals(dataJoin.getFirstDataSource())&&second.equals(dataJoin.getSecondDataSource())||
				first.equals(dataJoin.getSecondDataSource())&&second.equals(dataJoin.getFirstDataSource()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
