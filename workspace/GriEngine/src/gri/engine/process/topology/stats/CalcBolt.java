/**
* Created by weiwenjie on 2017-3-23
*/
package gri.engine.process.topology.stats;

import gri.driver.model.process.GroupBy;
import gri.driver.model.process.StatsConf;
import gri.driver.model.process.StatsContent;
import gri.driver.util.DriverConstant;
import gri.engine.process.MessagePack;
import gri.engine.service.RedisContainerService;
import gri.engine.service.ViewService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-23
 */
public class CalcBolt extends BaseRichBolt {

	private static final long serialVersionUID = 4223708336037089125L;
	
	private static final String viewName = "stats";
	
	private static final Logger log = Logger.getLogger(CalcBolt.class);

	private OutputCollector collector;
	
	public CalcBolt() {
	}
	
	
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector =collector;
	}

	
	public void execute(Tuple input) {
		MessagePack processPipeData = (MessagePack) input.getValueByField("data");
		System.out.println("calc-bolt:"+ processPipeData.toString() + ", group:" +input.getValueByField("group"));
		JSONObject data = JSONObject.fromObject(processPipeData.getData());
		
		StatsConf conf = StatsUtils.jsonToStatsConf(processPipeData.getConfig());
		
		for(StatsContent statsContent : conf.getStatsContents())
			calcStep(data,StatsSpout.VIEWNAME,(String)input.getValueByField("group"),statsContent);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("data"));	
	}
	
	private void calcStep(JSONObject object , String viewName, String group, StatsContent content){
		String contentAbs = content.getFieldName()+"__"+content.getOperType();
		RedisContainerService redisViewService = new RedisContainerService(viewName);
		
		String oriStr = redisViewService.hget(contentAbs, group);
		Double ori = (oriStr != null) ? Double.parseDouble(oriStr): null;
		
		double cur;
		boolean change = false;
		switch(content.getOperType()){
		case DriverConstant.StatsOperType_Count:	
			if(content.getFieldName().equals("*")){
				if(ori == null) ori=0.0;
				ori++;
				change = true;				
			}
			else{
				String colValue = object.get(content.getFieldName()).toString();
				redisViewService.sadd(contentAbs+"_"+group+"_"+"countSet", colValue);
				cur = redisViewService.slen(contentAbs+"_"+group+"_"+"countSet");
				if(cur!=ori){
					ori = cur;
					change = true;	
				}
			}
			break;
		case DriverConstant.StatsOperType_Sum:
			cur = Double.parseDouble(object.get(content.getFieldName()).toString());
			if(ori == null){
				ori=0.0;
				change = true;
			}
			else if(cur != 0){
				change = true;
				ori = ori + cur;
			}
			break;
		case DriverConstant.StatsOperType_Avg: 
			double num=0;
			cur = Double.parseDouble(object.get(content.getFieldName()).toString());
			String numAbs = content.getFieldName()+"_"+"avgNum";
			if(ori == null){
				ori = cur;
				num = 1;
				change = true;
			}
			else{
				num = Double.parseDouble(redisViewService.hget(numAbs, group));			
				if(ori != cur){												
					ori = (num*ori + cur)/(num+1);
					change = true;
				}
				num++;
			}
			redisViewService.hset(numAbs, group, Double.toString(num));
			break;
		case DriverConstant.StatsOperType_Max:
			cur = Double.parseDouble(object.get(content.getFieldName()).toString());
			if(ori == null){
				ori = cur;
				change = true;
			}
			else if(cur > ori){
				ori = cur;
				change = true;
			}
			break;
		case DriverConstant.StatsOperType_Min:
			cur = Double.parseDouble(object.get(content.getFieldName()).toString());
			if(ori == null){
				ori = cur;
				change = true;
			}
			else if(cur < ori){
				ori = cur;
				change = true;
			}
			break;
		}
		
		if(change) redisViewService.hset(contentAbs, group, Double.toString(ori));
	}
}

