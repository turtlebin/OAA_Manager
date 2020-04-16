/**
* Created by weiwenjie on 2017-3-23
*/
package gri.engine.test.wwj;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;

import gri.engine.process.KafkaPipeConsumer;
import gri.engine.process.KafkaPipeProducer;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-23
 */
public class multiTest {
	public static void main(String[] args) {
		Thread t1 = new Thread(new RunnerProducer());
	    t1.start();	
	    
	    Thread t2 = new Thread(new RunnerConsumer("c1"));
	    t2.start();	
	    
	    Thread t3 = new Thread(new RunnerConsumer("c2"));
	    t3.start();	
	}


}

class RunnerProducer implements Runnable {
    public void run() {
    	for(int i = 0;i<100;i++){
    		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("mytopic",Integer.toString(i));
    		KafkaPipeProducer.getProducer().send(producerRecord);
    		try{
				Thread.sleep(100); 
			}catch(InterruptedException e){}
		}
    	
    }
}

class RunnerConsumer implements Runnable {
	public String name;
	public int counter=0;
	public RunnerConsumer(String name){
		this.name = name;
	}
    public void run() {
    	/*Consumer<String,String> consumer = KafkaPipeConsumer.createConsumer("mytopic");
    	while(true){
    		counter++;
    		ConsumerRecords<String,String> consumerRecords = consumer.poll(0);
    		for(ConsumerRecord<String,String> consumerRecord : consumerRecords){
    			System.out.println(name+":"+counter+":"+consumerRecord.value());
    		}
    			
    		try{
				Thread.sleep(100); 
			}catch(InterruptedException e){}
    	}*/
    	
    }
}
