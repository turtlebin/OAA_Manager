package gri.manager.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gri.engine.dest.Column;

public class DBHelper2 {
	private String type;
    private String host;
    private String db_name;
    private String userName;
    private String password;
    private String port;
    private String tableName;
    private String join;
    private String driver;
    private String url;
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDb_name() {
		return db_name;
	}

	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getJoin() {
		return join;
	}

	public void setJoin(String join) {
		this.join = join;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
    public DBHelper2(String join) {
    	analyzeParameter(join);
    }
    
	private boolean analyzeParameter(String join) {
		this.join=join;
		String[] parameters=join.split("###");
		this.type=parameters[0];
		this.host=parameters[1];
		this.db_name=parameters[2];
		this.userName=parameters[3];
		this.password=parameters[4];
		this.port=parameters[5];
		this.tableName=parameters[6];
		
		if (type.equalsIgnoreCase("MySQL")) {
			this.driver = "com.mysql.jdbc.Driver";
			this.url = "jdbc:mysql://" + this.host + ":" + port + "/" + db_name
					+ "?useUnicode=true&characterEncoding=utf-8&useServerPrepStmts=false&rewriteBatchedStatements=true";// 生成url
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
			this.url = "jdbc:sybase:Tds:" + this.host + ":" + port + "/" + this.db_name;
		} else
			return false;
		// LOGGER.info("database url:" + this.url);

		return true;
	}
	
	public Connection getConnection() {
		try {
			Class.forName(this.driver);
			Connection conn=DriverManager.getConnection(this.url, this.userName,this.password);
			return conn;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
