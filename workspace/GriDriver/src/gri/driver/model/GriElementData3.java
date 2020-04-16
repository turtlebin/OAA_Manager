package gri.driver.model;

import java.io.Serializable;

public class GriElementData3 implements Serializable {

	private static final long serialVersionUID = 3495033941685177321L;

	private GriDoc belong;
	private GriElement father; // GriDoc,Section
	private GriElement child;// Section,Paragraph

	public GriElementData3(GriDoc belong, GriElement father, GriElement child) {
		super();
		this.belong = belong;
		this.father = father;
		this.child = child;
	}

	public GriDoc getBelong() {
		return belong;
	}

	public void setBelong(GriDoc belong) {
		this.belong = belong;
	}

	public GriElement getFather() {
		return father;
	}

	public void setFather(GriElement father) {
		this.father = father;
	}

	public GriElement getChild() {
		return child;
	}

	public void setChild(GriElement child) {
		this.child = child;
	}

	@Override
	public String toString() {
		return "GriElementData [belong=" + belong + ", father=" + father + ", child=" + child + "]";
	}

}
