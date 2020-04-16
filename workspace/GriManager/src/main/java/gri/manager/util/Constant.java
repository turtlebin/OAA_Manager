package gri.manager.util;

import java.io.File;
import java.util.Properties;

import gri.driver.util.DriverConstant;

public class Constant {

	public static final int WindowType_Add = 1;
	public static final int WindowType_Edit = 2;
	public static final int WindowType_Property = 3;

	public static boolean run_process;
	public static int eitpServerPort = 9010; // EITP Server端口

	// 数格引擎本地格文档信息数据库存储配置
	public static String driver;// 驱动
	public static String url;// 数据库
	public static String url2;
	public static String user; // 用户名
	public static String password; // 密码

	public static final String local_eitp_user = DriverConstant.GriEngineAccount;
	public static final String local_eitp_password = DriverConstant.GriEngineAccount;

	// 数据缓存存放路径
	public static String CacheFolder;
	
	public static String kafka_server;
	public static String redis_server;
	
	public static String ObjectFolder;
	
	//数据分片阈值
	public static int threshold;
	
	//是否使用数据分片
	public static Boolean data_fragment;
	
	public static String propertiesFolder;

	static {
		Properties prop = new Properties();
		try {
			prop.load(Constant.class.getResourceAsStream("/config.properties"));//此处加载config文件的配置信息，从而使得prop可以获取配置信息
			run_process = Boolean.valueOf(prop.getProperty("run_process"));
			eitpServerPort = Integer.valueOf(prop.getProperty("eitp_port"));
			driver = prop.getProperty("driver");
			url = prop.getProperty("url");
			url2=prop.getProperty("url2");
			user = prop.getProperty("user");
			password = prop.getProperty("password");
			CacheFolder = prop.getProperty("cache_file_path");
			kafka_server = prop.getProperty("kafka_server");
			redis_server = prop.getProperty("redis_server");
			ObjectFolder=prop.getProperty("object_file_path");
			threshold=Integer.parseInt(prop.getProperty("threshold"));
			data_fragment=prop.getProperty("data_fragment").equalsIgnoreCase("true")?true:false;
			propertiesFolder=prop.getProperty("file_path");

			File file = new File(CacheFolder);
			file.mkdirs();
			File file2=new File(ObjectFolder);
			file2.mkdirs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
