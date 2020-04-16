package gri.engine.integrate;

import java.sql.ResultSet;

public class DataSource {
	private String dataSource;
	private ResultSet result;
	
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public ResultSet getResult() {
		return result;
	}
	public void setResult(ResultSet result) {
		this.result = result;
	}
	
	public ResultSet getResultByDataSource(String dataSource)
	{
		if(dataSource.equals(this.dataSource))
		{
			return this.result;
		}
		else
		{
			return null;
		}
	}
	public String getTableName()
	{
		return this.dataSource.split("###")[6];
	}
	public String getSqlType()
	{
		return this.dataSource.split("###")[0];
	}
}
