package gri.manager.ui.tree;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import gri.driver.model.GriDoc;
import gri.driver.model.process.Paragraph;
import gri.driver.model.Section;

import gri.manager.model.QueryViewTreeNode;

/**
 * 查询视图-树标签提供器
 * 
 * @author 许诺
 *
 */
public class QueryViewLabelProvider extends LabelProvider {
	private Image gridoc;
	private Image section;
	private Image paragraph;

	public QueryViewLabelProvider() {
		this.gridoc = new Image(null, this.getClass().getResourceAsStream("/icons/gridoc16.png"));
		this.section = new Image(null, this.getClass().getResourceAsStream("/icons/section16.png"));
		this.paragraph = new Image(null, this.getClass().getResourceAsStream("/icons/paragraph16.png"));
	}

	public Image getImage(Object element) {
		if (element instanceof QueryViewTreeNode) {
			QueryViewTreeNode node = (QueryViewTreeNode) element;
			if (node.griElement instanceof GriDoc)
				return this.gridoc;
			else if (node.griElement instanceof Section)
				return this.section;
			else if (node.griElement instanceof Paragraph)
				return this.paragraph;
		}
		return null;
	}

	public String getText(Object element) {
		if (element instanceof QueryViewTreeNode) {
			QueryViewTreeNode node = (QueryViewTreeNode) element;
			return node.griElement.getName();
		}
		return null;
	}
}