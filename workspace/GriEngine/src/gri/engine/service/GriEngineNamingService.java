package gri.engine.service;

import csAsc.EIB.Engine.Authentication;
import csAsc.EIB.ServerEngine.INamingService;
import gri.driver.util.DriverConstant;
import gri.engine.util.Constant;
import gri.engine.util.DBHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * 格文档系统接入EITP消息服务使用的账号和密码<br>
 *
 */
public class GriEngineNamingService implements INamingService, Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(GriEngineNamingService.class);

	private Map<String, Long> accout2id = new Hashtable<String, Long>();
	private Map<Long, String> id2account = new Hashtable<Long, String>();

	private Map<String, String> user2password = new HashMap<String, String>();// 缓存数据库用户密码

	public void clearUser2PasswordCache() {
		this.user2password.clear();
	}

	public GriEngineNamingService() {

	}

	/**
	 * 根据会话ID查找帐号
	 *
	 * @param id
	 *            会话ID
	 * @return 帐号
	 */
	@Override
	public String getAccountById(long id) {
		return id2account.get(id);
	}

	/**
	 * 给会话ID注册帐号
	 * <p>
	 * <b>无认证</b>
	 * </p>
	 *
	 * @param id
	 *            会话ID
	 * @return 自动生成的账号
	 */
	@Override
	public String registerAccount(long id) {
		return null;
	}

	/**
	 * 给会话ID注册用户提供的帐号
	 * <p>
	 * <b>临时认证</b>
	 * </p>
	 *
	 * @param account
	 *            用户提供的帐号
	 * @param id
	 *            会话ID
	 * @return 若注册成功, 返回用户提供的帐号, 否则返回一个自动生成的帐号
	 */
	@Override
	public String registerAccount(String account, long id) {
		return null;
	}

	/**
	 * 给会话ID注册用户提供的帐号,匹配密码的才注册成功,否则注册失败
	 * <p>
	 * <b>基本认证</b>
	 * </p>
	 *
	 * @param account
	 *            用户提供的帐号
	 * @param password
	 *            用户提供的密码
	 * @param id
	 *            会话ID
	 * @return 若注册成功, 返回用户提供的帐号, 否则返回空字符串
	 */
	// @Override
	// public String registerAccount(String account, String password, long id) {
	//
	// // 数格引擎本地帐号开绿灯
	// if (account.equals(Constant.local_eitp_user) &&
	// password.equals(Constant.local_eitp_password)) {
	// accout2id.put(account, id);
	// id2account.put(id, account);
	// LOGGER.info("成功注册一个帐号:" + account);
	// return account;
	// }
	//
	// // 特殊临时一次连接帐号开绿灯
	// if (account.startsWith(DriverConstant.TempConnctionAccoutPrefix)
	// && (account.length() - DriverConstant.TempConnctionAccoutPrefix.length()) ==
	// UUID.randomUUID()
	// .toString().length()) {
	// accout2id.put(account, id);
	// id2account.put(id, account);
	// LOGGER.info("成功注册一个帐号:" + account);
	// return account;
	// }
	// String pw = null;
	// Object pass_obj = this.user2password.get(account);
	//
	// if (pass_obj == null) {
	// String sql = "select * from user where account = ?";
	// Object[] obj = new Object[] { account };
	// Connection conn=DBHelper.getConnection();
	// PreparedStatement ps=null;
	// try {
	// ps = conn.prepareStatement(sql);
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// ResultSet rs = DBHelper.executeQuery(conn,ps,sql, obj);
	// try {
	// while (rs.next()) {
	// pw = rs.getString(3);
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// DBHelper.free(rs,ps,conn);
	// }
	// if (pw != null)
	// user2password.put(account, pw);
	//
	// } else
	// pw = (String) pass_obj;
	// if (pw != null && pw.equals(password)) {
	// accout2id.put(account, id);
	// id2account.put(id, account);
	// LOGGER.info("成功注册一个帐号:" + account);
	// return account;
	// } else {
	// LOGGER.info("帐号[" + account + "]注册失败");
	// return null;
	// }
	// }
	@Override
	public String registerAccount(String account, String password, long id) {

		// 数格引擎本地帐号开绿灯
		if (account.equals(Constant.local_eitp_user) && password.equals(Constant.local_eitp_password)) {
			accout2id.put(account, id);
			id2account.put(id, account);
			LOGGER.info("成功注册一个帐号:" + account);
			return account;
		}

		// 特殊临时一次连接帐号开绿灯
		if (account.startsWith(DriverConstant.TempConnctionAccoutPrefix)
				&& (account.length() - DriverConstant.TempConnctionAccoutPrefix.length()) == UUID.randomUUID()
						.toString().length()) {
			accout2id.put(account, id);
			id2account.put(id, account);
			LOGGER.info("成功注册一个帐号:" + account);
			return account;
		}
		String pw = null;
		Object pass_obj = this.user2password.get(account);

		if (pass_obj == null) {
			String sql = "select * from user where account = ?";
			Object[] obj = new Object[] { account };
			Connection conn = DBHelper.getConnection();
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultSet rs = DBHelper.executeQuery(conn, ps, sql, obj);
			try {
				while (rs.next()) {
					pw = rs.getString(3);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBHelper.free(rs, ps, conn);
			}
			if (pw != null)
				user2password.put(account, pw);

		} else
			pw = (String) pass_obj;
		if (pw != null && pw.equals(password)) {
			accout2id.put(account, id);
			id2account.put(id, account);
			LOGGER.info("成功注册一个帐号:" + account);
			return account;
		} else if (!pw.equals(password)) {
			String pw_new = null;
			String sql = "select * from user where account = ?";
			Object[] obj = new Object[] { account };
			Connection conn = DBHelper.getConnection();
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultSet rs = DBHelper.executeQuery(conn, ps, sql, obj);
			try {
				while (rs.next()) {
					pw_new = rs.getString(3);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBHelper.free(rs, ps, conn);
			}
			if (pw != pw_new) {
				user2password.put(account, pw_new);
			}
			return account;
		} else {
			LOGGER.info("帐号[" + account + "]注册失败");
			return null;
		}
	}
//	@Override
//	public String registerAccount(String account, String password, long id) {
//
//		// 数格引擎本地帐号开绿灯
//		if (account.equals(Constant.local_eitp_user) && password.equals(Constant.local_eitp_password)) {
//			accout2id.put(account, id);
//			id2account.put(id, account);
//			LOGGER.info("成功注册一个帐号:" + account);
//			return account;
//		}
//
//		// 特殊临时一次连接帐号开绿灯
//		if (account.startsWith(DriverConstant.TempConnctionAccoutPrefix)
//				&& (account.length() - DriverConstant.TempConnctionAccoutPrefix.length()) == UUID.randomUUID()
//						.toString().length()) {
//			accout2id.put(account, id);
//			id2account.put(id, account);
//			LOGGER.info("成功注册一个帐号:" + account);
//			return account;
//		}
//		String pw = null;
//		Object pass_obj = this.user2password.get(account);
//
//		if (pass_obj == null) {
//			String sql = "select * from user where username = ?";
//			Object[] obj = new Object[] { account };
//			Connection conn=DBHelper.getConnection2();
//			PreparedStatement ps=null;
//			try {
//				ps = conn.prepareStatement(sql);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			ResultSet rs = DBHelper.executeQuery(conn,ps,sql, obj);
//			try {
//				while (rs.next()) {
//					pw = rs.getString(4);
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			} finally {
//				DBHelper.free(rs,ps,conn);
//			}
//			if (pw != null)
//				user2password.put(account, pw);
//
//		} else
//			pw = (String) pass_obj;
//		if (pw != null && pw.equals(password)) {
//			accout2id.put(account, id);
//			id2account.put(id, account);
//			LOGGER.info("成功注册一个帐号:" + account);
//			return account;
//		} else {
//			LOGGER.info("帐号[" + account + "]注册失败");
//			return null;
//		}
//	}
//	
	/**
	 * 根据帐号获取会话ID
	 *
	 * @param account
	 *            帐号
	 * @return 如果存在返回会话ID,否则返回null
	 */
	@Override
	public Long getIdByAccount(String account) {
		return accout2id.get(account);
	}

	/**
	 * 退出登录
	 *
	 * @param id
	 *            会话ID
	 */
	@Override
	public void signoutAccount(long id) {
		String account = getAccountById(id);
		if (account == null)
			return;
		LOGGER.debug("帐号" + "[" + account + "]退出登录");
		accout2id.remove(account);
		id2account.remove(id);
	}

	@Override
	public boolean applyAccount(Authentication arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
