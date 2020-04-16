package gri.engine.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gri.driver.util.DriverConstant;
import gri.engine.model.DataChangeEvent;
import gri.engine.service.CacheService;
import gri.engine.service.IndexService;
import gri.engine.service.ParseService;
import gri.engine.util.DBHelper;
import gri.engine.util.EITPSender;
import gri.engine.util.PathHelper;

public class DataChangeListener implements EventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataChangeListener.class);

	private CacheService cacheService = null;

	private String sync_time_type = "";
	private String syncDirection = "";
	private String dataSourceType = "";
	private String dataSourcePath = "";

	public void DataChanged(DataChangeEvent event) {
		cacheService = (CacheService) event.getSource();
		LOGGER.info("用户上传数据修改了缓存，缓存详情为：{}", cacheService);
		String sql = "update paragraph set cache_changed='Y' where id=?"; // 标记更改
		Object[] obj = new Object[] { cacheService.getParagraphID() };
		DBHelper.executeNonQuery(sql, obj);

		String sql2 = "select * from paragraph where id=?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql2, obj);

		try {
			while (rs.next()) {
				sync_time_type = rs.getString("sync_time_type");
				syncDirection = rs.getString("sync_direction_type");
				dataSourceType = rs.getString("data_source_type");
				dataSourcePath = rs.getString("data_source_path");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}

		if (dataSourceType.equals(DriverConstant.DataSourceType_File)) {
			LOGGER.info("用户更改File缓存后，开始进行预览文件、索引文件更新...");
			new Thread() {
				public void run() {
					// 等待用户把数据上传完毕。
					try {
						Thread.sleep(30 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					String extension = PathHelper.getExtension(dataSourcePath);
					ParseService ps = new ParseService(cacheService.getCacheUUID(), extension);
					ps.parse();// 更新预览文件

					IndexService is = new IndexService();
					is.delelteIndex(cacheService.getCacheUUID());
					is.createIndex(cacheService.getCacheUUID()); // 更新索引
				};
			}.start();
		} else if (dataSourceType.equals(DriverConstant.DataSourceType_Database)) {
			LOGGER.info("用户更改Database缓存后，开始进行预览文件、索引文件更新...");
			new Thread() {
				public void run() {
					ParseService ps = new ParseService(cacheService.getCacheUUID(), "db");
					ps.parse();// 更新预览文件
					IndexService is = new IndexService();
					is.delelteIndex(cacheService.getCacheUUID());
					is.createIndex(cacheService.getCacheUUID()); // 更新索引
				};
			}.start();
		} else if (dataSourceType.equals(DriverConstant.DataSourceType_WebService)) {
			// TODO
		} else if (dataSourceType.equals(DriverConstant.DataSourceType_ParagraphEngine)) {
			LOGGER.info("用户更改ParagraphEngine缓存后，开始进行预览文件、索引文件更新...");
			new Thread() {
				public void run() {
					String host = dataSourcePath.split("###")[0];
					String user = dataSourcePath.split("###")[1];
					String password = dataSourcePath.split("###")[2];
					// String name = dataSourcePath.split("###")[3];
					String id = dataSourcePath.split("###")[4];
					Object result = EITPSender.sendRequest(host, user, password, "publisher",
							"get paragraph extension name", id);
					if (result != null) {
						String extensionName = (String) result;
						ParseService ps = new ParseService(cacheService.getCacheUUID(), extensionName);
						ps.parse();// 更新预览文件
						IndexService is = new IndexService();
						is.delelteIndex(cacheService.getCacheUUID());
						is.createIndex(cacheService.getCacheUUID()); // 更新索引
					}
				};
			}.start();
		}

		// 实时 && （缓存->数据源）
		if (sync_time_type.equals(DriverConstant.SyncTimeType_0)
				&& !syncDirection.equals(DriverConstant.SyncDirectionType_0)) {
			new DataSync(cacheService.getParagraphID()).run();
		}

	}

}
