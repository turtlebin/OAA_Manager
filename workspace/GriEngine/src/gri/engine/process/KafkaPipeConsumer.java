/**
* Created by weiwenjie on 2017-3-23
*/
package gri.engine.process;

import gri.driver.model.process.Process;
import gri.engine.model.dao.ProcessDao;
import gri.engine.util.Constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-23
 */
public class KafkaPipeConsumer {
	private static Map<String,Consumer<String, String>> consumers = new ConcurrentHashMap<String,Consumer<String, String>>();
	
	public static Consumer<String,String> getConsumer(String groupName){
		if (!consumers.containsKey(groupName)){
			synchronized(KafkaPipeConsumer.class) {
		      if (!consumers.containsKey(groupName))createConsumer(groupName);
		    }
		}
		
		return consumers.get(groupName);
	}
	
	public static void createConsumer(String groupName){
		System.out.println("createConsumer:"+groupName);
		
		List<String> paragraphIds =  new ProcessDao().listParagraphIdByProcessorName(groupName);
		
		
		Properties props = new Properties();
        props.put("bootstrap.servers",Constant.kafka_server);
        //用来唯一标识consumer进程所在组的字符串，如果设置同样的group id，表示这些processes都是属于同一个consumer group
        props.put("group.id", groupName);
        //如果为真，consumer所fetch的消息的offset将会自动的同步到zookeeper。这项提交的offset将在进程挂掉时，由新的consumer使用
        props.put("enable.auto.commit", "true");
        //consumer向zookeeper提交offset的频率
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        
        //订阅topic，可以为多个用,隔开Arrays.asList("topic1","topic2");
        consumer.subscribe(paragraphIds);
        consumers.put(groupName, consumer);
	}
}
