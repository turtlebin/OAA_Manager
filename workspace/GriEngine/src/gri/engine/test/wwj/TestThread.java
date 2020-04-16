/**
* Created by weiwenjie on 2017-3-25
*/
package gri.engine.test.wwj;

import gri.engine.service.RedisContainerService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-25
 */
public class TestThread {

	/** 
	 * @Description
	 * @param @param args
	 * @return void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RedisContainerService viewService = new RedisContainerService("test");
		viewService.set("muti",Integer.toString(0));
		ExecutorService threadPool=Executors.newFixedThreadPool(4);
		for(int i = 0 ;i<10;i++) threadPool.execute(new MyTask(i));
	}
	
	

}

class MyTask implements Runnable {
    private int index;
     
    public MyTask(int index) {
        this.index = index;
    }
     
    @Override
    public void run() {
    	RedisContainerService viewService = new RedisContainerService("test");
        for(int i = 0 ;i<100;i++){
        	/*Jedis jedis= viewService.open();
        	Transaction tx = jedis.multi();
        	tx.set("__test__muti", Integer.toString(Integer.parseInt(tx.get("__test__muti").get())+1));
        	tx.exec();*/
        }
    }
}
