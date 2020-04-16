/**
* Created by weiwenjie on 2017-5-22
*/
package gri.driver.model.process;

import java.io.Serializable;

/**
 * @Description 
 * Created by weiwenjie on 2017-5-22
 */
public class ProcessDto1 implements Serializable{
	private static final long serialVersionUID = 6056557989254073903L;
	private Integer paragraphId;
	private Boolean increase;
	private String config;
	public Integer getParagraphId() {
		return paragraphId;
	}
	public void setParagraphId(Integer paragraphId) {
		this.paragraphId = paragraphId;
	}
	public Boolean getIncrease() {
		return increase;
	}
	public void setIncrease(Boolean increase) {
		this.increase = increase;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	@Override
	public String toString() {
		return "ProcessDto1 [paragraphId=" + paragraphId + ", increase="
				+ increase + ", config=" + config + "]";
	}
	
	
}
