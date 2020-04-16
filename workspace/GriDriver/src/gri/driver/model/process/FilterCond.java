package gri.driver.model.process;

import java.io.Serializable;

/**
 * @Description 
 * Created by weiwenjie on 2016-8-13
 */
public class FilterCond implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String fieldName;
	private Integer operType;
	private Object compare;
	private String operParam;
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Integer getOperType() {
		return operType;
	}
	public void setOperType(Integer operType) {
		this.operType = operType;
	}
	public Object getCompare() {
		return compare;
	}
	public void setCompare(Object compare) {
		this.compare = compare;
	}
	public String getOperParam() {
		return operParam;
	}
	public void setOperParam(String operParam) {
		this.operParam = operParam;
	}
	@Override
	public String toString() {
		return "FilterCond [fieldName=" + fieldName + ", operType=" + operType
				+ ", compare=" + compare + ", operParam=" + operParam + "]";
	}
}
