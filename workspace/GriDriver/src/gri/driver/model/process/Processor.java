/**
* Created by weiwenjie on 2017-3-24
*/
package gri.driver.model.process;

import java.io.Serializable;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-24
 */
public class Processor implements Serializable {
	private static final long serialVersionUID = 6056557989254073903L;
	private Integer id;
	private String name;
	private String className;
	private String state;
	private String configStruct;
	private String viewClass;
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
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getConfigStruct() {
		return configStruct;
	}
	public void setConfigStruct(String configStruct) {
		this.configStruct = configStruct;
	}
	public String getViewClass() {
		return viewClass;
	}
	public void setViewClass(String viewClass) {
		this.viewClass = viewClass;
	}
	@Override
	public String toString() {
		return "Processor [id=" + id + ", name=" + name + ", className="
				+ className + ", state=" + state + ", configStruct="
				+ configStruct + ", viewClass=" + viewClass + "]";
	}
}
