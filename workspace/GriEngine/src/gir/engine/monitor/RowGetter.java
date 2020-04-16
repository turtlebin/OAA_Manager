package gir.engine.monitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class RowGetter extends Thread{

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
	private Connection conn=null;
	private String ParagraphType;
	private String selectSql;
	public int rowCount=-1;
	public RowGetter(String dataSource,String table,String ParagraphType) {
		this.dataSource=dataSource;
		this.table=table;
		this.ParagraphType=ParagraphType;
		// TODO Auto-generated constructor stub
	}
	
	public RowGetter(String dataSource,String ParagraphType) {
		this.dataSource=dataSource;
		this.ParagraphType=ParagraphType;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void run() {
		if (this.ParagraphType.equalsIgnoreCase("Paragraph2")) {
			analyzeParagraph2();
			try {
				Class.forName(this.driver);
				conn = DriverManager.getConnection(this.url, this.username, this.password);
				String selectSql = "select count(*) from " +this.table;
				Statement state = conn.createStatement();
				ResultSet rs = state.executeQuery(selectSql);
				if (rs.next()) {
					rowCount = rs.getInt(1);
				}
				rs.close();
				state.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (this.ParagraphType.equalsIgnoreCase("Paragraph1")) {
			analyzeParagraph1();
			try {
				Class.forName(this.driver);
				conn=DriverManager.getConnection(this.url, this.username, this.password);
				Statement state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet rs=state.executeQuery(this.selectSql);
				rs.last();
				rowCount=rs.getRow();
				rs.close();
				state.close();
				conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean analyzeParagraph1() {
		try {
			String strs[] = this.dataSource.split("###");
			this.type = strs[0];
			this.host = strs[1];
			this.port = strs[2];
			this.db_name = strs[3];
			this.username = strs[4];
			this.password= strs[5];
			this.selectSql = strs[6];
			if (this.type.equalsIgnoreCase("MySQL")) {
				this.driver = "com.mysql.jdbc.Driver";
				this.url = "jdbc:mysql://" + this.host + ":" + port + "/" + db_name+"?zeroDateTimeBehavior=convertToNull";// 生成url
				// this.url= "jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + ;//生成url
			} else if (this.type.equalsIgnoreCase("SQL Server")) {
				this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
				this.url = "jdbc:sqlserver://" + this.host + ":" + this.port + ";DatabaseName="
						+ this.db_name;
			} else if (this.type.equalsIgnoreCase("Oracle")) {
				this.driver = "oracle.jdbc.driver.OracleDriver";
				this.url = "jdbc:oracle:thin:@" + this.host + ":" + this.port + ":"
						+ this.db_name;
			} else if (this.type.equalsIgnoreCase("DB2")) {
				this.driver = "com.ibm.db2.jdbc.app.DB2.Driver";
				this.url = "jdbc:db2://" + this.host + ":" + port + "/" + db_name;
			} else if (this.type.equalsIgnoreCase("SYBASE")) {
				this.driver = "com.sybase.jdbc3.jdbc.SybDriver";
				this.url = "jdbc:sybase:Tds:" + this.host + ":" + this.port + "/"
						+ this.db_name;
			} else
				return false;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	  private synchronized boolean analyzeParagraph2() {//这部分的synchronized关键词应该是多余的，因为该关键词锁定的是对象，只有当run方法中调用某一个对象的方法时，才需要用到synchronzied关键字进行同步
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
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
}
