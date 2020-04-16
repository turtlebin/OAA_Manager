package gri.engine.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gri.engine.util.DBHelper;

/**
 * 单个 段缓存重新初始化执行器。保证缓存文件存在。缓存数据大小为0时，重新同步创建缓存。
 */
public class CacheInit implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheInit.class);

	private Integer paragraphID;
	private String cacheUUID;
	private String dataSourceType;
	private String dataSourcePath;

	public CacheInit(Integer paragraphID, String cacheUUID, String dataSourceType, String dataSourcePath) {
		this.paragraphID = paragraphID;
		this.cacheUUID = cacheUUID;
		this.dataSourceType = dataSourceType;
		this.dataSourcePath = dataSourcePath;
	}

	private void executeFail(String failMessage) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTimeStr = sdf.format(new Date());
		String sql = "update paragraph set last_sync_succeed=?, last_sync_time=? where id=?";
		Object[] obj = new Object[] { "N", currentTimeStr, paragraphID };
		DBHelper.executeNonQuery(sql, obj);
		LOGGER.info(failMessage);
	}

	private void executeSucceed(String succeedMessage) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTimeStr = sdf.format(new Date());
		String sql = "update paragraph set last_sync_succeed=?, last_sync_time=? where id=?";
		Object[] obj = new Object[] { "Y", currentTimeStr, paragraphID };
		DBHelper.executeNonQuery(sql, obj);
		LOGGER.info(succeedMessage);
	}

	@Override
	public void run() {
		LOGGER.info("开始初始化缓存数据");// 缓存文件赋值：数据源->数格引擎
		LOGGER.info("cache file UUID:{}", cacheUUID);
		DataSourceService dsService = new DataSourceService(dataSourceType, dataSourcePath, paragraphID);
		CacheService cacheService = new CacheService(cacheUUID);
		if (cacheService.cacheSize() == 0) {
			//缓存不存在，重新生成缓存
			if (!dsService.readDataSource(cacheService)) {
				this.executeFail("缓存数据初始化失败");
				return;
			}
		} else {
			LOGGER.info("缓存已存在，智能跳过初始化");
		}
		this.executeSucceed("段缓存初始化成功");
	}

}
