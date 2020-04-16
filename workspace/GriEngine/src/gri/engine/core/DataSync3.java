package gri.engine.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gri.driver.util.DriverConstant;
import gri.engine.integrate.SyncHelper;
import gri.engine.integrate.SyncHelper2;
import gri.engine.integrate.SyncHelper3;
import gri.engine.service.CacheService;
import gri.engine.service.DataSourceService;
import gri.engine.util.DBHelper;

/**
 * 数据同步执行器<br>
 * <br>
 * 将数据从一侧同步至另一侧。只考虑方向。强制覆写同步。
 */
public class DataSync3 implements Runnable {//这个为段3的定时同步
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSync.class);

	private Integer paragraphID;

	private boolean force; // 是否强制同步（不智能跳过）
	
	private boolean success;

	public DataSync3(Integer paragraphID) {
		this.paragraphID = paragraphID;
		this.force = false;
		this.success = false;;
	}

	public DataSync3(Integer paragraphID, boolean force) {
		this.paragraphID = paragraphID;
		this.force = force;
	}

	private void executeFail(String failMessage) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTimeStr = sdf.format(new Date());
		String sql = "update paragraph set last_sync_succeed=?, last_sync_time=? where id=?";
		Object[] obj = new Object[] { "N", currentTimeStr, paragraphID };
		DBHelper.executeNonQuery(sql, obj);
		LOGGER.info(failMessage);
	}

	private void executeSucceed(String succeedMessage, String direction) {//如果执行同步成功时，要在格文档数据库执行的相应操作
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTimeStr = sdf.format(new Date());
		String sql = "";
		// 数格引擎->数据源
		sql = "update paragraph set last_sync_succeed='Y', datasource_changed='N', last_sync_time=? where id=?"; // 数据源->数格引擎
		Object[] obj = new Object[] { currentTimeStr, paragraphID };
		DBHelper.executeNonQuery(sql, obj);
		LOGGER.info(succeedMessage);
	}

	@Override
	public void run() {
		LOGGER.info("准备执行数据同步");
		LOGGER.info("Paragraph ID:{}", this.paragraphID);
		String syncDirectionType = DriverConstant.SyncDirectionType_0;
		Connection conn=DBHelper.getConnection();
		String sql="select * from paragraph where id=?";
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, new Object[] { paragraphID });
		try {
			while (rs.next()) {
				String paragraphName=rs.getString("name");
				String dataSourceType = rs.getString("data_source_type");
				String dataSourcePath = rs.getString("data_source_path");
				String cacheUUID = rs.getString("cache");
				syncDirectionType = rs.getString("sync_direction_type");
				boolean dataSourceChanged = rs.getString("datasource_changed").equals("Y");
				boolean cacheChanged = rs.getString("cache_changed").equals("Y");
				boolean lastSyncSucceed = rs.getString("last_sync_succeed").equals("Y");

				LOGGER.info(
						"dataSourceType:{}, dataSourcePath:{}, cacheUUID:{}, syncDirectionType:{}, dataSourceChanged:{}, cacheChanged:{}, lastSyncSucceed:{}",
						dataSourceType, dataSourcePath, cacheUUID, syncDirectionType, dataSourceChanged, cacheChanged,
						lastSyncSucceed);
				// 双向同步按情况拆解为单向同步处理
				if (this.force||syncDirectionType.equals(DriverConstant.SyncDirectionType_0)) {
						// 数据源->数格引擎
					try {
					success=new SyncHelper3().joinSync(paragraphName);//进行同步
					}
					catch(Exception e) 
					{
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			{
				this.executeFail("段属性读取失败");
				success = false;
				return;
			}
		} finally {
			DBHelper.free(rs);
			DBHelper.free(ps);
			DBHelper.free(conn);
		}
		if(success)
		this.executeSucceed("数据同步执行成功", syncDirectionType);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
