package gri.engine.integrate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gri.engine.dest.Column;

public class DataSourceNeededColumn implements Serializable{

	private String dataSource;
	private List<String> neededColumn=new ArrayList<String>();
	
	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public List<String> getNeededColumn() {
		return neededColumn;
	}

	public void setNeededColumn(List<String> neededColumn) {
		this.neededColumn = neededColumn;
	}
	
	public DataSourceNeededColumn(String dataSource)
	{
		this.dataSource=dataSource;
	}
	
	public void addColumn(String columnName)
	{
		this.neededColumn.add(columnName);
	}
	
	public void addColumns(Collection<Column> columns)
	{
		for(Column column:columns)
		{
			this.neededColumn.add(column.getColumnName());
		}
	}
	
	public void removeColumn(String columnName)
	{
		this.neededColumn.remove(columnName);
	}
	
	public boolean isNull()
	{
		return (this.neededColumn.size()<=0);
	}
	
	public boolean isExisted(String columnName)
	{   
		boolean existed=false;
		if(this.neededColumn.size()==0)
		{
			return false;
		}
		else
		{
			for(String column:neededColumn)
			{
				if(columnName.equals(column))
				{
					existed=true;
					break;
				}
			}
			return existed;
		}
	}
}
