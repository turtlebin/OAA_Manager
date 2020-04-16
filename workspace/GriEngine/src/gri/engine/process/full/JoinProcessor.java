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
public class JoinProcessor implements FullProcessor{
	private List<JSONArray> datas = new ArrayList<JSONArray>();
	
	public void process(String data, String viewName, String config){
		JSONArray dataArr = JSONArray.fromObject(data);
		datas.add(dataArr);
	}
	public String getResult(String viewName){
		JSONArray result = new JSONArray();
		
		if(datas.size()!=2 && datas.get(0).size()>0 && datas.get(1).size()>0) return "[]";
		
		List<String>[] colName = new List[2];
		for(int i =0 ;i<2; i++) colName[i]=new ArrayList<String>(datas.get(i).getJSONObject(0).keySet());
		
		System.out.println("first");
		for(int i =0 ;i<2; i++){
			for(int j =0 ;j<colName[i].size(); j++){
				System.out.print(colName[i].get(j)+" ");
			}
			System.out.println(" ");
		}
		
		String sameCol = "";
		for(int i =0 ;i<colName[0].size(); i++){
			for(int j =0 ;j<colName[1].size(); j++){
				if(colName[0].get(i).equals(colName[1].get(j))){
					sameCol = colName[0].get(i);
					break;
				}
			}
			if(!sameCol.equals("")) break;
		}
		if(sameCol.equals("")) return "[]";
		
		colName[0].remove(sameCol);
		colName[1].remove(sameCol);
		System.out.println("second");
		for(int i =0 ;i<2; i++){
			for(int j =0 ;j<colName[i].size(); j++){
				System.out.print(colName[i].get(j)+" ");
			}
			System.out.println(" ");
		}
		
		for(int i =0 ;i<datas.get(0).size(); i++){
			for(int j =0 ;j<datas.get(1).size(); j++){
				JSONObject obj1 = datas.get(0).getJSONObject(i);
				JSONObject obj2 = datas.get(1).getJSONObject(j);
				if(obj1.get(sameCol).equals(obj2.get(sameCol))){
					JSONObject objr = new JSONObject();
					objr.put(sameCol, obj1.get(sameCol));
					for(int m =0 ;m<colName[0].size(); m++){
						objr.put(colName[0].get(m), obj1.get(colName[0].get(m)));
					}
					for(int m =0 ;m<colName[1].size(); m++){
						objr.put(colName[1].get(m), obj2.get(colName[1].get(m)));
					}
					result.add(objr);
				}
				
			}
		
		}
		System.out.println(result.toString());
		return result.toString();
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
