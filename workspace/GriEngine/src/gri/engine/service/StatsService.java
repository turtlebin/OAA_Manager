package gri.engine.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import gri.driver.model.process.FilterCond;
import gri.driver.model.process.GroupBy;
import gri.driver.model.process.StatsConf;
import gri.driver.model.process.StatsContent;
import gri.driver.model.process.StatsResult;
import gri.driver.util.DriverConstant;

/**
 * @Description 
 * Created by weiwenjie on 2016-8-13
 */
public class StatsService {
	private JSONArray srcData;
	private StatsConf conf;
	StatsResult result;
	Map<String,List<JSONObject>> groupMap;
	
	public StatsService(String json, StatsConf conf){
		System.out.println(conf.toString());
		json=(String) json.subSequence(json.indexOf("["), json.length()-1);
		this.srcData=JSONArray.fromObject(json);
		this.conf=conf;		
		groupMap = new HashMap<String,List<JSONObject>>();
		result = new StatsResult();
		result.setTitle(conf.getViewName());
		result.setType(conf.getGraphType());
		result.setRowSummary(conf.getRowSummary());
		result.setColSummary(conf.getColSummary());
		result.setRowName(new ArrayList<String>());
		result.setColName(new ArrayList<String>());
		result.setData(new ArrayList<List<Double>>());
	}
	
	public StatsResult calculate(){
		//先进行数据过滤
		JSONArray newArr = new JSONArray();
		for(int j = 0;j<srcData.size();j++){
			boolean flag = true;
			for(int i = 0;i<conf.getFilters().size();i++){			
				if(!filter(srcData.getJSONObject(j),conf.getFilters().get(i))){
					flag = false;
					break;
				}					
			}
			if(flag) newArr.add(srcData.getJSONObject(j));
		}
		srcData = newArr;
		
		if(conf.getGroupBys().size()==1) return calculate1();
		else return calculate2();
	}
	
	//一维分析
	public StatsResult calculate1(){
		//设置行，列标题
		if(conf.getRowSummary()==null || conf.getRowSummary().equals(""))
			conf.setRowSummary(conf.getGroupBys().get(0).getFieldName());
		
		//计算分组可能取值,并由key映射到数组下标
		Map<String,Integer> keyMap = getKeyMap(conf.getGroupBys().get(0));
		
		//计算分组
		for(int i =0; i<srcData.size(); i++){
			JSONObject obj = (JSONObject)srcData.get(i);
			String group=getGroup(obj,conf.getGroupBys().get(0));
			List<JSONObject> inGroup=groupMap.get(group);
			if(inGroup == null){
				inGroup=new ArrayList<JSONObject>();
				groupMap.put(group, inGroup);
			}
			inGroup.add(obj);
		}		
		
		//计算结果值
		//这里算的行列搞反了
		for(int i =0; i<conf.getStatsContents().size(); i++){
			List<Double> tempList = new ArrayList<Double>(keyMap.size());
			for(int j= 0; j<keyMap.size();j++) tempList.add(0.0);  
			for(String key : groupMap.keySet()){
				List<JSONObject> objects = groupMap.get(key);
				Double result= calcStep(objects,conf.getStatsContents().get(i));
				tempList.set(keyMap.get(key), result);
			}
			result.getData().add(tempList);
		}
		
		//计算行名
		result.setRowName(getRowName(new ArrayList<String>(keyMap.keySet()),conf.getGroupBys().get(0)));
		
		//计算列名
		List<String> colName=new ArrayList<String>();
		for(StatsContent statsContent :conf.getStatsContents()){
			String type="";
			switch(statsContent.getOperType()){
			case DriverConstant.StatsOperType_Count: type="数量";break;
			case DriverConstant.StatsOperType_Sum: type="总和";break;
			case DriverConstant.StatsOperType_Avg: type="平均值";break;
			case DriverConstant.StatsOperType_Max: type="最大值";break;
			case DriverConstant.StatsOperType_Min: type="最小值";break;
			default: break;
			}
			colName.add(statsContent.getFieldName()+"的"+type);
		}
		result.setColName(colName);
		
		return result;
	}
	
	//二维分析
	public StatsResult calculate2(){
		//设置行，列标题
		if(conf.getRowSummary()==null || conf.getRowSummary().equals(""))
			conf.setRowSummary(conf.getGroupBys().get(0).getFieldName());
		if(conf.getColSummary()==null || conf.getColSummary().equals(""))
			conf.setColSummary(conf.getGroupBys().get(1).getFieldName());
		
		//计算分组可能取值,并由key映射到数组下标
		Map<String,Integer> [] keyMap = new Map[2];
		for(int i=0;i<2;i++)
			keyMap[i]=getKeyMap(conf.getGroupBys().get(i));
		
		//计算分组
		for(int i =0; i<srcData.size(); i++){
			JSONObject obj = (JSONObject)srcData.get(i);
			String group1=getGroup(obj,conf.getGroupBys().get(0));
			String group2=getGroup(obj,conf.getGroupBys().get(1));
			String group=group1+";"+group2;
			List<JSONObject> inGroup=groupMap.get(group);
			if(inGroup == null){
				inGroup=new ArrayList<JSONObject>();
				groupMap.put(group, inGroup);
			}
			inGroup.add(obj);
		}		
		
		//计算结果值
		for(int i = 0; i<keyMap[1].size(); i++){
			List<Double> tempList = new ArrayList<Double>(keyMap[1].size());
			for(int j = 0; j<keyMap[0].size(); j++) tempList.add(0.0);  
			result.getData().add(tempList);
		}
		for(String key : groupMap.keySet()){
			List<JSONObject> objects = groupMap.get(key);
			Double result= calcStep(objects,conf.getStatsContents().get(0));
			String []groups = key.split(";");
			this.result.getData().get(keyMap[1].get(groups[1])).set(keyMap[0].get(groups[0]),result);
		}
		
		//计算行名
		result.setRowName(getRowName(new ArrayList<String>(keyMap[0].keySet()),conf.getGroupBys().get(0)));
		
		//计算列名
		result.setColName(getRowName(new ArrayList<String>(keyMap[1].keySet()),conf.getGroupBys().get(1)));
		
		return result;
	}
	
