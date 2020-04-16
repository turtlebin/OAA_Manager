/**
* Created by weiwenjie on 2017-5-22
*/
package gri.engine.process.full;

import gri.driver.model.process.View;
import gri.engine.service.RedisContainerService;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

/**
 * @Description 
 * Created by weiwenjie on 2017-5-22
 */
public class MergeDProcessor implements FullProcessor{
	private RedisContainerService redis;
	public MergeDProcessor(){
		redis = new RedisContainerService("MergeD");
		redis.cleanView();
	}
	public void process(String data, String viewName, String config){
		JSONArray tempArr = JSONArray.fromObject(data);
		for(int i =0;i<tempArr.size();i++)
			redis.lpush(viewName, tempArr.getJSONObject(i).toString());
	}
	public String getResult(String viewName){
		List<String> ls = redis.lget(viewName);
		JSONArray arr = new JSONArray();
		for(String str:ls) arr.add(str);
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
