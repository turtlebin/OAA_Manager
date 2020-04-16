package gri.engine.dest;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import java.sql.ResultSetMetaData;

//import com.csasc.materialization.Column;
import com.google.gson.stream.JsonReader;
import com.sun.rowset.internal.Row;

import gir.engine.monitor.RowGetter;
import gir.engine.monitor.SendHelper;
import gir.engine.monitor.TimerHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gri.engine.util.SQLParser;

public class DestDatabase {
	private Integer paragraphID;
	private String dataSourcePath;

	private String destPath;
	private String type; // { "mysql", "sql server", "oracle" ,"db2"}
	private String host;
	private String port;
	private String db_name;
	private String username;
	private String password;
	private String sql;

	private String driver;
	private String url; // 数据库连接字符串
	private String sourceDriver;
	private String sourceUrl;
	private String tableName;// 数据库表名

	private String sourcePath;
	private String sourceType;
	private String sourceHost;
	private String sourcePort;
	private String source_db_name;
	private String sourceUsername;
	private String sourcePassword;
	private String selectSql;
	
	private int process=0;//进度计数
	


	public Map<String, ArrayList<Column>> tables = new HashMap<String, ArrayList<Column>>();

	public DestDatabase(String sourcePath, String destPath) {
		this.sourcePath = sourcePath;
		this.destPath = destPath;
		analyzeSourceParameter();
		analyzeParameter();
	}

	public DestDatabase(String destPath) {
		this.destPath = destPath;
		analyzeSourceParameter();
		analyzeParameter();
	}

	public DestDatabase() {
		analyzeSourceParameter();
		analyzeParameter();
	}

