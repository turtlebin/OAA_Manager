package gri.driver.model;

import java.io.Serializable;
import java.util.Arrays;

public class ParagraphDataReadResponse implements Serializable {
	private static final long serialVersionUID = -7518771938492585559L;

	public int size; // byte size
	public byte[] bytes;

	public ParagraphDataReadResponse(byte[] bytes, int size) {
		super();
		this.size = size;
		this.bytes = bytes;
	}

	@Override
	public String toString() {
		return "ParagraphDataResponse [size=" + size + ", bytes=" + Arrays.toString(bytes) + "]";
	}

}
