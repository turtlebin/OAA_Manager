package gri.manager.ui.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import gri.driver.model.GriDoc;
import gri.driver.model.process.Paragraph;
import gri.driver.model.Section;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.model.QueryViewTreeNode;
import gri.manager.ui.window.MainWindow;
import gri.manager.ui.window.GriDocDialog;
import gri.manager.ui.window.ParagraphDialog;
import gri.manager.ui.window.SavaDataDialog;
import gri.manager.ui.window.SectionDialog;
import gri.manager.ui.window.TextViewer;
import gri.manager.ui.window.UploadDataDialog;
import gri.manager.util.Constant;
import gri.manager.util.RunTimeData;

/**
 * 查询视图目录右键菜单
 * 
 * @author 许诺
 *
 */
public class QueryViewTreeMenu implements MouseListener {
	private Tree tree;
	private TreeViewer treeViewer;
	private Shell mainWindowShell;
	private Item item;

	private Action clear;//
	private Action refresh;//
	private Action saveData;// 保存数据到本地文件
	private Action previewData;// 预览数据
	private Action syncData;// 同步段数据
	private Action refreshPreviewData;// 刷新预览段数据

	private Action property;

	private Action expandAll;// 展开所有节点
	private Action collapseAll;// 收起所有节点

	public QueryViewTreeMenu(Shell parentShell) {
		this.treeViewer = MainWindow.treeViewer_queryView;
		this.tree = this.treeViewer.getTree();
		this.mainWindowShell = parentShell;
		this.createActions();
	}

	// 初始化右键菜单
	private void initPopMenu() {
		if (this.tree.getSelection().length == 0) {
			this.initEmptyPopMenu();
			return;
		}
		this.item = this.tree.getSelection()[0];// 选中的节点
		if (this.item != null) {
			Object obj = this.item.getData();
			if (obj instanceof QueryViewTreeNode) {
				QueryViewTreeNode node = (QueryViewTreeNode) obj;
				if (node.griElement instanceof GriDoc)
					this.initGriDocPoPMenu();
				else if (node.griElement instanceof Section)
					this.initSectionPoPMenu();
				else if (node.griElement instanceof Paragraph)
					this.initParagraphPoPMenu();
			}
		}
	}

