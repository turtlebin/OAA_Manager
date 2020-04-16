package gri.driver.model.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

/**
 * @Description 
 * Created by weiwenjie on 2016-8-13
 */

public class StatsConfDto1 implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private StatsConf statsConf;
	private int processId;
	private int paragraphId;
	private int containerId;
	private int processorId;
	private String paragraphName;
	private String containerName;
	private String processorName;
	public StatsConf getStatsConf() {
		return statsConf;
	}
	public void setStatsConf(StatsConf statsConf) {
		this.statsConf = statsConf;
	}
	public int getProcessId() {
		return processId;
	}
	public void setProcessId(int processId) {
		this.processId = processId;
	}
	public int getParagraphId() {
		return paragraphId;
	}
	public void setParagraphId(int paragraphId) {
		this.paragraphId = paragraphId;
	}
	public int getContainerId() {
		return containerId;
	}
	public void setContainerId(int containerId) {
		this.containerId = containerId;
	}
	public int getProcessorId() {
		return processorId;
	}
	public void setProcessorId(int processorId) {
		this.processorId = processorId;
	}
	public String getParagraphName() {
		return paragraphName;
	}
	public void setParagraphName(String paragraphName) {
		this.paragraphName = paragraphName;
	}
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	public String getProcessorName() {
		return processorName;
	}
	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}
	@Override
	public String toString() {
		return "StatsConfDto1 [statsConf=" + statsConf + ", processId="
				+ processId + ", paragraphId=" + paragraphId + ", containerId="
				+ containerId + ", processorId=" + processorId
				+ ", paragraphName=" + paragraphName + ", containerName="
				+ containerName + ", processorName=" + processorName + "]";
	}
}
