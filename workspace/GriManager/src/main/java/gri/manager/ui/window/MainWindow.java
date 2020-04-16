package gri.manager.ui.window;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.custom.SashForm;

import gri.driver.model.Connection;
import gri.driver.model.GriDoc;
import gri.driver.model.Section;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.ui.tree.GlobalViewLabelProvider;
import gri.manager.ui.tree.GlobalViewTreeContentProvider;
import gri.manager.ui.tree.GlobalViewTreeMenu;
import gri.manager.ui.tree.QueryViewLabelProvider;
import gri.manager.ui.tree.QueryViewTreeContentProvider;
import gri.manager.ui.tree.QueryViewTreeMenu;
import gri.manager.ui.tree.UserViewLabelProvider;
import gri.manager.ui.tree.UserViewTreeContentProvider;
import gri.manager.ui.tree.UserViewTreeMenu;
import gri.manager.util.Constant;

import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import java.io.IOException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 程序主窗口
 * 
 * @author 许诺
 *
 */
public class MainWindow {

	public static Shell shell;// 主窗口
	public static TreeViewer treeViewer_globalView;// 全局视图
	public static TreeViewer treeViewer_userView;// 用户视图
	public static TreeViewer treeViewer_queryView;// 查询视图
	// public static Browser browser_gridoc;// 格文档浏览器
	private static ScrolledComposite scrolledComposite_content;// 内容面板
	private static Composite composite_content;// 内容面板
	public static TabFolder tabFolder_view;

	public static void main(String[] args) {
		MainWindow window = new MainWindow();
		window.open();

		System.out.println("bye~");
	}

	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void createContents() {
		shell = new Shell();
		shell.setImage(new Image(Display.getCurrent(), this.getClass().getResourceAsStream("/icons/gridoc24.png")));
		shell.setSize(686, 608);
		shell.setText("GriDoc文档可视化管理器");

		shell.setLocation(Display.getCurrent().getClientArea().width / 2 - shell.getShell().getSize().x / 2,
				Display.getCurrent().getClientArea().height / 2 - shell.getSize().y / 2);// 主窗口居中

		GridLayout gl_shell = new GridLayout(10, true);
		shell.setLayout(gl_shell);

		shell.setMinimumSize(730, 450);

		// 菜单栏
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem menuItemFile = new MenuItem(menu, SWT.CASCADE);
		menuItemFile.setText("文件");

		Menu menu_1 = new Menu(menuItemFile);
		menuItemFile.setMenu(menu_1);

		MenuItem menuItemNew = new MenuItem(menu_1, SWT.CASCADE);
		menuItemNew.setText("新建");

		Menu menu_3 = new Menu(menuItemNew);
		menuItemNew.setMenu(menu_3);

		MenuItem menuItemNewGriDoc = new MenuItem(menu_3, SWT.NONE);
		menuItemNewGriDoc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newGriDoc();
			}
		});
		menuItemNewGriDoc.setText("格文档");

		MenuItem menuItemNewSection = new MenuItem(menu_3, SWT.NONE);
		menuItemNewSection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newSection();
			}
		});
		menuItemNewSection.setText("节");

		MenuItem menuItemNewParagraph = new MenuItem(menu_3, SWT.NONE);
		menuItemNewParagraph.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newParagraph();
			}
		});
		menuItemNewParagraph.setText("段");

		MenuItem menuItemExit = new MenuItem(menu_1, SWT.NONE);
		menuItemExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		menuItemExit.setText("退出");

		MenuItem menuItemEdit = new MenuItem(menu, SWT.CASCADE);
		menuItemEdit.setText("编辑");

		Menu menu_2 = new Menu(menuItemEdit);
		menuItemEdit.setMenu(menu_2);

//		MenuItem menuItemDelete = new MenuItem(menu_2, SWT.NONE);
//		menuItemDelete.setText("删除");
//
//		MenuItem menuItemTool = new MenuItem(menu, SWT.CASCADE);
//		menuItemTool.setText("工具");

//		Menu menu_5 = new Menu(menuItemTool);
//		menuItemTool.setMenu(menu_5);

