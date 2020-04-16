package gri.driver.model;

import java.io.Serializable;

public class ParagraphDataReadRequest implements Serializable {
	private static final long serialVersionUID = -7625576518487015766L;

	public Integer paragraphID;
	public int positon;
	public int size;

	public ParagraphDataReadRequest(Integer paragraphID, int positon, int size) {
		super();
		this.paragraphID = paragraphID;
		this.positon = positon;
		this.size = size;
	}

	@Override
	public String toString() {
		return "ParagraphDataRequest [paragraphID=" + paragraphID + ", positon=" + positon + ", size=" + size + "]";
	}

}
