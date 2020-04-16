/**
* Created by weiwenjie on 2017-5-22
*/
package gri.engine.process.full;

import gri.driver.model.process.View;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Description 
 * Created by weiwenjie on 2017-5-22
 */
public class GroupProcessor implements FullProcessor{
	private JSONArray result = new JSONArray();
	
	public void process(String data, String viewName, String config){
		JSONArray dataArr = JSONArray.fromObject(data);
		JSONObject configObj = JSONObject.fromObject(config);
		String fieldName = configObj.getString("field");
		int groupNo = configObj.getInt("groupNo");
		for(int i = 0;i<dataArr.size();i++){
			JSONObject obj = (JSONObject)dataArr.get(i);
			int hash = obj.get(fieldName).hashCode();
			if(hash % groupNo == Integer.parseInt(viewName)) result.add(obj);
		}
	}
	public String getResult(String viewName){
		return result.toString();
	}
	
	public List<View> getViews(String config){
		List<View> ls = new ArrayList<View>();
		JSONObject configObj = JSONObject.fromObject(config);
		int groupNo = configObj.getInt("groupNo");
		for(int i=0;i<groupNo;i++){
			View view =new View();
			view.setSubViewName(Integer.toString(i));
			view.setStore("redis");
			view.setStructure("list");
			view.setVirtual(false);
			ls.add(view);
		}
		return ls;
	}
}
