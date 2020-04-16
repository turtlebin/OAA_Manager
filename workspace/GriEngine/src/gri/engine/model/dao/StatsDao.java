/**
* Created by weiwenjie on 2017-4-15
*/
package gri.engine.model.dao;

import gri.driver.model.User;
import gri.driver.model.process.Container;
import gri.driver.model.process.Process;
import gri.driver.model.process.Processor;
import gri.driver.model.process.StatsConf;
import gri.driver.model.process.StatsConfDto1;
import gri.engine.process.topology.stats.StatsUtils;
import gri.engine.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description 
 * Created by weiwenjie on 2017-4-15
 */
public class StatsDao {
	public List<StatsConfDto1> listStatsConf(int paragraphId){
		List<StatsConfDto1>  ls = new ArrayList<StatsConfDto1>();
		String sql = "select process.id as id, paragraphId, processorId, containerId, paragraph.name as paragraphName, processor.name as processorName, container.name as containerName, config " +
				"from paragraph inner join process on paragraph.id = process.paragraphId " +
				"inner join container on process.containerId = container.id " +
				"inner join processor on container.processorId = processor.id " +
				"where paragraphId = ? and processor.name = ?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, new Object[]{paragraphId,"stats"});
		try {
			while (rs.next()) {
				StatsConfDto1 statsConfDto1 = new StatsConfDto1();
				
				statsConfDto1.setProcessId(rs.getInt("id"));
				statsConfDto1.setParagraphId(rs.getInt("paragraphId"));
				statsConfDto1.setProcessorId(rs.getInt("processorId"));
				statsConfDto1.setContainerId(rs.getInt("containerId"));
				statsConfDto1.setParagraphName(rs.getString("paragraphName"));
				statsConfDto1.setProcessorName(rs.getString("processorName"));
				statsConfDto1.setContainerName(rs.getString("containerName"));			
				
				String config = rs.getString("config");
				StatsConf statsConf =StatsUtils.jsonToStatsConf(config);
				statsConfDto1.setStatsConf(statsConf);
				
				ls.add(statsConfDto1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return ls;
	}
	
	public boolean addStatsConf(StatsConfDto1 statsConfDto1){
		ProcessDao processDao= new ProcessDao();
		Processor processor=processDao.getProcessorByName("stats");
		if(processor == null) return false;
		
		Container container = new Container();
		String containerName = "stats" + UUID.randomUUID().toString();
		container.setName(containerName);
		//用root账号创建统计的容器，这些容器不归一般用户管
		container.setUserName("root");
		container.setProcessorId(processor.getId());
		if(!processDao.addContainer(container)) return false;
		
		container = processDao.getContianerByName(containerName);
		if(container == null) return false;		
		
		Process process = new Process();
		process.setParagraphId(statsConfDto1.getParagraphId());
		process.setContainerId(container.getId());
		process.setConfig(statsConfDto1.getStatsConf().toString());
		return processDao.addProcess(process);
	}
	
	public boolean updateStatsConf(StatsConfDto1 statsConfDto1){
		boolean result = false;
		
		String sql = "update process set config=? where id =?";
		Object[] obj = new Object[] { statsConfDto1.getStatsConf().toString(),  statsConfDto1.getProcessId()};
		result = DBHelper.executeNonQuery(sql, obj) > 0;
		return result;
	}
	
	public boolean delStatsConf(Integer processId){
		ProcessDao processDao= new ProcessDao();
		Process process = processDao.getProcessById(processId);
		
		if(process == null) return false;
		
		processDao.delContainer(process.getContainerId());
		
		processDao.delProcess(processId);
		return true;
	}
}
