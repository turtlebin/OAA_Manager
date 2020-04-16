package gri.manager.ui.tree;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import gri.driver.model.Connection;
import gri.driver.model.GriDoc;
import gri.driver.model.process.Paragraph;
import gri.driver.model.Section;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.ui.window.MainWindow;
import gri.manager.ui.window.GriDocDialog;
import gri.manager.ui.window.ParagraphDialog;
import gri.manager.ui.window.SectionDialog;
import gri.manager.util.Constant;

/**
 * 格文档树目录右键菜单
 * 
 * @author 许诺
 *
 */
public class UserViewTreeMenu implements MouseListener {
	private Tree tree;
	private TreeViewer treeViewer;
	private Shell mainWindowShell;
	private Item item;

	private Action refreshGlobalView;//
	private Action refreshGriDocData;// 刷新格文档，重新生成格文档html预览文件
	private Action openParagraph;// 系统方式打开段文件

	private Action newGriDoc;
	private Action newSection;
	private Action newParagraph;

	private Action connectionProperty;
	private Action griDocProperty;
	private Action sectionProperty;
	private Action paragraphProperty;

	private Action deleteConnection;
	private Action deleteGriDoc;
	private Action deleteSection;
	private Action deleteParagraph;

	private Action expandAll;// 展开所有节点
	private Action collapseAll;// 收起所有节点

	public UserViewTreeMenu(Shell parentShell) {
		this.treeViewer = MainWindow.treeViewer_globalView;
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
		if (item != null) {
			Object obj = item.getData();
			if (obj instanceof GlobalViewTreeNode) {
				GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
				if (node.data instanceof Connection)
					this.initConnectionPoPMenu();
				else if (node.data instanceof GriDoc)
					this.initGriDocPoPMenu();
				else if (node.data instanceof Section)
					this.initSectionPoPMenu();
				else if (node.data instanceof Paragraph)
					this.initParagraphPoPMenu();
			}
		}
	}

