package gri.driver.model.process;

import java.io.Serializable;

/**
 * @Description 
 * Created by weiwenjie on 2016-8-13
 */
public class StatsContent implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String fieldName;
	private Integer operType;
	
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
	@Override
	public String toString() {
		return "StatsContent [fieldName=" + fieldName + ", operType="
				+ operType + "]";
	}
	
	public String toString1() {
		String[] statsType =new String[] {"统计数量", "求和", "平均值", "最大值", "最小值"};
		return fieldName+"的"+statsType[operType-1];
	}
}
