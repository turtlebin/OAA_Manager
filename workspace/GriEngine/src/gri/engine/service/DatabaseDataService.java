package gri.engine.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import gri.engine.dest.FileType;
import gri.engine.dest.TypeMap;
import gri.engine.model.dao.ProcessDao;
import gri.engine.process.KafkaProcessPipe;
import gri.engine.process.ProcessPipe;
import gri.engine.process.MessagePack;
import gri.driver.model.process.Paragraph;
import gri.driver.model.process.Process;
import gri.engine.util.Constant;
import gri.engine.util.SQLParser;

public class DatabaseDataService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseDataService.class);

	private Integer paragraphID;
	private String dataSourcePath;

	private String type; // { "mysql", "sql server", "oracle" ,"db2"}
	private String host;
	private String port;
	private String db_name;
	private String username;
	private String password;
	private String sql;

	private String driver;
	private String url; // 数据库连接字符串
	private String tableName;// 数据库表名

	public DatabaseDataService(String dataSourceURL, Integer paragraphID) {
		super();
		this.dataSourcePath = dataSourceURL;
		this.paragraphID = paragraphID;
	}

	private boolean analyzeParameter() {
		try {
			String strs[] = this.dataSourcePath.split("###");
			this.type = strs[0];
			this.host = strs[1];
			this.port = strs[2];
			this.db_name = strs[3];
			this.username = strs[4];// 由传入的数据源路径获取用户名和密码
			this.password = strs[5];
			this.sql = strs[6];

			List<String> tables = SQLParser.getTables(this.sql);
			if (tables.size() == 0 || tables.get(0).equals(""))
				return false;

			this.tableName = tables.get(0);
			// LOGGER.info("table:" + this.tableName);

			if (type.equalsIgnoreCase("MySQL")) {
				this.driver = "com.mysql.jdbc.Driver";
				this.url = "jdbc:mysql://" + this.host + ":" + port + "/" + this.db_name+"?relaxAutoCommit=true&zeroDateTimeBehavior=convertToNull";// 生成url
			} else if (type.equalsIgnoreCase("SQL Server")) {
				this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
				this.url = "jdbc:sqlserver://" + this.host + ":" + this.port + ";DatabaseName="
						+ this.db_name;
			} else if (type.equalsIgnoreCase("Oracle")) {
				this.driver = "oracle.jdbc.driver.OracleDriver";
				this.url = "jdbc:oracle:thin:@" + this.host + ":" + this.port + ":" + this.db_name;
			} else if (type.equalsIgnoreCase("DB2")) {
				this.driver = "com.ibm.db2.jdbc.app.DB2.Driver";
				this.url = "jdbc:db2://" + this.host + ":" + port + "/" + db_name;
			}else if(type.equalsIgnoreCase("SYBASE")) { 
				this.driver="com.sybase.jdbc3.jdbc.SybDriver";
				this.url="jdbc:sybase:Tds:"+this.host+":"+port+"/"+this.db_name;
			}else
				return false;
			// LOGGER.info("database url:" + this.url);

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
			Class.forName(driver);// 加载数据库驱动
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		try {
			
		
			Connection conn = DriverManager.getConnection(this.url, this.username,this.password);
              //Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "csasc");
			// 采用流式读取的方法,优化mysql在海量数据读取时的读取速度，
			// 参考 http://wentuotuo.com/2015/12/25/Java/use-cursor-read-bigdata/
			
		    ResultSet rs=null;
			if(type.equalsIgnoreCase("MySQL")){
				com.mysql.jdbc.Statement state=(com.mysql.jdbc.Statement)conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
				state.setFetchSize(1000);
				state.enableStreamingResults();
				this.sql+=" limit 10";
				rs=state.executeQuery(this.sql);
				}else {
					Statement state=conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
					state.setFetchSize(1000);
				    rs=state.executeQuery(this.sql);
				}
			/*
			 * Statement
			 * stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
			 * ResultSet.CONCUR_READ_ONLY);
			 */
			

			ResultSetMetaData rsm = rs.getMetaData();
		   
			int colNum = rsm.getColumnCount();
			String colName[] = new String[colNum];
			int precision[] = new int[colNum];
			String colType[] = new String[colNum];
			Boolean signed[] = new Boolean[colNum];
			String table[] = new String[colNum];
			int scale[]=new int[colNum];
			for (int i = 0; i < colNum; i++) {
				colName[i] = rsm.getColumnName(i + 1);
				colType[i] = rsm.getColumnTypeName(i + 1);
//				if(TypeMap.isNumber(colType[i])){
//				precision[i]=rsm.getPrecision(i+1);
//				scale[i]=rsm.getScale(i+1);
//				}else{
//					precision[i]=0;
//					scale[i]=0;
//				}
				precision[i]=rsm.getPrecision(i+1);
				scale[i]=rsm.getPrecision(i+1);
				signed[i] = rsm.isSigned(i + 1);
				table[i] = rsm.getTableName(i + 1);
			}

			// rs.beforeFirst();

			/*
			 * JsonArray array = new JsonArray(); while (rs.next()) { JsonObject
			 * jsObj = new JsonObject(); for (int i = 0; i < colNum; i++) {
			 * jsObj.addProperty(colName[i], rs.getString(i + 1)); }
			 * array.add(jsObj); } LOGGER.info("数据库数据用json字符串表示为：" + array);
			 * 
			 * BufferedWriter bw = new BufferedWriter(new
			 * OutputStreamWriter(out)); bw.write(array.toString());
			 */

			// 把一次性拼成字符串改成，分几次写入
			int count = 1;
			String buffer = "";
			String introduce = "";
			boolean has_introduce = false;
			int BLOCKSIZE = 4000;
			String type;
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));// 此时连接数据源，将数据源的数据写入缓存
			bw.write("{\"attr\":");
			String exjsonStr = "{";
			for (int i = 0; i < colNum; i++) {
				exjsonStr += "\"" + colName[i] + "\"" + ":";
				if(colType[i].indexOf("DATE")!=-1&&(this.type.equalsIgnoreCase("ORACLE"))){
					exjsonStr+="\"DATETIME\",";
				}else{
				exjsonStr += "\"" + colType[i] + "\",";
				}
			}
			if (exjsonStr.length() > 1) {
				exjsonStr = exjsonStr.substring(0, exjsonStr.length() - 1);
			}
			exjsonStr += "}," + "\"keys\":{\"key\":" + getKeysString(this.tableName);

			exjsonStr += "},\"signed\":{";
			for (int i = 0; i < colNum; i++) {
				exjsonStr += "\"" + colName[i] + "\"" + ":";
				exjsonStr += "\"" + signed[i] + "\",";
			}
			if (exjsonStr.length() > 1) {
				exjsonStr = exjsonStr.substring(0, exjsonStr.length() - 1);
			}
			exjsonStr += "},\"precision\":{";
			for (int i = 0; i < colNum; i++) {
				exjsonStr += "\"" + colName[i] + "\"" + ":";
				exjsonStr += "\"" + precision[i] + "\",";
			}
			if (exjsonStr.length() > 1) {
				exjsonStr = exjsonStr.substring(0, exjsonStr.length() - 1);
			}
			exjsonStr += "},\"scale\":{";
			for (int i = 0; i < colNum; i++) {
				exjsonStr += "\"" + colName[i] + "\"" + ":";
				exjsonStr += "\"" + scale[i] + "\",";
			}
			if (exjsonStr.length() > 1) {
				exjsonStr = exjsonStr.substring(0, exjsonStr.length() - 1);
			}
			exjsonStr += "},\"table\":{";
			for (int i = 0; i < colNum; i++) {
				exjsonStr += "\"" + colName[i] + "\"" + ":";
				exjsonStr += "\"" + table[i] + "\",";
			}
			if (exjsonStr.length() > 1) {
				exjsonStr = exjsonStr.substring(0, exjsonStr.length() - 1);
			}
			exjsonStr += "},\"data\":";
			bw.write(exjsonStr);
			bw.write("[");
			ProcessPipe processPipe = new KafkaProcessPipe();
			// Paragraph paragraph = new
			// ProcessDao().getParagraphById(paragraphID);
			String content = "[";
		
			// 硬盘块大小\
			String uuid=null;
			int b=0;
			while(rs.next()) {
				
				if(b>=10) {
					break;
				}
				b++;
				
				// 一次写一个硬盘块，以优化写入速度
				if (buffer.getBytes().length > BLOCKSIZE) {// 缓存足够长时才写入硬盘块，一次写4000字节
					if (!has_introduce) {
						if (buffer.length() > 100) {
							introduce = buffer.substring(0, 100) + "...";
						}
						introduce = buffer;
						has_introduce = true;
					}
					if (!buffer.equals("")) {
						bw.write(buffer);
						buffer = "";
					}
				}

				/*
				 * JsonObject jsObj = new JsonObject(); for (int i = 0; i <
				 * colNum; i++) { jsObj.addProperty(colName[i], rs.getString(i +
				 * 1)); }
				 */

				// 自己拼json，提高效率
				String jsonStr = "{";// 这部分读取数据源的数据并且以json格式保存下来
				String temp="";
				String filePath="";
				File file;
				
				for (int i = 0;  i< colNum; i++) {
					jsonStr += "\"" + colName[i] + "\"" + ":";
				
					if(colType[i].indexOf("BLOB")!=-1){//对BLOB类型的处理
						try{
							if(this.type.equalsIgnoreCase("oracle")&&(uuid==null)){//由于oracle无法通过jdbc获取表名，因此只能利用uuid创建唯一的文件夹，但会出现新的问题是：当重复对同一表进行download方法，会出现多个存储相同文件的文件夹，而旧文件夹及其内容无法删除
								uuid=UUID.randomUUID().toString();
								table[i]=uuid;
							}
							if(uuid!=null){
								table[i]=uuid;
							}
							Blob blob = (Blob)rs.getBlob(colName[i]);
							InputStream input=blob.getBinaryStream();
							byte[] data = new byte[(int)blob.length()];//这里不能用inStream。avalable（）；
							input.read(data);
							InputStream input2=blob.getBinaryStream();
							if((type=FileType.getFileType(input2))==null){
								filePath="./cache/"+table[i]+"/"+count+"#"+i;
								file=new File(filePath);
								if(!file.getParentFile().exists()){
								file.getParentFile().mkdirs();
								}
								
								jsonStr+="\"./cache/"+table[i]+"/"+count+"#"+i+"\",";
								
							}else{
								filePath="./cache/"+table[i]+"/"+count+"#"+i+"."+type;
								file=new File(filePath);
								if(!file.getParentFile().exists()){
								file.getParentFile().mkdirs();
								}
								jsonStr+="\"./cache/"+table[i]+"/"+count+"#"+i+"."+type+"\",";
							}
							
							FileOutputStream fs=new FileOutputStream(file);
							fs.write(data);
	
					}catch(Exception e){
						jsonStr+="\"\",";}
					}
					else if(colType[i].indexOf("CLOB")!=-1){//对BLOB类型的处理
						try{
							if(this.type.equalsIgnoreCase("oracle")&&(uuid==null)){
								uuid=UUID.randomUUID().toString();
								table[i]=uuid;
							}
							if(uuid!=null){
								table[i]=uuid;
							}
							oracle.sql.CLOB clob = (oracle.sql.CLOB)rs.getClob(colName[i]);
							Reader reader = clob.getCharacterStream();
							char[] data = new char[(int)clob.length()];//这里不能用inStream。avalable（）；
							BufferedReader br=new BufferedReader(reader);
							br.read(data);
							String a=new String(data);
							System.out.println(a);
						    filePath="./cache/"+table[i]+"/"+"CLOB#"+count+"#"+i+".txt";
							file=new File(filePath);
							if(!file.getParentFile().exists()){
							file.getParentFile().mkdirs();
							}
							jsonStr+="\"./cache/"+table[i]+"/"+"CLOB#"+count+"#"+i+".txt"+"\",";							
							FileWriter fw=new FileWriter(filePath);
							fw.write(data);
							fw.close();//必须要手动close，或者flush才能将流中数据冲进目标文件中
					}catch(Exception e){
						jsonStr+="\"\",";}
					}
					else if (rs.getString(i + 1) == null)
						jsonStr += "\"\",";
					else if(rs.getString(i+1).indexOf("\"")!=-1){//如果存在""引号
						temp=rs.getString(i+1).replace("\"","\\\"");
						jsonStr+="\"" + temp + "\",";
					}else if(rs.getString(i+1).indexOf("\\")!=-1) {
						temp=rs.getString(i+1).replace("\\","\\\\");
						jsonStr+="\""+temp+"\",";
					}
						else if(colType[i].equalsIgnoreCase("YEAR")){
						jsonStr+="\""+rs.getString(i+1).substring(0,4)+"\",";
						
					}
					else{
						jsonStr += "\"" + rs.getString(i + 1) + "\",";
					}
				}
				if (jsonStr.length() > 1) {
					jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
				}
				jsonStr += "}";
				
				buffer += jsonStr + ",";
				// 把新数据放入到处理管道里
			//	if (Constant.run_process && new ProcessDao().numProcessByParagraphId(paragraphID) > 0) {
			//		processPipe.offer(paragraphID, jsonStr);
				//}
				//if (hasBlock)
				//	content += jsonStr + ",";
				count++;
			}

			if (content.length() > 1) {
				content = content.substring(0, content.length() - 1);
			}
			content += "]";

			// new BlockService().syncBlock(paragraphID, content);
			// setRecordCount(paragraphID,count);

			// 生成数据摘要，用于日志显示
			if (!has_introduce) {
				if (buffer.length() > 100) {
					introduce = buffer.substring(0, 100) + "...";
				}
				introduce = buffer;
			}
			introduce = "[" + introduce + "]";

			if (!buffer.equals("")) {
				buffer = buffer.substring(0, buffer.length() - 1);
				bw.write(buffer);
			}
			bw.write("]}");

			// .info("数据库数据用json字符串表示为：" + introduce);
			bw.close();
			out.close();
			rs.close();
			conn.close();
			System.out.println("read database finish");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean upload(InputStream in) {// 从缓存写数据源,注意若不设置主键将无法上传数据
		if (!analyzeParameter())
			return false;
		try {
			Class.forName(driver);// 加载数据库驱动
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		try {
			Connection conn = DriverManager.getConnection(this.url, this.username, this.password);
			Statement stmt = conn.createStatement();
			String[] keys = null;

			JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(in)));
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {
				String name = jsonReader.nextName();
				if ((!name.equals("data")) && (!name.equals("keys"))) {
					jsonReader.skipValue();
				} else if (name.equals("keys")) {
					jsonReader.beginObject();
					jsonReader.nextName();
					keys = jsonReader.nextString().split("###");
					if (keys.length <= 0) {
						System.out.println("表不存在主键，无法修改");
					}
					jsonReader.endObject();
				} else {
					jsonReader.beginArray();
					while (jsonReader.hasNext()) {
						String id = " -1";
						int colNum = 0;

						String updateSql = "update " + this.tableName + " set ";
						String whereSql = " where ";
						jsonReader.beginObject();
						while (jsonReader.hasNext()) {
							String attr = jsonReader.nextName();
							String value = jsonReader.nextString();
							for (int i = 0; i < keys.length; i++) {
								if (attr.equalsIgnoreCase(keys[i])) {
									if (value.equals("")) {//如果value值为空值时，修改为null，且不带引号
										value = "null";

										if (i < keys.length - 2) {
											whereSql += attr + "=" + value + ",";
											continue;
										} else if (i < keys.length - 1) {
											whereSql += attr + "=" + value;
											continue;
										} else {
											if(keys.length>1){
											whereSql += " and " + attr + "=" + value;
											continue;}
											else{
												whereSql+= attr + "="+ value;
											}
										}
									} else {
										if (i < keys.length - 2) {
											whereSql += attr + "='" + value + "',";
											continue;
										}

										else if (i < keys.length - 1) {
											whereSql += attr + "='" + value + "'";
											continue;
										} else {
											if(keys.length>1){
											whereSql += " and " + attr + "='" + value + "'";
											continue;
											}
											else{
												whereSql+=attr + "='" + value + "'";
											}
										}
									}
								}
							}
							colNum++;// 这个column不计算主键所在的列
							if (value.equals("")) {
								value = "null";

								if (colNum == 1)

									updateSql += attr + "=" + value;
								else
									updateSql += ", " + attr + "=" + value;
							} else {

								if (colNum == 1)

									updateSql += attr + "='" + value + "'";
								else
									updateSql += ", " + attr + "='" + value + "'";
							}
						}
						jsonReader.endObject();
						updateSql += whereSql;
						System.out.println(updateSql);
						if (colNum == 0)
							continue;
						// LOGGER.info("执行数据库数据修改：" + updateSql);
						stmt.executeUpdate(updateSql);
					}
					jsonReader.endArray();
				}
			}
			jsonReader.endObject();
			jsonReader.close();
			in.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	// 此方法为获取数据库连接
	public List<String> getPrimaryKeys(String tableName) {
		this.tableName = tableName;
		Connection conn = null;
		try {

			Class.forName(driver); // 加载数据库驱动
			conn = DriverManager.getConnection(this.url, this.username, this.password);// url=jdbc:MySQL://222.201.145.241:3306/gridoc?useUnicode=true&characterEncoding=UTF-8
			// 所有的段信息均从这个连接中来
			List<String> keys = new ArrayList<String>();
			ResultSet rs = conn.getMetaData().getPrimaryKeys(null, null, this.tableName);
			while (rs.next()) {
				keys.add(rs.getString("COLUMN_NAME"));
			}
			return keys;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getKeysString(String tableName) {
		this.tableName = tableName;
		StringBuilder sb = new StringBuilder();
		if (getPrimaryKeys(tableName).size() <= 0) {
			return "\"\"";
		} else {
			sb.append("\"");
			for (int i = 0; i < getPrimaryKeys(tableName).size(); i++) {
				sb.append(getPrimaryKeys(tableName).get(i));
				if (i < getPrimaryKeys(tableName).size() - 1) {
					sb.append("###");
				}
			}
			sb.append("\"");
			return sb.toString();
		}
	}


	
	public static void setRecordCount(Integer paragraphId,Integer count){
		RedisContainerService redis = new RedisContainerService("coiz.dbCount");
		redis.hset("dbCount", Integer.toString(paragraphId), Integer.toString(count));
	} 
	
	public static int getRecordCount(Integer paragraphId){
		RedisContainerService redis = new RedisContainerService("coiz.dbCount");
		Integer count = Integer.parseInt(redis.hget("dbCount", Integer.toString(paragraphId)));
		return count;
	}
}
