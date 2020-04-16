package gri.engine.reflect;

import java.util.Map;

public class MethodMapper {

	public MethodMapper() {
		// TODO Auto-generated constructor stub
	}
	private static class SingletonHolder {
		private static Map<String, String> methodMap = XMLParser.getMapFromXML();//使用单例模式，只在第一次调用时初始化一次methodMap
	}

	public static Map<String, String> getMethodMapper() {
		return SingletonHolder.methodMap;
	}
}
