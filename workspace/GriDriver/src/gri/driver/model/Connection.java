package gri.driver.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import csAsc.EIB.Engine.Authentication;
import csAsc.EIB.Engine.EITPLocation;
import csAsc.EIB.Engine.Engine;
import csAsc.EIB.Engine.Order;
import csAsc.EIB.Engine.StatusCodeUtil;
import gri.driver.util.DriverConstant;

public class Connection {
	private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);

	private boolean using; // 正在使用中

	private String name;
	private String host;
	private String user;
	private String password;

	public Connection(String name, String host, String user, String password) {
		this.using = false;
		this.name = name;
		this.host = host; // "EITP://localhost:9010"
		this.user = user; // "aaa"
		this.password = password; // "123"
	}

	public boolean canConnect() {
		return execute_sync("test", this.user + " try to connect") != null;
	}

	// 同步执行指令
	public Object execute_sync(String operate, Object data) {
		LOGGER.info("开始发送同步消息");
		LOGGER.info("操作：{}", operate);
		LOGGER.info("数据：{}", data);
		Order response = new Order(); // 响应结果
		Engine eitp_engine = new Engine();
		eitp_engine.start(); // 启动引擎
		int ret = eitp_engine.once(new EITPLocation(this.host),
				new Authentication(Authentication.AT_BASIC, new String[] { this.user, this.password }),
				new Order(DriverConstant.GriEngineAccount, this.user, operate, data), response);
		eitp_engine.stop(); // 关闭引擎
		LOGGER.info("日志：[code:{}, message:{}]", ret, StatusCodeUtil.getMessage(ret));
		if (ret == 0) {
			LOGGER.info("结果：{}", response.data);
			return response.data;
		} else {
			LOGGER.info("结果：未收到");
			return null;
		}
	}

	public String getName() {
		return name;
	}

	public boolean isUsing() {
		return using;
	}

	public void setUsing(boolean using) {
		this.using = using;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
