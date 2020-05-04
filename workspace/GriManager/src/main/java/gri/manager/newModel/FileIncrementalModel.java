package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class FileIncrementalModel implements Serializable {
    @JSONField(ordinal=1)
    private FileSource sourceFileInfo;
    @JSONField(ordinal=2)
    private FileSource backupFileInfo;
    @JSONField(ordinal=3)
    private String cdc_Method;
    @JSONField(ordinal=4)
    private String operatingMode;
    @JSONField(ordinal=5)
    private boolean usingUOM=false;
    @JSONField(ordinal=6)
    private UOMDestInfo uomInfo;
    @JSONField(ordinal=7)
    private SyncConfig syncConfig;

    public static FileIncrementalModel.FileIncrementalModelBuilder builder(){
        return new FileIncrementalModel.FileIncrementalModelBuilder();
    }

    public FileSource getSourceFileInfo() {
        return sourceFileInfo;
    }

    public void setSourceFileInfo(FileSource sourceFileInfo) {
        this.sourceFileInfo = sourceFileInfo;
    }

    public FileSource getBackupFileInfo() {
        return backupFileInfo;
    }

    public void setBackupFileInfo(FileSource backupFileInfo) {
        this.backupFileInfo = backupFileInfo;
    }

    public String getCDC_Method() {
        return cdc_Method;
    }

    public void setCDC_Method(String CDC_Method) {
        this.cdc_Method = CDC_Method;
    }

    public String getOperatingMode() {
        return operatingMode;
    }

    public void setOperatingMode(String operatingMode) {
        this.operatingMode = operatingMode;
    }

    public boolean isUsingUOM() {
        return usingUOM;
    }

    public void setUsingUOM(boolean usingUOM) {
        this.usingUOM = usingUOM;
    }

    public UOMDestInfo getUomInfo() {
        return uomInfo;
    }

    public void setUomInfo(UOMDestInfo uomInfo) {
        this.uomInfo = uomInfo;
    }

    public SyncConfig getSyncConfig() {
        return syncConfig;
    }

    public void setSyncConfig(SyncConfig syncConfig) {
        this.syncConfig = syncConfig;
    }

    public static final class FileIncrementalModelBuilder {
        private FileSource sourceFileInfo;
        private FileSource backupFileInfo;
        private String CDC_Method;
        private String operatingMode;
        private boolean usingUOM=false;
        private UOMDestInfo uomInfo;
        private SyncConfig syncConfig;

        private FileIncrementalModelBuilder() {
        }

        public static FileIncrementalModelBuilder aFileIncrementalModel() {
            return new FileIncrementalModelBuilder();
        }

        public FileIncrementalModelBuilder sourceFileInfo(FileSource sourceFileInfo) {
            this.sourceFileInfo = sourceFileInfo;
            return this;
        }

        public FileIncrementalModelBuilder backupFileInfo(FileSource backupFileInfo) {
            this.backupFileInfo = backupFileInfo;
            return this;
        }

        public FileIncrementalModelBuilder CDC_Method(String CDC_Method) {
            this.CDC_Method = CDC_Method;
            return this;
        }

        public FileIncrementalModelBuilder operatingMode(String operatingMode) {
            this.operatingMode = operatingMode;
            return this;
        }

        public FileIncrementalModelBuilder usingUOM(boolean usingUOM) {
            this.usingUOM = usingUOM;
            return this;
        }

        public FileIncrementalModelBuilder uomInfo(UOMDestInfo uomInfo) {
            this.uomInfo = uomInfo;
            return this;
        }

        public FileIncrementalModelBuilder syncConfig(SyncConfig syncConfig) {
            this.syncConfig = syncConfig;
            return this;
        }

        public FileIncrementalModel build() {
            FileIncrementalModel fileIncrementalModel = new FileIncrementalModel();
            fileIncrementalModel.setSourceFileInfo(sourceFileInfo);
            fileIncrementalModel.setBackupFileInfo(backupFileInfo);
            fileIncrementalModel.setCDC_Method(CDC_Method);
            fileIncrementalModel.setOperatingMode(operatingMode);
            fileIncrementalModel.setUsingUOM(usingUOM);
            fileIncrementalModel.setUomInfo(uomInfo);
            fileIncrementalModel.setSyncConfig(syncConfig);
            return fileIncrementalModel;
        }
    }
}
