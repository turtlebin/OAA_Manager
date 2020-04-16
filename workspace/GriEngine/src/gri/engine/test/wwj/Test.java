/**
* Created by weiwenjie on 2017-3-24
*/
package gri.engine.test.wwj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import net.sf.json.JSONObject;
import gri.driver.manager.GriDocManager;
import gri.driver.model.Connection;
import gri.driver.model.process.Container;
import gri.engine.model.dao.ProcessDao;
import gri.engine.service.GriEngineService;
import gri.engine.service.ProcessService;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-24
 */
public class Test {

	/** 
	 * @Description
	 * @param @param args
	 * @return void
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {			
		File file = new File("D:/wwj/runtime/cache/07b1b8f7-f5f3-480b-b400-a8b54eddc2ee.txt");    		  
		try {    		    		  
	       /* reader = new InputStreamReader(new FileInputStream(file));    		  
	        int tempchar;    		  
	        while ((tempchar = reader.read()) != -1){    
	        	if (((char)tempchar) != '\r'){    		  
	        		System.out.println((char)tempchar);    	  
	        	}    		  
	        }    		  
	        reader.close();  */
			
			StringBuffer sb = new StringBuffer();
			int c;
			Reader reader = new FileReader(file);
			reader = new BufferedReader(reader);
			while ((c = reader.read()) != -1){
				sb.append((char) c);
				System.out.println((char) c);
			}
        } catch (Exception e) {    
    	   e.printStackTrace();    
        }		
	}
}
