/**
* Created by weiwenjie on 2017-4-7
*/
package gri.driver.model.process;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 
 * Created by weiwenjie on 2017-4-7
 */
public class View implements Serializable{
	private static final long serialVersionUID = 6056557989254073903L;
	//container的id
	private Integer id;
	//container的id
	private Integer containerId;
	//container的name
	private String containerName;
	//子视图名，用于虚拟视图
	private String subViewName;
	//所属处理器的ID
	private Integer processorId;
	//处理器名
	private String processorName;
	//存储介质
	private String store;
	//数据结构
	private String structure;
	//是否虚拟
	private boolean isVirtual;
	//处理器类名
	private String processorClassName;
	//虚拟视图类名
	private String viewClassName;
	//是否增量
	private Boolean increase;
	//视图对应的处理集合
	private List<ProcessDto1> processes;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public Integer getContainerId() {
		return containerId;
	}
	public void setContainerId(Integer containerId) {
		this.containerId = containerId;
	}
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	public Integer getProcessorId() {
		return processorId;
	}
	public void setProcessorId(Integer processorId) {
		this.processorId = processorId;
	}
	public String getProcessorName() {
		return processorName;
	}
	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getStructure() {
		return structure;
	}
	public void setStructure(String structure) {
		this.structure = structure;
	}
	public boolean isVirtual() {
		return isVirtual;
	}
	public void setVirtual(boolean isVirtual) {
		this.isVirtual = isVirtual;
	}
	
	public String getProcessorClassName() {
		return processorClassName;
	}
	public void setProcessorClassName(String processorClassName) {
		this.processorClassName = processorClassName;
	}
	public String getViewClassName() {
		return viewClassName;
	}
	public void setViewClassName(String viewClassName) {
		this.viewClassName = viewClassName;
	}
	public String getSubViewName() {
		return subViewName;
	}
	public void setSubViewName(String subViewName) {
		this.subViewName = subViewName;
	}
	public Boolean getIncrease() {
		return increase;
	}
	public void setIncrease(Boolean increase) {
		this.increase = increase;
	}
	public List<ProcessDto1> getProcesses() {
		return processes;
	}
	public void setProcesses(List<ProcessDto1> processes) {
		this.processes = processes;
	}
	@Override
	public String toString() {
		return "View [id=" + id + ", containerId=" + containerId
				+ ", containerName=" + containerName + ", subViewName="
				+ subViewName + ", processorId=" + processorId
				+ ", processorName=" + processorName + ", store=" + store
				+ ", structure=" + structure + ", isVirtual=" + isVirtual
				+ ", processorClassName=" + processorClassName
				+ ", viewClassName=" + viewClassName + ", increase=" + increase
				+ ", processes=" + processes + "]";
	}
}

