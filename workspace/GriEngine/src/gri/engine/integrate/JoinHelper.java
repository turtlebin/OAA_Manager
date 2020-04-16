package gri.engine.integrate;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gir.engine.monitor.Message3;
import gir.engine.monitor.SendHelper;
import gri.engine.dest.Pair;
import gri.engine.util.Constant;

public class JoinHelper {
	private List<DataSource> dataSources;
	private  List<DataJoin> dataJoins;
	private List<String> JoinedSourceList=new ArrayList<String>();//已经连接过的数据源
    private List<Pair<String,String>> preparedJoinPairList=new ArrayList<Pair<String,String>>();
    private int flag=0;//用于递归函数出错时强制退出
    private List<Record> results=new ArrayList<Record>();
    public int process=-1;
    private int joinSize=-1;
    private Message3 message;
    

    public int getProcess() {
    	if(process==-1||joinSize==-1||joinSize==0) {
    		return 0;
    	}
    	else 
    	{
    		return (process*100/joinSize);
    	}
    }
    
	public JoinHelper(List<DataSource> dataSources,List<DataJoin> dataJoins)
	{
		this.dataSources=dataSources;
		this.dataJoins=dataJoins;
		initPreparedJoinPairList();
	}
	
	public JoinHelper(List<DataSource> dataSources,List<DataJoin> dataJoins,Message3 message)
	{
		this.dataSources=dataSources;
		this.dataJoins=dataJoins;
		this.message=message;
		initPreparedJoinPairList();
	}
	
	public List<Record> getResults() {
		return results;
	}

	public void setResults(List<Record> results) {
		this.results = results;
	}
//	public void JoinMain()
//	{
//		while(preparedJoinPairList.size()!=0)
//		{
//			int size=preparedJoinPairList.size();
//			if(size>0)
//			{
//			JoinCore();
//			}
//			if(size==preparedJoinPairList.size())//若
//			{
//				System.out.println("连接数据源间配置出现问题，请修改后再尝试");
//				break;
//			}
//		}
//	}
	
