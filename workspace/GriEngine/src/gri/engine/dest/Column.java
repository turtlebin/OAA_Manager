package gri.engine.dest;

import java.io.Serializable;

public class Column implements Serializable {
	String tableName;
	String columnName;
	String columnTypeName;
	int resultSetIndex;
	int columnType;
	String precision;
	String scale;
	String isSigned;
	boolean isNull;
	boolean isAutoIncrement;
	
	
	
	public boolean isAutoIncrement() {
		return isAutoIncrement;
	}
	public void setAutoIncrement(boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnTypeName() {
		return columnTypeName;
	}
	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}
	public int getResultSetIndex() {
		return resultSetIndex;
	}
	public void setResultSetIndex(int resultSetIndex) {
		this.resultSetIndex = resultSetIndex;
	}
	public int getColumnType() {
		return columnType;
	}
	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String string) {
		this.precision = string;
	}
	
	public String getScale() {
		return scale;
	}
	public void setScale(String string) {
		this.scale = string;
	}
	public String isSigned() {
		return isSigned;
	}
	public void setSigned(String isSigned) {
		this.isSigned = isSigned;
	}
	public boolean isNull() {
		return isNull;
	}
	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}
	
	
}
