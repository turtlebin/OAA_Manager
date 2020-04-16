package com.csasc.gridocms.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串处理工具类
 * 
 * @author 许诺
 *
 */

public class StringHelper {
	private StringHelper() {

	}

	public static List<String> keywordsStr2List(String keywordsStr) {
		List<String> result = new ArrayList<String>();
		String strs[] = keywordsStr.split(",| ");
		for (String str : strs)
			if (!str.trim().equals(""))
				result.add(str.trim());
		return result;
	}

	public static String keywordsList2Str(List<String> keywordsList) {
		String res = "";
		for (String str : keywordsList)
			res = res + str + ",";
		if (res.length() > 0)
			res = res.substring(0, res.length() - 1);
		return res;
	}

	/**
	 * 获取给定格路径上的所有节点名<br>
	 * <br>
	 * 例如："/gridoc1/sec1/sec2/para1" 得到 {"gridoc1","sec1","sec2","para1"}
	 * 
	 * @param path
	 *            格节点路径
	 * @return
	 */
	public static List<String> getNodeNamesByPath(String path) {
		List<String> nodes = new ArrayList<String>();
		String[] names = path.split("/");
		for (String name : names)
			if (!name.equals(""))
				nodes.add(name);
		return nodes;
	}

}
