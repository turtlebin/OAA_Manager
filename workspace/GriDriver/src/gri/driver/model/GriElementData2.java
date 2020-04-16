package gri.driver.model;

import java.io.Serializable;

public class GriElementData2 implements Serializable {

	private static final long serialVersionUID = -123346639233022831L;

	private GriDoc belong;
	private GriElement griElement; // Section,Paragraph

	public GriElementData2(GriDoc belong, GriElement griElement) {
		super();
		this.belong = belong;
		this.griElement = griElement;
	}

	public GriDoc getBelong() {
		return belong;
	}

	public void setBelong(GriDoc belong) {
		this.belong = belong;
	}

	public GriElement getGriElement() {
		return griElement;
	}

	public void setGriElement(GriElement griElement) {
		this.griElement = griElement;
	}

	@Override
	public String toString() {
		return "GriElementData2 [belong=" + belong + ", griElement=" + griElement + "]";
	}

}
