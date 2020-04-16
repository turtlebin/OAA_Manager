/**
* Created by weiwenjie on 2017-5-21
*/
package gri.engine.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gri.driver.model.PageDto;
import gri.driver.model.ParagraphDataReadRequest;
import gri.driver.model.ParagraphDataReadResponse;
import gri.driver.model.process.Paragraph;
import gri.driver.model.process.ProcessDto1;
import gri.driver.model.process.Processor;
import gri.driver.model.process.View;
import gri.driver.util.DriverConstant;

import gri.engine.core.DataSync;
import gri.engine.core.DataSync2;
import gri.engine.core.DataSync3;
import gri.engine.model.dao.ProcessDao;
import gri.engine.util.Constant;
import gri.engine.util.DBHelper;
import gri.engine.util.SQLParser;


/**
 * @Description 
 * Created by weiwenjie on 2017-5-21
 */
public class GriEngineService {
	private static final Logger LOGGER = LoggerFactory.getLogger(GriEngineService.class);
	public int readParagraphSize(int paragraphId){
		Paragraph paragraph = new ProcessDao().getParagraphById(paragraphId);//通过ID获取一个段
		if (paragraph==null) return 0;
		CacheService cacheManaer = new CacheService(paragraph.getCache());
		return cacheManaer.cacheSize();
	}

	public String readParagraphPreview(int paragraphId){
		Paragraph paragraph = new ProcessDao().getParagraphById(paragraphId);
		if (paragraph==null) return null;
		CacheService cacheManaer = new CacheService(paragraph.getCache());
		String text =cacheManaer.readParagraphPreview();
		if(text==null) text="缓存不存在";
		return text;
	}
	
	//eitp能传的包大小有限，这个方法用于分片传输
	public ParagraphDataReadResponse readParagraphByByte(ParagraphDataReadRequest request){
		Integer paragraphId = request.paragraphID;
		Paragraph paragraph = new ProcessDao().getParagraphById(paragraphId);
		if (paragraph==null) new ParagraphDataReadResponse(new byte[0], -1);
		byte[] bytes = new byte[request.size];
		CacheService cacheManaer = new CacheService(paragraph.getCache());
		int size = cacheManaer.readCache(bytes, request.positon);
		return new ParagraphDataReadResponse(bytes, size);
	}
	