	private String getGroup(JSONObject obj, GroupBy groupBy){
		
		switch(groupBy.getOperType()){
		case DriverConstant.GroupByType_Enum: 
			return obj.get(groupBy.getFieldName()).toString();
		case DriverConstant.GroupByType_Part:
			return "";
		case DriverConstant.GroupByType_Interval:
			return "";
		case DriverConstant.GroupByType_Day:
			return "";
		case DriverConstant.GroupByType_Month:
			return "";
		case DriverConstant.GroupByType_Season:
			return "";
		case DriverConstant.GroupByType_Year:
			return "";
		case DriverConstant.GroupByType_EveryWeekDay:{
			SimpleDateFormat format;
			Date date;
			Calendar calendar;
			format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			date = null;
			try {
				date=format.parse(obj.get(groupBy.getFieldName()).toString().split("/.")[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			calendar= Calendar.getInstance();
			calendar.setTime(date);
			return Integer.toString(calendar.get(Calendar.DAY_OF_WEEK));
			}
		default : return "";
		}
	}
	
	private boolean filter(JSONObject obj, FilterCond filterCond){
		switch(filterCond.getOperType()){
		case DriverConstant.FilterOperType_Equal:
			return true;
			//break;
		case DriverConstant.FilterOperType_NotEqual:
			return true;
			//break;
		case DriverConstant.FilterOperTypeMoreThan:
			return true;
			//break;
		case DriverConstant.FilterOperTypeLessThan:
			return true;
			//break;
		case DriverConstant.FilterOperTypeCustom:
			return true;
			//break;
		default : 
			return true;
			//break;
		}
	}
	
	private Map<String,Integer> getKeyMap(GroupBy groupBy){
		Map<String,Integer> map = new TreeMap<String,Integer>();
		for(int i =0; i<srcData.size();i++){
			String key = getGroup((JSONObject)srcData.get(i),groupBy);
			if(!map.containsKey(key)) map.put(key,0);
		}
			
		int index;
		switch(groupBy.getOperType()){
		case DriverConstant.GroupByType_Enum:
			index=0;
			for(String key : map.keySet())
				map.replace(key, index++);
			break;
		case DriverConstant.GroupByType_Part:
			break;
		case DriverConstant.GroupByType_Interval:
			break;
		case DriverConstant.GroupByType_Day:
			break;
		case DriverConstant.GroupByType_Month:
			break;
		case DriverConstant.GroupByType_Season:
			break;
		case DriverConstant.GroupByType_Year:
			break;
		case DriverConstant.GroupByType_EveryWeekDay:
			map = new HashMap<String,Integer>();
			for(int i = 2 ;i<8 ; i++) map.put(Integer.toString(i), i-2);
			map.put(Integer.toString(1), 6);
			break;
		default : break;
		}
		
		return map;
	}
	
	private double calcStep(List<JSONObject> objects , StatsContent content){
		double sum;
		switch(content.getOperType()){
		case DriverConstant.StatsOperType_Count:
			if(content.getFieldName().equals("*")) return objects.size();
			else{
				Set<String> tempSet = new HashSet<String>();
				for(int i = 0;i<objects.size();i++)
					tempSet.add(objects.get(i).get(content.getFieldName()).toString());
				return tempSet.size();
			}
		case DriverConstant.StatsOperType_Sum:
			sum =0;
			for(int i = 0;i<objects.size();i++)
				sum+=Double.parseDouble(objects.get(i).get(content.getFieldName()).toString());
			return sum;
		case DriverConstant.StatsOperType_Avg: 
			sum =0;
			for(int i = 0;i<objects.size();i++)
				sum+=Double.parseDouble(objects.get(i).get(content.getFieldName()).toString());
			return sum/objects.size();
		case DriverConstant.StatsOperType_Max:
			double max = (objects.size()>0)  ? Double.parseDouble(objects.get(0).get(content.getFieldName()).toString()):0;
			for(int i = 0;i<objects.size();i++){
				double value=Double.parseDouble(objects.get(i).get(content.getFieldName()).toString());
				if(value>max) max= value;
			}
			return max;
		case DriverConstant.StatsOperType_Min:
			double min = (objects.size()>0)  ? Double.parseDouble(objects.get(0).get(content.getFieldName()).toString()):0;
			for(int i = 0;i<objects.size();i++){
				double value=Double.parseDouble(objects.get(i).get(content.getFieldName()).toString());
				if(value<min) min= value;
			}
			return min;
		default : return 0.0;
		}
	}
	
	private List<String> getRowName(List<String> groups , GroupBy groupBy){
		switch(groupBy.getOperType()){
		case DriverConstant.GroupByType_Enum: return groups;
		case DriverConstant.GroupByType_EveryWeekDay:{
			String []arr = new String[]{"周一","周二","周三","周四","周五","周六","周日"};
			return Arrays.asList(arr);
			}
		default : return groups;
		}
	}
	
}
