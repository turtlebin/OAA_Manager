package gri.engine.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import gri.engine.service.CacheInit;
import gri.engine.util.DBHelper;

/**
 * 数格引擎启动时初始化缓存数据
 * 
 */
public class CacheManager {
	public void initialAllCache() {
		String sql="select * from paragraph";
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
				Integer id = rs.getInt("id");
				String cacheUUID = rs.getString("cache");
				String dataSourceType = rs.getString("data_source_type");
				String dataSourcePath = rs.getString("data_source_path");
				new CacheInit(id, cacheUUID, dataSourceType, dataSourcePath).run();				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBHelper.free(rs,ps,conn);
		}
	}

	public void initialCache(Integer paragraphID, String cacheUUID, String dataSourceType, String dataSourcePath) {
		new CacheInit(paragraphID, cacheUUID, dataSourceType, dataSourcePath).run();
	}


}
