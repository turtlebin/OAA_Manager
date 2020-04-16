package gri.engine.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import csAsc.EIB.Engine.Authentication;
import csAsc.EIB.Engine.EITPLocation;
import csAsc.EIB.Engine.Engine;
import csAsc.EIB.Engine.Order;
import csAsc.EIB.Engine.StatusCodeUtil;

public class ParagraphEngineDataService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParagraphEngineDataService.class);

	private String dataSourcePath;

	private String eitp_host;
	private String eitp_username;
	private String eitp_password;
	// private String paragraphName;
	private String paragraphID;

	public ParagraphEngineDataService(String dataSourcePath) {
		super();
		this.dataSourcePath = dataSourcePath;
	}

	private boolean analyzeParameter() {
		try {
			String strs[] = this.dataSourcePath.split("###");
			this.eitp_host = strs[0];
			this.eitp_username = strs[1];
			this.eitp_password = strs[2];
			// this.paragraphName = strs[3];
			this.paragraphID = strs[4];
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean download(OutputStream out) {
		if (!analyzeParameter())
			return false;
		try {
			Object ret = execute_sync("read paragraph data", this.paragraphID);
			if (ret != null) {
				byte[] bytes = (byte[]) ret;
				out.write(bytes);
				out.close();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean upload(InputStream in) {
		if (!analyzeParameter())
			return false;
		// TODO
		return false;
	}

	// 同步执行指令
	private Object execute_sync(String operate, Object data) {
		LOGGER.info("开始发送同步消息");
		LOGGER.info("操作：{}", operate);
		LOGGER.info("数据：{}", data);
		Order response = new Order(); // 响应结果
		Engine eitp_engine = new Engine();
		eitp_engine.start(); // 启动引擎
		int ret = eitp_engine.once(new EITPLocation(this.eitp_host),
				new Authentication(Authentication.AT_BASIC, new String[] { this.eitp_username, this.eitp_password }),
				new Order("publisher", this.eitp_username, operate, data), response);
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
}
