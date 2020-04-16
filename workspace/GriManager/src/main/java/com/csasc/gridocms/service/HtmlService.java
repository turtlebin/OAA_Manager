package com.csasc.gridocms.service;

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintStream;
//import java.io.PrintWriter;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.util.List;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//
//import com.csasc.gridocms.util.FilePathHelper;
//import com.csasc.test.CreatePh;

/**
 * Html文件处理服务
 * 
 * @author 许诺
 *
 */
public class HtmlService {
//	private File htmlFile;// html文件
//	private Document htmlDoc;
//	private List<Paragraph> paras;// 内容源
//
//	public HtmlService(File file, List<Paragraph> paragraphList) {
//		this.htmlFile = file;
//		this.paras = paragraphList;
//
//		try {
//			if (!this.htmlFile.exists())
//				file.createNewFile();
//			else if (this.htmlFile.isDirectory()) {
//				this.htmlFile.delete();
//				file.createNewFile();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("此处不应该显示，严重问题～");
//		}
//	}

//	/**
//	 * 清空html文件内容
//	 */
//	public void clear() {
//		this.htmlFile.delete();
//		try {
//			this.htmlFile.createNewFile();
//			PrintStream printStream = new PrintStream(new FileOutputStream(this.htmlFile));
//			String content = "<html><head><meta http-equiv='Content-Type' content='text/html;charset=UTF-8'/><style>.paragraph {margin:10px; padding:20px; border: 2px dotted #785; background:#f5f5f5}</style></head><body><ol></ol></body></html>";
//			printStream.println(content);
//			printStream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 往html文件写内容
//	 */
//	public void writeContent() {
//		try {
//			this.htmlDoc = Jsoup.parse(this.htmlFile, "UTF-8");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Element body = this.htmlDoc.body();
//		if (this.paras.size() == 0)
//			body.append("<h3>空文档</h3>");
//		Element ol_Element = body.getElementsByTag("ol").get(0);
////		for (Paragraph p : this.paras) {
////			ol_Element.append(getContentOfParagraph(p));
////		}
//		this.writeHTML();
//	}

//	private String getContentOfParagraph(Paragraph paragraph) {
//		boolean bindingFile = false;
//		String dataFilePath = "";
//		String dataFileName = "";
//		String dataFileExtendName = "";
//		if (paragraph.filePath != null) {
//			bindingFile = true;
//			dataFilePath = paragraph.filePath.toUri().getPath();
//			dataFilePath = dataFilePath.substring(1, dataFilePath.length());
//			dataFileName = paragraph.filePath.getName();
//			dataFileExtendName = FilePathHelper.getFileTypeFromName(dataFileName);
//		}
//
//		boolean bindingDB = false;
//		assert (paragraph.attributes.get(1).getName().equals("db_binding"));
//		if (paragraph.attributes.get(1).getValue().equals("yes")) {
//			bindingDB = true;
//		}

		// "<li><b>数据段</b></li>"

//		String paragraphPropertyLI = "<li> <b>基本信息</b> <ul> <li>段名称<ul><b>" + paragraph.getName()
//				+ "</b></ul></li> <li> 关键词<ul>" + paragraph.attributes.get(0).getValue() + "</ul></li> <li>段备注<ul>"
//				+ paragraph.description + "</ul></li> </ul> </li>";
//		String fileContentLI = "";
//		String dbContentLI = "";
//		String paragraphLI = "";