//		MenuItem menuItem_clear = new MenuItem(menu_5, SWT.NONE);
//		menuItem_clear.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				clearUnreferenceGriElement();
//			}
//		});
//		menuItem_clear.setText("清理");

		MenuItem menuItemHelp = new MenuItem(menu, SWT.CASCADE);
		menuItemHelp.setText("帮助");

		Menu menu_4 = new Menu(menuItemHelp);
		menuItemHelp.setMenu(menu_4);

		MenuItem menuItemAbout = new MenuItem(menu_4, SWT.NONE);
		menuItemAbout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				about();
			}
		});
		menuItemAbout.setText("关于");

		MenuItem menuItemContact = new MenuItem(menu_4, SWT.NONE);
		menuItemContact.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				contact();
			}
		});
		menuItemContact.setText("联系我们");

		// 工具栏
		ToolBar toolBar = new ToolBar(shell, SWT.NONE);
		GridData gd_toolBar = new GridData(SWT.LEFT, SWT.CENTER, true, false, 10, 1);
		gd_toolBar.widthHint = 600;
		toolBar.setLayoutData(gd_toolBar);

		ToolItem toolItem_newConnection = new ToolItem(toolBar, SWT.NONE);
		toolItem_newConnection.setWidth(60);
		toolItem_newConnection.setText(" 连接 ");
		toolItem_newConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newConnection();
			}
		});
		toolItem_newConnection.setToolTipText("新建连接");
		toolItem_newConnection.setImage(new Image(toolItem_newConnection.getDisplay(),
				this.getClass().getResourceAsStream("/icons/connection24.png")));

		ToolItem toolItem_newGridoc = new ToolItem(toolBar, SWT.NONE);
		toolItem_newGridoc.setWidth(60);
		toolItem_newGridoc.setText("格文档");
		toolItem_newGridoc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newGriDoc();
			}
		});
		toolItem_newGridoc.setToolTipText("新建格文档");
		toolItem_newGridoc.setImage(
				new Image(toolItem_newGridoc.getDisplay(), this.getClass().getResourceAsStream("/icons/gridoc24.png")));

		ToolItem toolItem_newSection = new ToolItem(toolBar, SWT.NONE);
		toolItem_newSection.setWidth(60);
		toolItem_newSection.setText("  节  ");
		toolItem_newSection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newSection();
			}
		});
		toolItem_newSection.setToolTipText("新建节");
		toolItem_newSection.setImage(new Image(toolItem_newSection.getDisplay(),
				this.getClass().getResourceAsStream("/icons/section24.png")));

		ToolItem toolItem_newParagraph = new ToolItem(toolBar, SWT.NONE);
		toolItem_newParagraph.setWidth(60);
		toolItem_newParagraph.setText("  段  ");
		toolItem_newParagraph.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newParagraph();
			}
		});
		toolItem_newParagraph.setToolTipText("新建段");
		toolItem_newParagraph.setImage(new Image(toolItem_newParagraph.getDisplay(),
				this.getClass().getResourceAsStream("/icons/paragraph24.png")));

