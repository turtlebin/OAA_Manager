/**
* Created by weiwenjie on 2017-3-23
*/
package gri.engine.process;

import gri.engine.util.Constant;

import java.util.Properties;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-23
 */
public class KafkaPipeProducer {
	private static Producer<String,String> producer = createProducer();
	
	public static Producer<String,String> getProducer(){
		return producer;
	}
	
	private static Producer<String,String> createProducer(){
		Properties properties = new Properties();
        //客户端用于建立与kafka集群连接的host:port组，如果有多个broker,则用“,”隔开
		//"host1:port1,host2:port2,host3,post3"
        properties.put("bootstrap.servers", Constant.kafka_server);

        //producer在向servers发送信息后，是否需要serveres向客户端（producer）反馈接受消息状态用此参数配置
        //acks=0:表示producer不需要等待集群服务器发送的确认消息；acks=1:表示producer需要等到topic对应的leader发送的消息确认；
        //acks=all:表示producer需要等到leader以及所有followers的消息确认，这是最安全的消息保障机制
        properties.put("acks", "all");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("buffer.memory", "33554432");
        
        return new KafkaProducer<String,String>(properties);
	}
}
