package gri.manager.ui.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import gri.driver.manager.GriDocManager;
import gri.driver.model.GriElement;
import gri.manager.model.QueryViewTreeNode;
import gri.manager.util.RunTimeData;

public class QueryViewTreeContentProvider implements ITreeContentProvider {
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement.equals("root")) {
			List<QueryViewTreeNode> children = new ArrayList<QueryViewTreeNode>();
			for (GriElement griElement : RunTimeData.queryView)
				children.add(new QueryViewTreeNode(griElement, RunTimeData.workingGriDocManager));
			return children.toArray(new QueryViewTreeNode[0]);
		} else if (parentElement instanceof QueryViewTreeNode) {
			QueryViewTreeNode node = (QueryViewTreeNode) parentElement;
			GriElement root = node.griElement;
			GriDocManager manager = node.manager;

			List<QueryViewTreeNode> children = new ArrayList<QueryViewTreeNode>();
			for (GriElement child : manager.getChildren(root))
				children.add(new QueryViewTreeNode(child, manager));
			return children.toArray(new QueryViewTreeNode[0]);
		}
		return new QueryViewTreeNode[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}
}
