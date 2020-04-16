package gri.driver.model;

import java.io.Serializable;

public class DataChangeMessage implements Serializable {

	private static final long serialVersionUID = 4113897718155972981L;
	private String dataSourceType;
	private String dataSourcePath;

	public DataChangeMessage(String dataSourceType, String dataSourcePath) {
		super();
		this.dataSourceType = dataSourceType;
		this.dataSourcePath = dataSourcePath;
	}

	public String getDataSourceType() {
		return dataSourceType;
	}

	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public String getDataSourcePath() {
		return dataSourcePath;
	}

	public void setDataSourcePath(String dataSourcePath) {
		this.dataSourcePath = dataSourcePath;
	}

	@Override
	public String toString() {
		return "DataChangeMessage [dataSourceType=" + dataSourceType + ", dataSourcePath=" + dataSourcePath + "]";
	}

}
