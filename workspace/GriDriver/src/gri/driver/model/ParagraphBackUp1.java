package gri.driver.model;

import java.util.Date;

public class ParagraphBackUp1 extends GriElement {

	private static final long serialVersionUID = -4615793730511296247L;

	private String keywords;
	private String description;
	private String syncTimeType;
	private String syncDirectionType;
	private String warmSyncDetail;
	private String dataSourceType;
	private String dataSourcePath;

	private String cacheUUID;// update invalid
	private Integer dataSize;// update invalid
	private boolean last_sync_succeed;// update invalid
	private Date lastSyncTime;// update invalid

	public ParagraphBackUp1(String name, String keywords, String description, String syncTimeType, String syncDirectionType,
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

	public String getCacheUUID() {
		return cacheUUID;
	}

	public void setCacheUUID(String cacheUUID) {
		this.cacheUUID = cacheUUID;
	}

	public Integer getDataSize() {
		return dataSize;
	}

	public void setDataSize(Integer dataSize) {
		this.dataSize = dataSize;
	}

	public boolean isLast_sync_succeed() {
		return last_sync_succeed;
	}

	public void setLast_sync_succeed(boolean last_sync_succeed) {
		this.last_sync_succeed = last_sync_succeed;
	}

	public Date getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(Date lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

	@Override
	public String toString() {
		return "Paragraph [keywords=" + keywords + ", description=" + description + ", syncTimeType=" + syncTimeType
				+ ", syncDirectionType=" + syncDirectionType + ", warmSyncDetail=" + warmSyncDetail
				+ ", dataSourceType=" + dataSourceType + ", dataSourcePath=" + dataSourcePath + ", cacheUUID="
				+ cacheUUID + ", dataSize=" + dataSize + ", last_sync_succeed=" + last_sync_succeed + ", lastSyncTime="
				+ lastSyncTime + ", toString()=" + super.toString() + "]";
	}

}
