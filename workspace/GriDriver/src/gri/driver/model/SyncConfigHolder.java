package gri.driver.model;

import java.io.Serializable;

import gri.driver.model.process.Paragraph;

public class SyncConfigHolder implements Serializable {

	private String oldSyncType;
	private String oldSyncWarmInfo;
	private Paragraph paragraph;
	
	public String getOldSyncType() {
		return oldSyncType;
	}
	public void setOldSyncType(String oldSyncType) {
		this.oldSyncType = oldSyncType;
	}
	public String getOldSyncWarmInfo() {
		return oldSyncWarmInfo;
	}
	public void setOldSyncWarmInfo(String oldSyncWarmInfo) {
		this.oldSyncWarmInfo = oldSyncWarmInfo;
	}
	public Paragraph getParagrah() {
		return paragraph;
	}
	public void setParagrah(Paragraph paragrah) {
		this.paragraph = paragrah;
	}
	
	public SyncConfigHolder(Paragraph paragraph,String oldSyncType,String oldSyncWarmInfo) {
		this.paragraph=paragraph;
		this.oldSyncType=oldSyncType;
		this.oldSyncWarmInfo=oldSyncWarmInfo;
	}

}
