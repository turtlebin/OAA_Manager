package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;

public class SyncConfig {
    @JSONField(ordinal = 1)
    private String syncType;
    @JSONField(ordinal = 2)
    private String syncMethod;
    @JSONField(ordinal =3 )
    private String syncTimeConfig;

    public static SyncConfig.SyncConfigBuilder builder(){
        return new SyncConfig.SyncConfigBuilder();
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public String getSyncMethod() {
        return syncMethod;
    }

    public void setSyncMethod(String syncMethod) {
        this.syncMethod = syncMethod;
    }

    public String getSyncTimeConfig() {
        return syncTimeConfig;
    }

    public void setSyncTimeConfig(String syncTimeConfig) {
        this.syncTimeConfig = syncTimeConfig;
    }


    public static final class SyncConfigBuilder {
        private String syncType;

        private String syncMethod;
        private String syncTimeConfig;

        private SyncConfigBuilder() {
        }

        public static SyncConfigBuilder aSyncConfig() {
            return new SyncConfigBuilder();
        }

        public SyncConfigBuilder syncType(String syncType) {
            this.syncType = syncType;
            return this;
        }

        public SyncConfigBuilder syncMethod(String syncMethod) {
            this.syncMethod = syncMethod;
            return this;
        }

        public SyncConfigBuilder syncTimeConfig(String syncTimeConfig) {
            this.syncTimeConfig = syncTimeConfig;
            return this;
        }

        public SyncConfig build() {
            SyncConfig syncConfig = new SyncConfig();
            syncConfig.setSyncType(syncType);
            syncConfig.setSyncMethod(syncMethod);
            syncConfig.setSyncTimeConfig(syncTimeConfig);
            return syncConfig;
        }
    }
}
