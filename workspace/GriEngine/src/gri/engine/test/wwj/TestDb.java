/**
* Created by weiwenjie on 2017-4-17
*/
package gri.engine.test.wwj;

import gri.driver.manager.GriDocManager;
import gri.engine.util.Constant;
import gri.engine.util.DBHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

/**
 * @Description 
 * Created by weiwenjie on 2017-4-17
 */
public class TestDb {
	public static void main(String[] args) throws Exception {
		String url = "jdbc:MySQL://localhost:3306/gxsj";// 数据库
		String user = "root"; // 用户名
		String password = "csasc"; // 密码
		Class.forName("com.mysql.jdbc.Driver"); // 加载数据库驱动
		Connection conn = DriverManager.getConnection(url, user, password);
		
		PreparedStatement ps=conn.prepareStatement("select * from ta_jxgl_cj", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		ps.setFetchSize(Integer.MIN_VALUE);
        ps.setFetchDirection(ResultSet.FETCH_REVERSE);
		ResultSet rs = ps.executeQuery();
		int count =0;
		while (rs.next()) { 
			count ++;
		}
		System.out.println("finish:");
		System.out.println(count);
		conn.close();
	}

		
}
