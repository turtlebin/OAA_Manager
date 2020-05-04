package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class FileSource implements Serializable {
    @JSONField(ordinal=1)
    private String host;
    @JSONField(ordinal=2)
    private String port;
    @JSONField(ordinal=3)
    private String filePath;

    public static FileSource.FileSourceBuilder builder(){
        return new FileSource.FileSourceBuilder();
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static final class FileSourceBuilder {
        private String host;
        private String port;
        private String filePath;

        private FileSourceBuilder() {
        }

        public static FileSourceBuilder aFileSource() {
            return new FileSourceBuilder();
        }

        public FileSourceBuilder host(String host) {
            this.host = host;
            return this;
        }

        public FileSourceBuilder port(String port) {
            this.port = port;
            return this;
        }

        public FileSourceBuilder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public FileSource build() {
            FileSource fileSource = new FileSource();
            fileSource.setHost(host);
            fileSource.setPort(port);
            fileSource.setFilePath(filePath);
            return fileSource;
        }
    }
}