	public boolean dataSync(int paragraphId) {
		Connection conn = DBHelper.getConnection();
		String sql = "select isParagraph3,isParagraph2 from paragraph where id=?";

		PreparedStatement pstmt = null;
		ResultSet rs=DBHelper.executeQuery(conn, pstmt, sql, paragraphId);
		try {
			while(rs.next())
			{
				if(rs.getBoolean("isParagraph3"))
				{
					DataSync2 dataSync2=new DataSync2(paragraphId,true);
					dataSync2.run();
					return dataSync2.isSuccess();
				}
				else if(rs.getBoolean("isParagraph2"))
				{
					DataSync3 dataSync3=new DataSync3(paragraphId,true);
					dataSync3.run();
					return dataSync3.isSuccess();
				}
				else
				{
					DataSync dataSync = new DataSync(paragraphId, true);// 执行数据同步，只需要知道段ID，从段ID可以搜索数据库，获得段的数据源等全部信息。
					dataSync.run();
					return dataSync.isSuccess();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally {
			DBHelper.free(rs, pstmt, conn);
		}
		return false;
	}

	//用于外部通知数格引擎更新某个数据源
	public void handleDataChange(String dataSourceType, String dataSourcePath){
		Map<Integer, String> id2syncType = new HashMap<Integer, String>(); // 记录受影响的段

		if (dataSourceType.equals(DriverConstant.DataSourceType_Database)) {
			id2syncType.clear();

			LOGGER.info("收到数据库数据源变更通知，正在标记受影响段...");
			String dbHost = dataSourcePath.split("###")[0];
			String dbName = dataSourcePath.split("###")[1];
			String tableName = dataSourcePath.split("###")[2];

			String sql = "select * from paragraph where data_source_type=? and datasource_changed=?";
			LOGGER.info("sql:" + sql);
			Object[] obj = new Object[] { DriverConstant.DataSourceType_Database, "N" };
			Connection conn=DBHelper.getConnection();
			PreparedStatement ps=null;
			try {
				ps = conn.prepareStatement(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultSet rs = DBHelper.executeQuery(conn,ps,sql, obj);

			try {
				while (rs.next()) {
					LOGGER.info("识别是否是数据源关联段……");
					boolean find = false;
					String path = rs.getString("data_source_path");
					String sync_time_type = rs.getString("sync_time_type");
					Integer id = rs.getInt("id");
					// path格式:
					// type###host###port###database###user###password###sql
					String[] strs = path.split("###");
					if (strs.length < 7)
						continue;

					String host = strs[1];
					if (host.equalsIgnoreCase("localhost")) {
						try {
							InetAddress addr = InetAddress.getLocalHost();
							host = addr.getHostAddress();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					if (!host.equals(dbHost)) // 对比数据主机
						continue;

					if (!strs[3].equals(dbName)) // 比对数据库名称
						continue;
					for (String tb : SQLParser.getTables(strs[6])) {
						if (tb.equals(tableName)) {
							find = true;
							break;
						}
					}
					if (find)
						id2syncType.put(id, sync_time_type);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DBHelper.free(rs,ps,conn);
			}

		} else if (dataSourceType.equals(DriverConstant.DataSourceType_File)) {
			id2syncType.clear();

			LOGGER.info("收到文件数据源变更通知，正在初步标记受影响段...");
			String host = dataSourcePath.split("###")[0];
			String fileName = dataSourcePath.split("###")[1];

			String sql = "select * from paragraph where data_source_type=? and datasource_changed=?";
			LOGGER.info("sql:" + sql);
			Object[] obj = new Object[] { DriverConstant.DataSourceType_File, "N" };
			Connection conn=DBHelper.getConnection();
			PreparedStatement ps=null;
			try {
				ps = conn.prepareStatement(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultSet rs = DBHelper.executeQuery(conn,ps,sql, obj);

			try {
				while (rs.next()) {
					LOGGER.info("二次识别是否是数据源关联段……");
					String path = rs.getString("data_source_path");
					String sync_time_type = rs.getString("sync_time_type");
					Integer id = rs.getInt("id");
					// path格式:
					// filePath[###user###password]
					String[] strs = path.split("###");
					if (strs.length < 1)
						continue;
					// 比对主机
					if (strs[0].toLowerCase().contains("localhost")) {
						String ip = "localhost";
						try {
							InetAddress addr = InetAddress.getLocalHost();
							ip = addr.getHostAddress();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						if (!ip.equals(host))
							continue;
					} else if (!strs[0].contains(host)) // 比对主机
						continue;
					if (!strs[0].endsWith(fileName)) // 比对文件名
						continue;
					LOGGER.info("找到了一个受影响段……");
					id2syncType.put(id, sync_time_type); // 找到了受影响段
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DBHelper.free(rs,ps,conn);
			}

		} else if (dataSourceType.equals(DriverConstant.DataSourceType_WebService)) {
			// TODO
		} else if (dataSourceType.equals(DriverConstant.DataSourceType_ParagraphEngine)) {
			id2syncType.clear();
			LOGGER.info("收到文件数据源变更通知，正在初步标记受影响段...");
			// dataSourcePath=125.216.242.40&test7&#12:768
			String host = dataSourcePath.split("&")[0];
			String fileName = dataSourcePath.split("&")[1];
			String fileID = dataSourcePath.split("&")[2];

			String sql = "select * from paragraph where data_source_type=? and datasource_changed=?";
			LOGGER.info("sql:" + sql);
			Object[] obj = new Object[] { DriverConstant.DataSourceType_ParagraphEngine, "N" };
			Connection conn=DBHelper.getConnection();
			PreparedStatement ps=null;
			try {
				ps = conn.prepareStatement(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultSet rs = DBHelper.executeQuery(conn,ps,sql, obj);

			try {
				while (rs.next()) {
					LOGGER.info("二次识别是否是数据源关联段……");
					String path = rs.getString("data_source_path");
					String sync_time_type = rs.getString("sync_time_type");
					Integer id = rs.getInt("id");
					// path格式:
					// EITP://125.216.242.40:9010###client###123###test6####12:761
					String[] strs = path.split("###");
					if (strs.length < 5)
						continue;
					// 比对主机
					if (strs[0].toLowerCase().contains("localhost")) {
						String ip = "localhost";
						try {
							InetAddress addr = InetAddress.getLocalHost();
							ip = addr.getHostAddress();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						if (!ip.equals(host))
							continue;
					} else if (!strs[0].contains(host)) // 比对主机
						continue;
					if (!strs[3].endsWith(fileName)) // 比对文件名
						continue;
					if (!strs[4].endsWith(fileID)) // 比对文件ID
						continue;
					LOGGER.info("找到了一个受影响段……");
					id2syncType.put(id, sync_time_type); // 找到了受影响段
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DBHelper.free(rs,ps,conn);
			}

		}

		// 执行更新数据更改标记、实时同步的段数据额外执行数据同步
		if (id2syncType.size() > 0) {
			LOGGER.info("更新数据源更改标志，受影响数据段个数为：" + id2syncType.size());
			Integer[] ids = id2syncType.keySet().toArray(new Integer[0]);

			String idsStr = "(" + ids[0];
			for (int i = 1; i < ids.length; i++)
				idsStr += "," + ids[i];
			idsStr += ")";

			String sql_add_change_flag = "update  paragraph  set datasource_changed ='Y' where id in "
					+ idsStr;
			LOGGER.info("sql:" + sql_add_change_flag);
			LOGGER.info(sql_add_change_flag);
			DBHelper.executeNonQuery(sql_add_change_flag);

			for (Integer id : id2syncType.keySet())
				if (id2syncType.get(id).equals(DriverConstant.SyncTimeType_0)) {
					LOGGER.info("段" + id + "为实时同步！");
					LOGGER.info("触发数据同步任务");
					new DataSync(id).run();
				}
		}

	}

	public Object readView(String containerName, String viewName){
		Object result = null;
		View view = getView(containerName,viewName);
		if(view !=null){
			//现在不支持增量采集，所以重新采集一次数据
			if(view.getIncrease()){
				RedisContainerService redisViewService = new RedisContainerService(view.getContainerName());
				redisViewService.cleanView();
				List<Integer> ls = new ProcessDao().listParagraphIdByContianerName(view.getContainerName());
				for(Integer id :ls) new DataSync(id, true).run();
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}								
				
				try {
					Class<?> cls = Class.forName(view.getViewClassName());
					Object virtualView = cls.newInstance();
					Method resultFunc = cls.getMethod("result", String.class,String.class);
					result =resultFunc.invoke(virtualView, view.getContainerName(),  view.getSubViewName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else{
				try {
					Class<?> cls = Class.forName(view.getProcessorClassName());
					Object virtualView = cls.newInstance();
					
					Method processFunc = cls.getMethod("process", String.class,String.class,String.class);
					GriEngineService griEngineService=new GriEngineService();
					List<ProcessDto1> processes = view.getProcesses();
					for(int i=0;i<processes.size();i++){
						griEngineService.dataSync(processes.get(i).getParagraphId());
						String data = griEngineService.readParagraphPreview(processes.get(i).getParagraphId());
						processFunc.invoke(virtualView, data, view.getSubViewName(),  processes.get(i).getConfig());
					}
					
					Method resultFunc = cls.getMethod("getResult", String.class);
					result = resultFunc.invoke(virtualView, view.getSubViewName());					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
		} 
		return result;
	}

	public List<View> listView(int containerId){
		List<View> ls = null;
		
		ProcessDao processDao = new ProcessDao();
		Processor processor=processDao.getProcessorByContainer(containerId);
		
		List<ProcessDto1> processes = processDao.getProcessByContainerId(containerId);
		String config="[]";
		if(processes.size()>0) config = processes.get(0).getConfig();
		try {
			Class<?> cls = Class.forName(processor.getClassName());
			Object virtualView = cls.newInstance();		
			Method processFunc = cls.getMethod("getViews", String.class);
			ls = (List<View>) processFunc.invoke(virtualView, config);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ls;
	}
	
	public View getView(String containerName, String viewName){
		View view = new ProcessDao().getView(containerName);
		
		if(view!=null){
			List<View> views = listView(view.getContainerId());
			for(int i = 0;i<views.size();i++)
				if(views.get(i).getSubViewName().equals(viewName)){
					view.setSubViewName(views.get(i).getSubViewName());
					view.setStore(views.get(i).getStore());
					view.setStructure(views.get(i).getStructure());
					view.setVirtual(views.get(i).isVirtual());
					break;
				}
		}
		
		return view;
	}
	
	public PageDto readDbPage(Integer paragraphId, Integer pageNo, Integer pageSize){
		Paragraph paragraph = new ProcessDao().getParagraphById(paragraphId);
		if(paragraph == null) return null;
		File file = new File(Constant.CacheFolder + paragraph.getCache());

		//读取[startRecord,endRecord)的记录，记录数由0开始计算
		int startRecord = (pageNo-1)*pageSize;
		int endRecord = (pageNo)*pageSize;
		StringBuffer sb = new StringBuffer();
		int counter = 0;
		try {
			
			int c;
			Reader reader = new FileReader(file);
			reader = new BufferedReader(reader);

		while((c=reader.read())!=-1){
			//important！！
		  if(((char)c==',')&&((char)reader.read()=='"'&&((char)reader.read()=='d')&&((char)reader.read()=='a')&&((char)reader.read()=='t')&&((char)reader.read()=='a')&&((char)reader.read()=='"')&&((char)reader.read()==':'))){
		    //利用java的&&操作符特性，找到“，“data”：”字符串，然后进行操作	
			//跳过开始的[
			reader.read();
			sb.append('[');
			//以},为标记识别一条记录,用flag来控制状态机
			boolean flag = false;
			while ((c = reader.read()) != -1){
				if(counter>=startRecord) sb.append((char) c);
				if(!flag){
					if(c=='}') flag = true;
				}
				else{
					if(c==',' || c==']') counter ++;
					flag = false;
				}
				if(counter>=endRecord) {
					sb.append("}");
					break;
				}
			}break;
			
		  }
		}
        } catch (Exception e) {    
    	   e.printStackTrace();  
    	   return null;
        }
		String jsObj = "";
		
		if(sb.length()>1) jsObj=sb.substring(0, sb.length()-1);
		else jsObj = "[]";
		
		PageDto pageDto = new PageDto();
		System.out.println(jsObj);
		pageDto.setData(jsObj);
		//pageDto.setTotalRecord(DatabaseDataService.getRecordCount(paragraphId));
		return pageDto;
	}
}
