package gri.driver.model;

import java.io.Serializable;
import java.util.Arrays;

public class ParagraphDataWriteRequest implements Serializable {

	private static final long serialVersionUID = -5836357618282010632L;

	public Integer paragraphID;
	public byte[] bytes;
	public boolean append;

	public ParagraphDataWriteRequest(Integer paragraphID, byte[] bytes, boolean append) {
		super();
		this.paragraphID = paragraphID;
		this.bytes = bytes;
		this.append = append;
	}

	@Override
	public String toString() {
		return "ParagraphDataWriteRequest [paragraphID=" + paragraphID + ", bytes=" + Arrays.toString(bytes)
				+ ", append=" + append + "]";
	}

}
