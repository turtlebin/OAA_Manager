package gri.manager.ui.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import gri.driver.manager.GriDocManager;
import gri.driver.model.Connection;
import gri.driver.model.GriDoc;
import gri.driver.model.GriElement;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.util.RunTimeData;

public class UserViewTreeContentProvider implements ITreeContentProvider {
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement.equals("root")) {
			List<GlobalViewTreeNode> children = new ArrayList<GlobalViewTreeNode>();
			for (Connection conn : RunTimeData.globalView)
				children.add(new GlobalViewTreeNode(conn, null, new GriDocManager(conn)));
			return children.toArray(new GlobalViewTreeNode[0]);
		}

		else if (parentElement instanceof GlobalViewTreeNode) {
			GlobalViewTreeNode node = (GlobalViewTreeNode) parentElement;
			Object obj = node.data;
			GriDoc root = node.root;
			GriDocManager manager = node.manager;

			// Connection
			if (obj instanceof Connection) {
				List<GlobalViewTreeNode> children = new ArrayList<GlobalViewTreeNode>();
				for (GriDoc gridoc : manager.getAllGriDoc())
					children.add(new GlobalViewTreeNode(gridoc, gridoc, manager));
				return children.toArray(new GlobalViewTreeNode[0]);
			}

			// GriElement
			else if (obj instanceof GriElement) {
				List<GlobalViewTreeNode> children = new ArrayList<GlobalViewTreeNode>();
				GriElement father = (GriElement) obj;
				for (GriElement child : manager.getChildren(father))
					children.add(new GlobalViewTreeNode(child, root, manager));
				return children.toArray(new GlobalViewTreeNode[0]);
			}
		}
		return new GlobalViewTreeNode[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}
}