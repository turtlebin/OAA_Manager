package gri.engine.service;

import gri.driver.manager.GriDocManager;
import gri.driver.model.Connection;
import gri.driver.model.process.Paragraph;
import gri.driver.util.DriverConstant;
import gri.engine.model.dao.ProcessDao;
import gri.engine.util.Constant;
import gri.engine.util.PathHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import csAsc.EIB.Engine.Authentication;
import csAsc.EIB.Engine.EITPLocation;
import csAsc.EIB.Engine.Engine;
import csAsc.EIB.Engine.Order;
import csAsc.EIB.Engine.StatusCodeUtil;

public class GriEngineDataService {
	private static final Logger LOGGER = LoggerFactory.getLogger(GriEngineDataService.class);

	private String dataSourcePath;

	private String eitp_host;
	private String eitp_username;
	private String eitp_password;
	private Integer paragraphID;

	public GriEngineDataService(String dataSourcePath) {
		super();
		this.dataSourcePath = dataSourcePath;
	}

	private boolean analyzeParameter() {
		try {
			String strs[] = this.dataSourcePath.split("###");
			this.eitp_host = strs[0];
			this.eitp_username = strs[1];
			this.eitp_password = strs[2];
			this.paragraphID = Integer.parseInt(strs[3]);
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
			//get real path
			String curPath = dataSourcePath;
			Paragraph paragraph = null;
			String cur_host = "";
			String cur_username = "";
			String cur_password = "";
			String cur_paragraphID = "";
			do{			
				String strs[] = curPath.split("###");
				cur_host = strs[0];
				cur_username = strs[1];
				cur_password = strs[2];
				cur_paragraphID = strs[3];
				
				
				if(PathHelper.isEITPLocalhost(cur_host)){
					paragraph = new ProcessDao().getParagraphById(Integer.parseInt(cur_paragraphID));
				}
				else{
					Connection conn=new Connection("新建连接", "EITP://"+cur_host, cur_username, cur_password);
					GriDocManager griDocManager = new GriDocManager(conn);
					paragraph = griDocManager.getParagraphById(Integer.parseInt(cur_paragraphID));						
				}	
				curPath = paragraph.getDataSourcePath();
			} while(paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_GriEngine));
			
			String preview = "";
			if(PathHelper.isEITPLocalhost(cur_host)){
				GriEngineService griEngineService= new GriEngineService();
				Integer paragraphId = Integer.parseInt(cur_paragraphID);
				griEngineService.dataSync(paragraphId);
				preview = griEngineService.readParagraphPreview(paragraphId);
			}
			else{
				Connection conn=new Connection("新建连接", "EITP://"+cur_host, cur_username, cur_password );
				GriDocManager griDocManager = new GriDocManager(conn);
				griDocManager.forceSyncData(Integer.parseInt(cur_paragraphID));
				preview = griDocManager.previewParagraphData(Integer.parseInt(cur_paragraphID));
			}
			
			byte[] bytes = preview.getBytes();
			out.write(bytes);
			out.close();
			return true;
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
	
	public Paragraph realParagraph() {
		String curPath = dataSourcePath;
		Paragraph paragraph = null;
		do{		
			
			String strs[] = curPath.split("###");
			String cur_host = strs[0];
			String cur_username = strs[1];
			String cur_password = strs[2];
			String cur_paragraphID = strs[3];
			if(PathHelper.isEITPLocalhost(cur_host)){
				paragraph = new ProcessDao().getParagraphById(Integer.parseInt(cur_paragraphID));
			}
			else{
				Connection conn=new Connection("新建连接", "EITP://"+cur_host, cur_username, cur_password);
				GriDocManager griDocManager = new GriDocManager(conn);
				paragraph = griDocManager.getParagraphById(Integer.parseInt(cur_paragraphID));						
			}	
			curPath = paragraph.getDataSourcePath();
		} while(paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_GriEngine));
		return paragraph;
	}
}
