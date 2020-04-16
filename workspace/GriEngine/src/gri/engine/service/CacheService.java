package gri.engine.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gri.engine.core.DataChangeListener;
import gri.engine.model.DataChangeEvent;
import gri.engine.parse.ParseException;
import gri.engine.parse.TextParser;
import gri.engine.util.Constant;
import gri.engine.util.DBHelper;

public class CacheService {//主要负责读写缓存的操作
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheService.class);

	private String cacheUUID;
	private Integer paragraphID;
	private Set<EventListener> dataChangeListeners;

	// 主要给数格引擎用
	public CacheService(String cacheUUID) {
		this.cacheUUID = cacheUUID;
		this.dataChangeListeners = new HashSet<EventListener>();
	}

	// 主要给客户端用
	public CacheService(String cacheUUID, Integer paragraphId) {
		this.cacheUUID = cacheUUID;
		this.paragraphID = paragraphId;
		this.dataChangeListeners = new HashSet<EventListener>();
	}

	// 保证缓存文件存在
	private void createCacheIfNotExist() {
		File file = new File(Constant.CacheFolder + cacheUUID);
		try {
			if (!file.exists())
				file.createNewFile();
			else if (file.isDirectory()) {
				file.delete();
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public InputStream getInputStream() {
		this.createCacheIfNotExist();
		try {
			InputStream in = new FileInputStream(new File(Constant.CacheFolder + cacheUUID));
			return in;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public OutputStream getOutputStream() {
		this.createCacheIfNotExist();
		try {
			OutputStream out = new FileOutputStream(new File(Constant.CacheFolder + cacheUUID));
			return out;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteCache() {
		File file = new File(Constant.CacheFolder + cacheUUID);
		if (file.exists())
			file.delete(); // 删除缓存
		File preFile = new File(Constant.CacheFolder + cacheUUID + ".txt");
		if (preFile.exists())
			preFile.delete(); // 删除预览文件
		IndexService is = new IndexService();
		is.delelteIndex(cacheUUID); // 删除索引
	}

	// 缓存数据（字节）大小
	public int cacheSize() {
		this.createCacheIfNotExist();
		int size = 0;
		try {
			FileInputStream in = new FileInputStream(Constant.CacheFolder + cacheUUID);
			size = in.available();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 
	 * @param cacheUUID
	 *            //缓存数据
	 * @param bytes
	 *            //缓冲区
	 * @param pos
	 *            //缓存数据起始位置
	 * @return 字节大小
	 */
	public int readCache(byte[] bytes, int pos) {
		LOGGER.info("readCache: [UUID:{}, position:{}, size:{}]", cacheUUID, pos, bytes.length);
		this.createCacheIfNotExist();
		int size = -1; // end
		try {
			FileInputStream in = new FileInputStream(new File(Constant.CacheFolder + cacheUUID));
			in.skip(pos);
			size = in.read(bytes);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return size;
	}

	public String readParagraphPreview() {
		TextParser tp = new TextParser();
		String text = null;
		try {
			text = tp.renderText(new File(Constant.CacheFolder + cacheUUID + ".txt"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return text;
	}

	// 写缓存。事件源。
	public boolean writeCache(byte[] bytes, boolean append) {
		LOGGER.info("writeCache: [UUID:{}, size:{}, append:{}]", cacheUUID, bytes.length, append);
		this.createCacheIfNotExist();
		FileOutputStream out;
		try {
			out = new FileOutputStream(Constant.CacheFolder + cacheUUID, append);
			out.write(bytes);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (!append)
			this.notifies();// 缓存数据被外人修改，通知监听者
		return true;
	}

	// 通知监听者
	private void notifies() {
		Iterator<EventListener> iterator = this.dataChangeListeners.iterator();
		while (iterator.hasNext()) {
			DataChangeListener listener = (DataChangeListener) iterator.next();
			listener.DataChanged(new DataChangeEvent(this));
		}
	}

	public String getCacheUUID() {
		return cacheUUID;
	}

	public Integer getParagraphID() {
		if (this.paragraphID != null)
			return this.paragraphID;
		else {
			int id = -1;
			String sql="select * from paragraph where cache=?";
			Connection conn=DBHelper.getConnection();
			PreparedStatement ps=null;
			try {
				ps = conn.prepareStatement(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultSet rs = DBHelper.executeQuery(conn,ps,sql,
					new Object[] { this.cacheUUID });
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
	}

	public void addListenner(EventListener listener) {
		this.dataChangeListeners.add(listener);
	}

	@Override
	public String toString() {
		return "CacheService [cacheUUID=" + cacheUUID + ", paragraphID=" + paragraphID + ", dataChangeListeners="
				+ dataChangeListeners + "]";
	}

}
