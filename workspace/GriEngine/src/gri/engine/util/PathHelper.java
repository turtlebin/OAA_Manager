package gri.engine.util;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class PathHelper {

	public static String getExtension(String dataSourePath) {
		String strs[] = dataSourePath.split("###");
		if (strs.length == 0)
			return "";
		String urlStr = strs[0];
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "";
		}
		String ftpFilePath = url.getFile();
		String name = new File(ftpFilePath).getName();
		int pointIndex = name.lastIndexOf('.');
		if (pointIndex == -1)
			return "";
		return name.substring(pointIndex + 1, name.length());
	}
	
	//后台调用数格驱动器时，判断host是否localhost。如果是当前服务器，则直接调用。否则通过Connection类远程访问。
	public static boolean isEITPLocalhost(String host){
		String[] ips = host.split(":");
		boolean isLocal = false;
		try {
			isLocal = ips[0].equalsIgnoreCase("localhost") || ips[0].equals("127.0.0.1") ||
					ips[0].equals(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
		return isLocal && ips[1].equals(Integer.toString(Constant.eitpServerPort));
	}

}
