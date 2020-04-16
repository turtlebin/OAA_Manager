package gri.engine.exception;

public class RecordsNullException extends Exception{
	private String filePath;
	public RecordsNullException() {
		super();
	}
	public RecordsNullException(String msg,String filePath) {
		super(msg);
		this.filePath=filePath;
	}
	public String getFilePath() {
		return filePath;
	}
}