	// 空节点右键菜单
	private void initEmptyPopMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.add(this.refreshGlobalView);
		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}

	// 连接右键菜单
	private void initConnectionPoPMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.add(this.newGriDoc);
		menuManager.add(this.expandAll);
		menuManager.add(this.collapseAll);
		menuManager.add(this.refreshGlobalView);
		menuManager.add(this.deleteConnection);
		menuManager.add(this.connectionProperty);
		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}

	// 格文档右键菜单
	private void initGriDocPoPMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.add(this.refreshGriDocData);
		menuManager.add(this.expandAll);
		menuManager.add(this.collapseAll);
		menuManager.add(this.newSection);
		menuManager.add(this.newParagraph);
		menuManager.add(this.deleteGriDoc);
		menuManager.add(this.griDocProperty);
		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}

	// 节右键菜单
	private void initSectionPoPMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.add(this.newSection);
		menuManager.add(this.newParagraph);
		menuManager.add(this.expandAll);
		menuManager.add(this.collapseAll);
		menuManager.add(this.deleteSection);
		menuManager.add(this.sectionProperty);
		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}

	// 段右键菜单
	private void initParagraphPoPMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.add(this.expandAll);
		menuManager.add(this.collapseAll);
		menuManager.add(this.deleteParagraph);
		menuManager.add(this.paragraphProperty);
		menuManager.add(this.openParagraph);
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

		this.refreshGlobalView = new Action("刷新") {
			public void run() {
				treeViewer.refresh();
			}
		};

		this.refreshGriDocData = new Action("刷新预览缓存") {
			public void run() {
				System.out.println("刷新格文档预览缓存");
				// // 重新生成格文档html预览文件
				// Object obj = item.getData();
				// GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
				// assert (node.data instanceof GriDoc);
				//
				// GriDoc gridoc = (GriDoc) node.data;
				// List<Paragraph> paras = new
				// GriDocService().getAllParagraphOfGriDoc(gridoc);
				//
				// File htmlFile = new File(Constant.GriDoc_workspace +
				// gridoc.getName() + ".html");
				// HtmlService htmlService = new HtmlService(htmlFile, paras);
				//
				// htmlService.clear();// 清空html文件
				// htmlService.writeContent();// 写html文件
			}
		};

		this.connectionProperty = new Action("属性") {
			public void run() {
				// new ConnectionPropertyDialog(((GlobalViewTreeNode)
				// item.getData()), mainWindowShell,
				// SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};

		this.griDocProperty = new Action("属性") {
			public void run() {
				new GriDocDialog(Constant.WindowType_Edit, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
						SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};
		this.sectionProperty = new Action("属性") {
			public void run() {
			}
		};
		this.paragraphProperty = new Action("属性") {
			public void run() {
				new ParagraphDialog(Constant.WindowType_Edit, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
						SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};
		this.openParagraph = new Action("打开") {
			public void run() {
				System.out.println("打开段文件内容");
				// GlobalViewTreeNode node = ((GlobalViewTreeNode)
				// item.getData());
				// if (node.data instanceof Paragraph) {
				// Path filePath = ((Paragraph) node.data).filePath;
				// if (filePath == null) {
				// MessageBox m = new MessageBox(mainWindowShell, SWT.OK);
				// m.setMessage("没有绑定数据文件！");
				// m.open();
				// return;
				// }
				// String filePathStr =
				// FilePathHelper.formatPath(filePath.toUri().getPath());
				// try {
				// Desktop.getDesktop().open(new File(filePathStr));
				// System.out.println("打开段文件成功！");
				// } catch (Exception e1) {
				// System.out.println("打开段文件失败:" + e1.toString());
				// System.out.println("段绑定文件路径：" + filePath);
				// e1.printStackTrace();
				// MessageBox m = new MessageBox(mainWindowShell, SWT.OK);
				// m.setMessage("绑定数据文件失效！");
				// m.open();
				// }
				// }
			}
		};

		this.newGriDoc = new Action("新建格文档") {
			public void run() {
				new GriDocDialog(Constant.WindowType_Add, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
						SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};
		this.newSection = new Action("新建节") {
			public void run() {
				new SectionDialog(Constant.WindowType_Add, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
						SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};
		this.newParagraph = new Action("新建段") {
			public void run() {
				new ParagraphDialog(Constant.WindowType_Add, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
						SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};
		this.deleteConnection = new Action("删除") {
			public void run() {
				if (item != null) {
					MainWindow.treeViewer_globalView.refresh();
					MainWindow.treeViewer_globalView.expandAll();
				}
			}
		};
		this.deleteGriDoc = new Action("删除") {
			public void run() {
				if (item != null) {
					// Object obj = item.getData();
					// assert (obj instanceof GlobalViewTreeNode);
					// GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
					// assert (node.data instanceof GriDoc);
					// GriDoc gridoc = (GriDoc) node.data;
					//
					// MessageBox messageBox = new MessageBox(mainWindowShell,
					// SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
					// messageBox.setText("提示");
					// messageBox.setMessage("确定删除格文档：" + gridoc.getName() +
					// "？");
					// if (messageBox.open() == SWT.YES) {
					// // Environment.shareInfo.deleteRelationByGriDoc(gridoc);
					// //Environment.LocalGriDocFileSystem.namesystem.removeGridoc(gridoc);
					MainWindow.treeViewer_globalView.refresh();
					MainWindow.treeViewer_globalView.expandAll();
				}
				// }
			}
		};
		this.deleteSection = new Action("删除") {
			public void run() {
				if (item != null) {
					MainWindow.treeViewer_globalView.refresh();
					MainWindow.treeViewer_globalView.expandAll();
				}
			}
		};
		this.deleteParagraph = new Action("删除") {
			public void run() {
				if (item != null) {
					MainWindow.treeViewer_globalView.refresh();
					MainWindow.treeViewer_globalView.expandAll();
				}
			}
		};

	}

	// 双击打开段节点绑定的磁盘文件
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		System.out.println("小窗口打开段数据内容");
		if (tree.getSelection().length == 0)
			return;
		TreeItem item = tree.getSelection()[0];
		if (item != null) {
			Object obj = item.getData();
			if (obj instanceof GlobalViewTreeNode) {
				GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
				if (node.data instanceof Paragraph) {
					MessageBox m = new MessageBox(mainWindowShell, SWT.OK);
					m.setMessage("没有绑定数据文件！");
					m.open();
				}
			}
		}
	}

	// String filePathStr =
	// FilePathHelper.formatPath(filePath.toUri().getPath());
	// String dataFileExtendName =
	// FilePathHelper.getFileTypeFromName(filePath.getName());
	// // 纯文本
	// if (dataFileExtendName.toLowerCase().equals("txt")) {
	// new TextViewerDialog(filePathStr, mainWindowShell, SWT.CLOSE |
	// SWT.MAX | SWT.APPLICATION_MODAL)
	// .open();
	// }
	// // 办公文档
	// else if (dataFileExtendName.toLowerCase().equals("docx")
	// || dataFileExtendName.toLowerCase().equals("doc")
	// || dataFileExtendName.toLowerCase().equals("pptx")
	// || dataFileExtendName.toLowerCase().equals("ppt")
	// || dataFileExtendName.toLowerCase().equals("xlsx")
	// || dataFileExtendName.toLowerCase().equals("xls")) {
	// OfficeDocumentViewer v = new OfficeDocumentViewer(filePathStr,
	// mainWindowShell);
	// v.setBlockOnOpen(true);
	// v.open();
	// }
	// // PDF
	// else if (dataFileExtendName.toLowerCase().equals("pdf")) {
	// new PdfViewer(filePathStr).open();
	// }
	// // 视频
	// else if (dataFileExtendName.toLowerCase().equals("rmvb")
	// || dataFileExtendName.toLowerCase().equals("avi")
	// || dataFileExtendName.toLowerCase().equals("mp4")
	// || dataFileExtendName.toLowerCase().equals("wmv")
	// || dataFileExtendName.toLowerCase().equals("flv")
	// || dataFileExtendName.toLowerCase().equals("mpg")) {
	// System.out.println("视频!");
	// try {
	// JMFSample sp = new JMFSample(filePathStr);
	// sp.play();
	// } catch (Exception ex) {
	// System.out.println("视频播放失败！");
	// }
	// System.out.println("视频-END!");
	// }
	// // 图片
	// else if (dataFileExtendName.toLowerCase().equals("jpg")
	// || dataFileExtendName.toLowerCase().equals("png")
	// || dataFileExtendName.toLowerCase().equals("jpeg")
	// || dataFileExtendName.toLowerCase().equals("bmp")
	// || dataFileExtendName.toLowerCase().equals("gif")) {
	// new ImageViewerDialog(filePathStr, mainWindowShell, SWT.CLOSE |
	// SWT.MAX | SWT.APPLICATION_MODAL)
	// .open();
	// }
	// // 其他
	// else {
	// System.out.println("其他");
	// }
	// // }
	// } else if (node.data instanceof GriDoc) {
	// GriDoc gridoc = (GriDoc) node.data;
	// MainWindow.scrolledComposite_content.setContent(MainWindow.browser_gridoc);
	// File file = new File(Constant.GriDoc_workspace + gridoc.getName() +
	// ".html");
	// String[] headers = { "Accept: */*",
	// "Content-Type:text/html;charset=utf-8", };
	// if (file.exists()) {
	// MainWindow.browser_gridoc.setUrl(file.getPath(), "", headers);
	// } else
	// MainWindow.browser_gridoc.setText("<html><h3>格文档预览文件不存在，请刷新格文档重新生成！<h3></html>");
	// }
	// }
	// }

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
}
