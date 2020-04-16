package gri.engine.reflect;

import java.util.HashMap;
import java.util.Map;

public class MapGetter {
	private static class MapHolder{
		public static Map<Integer,String> map=InitialMap();
	}
	private static Map<Integer,String> InitialMap(){
		Map<Integer,String> map=new HashMap<Integer,String>();
		map.put(0, "借满");
		map.put(1,"超期");
		map.put(2, "暂停");
		map.put(3, "欠款");
		map.put(4, "违约");
		map.put(5, "挂失");
		map.put(6, "失效");
		map.put(7, "退证");
		map.put(8, "自停");
		return map;
	}
	public static Map<Integer,String> getStatusMap(){
		return MapHolder.map;
	}
}
