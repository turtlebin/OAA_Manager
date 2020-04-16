package gri.engine.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.net.ftp.FTPClient;

import gri.driver.util.DriverConstant;

public class DataSourceConnectTool {

	public boolean canConnect(String dataSourceConnectPath) {
		try {
			String strs[] = dataSourceConnectPath.split("###");
			String dataSourceType = strs[0];
			if (dataSourceType.equals(DriverConstant.DataSourceType_Database)) {
				String type = strs[1];
				String host = strs[2];
				String port = strs[3];
				String dbName = strs[4];
				String user = strs[5];
				String password = strs[6];
				String driver = "";
				String url = "";
				if (type.equalsIgnoreCase("MySQL")) {
					driver = "com.mysql.jdbc.Driver";
					url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
				} else if (type.equalsIgnoreCase("SQL Server")) {
					driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
					url = "jdbc:sqlserver://" + host + ":" + port + ";DatabaseName=" + dbName;
				} else if (type.equalsIgnoreCase("Oracle")) {
					driver = "oracle.jdbc.driver.OracleDriver";
					url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
				} else if (type.equalsIgnoreCase("DB2")) {
					driver = "com.ibm.db2.jdbc.app.DB2.Driver";
					url = "jdbc:db2://" + host + ":" + port + "/" + dbName;
				} else if(type.equalsIgnoreCase("SYBASE")) {
					driver="com.sybase.jdbc3.jdbc.SybDriver";
					url="jdbc:sybase:Tds:"+host+":"+port+"/"+dbName;
				} else
					return false;
				Class.forName(driver);// 加载数据库驱动
				Connection conn = DriverManager.getConnection(url, user, password);
				conn.close();
				return true;
			} else if (dataSourceType.equals(DriverConstant.DataSourceType_File)) {
				String urlStr = strs[1];
				String username = strs.length > 2 ? strs[2] : "anonymous";
				String password = strs.length > 3 ? strs[3] : "anonymous";
				URL url = new URL(urlStr);
				String host = url.getHost();
				int port = url.getPort() == -1 ? 21 : url.getPort();
				FTPClient ftpClient = new FTPClient();
				ftpClient.connect(host, port);
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);// 重要
				if (!ftpClient.login(username, password))
					return false;
				return true;
			} else if (dataSourceType.equals(DriverConstant.DataSourceType_WebService)) {
				// TODO
				return true;
			} else if (dataSourceType.equals(DriverConstant.DataSourceType_ParagraphEngine)) {
				// TODO
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static void main(String[] args) throws SocketException, IOException{
		String urlStr = "ftp://125.216.242.9/hello.txt";
		String username = "root";
		String password = "root";
		URL url = new URL(urlStr);
		String host = url.getHost();
		int port = url.getPort() == -1 ? 21 : url.getPort();
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(host, port);
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);// 重要
		if (!ftpClient.login(username, password)){
			System.out.println("false:");
		}else{
			System.out.println("true:");
		}
		URL url1 = new URL("ftp://125.216.242.9");
		String host1 = url1.getHost();
		System.out.println("host:"+host1);
		FTPClient ftpClient1 = new FTPClient();
		ftpClient1.connect("125.216.242.9", 21);
//		ftpClient1.setFileType(FTPClient.BINARY_FILE_TYPE);// 重要
		if (!ftpClient1.login("root", "root")){
			System.out.println("reply:"+ftpClient1.getReplyCode());
			System.out.println("ftpclient1,false:");
		}else{
			System.out.println("ftpclient1,true:");
		}
	}
}
