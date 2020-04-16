/**
* Created by weiwenjie on 2017-3-23
*/
package gri.engine.process;

import gri.driver.model.process.Process;
import gri.engine.model.dao.ProcessDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-23
 */
public class KafkaProcessPipe implements ProcessPipe{
	private static List<MessagePack> emptyLs = new ArrayList<MessagePack>();
	
	@Override
	public void offer(Integer paragraphId, String data) {
		// TODO Auto-generated method stub
		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(Integer.toString(paragraphId),data);
		KafkaPipeProducer.getProducer().send(producerRecord);
	}

	
	@Override
	public List<MessagePack> poll(String processorName ,long timeout) {
		if( new ProcessDao().listParagraphIdByProcessorName(processorName).size()==0) return emptyLs;
		ConsumerRecords<String,String> consumerRecords=KafkaPipeConsumer.getConsumer(processorName).poll(timeout);
		if(consumerRecords.isEmpty()) return emptyLs;
		List<MessagePack> ls = new ArrayList<MessagePack>();
		
		List<Process> processes =  new ProcessDao().listProcessByProcessorName(processorName);
		
		Map<Integer,List<Process>> processesMap = new HashMap<Integer,List<Process>>();
		for(Process process : processes){
			if(!processesMap.containsKey(process.getParagraphId())){
				processesMap.put(process.getParagraphId(), new ArrayList<Process>());
			}		
			processesMap.get(process.getParagraphId()).add(process);
		}
		for(ConsumerRecord<String,String> consumerRecord : consumerRecords){
			Integer paragraphId = Integer.parseInt(consumerRecord.topic());
			List<Process> tempProcesses = processesMap.get(paragraphId);
			for(Process process : tempProcesses){
				MessagePack messagePack = new MessagePack();
				messagePack.setData(consumerRecord.value());
				messagePack.setContainerName(process.getContainerName());
				messagePack.setConfig(process.getConfig());
				ls.add(messagePack);
			}
        }
		for(MessagePack messagePack : ls) System.out.println("messagePacks:"+messagePack.toString());
		return ls;
	}

}
