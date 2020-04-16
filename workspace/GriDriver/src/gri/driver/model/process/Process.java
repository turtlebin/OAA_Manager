/**
* Created by weiwenjie on 2017-3-24
*/
package gri.driver.model.process;

import java.io.Serializable;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-24
 */
public class Process implements Serializable {
	private static final long serialVersionUID = 6056557989254073903L;
	private int id;
	private int paragraphId;
	private int containerId;
	private int processorId;
	private String paragraphName;
	private String containerName;
	private String processorName;
	private String config;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	@Override
	public String toString() {
		return "Process [id=" + id + ", paragraphId=" + paragraphId
				+ ", viewId=" + containerId + ", processorId=" + processorId
				+ ", paragraphName=" + paragraphName + ", viewName=" + containerName
				+ ", processorName=" + processorName + ", config=" + config
				+ "]";
	}
	
}
