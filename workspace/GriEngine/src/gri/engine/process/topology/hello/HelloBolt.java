/**
* Created by weiwenjie on 2017-3-23
*/
package gri.engine.process.topology.hello;

import gri.engine.process.MessagePack;
import gri.engine.service.RedisContainerService;
import gri.engine.service.ViewService;

import java.util.Map;
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
public class HelloBolt extends BaseRichBolt {

	private static final long serialVersionUID = 4223708336037089125L;

	private static final Logger log = Logger.getLogger(HelloBolt.class);

	private OutputCollector collector;
	
	public HelloBolt() {
	}
	
	
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector =collector;
	}

	
	public void execute(Tuple input) {
		MessagePack processPipeData = (MessagePack) input.getValueByField("data");
		System.out.println("bolt:"+ processPipeData.getData());
		RedisContainerService containerService = new RedisContainerService(processPipeData.getContainerName());
		containerService.lpush("result", processPipeData.getData());	
		/*String word = input.getStringByField("str");
		//途中的每个bolt要在emit中加tuple参数，方便监控
		//同时处理完后要ack，每个节点都要ack，而不仅是末端的节点
		collector.emit(input,new Values(word));
		collector.ack(input);*/
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("data"));	
	}
}

