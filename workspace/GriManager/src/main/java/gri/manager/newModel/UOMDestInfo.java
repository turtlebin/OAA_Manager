package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UOMDestInfo implements DataDestInfo{
    @JSONField(ordinal=1)
    private String brokerList;
    @JSONField(ordinal=2)
    private String topic;
    @JSONField(ordinal=3)
    private String keySerializer;
    @JSONField(ordinal=4)
    private String valueSerializer;
    @JSONField(ordinal=5)
    private String partitionCounts;
    @JSONField(ordinal=6)
    private String replication;
    @JSONField(ordinal=7)
    private String mode;
    public String getDestType(){return "UOM";};

    public static UOMDestInfo.UOMDestInfoBuilder builder(){
        return new UOMDestInfo.UOMDestInfoBuilder();
    }

    public String getBrokerList() {
        return brokerList;
    }

    public void setBrokerList(String brokerList) {
        this.brokerList = brokerList;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public String getPartitionCounts() {
        return partitionCounts;
    }

    public void setPartitionCounts(String partitionCounts) {
        this.partitionCounts = partitionCounts;
    }

    public String getReplication() {
        return replication;
    }

    public void setReplication(String replication) {
        this.replication = replication;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public static final class UOMDestInfoBuilder {
        private String brokerList;
        private String topic;
        private String keySerializer;
        private String valueSerializer;
        private String partitionCounts;
        private String replication;
        private String mode;

        private UOMDestInfoBuilder() {
        }

        public static UOMDestInfoBuilder anUOMDestInfo() {
            return new UOMDestInfoBuilder();
        }

        public UOMDestInfoBuilder brokerList(String brokerList) {
            this.brokerList = brokerList;
            return this;
        }

        public UOMDestInfoBuilder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public UOMDestInfoBuilder keySerializer(String keySerializer) {
            this.keySerializer = keySerializer;
            return this;
        }

        public UOMDestInfoBuilder valueSerializer(String valueSerializer) {
            this.valueSerializer = valueSerializer;
            return this;
        }

        public UOMDestInfoBuilder partitionCounts(String partitionCounts) {
            this.partitionCounts = partitionCounts;
            return this;
        }

        public UOMDestInfoBuilder replication(String replication) {
            this.replication = replication;
            return this;
        }

        public UOMDestInfoBuilder mode(String mode) {
            this.mode = mode;
            return this;
        }

        public UOMDestInfo build() {
            UOMDestInfo uOMDestInfo = new UOMDestInfo();
            uOMDestInfo.setBrokerList(brokerList);
            uOMDestInfo.setTopic(topic);
            uOMDestInfo.setKeySerializer(keySerializer);
            uOMDestInfo.setValueSerializer(valueSerializer);
            uOMDestInfo.setPartitionCounts(partitionCounts);
            uOMDestInfo.setReplication(replication);
            uOMDestInfo.setMode(mode);
            return uOMDestInfo;
        }
    }
}
