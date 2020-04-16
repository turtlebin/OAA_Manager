/**
* Created by weiwenjie on 2017-5-22
*/
package gri.engine.process.full;

import gri.driver.model.process.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Description 
 * Created by weiwenjie on 2017-5-22
 */
public class WordCountProcessor implements FullProcessor{
	private Map<String,Integer> map = new HashMap<String,Integer>();
	
	public void process(String data, String viewName, String config){
		JSONArray arr = JSONArray.fromObject(data);
		for(int i=0;i<arr.size();i++){
			String word = arr.getJSONObject(i).getString("word");
			if(map.containsKey(word)) map.put(word,map.get(word)+1);
			else map.put(word,1);
		}
			
	}
	public String getResult(String viewName){
		JSONArray arr = new JSONArray();
		
		for (String str : map.keySet()) {
			JSONObject obj = new JSONObject();
			obj.put("word", str);
			obj.put("count", map.get(str));
			arr.add(obj);
		}
		return arr.toString();
	}
	
	public List<View> getViews(String config){
		List<View> ls = new ArrayList<View>();
		View view =new View();
		view.setSubViewName("default");
		view.setStore("redis");
		view.setStructure("list");
		view.setVirtual(false);
		ls.add(view);
		return ls;
	}
}
