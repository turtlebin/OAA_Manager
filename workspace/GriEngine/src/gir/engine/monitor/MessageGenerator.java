package gir.engine.monitor;

import net.sf.json.JSONObject;

public class MessageGenerator {

	public MessageGenerator() {
		// TODO Auto-generated constructor stub

	}

	public static String generateData(aMessage message) {
		if (message instanceof Message1) {
			return createMessage1((Message1) message);
		} else if (message instanceof Message2) {
			return createMessage2((Message2) message);
		} else if (message instanceof Message3) {
			return createMessage3((Message3) message);
		} else if (message instanceof MessageEngine) {
			return createEngineMessage((MessageEngine) message);
		}
		return null;
	}

	private static String createMessage3(Message3 message) {
		JSONObject whole = new JSONObject();
		whole.put("op", message.getOp());
		whole.put("subid", message.getSubjectID());
		whole.put("dataName", message.getDataName());
		whole.put("type", message.getType());
		whole.put("status", message.getStatus());
		whole.put("time", message.getTime());
		whole.put("sourcePath", message.getDataSourceList());
		whole.put("destPath", message.getDestPath());
		whole.put("paragraphName", message.getParagraphName());
		if(message.getType()==2) {
			whole.put("unit", message.getUnit());
			whole.put("value", message.getProcess());
		}
//		JSONObject whole = new JSONObject();
//		JSONObject iMessage = new JSONObject();
//		JSONObject content = new JSONObject();
//		content.put("state", message.getState());
//		content.put("process", message.getProcess());
//		if (message.getStage() != null) {
//			content.put("stage", message.getStage());
//		}
//		iMessage.put("content", content);
//		iMessage.put("to", message.getClientID());
//		iMessage.put("from", message.getSubjectID());
//		iMessage.put("time", message.getTime());
//		whole.put("op", message.getOp());
//		whole.put("iMessage", iMessage);
		return whole.toString();
	}

	private static String createMessage1(Message1 message) {
		JSONObject whole = new JSONObject();
		whole.put("op", message.getOp());
		whole.put("subid", message.getSubjectID());
		whole.put("dataName", message.getDataName());
		whole.put("type", message.getType());
		whole.put("status", message.getStatus());
		whole.put("time", message.getTime());
		whole.put("sourcePath", message.getDataSourceList());
		whole.put("destPath", message.getDestPath());
		whole.put("paragraphName", message.getParagraphName());
		if(message.getType()==2) {
			whole.put("unit", message.getUnit());
			whole.put("value", message.getProcess());
		}
		// JSONObject whole=new JSONObject();
		// JSONObject iMessage=new JSONObject();
		// JSONObject content=new JSONObject();
		// content.put("state", message.getState());
		// content.put("process", message.getProcess());
		// iMessage.put("content", content);
		// iMessage.put("to", message.getClientID());
		// iMessage.put("from", message.getSubjectID());
		// iMessage.put("time", message.getTime());
		// whole.put("op", message.getOp());
		// whole.put("iMessage",iMessage);
		return whole.toString();
	}

	private static String createMessage2(Message2 message) {
		JSONObject whole = new JSONObject();
		whole.put("op", message.getOp());
		whole.put("subid", message.getSubjectID());
		whole.put("dataName", message.getDataName());
		whole.put("type", message.getType());
		whole.put("status", message.getStatus());
		whole.put("time", message.getTime());
		whole.put("sourcePath", message.getDataSourceList());
		whole.put("destPath", message.getDestPath());
		whole.put("paragraphName", message.getParagraphName());
		if(message.getType()==2) {
			whole.put("unit", message.getUnit());
			whole.put("value", message.getProcess());
		}
//		JSONObject whole = new JSONObject();
//		JSONObject iMessage = new JSONObject();
//		JSONObject content = new JSONObject();
//		content.put("state", message.getState());
//		content.put("process", message.getProcess());
//		iMessage.put("content", content);
//		iMessage.put("to", message.getClientID());
//		iMessage.put("from", message.getSubjectID());
//		iMessage.put("time", message.getTime());
//		whole.put("op", message.getOp());
//		whole.put("iMessage", iMessage);
		return whole.toString();
	}

	private static String createEngineMessage(MessageEngine message) {
		JSONObject whole = new JSONObject();
		whole.put("op", message.getOp());
		whole.put("subid", message.getSubjectID());
		whole.put("dataName", message.getDataName());
		whole.put("type", message.getType());
		whole.put("status", message.getStatus());
		whole.put("time", message.getTime());
		// JSONObject whole=new JSONObject();
		// JSONObject iMessage=new JSONObject();
		// JSONObject content=new JSONObject();
		// content.put("state", message.getState());
		// iMessage.put("content", content);
		// iMessage.put("to", message.getClientID());
		// iMessage.put("from", message.getSubjectID());
		// iMessage.put("time", message.getTime());
		// whole.put("op", message.getOp());
		// whole.put("iMessage",iMessage);
		return whole.toString();
	}

}
