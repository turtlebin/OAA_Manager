package gri.manager.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

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

}
