package gri.engine.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Description 
 * Created by weiwenjie on 2016-9-8
 */
public class ToJson {
	public static JSONArray fromMysql(String tableName){
		JSONArray result =new JSONArray();
		Connection conn;
		PreparedStatement ps;
		ResultSet rs;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/myTest?useUnicode=true&characterEncoding=UTF-8",
					"root", "014569");
			ps = conn.prepareStatement("select * from "+tableName);
			rs=ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				 JSONObject obj =new JSONObject();
				 for(int i =1;i<=rsmd.getColumnCount();i++){
					 obj.accumulate(rsmd.getColumnName(i), toString(rs.getObject(i)));
				 }
				 result.add(obj);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(result.toString());
		return result;
		
	}
	
	public static String toString(Object obj){
		if(obj==null) return "";
		else return obj.toString();
	}
}
