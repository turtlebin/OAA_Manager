package gri.engine.test;

import java.io.IOException;

import gri.driver.manager.GriDocManager;
import gri.driver.model.Connection;

/**
 * @Description 
 * Created by weiwenjie on 2016-12-29
 */
public class TestService {

	/** 
	 * @Description
	 * @param @param args
	 * @return void
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*Connection conn=new Connection("新建连接", "EITP://localhost:9020", "root", "root");
		GriDocManager griDocManager = new GriDocManager(conn);
		Paragraph paragraph = new Paragraph("");
		paragraph.setId(34);
		byte [] result = griDocManager.readData1(paragraph);
		//System.out.println(new String(result));
		System.out.println(result.length);
		
		FileOutputStream fos = new FileOutputStream("D:\\file.wmv");  		  
        fos.write(result);  
        fos.close();  */
		String text="[存不存,存不存,{'1','2'},{'3','4'}]";
		System.out.println(text.indexOf("{"));
		String text1 = "["+(text.substring(text.indexOf("{"),text.length()));
		System.out.println(text1);
//		Connection conn=new Connection("新建连接", "EITP://localhost:9020", "root", "root");
//		GriDocManager griDocManager = new GriDocManager(conn);
//		System.out.println((String) griDocManager.ProcessManager().readView("aes1","test"));
	}

}
