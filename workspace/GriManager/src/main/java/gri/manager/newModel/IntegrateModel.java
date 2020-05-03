package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class IntegrateModel<T> implements Serializable {
    @JSONField(ordinal=1)
    private ArrayList<String> sourceList;
    @JSONField(ordinal=2)
    private ArrayList<JoinInfo> joinInfos;
    @JSONField(ordinal=3)
    private InsertMapInfo<T> insertMapInfo;
    @JSONField(ordinal=4)
    private SyncConfig syncConfig;

    public SyncConfig getSyncConfig() {
        return syncConfig;
    }

    public void setSyncConfig(SyncConfig syncConfig) {
        this.syncConfig = syncConfig;
    }

    public InsertMapInfo<T> getInsertMapInfo() {
        return insertMapInfo;
    }

    public void setInsertMapInfo(InsertMapInfo<T> insertMapInfo) {
        this.insertMapInfo = insertMapInfo;
    }

    public ArrayList<JoinInfo> getJoinInfos() {
        return joinInfos;
    }

    public void setJoinInfos(ArrayList<JoinInfo> joinInfos) {
        this.joinInfos = joinInfos;
    }

    public ArrayList<String> getSourceList() {
        return sourceList;
    }

    public void setSourceList(ArrayList<String> sourceList) {
        this.sourceList = sourceList;
    }
}
