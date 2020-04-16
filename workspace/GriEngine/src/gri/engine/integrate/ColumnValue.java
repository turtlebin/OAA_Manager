package gri.engine.integrate;

import java.io.Serializable;

public class ColumnValue implements Serializable {
	ColumnMeta columnMetaData;
	String dataSource;
	Object value;
	
	public ColumnValue(Object value) {
		this.value=value;
	}
	public ColumnValue() {
		
	}
	
	public ColumnMeta getColumnMetaData() {
		return columnMetaData;
	}
	public void setColumnMetaData(ColumnMeta columnMetaData) {
		this.columnMetaData = columnMetaData;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
}