	public boolean JoinCore() throws IOException
	{
		joinSize=(joinSize==-1)?preparedJoinPairList.size():joinSize;//因为存在重复调用该函数的情况，因此作判断，第二次调用时不改变该值
		process=(process==-1)?0:process;
		Iterator<Pair<String,String>> it=preparedJoinPairList.iterator();
		while(it.hasNext())//执行一次循环不一定能够连接全部数据源，需要递归执行该函数
		{
			Pair<String,String> preparedJoinPair=it.next();
			if(JoinedSourceList.size()==0)
			{
				JoinedSourceList.add(preparedJoinPair.getFirst());
				JoinedSourceList.add(preparedJoinPair.getSecond());
				DataSource dataSource1=getDataSourceBySource(preparedJoinPair.getFirst());
				DataSource dataSource2=getDataSourceBySource(preparedJoinPair.getSecond());
				List<Pair<String,String>> list=getJoinListBySourcePair(preparedJoinPair);//获取对应数据源pair中的数据连接关系列表，这个方法有问题
				try {
					if((results=Join(dataSource1,dataSource2,list)).size()==0)//进行连接
						{
						message.setStatus("joined failed");
						SendHelper.send("Message3", message);
						System.out.println("数据源连接失败");
						return false;
						//break;//连接失败，报错
						}
					else 
					{
						process++;
					}
				} catch (SQLException e) {
					message.setStatus("joined failed");
					SendHelper.send("Message3", message);
					e.printStackTrace();
					return true;
				}
				it.remove();//然后利用迭代器在循环中删除该连接对
			}
			else
			{
				if(!(JoinedSourceList.contains(preparedJoinPair.getFirst())||
						JoinedSourceList.contains(preparedJoinPair.getSecond()))) //如果已连接数据源列表不包含正要准备连接的数据源，则暂时跳过连接
				{
					continue;
				}
				else if(JoinedSourceList.contains(preparedJoinPair.getFirst())&&
						JoinedSourceList.contains(preparedJoinPair.getSecond()))//如果已连接数据源列表包含两个正要准备连接的数据源，则只需要对resultSet做等值筛选处理。
				{
					List<Pair<String,String>> list=getJoinListBySourcePair(preparedJoinPair);
					results=MultiSourceSchemaTransformation.recordsFilter(results, preparedJoinPair, list);
					//等值筛选操作
					it.remove();
					process++;
				}
				else//只有一个数据源在已连接列表的情况
				{
					String outterSource=findOutterSource(preparedJoinPair);//找到那个不在已连接数据源列表的数据源，然后进行连接操作
					String innerSource=findInnerSource(preparedJoinPair);
					JoinedSourceList.add(outterSource);
					DataSource dataSource1=getDataSourceBySource(innerSource);
					DataSource dataSource2=getDataSourceBySource(outterSource);
					boolean joinPairInverse=false;
					if(outterSource.equalsIgnoreCase(preparedJoinPair.getFirst()))
					{
						joinPairInverse=true;
					}
					List<Pair<String,String>> list=getJoinListBySourcePair(preparedJoinPair);//获取对应数据源pair中的数据连接关系列表，这个方法有问题,解决不了12再32的情况
					try {
						if((results=Join(results,dataSource1,dataSource2,list,joinPairInverse)).size()==0)
						{
							message.setStatus("joined failed");
							SendHelper.send("Message3", message);
							System.out.println("数据源连接失败");
							return false;
							//break;//连接失败报错
						}
						else 
						{
							process++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						message.setStatus("joined failed");
						SendHelper.send("Message3", message);
						e.printStackTrace();
						return false;
					}
					it.remove();  
				}
			}
		}
		if(preparedJoinPairList.size()>0)
		{
			int size_before=preparedJoinPairList.size();
			JoinCore();//递归调用
			if(flag==1)
			{
				return false;
			}
			int size_after=preparedJoinPairList.size();
			if(size_after>0&&size_before==size_after)//如果preparedJoinPairList.size()没有减少，则说明连接配置出现问题，不能正常把所有数据源连接起来
			{
			   System.out.println("连接数据源间配置出现问题，请修改后再尝试");
			   flag=1;//将flag设置为1，说明退出递归
			   return false;
			}
		}
		return true;
	}

	
	public boolean JoinCore2(String destTable) throws IOException
	{
		joinSize=(joinSize==-1)?preparedJoinPairList.size():joinSize;//因为存在重复调用该函数的情况，因此作判断，第二次调用时不改变该值
		process=(process==-1)?0:process;
		Iterator<Pair<String,String>> it=preparedJoinPairList.iterator();
		while(it.hasNext())//执行一次循环不一定能够连接全部数据源，需要递归执行该函数
		{	
			Pair<String,String> preparedJoinPair=it.next();
			if (JoinedSourceList.size() == 0) 
			{
				JoinedSourceList.add(preparedJoinPair.getFirst());
				JoinedSourceList.add(preparedJoinPair.getSecond());
				DataSource dataSource1 = getDataSourceBySource(preparedJoinPair.getFirst());
				DataSource dataSource2 = getDataSourceBySource(preparedJoinPair.getSecond());
				List<Pair<String, String>> list = getJoinListBySourcePair(preparedJoinPair);// 获取对应数据源pair中的数据连接关系列表，返回的连接关系列表first，second顺序与数据源pair中first，second顺序是对应的
				if (!Join2(dataSource1, dataSource2, list,destTable))// 进行连接
				{
					message.setStatus("joined failed");
					SendHelper.send("Message3", message);
					System.out.println("数据源连接失败");
					return false;
					//break;// 连接失败，报错
				}
				else 
				{
					process++;
				}
				it.remove();// 然后利用迭代器在循环中删除该连接对
			}
			else
			{
				if(!(JoinedSourceList.contains(preparedJoinPair.getFirst())||
						JoinedSourceList.contains(preparedJoinPair.getSecond()))) //如果已连接数据源列表不包含正要准备连接的数据源，则暂时跳过连接
				{
					continue;
				}
				else if(JoinedSourceList.contains(preparedJoinPair.getFirst())&&
						JoinedSourceList.contains(preparedJoinPair.getSecond()))//如果已连接数据源列表包含两个正要准备连接的数据源，则只需要对resultSet做等值筛选处理。
				{
					List<Pair<String,String>> list=getJoinListBySourcePair(preparedJoinPair);//prepareJoinPair是数据源字符串pair，通过该pair获取数据源列pair
					MultiSourceSchemaTransformation.recordsFilter2("destination",preparedJoinPair, list,destTable);
					FileUtil.deleteFilebyName(Constant.ObjectFolder,destTable, "destination");
				    FileUtil.renameFilebyName(Constant.ObjectFolder,destTable, "temp", "destination");
					//等值筛选操作
					it.remove();
					process++;
				}
				else//只有一个数据源在已连接列表的情况
				{
					String outterSource=findOutterSource(preparedJoinPair);//找到那个不在已连接数据源列表的数据源，然后进行连接操作
					String innerSource=findInnerSource(preparedJoinPair);
					JoinedSourceList.add(outterSource);
					DataSource dataSource1=getDataSourceBySource(innerSource);
					DataSource dataSource2=getDataSourceBySource(outterSource);
					boolean joinPairInverse=false;
					if(outterSource.equalsIgnoreCase(preparedJoinPair.getFirst()))
					{
						joinPairInverse=true;
					}
					List<Pair<String,String>> list=getJoinListBySourcePair(preparedJoinPair);//获取对应数据源pair中的数据列连接关系列表
					try {
						if(!(Join2(dataSource1,dataSource2,list,joinPairInverse,destTable)))
						{
							message.setStatus("joined failed");
							SendHelper.send("Message3", message);
							System.out.println("数据源连接失败");
							return false;
							//break;//连接失败报错
						}
						else 
						{
							process++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
					it.remove();  
				}
			}
		}
		if(preparedJoinPairList.size()>0)
		{
			int size_before=preparedJoinPairList.size();
			JoinCore2(destTable);//递归调用
			if(flag==1)
			{
				return false;
			}
			int size_after=preparedJoinPairList.size();
			if(size_after>0&&size_before==size_after)//如果preparedJoinPairList.size()没有减少，则说明连接配置出现问题，不能正常把所有数据源连接起来
			{
			   System.out.println("连接数据源间配置出现问题，请修改后再尝试");
			   flag=1;//将flag设置为1，说明退出递归
			   return false;
			}
		}
		return true;
	}

	
	
	private  void initPreparedJoinPairList() 
	{
		for(DataJoin dataJoin:dataJoins)
		{
			preparedJoinPairList.add(dataJoin.getPreparedJoinPair());
		}
	}
	
	private DataSource getDataSourceBySource(String source)
	{
		for(DataSource dataSource:dataSources)
		{
			if(dataSource.getDataSource().equals(source))
			{
				return dataSource;
			}
		}
		return null;
	}
	
	private List<Pair<String,String>> getJoinListBySourcePair(Pair<String,String> pair)
	{
		for(DataJoin dataJoin:dataJoins)
		{
			if(dataJoin.sourceEquals(pair))
			{
				return dataJoin.getList();
			}
		}
		return null;
	}
	
	private List<Record> Join(DataSource dataSource1,DataSource dataSource2,List<Pair<String,String>> JoinInfoPair) throws SQLException
	{
		if(dataSource1==null||dataSource2==null||JoinInfoPair.size()==0)
		{
			return null;
		}
		ResultSet rs1=dataSource1.getResult();
		ResultSet rs2=dataSource2.getResult();
		String sqlType1=dataSource1.getSqlType();
		String sqlType2=dataSource2.getSqlType();
		List<Record> records1=MultiSourceSchemaTransformation.resultSetToRecords(dataSource1.getDataSource(),rs1, sqlType1);
		List<Record> records2=MultiSourceSchemaTransformation.resultSetToRecords(dataSource2.getDataSource(),rs2, sqlType2);
		//List<Record> results=MultiSourceSchemaTransformation.outterJoinNatureOnProperties(records1, records2,JoinInfoPair);
		List<Record> results=MultiSourceSchemaTransformation.outterJoinEqualOnProperties(records1, records2, JoinInfoPair);
		return results;
	}
	
	private boolean Join2(DataSource dataSource1,DataSource dataSource2,List<Pair<String,String>> JoinInfoPair,String destTable)
	{
//		if(dataSource1==null||dataSource2==null||JoinInfoPair.size()==0)
//		{
//			return false;
//		}
		ResultSet rs1=dataSource1.getResult();
		ResultSet rs2=dataSource2.getResult();
		String sqlType1=dataSource1.getSqlType();
		String sqlType2=dataSource2.getSqlType();
	    MultiSourceSchemaTransformation.resultSetToRecords2(dataSource1.getDataSource(),destTable,rs1, sqlType1);
        MultiSourceSchemaTransformation.resultSetToRecords2(dataSource2.getDataSource(),destTable,rs2, sqlType2);
        boolean success=MultiSourceSchemaTransformation.joinWithFragment(JoinInfoPair,dataSource1.getDataSource(),dataSource2.getDataSource(),"destination",destTable);
		//List<Record> results=MultiSourceSchemaTransformation.outterJoinEqualOnProperties(records1, records2, JoinInfoPair);
		return success;
	}
	


	private List<Record> Join(List<Record> records,DataSource dataSource1,DataSource dataSource2,List<Pair<String,String>> JoinInfoPair,boolean joinPairInverse) throws SQLException
	{
		if(records==null||dataSource2==null||JoinInfoPair.size()==0)
		{
			return null;
		}
		ResultSet rs2=dataSource2.getResult();
		String sqlType2=dataSource2.getSqlType();
		List<Record> records2=MultiSourceSchemaTransformation.resultSetToRecords(dataSource2.getDataSource(),rs2, sqlType2);
		//List<Record> results=MultiSourceSchemaTransformation.outterJoinNatureOnProperties(records,dataSource1.getDataSource(), records2,JoinInfoPair,joinPairInverse);
		List<Record> results=MultiSourceSchemaTransformation.outterJoinEqualOnProperties(records, dataSource1.getDataSource(),records2,JoinInfoPair,joinPairInverse);
		return results;
	}
	
	private boolean Join2(DataSource dataSource1,DataSource dataSource2,List<Pair<String,String>> JoinInfoPair,boolean joinPairInverse,String destTable) throws SQLException
	{
		if(dataSource2==null||JoinInfoPair.size()==0)
		{
			return false;
		}
		ResultSet rs2=dataSource2.getResult();
		String sqlType2=dataSource2.getSqlType();
		MultiSourceSchemaTransformation.resultSetToRecords2(dataSource2.getDataSource(),destTable,rs2, sqlType2);
		//List<Record> results=MultiSourceSchemaTransformation.outterJoinEqualOnProperties(records, dataSource1.getDataSource(),records2,JoinInfoPair,joinPairInverse);
        boolean success=MultiSourceSchemaTransformation.joinWithFragment2(JoinInfoPair,"destination",dataSource1.getDataSource(),dataSource2.getDataSource(),"temp",joinPairInverse,destTable);
        boolean success1=FileUtil.deleteFilebyName(Constant.ObjectFolder,destTable, "destination");
        boolean success2=FileUtil.renameFilebyName(Constant.ObjectFolder, destTable,"temp", "destination");
		return (success&&success1&&success2);
	}
	
	private String findOutterSource(Pair<String,String> pair)
	{
		if(JoinedSourceList.contains(pair.getFirst()))
		{
			return pair.getSecond();
		}
		else
		{
			return pair.getFirst();
		}
	}
	
	private String findInnerSource(Pair<String,String> pair)
	{
		if(JoinedSourceList.contains(pair.getFirst()))
		{
			return pair.getFirst();
		}
		else
		{
			return pair.getSecond();
		}
	}
}
