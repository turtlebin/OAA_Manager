package com.csasc.gridocms.util;


public class FilePathHelper {

	private static final int START_INDEX = 8;

	public static String transferToGridocFilePath(String filePath) {
		String gridocFilePath = "file:///" + filePath.replace('\\', '/');

		return gridocFilePath;
	}

	public static String transferToFilePath(String gridocFilePath) {

		String filePath = gridocFilePath.substring(START_INDEX).replace('\\', '/');

		return filePath;
	}

	/**
	 * 删除文件路径的首斜杠 "/c:/test.txt" -> "c:/test.txt"
	 * 
	 * @param path
	 * @return
	 */
	public static String formatPath(String path) {
		if (path.length() == 0)
			return path;
		if (path.substring(0, 1).equals("/"))
			return path.substring(1, path.length());
		else
			return path;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileTypeFromPath(String path) {
		int index = path.lastIndexOf(".");
		if (-1 == index)
			return "";
		else
			return path.substring(index + 1).toLowerCase();
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileTypeFromName(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (-1 == index)
			return "";
		else
			return fileName.substring(index + 1).toLowerCase();
	}

	/**
	 * 去除文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileNameWithNoExtension(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	/**
	 * 文件名是否合法
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isValidFileName(String fileName) {
		if (fileName == null || fileName.length() > 255)
			return false;
		else
			return fileName.matches(
					"[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
	}

}