		//
//		if (bindingFile) {
//			// 文本文件
//			if (dataFileExtendName.toLowerCase().equals("txt")) {
//				BufferedReader reader;
//				String txt = new String();
//				try {
//					reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFilePath), "GBK"));
//					String line;
//					while ((line = reader.readLine()) != null) {
//						txt += line + "<br>";
//					}
//					reader.close();
//					fileContentLI = "<li><b>文件数据</b><ul>" + txt + "</ul></li>";
//				} catch (Exception e) {
//					e.printStackTrace();
//					fileContentLI = "<li><b>文件数据</b><ul>文件读取错误，可能是绑定文件失效</ul></li>";
//				}
//			}
//			// 办公文档
//			else if (dataFileExtendName.toLowerCase().equals("docx") || dataFileExtendName.toLowerCase().equals("doc")
//					|| dataFileExtendName.toLowerCase().equals("pptx") || dataFileExtendName.toLowerCase().equals("ppt")
//					|| dataFileExtendName.toLowerCase().equals("xlsx")
//					|| dataFileExtendName.toLowerCase().equals("xls")) {
//				fileContentLI = "<li><b>文件数据</b><ul>办公文档</ul></li>";
//			}
//			// PDF文档
//			else if (dataFileExtendName.toLowerCase().equals("pdf")) {
//				fileContentLI = "<li><b>文件数据</b><ul>PDF文档</ul></li>";
//			}
//			// 视频
//			else if (dataFileExtendName.toLowerCase().equals("rmvb") || dataFileExtendName.toLowerCase().equals("avi")
//					|| dataFileExtendName.toLowerCase().equals("mp4") || dataFileExtendName.toLowerCase().equals("wmv")
//					|| dataFileExtendName.toLowerCase().equals("flv")
//					|| dataFileExtendName.toLowerCase().equals("mpg")) {
//				fileContentLI = "<li><b>文件数据</b><ul>视频数据</ul></li>";
//			}
//			// 图片
//			else if (dataFileExtendName.toLowerCase().equals("jpg") || dataFileExtendName.toLowerCase().equals("png")
//					|| dataFileExtendName.toLowerCase().equals("jpeg") || dataFileExtendName.toLowerCase().equals("bmp")
//					|| dataFileExtendName.toLowerCase().equals("gif")) {
//				fileContentLI = "<li><b>文件数据</b><ul><img src=\"" + dataFilePath + "\"></img></ul></li>";
//			}
//			// 其他文件
//			else {
//				fileContentLI = "<li><b>文件数据</b><ul>其他类型文件</ul></li>";
//			}
//		} else {
//			fileContentLI = "<li><b>文件数据</b><ul>没有绑定文件</ul></li>";
//		}
//		//
//		if (bindingDB) {
//			// 先测试连接
//			String db_type = paragraph.attributes.get(2).getValue();
//			String db_host = paragraph.attributes.get(3).getValue();
//			String db_port = paragraph.attributes.get(4).getValue();
//			String db_username = paragraph.attributes.get(5).getValue();
//			String db_password = paragraph.attributes.get(6).getValue();
//			String db_name = paragraph.attributes.get(7).getValue();
//			String sql = paragraph.attributes.get(8).getValue();
//
//			DBService dbservice = new DBService(db_type, db_host, db_port, db_name, db_username, db_password);
//
//			if (dbservice.connectDB()) {
//				try {
//					ResultSet rs = dbservice.getResultSet(sql);
//					ResultSetMetaData rsmd = rs.getMetaData();
//					int columnCount = rsmd.getColumnCount();
//					String rows = "";
//					while (rs.next()) {
//						String row = "<tr>";
//						for (int i = 1; i <= columnCount; i++)
//							row = row + "<td>" + rs.getString(i) + "</td>";
//						row = row + "</tr>";
//						rows = rows + row;
//					}
//					dbContentLI = "<li><b>数据库数据</b><ul><table>" + rows + "</table></ul></li>";
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					dbContentLI = "<li><b>数据库数据</b><ul>绑定数据库SQL失效，无法预览</ul></li>";
//				}
//
//			} else {
//				dbContentLI = "<li><b>数据库数据</b><ul>绑定数据库连接失效，无法预览</ul></li>";
//			}
//
//		} else {
//			dbContentLI = "<li><b>数据库数据</b><ul>没有绑定数据库</ul></li>";
//		}
//
//		paragraphLI = "<li class=\"paragraph\"><b> 数据段</b><ul>" + paragraphPropertyLI + fileContentLI + dbContentLI
//				+ "</ul></li>";
//
//		return paragraphLI;
//	}

//	private void writeHTML() {
//		PrintWriter printWriter = null;
//		try {
//			printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.htmlFile)));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		printWriter.write(this.htmlDoc.html());
//		printWriter.close();
//	}

}
