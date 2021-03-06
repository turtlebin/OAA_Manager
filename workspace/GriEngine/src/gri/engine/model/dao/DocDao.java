/**
* Created by weiwenjie on 2017-5-21
*/
package gri.engine.model.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gri.driver.model.GriDoc;
import gri.driver.model.GriElement;
import gri.driver.model.GriElementData2;
import gri.driver.model.GriElementData3;
import gri.driver.model.GriElementData4;
import gri.driver.model.Section;
import gri.driver.model.User;
import gri.driver.model.process.Paragraph;
import gri.driver.util.DriverConstant;
import gri.engine.core.CacheManager;
import gri.engine.core.DataSyncTaskManager;
import gri.engine.dest.Column;
import gri.engine.dest.DestDatabase;
import gri.engine.integrate.Paragraph2;
import gri.engine.integrate.Paragraph3;
import gri.engine.service.CacheService;
import gri.engine.util.DBHelper;

/**
 * @Description 
 * Created by weiwenjie on 2017-5-21
 */
public class DocDao {//该类主要提供了大量的对数据库表的操作，目标数据库为222.201.145.241
	//*****user*********
	public User addUser(User user){
		User newUser = null;
		String sql = "insert into user(account, password, nickname) values (?,?,?)";
		Object[] obj = new Object[] { user.getAccount(), user.getPassword(), user.getNickname() };
		if (DBHelper.executeNonQuery(sql, obj) > 0) {
			// account存在唯一约束
			String sql2 = "select * from user where account=?";
			Object[] obj2 = new Object[] { user.getAccount() };
			Connection conn=DBHelper.getConnection();
			PreparedStatement ps=null;
			try {
				ps = conn.prepareStatement(sql2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultSet rs2 = DBHelper.executeQuery(conn,ps,sql2, obj2);
			try {
				while (rs2.next()) {
					newUser = user;
					newUser.setId(rs2.getInt("id"));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBHelper.free(rs2);
			}
			
			String sql3="select distinct id from gridoc";
			PreparedStatement ps3=null;
			try {
				ps3 = conn.prepareStatement(sql3);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultSet rs3=DBHelper.executeQuery(conn, ps3, sql3);
			String sql4="insert into gridoc_user values(?,?,?)";
			
			try {
				while(rs3.next()) {
					DBHelper.executeNonQuery(sql4, rs3.getInt(1),newUser.getId(),"N");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				DBHelper.free(rs3,ps3,conn);
			}
			
		}
		return newUser;
	}
	
	public boolean delUser(User user){
		String sql = "delete from user Where id=?";//删除用户会导致级联删除，因此无需再删除gridoc_user
		Object[] obj = new Object[] { user.getId() };
		boolean success = false;
		if (DBHelper.executeNonQuery(sql, obj) > 0)
			success = true;
		return success;
	}
	
	public List<User> getAllUser(){
		List<User> users = new ArrayList<User>();
		String sql="select * from user";
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
				User user = new User(rs.getString("account"), rs.getString("password"),
						rs.getString("nickname"));
				user.setId(rs.getInt("id"));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBHelper.free(rs,ps,conn);
		}
		return users;
	}
	
//	public List<User> getAllUser(){
//	List<User> users = new ArrayList<User>();
//	String sql="select * from user";
//	Connection conn=DBHelper.getConnection2();
//	PreparedStatement ps=null;
//	try {
//		ps = conn.prepareStatement(sql);
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	ResultSet rs = DBHelper.executeQuery(conn,ps,sql);
//	try {
//		while (rs.next()) {
//			User user = new User(rs.getString("username"), rs.getString("password"),
//					rs.getString("name"));
//			user.setId(rs.getInt("id"));
//			users.add(user);
//		}
//	} catch (SQLException e) {
//		e.printStackTrace();
//	}finally {
//		DBHelper.free(rs,ps,conn);
//	}
//	return users;
//}
	
	
	public boolean updateUser(User user){
		String sql = "update user set account=?, password=?, nickname=?  Where id=?";
		Object[] obj = new Object[] { user.getAccount(), user.getPassword(), user.getNickname(),
				user.getId() };
		boolean success = false;
		if (DBHelper.executeNonQuery(sql, obj) > 0)
			success = true;
		return success;
	}
	
	public int getIdByAccount(String account) {
		int id = -1;
		String sql = "select * from user where account=?";
		Object[] obj = new Object[] { account };
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
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return id;
	}
	
//	public int getIdByAccount(String account) {
//	int id = -1;
//	String sql = "select * from user where username=?";
//	Object[] obj = new Object[] { account };
//	Connection conn=DBHelper.getConnection2();
//	PreparedStatement ps=null;
//	try {
//		ps = conn.prepareStatement(sql);
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	ResultSet rs = DBHelper.executeQuery(conn,ps,sql, obj);
//	try {
//		while (rs.next()) {
//			id = rs.getInt("id");
//		}
//	} catch (SQLException e) {
//		e.printStackTrace();
//	} finally {
//		DBHelper.free(rs,ps,conn);
//	}
//	return id;
//}
	
	//*****GriDoc*********	
	public GriDoc addGriDoc(GriDoc gridoc, int userId){//添加格文档至gridoc和gridoc_user表中
		GriDoc newGridoc = null;
		String sql = "insert into gridoc(name) values(?); ";
		Object[] obj = new Object[] { gridoc.getName() };
		int id = DBHelper.executeInsert(sql, obj);
		if (id > 0) {
			String sql2 = "select * from gridoc where id=?";
			Object[] obj2 = new Object[] { id };
			Connection conn=DBHelper.getConnection();
			PreparedStatement ps=null;
			try {
				ps = conn.prepareStatement(sql2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultSet rs2 = DBHelper.executeQuery(conn,ps,sql2, obj2);
			ResultSet rs4=null;
			PreparedStatement ps4=null;
			PreparedStatement ps3=null;
			try {
				if (rs2.next()) {
					newGridoc = gridoc;
					int docId=rs2.getInt("id");
					newGridoc.setId(docId);
					newGridoc.setUpdateTime(rs2.getTimestamp("update_time"));
					String sql3 = "insert into gridoc_user(gridoc, user, read_only) values(?,?,?)";
					String sql4="select distinct id from user";
					ps4=conn.prepareStatement(sql4);
					ps3=conn.prepareStatement(sql3);
					rs4=DBHelper.executeQuery(conn, ps4, sql4);
					while(rs4.next()) {
						DBHelper.executeNonQuery(conn,ps3,sql3,docId,rs4.getInt("id"),"N");
					}
//					Object[] obj3 = new Object[] { newGridoc.getId(), userId, "N" };
//					DBHelper.executeNonQuery(sql3, obj3);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBHelper.free(conn);
			}
		}
		return newGridoc;
	}
	
	public boolean delGriDoc(GriDoc gridoc, int userId){
		boolean success = false;
		boolean readOnly = docReadOnly(gridoc.getId(), userId);
		if (!readOnly) {
			String sql = "delete from gridoc Where id=?";
			Object[] obj = new Object[] { gridoc.getId() };
			if (DBHelper.executeNonQuery(sql, obj) > 0)
				success = true;
		}
		return success;
	}
	
	public List<GriDoc> getAllGriDoc(int userId) {
		List<GriDoc> gridocs = new ArrayList<GriDoc>();
		String sql = "select distinct gridoc.id, gridoc.name, gridoc.update_time, gridoc_user.read_only from gridoc "
				+ "inner join gridoc_user on gridoc_user.gridoc = gridoc.id "
				+ "where gridoc_user.user=?";
		Object[] obj = new Object[] { userId };
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
				GriDoc gridoc = new GriDoc(rs.getString("name"));
				gridoc.setId(rs.getInt("id"));
				gridoc.setUpdateTime(rs.getTimestamp("update_time"));
				gridoc.setReadOnly(rs.getString("read_only").equals("Y"));
				gridocs.add(gridoc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return gridocs;
	}
	
//	public List<GriDoc> getAllGriDoc(int userId) {
//	List<GriDoc> gridocs = new ArrayList<GriDoc>();
//	String sql = "select distinct gridoc.id, gridoc.name, gridoc.update_time, gridoc_user.read_only from gridoc "
//			+ "inner join gridoc_user on gridoc_user.gridoc = gridoc.id ";
//	Connection conn=DBHelper.getConnection();
//	PreparedStatement ps=null;
//	try {
//		ps = conn.prepareStatement(sql);
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	ResultSet rs = null;
//	try {
//		rs=ps.executeQuery(sql);
//		while (rs.next()) {
//			GriDoc gridoc = new GriDoc(rs.getString("name"));
//			gridoc.setId(rs.getInt("id"));
//			gridoc.setUpdateTime(rs.getTimestamp("update_time"));
//			gridoc.setReadOnly(rs.getString("read_only").equals("Y"));
//			gridocs.add(gridoc);
//		}
//	} catch (SQLException e) {
//		e.printStackTrace();
//	} finally {
//		DBHelper.free(rs,ps,conn);
//	}
//	return gridocs;
//}
	
	public boolean updateGriDoc(GriDoc gridoc, int userId){
		boolean success = false;
		boolean readOnly = docReadOnly(gridoc.getId(), userId);
		if (!readOnly) {
			String sql = "update gridoc set name=? where id=?";
			Object[] obj = new Object[] { gridoc.getName(), gridoc.getId() };
			if (DBHelper.executeNonQuery(sql, obj) > 0)
				success = true;
		}
		return success;
	}
	
	public List<GriElement> getChildrenOfGriDoc(GriDoc gridoc) {//获取格文档的孩子,可以获取节或段
		List<GriElement> children = new ArrayList<GriElement>();
		String sql = "select * from gridoc_child LEFT JOIN section on gridoc_child.section=section.id "
				+ "LEFT JOIN paragraph on gridoc_child.paragraph=paragraph.id "
				+ "where gridoc_child.gridoc=? order by gridoc_child.sequence";
		Object[] obj = new Object[] { gridoc.getId() };
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, obj);
		String sql2;
		Object[] obj2;
		ResultSet rs2;
		try {
			while (rs.next()) {
				String flag = rs.getString("gridoc_child.flag");
				if (flag.equals("section")) {
					Section section = new Section(rs.getString("section.name"));
					section.setId(rs.getInt("section.id"));
					section.setUpdateTime(rs.getTimestamp("section.update_time"));
					section.setSequence(rs.getInt("gridoc_child.sequence"));
					children.add(section);
				} else if (flag.equals("paragraph")) {
					Paragraph paragraph = new Paragraph();
					paragraph.setName(rs.getString("paragraph.name"));
					paragraph.setKeywords(rs.getString("paragraph.keywords"));
					paragraph.setDescription(rs.getString("paragraph.description"));
					paragraph.setSyncTimeType(rs.getString("paragraph.sync_time_type"));
					paragraph.setSyncDirectionType(rs.getString("paragraph.sync_direction_type"));
					paragraph.setWarmSyncDetail(rs.getString("paragraph.warm_sync_detail"));
					paragraph.setDataSourcePath(rs.getString("paragraph.data_source_path"));
					paragraph.setDataSourceType(rs.getString("paragraph.data_source_type"));
					paragraph.setId(rs.getInt("paragraph.id"));
					paragraph.setUpdateTime(rs.getTimestamp("paragraph.update_time"));
					paragraph.setSequence(rs.getInt("gridoc_child.sequence"));
					paragraph.setLastSyncSucceed(rs.getString("paragraph.last_sync_succeed").equals("Y"));
					paragraph.setLastSyncTime(rs.getTimestamp("paragraph.last_sync_time"));
					String cacheUUID = rs.getString("paragraph.cache");
					paragraph.setCache(cacheUUID);
					paragraph.setDataSize(new CacheService(cacheUUID).cacheSize());
					sql2="select * from dest_info where paragraph_id=?";
					obj2=new Object[]{paragraph.getId()};
					Connection conn2=DBHelper.getConnection();
					PreparedStatement ps2=null;
					try {
						ps2 = conn2.prepareStatement(sql2);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rs2=DBHelper.executeQuery(conn2,ps2,sql2, obj2);
					if(rs2.next()){
//						if(!rs2.isLast()){
//							rs2.last();
//						}
						paragraph.setDataDestbase(rs2.getString("data_dest_base"));
						paragraph.setDataDestType(rs2.getString("data_dest_type"));
						paragraph.setDestPort(rs2.getString("dest_port"));
						paragraph.setDestAccount(rs2.getString("dest_account"));
						paragraph.setDestPassword(rs2.getString("dest_password"));
						paragraph.setDestIP(rs2.getString("dest_IP"));
					}
					children.add(paragraph);
					DBHelper.free(rs2,ps2,conn2);
				} else if (flag.equals("paragraph3")) {
						Paragraph3 paragraph = new Paragraph3();
						paragraph.setName(rs.getString("paragraph.name"));
						paragraph.setKeywords(rs.getString("paragraph.keywords"));
						paragraph.setDescription(rs.getString("paragraph.description"));
						paragraph.setSyncTimeType(rs.getString("paragraph.sync_time_type"));
						paragraph.setSyncDirectionType(rs.getString("paragraph.sync_direction_type"));
						paragraph.setWarmSyncDetail(rs.getString("paragraph.warm_sync_detail"));
						paragraph.setDataSourcePath(rs.getString("paragraph.data_source_path"));
						paragraph.setDataSourceType(rs.getString("paragraph.data_source_type"));
						paragraph.setId(rs.getInt("paragraph.id"));
						paragraph.setUpdateTime(rs.getTimestamp("paragraph.update_time"));
						paragraph.setSequence(rs.getInt("gridoc_child.sequence"));
						paragraph.setLastSyncSucceed(rs.getString("paragraph.last_sync_succeed").equals("Y"));
						paragraph.setLastSyncTime(rs.getTimestamp("paragraph.last_sync_time"));
						paragraph.setParagraph3(true);
						String cacheUUID = rs.getString("paragraph.cache");
						paragraph.setCache(cacheUUID);
						paragraph.setDataSize(new CacheService(cacheUUID).cacheSize());
						children.add(paragraph);
					}else if (flag.equals("paragraph2")) {
						Paragraph2 paragraph = new Paragraph2();
						paragraph.setName(rs.getString("paragraph.name"));
						paragraph.setKeywords(rs.getString("paragraph.keywords"));
						paragraph.setDescription(rs.getString("paragraph.description"));
						paragraph.setSyncTimeType(rs.getString("paragraph.sync_time_type"));
						paragraph.setSyncDirectionType(rs.getString("paragraph.sync_direction_type"));
						paragraph.setWarmSyncDetail(rs.getString("paragraph.warm_sync_detail"));
						paragraph.setDataSourcePath(rs.getString("paragraph.data_source_path"));
						paragraph.setDataSourceType(rs.getString("paragraph.data_source_type"));
						paragraph.setId(rs.getInt("paragraph.id"));
						paragraph.setUpdateTime(rs.getTimestamp("paragraph.update_time"));
						paragraph.setSequence(rs.getInt("gridoc_child.sequence"));
						paragraph.setLastSyncSucceed(rs.getString("paragraph.last_sync_succeed").equals("Y"));
						paragraph.setLastSyncTime(rs.getTimestamp("paragraph.last_sync_time"));
						paragraph.setParagraph2(true);
						String cacheUUID = rs.getString("paragraph.cache");
						paragraph.setCache(cacheUUID);
						paragraph.setDataSize(new CacheService(cacheUUID).cacheSize());
						children.add(paragraph);
					}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
				DBHelper.free(rs,ps,conn);
		}
		return children;
	}
	
	public boolean docReadOnly(int gridocId, int userId) {
		String sql = "select * from gridoc_user where gridoc=? and user=?";
		Object[] obj = new Object[] { gridocId, userId };
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
				return rs.getString("read_only").equals("Y");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return true;
	}
	
	
	//*****section*********	
	
	public List<GriElement> getChildrenOfSection(Section root) {//获取节的孩子（实际上只能获取段），这部分的代码重复严重
		List<GriElement> children = new ArrayList<GriElement>();
		String sql = "select * from section_child " + "LEFT JOIN section on section_child.sub_section=section.id "
				+ "LEFT JOIN paragraph on section_child.paragraph=paragraph.id "
				+ "where section_child.section=? order by section_child.sequence";
		
		Object[] obj = new Object[] { root.getId() };
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, obj);
		String sql2;
		Object[] obj2;
		ResultSet rs2;
		try {
			while (rs.next()) {
				String flag = rs.getString("section_child.flag");
				if (flag.equals("section")) {
					Section section = new Section(rs.getString("section.name"));
					section.setId(rs.getInt("section.id"));
					section.setUpdateTime(rs.getTimestamp("section.update_time"));
					section.setSequence(rs.getInt("section_child.sequence"));
					children.add(section);
				} else if (flag.equals("paragraph")) {
					Paragraph paragraph = new Paragraph();
					paragraph.setName(rs.getString("paragraph.name"));
					paragraph.setKeywords(rs.getString("paragraph.keywords"));
					paragraph.setDescription(rs.getString("paragraph.description"));
					paragraph.setSyncTimeType(rs.getString("paragraph.sync_time_type"));
					paragraph.setSyncDirectionType(rs.getString("paragraph.sync_direction_type"));
					paragraph.setWarmSyncDetail(rs.getString("paragraph.warm_sync_detail"));
					paragraph.setDataSourcePath(rs.getString("paragraph.data_source_path"));
					paragraph.setDataSourceType(rs.getString("paragraph.data_source_type"));
					paragraph.setId(rs.getInt("paragraph.id"));
					paragraph.setUpdateTime(rs.getTimestamp("paragraph.update_time"));
					paragraph.setSequence(rs.getInt("section_child.sequence"));
					paragraph.setLastSyncSucceed(rs.getString("paragraph.last_sync_succeed").equals("Y"));
					paragraph.setLastSyncTime(rs.getTimestamp("paragraph.last_sync_time"));
					String cacheUUID = rs.getString("paragraph.cache");
					paragraph.setCache(cacheUUID);
					paragraph.setDataSize(new CacheService(cacheUUID).cacheSize());
					sql2="select * from dest_info where paragraph_id=?";
					obj2=new Object[]{paragraph.getId()};
					Connection conn2=DBHelper.getConnection();
					PreparedStatement ps2=null;
					try {
						ps2 = conn2.prepareStatement(sql2);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rs2=DBHelper.executeQuery(conn2,ps2,sql2, obj2);
					if(rs2.next()){
//						if(!rs2.isLast()){
//							rs2.last();
//						}
						paragraph.setDataDestbase(rs2.getString("data_dest_base"));
						paragraph.setDataDestType(rs2.getString("data_dest_type"));
						paragraph.setDestPort(rs2.getString("dest_port"));
						paragraph.setDestAccount(rs2.getString("dest_account"));
						paragraph.setDestPassword(rs2.getString("dest_password"));
						paragraph.setDestIP(rs2.getString("dest_IP"));
					}
					DBHelper.free(rs2,ps2,conn2);
					children.add(paragraph);
				}else if (flag.equals("paragraph3")) {
					Paragraph3 paragraph = new Paragraph3();
					paragraph.setName(rs.getString("paragraph.name"));
					paragraph.setKeywords(rs.getString("paragraph.keywords"));
					paragraph.setDescription(rs.getString("paragraph.description"));
					paragraph.setSyncTimeType(rs.getString("paragraph.sync_time_type"));
					paragraph.setSyncDirectionType(rs.getString("paragraph.sync_direction_type"));
					paragraph.setWarmSyncDetail(rs.getString("paragraph.warm_sync_detail"));
					paragraph.setDataSourcePath(rs.getString("paragraph.data_source_path"));
					paragraph.setDataSourceType(rs.getString("paragraph.data_source_type"));
					paragraph.setId(rs.getInt("paragraph.id"));
					paragraph.setUpdateTime(rs.getTimestamp("paragraph.update_time"));
					paragraph.setSequence(rs.getInt("section_child.sequence"));
					paragraph.setLastSyncSucceed(rs.getString("paragraph.last_sync_succeed").equals("Y"));
					paragraph.setLastSyncTime(rs.getTimestamp("paragraph.last_sync_time"));
					paragraph.setParagraph3(true);
					String cacheUUID = rs.getString("paragraph.cache");
					paragraph.setCache(cacheUUID);
					//paragraph.setDataSize(new CacheService(cacheUUID).cacheSize());
					children.add(paragraph);
				}else if (flag.equals("paragraph2")) {
					Paragraph2 paragraph = new Paragraph2();
					paragraph.setName(rs.getString("paragraph.name"));
					paragraph.setKeywords(rs.getString("paragraph.keywords"));
					paragraph.setDescription(rs.getString("paragraph.description"));
					paragraph.setSyncTimeType(rs.getString("paragraph.sync_time_type"));
					paragraph.setSyncDirectionType(rs.getString("paragraph.sync_direction_type"));
					paragraph.setWarmSyncDetail(rs.getString("paragraph.warm_sync_detail"));
					paragraph.setDataSourcePath(rs.getString("paragraph.data_source_path"));
					paragraph.setDataSourceType(rs.getString("paragraph.data_source_type"));
					paragraph.setId(rs.getInt("paragraph.id"));
					paragraph.setUpdateTime(rs.getTimestamp("paragraph.update_time"));
					paragraph.setSequence(rs.getInt("section_child.sequence"));
					paragraph.setLastSyncSucceed(rs.getString("paragraph.last_sync_succeed").equals("Y"));
					paragraph.setLastSyncTime(rs.getTimestamp("paragraph.last_sync_time"));
					paragraph.setParagraph2(true);
					String cacheUUID = rs.getString("paragraph.cache");
					paragraph.setCache(cacheUUID);
					//paragraph.setDataSize(new CacheService(cacheUUID).cacheSize());
					children.add(paragraph);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
		return children;
	}
	
	
	//*****GriElement*********	
	public GriElement addGriElement(GriElementData4 griElementData4, int userId,boolean addTableName){//注意这里是关键，添加格元素方法返回newChildSection或newChildParagraph，而这些东西都已经执行了setID方法，因此可以直接用getID获取ID。
		GriDoc belong = griElementData4.getBelong();
		GriElement father = griElementData4.getFather();
		GriElement child = griElementData4.getChild();
		boolean readOnly = docReadOnly(belong.getId(), userId);
		if (readOnly) {
			//LOGGER.info("格文档为只读：[user:{}, gridoc id:{}]", userId, belong.getId());			
			return null;
		}

		if (child instanceof Section) {
			Section childSection = (Section) child;
			Section newChildSection = null;
			String sql = "insert into section(name) values(?)";
			Object[] obj = new Object[] { childSection.getName() };
			int id = DBHelper.executeInsert(sql, obj);//添加节，然后再修改其他关联表
			if (id > 0) {
				String sql2 = "select * from section where id=?";
				Object[] obj2 = new Object[] { id };
				Connection conn=DBHelper.getConnection();
				PreparedStatement ps=null;
				try {
					ps = conn.prepareStatement(sql2);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ResultSet rs2 = DBHelper.executeQuery(conn,ps,sql2, obj2);
				try {
					while (rs2.next()) {
						newChildSection = childSection;
						newChildSection.setId(rs2.getInt("id"));
						newChildSection.setUpdateTime(rs2.getTimestamp("update_time"));

						if (father instanceof GriDoc) {
							GriDoc fatherGridoc = (GriDoc) father;
							String sql3="select MAX(sequence) as max from gridoc_child";
							Connection conn3=DBHelper.getConnection();
							PreparedStatement ps3=null;
							try {
								ps3 = conn3.prepareStatement(sql3);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ResultSet rs3 = DBHelper
									.executeQuery(conn3,ps3,sql3);
							int seq = 1;
							try {
								while (rs3.next()) {
									seq = rs3.getInt("max") + 1;
								}
							} catch (SQLException e3) {
								e3.printStackTrace();
							} finally {
								DBHelper.free(rs3,ps3,conn3);
							}
							String sql4 = "insert into gridoc_child(gridoc, section, flag, sequence) values(?,?,?,?)";
							Object[] obj4 = new Object[] { fatherGridoc.getId(), newChildSection.getId(),
									"section", seq };
							DBHelper.executeNonQuery(sql4, obj4);
						} else if (father instanceof Section) {
							Section fatherSection = (Section) father;
							String sql3="select MAX(sequence) as max from section_child";
							Connection conn3=DBHelper.getConnection();
							PreparedStatement ps3=null;
							try {
								ps3 = conn3.prepareStatement(sql3);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ResultSet rs3 = DBHelper
									.executeQuery(conn3,ps3,sql3);
							int sequence = 1;
							try {
								while (rs3.next()) {
									sequence = rs3.getInt("max") + 1;
								}
							} catch (SQLException e3) {
								e3.printStackTrace();
							} finally {
								DBHelper.free(rs3,ps3,conn3);
							}
							String sql4 = "insert into section_child(section, sub_section, flag, sequence) values(?,?,?,?)";
							Object[] obj4 = new Object[] { fatherSection.getId(), newChildSection.getId(),
									"section", sequence };
							DBHelper.executeNonQuery(sql4, obj4);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DBHelper.free(rs2,ps,conn);
				}

			}
			return newChildSection;
		}else if(child instanceof Paragraph2) {

			Paragraph2 childParagraph2=(Paragraph2) child;
			Paragraph2 newChildParagraph2=null;
			String sql = "insert into paragraph(name,keywords,description,"
					+ "sync_time_type,sync_direction_type,warm_sync_detail,data_source_type,data_source_path,"
					+ "cache,last_sync_succeed,last_sync_time,datasource_changed,cache_changed,increase,isParagraph2)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTimeStr = sdf.format(new Date());
			String cacheUUID = UUID.randomUUID().toString();//随机生成缓存号
			Object[] obj = new Object[] { childParagraph2.getName(), childParagraph2.getKeywords(),
					childParagraph2.getDescription(), childParagraph2.getSyncTimeType(),
					childParagraph2.getSyncDirectionType(), childParagraph2.getWarmSyncDetail(),
					childParagraph2.getDataSourceType(), childParagraph2.getDataSourcePath(), cacheUUID, "N",//注意后面的几个字段原paragraph是没有的
					currentTimeStr, "N", "N" ,false,true};
			int id = DBHelper.executeInsert(sql, obj);//生成段ID
			if(id>=0)
			{
				if (childParagraph2.getSyncTimeType().equals(DriverConstant.SyncTimeType_1)) {//添加定时任务
					new DataSyncTaskManager().addTask3(id);
				}
			}
			String sql2 = "select * from paragraph where id=?";
			Object[] obj2 = new Object[] { id };
			Connection conn=DBHelper.getConnection();
			PreparedStatement ps=null;
			try {
				ps = conn.prepareStatement(sql2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultSet rs2 = DBHelper.executeQuery(conn,ps,sql2, obj2);
			try {
				while (rs2.next()) {
					newChildParagraph2 = childParagraph2;
					newChildParagraph2.setId(rs2.getInt("id"));
					newChildParagraph2.setCache(rs2.getString("cache"));
					newChildParagraph2.setUpdateTime(rs2.getTimestamp("update_time"));					
				}
				if (father instanceof GriDoc) {
					GriDoc fatherGridoc = (GriDoc) father;
					String sql3="select MAX(sequence) as max from gridoc_child";
					Connection conn3=DBHelper.getConnection();
					PreparedStatement ps3=null;
					try {
						ps3 = conn3.prepareStatement(sql3);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ResultSet rs3 = DBHelper
							.executeQuery(conn3,ps3,sql3);
					int seq = 1;
					try {
						while (rs3.next()) {
							seq = rs3.getInt("max") + 1;
						}
					} catch (SQLException e3) {
						e3.printStackTrace();
					} finally {
						DBHelper.free(rs3,ps3,conn3);
					}
					String sql4 = "insert into gridoc_child(gridoc, paragraph, flag, sequence) values(?,?,?,?)";
					Object[] obj4 = new Object[] { fatherGridoc.getId(), newChildParagraph2.getId(),
							"paragraph2", seq };
					DBHelper.executeNonQuery(sql4, obj4);
				} else if (father instanceof Section) {
					Section fatherSection = (Section) father;
					String sql3="select MAX(sequence) as max from section_child";
					Connection conn3=DBHelper.getConnection();
					PreparedStatement ps3=null;
					try {
						ps3 = conn3.prepareStatement(sql3);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ResultSet rs3 = DBHelper
							.executeQuery(conn3,ps3,sql3);
					int seq2 = 1;
					try {
						while (rs3.next()) {
							seq2 = rs3.getInt("max") + 1;
						}
					} catch (SQLException e3) {
						e3.printStackTrace();
					} finally {
						DBHelper.free(rs3,ps3,conn3);
					}
					String sql4 = "insert into section_child(section, paragraph, flag, sequence) values(?,?,?,?)";
					Object[] obj4 = new Object[] { fatherSection.getId(), newChildParagraph2.getId(),
							"paragraph2", seq2 };
					DBHelper.executeNonQuery(sql4, obj4);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBHelper.free(rs2,ps,conn);
			}
			System.out.println("paragraph2");
			return newChildParagraph2;
		
		} else if(child instanceof Paragraph3) {
			Paragraph3 childParagraph3=(Paragraph3) child;
			Paragraph3 newChildParagraph3=null;
			String sql = "insert into paragraph(name,keywords,description,"
					+ "sync_time_type,sync_direction_type,warm_sync_detail,data_source_type,data_source_path,"
					+ "cache,last_sync_succeed,last_sync_time,datasource_changed,cache_changed,increase,isParagraph3)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTimeStr = sdf.format(new Date());
			String cacheUUID = UUID.randomUUID().toString();//随机生成缓存号
			Object[] obj = new Object[] { childParagraph3.getName(), childParagraph3.getKeywords(),
					childParagraph3.getDescription(), childParagraph3.getSyncTimeType(),
					childParagraph3.getSyncDirectionType(), childParagraph3.getWarmSyncDetail(),
					childParagraph3.getDataSourceType(), childParagraph3.getDataSourcePath(), cacheUUID, "N",//注意后面的几个字段原paragraph是没有的
					currentTimeStr, "N", "N" ,false,true};
			int id = DBHelper.executeInsert(sql, obj);//生成段ID
			if(id>=0)
			{
				if (childParagraph3.getSyncTimeType().equals(DriverConstant.SyncTimeType_1)) {//添加定时任务
					new DataSyncTaskManager().addTask2(id);
				}
			}
			String sql2 = "select * from paragraph where id=?";
			Object[] obj2 = new Object[] { id };
			Connection conn=DBHelper.getConnection();
			PreparedStatement ps=null;
			try {
				ps = conn.prepareStatement(sql2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultSet rs2 = DBHelper.executeQuery(conn,ps,sql2, obj2);
			try {
				while (rs2.next()) {
					newChildParagraph3 = childParagraph3;
					newChildParagraph3.setId(rs2.getInt("id"));
					newChildParagraph3.setCache(rs2.getString("cache"));
					newChildParagraph3.setUpdateTime(rs2.getTimestamp("update_time"));					
				}
				if (father instanceof GriDoc) {
					GriDoc fatherGridoc = (GriDoc) father;
					String sql3="select MAX(sequence) as max from gridoc_child";
					Connection conn3=DBHelper.getConnection();
					PreparedStatement ps3=null;
					try {
						ps3 = conn3.prepareStatement(sql3);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ResultSet rs3 = DBHelper
							.executeQuery(conn3,ps3,sql3);
					int seq = 1;
					try {
						while (rs3.next()) {
							seq = rs3.getInt("max") + 1;
						}
					} catch (SQLException e3) {
						e3.printStackTrace();
					} finally {
						DBHelper.free(rs3,ps3,conn3);
					}
					String sql4 = "insert into gridoc_child(gridoc, paragraph, flag, sequence) values(?,?,?,?)";
					Object[] obj4 = new Object[] { fatherGridoc.getId(), newChildParagraph3.getId(),
							"paragraph3", seq };
					DBHelper.executeNonQuery(sql4, obj4);
				} else if (father instanceof Section) {
					Section fatherSection = (Section) father;
					String sql3="select MAX(sequence) as max from section_child";
					Connection conn3=DBHelper.getConnection();
					PreparedStatement ps3=null;
					try {
						ps3 = conn3.prepareStatement(sql3);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ResultSet rs3 = DBHelper
							.executeQuery(conn3,ps3,sql3);
					int seq2 = 1;
					try {
						while (rs3.next()) {
							seq2 = rs3.getInt("max") + 1;
						}
					} catch (SQLException e3) {
						e3.printStackTrace();
					} finally {
						DBHelper.free(rs3,ps3,conn3);
					}
					String sql4 = "insert into section_child(section, paragraph, flag, sequence) values(?,?,?,?)";
					Object[] obj4 = new Object[] { fatherSection.getId(), newChildParagraph3.getId(),
							"paragraph3", seq2 };
					DBHelper.executeNonQuery(sql4, obj4);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBHelper.free(rs2,ps,conn);
			}
			System.out.println("paragraph3");
			return newChildParagraph3;
		}
			else if (child instanceof Paragraph) {//如果child是Paragraph的类型，另外class.isInstance(obj)意思是obj能否转换为class类
			
			Paragraph childParagraph = (Paragraph) child;
			Paragraph newChildParagraph = null;

			String sql = "insert into paragraph(name,keywords,description,"
					+ "sync_time_type,sync_direction_type,warm_sync_detail,data_source_type,data_source_path,"
					+ "cache,last_sync_succeed,last_sync_time,datasource_changed,cache_changed,increase)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTimeStr = sdf.format(new Date());
			String cacheUUID = UUID.randomUUID().toString();//随机生成缓存号
			Object[] obj = new Object[] { childParagraph.getName(), childParagraph.getKeywords(),
					childParagraph.getDescription(), childParagraph.getSyncTimeType(),
					childParagraph.getSyncDirectionType(), childParagraph.getWarmSyncDetail(),
					childParagraph.getDataSourceType(), childParagraph.getDataSourcePath(), cacheUUID, "N",//注意后面的几个字段原paragraph是没有的
					currentTimeStr, "N", "N" ,false};
			int id = DBHelper.executeInsert(sql, obj);//生成段ID
			if (id > 0) {
				new CacheManager().initialCache(id, cacheUUID, childParagraph.getDataSourceType(),
						childParagraph.getDataSourcePath());// 初始化缓存文件
				if (childParagraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_1)) {
					new DataSyncTaskManager().addTask(id);
				}
				
//				if(!(childParagraph.getDataDestType().equals("")||childParagraph.getDataDestbase().equals("")//此处的判断只能用这种方式，不能用==“”或者。equals（null）
//						||childParagraph.getDestAccount().equals("")||childParagraph.getDestIP().equals("")
//						||childParagraph.getDestPassword().equals("")||childParagraph.getDestPort().equals(""))){//若选择了物化
//					CacheService cs=new CacheService(cacheUUID);
//					InputStream input=cs.getInputStream();
//					InputStream input2=cs.getInputStream();
//					InputStream input3=cs.getInputStream();
//					
//					String destPath=childParagraph.getDataDestType()+"###"+childParagraph.getDataDestbase()+"###"+childParagraph.getDestAccount()+"###"+
//					childParagraph.getDestPassword()+"###"+childParagraph.getDestIP()+"###"+childParagraph.getDestPort();
//					
//					DestDatabase des=new DestDatabase(childParagraph.getDataSourcePath(),destPath);
//					int columnCount=des.getColumnCount(input);
//					List<Column> columns=des.initColumn(input2, columnCount);
//				   String sourceBaseType=childParagraph.getDataSourcePath().split("###")[0];
//				   String destBaseType=childParagraph.getDataDestType();
//				   boolean create=false;
//				   boolean insert=false;
//				   if(destBaseType.equalsIgnoreCase("mysql")){
//					   create= des.createTable(columns, columnCount, childParagraph.getName(),addTableName);
//					   insert=des.insertMySql4(input3, childParagraph.getName());
//				   }
//				   else if(destBaseType.equalsIgnoreCase("Oracle")){
//					   create=des.createOracleTable(columns, columnCount, childParagraph.getName(), addTableName);
//					   insert=des.insertOracle(input3, childParagraph.getName());
//				   }

//					if(create&&insert){
				if(!(childParagraph.getDataDestType().equals("")||childParagraph.getDataDestbase().equals("")//此处的判断只能用这种方式，不能用==“”或者。equals（null）
				||childParagraph.getDestAccount().equals("")||childParagraph.getDestIP().equals("")
				||childParagraph.getDestPassword().equals("")||childParagraph.getDestPort().equals(""))){
					String sql5="insert into dest_info(paragraph_id,data_dest_type,data_dest_base,dest_port,dest_account,dest_password,dest_IP)values(?,?,?,?,?,?,?)";
					Object[] obj5=new Object[] {id,childParagraph.getDataDestType(),childParagraph.getDataDestbase(),
							childParagraph.getDestPort(),childParagraph.getDestAccount(),
							childParagraph.getDestPassword(),childParagraph.getDestIP()};
					DBHelper.executeNonQuery(sql5, obj5);
					}
//				}
				String sql2 = "select * from paragraph where id=?";
				Object[] obj2 = new Object[] { id };
				Connection conn2=DBHelper.getConnection();
				PreparedStatement ps2=null;
				try {
					ps2 = conn2.prepareStatement(sql2);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ResultSet rs2 = DBHelper.executeQuery(conn2,ps2,sql2, obj2);
				try {
					while (rs2.next()) {
						newChildParagraph = childParagraph;
						newChildParagraph.setId(rs2.getInt("id"));
						newChildParagraph.setCache(rs2.getString("cache"));
						newChildParagraph.setUpdateTime(rs2.getTimestamp("update_time"));
						if (father instanceof GriDoc) {
							GriDoc fatherGridoc = (GriDoc) father;
							String sql3="select MAX(sequence) as max from gridoc_child";
							Connection conn3=DBHelper.getConnection();
							PreparedStatement ps3=null;
							try {
								ps3 = conn3.prepareStatement(sql3);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ResultSet rs3 = DBHelper
									.executeQuery(conn3,ps3,sql3);
							int seq = 1;
							try {
								while (rs3.next()) {
									seq = rs3.getInt("max") + 1;
								}
							} catch (SQLException e3) {
								e3.printStackTrace();
							} finally {
								DBHelper.free(rs3,ps3,conn3);
							}
							String sql4 = "insert into gridoc_child(gridoc, paragraph, flag, sequence) values(?,?,?,?)";
							Object[] obj4 = new Object[] { fatherGridoc.getId(), newChildParagraph.getId(),
									"paragraph", seq };
							DBHelper.executeNonQuery(sql4, obj4);
						} else if (father instanceof Section) {
							Section fatherSection = (Section) father;
							String sql3="select MAX(sequence) as max from section_child";
							Connection conn3=DBHelper.getConnection();
							PreparedStatement ps3=null;
							try {
								ps3 = conn3.prepareStatement(sql3);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ResultSet rs3 = DBHelper
									.executeQuery(conn3,ps3,sql3);
							int seq2 = 1;
							try {
								while (rs3.next()) {
									seq2 = rs3.getInt("max") + 1;
								}
							} catch (SQLException e3) {
								e3.printStackTrace();
							} finally {
								DBHelper.free(rs3,ps3,conn3);
							}
							String sql4 = "insert into section_child(section, paragraph, flag, sequence) values(?,?,?,?)";
							Object[] obj4 = new Object[] { fatherSection.getId(), newChildParagraph.getId(),
									"paragraph", seq2 };
							DBHelper.executeNonQuery(sql4, obj4);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DBHelper.free(rs2,ps2,conn2);
				}
			}
			//System.out.println(newChildParagraph.getDestPassword());
			return newChildParagraph;
		}
		return null;
	}
	
	public GriElement addGriElement(GriElementData3 griElementData3, int userId){//注意这里是关键，添加格元素方法返回newChildSection或newChildParagraph，而这些东西都已经执行了setID方法，因此可以直接用getID获取ID。
		GriDoc belong = griElementData3.getBelong();
		GriElement father = griElementData3.getFather();
		GriElement child = griElementData3.getChild();
		boolean readOnly = docReadOnly(belong.getId(), userId);
		if (readOnly) {
			//LOGGER.info("格文档为只读：[user:{}, gridoc id:{}]", userId, belong.getId());			
			return null;
		}

		if (child instanceof Section) {
			Section childSection = (Section) child;
			Section newChildSection = null;
			String sql = "insert into section(name) values(?)";
			Object[] obj = new Object[] { childSection.getName() };
			int id = DBHelper.executeInsert(sql, obj);//添加节，然后再修改其他关联表
			if (id > 0) {
				String sql2 = "select * from section where id=?";
				Object[] obj2 = new Object[] { id };
				Connection conn=DBHelper.getConnection();
				PreparedStatement ps=null;
				try {
					ps = conn.prepareStatement(sql2);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ResultSet rs2 = DBHelper.executeQuery(conn,ps,sql2, obj2);
				try {
					while (rs2.next()) {
						newChildSection = childSection;
						newChildSection.setId(rs2.getInt("id"));
						newChildSection.setUpdateTime(rs2.getTimestamp("update_time"));

						if (father instanceof GriDoc) {
							GriDoc fatherGridoc = (GriDoc) father;
							String sql3="select MAX(sequence) as max from gridoc_child";
							Connection conn3=DBHelper.getConnection();
							PreparedStatement ps3=null;
							try {
								ps3 = conn3.prepareStatement(sql3);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ResultSet rs3 = DBHelper
									.executeQuery(conn3,ps3,sql3);
							int seq = 1;
							try {
								while (rs3.next()) {
									seq = rs3.getInt("max") + 1;
								}
							} catch (SQLException e3) {
								e3.printStackTrace();
							} finally {
								DBHelper.free(rs3,ps3,conn3);
							}
							String sql4 = "insert into gridoc_child(gridoc, section, flag, sequence) values(?,?,?,?)";
							Object[] obj4 = new Object[] { fatherGridoc.getId(), newChildSection.getId(),
									"section", seq };
							DBHelper.executeNonQuery(sql4, obj4);
						} else if (father instanceof Section) {
							Section fatherSection = (Section) father;
							String sql3="select MAX(sequence) as max from section_child";
							Connection conn3=DBHelper.getConnection();
							PreparedStatement ps3=null;
							try {
								ps3 = conn3.prepareStatement(sql3);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ResultSet rs3 = DBHelper
									.executeQuery(conn3,ps3,sql3);
							int sequence = 1;
							try {
								while (rs3.next()) {
									sequence = rs3.getInt("max") + 1;
								}
							} catch (SQLException e3) {
								e3.printStackTrace();
							} finally {
								DBHelper.free(rs3,ps3,conn3);
							}
							String sql4 = "insert into section_child(section, sub_section, flag, sequence) values(?,?,?,?)";
							Object[] obj4 = new Object[] { fatherSection.getId(), newChildSection.getId(),
									"section", sequence };
							DBHelper.executeNonQuery(sql4, obj4);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DBHelper.free(rs2,ps,conn);
				}

			}
			return newChildSection;
		} else if (child instanceof Paragraph) {//如果child是Paragraph的类型，另外class.isInstance(obj)意思是obj能否转换为class类
			
			Paragraph childParagraph = (Paragraph) child;
			Paragraph newChildParagraph = null;

			String sql = "insert into paragraph(name,keywords,description,"
					+ "sync_time_type,sync_direction_type,warm_sync_detail,data_source_type,data_source_path,"
					+ "cache,last_sync_succeed,last_sync_time,datasource_changed,cache_changed,increase)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTimeStr = sdf.format(new Date());
			String cacheUUID = UUID.randomUUID().toString();//随机生成缓存号
			Object[] obj = new Object[] { childParagraph.getName(), childParagraph.getKeywords(),
					childParagraph.getDescription(), childParagraph.getSyncTimeType(),
					childParagraph.getSyncDirectionType(), childParagraph.getWarmSyncDetail(),
					childParagraph.getDataSourceType(), childParagraph.getDataSourcePath(), cacheUUID, "N",//注意后面的几个字段原paragraph是没有的
					currentTimeStr, "N", "N" ,false};
			int id = DBHelper.executeInsert(sql, obj);//生成段ID
			if (id > 0) {
				
				new CacheManager().initialCache(id, cacheUUID, childParagraph.getDataSourceType(),
						childParagraph.getDataSourcePath());// 初始化缓存文件
				if (childParagraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_1)) {
					new DataSyncTaskManager().addTask(id);
				}
				if(!(childParagraph.getDataDestType().equals("")||childParagraph.getDataDestbase().equals("")
						||childParagraph.getDestAccount().equals("")||childParagraph.getDestIP().equals("")
						||childParagraph.getDestPassword().equals("")||childParagraph.getDestPort().equals(""))){//若选择了物化
					CacheService cs=new CacheService(cacheUUID);
					InputStream input=cs.getInputStream();
					InputStream input2=cs.getInputStream();
					InputStream input3=cs.getInputStream();
					
					String destPath=childParagraph.getDataDestType()+"###"+childParagraph.getDataDestbase()+"###"+childParagraph.getDestAccount()+"###"+
					childParagraph.getDestPassword()+"###"+childParagraph.getDestIP()+"###"+childParagraph.getDestPort();
					
					DestDatabase des=new DestDatabase(childParagraph.getDataSourcePath(),destPath);
					int columnCount=des.getColumnCount(input);
					List<Column> columns=des.initColumn(input2, columnCount);
			       boolean create= des.createTable(columns, columnCount, childParagraph.getName(), false);
			       boolean insert= des.insertOracle(input3,childParagraph.getName());
					if(create&&insert){
					String sql5="insert into dest_info(paragraph_id,data_dest_type,data_dest_base,dest_port,dest_account,dest_password,dest_IP)values(?,?,?,?,?,?,?)";
					Object[] obj5=new Object[] {id,childParagraph.getDataDestType(),childParagraph.getDataDestbase(),
							childParagraph.getDestPort(),childParagraph.getDestAccount(),
							childParagraph.getDestPassword(),childParagraph.getDestIP()};
					DBHelper.executeNonQuery(sql5, obj5);
					}
				}
				String sql2 = "select * from paragraph where id=?";
				Object[] obj2 = new Object[] { id };
				Connection conn2=DBHelper.getConnection();
				PreparedStatement ps2=null;
				try {
					ps2 = conn2.prepareStatement(sql2);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ResultSet rs2 = DBHelper.executeQuery(conn2,ps2,sql2, obj2);
				try {
					while (rs2.next()) {
						newChildParagraph = childParagraph;
						newChildParagraph.setId(rs2.getInt("id"));
						newChildParagraph.setCache(rs2.getString("cache"));
						newChildParagraph.setUpdateTime(rs2.getTimestamp("update_time"));
						if (father instanceof GriDoc) {
							GriDoc fatherGridoc = (GriDoc) father;
							String sql3="select MAX(sequence) as max from gridoc_child";
							Connection conn3=DBHelper.getConnection();
							PreparedStatement ps3=null;
							try {
								ps3 = conn3.prepareStatement(sql3);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ResultSet rs3 = DBHelper
									.executeQuery(conn3,ps3,sql3);
							int seq = 1;
							try {
								while (rs3.next()) {
									seq = rs3.getInt("max") + 1;
								}
							} catch (SQLException e3) {
								e3.printStackTrace();
							} finally {
								DBHelper.free(rs3,ps3,conn3);
							}
							String sql4 = "insert into gridoc_child(gridoc, paragraph, flag, sequence) values(?,?,?,?)";
							Object[] obj4 = new Object[] { fatherGridoc.getId(), newChildParagraph.getId(),
									"paragraph", seq };
							DBHelper.executeNonQuery(sql4, obj4);
						} else if (father instanceof Section) {
							Section fatherSection = (Section) father;
							String sql3="select MAX(sequence) as max from section_child";
							Connection conn3=DBHelper.getConnection();
							PreparedStatement ps3=null;
							try {
								ps3 = conn3.prepareStatement(sql3);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ResultSet rs3 = DBHelper
									.executeQuery(conn3,ps3,sql3);
							int seq2 = 1;
							try {
								while (rs3.next()) {
									seq2 = rs3.getInt("max") + 1;
								}
							} catch (SQLException e3) {
								e3.printStackTrace();
							} finally {
								DBHelper.free(rs3,ps3,conn3);
							}
							String sql4 = "insert into section_child(section, paragraph, flag, sequence) values(?,?,?,?)";
							Object[] obj4 = new Object[] { fatherSection.getId(), newChildParagraph.getId(),
									"paragraph", seq2 };
							DBHelper.executeNonQuery(sql4, obj4);
						}
						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DBHelper.free(rs2,ps2,conn2);
				}
			
			}
			return newChildParagraph;
		}
		return null;
	}
	
	
	public boolean delGriElement(GriElementData2 griElementData2,  int userId){
		GriDoc belong = griElementData2.getBelong();
		GriElement griElement = griElementData2.getGriElement();
		boolean success = false;
		boolean readOnly = docReadOnly(belong.getId(), userId);
		if (readOnly) {
			//LOGGER.info("格文档为只读：[user:{}, gridoc id:{}]", order.from, belong.getId());
			return false;
		}
		if (griElement instanceof Section) {
			Section section = (Section) griElement;
			String sql = "delete from section where id=?";
			Object[] obj = new Object[] { section.getId() };
			if (DBHelper.executeNonQuery(sql, obj) > 0)
				success = true;
		} else if (griElement instanceof Paragraph) {
			Paragraph paragraph = (Paragraph) griElement;
			CacheService cacheService = null;
			{
				String sql2 = "select * from paragraph where id=?";
				Object[] obj2 = new Object[] { paragraph.getId() };
				Connection conn=DBHelper.getConnection();
				PreparedStatement ps=null;
				try {
					ps = conn.prepareStatement(sql2);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ResultSet rs2 = DBHelper.executeQuery(conn,ps,sql2, obj2);
				String cacheUUID = "";
				try {
					while (rs2.next()) {
						cacheUUID = rs2.getString("cache");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}finally {
					DBHelper.free(rs2,ps,conn);
				}
				cacheService = new CacheService(cacheUUID);
			}

			String sql = "delete from paragraph where id=?";
			Object[] obj = new Object[] { paragraph.getId() };
			if (DBHelper.executeNonQuery(sql, obj) > 0) {
				success = true;
				cacheService.deleteCache(); // 同时删除缓存、预览文件
				if (paragraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_1)) {
					DataSyncTaskManager dataSyncTaskManager = new DataSyncTaskManager();
					dataSyncTaskManager.init();
					dataSyncTaskManager.shutdown();
					dataSyncTaskManager.addAllTask();
					dataSyncTaskManager.addAllTask2();
					dataSyncTaskManager.addAllTask3();
				}
			}
		}
		return success;
	}

	public void removeUnreferenceGriElement(){
		String sql_delete_section = "DELETE FROM section WHERE id NOT IN "
				+ "(SELECT sub_section AS id FROM section_child	WHERE flag = 'section' "
				+ "UNION SELECT	section AS id FROM gridoc_child	WHERE flag = 'section') ";
		if (DBHelper.executeNonQuery(sql_delete_section) > 0) {
			String sql_quety_para = "select * FROM paragraph WHERE id NOT IN "
					+ "(SELECT paragraph AS id FROM section_child WHERE flag = 'paragraph' "
					+ "UNION SELECT	paragraph AS id	FROM gridoc_child WHERE flag = 'paragraph') ";
			boolean needRebootTimerTask = false;
			Connection conn=DBHelper.getConnection();
			PreparedStatement ps=null;
			try {
				ps = conn.prepareStatement(sql_quety_para);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultSet rs = DBHelper.executeQuery(conn,ps,sql_quety_para);
			try {
				while (rs.next()) {
					CacheService cacheService = new CacheService(rs.getString("cache"));
					cacheService.deleteCache();// 同时删除缓存、预览文件、索引
					if (rs.getString("sync_time_type").equals(DriverConstant.SyncTimeType_1))
						needRebootTimerTask = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				DBHelper.free(rs,ps,conn);
			}

			String sql_delete_para = "delete FROM paragraph WHERE id NOT IN "
					+ "(SELECT paragraph AS id FROM section_child WHERE flag = 'paragraph' "
					+ "UNION SELECT	paragraph AS id	FROM gridoc_child WHERE flag = 'paragraph') ";
			if (DBHelper.executeNonQuery(sql_delete_para) > 0 && needRebootTimerTask) {
				DataSyncTaskManager dataSyncTaskManager = new DataSyncTaskManager();
				dataSyncTaskManager.init();
				dataSyncTaskManager.shutdown();
				dataSyncTaskManager.addAllTask();
			}
		}
	}

	public boolean remGriElement(GriElementData2 griElementData2,  int userId){//这个方法也没被实际使用到
		GriDoc belong = griElementData2.getBelong();
		GriElement griElement = griElementData2.getGriElement();
		boolean success = false;
		boolean readOnly = docReadOnly(belong.getId(), userId);
		if (readOnly) {
			//LOGGER.info("格文档为只读：[user:{}, gridoc id:{}]", userId, belong.getId());
			return false;
		}
		if (griElement instanceof Section) {
			Section section = (Section) griElement;
			{
				int id = 0;
				String sql = "select id from gridoc_child where flag=? and section=?";
				Object[] obj = new Object[] { "section", section.getId() };
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
						id = rs.getInt("id");
						break;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DBHelper.free(rs,ps,conn);
				}
				if (id > 0) {
					String sql1 = "delete from gridoc_child where id=?";
					Object[] obj1 = new Object[] { id };
					if (DBHelper.executeNonQuery(sql1, obj1) > 0)
						success = true;
				}
			}
			{
				int id = 0;
				String sql = "select id from section_child where flag=? and sub_section=?";
				Object[] obj = new Object[] { "section", section.getId() };
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
						id = rs.getInt("id");
						break;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DBHelper.free(rs,ps,conn);
				}
				if (id > 0) {
					String sql1 = "delete from section_child where id=?";
					Object[] obj1 = new Object[] { id };
					if (DBHelper.executeNonQuery(sql1, obj1) > 0)
						success = true;
				}
			}
		} else if (griElement instanceof Paragraph) {
			Paragraph paragraph = (Paragraph) griElement;
			{
				int id = 0;
				String sql = "select id from gridoc_child where flag=? and section=?";
				Object[] obj = new Object[] { "paragraph", paragraph.getId() };
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
						id = rs.getInt("id");
						break;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DBHelper.free(rs,ps,conn);
				}
				if (id > 0) {
					String sql1 = "delete from gridoc_child where id=?";
					Object[] obj1 = new Object[] { id };
					if (DBHelper.executeNonQuery(sql1, obj1) > 0)
						success = true;
				}
			}
			{
				int id = 0;
				String sql = "select id from section_child where flag=? and sub_section=?";
				Object[] obj = new Object[] { "paragraph", paragraph.getId() };
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
						id = rs.getInt("id");
						break;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DBHelper.free(rs,ps,conn);
				}
				if (id > 0) {
					String sql1 = "delete from section_child where id=?";
					Object[] obj1 = new Object[] { id };
					if (DBHelper.executeNonQuery(sql1, obj1) > 0)
						success = true;
				}
			}
		}
		return success;
	}
	
	public List<Paragraph> getParagraphOfGriElement(GriElement root) {//这个方法其实没有被实际调用到
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		getParagraphOfGriElementHelper(root, paragraphs);
		return paragraphs;
	}

	private void getParagraphOfGriElementHelper(GriElement root, List<Paragraph> paragraphs) {
		if (root instanceof Paragraph)
			paragraphs.add((Paragraph) root);
		else if (root instanceof GriDoc)
			for (GriElement child : getChildrenOfGriDoc((GriDoc) root))
				getParagraphOfGriElementHelper(child, paragraphs);
		else if (root instanceof Section)
			for (GriElement child : getChildrenOfSection((Section) root))
				getParagraphOfGriElementHelper(child, paragraphs);
	}
	
	public boolean updateGriElement(GriElementData2 griElementData2, int userId) {
		GriDoc belong = griElementData2.getBelong();
		GriElement griElement = griElementData2.getGriElement();
		boolean success = false;
		boolean readOnly = docReadOnly(belong.getId(), userId);
		if (readOnly) {
			// LOGGER.info("格文档为只读：[user:{}, gridoc id:{}]", order.from, belong.getId());
			return false;
		}
		if (griElement instanceof Section) {
			Section section = (Section) griElement;
			String sql = "update section set name=? where id=?";
			Object[] obj = new Object[] { section.getName(), section.getId() };
			if (DBHelper.executeNonQuery(sql, obj) > 0)
				success = true;
		} else if (griElement instanceof Paragraph) {
			Paragraph paragraph = (Paragraph) griElement;
			String oldSyncType = "";
			String oldSyncWarmInfo = "";
			{
				String sql1 = "select * from paragraph where id=?";
				Connection conn=DBHelper.getConnection();
				PreparedStatement ps=null;
				try {
					ps = conn.prepareStatement(sql1);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ResultSet rs1 = DBHelper.executeQuery(conn,ps,sql1, new Object[] { paragraph.getId() });
				try {
					while (rs1.next()) {
						oldSyncType = rs1.getString("sync_time_type");
						oldSyncWarmInfo = rs1.getString("warm_sync_detail");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DBHelper.free(rs1,ps,conn);
				}
			}

			if (!(paragraph.getDataDestType().equals("") || paragraph.getDataDestbase().equals("")
					|| paragraph.getDestAccount().equals("") || paragraph.getDestIP().equals("")
					|| paragraph.getDestPassword().equals("") || paragraph.getDestPort().equals(""))) {// 若选择了物化
				CacheService cs = new CacheService(paragraph.getCache());
				InputStream input = cs.getInputStream();
				InputStream input2 = cs.getInputStream();
				InputStream input3 = cs.getInputStream();

				String destPath = paragraph.getDataDestType() + "###" + paragraph.getDataDestbase() + "###"
						+ paragraph.getDestAccount() + "###" + paragraph.getDestPassword() + "###"
						+ paragraph.getDestIP() + "###" + paragraph.getDestPort();

				DestDatabase des = new DestDatabase(paragraph.getDataSourcePath(), destPath);
				int columnCount = des.getColumnCount(input);
				List<Column> columns = des.initColumn(input2, columnCount);
				String sourceBaseType = paragraph.getDataSourcePath().split("###")[0];
				String destBaseType = paragraph.getDataDestType();
				boolean create = false;
				boolean insert = false;
				if (destBaseType.equalsIgnoreCase("mysql")) {
					create = des.createTable(columns, columnCount, paragraph.getName(), false);
					insert = des.insertMySql3(input3, paragraph.getName());
				} else if (destBaseType.equalsIgnoreCase("Oracle")) {
					create = des.createOracleTable(columns, columnCount, paragraph.getName(), false);
					insert = des.insertOracle(input3, paragraph.getName());
				}

				if (!(create && insert)) {
					return false;
				} else {
//					String sql5 = "insert into dest_info(paragraph_id,data_dest_type,data_dest_base,dest_port,dest_account,dest_password,dest_IP)values(?,?,?,?,?,?,?)";
					String sql6="select * from dest_info where paragraph_id=?";
					Object[] obj6=new Object[] {paragraph.getId()};
					if(DBHelper.executeNonQuery(sql6,obj6)>0) {
						String sql5="update dest_info set data_dest_type=?,data_dest_base=?,dest_port=?,dest_account=?,dest_password=?,dest_IP=? where paragraph_id=?";
						Object[] obj5 = new Object[] { paragraph.getDataDestType(),
								paragraph.getDataDestbase(), paragraph.getDestPort(), paragraph.getDestAccount(),
								paragraph.getDestPassword(), paragraph.getDestIP(),paragraph.getId() };
						DBHelper.executeNonQuery(sql5, obj5);
					}
					else {
						String sql5 = "insert into dest_info(paragraph_id,data_dest_type,data_dest_base,dest_port,dest_account,dest_password,dest_IP)values(?,?,?,?,?,?,?)";
						Object[] obj5 = new Object[] { paragraph.getId(),paragraph.getDataDestType(),
								paragraph.getDataDestbase(), paragraph.getDestPort(), paragraph.getDestAccount(),
								paragraph.getDestPassword(), paragraph.getDestIP()};
						DBHelper.executeNonQuery(sql5, obj5);
					}
					String sql5="update dest_info set data_dest_type=?,data_dest_base=?,dest_port=?,dest_account=?,dest_password=?,dest_IP=? where paragraph_id=?";
					Object[] obj5 = new Object[] { paragraph.getDataDestType(),
							paragraph.getDataDestbase(), paragraph.getDestPort(), paragraph.getDestAccount(),
							paragraph.getDestPassword(), paragraph.getDestIP(),paragraph.getId() };
					DBHelper.executeNonQuery(sql5, obj5);
				}
			}

			String sql = "update paragraph set name=?,keywords=?,description=?,"
					+ "sync_time_type=?,sync_direction_type=?,warm_sync_detail=?,"
					+ "data_source_type=?,data_source_path=? where id=?";
			Object[] obj = new Object[] { paragraph.getName(), paragraph.getKeywords(), paragraph.getDescription(),
					paragraph.getSyncTimeType(), paragraph.getSyncDirectionType(), paragraph.getWarmSyncDetail(),
					paragraph.getDataSourceType(), paragraph.getDataSourcePath(), paragraph.getId() };
			if (DBHelper.executeNonQuery(sql, obj) > 0) {
				success = true;
				if ((paragraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_1)
						&&oldSyncType.equals(DriverConstant.SyncTimeType_1)
						&& !paragraph.getWarmSyncDetail().equals(oldSyncWarmInfo))
						|| (oldSyncType.equals(DriverConstant.SyncTimeType_2)
								&& paragraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_1))
						|| (oldSyncType.equals(DriverConstant.SyncTimeType_1)
								&& !paragraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_1))) {
					DataSyncTaskManager dataSyncTaskManager = new DataSyncTaskManager();
					dataSyncTaskManager.init();
					dataSyncTaskManager.shutdown();
					dataSyncTaskManager.addAllTask();
				}
			}
		}
		return success;
	}
	
}

