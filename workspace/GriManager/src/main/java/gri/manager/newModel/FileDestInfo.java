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
public class FileDestInfo implements DataDestInfo{
    @JSONField(ordinal=1)
    private String type="";
    @JSONField(ordinal=2)
    private String host="";
    @JSONField(ordinal=3)
    private String port="";
    @JSONField(ordinal=4)
    private String path="";
    @JSONField(ordinal=5)
    private String mode="";

    public String getDestType(){
        return "File";
    }

    public static FileDestInfo.FileDestInfoBuilder builder(){
        return new FileDestInfo.FileDestInfoBuilder();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public static final class FileDestInfoBuilder {
        private String type="";
        private String host="";
        private String port="";
        private String path="";
        private String mode="";

        private FileDestInfoBuilder() {
        }

        public static FileDestInfoBuilder aFileDestInfo() {
            return new FileDestInfoBuilder();
        }

        public FileDestInfoBuilder type(String type) {
            this.type = type;
            return this;
        }

        public FileDestInfoBuilder host(String host) {
            this.host = host;
            return this;
        }

        public FileDestInfoBuilder port(String port) {
            this.port = port;
            return this;
        }

        public FileDestInfoBuilder path(String path) {
            this.path = path;
            return this;
        }

        public FileDestInfoBuilder mode(String mode) {
            this.mode = mode;
            return this;
        }

        public FileDestInfo build() {
            FileDestInfo fileDestInfo = new FileDestInfo();
            fileDestInfo.setType(type);
            fileDestInfo.setHost(host);
            fileDestInfo.setPort(port);
            fileDestInfo.setPath(path);
            fileDestInfo.setMode(mode);
            return fileDestInfo;
        }
    }
}