	private boolean analyzeSourceParameter() {
		try {
			String strs[] = this.sourcePath.split("###");
			this.sourceType = strs[0];
			this.sourceHost = strs[1];
			this.sourcePort = strs[2];
			this.source_db_name = strs[3];
			this.sourceUsername = strs[4];
			this.sourcePassword = strs[5];
			this.selectSql = strs[6];
			if (this.sourceType.equalsIgnoreCase("MySQL")) {
				this.sourceDriver = "com.mysql.jdbc.Driver";
				this.sourceUrl = "jdbc:mysql://" + this.sourceHost + ":" + sourcePort + "/" + source_db_name+"?zeroDateTimeBehavior=convertToNull";// 生成url
				// this.url= "jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + ;//生成url
			} else if (this.sourceType.equalsIgnoreCase("SQL Server")) {
				this.sourceDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
				this.sourceUrl = "jdbc:sqlserver://" + this.sourceHost + ":" + this.sourcePort + ";DatabaseName="
						+ this.source_db_name;
			} else if (this.sourceType.equalsIgnoreCase("Oracle")) {
				this.sourceDriver = "oracle.jdbc.driver.OracleDriver";
				this.sourceUrl = "jdbc:oracle:thin:@" + this.sourceHost + ":" + this.sourcePort + ":"
						+ this.source_db_name;
			} else if (this.sourceType.equalsIgnoreCase("DB2")) {
				this.sourceDriver = "com.ibm.db2.jdbc.app.DB2.Driver";
				this.sourceUrl = "jdbc:db2://" + this.host + ":" + port + "/" + db_name;
			} else if (this.sourceType.equalsIgnoreCase("SYBASE")) {
				this.sourceDriver = "com.sybase.jdbc3.jdbc.SybDriver";
				this.sourceUrl="jdbc:sybase:Tds:"+this.sourceHost+":"+this.sourcePort+"/"+this.source_db_name+"?charset=eucgb";
			} else
				return false;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean analyzeParameter() {
		try {
			String strs[] = this.destPath.split("###");
			this.type = strs[0];
			this.db_name = strs[1];
			this.username = strs[2];
			this.password = strs[3];
			this.host = strs[4];// 由传入的数据源路径获取用户名和密码
			this.port = strs[5];

			if (type.equalsIgnoreCase("MySQL")) {
				this.driver = "com.mysql.jdbc.Driver";
				this.url = "jdbc:mysql://" + this.host + ":" + port + "/" + db_name
						+ "?useUnicode=true&characterEncoding=utf-8&useServerPrepStmts=false&rewriteBatchedStatements=true&zeroDateTimeBehavior=convertToNull";// 生成url
				// this.url= "jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + ;//生成url
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
			} else if (type.equalsIgnoreCase("SYBASE")) {
				this.driver = "com.sybase.jdbc3.jdbc.SybDriver";
				this.url = "jdbc:sybase:Tds:" + this.host + ":" + port + "/" + this.db_name;
			} else
				return false;
			// LOGGER.info("database url:" + this.url);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean createMysql(InputStream in, int columnCount) {

		// System.out.println(getColumnCount(in));
		// System.out.println(initColumn(in).get(1).getColumnName());
		// System.out.println(initColumn(in).get(1).isSigned());
		// System.out.println(initColumn(in).get(1).getTableName());

		List<Column> list = initColumn(in, columnCount);
		System.out.println(list.get(1).getColumnName());
		System.out.println(list.get(1).getTableName());
		return true;
	}

	public boolean insert(InputStream in, String tableName) {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> blobMap = new HashMap<String, String>();
		Map<String, String> clobMap = new HashMap<String, String>();
		String colName;
		String type;

		boolean hasBlobOrClob = false;
		this.tableName = tableName;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		try {
			Connection conn = DriverManager.getConnection(this.url, this.username, this.password);
			Statement stmt = conn.createStatement();
			String temp = "";
			JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(in)));
			jsonReader.setLenient(true);
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {

				String name = jsonReader.nextName();
				if (!(name.equals("data") || name.equals("attr"))) {
					jsonReader.skipValue();
				} else if (name.equals("attr")) {
					jsonReader.beginObject();
					while (jsonReader.hasNext()) {
						colName = jsonReader.nextName();
						type = jsonReader.nextString();
						if (type.indexOf("BLOB") != -1 || type.indexOf("CLOB") != -1) {
							hasBlobOrClob = true;
						}
						map.put(colName, type);
					}
					jsonReader.endObject();
				} else if (name.equals("data")) {
					jsonReader.beginArray();
					while (jsonReader.hasNext()) {
						String whereSql = "";
						String insertSql = "insert into " + this.tableName + " values(";
						jsonReader.beginObject();
						while (jsonReader.hasNext()) {

							String col = jsonReader.nextName();
							String value = jsonReader.nextString();
							if (hasBlobOrClob) {
								if (map.get(col).indexOf("BLOB") == -1 && map.get(col).indexOf("CLOB") == -1) {

									if (value.equals("")) {
										whereSql += col + " is null" + " and ";
									} else if (map.get(col).indexOf("DATE") != -1
											|| map.get(col).indexOf("TIMESTAMP") != -1) {
										whereSql += col + "= ";
										if (value.length() <= 11) {
											whereSql += "TO_DATE('" + value.split("\\.")[0] + "', 'YYYY-MM-DD') and ";
										} else {
											whereSql += "TO_DATE('" + value.split("\\.")[0]
													+ "', 'YYYY-MM-DD HH24:MI:SS') and ";
										}
									} else {
										whereSql += col + "= ";
										if (value.indexOf("\'") != -1) {
											temp = value.replace("\'", "\\\'");
											whereSql += "'" + temp + "'" + " and ";
										} else {
											whereSql += "'" + value + "'" + " and ";
										}
									}
								}

							}
							if (map.get(col).indexOf("BLOB") != -1) {
								blobMap.put(col, value);
								insertSql += "empty_blob(),";
							} else if (map.get(col).indexOf("CLOB") != -1) {
								clobMap.put(col, value);
								insertSql += "empty_clob(),";
							} else {

								if (value.indexOf("\'") != -1) {
									temp = value.replace("\'", "\\\'");
									insertSql += "'" + temp + "',";
								} else if (value.equals("")) {
									insertSql += "null,";
								} else if (map.get(col).indexOf("DATE") != -1) {
									if (value.length() <= 11) {
										insertSql += "TO_DATE('" + value.split("\\.")[0] + "', 'YYYY-MM-DD'),";
									} else {
										insertSql += "TO_DATE('" + value.split("\\.")[0]
												+ "', 'YYYY-MM-DD HH24:MI:SS'),";
									}
								} else if (map.get(col).equalsIgnoreCase("Timestamp")) {
									insertSql += "TO_TIMESTAMP('" + value.split("\\.")[0]
											+ "', 'YYYY-MM-DD HH24:MI:SS'),";
								} else {
									insertSql += "'" + value + "',";
								}
							}
						}
						String sql = insertSql.substring(0, insertSql.length() - 1);
						sql += ")";

						System.out.println(sql);
						jsonReader.endObject();
						stmt.executeUpdate(sql);

						if (whereSql.length() > 0) {
							whereSql = " where " + whereSql.substring(0, whereSql.length() - 5);
							if (blobMap.size() > 0) {

								conn.setAutoCommit(false);
								String sql2 = "select * from " + this.tableName + whereSql + " for update";
								System.out.println(sql2);
								ResultSet rs = stmt.executeQuery(sql2);
								while (rs.next()) {
									for (String key : blobMap.keySet()) {
										try {
											Blob blob = rs.getBlob(key);
											String path = blobMap.get(key);// 由于是读取一行数据就put一次map，所以get(key)方法往往能获取最新的value值

											if (path.equals("")) {
												break;
											}
											InputStream inStream = new FileInputStream(path);
											OutputStream out = blob.setBinaryStream(0);
											BufferedOutputStream bf = new BufferedOutputStream(out);
											int i = 0;
											while ((i = inStream.read()) != -1) {
												bf.write(i); // Blob的输入流，相当于输入到数据库中
											}
											out.flush();
											out.close();
											inStream.close();

										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
								conn.commit();
								conn.setAutoCommit(true);
							}
							if (clobMap.size() > 0) {
								conn.setAutoCommit(false);
								String sql3 = "select * from " + this.tableName + whereSql + " for update";
								ResultSet rs = stmt.executeQuery(sql3);
								while (rs.next()) {
									for (String key : clobMap.keySet()) {
										try {
											oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(key);
											String path = clobMap.get(key);
											if (path.equals("")) {
												break;
											}
											FileReader fr = new FileReader(path);
											BufferedReader br = new BufferedReader(fr);
											Writer out = clob.setCharacterStream(0);

											BufferedWriter bw = new BufferedWriter(out);
											int i;
											while ((i = br.read()) != -1) {
												bw.write((char) i); // Clob的输入流，相当于输入到数据库中
											}
											bw.close();

										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
								conn.commit();
							}
							conn.setAutoCommit(true);
						}
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

	public boolean insertOracle(InputStream in, String tableName) {
		Map<Integer, String> colMap = new HashMap<Integer, String>();
		Map<Integer, Integer> blobMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> clobMap = new HashMap<Integer, Integer>();
		String type;
		Statement state = null;
		Statement state2 = null;
		Statement state3 = null;
		ResultSet rs4 = null;
		int iterator = 0;
		int count = 0;
		boolean hasBloborClob = false;
		int blobCount = 0;
		int clobCount = 0;
		this.tableName = tableName;
		try {
			Class.forName(this.sourceDriver);
			Class.forName(this.driver);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		try {
			Connection conn = DriverManager.getConnection(this.url, this.username, this.password);
			Connection sourceConn = DriverManager.getConnection(this.sourceUrl, this.sourceUsername,
					this.sourcePassword);
			JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(in)));
			jsonReader.setLenient(true);
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {
				String name = jsonReader.nextName();
				if (!name.equals("attr")) {
					jsonReader.skipValue();
				} else if (name.equals("attr")) {
					jsonReader.beginObject();
					int colIndex = 1;
					while (jsonReader.hasNext()) {
						jsonReader.nextName();
						type = jsonReader.nextString();
						colMap.put(colIndex, type);
						if (type.indexOf("BLOB") != -1) {
							blobCount++;
							blobMap.put(colIndex, colIndex);
							hasBloborClob = true;
						} else if (type.indexOf("CLOB") != -1) {
							clobCount++;
							clobMap.put(colIndex, colIndex);
							hasBloborClob = true;
						}
						colIndex++;
					}
					jsonReader.endObject();
				}
			}
			jsonReader.endObject();
			jsonReader.close();
			in.close();
			state = sourceConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			state2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = state.executeQuery(selectSql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			int rowCount = getRowCount(rs);

			StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(this.tableName).append(" VALUES(");
			for (int i = 0; i < columnCount; i++) {
				insertSql.append((i == columnCount - 1) ? "?" : "?,");
			}
			insertSql.append(")");
			String sql = insertSql.toString();
			int index;
			if (!hasBloborClob) {
				state3 = sourceConn.createStatement();
				rs4 = state3.executeQuery(selectSql);
				rsmd = rs4.getMetaData();// 不存在BLOB或CLOB时采取批量插入处理
				while (iterator * 1000000 < rowCount) {
					PreparedStatement ps = conn.prepareStatement(sql);
					conn.setAutoCommit(false);
					while (count < 1000000 && rs4.next()) {
						for (index = 1; index <= columnCount; index++) {
							if (colMap.get(index).indexOf("TIME") == -1
									&& colMap.get(index).indexOf("DATETIME") == -1) {
								ps.setObject(index, rs4.getObject(index));
							} else {// 对时间类型的特殊处理
								String time = rs4.getString(index);
								if (time != null) {
									// time = time.split("\\.")[0];
									ps.setObject(index, rs4.getObject(index));
								} else {
									ps.setObject(index, null);
								}
							}
						}
						if (index > columnCount) {
							count++;
							ps.addBatch();
							System.out.println(count);
							continue;
						}
					}
					long start = System.currentTimeMillis();
					ps.executeBatch();
					ps.clearBatch();
					conn.commit();
					long end = System.currentTimeMillis();
					System.out.println("运行时间：" + (end - start));
					ps.close();
					iterator++;
					count = 0;

				}
				rs4.close();
			} else {// 存在BLOB或CLOB数据类型时需要特殊处理,先插入emptyBlob和emptyClob，再查询

				PreparedStatement ps = conn.prepareStatement(sql);
				conn.setAutoCommit(false);
				while (count < 1000000 && rs.next()) {
					for (index = 1; index <= columnCount; index++) {
						String colType = colMap.get(index);
						if (colType.indexOf("TIME") == -1 && colType.indexOf("DATETIME") == -1
								&& colType.indexOf("BLOB") == -1 && colType.indexOf("CLOB") == -1) {
							ps.setObject(index, rs.getObject(index));
						} else if (colType.indexOf("BLOB") != -1) {
							ps.setObject(index, oracle.sql.BLOB.getEmptyBLOB());
						} else if (colType.indexOf("CLOB") != -1) {
							ps.setObject(index, oracle.sql.CLOB.getEmptyCLOB());
						} else {// 对时间类型的特殊处理
							String time = rs.getString(index);
							if (time != null) {
								// time = time.split("\\.")[0];
								ps.setObject(index, time);
							} else {
								ps.setObject(index, null);
							}
						}
					}
					if (index > columnCount) {
						count++;
						ps.addBatch();
						System.out.println(count);
						continue;
					}
				}
				long start = System.currentTimeMillis();
				ps.executeBatch();
				ps.clearBatch();
				conn.commit();
				conn.setAutoCommit(true);
				long end = System.currentTimeMillis();
				System.out.println("运行时间：" + (end - start));
				iterator++;
				count = 0;
				ps.close();
				rs.close();

				// rs.beforeFirst();
				// rsmd = rs.getMetaData();
				state3 = sourceConn.createStatement();
				rs4 = state3.executeQuery(selectSql);
				rsmd = rs4.getMetaData();
				while (rs4.next()) {
					String whereSql = "";
					String temp = "";
					String sql2 = "";
					for (int i = 1; i <= columnCount; i++) {
						if (colMap.get(i).indexOf("BLOB") == -1 && colMap.get(i).indexOf("CLOB") == -1) {
							String value = rs4.getString(i);
							String col = rsmd.getColumnName(i);
							if (value.equals("")) {
								whereSql += col + " is null" + " and ";
							} else if (colMap.get(i).indexOf("DATE") != -1
									|| colMap.get(i).indexOf("TIMESTAMP") != -1) {
								whereSql += col + "= ";
								if (value.length() <= 11) {// 由于存在精度问题，所以可能出现whereSql匹配不到值的问题，所以应该用主键值构建whereSql，且主键值不应存在精度问题
									whereSql += "TO_DATE('" + value.split("\\.")[0] + "', 'YYYY-MM-DD') and ";
								} else {
									whereSql += "TO_DATE('" + value.split("\\.")[0]
											+ "', 'YYYY-MM-DD HH24:MI:SS') and ";
								}
							} else {
								whereSql += col + "= ";
								if (value.indexOf("\'") != -1) {
									temp = value.replace("\'", "\\\'");
									whereSql += "'" + temp + "'" + " and ";
								} else {
									whereSql += "'" + value + "'" + " and ";
								}
							}
						}
					}
					if (whereSql.length() > 0) {
						whereSql = " where " + whereSql.substring(0, whereSql.length() - 5);
						if (blobMap.size() > 0) {
							conn.setAutoCommit(false);
							sql2 = "select * from " + this.tableName + whereSql + " for update ";
							ResultSet rs2 = state2.executeQuery(sql2);
							while (rs2.next()) {
								for (Integer key : blobMap.keySet()) {
									try {
										Blob blob = rs4.getBlob(key);
										if (blob == null) {
											continue;
										}
										Blob blob2 = rs2.getBlob(key);
										InputStream input = blob.getBinaryStream();
										// byte[] data=new byte[(int)blob.length()];
										// input.read(data);
										OutputStream out = blob2.setBinaryStream(0);
										BufferedOutputStream bf = new BufferedOutputStream(out);
										int i;
										while ((i = input.read()) != -1) {
											bf.write(i);
										}
										bf.flush();
										out.flush();
										out.close();
										input.close();
										/*
										 * 
										 * Blob blob = rs.getBlob(key); Blob blob2 = rs2.getBlob(key); InputStream input
										 * = blob.getBinaryStream(); byte[] data=new byte[(int)blob.length()];
										 * input.read(data); OutputStream out = blob2.setBinaryStream(0);
										 * BufferedOutputStream bf = new BufferedOutputStream(out); bf.write(data);
										 * out.flush(); out.close(); input.close();
										 */
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							rs2.close();
							conn.commit();
							conn.setAutoCommit(true);
						}
						if (clobMap.size() > 0) {
							conn.setAutoCommit(false);
							String sql3 = "select * from " + this.tableName + whereSql + " for update";
							ResultSet rs3 = state.executeQuery(sql3);
							while (rs3.next()) {
								for (Integer key : clobMap.keySet()) {
									try {
										oracle.sql.CLOB clob = (oracle.sql.CLOB) rs4.getClob(key);
										if (clob == null) {
											continue;
										}
										oracle.sql.CLOB clob2 = (oracle.sql.CLOB) rs3.getClob(key);
										Reader reader = clob.getCharacterStream();
										BufferedReader br = new BufferedReader(reader);
										Writer out = clob2.setCharacterStream(0);
										BufferedWriter bw = new BufferedWriter(out);
										int i;
										while ((i = br.read()) != -1) {
											bw.write((char) i);
										}
										bw.flush();
										out.flush();
										out.close();
										br.close();
										reader.close();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							rs3.close();
							conn.commit();
							conn.setAutoCommit(true);
						}
					}
				}
			}
			sourceConn.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean insertMySql(InputStream in, String tableName) {

		Map<String, String> map = new HashMap<String, String>();
		Map<Integer, String> blobMap = new HashMap<Integer, String>();
		String colName;
		String type;

		boolean hasBlob = false;
		this.tableName = tableName;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		try {
			Connection conn = DriverManager.getConnection(this.url, this.username, this.password);
			// Statement stmt=conn.createStatement();
			String temp = "";
			JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(in)));
			jsonReader.setLenient(true);
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {
				String name = jsonReader.nextName();
				if (!(name.equals("data") || name.equals("attr"))) {
					jsonReader.skipValue();
				} else if (name.equals("attr")) {
					jsonReader.beginObject();
					while (jsonReader.hasNext()) {
						colName = jsonReader.nextName();
						type = jsonReader.nextString();
						if (type.indexOf("BLOB") != -1) {
							hasBlob = true;
						}
						map.put(colName, type);
					}
					jsonReader.endObject();
				} else if (name.equals("data")) {
					jsonReader.beginArray();
					while (jsonReader.hasNext()) {
						int countBlob = 0;
						String whereSql = "";
						String insertSql = "insert into " + this.tableName + " values(";
						jsonReader.beginObject();
						while (jsonReader.hasNext()) {

							String col = jsonReader.nextName();
							String value = jsonReader.nextString();
							/*
							 * if(hasBlob){ if(map.get(col).indexOf("BLOB")==-1){ whereSql+=col+"= ";
							 * if(value.equals("")){ whereSql+="null"+" and "; } else
							 * if(value.indexOf("\'")!=-1){ temp=value.replace("\'", "\\\'");
							 * whereSql+="'"+temp+"'"+" and "; }else{ whereSql+="'"+value+"'"+" and "; } }
							 * 
							 * }
							 */
							if (map.get(col).indexOf("BLOB") != -1) {

								insertSql += "?,";
								countBlob++;
								blobMap.put(countBlob, value);

							} else if (map.get(col).indexOf("CLOB") != -1) {
								String s;
								StringBuilder s1 = new StringBuilder();
								if (value.equals("")) {
									insertSql += "null,";
								} else {
									try {// oracle→mysql对CLOB数据类型的处理
										FileReader fr = new FileReader(value);
										BufferedReader br = new BufferedReader(fr);
										while ((s = br.readLine()) != null) {
											s1.append(s);
										}
										br.close();
										fr.close();
									} catch (Exception e) {
										e.printStackTrace();
									}
									value = s1.toString();
									if (value.indexOf("\'") != -1) {
										temp = value.replace("\'", "\\\'");
										insertSql += "'" + temp + "',";
									} else if (value.equals("")) {
										insertSql += "null,";
									} else {
										insertSql += "'" + value + "',";
									}
								}
							} else if (map.get(col).indexOf("BIT") != -1) {
								if (value.equals("")) {
									insertSql += "null,";
								} else
									insertSql += value + ",";
							} else if (map.get(col).indexOf("DATE") != -1
									|| map.get(col).equalsIgnoreCase("Timestamp")) {
								if (value.equals("")) {
									insertSql += "null,";
								} else {
									insertSql += "'" + value.split("\\.")[0] + "',";
								}

							} else {
								if (value.indexOf("\'") != -1) {
									temp = value.replace("\'", "\\\'");
									insertSql += "'" + temp + "',";
								} else if (value.equals("")) {
									insertSql += "null,";
								} else {
									insertSql += "'" + value + "',";
								}
							}
						}
						String sql = insertSql.substring(0, insertSql.length() - 1);
						sql += ")";
						PreparedStatement ps = conn.prepareStatement(sql);

						// System.out.println(sql);
						jsonReader.endObject();
						if (countBlob == 0) {
							ps.executeUpdate(sql);
						} else {

							for (int i = 1; i <= countBlob; i++) {
								String filePath = blobMap.get(i);
								if (filePath.equals("")) {
									ps.setObject(i, "");
									continue;
								}
								System.out.println(filePath);
								InputStream input = new FileInputStream(filePath);
								ps.setBlob(i, input);

							}
							ps.executeUpdate();
						}
						ps.close();
						/*
						 * if(whereSql.length()>0){ whereSql =" where "+whereSql.substring(0,
						 * whereSql.length() - 5); String
						 * sql2="select * from "+"blob_test2"+whereSql+" for update"; ResultSet
						 * rs=stmt.executeQuery(sql2); while(rs.next()){ for(String
						 * key:blobMap.keySet()){ try{ conn.setAutoCommit(false); Blob
						 * blob=rs.getBlob(key); String path=blobMap.get(key); if(path.equals("")){
						 * break; } InputStream inStream = new FileInputStream(path); OutputStream out
						 * =blob.setBinaryStream(1); BufferedOutputStream bf=new
						 * BufferedOutputStream(out); int i = 0; while ((i = inStream.read()) != -1) {
						 * bf.write(i); //Blob的输入流，相当于输入到数据库中 } out.flush(); out.close(); conn.commit();
						 * conn.setAutoCommit(true); /*InputStream inStream = new FileInputStream(path);
						 * OutputStream out=blob.setBinaryStream(0); int count = inStream.available();
						 * byte[] b = new byte[count]; inStream.read(b); out.write(b); // out.flush();
						 * out.close(); inStream.close();
						 */

						/*
						 * }catch(Exception e){ e.printStackTrace(); }
						 * 
						 * } } }
						 */

					}

					conn.close();
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

	public boolean insertMySql2(InputStream in, String tableName, int columnCount) {

		Map<String, String> map = new HashMap<String, String>();
		Map<Integer, String> blobMap = new HashMap<Integer, String>();
		String colName;
		String type;

		boolean hasBlob = false;
		this.tableName = tableName;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		    Connection conn=null;
		try {
			conn = DriverManager.getConnection(this.url, this.username, this.password);

			// Statement stmt=conn.createStatement();
			String temp = "";
			JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(in)));
			jsonReader.setLenient(true);
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {

				String name = jsonReader.nextName();
				if (!(name.equals("data") || name.equals("attr"))) {
					jsonReader.skipValue();
				} else if (name.equals("attr")) {
					jsonReader.beginObject();
					while (jsonReader.hasNext()) {
						colName = jsonReader.nextName();
						type = jsonReader.nextString();
						if (type.indexOf("BLOB") != -1) {
							hasBlob = true;
						}
						map.put(colName, type);
					}
					jsonReader.endObject();
				} else if (name.equals("data")) {
					jsonReader.beginArray();
					PreparedStatement ps = null;
					StringBuilder insertSql = new StringBuilder("insert into ").append(this.tableName)
							.append(" values(");
					for (int i = 0; i < columnCount; i++) {
						insertSql.append((i == columnCount - 1) ? "?" : "?,");
					}
					insertSql.append(")");
					ps = conn.prepareStatement(insertSql.toString());
					conn.setAutoCommit(false);
					int index = 1;
					while (jsonReader.hasNext()) {
						int countBlob = 0;

						jsonReader.beginObject();
						while (jsonReader.hasNext()) {
							String col = jsonReader.nextName();
							String value = jsonReader.nextString();
							/*
							 * if(hasBlob){ if(map.get(col).indexOf("BLOB")==-1){ whereSql+=col+"= ";
							 * if(value.equals("")){ whereSql+="null"+" and "; } else
							 * if(value.indexOf("\'")!=-1){ temp=value.replace("\'", "\\\'");
							 * whereSql+="'"+temp+"'"+" and "; }else{ whereSql+="'"+value+"'"+" and "; } }
							 * 
							 * }
							 */

							if (map.get(col).indexOf("BLOB") != -1) {
								countBlob++;
								blobMap.put(countBlob, value);

							} else if (map.get(col).indexOf("CLOB") != -1) {
								String s;
								StringBuilder s1 = new StringBuilder();
								if (value.equals("")) {
									ps.setObject(index, null);
								} else {
									try {// oracle→mysql对CLOB数据类型的处理
										FileReader fr = new FileReader(value);
										BufferedReader br = new BufferedReader(fr);
										while ((s = br.readLine()) != null) {
											s1.append(s);
										}
										br.close();
										fr.close();
									} catch (Exception e) {
										e.printStackTrace();
									}
									value = s1.toString();
									if (value.indexOf("\"") != -1) {
										temp = value.replace("\"", "\\\"");
										ps.setObject(index, temp);
									}

									else if (value.equals("")) {
										ps.setObject(index, null);
									} else {
										ps.setObject(index, value);
									}
								}
							} else if (map.get(col).indexOf("BIT") != -1) {
								if (value.equals("")) {
									ps.setObject(index, null);
								} else
									ps.setObject(index, value);
							} else if (map.get(col).indexOf("DATE") != -1
									|| map.get(col).equalsIgnoreCase("Timestamp")) {
								if (value.equals("")) {
									ps.setObject(index, null);
								} else {
									value = value.split("\\.")[0];
									ps.setObject(index, value);
								}

							} else {
								if (value.indexOf("\"") != -1) {
									temp = value.replace("\"", "\\\"");
									ps.setObject(index, temp);
								} else if (value.equals("")) {
									ps.setObject(index, null);
								} else {
									ps.setObject(index, value);
								}
							}
							index++;
							if (index > columnCount) {
								ps.addBatch();
								index = 1;
							}
						}
						// System.out.println(sql);
						jsonReader.endObject();
						if (countBlob == 0) {
							// ps.addBatch(sql);
						} else {

							for (int i = 1; i <= countBlob; i++) {
								String filePath = blobMap.get(i);
								if (filePath.equals("")) {
									ps.setObject(index, "");
									continue;
								}
								System.out.println(filePath);
								InputStream input = new FileInputStream(filePath);
								ps.setBlob(index, input);

							}
							ps.addBatch();
						}

						/*
						 * if(whereSql.length()>0){ whereSql =" where "+whereSql.substring(0,
						 * whereSql.length() - 5); String
						 * sql2="select * from "+"blob_test2"+whereSql+" for update"; ResultSet
						 * rs=stmt.executeQuery(sql2); while(rs.next()){ for(String
						 * key:blobMap.keySet()){ try{ conn.setAutoCommit(false); Blob
						 * blob=rs.getBlob(key); String path=blobMap.get(key); if(path.equals("")){
						 * break; } InputStream inStream = new FileInputStream(path); OutputStream out
						 * =blob.setBinaryStream(1); BufferedOutputStream bf=new
						 * BufferedOutputStream(out); int i = 0; while ((i = inStream.read()) != -1) {
						 * bf.write(i); //Blob的输入流，相当于输入到数据库中 } out.flush(); out.close(); conn.commit();
						 * conn.setAutoCommit(true); /*InputStream inStream = new FileInputStream(path);
						 * OutputStream out=blob.setBinaryStream(0); int count = inStream.available();
						 * byte[] b = new byte[count]; inStream.read(b); out.write(b); // out.flush();
						 * out.close(); inStream.close();
						 */

						/*
						 * }catch(Exception e){ e.printStackTrace(); }
						 * 
						 * } } }
						 */

					}
					ps.executeBatch();
					conn.commit();
					conn.setAutoCommit(true);
					ps.close();
					conn.close();
					jsonReader.endArray();
				}
			}

			jsonReader.endObject();
			jsonReader.close();
			in.close();
			conn.close();
			return true;

		} catch (SQLException e) {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean insertMySql3(InputStream in, String tableName) {

		Map<String, String> map = new HashMap<String, String>();
		// Map<Integer, String> blobMap = new HashMap<Integer, String>();
		Map<Integer, String> colMap = new HashMap<Integer, String>();
		String colName;
		String type;
		Statement state = null;
		int iterator = 0;// 迭代数
		int count = 0;// 行计数
		this.tableName = tableName;
		try {
			Class.forName(sourceDriver);
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		try {
			Logger logger = LoggerFactory.getLogger(DestDatabase.class);
			logger.info("test start");
			Connection conn = DriverManager.getConnection(this.url, this.username, this.password);
			Connection sourceConn = DriverManager.getConnection(this.sourceUrl, this.sourceUsername,
					this.sourcePassword);
			JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(in)));
			jsonReader.setLenient(true);
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {

				String name = jsonReader.nextName();
				if (!name.equals("attr")) {// 初始化列序号与列关系类型的映射
					jsonReader.skipValue();
				} else {
					jsonReader.beginObject();
					int index = 1;
					while (jsonReader.hasNext()) {
						colName = jsonReader.nextName();
						type = jsonReader.nextString();
						/*
						 * if (type.indexOf("BLOB") != -1) { hasBlob = true; countBlob++; }
						 */
						colMap.put(index, type);
						index++;
						// map.put(colName, type);
					}
					jsonReader.endObject();
				}
			}
			jsonReader.endObject();
			jsonReader.close();
			in.close();
			logger.info("test middle");

			// ps = null;
			// state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
			// ResultSet.CONCUR_READ_ONLY);
			state = sourceConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// state.setFetchSize(2000000);
			logger.info("before select");
			long before = System.currentTimeMillis();
			ResultSet rs = state.executeQuery(this.selectSql);// 直接查询全表的话很容易出现内存不足的问题，这种情况下只能改用limit关键词（oracle需要改写select语句）分多次进行查询，并且每次查询结束后清理内存，这样的话可能能够解决内存不足的问题。
			long after = System.currentTimeMillis();
			logger.info("花了这么长的时间：?", (after - before));
			logger.info("after select");
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			int rowCount = getRowCount(rs);
			StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(this.tableName).append(" VALUES(");
			for (int i = 0; i < columnCount; i++) {
				insertSql.append((i == columnCount - 1) ? "?" : "?,");
			}
			insertSql.append(")");
			String sql = insertSql.toString();
			int index;
			logger.info("before insert");
			// PreparedStatement ps=null;
			while (iterator * 1000000 < rowCount) {
				PreparedStatement ps = conn.prepareStatement(sql);
				conn.setAutoCommit(false);
				while (count < 1000000 && rs.next()) {
					for (index = 1; index <= columnCount; index++) {
						if (colMap.get(index).indexOf("TIME") == -1 && colMap.get(index).indexOf("DATETIME") == -1) {
							ps.setObject(index, rs.getObject(index));
						} else {// 对时间类型的特殊处理
							String time = rs.getString(index);
							if (time != null) {
								// time = time.split("\\.")[0];
								ps.setObject(index, time);
							} else {
								ps.setObject(index, null);
							}
						}
					}
					if (index > columnCount) {
						count++;
						ps.addBatch();
						System.out.println(count);
						continue;
					}
				}
				long start = System.currentTimeMillis();
				ps.executeBatch();
				ps.clearBatch();
				conn.commit();
				long end = System.currentTimeMillis();
				System.out.println("运行时间=" + (end - start));
				ps.close();
				iterator++;
				count = 0;
			}
			logger.info("after insert");
			rs.close();
			sourceConn.close();
			conn.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean insertMySql4(InputStream in, String tableName) {
		Map<String, String> map = new HashMap<String, String>();
		// Map<Integer, String> blobMap = new HashMap<Integer, String>();
		Map<Integer, String> colMap = new HashMap<Integer, String>();
		String colName;
		String type;
		Statement state = null;
		int iterator = 0;// 迭代数
		int count = 0;// 行计数
		this.tableName = tableName;
		try {
			Class.forName(sourceDriver);
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		Connection conn=null;
		Connection sourceConn=null;
		try {
			Logger logger = LoggerFactory.getLogger(DestDatabase.class);
			logger.info("test start");
			conn = DriverManager.getConnection(this.url, this.username, this.password);
			sourceConn = DriverManager.getConnection(this.sourceUrl, this.sourceUsername,
					this.sourcePassword);
			JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(in)));
			jsonReader.setLenient(true);
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {

				String name = jsonReader.nextName();
				if (!name.equals("attr")) {// 初始化列序号与列关系类型的映射
					jsonReader.skipValue();
				} else {
					jsonReader.beginObject();
					int index = 1;
					while (jsonReader.hasNext()) {
						colName = jsonReader.nextName();
						type = jsonReader.nextString();
						/*
						 * if (type.indexOf("BLOB") != -1) { hasBlob = true; countBlob++; }
						 */
						colMap.put(index, type);
						index++;
						// map.put(colName, type);
					}
					jsonReader.endObject();
				}
			}
			jsonReader.endObject();
			jsonReader.close();
			in.close();
			logger.info("test middle");

			// ps = null;
			// state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
			// ResultSet.CONCUR_READ_ONLY);
			// state = sourceConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
			// ResultSet.CONCUR_READ_ONLY);
			// state.setFetchSize(2000000);
			ResultSet rs=null;
			if (this.sourceType.equalsIgnoreCase("MYSQL")) {
				com.mysql.jdbc.Statement state2 = (com.mysql.jdbc.Statement) sourceConn
						.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				state2.setFetchSize(1000);
				state2.enableStreamingResults();// 对于数据源为mysql数据库，其不支持setFetchSize方法，必须显式调用mysql.jdbc.Statement类，其中该语句非常关键。
				logger.info("before select");
				long before = System.currentTimeMillis();
				rs=state2.executeQuery(this.selectSql);
				long after = System.currentTimeMillis();
				logger.info("花了这么长的时间：?", (after - before));
				logger.info("after select");
			} else {
				Statement state2 = sourceConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				state2.setFetchSize(1000);
				//rs=state2.executeQuery(this.selectSql);
				logger.info("before select");
				long before = System.currentTimeMillis();
				rs=state2.executeQuery(this.selectSql);
				long after = System.currentTimeMillis();
				logger.info("花了这么长的时间：?", (after - before));
				logger.info("after select");
			}
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			// int rowCount = getRowCount(rs);
			int rowCount=Integer.MAX_VALUE;//设置为一个很大的数，Integer的最大值
			StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(this.tableName).append(" VALUES(");
			for (int i = 0; i < columnCount; i++) {
				insertSql.append((i == columnCount - 1) ? "?" : "?,");
			}
			insertSql.append(")");
			String sql = insertSql.toString();
			int index;
			logger.info("before insert");
			// PreparedStatement ps=null;
			while (iterator * 1000000 < rowCount) {
				PreparedStatement ps = conn.prepareStatement(sql);
//				conn.setAutoCommit(false);
				while (count < 1000000 && rs.next()) {
					for (index = 1; index <= columnCount; index++) {
						if (colMap.get(index).indexOf("TIME") == -1 && colMap.get(index).indexOf("DATETIME") == -1) {
							ps.setObject(index, rs.getObject(index));
						} else {// 对时间类型的特殊处理
							String time = rs.getString(index);
							if (time != null) {
								// time = time.split("\\.")[0];
								ps.setObject(index, time);
							} else {
								ps.setObject(index, null);
							}
						}
					}
					if (index > columnCount) {
						ps.executeUpdate();
//						process++;
//						count++;
//						ps.addBatch();
						//System.out.println(count);
						continue;
					}
				}
				if(count==0) {
					break;//通过对rowCount设置为一个很大的数，利用count==0特性，说明内循环没有执行，可知rs.next()==false，从而可以退出外循环，而不需要获得rowCount属性，否则获取rowCount会耗费大量时间。
				}
				long start = System.currentTimeMillis();
				ps.executeBatch();
				ps.clearBatch();
				conn.commit();
				long end = System.currentTimeMillis();
				System.out.println("运行时间=" + (end - start));
				ps.close();
				iterator++;
				count = 0;
			}
			logger.info("after insert");
			rs.close();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			SendHelper.sendError(tableName, e);
		} finally {
			if(conn!=null) {
				try {
					if(!conn.isClosed())
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(sourceConn!=null) 
			{
				try {
					if(!sourceConn.isClosed())
					sourceConn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					
					e1.printStackTrace();
				}
			}
		}
		return false;
	}

	public int getProcess() {
		return process;
	}

	public int getColumnCount(InputStream in) {
		try {
			JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(in)));
			jsonReader.beginObject();
			int count = 0;
			while (jsonReader.hasNext()) {
				String name = jsonReader.nextName();
				if (name.equals("attr")) {
					jsonReader.beginObject();
					while (jsonReader.hasNext()) {
						jsonReader.nextName();
						jsonReader.nextString();
						count++;
					}
					jsonReader.endObject();
				} else {
					jsonReader.skipValue();
				}
			}
			jsonReader.endObject();
			jsonReader.close();
			in.close();
			return count;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public boolean createTable(List<Column> columns, int columnCount, String prgName, boolean addTableName) {
		if (!analyzeParameter()) {
			return false;
		}
		for (int i = 0; i < columnCount; i++) {
			String tableName = columns.get(i).getTableName();
			ArrayList<Column> cols = (ArrayList<Column>) tables.get(tableName);
			if (cols != null) {
				cols.add(columns.get(i));
			} else {
				cols = new ArrayList<Column>();
				cols.add(columns.get(i));
				tables.put(tableName, cols);
			}
		}
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		Connection conn =null;
		try {
			conn = DriverManager.getConnection(this.url, this.username, this.password);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS `" + prgName + "`;");
			String sql;
			if (this.type.equalsIgnoreCase("ORACLE")) {
				sql = "CREATE TABLE " + prgName.toUpperCase() + " (";
			} else {
				sql = "CREATE TABLE " + prgName.toUpperCase() + " (";
			}
			Iterator it = tables.entrySet().iterator();
			while (it.hasNext()) {// 遍历，依次创建每个表
				Map.Entry entry = (Map.Entry) it.next();
				String table = (String) entry.getKey();
				ArrayList<Column> cols = (ArrayList<Column>) entry.getValue();
				for (Column column : cols) {// 创建每个字段
					if (addTableName) {
						sql = sql + "`" + column.getTableName() + "." + column.getColumnName() + "` ";
					} else {
						sql = sql + "`" + column.getColumnName() + "` ";
					}
					sql = sql + TypeMap.jdbcTypeToMysql(column);

					sql = sql + ", ";

					// if((column.isSigned()=="false")){//为无符号类型变量
					// sql=sql+"unsigned ";
					/*
					 * }if(!column.isNull()){//字段是否允许为空 sql=sql+"NOT NULL "; }else {
					 * sql=sql+"DEFAULT NULL "; }
					 */
				}
			}
			if (sql.endsWith(", ")) // 去掉最后的“，”
				sql = sql.substring(0, sql.length() - 2);

			sql = sql + ")";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			conn.close();
			return true;
		} catch (SQLException e) {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
			return false;
		}
	}

	public boolean createOracleTable(List<Column> columns, int columnCount, String prgName, boolean addTableName) {
		if (!analyzeParameter()) {
			return false;
		}
		for (int i = 0; i < columnCount; i++) {
			String tableName = columns.get(i).getTableName();
			ArrayList<Column> cols = (ArrayList<Column>) tables.get(tableName);
			if (cols != null) {
				cols.add(columns.get(i));
			} else {
				cols = new ArrayList<Column>();
				cols.add(columns.get(i));
				tables.put(tableName, cols);
			}
		}
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		try {
			Connection conn = DriverManager.getConnection(this.url, this.username, this.password);
			Statement stmt = conn.createStatement();
			ResultSet exist = stmt.executeQuery(
					"select count(*) as count from user_tables where table_name='" + prgName.toUpperCase() + "'");
			int count = 0;
			while (exist.next()) {
				count = exist.getInt("count");
			}
			if (count > 0)
				stmt.executeUpdate("DROP TABLE \"" + prgName.toUpperCase() + "\"");

			String sql;
			if (this.type.equalsIgnoreCase("ORACLE")) {
				sql = "CREATE TABLE " + prgName.toUpperCase() + " (";
			} else {
				sql = "CREATE TABLE " + prgName.toUpperCase() + " (";
			}
			Iterator it = tables.entrySet().iterator();
			while (it.hasNext()) {// 遍历，依次创建每个表
				Map.Entry entry = (Map.Entry) it.next();
				String table = (String) entry.getKey();
				ArrayList<Column> cols = (ArrayList<Column>) entry.getValue();
				for (Column column : cols) {// 创建每个字段，在oracle中用双引号，在mysql中用1左边的`符号
					if (addTableName) {
						sql = sql + "\"" + column.getTableName() + "." + column.getColumnName() + "\" ";
					} else {
						sql = sql + "\"" + column.getColumnName() + "\" ";
					}

					sql = sql + TypeMap.jdbcTypeToOracle(column);

					sql = sql + ", ";

					// if((column.isSigned()=="false")){//为无符号类型变量
					// sql=sql+"unsigned ";
					/*
					 * }if(!column.isNull()){//字段是否允许为空 sql=sql+"NOT NULL "; }else {
					 * sql=sql+"DEFAULT NULL "; }
					 */
				}
			}
			if (sql.endsWith(", ")) // 去掉最后的“，”
				sql = sql.substring(0, sql.length() - 2);

			sql = sql + ")";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private int getRowCount(ResultSet rs) {
		int rowCount = 0;
		try {
			rs.last();
			rowCount = rs.getRow();
			rs.beforeFirst();
			return rowCount;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public List<Column> initColumn(InputStream in, int columnCount) {
		int size = columnCount;
		try {
			JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(in)));
			jsonReader.beginObject();

			Column[] column = new Column[size];
			for (int i = 0; i < size; i++) {
				column[i] = new Column();
			}
			while (jsonReader.hasNext()) {
				String name = jsonReader.nextName();
				if (name.equals("attr")) {
					jsonReader.beginObject();
					for (int i = 0; i < size; i++) {
						column[i].setColumnName(jsonReader.nextName());
						column[i].setColumnTypeName(jsonReader.nextString());
					}
					jsonReader.endObject();
					continue;
				}
				if (name.equals("signed")) {
					jsonReader.beginObject();
					for (int i = 0; i < size; i++) {
						jsonReader.nextName();
						column[i].setSigned(jsonReader.nextString());
					}
					jsonReader.endObject();
					continue;
				}
				if (name.equals("table")) {
					jsonReader.beginObject();
					for (int i = 0; i < size; i++) {
						jsonReader.nextName();
						column[i].setTableName(jsonReader.nextString());
					}
					jsonReader.endObject();
					continue;
				}
				if (name.equals("precision")) {
					jsonReader.beginObject();
					for (int i = 0; i < size; i++) {
						jsonReader.nextName();
						column[i].setPrecision(jsonReader.nextString());
					}
					jsonReader.endObject();
					continue;
				}
				if (name.equals("scale")) {
					jsonReader.beginObject();
					for (int i = 0; i < size; i++) {
						jsonReader.nextName();
						column[i].setScale(jsonReader.nextString());
					}
					jsonReader.endObject();
					continue;
				} else {
					jsonReader.skipValue();
				}

			}
			jsonReader.endObject();
			jsonReader.close();
			in.close();
			List<Column> list = Arrays.asList(column);
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
