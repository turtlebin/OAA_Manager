package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class InsertMapInfo<T> {
    @JSONField(ordinal=1)
    private String dataSourceTableName;
    @JSONField(ordinal=2)
    private T destInfo;
    @JSONField(ordinal=3)
    private ArrayList<String> insertColumnsMap;
    @JSONField(ordinal=4)
    private ArrayList<String> selectedColumns;

    public static <T> InsertMapInfo.InsertMapInfoBuilder<T> builder(){
        return new InsertMapInfo.InsertMapInfoBuilder<T>();
    }

    public String getDataSourceTableName() {
        return dataSourceTableName;
    }

    public void setDataSourceTableName(String dataSourceTableName) {
        this.dataSourceTableName = dataSourceTableName;
    }

    public T getDestInfo() {
        return destInfo;
    }

    public void setDestInfo(T destInfo) {
        this.destInfo = destInfo;
    }

    public ArrayList<String> getInsertColumnsMap() {
        return insertColumnsMap;
    }

    public void setInsertColumnsMap(ArrayList<String> insertColumnsMap) {
        this.insertColumnsMap = insertColumnsMap;
    }

    public ArrayList<String> getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(ArrayList<String> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public static final class InsertMapInfoBuilder<T> {
        private String dataSource;
        private T destInfo;
        private ArrayList<String> insertColumnsMap;
        private ArrayList<String> selectedColumns;

        private InsertMapInfoBuilder() {
        }

        public static InsertMapInfoBuilder anInsertMapInfo() {
            return new InsertMapInfoBuilder();
        }

        public InsertMapInfoBuilder dataSource(String dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public InsertMapInfoBuilder destInfo(T destInfo) {
            this.destInfo = destInfo;
            return this;
        }

        public InsertMapInfoBuilder insertColumnsMap(ArrayList<String> insertColumnsMap) {
            this.insertColumnsMap = insertColumnsMap;
            return this;
        }

        public InsertMapInfoBuilder selectedColumns(ArrayList<String> selectedColumns) {
            this.selectedColumns = selectedColumns;
            return this;
        }

        public InsertMapInfo build() {
            InsertMapInfo insertMapInfo = new InsertMapInfo();
            insertMapInfo.setDataSourceTableName(dataSource);
            insertMapInfo.setDestInfo(destInfo);
            insertMapInfo.setInsertColumnsMap(insertColumnsMap);
            insertMapInfo.setSelectedColumns(selectedColumns);
            return insertMapInfo;
        }
    }
}
