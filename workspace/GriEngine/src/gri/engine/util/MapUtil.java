package gri.engine.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import gri.engine.dest.Column;
import gri.engine.dest.Pair;

public class MapUtil {
	
	public static void show(Map<String,String> map) 
	{
		for(Map.Entry<String, String> entry:map.entrySet())
		{
			System.out.println("columnMap:"+entry.getKey() + ":" + entry.getValue());
		}
		System.out.println("---------------------------------------------------");
	}
	
	public static void show2(Map<String,String> map) 
	{
		for(Map.Entry<String, String> entry:map.entrySet())
		{
			System.out.println("joinMap:"+entry.getKey() + ":" + entry.getValue());
		}
		System.out.println("---------------------------------------------------");
	}
	
	public static void show3(Map<String,Map<String,Column>> map)
	{
		for(Map.Entry<String, Map<String,Column>> entry:map.entrySet())
		{
			for(Map.Entry<String, Column> entry2:entry.getValue().entrySet())
			{
				System.out.println("test:"+entry2.getKey()+":"+entry2.getValue().getColumnName()+"###"+entry2.getValue().getColumnTypeName());
			}
		}
	}
	
	public static void showList(List<String> lists)
	{
		for(String list:lists)
		{
			System.out.println(list);
		}
		System.out.println("-----------------------------");
	}
	
	public static int countMap(Map<String,Column> map)
	{   
		int i=0;
		if(map.isEmpty())
		{
			return 0;
		}
		for(Map.Entry<String, Column> entry:map.entrySet())
		{			
			i++;
		}
		return i;	
	}
	
    public static String getFirstKeyOrNull(Map<String,Map<String,Column>> map) {  
        String obj = null;  
        for (Entry<String,Map<String,Column>> entry : map.entrySet()) {  
            obj = entry.getKey();  
            if (obj != null) {  
                break;  
            }  
        }  
        return  obj;  
    }  

	
	public static void putMap(Map<String,String> map,String destCol,String sourceCol) 
	{
		map.put(destCol, sourceCol);
	}
	
	public static int[] getIntArray(List<Integer> indexList)
	{
		int a[]=new int[indexList.size()];
		for(int i=0;i<indexList.size();i++)
		{
			a[i]=indexList.get(i);
		}
		return a;
	}
	
	public static void removeMap(Map<String,String> map,String destCol) 
	{
		Iterator<Map.Entry<String,String>> it=map.entrySet().iterator();
		while(it.hasNext()) 
		{
			Map.Entry<String, String>entry=it.next();
			if(entry.getKey().equals(destCol)) 
			{
				it.remove();
				break;
			}
		}
	}
	
	public static String[][] getStringArray(Map<String,Column> map)
	{
		int mapCount=MapUtil.countMap(map);
		String[][] strs=new String[mapCount][5];
		int i=0;
		for(Map.Entry<String, Column> entry:map.entrySet())
		{   
			strs[i][0]=entry.getKey();
			strs[i][1]=entry.getValue().getColumnName();
			strs[i][2]=entry.getValue().getColumnTypeName();
			strs[i][3]=entry.getValue().getPrecision();
			strs[i][4]=entry.getValue().getScale();
			i++;
		}
		return strs;
	}
	
	public static <K extends String,V extends String> void showPairList(List<Pair<K,V>> list)
	{
		for(Pair<K,V> pair:list)
		{
			System.out.println(pair.getFirst()+"!!!"+pair.getSecond());
		}
	}
}
