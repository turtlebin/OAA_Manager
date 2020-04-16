package gir.engine.monitor;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gri.engine.core.DataSync;
import gri.engine.util.Constant;
import gri.engine.util.DateUtil;

public class SendHelper {
	public static BlockingQueue<aMessage> messageQueue = new ArrayBlockingQueue<aMessage>(100);
	private static final Logger LOGGER = LoggerFactory.getLogger(SendHelper.class);

	public SendHelper() {
		// TODO Auto-generated constructor stub
	}

	public static synchronized void send(String messageType, aMessage message) throws IOException {
		message.setTime(DateUtil.getCurrentDate());
		String data = MessageGenerator.generateData(message);
		LOGGER.info("集成进行中：{}", data);
		//OKHttpUtil.httpPost("http://localhost:8080/ECPServer/EIOServletMsgEngine", data);
	}
	
//	public static synchronized void sendError(String paragraphName,String errorMessage) {
//		LOGGER.error("发生错误，发生错误的数据段为:{},错误消息为:{}", paragraphName,errorMessage);
//	}
	public static synchronized void sendError(String paragraphName,Exception e) {
		String s=null;
		StackTraceElement[] elements=e.getStackTrace();
		for(int i=elements.length-1;i>=0;i--) {
			s=s==null?e.getStackTrace()[i]+"":s+e.getStackTrace()[i];
			s+="\n";
		}
		s=s.substring(0, s.length()-1);
		LOGGER.error("发生错误，发生错误的数据段为:{},错误类型为:{},错误调用栈顺序为:{}",paragraphName,e,s);
	}
	
	public static synchronized void sendErrorFile(String filePath,Exception e) {
		String s=null;
		StackTraceElement[] elements=e.getStackTrace();
		for(int i=elements.length-1;i>=0;i--) {
			s=s==null?e.getStackTrace()[i]+"":s+e.getStackTrace()[i];
			s+="\n";
		}
		s=s.substring(0, s.length()-1);
		LOGGER.error("文件读取错误，请求读取的文件路径为:{},错误类型为:{},错误调用栈顺序为:{}",filePath,e,s);
	}
	

	// private static void sendMessage2(aMessage message) throws IOException{
	// String data=MessageGenerator.generateData(message);
	// OKHttpUtil.httpPost("http://localhost:8080/ECPServer/EIOServletMsgEngine",data);
	// }
	//
	// private static void sendMessageEngine(String content) throws IOException {
	// aMessage message=new MessageEngine();
	// message.setClientID(10001);
	// message.setSubjectID(20001);
	// if(content.equalsIgnoreCase("started"))
	// {
	// message.setState("started");
	// }
	// else
	// {
	// message.setState("terminated");
	// }
	// message.setOp("sendiMessage");
	// message.setTime(DateUtil.getCurrentDate());
	// String data=MessageGenerator.generateData(message);
	// OKHttpUtil.httpPost("http://localhost:8080/ECPServer/EIOServletMsgEngine",data);
	// //data格式大概為{"op":"sendiMessage","iMessage":{"to":10001,"content":{"state":"terminated"},"time":"2018-07-20
	// 04:32:45","from":20001}}
	// }
}
