package gri.engine.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import gri.driver.util.DriverConstant;
import gri.engine.core.DataSync;
import gri.engine.util.DBHelper;

public class TimerTask extends java.util.TimerTask {

	private Integer paragraphID;

	@Override
	public void run() {
		String syncTimeType = "";
		String sql = "select * from paragraph where id = ?";
		Object[] obj = new Object[] { paragraphID };
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, obj);
		try {
			while (rs.next()) {
				syncTimeType = rs.getString("sync_time_type");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		if (!syncTimeType.equals(DriverConstant.SyncTimeType_1))
			return;

		new DataSync(paragraphID).run();

	}

	public TimerTask(Integer paragraphID) {
		super();
		this.paragraphID = paragraphID;
	}

}
