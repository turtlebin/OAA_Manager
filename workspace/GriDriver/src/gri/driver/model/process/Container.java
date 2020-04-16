/**
* Created by weiwenjie on 2017-3-25
*/
package gri.driver.model.process;

import java.io.Serializable;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-25
 */
public class Container implements Serializable {
	private static final long serialVersionUID = 6056557989254073903L;
	
	private Integer id;
	private String name;
	private Integer userId;
	private String userName;
	private Integer processorId;
	private String processorName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	@Override
	public String toString() {
		return "Container [id=" + id + ", name=" + name + ", userId=" + userId
				+ ", userName=" + userName + ", processorId=" + processorId
				+ ", processorName=" + processorName + "]";
	}
}
