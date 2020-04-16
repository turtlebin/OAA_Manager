package gri.engine.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gri.engine.parse.HTMLParser;
import gri.engine.parse.MSExcelParser;
import gri.engine.parse.MSPowerPointParser;
import gri.engine.parse.MSWordParser;
import gri.engine.parse.PDFParser;
import gri.engine.parse.TextParser;
import gri.engine.util.Constant;

public class ParseService {//该类的主要作用是生成预览文件

	private static final Logger LOGGER = LoggerFactory.getLogger(ParseService.class);

	private String cacheUUID;
	private String extension;

	public String getCacheUUID() {
		return cacheUUID;
	}

	public void setCacheUUID(String cacheUUID) {
		this.cacheUUID = cacheUUID;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public ParseService(String cacheUUID, String extension) {
		super();
		this.cacheUUID = cacheUUID;
		this.extension = extension;
	}

	public void parse() {
		LOGGER.info("解析数据文件...");
		String text = "";
		if ("doc".equalsIgnoreCase(this.extension)) {
			MSWordParser ms = new MSWordParser();
			try {
				text = ms.renderText(new File(Constant.CacheFolder + this.cacheUUID));
				LOGGER.info("预览内容字符数为：" + text.length());
			} catch (Exception ex) {
				LOGGER.info("解析文件内容出错");
				return;
			}
		} else if ("xls".equalsIgnoreCase(this.extension)) {
			MSExcelParser ms = new MSExcelParser();
			try {
				text = ms.renderText(new File(Constant.CacheFolder + this.cacheUUID));
				LOGGER.info("预览内容字符数为：" + text.length());
			} catch (Exception ex) {
				LOGGER.info("解析文件内容出错");
				return;
			}
		} else if ("ppt".equalsIgnoreCase(this.extension)) {
			MSPowerPointParser ms = new MSPowerPointParser();
			try {
				text = ms.renderText(new File(Constant.CacheFolder + this.cacheUUID));
				LOGGER.info("预览内容字符数为：" + text.length());
			} catch (Exception ex) {
				LOGGER.info("解析文件内容出错");
				return;
			}
		} else if ("pdf".equalsIgnoreCase(this.extension)) {
			PDFParser ms = new PDFParser();
			try {
				text = ms.renderText(new File(Constant.CacheFolder + this.cacheUUID));
				LOGGER.info("预览内容字符数为：" + text.length());
			} catch (Exception ex) {
				LOGGER.info("解析文件内容出错");
				return;
			}
		} else if ("txt".equalsIgnoreCase(this.extension) || "db".equalsIgnoreCase(this.extension)) {
			TextParser ms = new TextParser();
			try {
				text = ms.renderText(new File(Constant.CacheFolder + this.cacheUUID));
				LOGGER.info("预览内容字符数为：" + text.length());
			} catch (Exception ex) {
				LOGGER.info("解析文件内容出错");
				return;
			}
		} else if ("html".equalsIgnoreCase(this.extension) || "htm".equalsIgnoreCase(this.extension)) {
			HTMLParser ms = new HTMLParser();
			try {
				text = ms.renderText(new File(Constant.CacheFolder + this.cacheUUID));
				LOGGER.info("预览内容字符数为：" + text.length());
			} catch (Exception ex) {
				LOGGER.info("解析文件内容出错");
				return;
			}
		}
		// 其他格式类型不解析生成预览文件

		if (!text.equals("")) {
			File file = new File(Constant.CacheFolder + this.cacheUUID + ".txt");
			try {
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
				out.write(text.toCharArray());
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.info("保存预览内容出错");
			}
		}
	}

}
