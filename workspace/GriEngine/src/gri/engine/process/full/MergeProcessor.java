/**
* Created by weiwenjie on 2017-5-22
*/
package gri.engine.process.full;

import gri.driver.model.process.View;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

/**
 * @Description 
 * Created by weiwenjie on 2017-5-22
 */
public class MergeProcessor implements FullProcessor{
	private String arr ="[]";
	
	public void process(String data, String viewName, String config){
		//JSONArray tempArr = JSONArray.fromObject(data);
		if(arr.equals("[]") || data.equals("[]")) arr = arr.substring(0, arr.length()-1) + data.substring(1, data.length());
		else arr =  arr.substring(0, arr.length()-1) +"," + data.substring(1, data.length());
	}
	public String getResult(String viewName){
		return arr;
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
