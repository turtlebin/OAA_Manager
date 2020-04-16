package gri.engine.service;

import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gri.driver.util.DriverConstant;
import gri.engine.util.EITPSender;
import gri.engine.util.PathHelper;

/**
 * 数据源数据操作
 * 
 */
public class DataSourceService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceService.class);

	private String dataSourceType;
	private String dataSourcePath;
	private Integer paragraphID;

	public DataSourceService(String dataSourceType, String dataSourcePath, Integer paragraphID) {
		super();
		this.dataSourceType = dataSourceType;
		this.dataSourcePath = dataSourcePath;
		this.paragraphID = paragraphID;
	}

	// 读数据源到缓存
	public boolean readDataSource(final CacheService cs) {
		LOGGER.info("read data source: [data source:{}]", this.dataSourcePath);
		OutputStream out = cs.getOutputStream();
		if (out == null)
			return false;
		// 文件型数据源
		if (this.dataSourceType.equals(DriverConstant.DataSourceType_File)) {
			boolean downSucceed = new FileDataService(this.dataSourcePath).download(out); // 更新缓存
			LOGGER.info("读File数据源：" + downSucceed);
			if (downSucceed) {
				LOGGER.info("从File数据源成功读取数据到缓存后，开始进行预览文件、索引文件更新...");
				new Thread() {
					public void run() {
						String extension = PathHelper.getExtension(dataSourcePath);
						ParseService ps = new ParseService(cs.getCacheUUID(), extension);
						ps.parse();// 更新预览文件

						IndexService is = new IndexService();
						is.delelteIndex(cs.getCacheUUID());
						is.createIndex(cs.getCacheUUID()); // 更新索引
					};
				}.start();
			}
			return downSucceed;
		}
		// 数据库型数据源
		else if (this.dataSourceType.equals(DriverConstant.DataSourceType_Database)) {
			boolean downSucceed = new DatabaseDataService(this.dataSourcePath,paragraphID).download(out);// 更新缓存
			LOGGER.info("读Database数据源：" + downSucceed);
			if (downSucceed) {
				LOGGER.info("从Database数据源成功读取数据到缓存后，开始进行预览文件、索引文件更新...");
				new Thread() {
					public void run() {
						ParseService ps = new ParseService(cs.getCacheUUID(), "db");
						ps.parse();// 更新预览文件
						IndexService is = new IndexService();
						is.delelteIndex(cs.getCacheUUID());
						is.createIndex(cs.getCacheUUID()); // 更新索引
					};
				}.start();
			}
			return downSucceed;
		}
		// Web型数据源
		else if (this.dataSourceType.equals(DriverConstant.DataSourceType_WebService)) {
			System.out.println("DataSourceService : WebService:line 77");
			boolean downSucceed = new WebDataService(this.dataSourcePath).download(out);// 更新缓存
			LOGGER.info("读web服务：" + downSucceed);
			if (downSucceed) {
				LOGGER.info("从web服务数据源成功读取数据到缓存后，开始进行预览文件、索引文件更新...");
				new Thread() {
					public void run() {
						ParseService ps = new ParseService(cs.getCacheUUID(), "txt");
						ps.parse();// 更新预览文件
						IndexService is = new IndexService();
						is.delelteIndex(cs.getCacheUUID());
						is.createIndex(cs.getCacheUUID()); // 更新索引
					};
				}.start();
			}
			return downSucceed;
		}
		// 段引擎型数据源
		else if (this.dataSourceType.equals(DriverConstant.DataSourceType_ParagraphEngine)) {
			boolean downSucceed = new ParagraphEngineDataService(this.dataSourcePath).download(out);// 更新缓存
			LOGGER.info("读ParagraphEngine数据源：" + downSucceed);
			if (downSucceed) {
				LOGGER.info("从ParagraphEngine数据源成功读取数据到缓存后，开始进行预览文件、索引文件更新...");
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
							ParseService ps = new ParseService(cs.getCacheUUID(), extensionName);
							ps.parse();// 更新预览文件
							IndexService is = new IndexService();
							is.delelteIndex(cs.getCacheUUID());
							is.createIndex(cs.getCacheUUID()); // 更新索引
						}
					};
				}.start();
			}
			return downSucceed;
		}
		// 数格引擎
		else if (this.dataSourceType.equals(DriverConstant.DataSourceType_GriEngine)) {
			GriEngineDataService griEngineDataService = new GriEngineDataService(this.dataSourcePath);
			boolean downSucceed = griEngineDataService.download(out);// 更新缓存
			LOGGER.info("读GriEngine数据源：" + downSucceed);
			if (downSucceed) {
				LOGGER.info("从GriEngine数据源成功读取数据到缓存后，开始进行预览文件、索引文件更新...");
				new Thread() {
					public void run() {
						ParseService ps = new ParseService(cs.getCacheUUID(), "txt");
						ps.parse();// 更新预览文件
						IndexService is = new IndexService();
						is.delelteIndex(cs.getCacheUUID());
						is.createIndex(cs.getCacheUUID()); // 更新索引
					};
				}.start();
			}
			return downSucceed;
		}
		// 视图
		else if (this.dataSourceType.equals(DriverConstant.DataSourceType_View)) {
			ViewDataService viewDataService = new ViewDataService(this.dataSourcePath);
			boolean downSucceed = viewDataService.download(out);// 更新缓存
			LOGGER.info("读视图数据源：" + downSucceed);
			if (downSucceed) {
				LOGGER.info("视图数据源成功读取数据到缓存后，开始进行预览文件、索引文件更新...");
				new Thread() {
					public void run() {
						ParseService ps = new ParseService(cs.getCacheUUID(), "txt");
						ps.parse();// 更新预览文件
						IndexService is = new IndexService();
						is.delelteIndex(cs.getCacheUUID());
						is.createIndex(cs.getCacheUUID()); // 更新索引
					};
				}.start();
			}
			return downSucceed;
		}
		return false;

	}

	// 从缓存写数据源
	public boolean writeDataSource(InputStream in) {
		LOGGER.info("write data source: [data source:{}]", this.dataSourcePath);
		if (in == null)
			return false;

		// 文件型数据源
		if (this.dataSourceType.equals(DriverConstant.DataSourceType_File)) {
			return new FileDataService(this.dataSourcePath).upload(in);
		}

		// 数据库型数据源
		else if (this.dataSourceType.equals(DriverConstant.DataSourceType_Database)) {
			return new DatabaseDataService(this.dataSourcePath,paragraphID).upload(in);
		}
		// Web型数据源
		else if (this.dataSourceType.equals(DriverConstant.DataSourceType_WebService)) {
			// web service只读
		}
		// 段引擎型数据源
		else if (this.dataSourceType.equals(DriverConstant.DataSourceType_ParagraphEngine)) {
			return new ParagraphEngineDataService(this.dataSourcePath).upload(in);
		}
		return false;
	}

	public String getDataSourceType() {
		return dataSourceType;
	}

	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public String getDataSourceURL() {
		return dataSourcePath;
	}

	public void setDataSourceURL(String dataSourceURL) {
		this.dataSourcePath = dataSourceURL;
	}

	@Override
	public String toString() {
		return "DataSourceManager [dataSourceType=" + dataSourceType + ", dataSourceURL=" + dataSourcePath + "]";
	}

}
