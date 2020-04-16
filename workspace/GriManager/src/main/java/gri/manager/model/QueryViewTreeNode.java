package gri.manager.model;

import gri.driver.manager.GriDocManager;
import gri.driver.model.GriElement;

public class QueryViewTreeNode {

	public GriElement griElement;
	public GriDocManager manager;

	public QueryViewTreeNode(GriElement griElement, GriDocManager manager) {
		super();
		this.griElement = griElement;
		this.manager = manager;
	}

}
