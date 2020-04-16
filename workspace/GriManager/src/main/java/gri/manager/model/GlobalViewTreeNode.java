package gri.manager.model;

import gri.driver.manager.GriDocManager;
import gri.driver.model.GriDoc;

public class GlobalViewTreeNode {

	public Object data;
	public GriDoc root;
	public GriDocManager manager;

	public GlobalViewTreeNode(Object data, GriDoc root, GriDocManager manager) {
		super();
		this.data = data;
		this.root = root;
		this.manager = manager;
	}
}
