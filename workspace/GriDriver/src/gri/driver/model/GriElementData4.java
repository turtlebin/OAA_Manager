package gri.driver.model;

import java.io.Serializable;

public class GriElementData4 implements Serializable {

	private static final long serialVersionUID = 3495033941685177321L;

	private GriDoc belong;
	private GriElement father; // GriDoc,Section
	private GriElement child;// Section,Paragraph
	private boolean addTableName;



	public GriElementData4(GriDoc belong, GriElement father, GriElement child,boolean addTableName) {
		super();
		this.belong = belong;
		this.father = father;
		this.child = child;
		this.addTableName=addTableName;
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

	public boolean isAddTableName() {
		return addTableName;
	}

	public void setAddTableName(boolean addTableName) {
		this.addTableName = addTableName;
	}
	@Override
	public String toString() {
		return "GriElementData [belong=" + belong + ", father=" + father + ", child=" + child + "]";
	}

}
