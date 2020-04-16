package gir.engine.monitor;

import java.util.Date;

public class aMessage {
	
    public String op;
	public String url;
	public int SubjectID;
	public int ClientID;
	public String state;
	public int process;
	public String time;
	public Object content;
	public String dataName;
	public String status;
	public String Unit;
	public String paragraphName;
	public String dataSourceList;
	public String destPath;
	
	public String getParagraphName() {
		return paragraphName;
	}
	public void setParagraphName(String paragraphName) {
		this.paragraphName = paragraphName;
	}
	public String getDataSourceList() {
		return dataSourceList;
	}
	public void setDataSourceList(String dataSourceList) {
		this.dataSourceList = dataSourceList;
	}
	public String getDestPath() {
		return destPath;
	}
	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}
	public int type;
	
	public String getDataName() {
		return dataName;
	}
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUnit() {
		return Unit;
	}
	public void setUnit(String unit) {
		Unit = unit;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content=content;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op=op;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSubjectID() {
		return SubjectID;
	}
	public void setSubjectID(int subjectID) {
		SubjectID = subjectID;
	}
	public int getClientID() {
		return ClientID;
	}
	public void setClientID(int clientID) {
		ClientID = clientID;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getProcess() {
		return process;
	}
	public void setProcess(int process) {
		this.process = process;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
