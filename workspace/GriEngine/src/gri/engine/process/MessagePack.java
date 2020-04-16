/**
* Created by weiwenjie on 2017-3-23
*/
package gri.engine.process;

import java.io.Serializable;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-23
 */
public class MessagePack implements Serializable{
	private String data;
	private String config;
	private String containerName;
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	@Override
	public String toString() {
		return "MessagePack [data=" + data + ", config=" + config
				+ ", containerName=" + containerName + "]";
	}
	
	
}
