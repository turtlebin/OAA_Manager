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
import java.sql.SQLException;
import java.util.Random;

/**
 * @Description 
 * Created by weiwenjie on 2017-4-17
 */
public class createData {
	public static void main(String[] args) throws Exception {
		String url = "jdbc:MySQL://localhost:3306/gridoc_test";// 数据库
		String user = "root"; // 用户名
		String password = "014569"; // 密码
		Class.forName("com.mysql.jdbc.Driver"); // 加载数据库驱动
		Connection conn = DriverManager.getConnection(url, user, password);
		
		Random random=new Random();
		int max =50;
		int min =20;
		for(int i =0; i<1; i++){
			for(int j =1994; j<2018; j++){
				String sql = "insert into sale values (null, ? ,? ,? )";
				
				Object[] obj = new Object[] { "北京总部", j, random.nextInt(max)%(max-min+1) + min};
				
				PreparedStatement pstmt = conn.prepareStatement(sql);
				for (int k = 0; k < obj.length; k++) {
					pstmt.setObject(k + 1, obj[k]);
				}
				pstmt.executeUpdate();
			}
		}
		

		conn.close();
	}

		
}
