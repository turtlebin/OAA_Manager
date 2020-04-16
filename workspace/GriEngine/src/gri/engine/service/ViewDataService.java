package gri.engine.service;

import gri.driver.manager.GriDocManager;
import gri.driver.model.Connection;
import gri.driver.model.process.Paragraph;
import gri.driver.model.process.ProcessDto1;
import gri.driver.model.process.Processor;
import gri.driver.util.DriverConstant;
import gri.engine.model.dao.ProcessDao;
import gri.engine.util.Constant;
import gri.engine.util.PathHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import csAsc.EIB.Engine.Authentication;
import csAsc.EIB.Engine.EITPLocation;
import csAsc.EIB.Engine.Engine;
import csAsc.EIB.Engine.Order;
import csAsc.EIB.Engine.StatusCodeUtil;

public class ViewDataService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ViewDataService.class);

	private String dataSourcePath;

	private String eitp_host;
	private String eitp_username;
	private String eitp_password;
	private String containerName;
	private String viewName;
	//视图数据源的前置处理器
	private Integer processorId;

	public ViewDataService(String dataSourcePath) {
		super();
		this.dataSourcePath = dataSourcePath;
	}

	private boolean analyzeParameter() {
		try {
			String strs[] = this.dataSourcePath.split("###");
			this.eitp_host = strs[0];
			this.eitp_username = strs[1];
			this.eitp_password = strs[2];
			String [] views = strs[3].split("-");
			this.containerName = views[0];
			this.viewName = views[1];
			this.processorId = Integer.parseInt(strs[4]);
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
			String data ="";
			//如果是当前服务器，则直接调用。否则通过Connection类远程访问。
			if(PathHelper.isEITPLocalhost(eitp_host)){
				data =(String) new GriEngineService().readView(containerName, viewName);
			}
			else{
				Connection conn=new Connection("新建连接", "EITP://"+eitp_host, eitp_username, eitp_password);
				GriDocManager griDocManager = new GriDocManager(conn);
				data =(String) griDocManager.ProcessManager().readView(containerName, viewName);
			}
			
			
			ProcessDao processDao = new ProcessDao();
			Processor processor = processDao.getProcessorById(processorId);		
			String result = data;
			
			if(data!=null && processor!=null){
				Class<?> cls = Class.forName(processor.getClassName());
				Object virtualView = cls.newInstance();
				
				Method processFunc = cls.getMethod("process", String.class,String.class,String.class);
				GriEngineService griEngineService=new GriEngineService();
				processFunc.invoke(virtualView, data, "",  "[]");
				
				Method resultFunc = cls.getMethod("getResult", String.class);
				result = (String)resultFunc.invoke(virtualView, "");
			}
			
			byte[] bytes = result.getBytes();
			out.write(bytes);
			out.close();
			return true;
		} catch (Exception e) {
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
}
