package gri.driver.model;

import java.io.Serializable;
import java.util.Arrays;

public class ParagraphIDWithData implements Serializable {

	private static final long serialVersionUID = 5884999328830143172L;
	private String paragrapgID;
	private byte[] data;

	public ParagraphIDWithData(String paragrapgID, byte[] data) {
		super();
		this.paragrapgID = paragrapgID;
		this.data = data;
	}

	public String getParagrapgID() {
		return paragrapgID;
	}

	public void setParagrapgID(String paragrapgID) {
		this.paragrapgID = paragrapgID;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ParagraphIDWithData [paragrapgID=" + paragrapgID + ", data=" + Arrays.toString(data) + "]";
	}

}
