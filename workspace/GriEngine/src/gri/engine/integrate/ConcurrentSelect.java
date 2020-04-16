package gri.engine.integrate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.sql.Statement;

public class ConcurrentSelect extends Thread{
    private String db_name;
    private String port;
    private String type;
    private String username;
    private String password;
    private String host;
    private String table;
    private String dataSource;
    private String driver;
    private String url;
    private DataSourceNeededColumn neededInit;
    private List<String> neededColumn;
	private ResultSet rs;
	private CyclicBarrier barrier;
	private Connection conn=null;
    private String flag=null;
	
    public ResultSet getResultSet() {
		return rs;
	}

	public void setResultSet(ResultSet rs) {
		this.rs = rs;
	}
	
    public ConcurrentSelect(String dataSource)
    {
    	this.dataSource=dataSource;
    }
    
    public Connection getConnection() {
    	return conn;
    }
    
    public ConcurrentSelect(DataSourceNeededColumn neededInit)
    {
    	this.neededInit=neededInit;
    	this.dataSource=neededInit.getDataSource();
    	this.neededColumn=neededInit.getNeededColumn();
    }
    
    public ConcurrentSelect(DataSourceNeededColumn neededInit,CyclicBarrier barrier,String flag)
    {
    	this.neededInit=neededInit;
    	this.dataSource=neededInit.getDataSource();
    	this.neededColumn=neededInit.getNeededColumn();
    	this.barrier=barrier;
    	this.flag=flag;
    }
    
    @Override
    public void run()
    {
    	analyzeParameter();
    	String selectSql=initSelectSql();
    	try 
    	{
    		Class.forName(this.driver);
    		conn=DriverManager.getConnection(this.url, this.username, this.password);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	try
    	{
    	if(conn!=null)
    	{
    		if(this.type.equalsIgnoreCase("MYSQL"))
    		{
    		  com.mysql.jdbc.Statement state=(com.mysql.jdbc.Statement) conn.createStatement();
    		  state.setFetchSize(1000);
			  state.enableStreamingResults();
    		  rs=state.executeQuery(selectSql);
    		  if(rs!=null)
    		  {
    			  if(this.flag.equals("2")) {
    			  DBDao2.addDataSources(this.neededInit, rs);
    			  }else {
        			  DBDao.addDataSources(this.neededInit, rs);
    			  }
    		  }
    		}
    		else
    		{
    			Statement state2 = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				state2.setFetchSize(1000);
				rs=state2.executeQuery(selectSql);
				if(rs!=null)
	    	    {
					  if(this.flag.equals("2")) {
		    			  DBDao2.addDataSources(this.neededInit, rs);
		    			  }else {
		        			  DBDao.addDataSources(this.neededInit, rs);
		    			  }	    		}
    		}
    		//conn.close();//此处如果关闭连接，则此后访问到resultSet时均会抛出异常，实际上可用RowSet取代ResultSet，但此处暂不关闭连接
    	}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	try {
			this.barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private synchronized String initSelectSql()
    {
    	String selectSql="select ";
    	for(String column:neededColumn)
    	{
    		selectSql=selectSql+column;
    		selectSql+=",";
    	}
    	selectSql=selectSql.substring(0,selectSql.length()-1);
    	selectSql+=" from ";
    	selectSql=selectSql+this.table;
    	return selectSql;
    }
    
    private synchronized boolean analyzeParameter() {//这部分的synchronized关键词应该是多余的，因为该关键词锁定的是对象，只有当run方法中调用某一个对象的方法时，才需要用到synchronzied关键字进行同步
		try {
			String strs[] = this.dataSource.split("###");
			this.type = strs[0];
			this.host = strs[1];
			this.db_name = strs[2];
			this.username= strs[3];
			this.password = strs[4];// 由传入的数据源路径获取用户名和密码
			this.port = strs[5];
			this.table=strs[6];

			if (type.equalsIgnoreCase("MySQL")) {
				this.driver = "com.mysql.jdbc.Driver";
				this.url = "jdbc:mysql://" + this.host + ":" + port + "/" + db_name
						+ "?useUnicode=true&characterEncoding=utf-8&useServerPrepStmts=false&rewriteBatchedStatements=true&zeroDateTimeBehavior=convertToNull";// 生成url
				// this.url= "jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + ;//生成url
			} else if (type.equalsIgnoreCase("SQL Server")) {
				this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
				this.url = "jdbc:sqlserver://" + this.host + ":" + this.port + ";DatabaseName="
						+ this.db_name;
			} else if (type.equalsIgnoreCase("Oracle")) {
				this.driver = "oracle.jdbc.driver.OracleDriver";
				this.url = "jdbc:oracle:thin:@" + this.host + ":" + this.port + ":" + this.db_name;
			} else if (type.equalsIgnoreCase("DB2")) {
				this.driver = "com.ibm.db2.jdbc.app.DB2.Driver";
				this.url = "jdbc:db2://" + this.host + ":" + port + "/" + db_name;
			} else if (type.equalsIgnoreCase("SYBASE")) {
				this.driver = "com.sybase.jdbc3.jdbc.SybDriver";
				this.url = "jdbc:sybase:Tds:" + this.host + ":" + port + "/" + this.db_name+"?charset=eucgb";
			} else
				return false;
			// LOGGER.info("database url:" + this.url);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
    
}
