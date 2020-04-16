package gri.engine.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;

import org.apache.commons.net.ftp.FTPClient;

public class FileDataService {

	private String dataSourceURL;

	private String host;
	private int port;
	private String username;
	private String password;
	private String ftpFilePath;

	private boolean analyzeParameter() {
		String strs[] = this.dataSourceURL.split("###");
		String urlStr = strs[0];
		this.username = strs.length > 1 ? strs[1] : "anonymous";
		this.password = strs.length > 2 ? strs[2] : "anonymous";
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
		this.host = url.getHost();
		this.port = url.getPort() == -1 ? 21 : url.getPort();
		this.ftpFilePath = url.getFile();
		return true;
	}

	public FileDataService(String dataSourceURL) {
		this.dataSourceURL = dataSourceURL;
	}

	public boolean download(OutputStream out) {
		if (!analyzeParameter())
			return false;
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(this.host, this.port);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);// 重要
			if (!ftpClient.login(this.username, this.password))
				return false;
			this.ftpFilePath = new String(this.ftpFilePath.getBytes("GBK"), "ISO-8859-1");// 重要
			if (!ftpClient.retrieveFile(this.ftpFilePath, out))//从服务器检索命名文件并将其写入给定的OutputStream中。
				return false;
			out.close();
			return true;
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean upload(InputStream in) {
		if (!analyzeParameter())
			return false;
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(this.host, this.port);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);// 重要
			if (!ftpClient.login(this.username, this.password))
				return false;
			this.ftpFilePath = new String(this.ftpFilePath.getBytes("GBK"), "ISO-8859-1");// 重要
			if (!ftpClient.storeFile(this.ftpFilePath, in))
				return false;
			return true;
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
