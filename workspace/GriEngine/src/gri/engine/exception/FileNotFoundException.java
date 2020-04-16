package gri.engine.exception;

public class FileNotFoundException extends Exception{
	private String filePath;
	private String fileName;
	public FileNotFoundException() {
		super();
	}
	public FileNotFoundException(String msg,String filePath,String fileName) {
		super(msg);
		this.filePath=filePath;
		this.fileName=fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public String getFileName() {
		return fileName;
	}
}
