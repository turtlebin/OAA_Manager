package gri.engine.integrate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Record implements Serializable {
	List <ColumnValue> columns=new ArrayList<ColumnValue>();

	private boolean joined=false;
	
	public boolean isJoined() {
		return joined;
	}

	public void setJoined(boolean joined) {
		this.joined = joined;
	}

	public List<ColumnValue> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnValue> columns) {
		this.columns = columns;
	}
	
	/**
	 * ���������õ��е�Ԫ����
	 * @param columnName
	 * @return
	 */
	public ColumnMeta getColumnMetaByName(String columnName){
		if(columns!=null){
			for(ColumnValue value:columns){
				if(value.getColumnMetaData().getColumnName().equalsIgnoreCase(columnName)){
					return value.getColumnMetaData();
				}
			}
		}
		return null;
	}
	
	/**
	 * ���������õ���ֵ
	 * @param columnName
	 * @return
	 */
	public String getValueByColumnName(String columnName){
		for(ColumnValue value:columns){
			if(value.getColumnMetaData().getColumnName().equalsIgnoreCase(columnName)){
				return (value.getValue()==null)?"null":value.getValue().toString();
			}
		}
		return "null";
	}
	
	public String getValueByColumnNameAndDataSource(String columnName,String dataSource)
	{
		for(ColumnValue value:columns)
		{
			if(value.getColumnMetaData().getColumnName().equalsIgnoreCase(columnName)&&value.getDataSource().equalsIgnoreCase(dataSource))
			{
				return (value.getValue()==null)?"null":value.getValue().toString();
			}
		}
		return "null";
	}
	
	/**
	 * ���������õ�columnValue����
	 * @param columnName
	 * @return
	 */
	public ColumnValue getColumnValueByName(String columnName){
		for(ColumnValue value:columns){
			if(value.getColumnMetaData().getColumnName().equalsIgnoreCase(columnName)){
				return value;
			}
		}
		return null;
	}
	
	
	public List<ColumnMeta> getColumnsMeta(){
		List<ColumnMeta> columnsMeta=new ArrayList<ColumnMeta>();
		if(columns==null) return null;
		for(ColumnValue value:columns){
			columnsMeta.add(value.getColumnMetaData());
		}
		return columnsMeta;
	}
}
