package gri.engine.test;

import java.io.IOException;

import gri.engine.core.GriEngine;
import gri.engine.core.ShutDownWork;

public class StartService {
	// 程序入口
	public static void main(String[] args) {
		try {
		GriEngine engine = GriEngine.getGriEngine();
        Runtime.getRuntime().addShutdownHook(new ShutDownWork());  
		engine.start();
		//engine.shutdown();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
