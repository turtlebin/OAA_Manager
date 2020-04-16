package gri.manager.ui.tree;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import gri.driver.model.Connection;
import gri.driver.model.GriDoc;
import gri.driver.model.GriElement;
import gri.driver.model.process.Paragraph;
import gri.driver.model.Section;

import gri.manager.model.GlobalViewTreeNode;

/**
 * 格文件系统视图-树标签提供器
 * 
 * @author 许诺
 *
 */
public class GlobalViewLabelProvider extends LabelProvider {//获取标签，实际上即为设置图标和文字
	private Image connect;
	private Image connect_offline;
	private Image gridoc;
	private Image section;
	private Image paragraph;

	public GlobalViewLabelProvider() {
		this.connect = new Image(null, this.getClass().getResourceAsStream("/icons/connection16.png"));
		this.connect_offline = new Image(null, this.getClass().getResourceAsStream("/icons/connection_offline16.png"));
		this.gridoc = new Image(null, this.getClass().getResourceAsStream("/icons/gridoc16.png"));
		this.section = new Image(null, this.getClass().getResourceAsStream("/icons/section16.png"));
		this.paragraph = new Image(null, this.getClass().getResourceAsStream("/icons/paragraph16.png"));
	}

	public Image getImage(Object element) {
		if (element instanceof GlobalViewTreeNode) {//返回图标，node。data可以为连接，格文档，节或者段
			GlobalViewTreeNode node = (GlobalViewTreeNode) element;
			if (node.data instanceof Connection) {
				Connection con = (Connection) node.data;
				if (con.isUsing() && con.canConnect())
					return this.connect;
				else
					return this.connect_offline;
			} else if (node.data instanceof GriDoc)
				return this.gridoc;
			else if (node.data instanceof Section)
				return this.section;
			else if (node.data instanceof Paragraph)
				return this.paragraph;
		}
		return null;
	}

	public String getText(Object element) {
		if (element instanceof GlobalViewTreeNode) {
			GlobalViewTreeNode node = (GlobalViewTreeNode) element;
			if (node.data instanceof Connection)
				return ((Connection) node.data).getName();
			else if (node.data instanceof GriElement)
				return ((GriElement) node.data).getName();
		}
		return null;
	}
}