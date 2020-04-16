package gri.driver.util;

import java.util.UUID;

import gri.driver.model.Connection;
import gri.driver.model.DataChangeMessage;

/**
 * 消息通知器
 * 
 * @author 许诺
 *
 */
public class Notifier {

	private String griEngineHost;

	/**
	 * 消息通知器
	 * 
	 * @param griEngineHost
	 *            数格引擎服务器地址<br>
	 *            例如“EITP://125.216.242.40”
	 */
	public Notifier(String griEngineHost) {
		super();
		this.griEngineHost = griEngineHost;
	}

	/**
	 * 发送文件数据变化通知
	 * 
	 * @param filePath
	 *            数据唯一访问标识
	 */
	public void sendFileDataChangeNotify(String filePath) {
		this.sendChangeNotify(new DataChangeMessage(DriverConstant.DataSourceType_File, filePath));
	}

	/**
	 * 发送数据库数据变化通知
	 * 
	 * @param tablePath
	 *            数据唯一访问标识 ，形式为"host###databaseName###tableName"
	 */
	public void sendDatabaseDataChangeNotify(String tablePath) {
		this.sendChangeNotify(new DataChangeMessage(DriverConstant.DataSourceType_Database, tablePath));
	}

	/**
	 * 发送网络服务数据变化通知
	 * 
	 * @param webURL
	 *            数据唯一访问标识
	 */
	public void sendWebDataChangeNotify(String webURL) {
		this.sendChangeNotify(new DataChangeMessage(DriverConstant.DataSourceType_WebService, webURL));
	}

	/**
	 * 发送段引擎数据变化通知
	 * 
	 * @param dataPath
	 *            数据唯一访问标识
	 */
	public void sendParagraphEngineDataChangeNotify(String dataPath) {
		this.sendChangeNotify(new DataChangeMessage(DriverConstant.DataSourceType_ParagraphEngine, dataPath));
	}

	/**
	 * 发送（通用）数据变化通知
	 * 
	 * @param message
	 *            DataChangeMessage
	 */
	private void sendChangeNotify(DataChangeMessage message) {
		this.getTempConnection().execute_sync(DriverConstant.OPERATE_DATA_CHANGE, message);
	}

	private Connection getTempConnection() {
		return new Connection("notifier connection", this.griEngineHost,
				DriverConstant.TempConnctionAccoutPrefix + UUID.randomUUID().toString(), "");
	}

}
