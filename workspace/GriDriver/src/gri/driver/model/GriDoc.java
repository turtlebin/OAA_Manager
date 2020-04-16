package gri.driver.model;

public class GriDoc extends GriElement {

	private static final long serialVersionUID = 5923397080802659317L;

	private boolean readOnly; // update invalid

	public GriDoc(String name) {
		super(name);
	}

	public boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public String toString() {
		return "GriDoc [readOnly=" + readOnly + ", toString()=" + super.toString() + "]";
	}

}
