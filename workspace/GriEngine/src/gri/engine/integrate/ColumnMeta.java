/**
 * 
 */
package gri.engine.integrate;

import java.io.Serializable;

/**
 * @author wxyyuppie
 *
 */
public class ColumnMeta implements Serializable{
	String tableName;
	String columnName;
	String columnTypeName;
	int resultSetIndex;
	int columnType;
	long precision;
	int scale;
	boolean isSigned;
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
	public long getPrecision() {
		return precision;
	}
	public void setPrecision(long precision) {
		this.precision = precision;
	}
	
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public boolean isSigned() {
		return isSigned;
	}
	public void setSigned(boolean isSigned) {
		this.isSigned = isSigned;
	}
	public boolean isNull() {
		return isNull;
	}
	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}
	
	
}
