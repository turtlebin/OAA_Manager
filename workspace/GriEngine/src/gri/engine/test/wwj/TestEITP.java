/**
* Created by weiwenjie on 2017-3-25
*/
package gri.engine.test.wwj;

import gri.driver.manager.GriDocManager;
import gri.driver.model.Connection;
import gri.driver.util.DriverConstant;
import gri.engine.core.GriEngine;
import gri.engine.service.ViewService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-25
 */
public class TestEITP {

	/** 
	 * @Description
	 * @param @param args
	 * @return void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService threadPool=Executors.newFixedThreadPool(4);
		threadPool.execute(new ServerThread());
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadPool.execute(new EITPThread());
		
		
	}

}

class ServerThread implements Runnable {    
    @Override
    public void run() {
    	GriEngine engine = GriEngine.getGriEngine();
		//engine.start();
    }
}

class EITPThread implements Runnable {    
    @Override
    public void run() {
    	Connection conn=new Connection("新建连接", "EITP://localhost:9020", "root", "root");
		GriDocManager griDocManager = new GriDocManager(conn);
		Object result = griDocManager.ProcessManager().readView("aes1","test");
		String str;
		if (result != null)	str= (String) result;
		else str= "null";
		System.out.println("EITP:"+str);
    }
}

