package gri.engine.integrate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class DBDao {

	private List<DataSourceNeededColumn> neededList;
	private List<ResultSet> resultSets=new ArrayList<ResultSet>();
	public static List<DataSource> dataSources=new ArrayList<DataSource>();
	private List<Connection> conns=new ArrayList<Connection>();
	
	public List<DataSourceNeededColumn> getNeededList() {
		return neededList;
	}

	public void setNeededList(List<DataSourceNeededColumn> neededList) {
		this.neededList = neededList;
	}

	public void setResultSet(List<ResultSet> resultSets) {
		this.resultSets = resultSets;
	}
	
	public List<Connection> getConnections(){
		return conns;
	}
	
    public DBDao(List<DataSourceNeededColumn> list)
    {
    	this.neededList=list;
    }
    
    public List<ResultSet> getResultSets()
    {
    	for(DataSourceNeededColumn neededInit:neededList)
    	{
    		ConcurrentSelect select=new ConcurrentSelect(neededInit);//开启多线程查询
    		select.start();
    		ResultSet rs=select.getResultSet();
    		if(rs!=null)
    		{
    		resultSets.add(rs);
    		}
    	}
    	return resultSets;
    }
    public List<DataSource> getDataSources() throws SQLException
    {
    	ConcurrentSelect[] select  =new ConcurrentSelect[neededList.size()];
    	int i=0;
//    	for(DataSourceNeededColumn neededInit:neededList)
//    	{
//    	    select[i]=new ConcurrentSelect(neededInit);//开启多线程查询
//    		select[i].start();
//   		    DataSource dataSource=new DataSource();
//    		ResultSet rs=select[i].getResultSet();
//    		i++;
//    		if(rs!=null)
//    		{
//    		  dataSource.setDataSource(neededInit.getDataSource());
//    		  dataSource.setResult(rs);
//    		  dataSources.add(dataSource);
//    		}
//    	}
    	CyclicBarrier barrier=new CyclicBarrier(neededList.size()+1);//设置barrier await次数为size+1，其中1代表主线程的await操作， 此时可实现等待所有子线程均完成后主线程才继续执行
    	for(DataSourceNeededColumn neededInit:neededList)
    	{
    		select[i]=new ConcurrentSelect(neededInit,barrier,"1");
    		select[i].start();
    		i++;
    	}
    	try {
			barrier.await();
			for(int a=0;a<=i-1;a++) {
			Connection conn=null;
			if((conn=select[a].getConnection())!=null&&!conn.isClosed()) {
				conns.add(conn);
			}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return dataSources;
    }


    public static synchronized void addDataSources(DataSourceNeededColumn neededInit,ResultSet result)
    {
    	DataSource dataSource=new DataSource();
    	dataSource.setDataSource(neededInit.getDataSource());
    	dataSource.setResult(result);
    	dataSources.add(dataSource);
    }
}
