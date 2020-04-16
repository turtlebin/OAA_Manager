/**
* Created by weiwenjie on 2017-3-23
*/
package gri.engine.model.dao;

import gri.driver.model.User;
import gri.driver.model.process.Container;
import gri.driver.model.process.Paragraph;
import gri.driver.model.process.ProcessDto1;
import gri.driver.model.process.Processor;
import gri.driver.model.process.View;
import gri.driver.model.process.Process;
import gri.engine.service.CacheService;
import gri.engine.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-23
 */
public class ProcessDao {
	//*****paragraph*********
	public Paragraph getParagraphById(Integer id){
		Paragraph paragraph = null;
		String sql = "select * from paragraph where id = ?";//此处的sql语句是from paragraph，但是paragraph这个表在哪里维护
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql ,new Object[]{id});
		try {
			if (rs.next()) {
				paragraph = new Paragraph();
				paragraph.setId(rs.getInt("id"));
				paragraph.setName(rs.getString("name"));
				paragraph.setKeywords(rs.getString("keywords"));
				paragraph.setDescription(rs.getString("description"));
				paragraph.setSyncTimeType(rs.getString("sync_time_type"));
				paragraph.setSyncDirectionType(rs.getString("sync_direction_type"));
				paragraph.setWarmSyncDetail(rs.getString("warm_sync_detail"));
				paragraph.setDataSourceType(rs.getString("data_source_type"));
				paragraph.setDataSourcePath(rs.getString("data_source_path"));
				paragraph.setLastSyncTime(new Date (rs.getDate("update_time").getTime()));
				paragraph.setCache(rs.getString("cache"));
				paragraph.setLastSyncSucceed(rs.getString("last_sync_succeed").equals("Y"));
				paragraph.setLastSyncTime(new Date (rs.getDate("last_sync_time").getTime()));
				paragraph.setDatasourceChanged(rs.getString("datasource_changed"));
				paragraph.setCacheChanged(rs.getString("cache_changed"));
				paragraph.setIncrease(rs.getBoolean("increase"));
				paragraph.setDataSize(new CacheService(paragraph.getCache()).cacheSize());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return paragraph;
	}
	
	public Paragraph getParagraphByName(String name){
		Paragraph paragraph = null;
		String sql = "select * from paragraph where name = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql ,new Object[]{name});
		try {
			if (rs.next()) {
				paragraph = new Paragraph();
				paragraph.setId(rs.getInt("id"));
				paragraph.setName(rs.getString("name"));
				paragraph.setKeywords(rs.getString("keywords"));
				paragraph.setDescription(rs.getString("description"));
				paragraph.setSyncTimeType(rs.getString("sync_time_type"));
				paragraph.setSyncDirectionType(rs.getString("sync_direction_type"));
				paragraph.setWarmSyncDetail(rs.getString("warm_sync_detail"));
				paragraph.setDataSourceType(rs.getString("data_source_type"));
				paragraph.setDataSourcePath(rs.getString("data_source_path"));
				paragraph.setLastSyncTime(new Date (rs.getDate("update_time").getTime()));
				paragraph.setCache(rs.getString("cache"));
				paragraph.setLastSyncSucceed(rs.getString("last_sync_succeed").equals("Y"));
				paragraph.setLastSyncTime(new Date (rs.getDate("last_sync_time").getTime()));
				paragraph.setDatasourceChanged(rs.getString("datasource_changed"));
				paragraph.setCacheChanged(rs.getString("cache_changed"));
				paragraph.setIncrease(rs.getBoolean("increase"));
				paragraph.setDataSize(new CacheService(paragraph.getCache()).cacheSize());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return paragraph;
	}
	
	//*****processor*********
	public List<Processor> listProcessor(){
		List<Processor> ls = new ArrayList<Processor>();
		String sql = "select * from processor";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql);
		try {
			while (rs.next()) {
				Processor processor = new Processor();
				processor.setId(rs.getInt("id"));
				processor.setName(rs.getString("name"));
				processor.setClassName(rs.getString("className"));
				processor.setState(rs.getString("state"));
				processor.setConfigStruct(rs.getString("configStruct"));
				processor.setViewClass(rs.getString("viewClass"));
				
				ls.add(processor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return ls;
	}
	
	public Processor getProcessorById(int id){
		Processor processor = null;
		String sql = "select * from processor where id = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql ,new Object[]{id});
		try {
			if (rs.next()) {
				processor = new Processor();
				processor.setId(rs.getInt("id"));
				processor.setName(rs.getString("name"));
				processor.setClassName(rs.getString("className"));
				processor.setState(rs.getString("state"));
				processor.setConfigStruct(rs.getString("configStruct"));
				processor.setViewClass(rs.getString("viewClass"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return processor;
	}
	
	public Processor getProcessorByName(String name){
		Processor processor = null;
		String sql = "select * from processor where name = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql ,new Object[]{name});
		try {
			if (rs.next()) {
				processor = new Processor();
				processor.setId(rs.getInt("id"));
				processor.setName(rs.getString("name"));
				processor.setClassName(rs.getString("className"));
				processor.setState(rs.getString("state"));
				processor.setConfigStruct(rs.getString("configStruct"));
				processor.setViewClass(rs.getString("viewClass"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return processor;
	}
	
	public boolean addProcessor(Processor processor){
		boolean result = false;
		
		String sql = "insert into processor values (null, ? ,? ,? ,?, ?)";
		Object[] obj = new Object[] { processor.getName(), processor.getClassName(), processor.getState(), processor.getConfigStruct(), processor.getViewClass() };
		result = DBHelper.executeNonQuery(sql, obj) > 0;
		return result;
	}
	
	public boolean updateProcessor(Processor processor){
		boolean result = false;
		
		String sql = "update processor set name=? ,className=? ,state=? ,configStruct=?, viewClass=? where id =?";
		Object[] obj = new Object[] { processor.getName(), processor.getClassName(), processor.getState(), processor.getConfigStruct(), processor.getViewClass(), processor.getId()};
		result = DBHelper.executeNonQuery(sql, obj) > 0;
		return result;
	}
	
	public boolean delProcessor(Integer id){
		boolean result = false;
		
		String sql = "delete from processor where id = ?";
		Object[] obj = new Object[] { id};
		result = DBHelper.executeNonQuery(sql, obj) > 0;
		return result;
	}
	
	public Processor getProcessorByContainer(int containerId){
		Processor processor = null;
		String sql = "select * from processor " +
				"inner join container on container.processorId = processor.id " +
				"where container.id = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql ,new Object[]{containerId});
		try {
			if (rs.next()) {
				processor = new Processor();
				processor.setId(rs.getInt("id"));
				processor.setName(rs.getString("name"));
				processor.setClassName(rs.getString("className"));
				processor.setState(rs.getString("state"));
				processor.setConfigStruct(rs.getString("configStruct"));
				processor.setViewClass(rs.getString("viewClass"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return processor;
	}
	
	//*****container*********
	public List<Container> listContainer(String account){
		List<Container> ls = new ArrayList<Container>();
		String sql = "select container.*,account as userName, processor.name as processorName " +
				"from container inner join `user` on container.userId = `user`.id " +
				"inner join processor on container.processorId = processor.id " +
				"where account = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql,new Object[]{account});
		try {
			while (rs.next()) {
				Container container = new Container();
				container.setId(rs.getInt("id"));
				container.setName(rs.getString("name"));
				container.setUserId(rs.getInt("userId"));
				container.setProcessorId(rs.getInt("processorId"));		
				container.setUserName(rs.getString("userName"));	
				container.setProcessorName(rs.getString("processorName"));
				ls.add(container);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return ls;
	}
	
	public List<Container> listContianerByProcessor(String account, Integer processorId){
		List<Container> ls = new ArrayList<Container>();
		String sql = "select container.*,account as userName, processor.name as processorName " +
				"from container inner join `user` on container.userId = `user`.id " +
				"inner join processor on container.processorId = processor.id " +
				"where account = ? and processorId = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql,new Object[]{account,processorId});
		try {
			while (rs.next()) {
				Container container = new Container();
				container.setId(rs.getInt("id"));
				container.setName(rs.getString("name"));
				container.setUserId(rs.getInt("userId"));
				container.setProcessorId(rs.getInt("processorId"));		
				container.setUserName(rs.getString("userName"));	
				container.setProcessorName(rs.getString("processorName"));
				ls.add(container);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return ls;
	}
	
	public Container getContianerById(Integer id){
		Container container = null;
		String sql = "select container.*,account as userName, processor.name as processorName " +
				"from container inner join `user` on container.userId = `user`.id " +
				"inner join processor on container.processorId = processor.id " +
				"where container.id = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql,new Object[]{id});
		try {
			if(rs.next()) {
				container = new Container();
				container.setId(rs.getInt("id"));
				container.setName(rs.getString("name"));
				container.setUserId(rs.getInt("userId"));
				container.setProcessorId(rs.getInt("processorId"));		
				container.setUserName(rs.getString("userName"));	
				container.setProcessorName(rs.getString("processorName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return container;
	}
	
	public Container getContianerByName(String name){
		Container container = null;
		String sql = "select container.*,account as userName, processor.name as processorName " +
				"from container inner join `user` on container.userId = `user`.id " +
				"inner join processor on container.processorId = processor.id " +
				"where container.name = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql,new Object[]{name});
		try {
			if(rs.next()) {
				container = new Container();
				container.setId(rs.getInt("id"));
				container.setName(rs.getString("name"));
				container.setUserId(rs.getInt("userId"));
				container.setProcessorId(rs.getInt("processorId"));		
				container.setUserName(rs.getString("userName"));	
				container.setProcessorName(rs.getString("processorName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return container;
	}
	
	public boolean addContainer(Container container){
		container.setUserId(getUserByAccount(container.getUserName()).getId());
		boolean result = false;			
		String sql = "insert into container values (null, ? ,? ,?)";
		Object[] obj = new Object[] { container.getName(), container.getUserId(), container.getProcessorId()};
		result = DBHelper.executeNonQuery(sql, obj) > 0;
		return result;
	}
	
	public boolean updateContainer(Container container){
		boolean result = false;
		
		String sql = "update container set name=? ,processorId=? where id =?";
		Object[] obj = new Object[] { container.getName(), container.getProcessorId(),  container.getId()};
		result = DBHelper.executeNonQuery(sql, obj) > 0;
		return result;
	}
	
	public boolean delContainer(Integer id){
		boolean result = false;			
		String sql = "delete from container where id = ?";
		Object[] obj = new Object[] { id};
		result = DBHelper.executeNonQuery(sql, obj) > 0;
		return result;
	}
	
	//*****view*********
	public View getView(String containerName){
		String sql = "select processor.id as processorId, container.id as containerId, container.name as containerName, processor.name as processorName, className, viewClass " + 
					"from container INNER JOIN processor on container.processorId = processor.id " +
					"where container.name = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql,new Object[]{containerName});
		View view=null;
		try {
			if (rs.next()) {
				view = new View();
				view.setProcessorId(rs.getInt("processorId"));
				view.setContainerId(rs.getInt("containerId"));
				view.setContainerName(rs.getString("containerName"));
				view.setProcessorName(rs.getString("processorName"));
				view.setViewClassName(rs.getString("viewClass"));
				view.setProcessorClassName(rs.getString("className"));
				
				List<ProcessDto1> ls = getProcessByContainerId(rs.getInt("containerId"));				
				view.setProcesses(ls);
				Boolean increase = ls.size()>0;
				for(int i=0;i<ls.size();i++) 
					if(!ls.get(i).getIncrease()){
						increase = false;
						break;
					}
				view.setIncrease(increase);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return view;
	}
	
	public List<ProcessDto1> getProcessByContainerId(int containerId){
		String sql = "select paragraphId, increase, config " + 
				"from process INNER JOIN paragraph on process.paragraphId = paragraph.id " +
				"where containerId = ? order by process.id";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql,new Object[]{containerId});
		List<ProcessDto1> ls = new ArrayList<ProcessDto1>();
		try {
			while (rs.next()) {
				ProcessDto1 processDto1 = new ProcessDto1();
				processDto1.setParagraphId(rs.getInt("paragraphId"));
				processDto1.setIncrease(rs.getBoolean("increase"));
				processDto1.setConfig(rs.getString("config"));
				ls.add(processDto1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return ls;
	}
	
	public List<String> listViewByContainer(Integer containerId){
		String sql = "select griView.name as name " + 
					"from container INNER JOIN processor on container.processorId = processor.id " +
					"INNER JOIN griView on griView.processorId = processor.id " +
					"where container.id = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql,new Object[]{containerId});
		List<String> ls = new ArrayList<String>();
		try {
			if (rs.next()) {			
				ls.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return ls;
	}
	
	public List<View> listView(){
		List<View> ls = new ArrayList<View>();
		String sql = "select griView.*,  processor.name as processorName " +
				"from griView inner join processor on griView.processorId = processor.id ";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql);
		try {
			while (rs.next()) {
				View view = new View();
				view.setId(rs.getInt("id"));
				view.setSubViewName(rs.getString("name"));
				view.setProcessorId(rs.getInt("processorId"));		
				view.setVirtual(rs.getBoolean("isVirtual"));	
				view.setStore(rs.getString("store"));
				view.setStructure(rs.getString("structure"));
				view.setProcessorName(rs.getString("processorName"));
				ls.add(view);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return ls;
	}
	
	public List<View> listViewByProcessor(Integer processorId){
		List<View> ls = new ArrayList<View>();
		String sql = "select griView.*,  processor.name as processorName " +
				"from griView inner join processor on griView.processorId = processor.id " + 
				"where processorId = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql,new Object[]{processorId});
		try {
			while (rs.next()) {
				View view = new View();
				view.setId(rs.getInt("id"));
				view.setSubViewName(rs.getString("name"));
				view.setProcessorId(rs.getInt("processorId"));		
				view.setVirtual(rs.getBoolean("isVirtual"));	
				view.setStore(rs.getString("store"));
				view.setStructure(rs.getString("structure"));
				view.setProcessorName(rs.getString("processorName"));
				ls.add(view);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return ls;
	}
	
	public View getViewByName(String name){
		View view = null;
		String sql = "select * from griView " +
				"where name = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql,new Object[]{name});
		try {
			if(rs.next()) {
				view = new View();
				view.setId(rs.getInt("id"));
				view.setSubViewName(rs.getString("name"));
				view.setProcessorId(rs.getInt("processorId"));		
				view.setVirtual(rs.getBoolean("isVirtual"));	
				view.setStore(rs.getString("store"));
				view.setStructure(rs.getString("structure"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return view;
	}
	
	public boolean addView(View view){
		boolean result = false;			
		String sql = "insert into griView values (null, ? ,? ,?, ? ,?)";
		Object[] obj = new Object[] { view.getSubViewName(), view.getProcessorId(), view.isVirtual(), view.getStore(), view.getStructure()};
		result = DBHelper.executeNonQuery(sql, obj) > 0;
		return result;
	}
	
	public boolean updateView(View view){
		boolean result = false;		
		String sql = "update griView set name=? ,processorId=? ,isVirtual=? , store= ?, structure=? where id =?";
		Object[] obj = new Object[] {  view.getSubViewName(), view.getProcessorId(), view.isVirtual(), view.getStore(), view.getStructure(),  view.getId()};
		result = DBHelper.executeNonQuery(sql, obj) > 0;
		return result;
	}
	
	public boolean delView(Integer id){
		boolean result = false;			
		String sql = "delete from griView where id = ?";
		Object[] obj = new Object[] { id};
		result = DBHelper.executeNonQuery(sql, obj) > 0;
		return result;
	}
	
	//*****process*********
	public List<Process> listProcess(int paragraphId){
		List<Process> ls = new ArrayList<Process>();
		String sql = "select process.id as id, paragraphId, processorId, containerId, paragraph.name as paragraphName, processor.name as processorName, container.name as containerName, config " +
				"from paragraph inner join process on paragraph.id = process.paragraphId " +
				"inner join container on process.containerId = container.id " +
				"inner join processor on container.processorId = processor.id " +
				"where paragraphId = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, new Object[]{paragraphId});
		try {
			while (rs.next()) {
				Process process = new Process();
				process.setId(rs.getInt("id"));
				process.setParagraphId(rs.getInt("paragraphId"));
				process.setProcessorId(rs.getInt("processorId"));
				process.setContainerId(rs.getInt("containerId"));
				process.setParagraphName(rs.getString("paragraphName"));
				process.setProcessorName(rs.getString("processorName"));
				process.setContainerName(rs.getString("containerName"));
				process.setConfig(rs.getString("config"));
				ls.add(process);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return ls;
	}
	
	public Process getProcessById(int processId){
		Process process = null;
		String sql = "select process.id as id, paragraphId, processorId, containerId, paragraph.name as paragraphName, processor.name as processorName, container.name as containerName, config " +
				"from paragraph inner join process on paragraph.id = process.paragraphId " +
				"inner join container on process.containerId = container.id " +
				"inner join processor on container.processorId = processor.id " +
				"where process.id = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, new Object[]{processId});
		try {
			if(rs.next()) {
				process = new Process();
				process.setId(rs.getInt("id"));
				process.setParagraphId(rs.getInt("paragraphId"));
				process.setProcessorId(rs.getInt("processorId"));
				process.setContainerId(rs.getInt("containerId"));
				process.setParagraphName(rs.getString("paragraphName"));
				process.setProcessorName(rs.getString("processorName"));
				process.setContainerName(rs.getString("containerName"));
				process.setConfig(rs.getString("config"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return process;
	}
	
	public boolean addProcess(Process process){
		boolean result = false;			
		String sql = "insert into process values (null, ? ,? ,?)";
		Object[] obj = new Object[] { process.getParagraphId(), process.getContainerId(), process.getConfig()};
		result = DBHelper.executeNonQuery(sql, obj) > 0;
		return result;
	}
	
	public boolean updateProcess(Process process){
		boolean result = false;
		
		String sql = "update process set paragraphId=? ,containerId=?, config=? where id =?";
		Object[] obj = new Object[] { process.getParagraphId(), process.getContainerId(), process.getConfig(),  process.getId()};
		result = DBHelper.executeNonQuery(sql, obj) > 0;
		return result;
	}
	
	public boolean delProcess(Integer id){
		boolean result = false;			
		String sql = "delete from process where id = ?";
		Object[] obj = new Object[] { id};
		result = DBHelper.executeNonQuery(sql, obj) > 0;
		return result;
	}
	
	public List<Process> listProcessByProcessorName(String processorName){
		List<Process> ls = new ArrayList<Process>();
		String sql = "select process.id as id, paragraphId, processorId, containerId, paragraph.name as paragraphName, processor.name as processorName, container.name as containerName, config " +
				"from paragraph inner join process on paragraph.id = process.paragraphId " +
				"inner join container on process.containerId = container.id " +
				"inner join processor on container.processorId = processor.id " +
				"where processor.name = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, new Object[]{processorName});
		try {
			while (rs.next()) {
				Process process = new Process();
				process.setId(rs.getInt("id"));
				process.setParagraphId(rs.getInt("paragraphId"));
				process.setProcessorId(rs.getInt("processorId"));
				process.setContainerId(rs.getInt("containerId"));
				process.setParagraphName(rs.getString("paragraphName"));
				process.setProcessorName(rs.getString("processorName"));
				process.setContainerName(rs.getString("containerName"));
				process.setConfig(rs.getString("config"));
				ls.add(process);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return ls;
	}
	
	public List<String> listParagraphIdByProcessorName(String processorName){
		List<String> ls = new ArrayList<String>();
		String sql = "select paragraphId " +
				"from process inner join container on process.containerId = container.id " +
				"inner join processor on container.processorId = processor.id " +
				"where processor.name = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, new Object[]{processorName});
		try {
			while (rs.next()) {
				ls.add(Integer.toString(rs.getInt("paragraphId")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return ls;
	}
	
	public List<Integer> listParagraphIdByContianerName(String contianerName){
		List<Integer> ls = new ArrayList<Integer>();
		String sql = "select paragraphId " +
				"from process inner join container on process.containerId = container.id " +
				"where container.name = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, new Object[]{contianerName});
		try {
			while (rs.next()) {
				ls.add(rs.getInt("paragraphId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return ls;
	}
	
	public Integer numProcessByParagraphId(Integer paragraphId){
		Integer result = -1;
		String sql = "select count(*) as num " +
				"from process "+
				"where paragraphId = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, new Object[]{paragraphId});
		try {
			if (rs.next()) {
				result = rs.getInt("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return result;
	}
	
	//*****user*********
	public User getUserByAccount(String account){
		User user = null;
		String sql = "select * from user where account = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql ,new Object[]{account});
		try {
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setAccount(rs.getString("account"));
				user.setPassword(rs.getString("password"));
				user.setNickname(rs.getString("nickname"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return user;
	}
	
}