//		ToolItem toolItem_query = new ToolItem(toolBar, SWT.NONE);
//		toolItem_query.setWidth(60);
//		toolItem_query.setText(" 查询 ");
//		toolItem_query.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				query();
//			}
//		});
//		toolItem_query.setToolTipText("格文档数据查询");
//		toolItem_query.setImage(
//				new Image(toolItem_query.getDisplay(), this.getClass().getResourceAsStream("/icons/query24.png")));
//
//		ToolItem toolItem_processor = new ToolItem(toolBar, SWT.NONE);
//		toolItem_processor.setWidth(60);
//		toolItem_processor.setText("管道 ");
//		toolItem_processor.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				processor();
//			}
//		});
//		toolItem_processor.setToolTipText("管道管理");
//		toolItem_processor.setImage(new Image(toolItem_newConnection.getDisplay(),
//				this.getClass().getResourceAsStream("/icons/processor1.png")));
//		
//		ToolItem toolItem_container = new ToolItem(toolBar, SWT.NONE);
//		toolItem_container.setWidth(60);
//		toolItem_container.setText(" 空间");
//		toolItem_container.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				container();
//			}
//		});
//		toolItem_container.setToolTipText("容器空间管理");
//		toolItem_container.setImage(new Image(toolItem_newConnection.getDisplay(),
//				this.getClass().getResourceAsStream("/icons/container1.png")));
//		
//		ToolItem toolItem_view = new ToolItem(toolBar, SWT.NONE);
//		toolItem_view.setWidth(60);
//		toolItem_view.setText(" 视图");
//		toolItem_view.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				view();
//			}
//		});
//		toolItem_view.setToolTipText("视图管理");
//		toolItem_view.setImage(new Image(toolItem_newConnection.getDisplay(),
//				this.getClass().getResourceAsStream("/icons/state24.png")));
		
		ToolItem toolItem_user = new ToolItem(toolBar, SWT.NONE);
		toolItem_user.setWidth(60);
		toolItem_user.setText(" 用户 ");
		toolItem_user.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				user();
			}
		});
		
		toolItem_user.setToolTipText("用户管理");
		toolItem_user.setImage(
				new Image(toolItem_user.getDisplay(), this.getClass().getResourceAsStream("/icons/user24.png")));

		ToolItem toolItem_about = new ToolItem(toolBar, SWT.NONE);
		toolItem_about.setWidth(60);
		toolItem_about.setText(" 关于 ");
		toolItem_about.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				about();
			}
		});
		toolItem_about.setToolTipText("关于");
		toolItem_about.setImage(
				new Image(toolItem_about.getDisplay(), this.getClass().getResourceAsStream("/icons/about24.png")));

		ToolItem toolItem_contact = new ToolItem(toolBar, SWT.NONE);
		toolItem_contact.setWidth(60);
		toolItem_contact.setText(" 联系 ");
		toolItem_contact.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				contact();
			}
		});
		toolItem_contact.setToolTipText("联系我们");
		toolItem_contact.setImage(
				new Image(toolItem_contact.getDisplay(), this.getClass().getResourceAsStream("/icons/contact24.png")));

		// 左右二分栏
		final SashForm sashForm = new SashForm(shell, SWT.NONE);
		GridData gd_sashForm = new GridData(SWT.FILL, SWT.CENTER, true, true, 10, 1);
		gd_sashForm.heightHint = 1000;
		gd_sashForm.widthHint = 1800;
		sashForm.setLayoutData(gd_sashForm);

		// 多视图选项卡
		tabFolder_view = new TabFolder(sashForm, SWT.NONE);

		// 全局视图
		TabItem tabItem_globalView = new TabItem(tabFolder_view, SWT.NONE);
		tabItem_globalView.setText("全局视图");
		ScrolledComposite scrolledComposite_globalView = new ScrolledComposite(tabFolder_view,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tabItem_globalView.setControl(scrolledComposite_globalView);
		scrolledComposite_globalView.setExpandHorizontal(true);
		scrolledComposite_globalView.setExpandVertical(true);
		treeViewer_globalView = new TreeViewer(scrolledComposite_globalView, SWT.BORDER);
		Tree tree_globalView = treeViewer_globalView.getTree();
		treeViewer_globalView.setLabelProvider(new GlobalViewLabelProvider());// 标签
		treeViewer_globalView.setContentProvider(new GlobalViewTreeContentProvider());// 数据
		tree_globalView.addMouseListener(new GlobalViewTreeMenu(shell));// 右键
		treeViewer_globalView.setInput(new String("root"));// 初始数据
		scrolledComposite_globalView.setContent(tree_globalView);
		scrolledComposite_globalView.setMinSize(tree_globalView.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// 用户视图
		TabItem tabItem_userView = new TabItem(tabFolder_view, SWT.NONE);
		tabItem_userView.setText("用户视图");
		ScrolledComposite scrolledComposite_userView = new ScrolledComposite(tabFolder_view,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_userView.setExpandVertical(true);
		scrolledComposite_userView.setExpandHorizontal(true);
		tabItem_userView.setControl(scrolledComposite_userView);
		treeViewer_userView = new TreeViewer(scrolledComposite_userView, SWT.BORDER);
		Tree tree_userView = treeViewer_userView.getTree();
		treeViewer_userView.setLabelProvider(new UserViewLabelProvider());// 标签
		treeViewer_userView.setContentProvider(new UserViewTreeContentProvider());// 数据
		tree_userView.addMouseListener(new UserViewTreeMenu(shell));// 右键
		treeViewer_userView.setInput(new String("root"));// 初始数据
		scrolledComposite_userView.setContent(tree_userView);
		scrolledComposite_userView.setMinSize(tree_userView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		

		// 查询视图
		TabItem tabItem_queryView = new TabItem(tabFolder_view, SWT.NONE);
		tabItem_queryView.setText("查询视图");
		ScrolledComposite scrolledComposite_queryView = new ScrolledComposite(tabFolder_view,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tabItem_queryView.setControl(scrolledComposite_queryView);
		scrolledComposite_queryView.setExpandHorizontal(true);
		scrolledComposite_queryView.setExpandVertical(true);
		treeViewer_queryView = new TreeViewer(scrolledComposite_queryView, SWT.BORDER);
		Tree tree_queryView = treeViewer_queryView.getTree();
		treeViewer_queryView.setLabelProvider(new QueryViewLabelProvider());// 标签
		treeViewer_queryView.setContentProvider(new QueryViewTreeContentProvider());// 数据
		tree_queryView.addMouseListener(new QueryViewTreeMenu(shell));// 右键
		treeViewer_queryView.setInput(new String("root"));// 初始数据
		scrolledComposite_queryView.setContent(tree_queryView);
		scrolledComposite_queryView.setMinSize(tree_queryView.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// 右侧工作区
		scrolledComposite_content = new ScrolledComposite(sashForm, SWT.BORDER | SWT.V_SCROLL);
		scrolledComposite_content.setExpandHorizontal(true);
		scrolledComposite_content.setExpandVertical(true);

		composite_content = new Composite(scrolledComposite_content, SWT.NONE);
		composite_content.setLayout(new GridLayout(5, true));
		scrolledComposite_content.setContent(composite_content);

		scrolledComposite_content.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT)); // 重新计算composite，写在最后

		sashForm.setWeights(new int[] { 2, 5 });
		sashForm.addControlListener(new ControlListener() {
			int left_width = 200;// 固定左侧边栏宽度

			@Override
			public void controlMoved(ControlEvent arg0) {
			}

			@Override
			public void controlResized(ControlEvent arg0) {
				Point p = sashForm.getSize();
				sashForm.setWeights(new int[] { left_width, p.x - left_width }); // 这里是关键
			}
		});

	}

	// 新建连接
	private void newConnection() {
		if (MainWindow.tabFolder_view != null)
			MainWindow.tabFolder_view.setSelection(0);
		new ConnectionDialog(Constant.WindowType_Add, null, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
	}

	// 新建格文档
	private void newGriDoc() {
		if (MainWindow.tabFolder_view != null)
			MainWindow.tabFolder_view.setSelection(0);
		if (treeViewer_globalView.getTree().getSelection().length == 0) {
			MessageBox box2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
			box2.setMessage("请选择所要操作的数格引擎！");
			box2.open();
			return;
		}
		GlobalViewTreeNode selectedNode = (GlobalViewTreeNode) treeViewer_globalView.getTree().getSelection()[0]
				.getData();
		if (!(selectedNode.data instanceof Connection)) {
			MessageBox box2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
			box2.setMessage("请选择所要操作的数格引擎！");
			box2.open();
			return;
		}
		new GriDocDialog(Constant.WindowType_Add, selectedNode, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
	}

	// 新建节
	private void newSection() {
		if (MainWindow.tabFolder_view != null)
			MainWindow.tabFolder_view.setSelection(0);
		if (treeViewer_globalView.getTree().getSelection().length == 0) {
			MessageBox box2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
			box2.setMessage("请选择所要操作的格文档或节节点！");
			box2.open();
			return;
		}
		GlobalViewTreeNode selectedNode = (GlobalViewTreeNode) treeViewer_globalView.getTree().getSelection()[0]
				.getData();
		if (!(selectedNode.data instanceof GriDoc) && !(selectedNode.data instanceof Section)) {
			MessageBox box2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
			box2.setMessage("请选择所要操作的格文档或节节点！");
			box2.open();
			return;
		}
		new SectionDialog(Constant.WindowType_Add, selectedNode, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
	}

	// 新建段
	private void newParagraph() {
		if (MainWindow.tabFolder_view != null)
			MainWindow.tabFolder_view.setSelection(0);
		if (treeViewer_globalView.getTree().getSelection().length == 0) {
			MessageBox box2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
			box2.setMessage("请选择所要操作的格文档或节节点！");
			box2.open();
			return;
		}
		GlobalViewTreeNode selectedNode = (GlobalViewTreeNode) treeViewer_globalView.getTree().getSelection()[0]
				.getData();
		if (!(selectedNode.data instanceof GriDoc) && !(selectedNode.data instanceof Section)) {
			MessageBox box2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
			box2.setMessage("请选择所要操作的格文档或节节点！");
			box2.open();
			return;
		}
		new ParagraphDialog(Constant.WindowType_Add, selectedNode, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
	}

	// 查询
	private void query() {
		if (treeViewer_globalView.getTree().getSelection().length == 0) {
			MessageBox box2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
			box2.setMessage("请选择所要操作数格引擎连接！");
			box2.open();
			return;
		}
		TreeItem item = treeViewer_globalView.getTree().getSelection()[0];// 选中的节点
		if (item != null) {
			Object obj = item.getData();
			if (obj instanceof GlobalViewTreeNode) {
				GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
				if (node.data instanceof Connection) {
					Connection con = (Connection) node.data;
					if (!con.isUsing()) {
						MessageBox box3 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
						box3.setMessage("数格引擎连接未打开！");
						box3.open();
						return;
					}
				}
				new QueryDialog(node.manager, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		}
	}

	// 处理器
	private void processor() {
		if (treeViewer_globalView.getTree().getSelection().length == 0) {
			MessageBox box2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
			box2.setMessage("请选择所要操作数格引擎连接！");
			box2.open();
			return;
		}
		TreeItem item = treeViewer_globalView.getTree().getSelection()[0];// 选中的节点
		if (item != null) {
			Object obj = item.getData();
			if (obj instanceof GlobalViewTreeNode) {
				GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
				if (node.data instanceof Connection) {
					Connection con = (Connection) node.data;
					if (!con.isUsing()) {
						MessageBox box3 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
						box3.setMessage("数格引擎连接未打开！");
						box3.open();
						return;
					}
				}
				if (!node.manager.getCurrentAccount().equals("root")) {
					MessageBox box4 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
					box4.setMessage("请使用超级管理员账户root进行处理器管理！");
					box4.open();
					return;
				}
				new ProcessorManageDialog(node.manager.ProcessManager(), shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		}
	}
	
	// 容器空间
	private void container() {
		if (treeViewer_globalView.getTree().getSelection().length == 0) {
			MessageBox box2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
			box2.setMessage("请选择所要操作数格引擎连接！");
			box2.open();
			return;
		}
		TreeItem item = treeViewer_globalView.getTree().getSelection()[0];// 选中的节点
		if (item != null) {
			Object obj = item.getData();
			if (obj instanceof GlobalViewTreeNode) {
				GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
				if (node.data instanceof Connection) {
					Connection con = (Connection) node.data;
					if (!con.isUsing()) {
						MessageBox box3 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
						box3.setMessage("数格引擎连接未打开！");
						box3.open();
						return;
					}
				}
				if (!node.manager.getCurrentAccount().equals("root")) {
					MessageBox box4 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
					box4.setMessage("请使用超级管理员账户root进行容器空间管理！");
					box4.open();
					return;
				}
				new ContainerManageDialog(node.manager.ProcessManager(),node.manager.getCurrentAccount(), shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		}
	}
	
	// 视图
	private void view() {
		if (treeViewer_globalView.getTree().getSelection().length == 0) {
			MessageBox box2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
			box2.setMessage("请选择所要操作数格引擎连接！");
			box2.open();
			return;
		}
		TreeItem item = treeViewer_globalView.getTree().getSelection()[0];// 选中的节点
		if (item != null) {
			Object obj = item.getData();
			if (obj instanceof GlobalViewTreeNode) {
				GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
				if (node.data instanceof Connection) {
					Connection con = (Connection) node.data;
					if (!con.isUsing()) {
						MessageBox box3 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
						box3.setMessage("数格引擎连接未打开！");
						box3.open();
						return;
					}
				}
				if (!node.manager.getCurrentAccount().equals("root")) {
					MessageBox box4 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
					box4.setMessage("请使用超级管理员账户root进行容器空间管理！");
					box4.open();
					return;
				}
				new ViewManageDialog(node.manager.ProcessManager(),null, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		}
	}
	
	// 用户
	private void user() {
		if (treeViewer_globalView.getTree().getSelection().length == 0) {
			MessageBox box2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
			box2.setMessage("请选择所要操作数格引擎连接！");
			box2.open();
			return;
		}
		TreeItem item = treeViewer_globalView.getTree().getSelection()[0];// 选中的节点
		if (item != null) {
			Object obj = item.getData();
			if (obj instanceof GlobalViewTreeNode) {
				GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
				if (node.data instanceof Connection) {
					Connection con = (Connection) node.data;
					if (!con.isUsing()) {
						MessageBox box3 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
						box3.setMessage("数格引擎连接未打开！");
						box3.open();
						return;
					}
				}
				if (!node.manager.getCurrentAccount().equals("root")) {
					MessageBox box4 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
					box4.setMessage("请使用超级管理员账户root进行用户管理！");
					box4.open();
					return;
				}
				new UserManageDialog(node.manager.UserManager(), shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		}
	}

	// 清理--删除服务器未引用格文档元素
	private void clearUnreferenceGriElement() {
		if (treeViewer_globalView.getTree().getSelection().length == 0) {
			MessageBox box2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
			box2.setMessage("请选择所要操作数格引擎连接！");
			box2.open();
			return;
		}
		TreeItem item = treeViewer_globalView.getTree().getSelection()[0];// 选中的节点
		if (item != null) {
			Object obj = item.getData();
			if (obj instanceof GlobalViewTreeNode) {
				GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
				if (node.data instanceof Connection) {
					Connection con = (Connection) node.data;
					if (!con.isUsing()) {
						MessageBox box3 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
						box3.setMessage("数格引擎连接未打开！");
						box3.open();
						return;
					}
				}
				if (!node.manager.getCurrentAccount().equals("root")) {
					MessageBox box4 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
					box4.setMessage("请使用超级管理员账户root进行用户管理！");
					box4.open();
					return;
				}
				node.manager.clearUnreferenceParagraph();
			}
		}
	}

	// 关于
	private void about() {
		new AboutDialog(shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
	}

	// 联系我们
	private void contact() {
		try {
			Runtime.getRuntime().exec("cmd /c start" + " mailto:qideyu@gmail.com");
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	// 清空预览区组件
	public static void clearContentComposite() {
		for (Control ctr : composite_content.getChildren())
			ctr.dispose();
	}

	// 创建预览组件
	public static TextViewer createContentComposite() {
		TextViewer textViewer = new TextViewer(composite_content, shell ,SWT.NONE);
		GridData gd_textViewer = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1);
		gd_textViewer.heightHint = 360;
		textViewer.setLayoutData(gd_textViewer);
		return textViewer;
	}

	// 重新加载预览区布局
	public static void reLayoutContentComposite() {
		composite_content.layout(true);
		scrolledComposite_content.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT)); // 重新计算composite，写在最后
	//	scrolledComposite_content.setMinSize(composite_content.computeSize(420, 256));
	}
}
