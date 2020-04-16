package gri.driver.model;

import java.io.Serializable;
import java.util.Date;

public class GriElement implements Serializable {

	private static final long serialVersionUID = 1L;

	public GriElement() {
	}
	
	public GriElement(String name) {
		this.name = name;
	}

	private Integer id;// update invalid
	private String name;
	private Date updateTime;// update invalid
	private Integer sequence;// update invalid

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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return "GriElement [id=" + id + ", name=" + name + ", updateTime=" + updateTime + ", sequence=" + sequence
				+ "]";
	}

}
