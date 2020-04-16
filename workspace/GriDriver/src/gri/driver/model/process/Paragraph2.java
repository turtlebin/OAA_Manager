/**
* Created by weiwenjie on 2017-3-24
*/
package gri.driver.model.process;

import gri.driver.model.GriElement;

import java.io.Serializable;
import java.util.Date;


public class Paragraph2 extends GriElement implements Serializable{
	private static final long serialVersionUID = 6056557989254073903L;
	private String keywords;
	private String description;
	private String syncTimeType;
	private String syncDirectionType;
	private String warmSyncDetail;
	private String dataSourceType;
	private String dataSourcePath;
	private Date updateTime;
	private String cache;
	private Boolean lastSyncSucceed;
	private Date lastSyncTime;
	private String datasourceChanged;
	private String cacheChanged;

	private String dataDestType;
	private String dataDestbase;
	private String destPort;
	private String destAccount;
	private String destPassword;
	private String destIP;
	private boolean increase;
	
	private Integer dataSize;

	public Paragraph2(){
		super();
	}
	
	public Paragraph2(String name){ super(name);}
	
	public Paragraph2(String name, String keywords, String description, String syncTimeType, String syncDirectionType,
			String warmSyncDetail, String dataSourceType, String dataSourcePath,String dataDestType,String dataDestbase,
			String destPort,String destAccount,String destPassword,String destIP) {
		super(name);
		this.keywords = keywords;
		this.description = description;
		this.syncTimeType = syncTimeType;
		this.syncDirectionType = syncDirectionType;
		this.warmSyncDetail = warmSyncDetail;
		this.dataSourceType = dataSourceType;
		this.dataSourcePath = dataSourcePath;
		this.dataDestType=dataDestType;
		this.dataDestbase=dataDestbase;
		this.destPort=destPort;
		this.destAccount=destAccount;
		this.destPassword=destPassword;
		this.destIP=destIP;
	}

	public Paragraph2(String name, String keywords, String description, String syncTimeType, String syncDirectionType,
			String warmSyncDetail, String dataSourceType, String dataSourcePath) {
		super(name);
		this.keywords = keywords;
		this.description = description;
		this.syncTimeType = syncTimeType;
		this.syncDirectionType = syncDirectionType;
		this.warmSyncDetail = warmSyncDetail;
		this.dataSourceType = dataSourceType;
		this.dataSourcePath = dataSourcePath;
	}
	
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSyncTimeType() {
		return syncTimeType;
	}

	public void setSyncTimeType(String syncTimeType) {
		this.syncTimeType = syncTimeType;
	}

	public String getSyncDirectionType() {
		return syncDirectionType;
	}

	public void setSyncDirectionType(String syncDirectionType) {
		this.syncDirectionType = syncDirectionType;
	}

	public String getWarmSyncDetail() {
		return warmSyncDetail;
	}

	public void setWarmSyncDetail(String warmSyncDetail) {
		this.warmSyncDetail = warmSyncDetail;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

	public Boolean getLastSyncSucceed() {
		return lastSyncSucceed;
	}

	public void setLastSyncSucceed(Boolean lastSyncSucceed) {
		this.lastSyncSucceed = lastSyncSucceed;
	}

	public Date getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(Date lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

	public String getDatasourceChanged() {
		return datasourceChanged;
	}

	public void setDatasourceChanged(String datasourceChanged) {
		this.datasourceChanged = datasourceChanged;
	}

	public String getCacheChanged() {
		return cacheChanged;
	}

	public void setCacheChanged(String cacheChanged) {
		this.cacheChanged = cacheChanged;
	}

	public boolean isIncrease() {
		return increase;
	}

	public void setIncrease(boolean increase) {
		this.increase = increase;
	}

	public Integer getDataSize() {
		return dataSize;
	}

	public void setDataSize(Integer dataSize) {
		this.dataSize = dataSize;
	}
	public String getDataDestType() {
		return dataDestType;
	}

	public void setDataDestType(String dataDestType) {
		this.dataDestType = dataDestType;
	}

	public String getDataDestbase() {
		return dataDestbase;
	}

	public void setDataDestbase(String dataDestbase) {
		this.dataDestbase = dataDestbase;
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public String getDestAccount() {
		return destAccount;
	}

	public void setDestAccount(String destAccount) {
		this.destAccount = destAccount;
	}

	public String getDestPassword() {
		return destPassword;
	}

	public void setDestPassword(String destPassword) {
		this.destPassword = destPassword;
	}

	public String getDestIP() {
		return destIP;
	}

	public void setDestIP(String destIP) {
		this.destIP = destIP;
	}

	@Override
	public String toString() {
		return "Paragraph [keywords=" + keywords + ", description="
				+ description + ", syncTimeType=" + syncTimeType
				+ ", syncDirectionType=" + syncDirectionType
				+ ", warmSyncDetail=" + warmSyncDetail + ", dataSourceType="
				+ dataSourceType + ", dataSourcePath=" + dataSourcePath
				+ ", updateTime=" + updateTime + ", cache=" + cache
				+ ", lastSyncSucceed=" + lastSyncSucceed + ", lastSyncTime="
				+ lastSyncTime + ", datasourceChanged=" + datasourceChanged
				+ ", cacheChanged=" + cacheChanged + ", increase=" + increase
				+ ", dataSize=" + dataSize + "]";
	}
}
