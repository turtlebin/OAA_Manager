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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @Description 
 * Created by weiwenjie on 2017-4-17
 */
public class createDataWord {
	public static void main(String[] args) throws Exception {
		int RANDOM_MAX = 6;
		Random random = new Random();
		String[] word1s = new String[]{"hadoop","spark","storm","redis","ajax","kafka","java","php","ngnix","apache","bootstrap","andriod","mac"};
		List<String> word2s=new ArrayList<String>();
		for(int i = 0; i<word1s.length;i++){
			int times = random.nextInt(RANDOM_MAX) + 1;
			for(int j = 0 ; j<times ;j++) word2s.add(word1s[i]);
		}
		Collections.shuffle(word2s);
			
		
		String url = "jdbc:MySQL://localhost:3306/gridoc_test";// 数据库
		String user = "root"; // 用户名
		String password = "014569"; // 密码
		Class.forName("com.mysql.jdbc.Driver"); // 加载数据库驱动
		Connection conn = DriverManager.getConnection(url, user, password);

		for(int i =0; i<word2s.size(); i++){
			String sql = "insert into word values (?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setObject(1, word2s.get(i));
			pstmt.executeUpdate();
		}
		conn.close();
	}

		
}
