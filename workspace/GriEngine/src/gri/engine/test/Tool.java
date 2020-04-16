package gri.engine.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Random;

/**
 * @Description 
 * Created by weiwenjie on 2016-9-8
 */
public class Tool {
	public static void makeData(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/myTest?useUnicode=true&characterEncoding=UTF-8",
					"root", "014569");
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("insert into student values (null, ? , ?, ?, ?)");
			Random random = new Random();
			for(int i=0;i<100; i++){
				ps.setString(1,"学生"+(i+1));
				ps.setString(2, (random.nextInt(9)+1)+"年级");
				ps.setInt(3, random.nextInt(50)+80);
				ps.setInt(4, random.nextInt(40)+140);
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
			ps.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
