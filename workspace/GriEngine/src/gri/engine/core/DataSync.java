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
import gri.engine.service.CacheService;
import gri.engine.service.DataSourceService;
import gri.engine.util.DBHelper;

/**
 * 数据同步执行器<br>
 * <br>
 * 将数据从一侧同步至另一侧。只考虑方向。强制覆写同步。
 */
public class DataSync implements Runnable {//段1的定时同步
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSync.class);

	private Integer paragraphID;

	private boolean force; // 是否强制同步（不智能跳过）
	
	private boolean success;

	public DataSync(Integer paragraphID) {
		this.paragraphID = paragraphID;
		this.force = false;
		this.success = false;;
	}

	public DataSync(Integer paragraphID, boolean force) {
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
		if (direction.equals(DriverConstant.SyncDirectionType_1))
			sql = "update paragraph set last_sync_succeed='Y', cache_changed='N', last_sync_time=? where id=?";
		else
			sql = "update paragraph set last_sync_succeed='Y', datasource_changed='N', last_sync_time=? where id=?"; // 数据源->数格引擎
		Object[] obj = new Object[] { currentTimeStr, paragraphID };
		DBHelper.executeNonQuery(sql, obj);
		LOGGER.info(succeedMessage);
	}

	@Override
	public void run() {
		LOGGER.info("准备执行数据同步");
		LOGGER.info("Paragraph ID:{}", this.paragraphID);
		Connection conn=DBHelper.getConnection();
		String syncDirectionType = DriverConstant.SyncDirectionType_0;
		String sql="select * from paragraph where id=?";
		PreparedStatement ps=null;
		Connection conn2=DBHelper.getConnection();
		String sql2="select * from dest_info where paragraph_id=?";
		PreparedStatement ps2=null;
		try {
			ps = conn.prepareStatement(sql);
			ps2= conn2.prepareStatement(sql2);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, new Object[] { paragraphID });
		ResultSet rs2 = DBHelper.executeQuery(conn2,ps2,sql2, new Object[] { paragraphID });
		try {
			while (rs.next()&&rs2.next()) {
				String paragraphName=rs.getString("name");
				String dataSourceType = rs.getString("data_source_type");
				String dataSourcePath = rs.getString("data_source_path");
				String cacheUUID = rs.getString("cache");
				String destPath=rs2.getString("data_dest_type")+"###"+rs2.getString("data_dest_base")+"###"+rs2.getString("dest_account")+"###"+
						rs2.getString("dest_password")+"###"+rs2.getString("dest_IP")+"###"+rs2.getString("dest_port");
				syncDirectionType = rs.getString("sync_direction_type");
				boolean dataSourceChanged = rs.getString("datasource_changed").equals("Y");
				boolean cacheChanged = rs.getString("cache_changed").equals("Y");
				boolean lastSyncSucceed = rs.getString("last_sync_succeed").equals("Y");

				LOGGER.info(
						"dataSourceType:{}, dataSourcePath:{}, cacheUUID:{}, syncDirectionType:{}, dataSourceChanged:{}, cacheChanged:{}, lastSyncSucceed:{}",
						dataSourceType, dataSourcePath, cacheUUID, syncDirectionType, dataSourceChanged, cacheChanged,
						lastSyncSucceed);

				DataSourceService dsService = new DataSourceService(dataSourceType, dataSourcePath,paragraphID);
				CacheService cacheService = new CacheService(cacheUUID);

				// 双向同步按情况拆解为单向同步处理
				if (syncDirectionType.equals(DriverConstant.SyncDirectionType_2)) {
					if (cacheChanged && !dataSourceChanged)
						syncDirectionType = DriverConstant.SyncDirectionType_1;
					else
						syncDirectionType = DriverConstant.SyncDirectionType_0; // 主要以数据源数据为准。
				}
				if (syncDirectionType.equals(DriverConstant.SyncDirectionType_0)) {
						// 数据源->数格引擎
					try {
						new SyncHelper().joinSync(paragraphName, dataSourcePath, destPath, cacheUUID);
					}catch(Exception e) {
						e.printStackTrace();
					}
//						if (!dsService.readDataSource(cacheService)) {
//							this.executeFail("同步数据源数据至数格引擎失败");
//							success = false;
//							return;
//						}
//					 else {
//						LOGGER.info("数据源没有更改，智能跳过同步至数格引擎缓存");
//					}
				} else if (syncDirectionType.equals(DriverConstant.SyncDirectionType_1)) {
					if (this.force || cacheChanged || !lastSyncSucceed) {
						// 数格引擎->数据源
						if (!dsService.writeDataSource(cacheService.getInputStream())) {
							this.executeFail("同步数格引擎数据至数据源失败");
							success = false;
							return;
						}
					} else {
						LOGGER.info("数格引擎缓存没有更改，智能跳过同步至数数据源");
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
			DBHelper.free(rs2,ps2,conn2);
		}

		this.executeSucceed("数据同步执行成功", syncDirectionType);
		success = true;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
