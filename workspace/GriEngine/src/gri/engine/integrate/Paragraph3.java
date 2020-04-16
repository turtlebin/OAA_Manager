package gri.engine.integrate;

import java.io.Serializable;

import gri.driver.model.process.Paragraph;

public class Paragraph3 extends Paragraph implements Serializable 
{
	private boolean isParagraph3;
	public boolean isParagraph3() {
		return isParagraph3;
	}
	public void setParagraph3(boolean isParagraph3) {
		this.isParagraph3 = isParagraph3;
	}
	public Paragraph3()
	{
		super();
	}
	public Paragraph3(String name, String keywords, String description, String syncTimeType, String syncDirectionType,
			String warmSyncDetail, String dataSourceType, String dataSourcePath,String dataDestType,String dataDestbase,
			String destPort,String destAccount,String destPassword,String destIP)
	{
		super(name,keywords,description,syncTimeType,syncDirectionType,warmSyncDetail,dataSourceType,
				dataSourcePath,dataDestType,dataDestbase,destPort,destAccount,destPassword,destIP);
	}
	public Paragraph3(String name, String keywords, String description, String syncTimeType, String syncDirectionType,
			String warmSyncDetail, String dataSourceType, String dataSourcePath,String dataDestType,String dataDestbase,
			String destPort,String destAccount,String destPassword,String destIP,boolean isParagraph3)
	{
		super(name,keywords,description,syncTimeType,syncDirectionType,warmSyncDetail,dataSourceType,
				dataSourcePath,dataDestType,dataDestbase,destPort,destAccount,destPassword,destIP);
		this.isParagraph3=true;
	}
	
}
