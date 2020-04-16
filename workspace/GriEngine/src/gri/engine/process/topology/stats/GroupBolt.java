/**
* Created by weiwenjie on 2017-3-23
*/
package gri.engine.process.topology.stats;

import gri.driver.model.process.GroupBy;
import gri.driver.model.process.StatsConf;
import gri.driver.model.process.StatsContent;
import gri.driver.util.DriverConstant;
import gri.engine.process.MessagePack;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
public class GroupBolt extends BaseRichBolt {

	private static final long serialVersionUID = 4223708336037089125L;

	private static final Logger log = Logger.getLogger(GroupBolt.class);

	private OutputCollector collector;
	
	public GroupBolt() {
	}
	
	
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector =collector;
	}

	
	public void execute(Tuple input) {
		MessagePack processPipeData = (MessagePack) input.getValueByField("data");
		//System.out.println("group-bolt:"+ processPipeData.toString());
		JSONObject data = JSONObject.fromObject(processPipeData.getData());
		
		StatsConf conf = StatsUtils.jsonToStatsConf(processPipeData.getConfig());
		
		String group = getGroup(data,conf.getGroupBys().get(0));
		collector.emit(new Values(group,processPipeData));

		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("group","data"));	
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
}

