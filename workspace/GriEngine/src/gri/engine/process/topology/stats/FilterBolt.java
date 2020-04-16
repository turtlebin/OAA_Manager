/**
* Created by weiwenjie on 2017-3-23
*/
package gri.engine.process.topology.stats;

import gri.driver.model.process.FilterCond;
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
public class FilterBolt extends BaseRichBolt {

	private static final long serialVersionUID = 4223708336037089125L;

	private static final Logger log = Logger.getLogger(FilterBolt.class);

	private OutputCollector collector;
	
	public FilterBolt() {
	}
	
	
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector =collector;
	}

	
	public void execute(Tuple input) {
		MessagePack processPipeData = (MessagePack) input.getValueByField("data");
		//System.out.println("group-bolt:"+ processPipeData.toString());
		JSONObject data = JSONObject.fromObject(processPipeData.getData());
		
		StatsConf conf = StatsUtils.jsonToStatsConf(processPipeData.getConfig());
 
		boolean flag = true;
		for(FilterCond cond : conf.getFilters())
			if(!filter(data,cond)) {
				flag = false;
				break;
			}
		if(flag) collector.emit(new Values(processPipeData));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("data"));	
	}
	
	private Boolean filter(JSONObject obj, FilterCond filter){	
		Object cur = obj.get(filter.getFieldName());
		Object compare = filter.getCompare();
		Double cur_d;
		Double compare_d;
		switch(filter.getOperType()){
		case DriverConstant.FilterOperType_Equal: 
			return obj.equals(compare);
		case DriverConstant.FilterOperType_NotEqual:
			return !obj.equals(compare);
		case DriverConstant.FilterOperTypeMoreThan:
			cur_d = (Double) cur;
			compare_d = (Double) compare;
			return cur_d > compare_d;
		case DriverConstant.FilterOperTypeLessThan:
			cur_d = (Double) cur;
			compare_d = (Double) compare;
			return cur_d < compare_d;
		case DriverConstant.FilterOperTypeCustom:
			return true;
		default : 
			return true;
		}
	}
}

