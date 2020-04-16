package gir.engine.monitor;

import gri.engine.util.DateUtil;

public class MessageInitiator {

	public MessageInitiator() {
		// TODO Auto-generated constructor stub
	}
	
	public aMessage InitMessage3(String status,int type,String sourceArray,String destPath,String paragraphName) {
		aMessage message=null;
		message=new Message3();
		message.setSubjectID(20001);
		message.setOp("sendMoniData");
		message.setTime(DateUtil.getCurrentDate());
		message.setDestPath(destPath);
		message.setParagraphName(paragraphName);
		message.setDataSourceList(sourceArray);
		if(type==1) {
			message.setDataName("Paragraph3-State");
			message.setType(type);
			message.setStatus(status);
		}
		else if(type==2) {
			message.setDataName("Paragraph3 Running Process");
			message.setProcess(0);
			message.setUnit("%");
			message.setStatus(status);
			message.setType(type);
		}
//		aMessage message=new Message3();
//		message.setClientID(10001);
//		message.setSubjectID(20001);
//		message.setOp("sendiMessage");
//		message.setTime(DateUtil.getCurrentDate());
//		message.setState("Paragraph3 started");
//		if(process!=0) {
//			message.setProcess(process);
//		}
		return message;
	}
	public aMessage UpdateMessage3(aMessage message,String status,int process) {
		if(process>0) {
			message.setProcess(process);
		}
		message.setStatus(status);
		message.setTime(DateUtil.getCurrentDate());
		return message;
	}
	
	
	public aMessage InitMessage1(String status,int type,String sourceArray,String destPath,String paragraphName) {
		aMessage message=null;
		message=new Message1();
		message.setSubjectID(20001);
		message.setOp("sendMoniData");
		message.setTime(DateUtil.getCurrentDate());
		message.setDestPath(destPath);
		message.setParagraphName(paragraphName);
		message.setDataSourceList(sourceArray);
		if(type==1) {
			message.setDataName("Paragraph1-State");
			message.setType(type);
			message.setStatus(status);
		}
		else if(type==2) {
			message.setDataName("Paragraph1 Running Process");
			message.setProcess(0);
			message.setUnit("%");
			message.setStatus(status);
			message.setType(type);
		}
		return message;
	}
	
	public aMessage UpdateMessage1(aMessage message,String status,int process) {
		if(process>0) {
			message.setProcess(process);
		}
		message.setStatus(status);
		message.setTime(DateUtil.getCurrentDate());
		return message;
	}
	
	public aMessage InitMessage2(String status,int type,String sourceArray,String destPath,String paragraphName) {
		aMessage message=null;
		message=new Message2();
		message.setSubjectID(20001);
		message.setOp("sendMoniData");
		message.setTime(DateUtil.getCurrentDate());
		message.setDestPath(destPath);
		message.setParagraphName(paragraphName);
		message.setDataSourceList(sourceArray);
		if(type==1) {
			message.setDataName("Paragraph2-State");
			message.setType(type);
			message.setStatus(status);
		}
		else if(type==2) {
			message.setDataName("Paragraph2 Running Process");
			message.setProcess(0);
			message.setUnit("%");
			message.setStatus(status);
			message.setType(type);
		}
//		aMessage message=new Message2();
//		message.setClientID(10001);
//		message.setSubjectID(20001);
//		message.setOp("sendiMessage");
//		message.setTime(DateUtil.getCurrentDate());
//		message.setState("Paragraph2 started");
//		if(process!=0) {
//			message.setProcess(process);
//		}
		return message;
	}
	public aMessage UpdateMessage2(aMessage message,String status,int process) {
		if(process>0) {
			message.setProcess(process);
		}
		message.setStatus(status);
		message.setTime(DateUtil.getCurrentDate());
		return message;
	}
	
	public aMessage InitMessageEngine(String status) {
		aMessage message=new MessageEngine();
		//message.setClientID(10001);
		message.setSubjectID(20001);
		message.setOp("sendMoniData");
		message.setType(1);
		message.setTime(DateUtil.getCurrentDate());
		message.setDataName("Engine-State");
		message.setStatus(status);
		return message;
	}
}
