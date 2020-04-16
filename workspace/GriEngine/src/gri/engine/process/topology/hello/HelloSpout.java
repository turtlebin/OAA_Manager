/**
* Created by weiwenjie on 2017-3-23
*/
package gri.engine.process.topology.hello;

import gri.engine.process.KafkaProcessPipe;
import gri.engine.process.ProcessPipe;
import gri.engine.process.MessagePack;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
/**
 * @Description 
 * Created by weiwenjie on 2017-3-23
 */
public class HelloSpout extends BaseRichSpout {

	private static final long serialVersionUID = -4287209449750623371L;
	
	private static final String TOPIC = "merge";
	
	private static final Logger log = Logger.getLogger(HelloSpout.class);

	private SpoutOutputCollector collector;
	
	public HelloSpout() {
	}

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("data"));
	}

	
	public void nextTuple() {
		ProcessPipe processPipe=new KafkaProcessPipe();
		List<MessagePack> ls = processPipe.poll(TOPIC, 100);
		for(MessagePack processPipeData : ls){
			System.out.println("emit:"+processPipeData.toString());
			collector.emit(new Values(processPipeData));
		}
		/*if(counter<TIMES){
			try {
				Thread.sleep(10);
				//emit加一个参数，作为Value的id，方便重发
				collector.emit(new Values(Integer.toString(counter)),counter);
			} catch (InterruptedException e) {
	            e.printStackTrace();
	        }
			counter++;
		}*/
		
	}


	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
	}
	
	/*@Override
	//ack方法在处理记录成功后调用
	public void ack(Object msgId) {
		System.out.println("message sends successfully (counter = " + msgId +")");
	}

	@Override
	//fail方法在处理记录失败后调用，一般是重发记录
	public void fail(Object msgId) {
		System.out.println("error : message sends unsuccessfully (counter = " + msgId +")");
		System.out.println("resending...");
		collector.emit(new Values(Integer.toString((Integer)msgId)), msgId);
		System.out.println("resend successfully");
	}*/
	
	

}