	// 空节点右键菜单
	private void initEmptyPopMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.add(this.refresh);
		menuManager.add(this.clear);
		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}

	// 格文档右键菜单
	private void initGriDocPoPMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.add(this.property);
		menuManager.add(this.expandAll);
		menuManager.add(this.collapseAll);
		menuManager.add(this.previewData);
		menuManager.add(this.refresh);
		menuManager.add(this.clear);
		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}

	// 节右键菜单
	private void initSectionPoPMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.add(this.property);
		menuManager.add(this.expandAll);
		menuManager.add(this.collapseAll);
		menuManager.add(this.previewData);
		menuManager.add(this.refresh);
		menuManager.add(this.clear);
		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}

	// 段右键菜单
	private void initParagraphPoPMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.add(this.property);
		menuManager.add(this.expandAll);
		menuManager.add(this.collapseAll);
		menuManager.add(this.saveData);
		menuManager.add(this.previewData);
		menuManager.add(this.syncData);
		menuManager.add(this.refreshPreviewData);
		menuManager.add(this.refresh);
		menuManager.add(this.clear);
		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}

	private void createActions() {
		this.collapseAll = new Action("收起") {
			public void run() {
				treeViewer.collapseToLevel(item.getData(), 30);
			}
		};

		this.expandAll = new Action("展开") {
			public void run() {
				treeViewer.expandToLevel(item.getData(), 30);
			}
		};

		this.refresh = new Action("刷新") {
			public void run() {
				treeViewer.refresh();
			}
		};
		this.clear = new Action("清空结果") {
			public void run() {
				RunTimeData.queryView.clear();
				treeViewer.refresh();
			}
		};

		this.property = new Action("属性") {
			public void run() {
				property();
			}
		};
		this.syncData = new Action("同步数据") {
			public void run() {
				QueryViewTreeNode qnode = ((QueryViewTreeNode) item.getData());
				if (qnode.griElement instanceof Paragraph) {
					Paragraph para = (Paragraph) qnode.griElement;
					qnode.manager.forceSyncData(para.getId());
					System.out.println("已发送同步请求 [paraID:" + para.getId() + "]");
				}
			}
		};
		this.saveData = new Action("保存数据") {
			public void run() {
				QueryViewTreeNode node = (QueryViewTreeNode) item.getData();
				if (node.griElement instanceof Paragraph) {
					new SavaDataDialog((Paragraph) node.griElement, node.manager, mainWindowShell,
							SWT.CLOSE | SWT.APPLICATION_MODAL).open();
				}
			}
		};
		this.previewData = new Action("预览数据") {
			public void run() {
				previewData();
			}
		};
		this.refreshPreviewData = new Action("刷新段预览") {
			public void run() {
				refrehshPreviewData();
			}
		};

	}

	// 鼠标按下
	@Override
	public void mouseDown(MouseEvent e) {
		// 右键
		if (e.button == 3)
			this.initPopMenu();// 弹出菜单
	}

	@Override
	public void mouseUp(MouseEvent e) {
	}

	// 双击
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		previewData();
	}

	private void property() {
		if (this.tree.getSelection().length == 0)
			return;
		TreeItem item = tree.getSelection()[0];
		QueryViewTreeNode queryNode = ((QueryViewTreeNode) item.getData());
		GlobalViewTreeNode node = new GlobalViewTreeNode(queryNode.griElement, null, queryNode.manager);

		if (queryNode.griElement instanceof GriDoc)
			new GriDocDialog(Constant.WindowType_Property, node, mainWindowShell, SWT.CLOSE | SWT.APPLICATION_MODAL)
					.open();
		else if (queryNode.griElement instanceof Section)
			new SectionDialog(Constant.WindowType_Property, node, mainWindowShell, SWT.CLOSE | SWT.APPLICATION_MODAL)
					.open();
		else if (queryNode.griElement instanceof Paragraph)
			new ParagraphDialog(Constant.WindowType_Property, node, mainWindowShell, SWT.CLOSE | SWT.APPLICATION_MODAL)
					.open();
	}

	private void previewData() {
		if (this.tree.getSelection().length == 0)
			return;
		TreeItem item = tree.getSelection()[0];
		final QueryViewTreeNode node = (QueryViewTreeNode) item.getData();
		if (node.griElement instanceof Paragraph) {
			MainWindow.clearContentComposite(); // [1]
			TextViewer textViewer = MainWindow.createContentComposite();// [2]
			MainWindow.reLayoutContentComposite();// [3]important!
			textViewer.loadData((Paragraph) node.griElement, node.manager); // [4]
			textViewer.getButton_download().addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					new SavaDataDialog((Paragraph) node.griElement, node.manager, mainWindowShell,
							SWT.CLOSE | SWT.APPLICATION_MODAL).open();
				}
			});
			textViewer.getButton_upload().addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					new UploadDataDialog((Paragraph) node.griElement, node.manager, mainWindowShell,
							SWT.CLOSE | SWT.APPLICATION_MODAL).open();
				}
			});
		} else if (node.griElement instanceof Section || node.griElement instanceof GriDoc) {
			MainWindow.clearContentComposite(); // [1]
			List<Paragraph> paragraphs = node.manager.getParagraphOfGriElement(node.griElement);
			List<TextViewer> textViewers = new ArrayList<TextViewer>();
			for (int i = 0; i < paragraphs.size(); i++)
				textViewers.add(MainWindow.createContentComposite());// [2]
			MainWindow.reLayoutContentComposite();// [3]important!
			for (int i = 0; i < paragraphs.size(); i++) {
				final Paragraph paragraph = paragraphs.get(i);
				textViewers.get(i).loadData(paragraph, node.manager);// [4]
				textViewers.get(i).getButton_download().addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						new SavaDataDialog(paragraph, node.manager, mainWindowShell, SWT.CLOSE | SWT.APPLICATION_MODAL)
								.open();
					}
				});
				textViewers.get(i).getButton_upload().addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						new UploadDataDialog(paragraph, node.manager, mainWindowShell,
								SWT.CLOSE | SWT.APPLICATION_MODAL).open();
					}
				});
			}
		}
	}

	private void refrehshPreviewData() {
		if (this.tree.getSelection().length == 0)
			return;
		TreeItem item = tree.getSelection()[0];
		QueryViewTreeNode node = (QueryViewTreeNode) item.getData();
		if (node.griElement instanceof Paragraph) {
			node.manager.refreshPreviewParagraphData(node.griElement.getId());
		}
	}

}
