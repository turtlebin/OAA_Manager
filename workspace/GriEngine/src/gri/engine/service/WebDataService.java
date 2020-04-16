/**
* Created by weiwenjie on 2017-5-8
*/
package gri.engine.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.SocketException;

import javax.xml.namespace.QName;

//import org.apache.axis2.AxisFault;
//import org.apache.axis2.addressing.EndpointReference;
//import org.apache.axis2.client.Options;
//import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.commons.net.ftp.FTPClient;

/**
 * @Description 
 * Created by weiwenjie on 2017-5-8
 */
public class WebDataService {
	private String url;
	private String method;
	private String[] param;
	
	public WebDataService(String dataSourcePath) {
		String strs[] = dataSourcePath.split("###");
		url = strs[0];
		method = strs[1];
		param = strs[2].split(",");
	}
	
	public boolean download(OutputStream out) {
/*		//  使用RPC方式调用WebService          
	    RPCServiceClient serviceClient =null;
		try {
			serviceClient = new RPCServiceClient();
		} catch (AxisFault e1) {
			e1.printStackTrace();
			return false;
		}  
	    Options options = serviceClient.getOptions();  
	    //  指定调用WebService的URL  
	    EndpointReference targetEPR = new EndpointReference(  
	            "http://"+url);  
	    options.setTo(targetEPR);  
	    //  指定sayHelloToPerson方法的参数值  
	    Object[] opAddEntryArgs = param;  
	    //  指定sayHelloToPerson方法返回值的数据类型的Class对象  
	    Class[] classes = new Class[] {String.class};  
	    //  指定要调用的sayHelloToPerson方法及WSDL文件的命名空间  
	    QName opAddEntry = new QName("http://ws.apache.org/axis2", method);  
	    //  调用sayHelloToPerson方法并输出该方法的返回值  
	    Object result;
		try {
			result = serviceClient.invokeBlocking(opAddEntry, opAddEntryArgs, classes)[0];
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			bw.write(result.toString());
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} */
		return true;
	}
}
