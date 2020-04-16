package gri.engine.core;

import java.io.FileWriter;  
import java.io.IOException;  
import java.util.Date;

import gir.engine.monitor.MessageEngine;
import gir.engine.monitor.MessageInitiator;
import gir.engine.monitor.SendHelper;  

public class ShutDownWork extends Thread {  
    @Override  
    public void run() {  
    	try {
			MessageInitiator init=new MessageInitiator();
			MessageEngine message=(MessageEngine)init.InitMessageEngine("Engine terminated");
			SendHelper.send("MessageEngine", message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            System.out.println("Im going to end.");  
    }  
      
    /**************************************************** 
     * 这是程序的入口，仅为演示，方法中的代码无关紧要 
    *****************************************************/  
    public static void main(String[] args) {  
        //添加程序结束监听  
        Runtime.getRuntime().addShutdownHook(new ShutDownWork());  
        try {
			FileWriter fw = new FileWriter("E://test.log");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
        System.exit(0);  
        long s = System.currentTimeMillis();  
        for (int i = 0; i < 1000000000; i++) {  
            try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 在这里增添您需要处理代码  
        }  
        long se = System.currentTimeMillis();  
        System.out.println(se - s);  
    }  
}  