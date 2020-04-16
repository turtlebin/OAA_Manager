package gri.engine.integrate;

import java.io.Serializable;

import gri.driver.model.process.Paragraph;

public class Paragraph2 extends Paragraph implements Serializable 
{
	private boolean isParagraph2;
	public boolean isParagraph2() {
		return isParagraph2;
	}
	public void setParagraph2(boolean isParagraph2) {
		this.isParagraph2 = isParagraph2;
	}
	public Paragraph2()
	{
		super();
	}
	public Paragraph2(String name, String keywords, String description, String syncTimeType, String syncDirectionType,
			String warmSyncDetail, String dataSourceType, String dataSourcePath,String dataDestType,String dataDestbase,
			String destPort,String destAccount,String destPassword,String destIP)
	{
		super(name,keywords,description,syncTimeType,syncDirectionType,warmSyncDetail,dataSourceType,
				dataSourcePath,dataDestType,dataDestbase,destPort,destAccount,destPassword,destIP);
	}
	public Paragraph2(String name, String keywords, String description, String syncTimeType, String syncDirectionType,
			String warmSyncDetail, String dataSourceType, String dataSourcePath,String dataDestType,String dataDestbase,
			String destPort,String destAccount,String destPassword,String destIP,boolean isParagraph2)
	{
		super(name,keywords,description,syncTimeType,syncDirectionType,warmSyncDetail,dataSourceType,
				dataSourcePath,dataDestType,dataDestbase,destPort,destAccount,destPassword,destIP);
		this.isParagraph2=true;
	}
	
}
