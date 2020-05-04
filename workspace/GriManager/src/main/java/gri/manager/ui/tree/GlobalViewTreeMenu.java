package gri.manager.ui.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gri.driver.model.process.SourceNode;
import gri.manager.ui.window.newWindow.FileIncNodeDialog;
import gri.manager.ui.window.newWindow.SourceNodeDialog;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import gri.driver.model.Connection;
import gri.driver.model.GriDoc;
import gri.driver.model.GriElement;
import gri.driver.model.Section;
import gri.driver.model.process.Paragraph;
import gri.engine.integrate.Paragraph2;
import gri.engine.integrate.Paragraph3;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.ui.window.BlockDialog;
import gri.manager.ui.window.ConnectionDialog;
import gri.manager.ui.window.GriDocDialog;
import gri.manager.ui.window.MainWindow;
import gri.manager.ui.window.ParagraphDialog;
import gri.manager.ui.window.ProcessManageDialog;
import gri.manager.ui.window.SavaDataDialog;
import gri.manager.ui.window.SectionDialog;
import gri.manager.ui.window.StatsConfManageDialog;
import gri.manager.ui.window.SyncConfigDialog;
import gri.manager.ui.window.TextViewer;
import gri.manager.ui.window.UploadDataDialog;
import gri.manager.ui.window.paragraph3.ParagraphDialog3;
import gri.manager.util.Constant;
import gri.manager.util.RunTimeData;

/**
 * 格文档树目录右键菜单
 * 
 * @author 许诺
 *
 */
public class GlobalViewTreeMenu implements MouseListener {
	private Tree tree;
	private TreeViewer treeViewer;
	private Shell mainWindowShell;
	private Item item;

	private Action refreshGlobalView;// 刷新树
	private Action refresh;// 刷新节点

	private Action syncData;// 同步段数据
	private Action saveData;// 保存段数据到本地文件

	private Action refreshPreviewData;// 刷新预览数据
	private Action previewData;// 预览段数据
	private Action statistics;//统计
	private Action statsConf;//通用统计
	private Action process;//数据处理
	private Action block;//区块链

	private Action openConnection;
	private Action closeConnection;
	
	private Action shutEngine;

	private Action newConnection;
	private Action newGriDoc;
	private Action newSection;
	private Action newParagraph;
	private Action newParagraph2;
	private Action newParagraph3;

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

	private Action updateSync;
	private Action updataAllSync;

	private Action newSourceNode;
	private Action newFileIncremental;
	
	private String CurrentUser;
	
	public GlobalViewTreeMenu(Shell parentShell) {
		this.treeViewer = MainWindow.treeViewer_globalView;
		this.tree = this.treeViewer.getTree();
		this.mainWindowShell = parentShell;
		this.createActions();
	}

	// 初始化右键菜单
	private void initPopMenu() {
		if (this.tree.getSelection().length == 0) {
			this.initEmptyPopMenu();//若未选中
			return;
		}
		this.item = this.tree.getSelection()[0];// 选中的节点
		if (item != null) {
			Object obj = item.getData();
			if (obj instanceof GlobalViewTreeNode) {
				GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
			    Connection conn=RunTimeData.globalView.get(0);
				CurrentUser=conn.getUser();
				if (node.data instanceof Connection) {
					this.initConnectionPoPMenu((Connection) node.data);
				} else if (node.data instanceof GriDoc)
					this.initGriDocPoPMenu();
				else if (node.data instanceof Section)
					this.initSectionPoPMenu();
				else if(node.data instanceof SourceNode)
					this.initSourceNodeMenu();
				else if(node.data instanceof Paragraph2)
					this.initParagraph2PoPMenu();
				else if (node.data instanceof Paragraph3)
					this.initParagraph3PoPMenu();
				else if (node.data instanceof Paragraph)
					this.initParagraphPoPMenu();
			}
		}
	}


	// 空节点右键菜单
	private void initEmptyPopMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.add(this.refreshGlobalView);
		menuManager.add(this.newConnection);
		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}

	// 连接右键菜单
	private void initConnectionPoPMenu(Connection conn) {
		MenuManager menuManager = new MenuManager();

		menuManager.add(this.openConnection);
		menuManager.add(this.closeConnection);
		menuManager.add(this.newConnection);
		menuManager.add(this.newGriDoc);
		menuManager.add(this.updataAllSync);
		
		if (conn.isUsing()) {
			this.openConnection.setEnabled(false);
			this.closeConnection.setEnabled(true);
			this.newGriDoc.setEnabled(true);
			this.shutEngine.setEnabled(true);
		} else {
			this.openConnection.setEnabled(true);
			this.closeConnection.setEnabled(false);
			this.newGriDoc.setEnabled(false);
			this.shutEngine.setEnabled(false);
		}
		menuManager.add(this.shutEngine);
		menuManager.add(this.expandAll);
		menuManager.add(this.collapseAll);
		menuManager.add(this.refresh);
		menuManager.add(this.deleteConnection);
		menuManager.add(this.connectionProperty);
		if(!CurrentUser.equals("root")) {
			this.newGriDoc.setEnabled(false);
			this.shutEngine.setEnabled(false);
		}else {
			this.newGriDoc.setEnabled(true);
			this.shutEngine.setEnabled(true);
		}
		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu); 
	}

	// 格文档右键菜单
	private void initGriDocPoPMenu() {
		MenuManager menuManager = new MenuManager();
//		menuManager.add(this.newSection);
//		menuManager.add(this.newParagraph);
//		menuManager.add(this.newParagraph2);
//		menuManager.add(this.newParagraph3);
		menuManager.add(this.expandAll);
		menuManager.add(this.collapseAll);
		menuManager.add(this.refresh);
		menuManager.add(this.deleteGriDoc);
		menuManager.add(this.griDocProperty);
//		menuManager.add(this.previewData);
		menuManager.add(this.newFileIncremental);
		menuManager.add(this.newSourceNode);

		if(!CurrentUser.equals("root")) {
			this.newSection.setEnabled(false);
			this.newParagraph.setEnabled(false);
			this.newParagraph3.setEnabled(false);
			this.deleteGriDoc.setEnabled(false);
		}else {
			this.newSection.setEnabled(true);
			this.newParagraph.setEnabled(true);
			this.newParagraph3.setEnabled(true);
			this.deleteGriDoc.setEnabled(true);
		}
		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}

	// 节右键菜单
	private void initSectionPoPMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.add(this.newSection);
		menuManager.add(this.newParagraph);
//		menuManager.add(this.newParagraph2);
		menuManager.add(this.newParagraph3);
//		menuManager.add(this.expandAll);
//		menuManager.add(this.collapseAll);
		menuManager.add(this.refresh);
		menuManager.add(this.deleteSection);
		menuManager.add(this.sectionProperty);
		menuManager.add(this.previewData);

		menuManager.add(this.newSourceNode);

		if(!CurrentUser.equals("root")) {
			this.newSection.setEnabled(false);
			this.deleteSection.setEnabled(false);
			this.newParagraph.setEnabled(false);
			this.newParagraph3.setEnabled(false);
		}else {
			this.newSection.setEnabled(true);
			this.deleteSection.setEnabled(true);
			this.newParagraph.setEnabled(true);
			this.newParagraph3.setEnabled(true);
		}
		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}

	private void initSourceNodeMenu(){
		MenuManager menuManager = new MenuManager();

	}

	// 段右键菜单
	private void initParagraphPoPMenu() {
		MenuManager menuManager = new MenuManager();
//		menuManager.add(this.expandAll);
//		menuManager.add(this.collapseAll);
		menuManager.add(this.refresh);
		menuManager.add(this.deleteParagraph);
		menuManager.add(this.paragraphProperty);
        menuManager.add(this.updateSync);
		menuManager.add(this.syncData); // 同步段
		if(!CurrentUser.equals("root")) {
			this.deleteParagraph.setEnabled(false);
		}else {
			this.deleteParagraph.setEnabled(true);
		}
//		menuManager.add(this.saveData);// 保存段

//		menuManager.add(this.refreshPreviewData); // 刷新段预览
//		menuManager.add(this.previewData);// 预览段
//		//menuManager.add(this.statistics);// 统计
//		menuManager.add(this.statsConf);// 通用统计
//		menuManager.add(this.process);// 数据处理
//		menuManager.add(this.block);// 区块链
		

		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}

	private void initParagraph3PoPMenu() {
		MenuManager menuManager = new MenuManager();
//		menuManager.add(this.expandAll);
//		menuManager.add(this.collapseAll);
		menuManager.add(this.refresh);
		menuManager.add(this.deleteParagraph);
		menuManager.add(this.paragraphProperty);
        menuManager.add(this.updateSync);
		menuManager.add(this.syncData); // 同步段
		if(!CurrentUser.equals("root")) {
			this.deleteParagraph.setEnabled(false);
		}else {
			this.deleteParagraph.setEnabled(true);
		}
//		menuManager.add(this.saveData);// 保存段

//		menuManager.add(this.refreshPreviewData); // 刷新段预览
//		menuManager.add(this.previewData);// 预览段
//		//menuManager.add(this.statistics);// 统计
//		menuManager.add(this.statsConf);// 通用统计
//		menuManager.add(this.process);// 数据处理
//		menuManager.add(this.block);// 区块链
		

		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}
	private void initParagraph2PoPMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.add(this.expandAll);
		menuManager.add(this.collapseAll);
		menuManager.add(this.refresh);
		menuManager.add(this.deleteParagraph);
		menuManager.add(this.paragraphProperty);
        menuManager.add(this.updateSync);
		menuManager.add(this.syncData); // 同步段
		if(!CurrentUser.equals("root")) {
			this.deleteParagraph.setEnabled(false);
		}else {
			this.deleteParagraph.setEnabled(true);
		}
//		menuManager.add(this.saveData);// 保存段

//		menuManager.add(this.refreshPreviewData); // 刷新段预览
//		menuManager.add(this.previewData);// 预览段
//		//menuManager.add(this.statistics);// 统计
//		menuManager.add(this.statsConf);// 通用统计
//		menuManager.add(this.process);// 数据处理
//		menuManager.add(this.block);// 区块链
		

		Menu rootMenu = menuManager.createContextMenu(this.tree);
		this.tree.setMenu(rootMenu);
	}
	
	private void createActions() {

		this.updateSync=new Action("修改同步配置") {
			public void run() {
				Paragraph paragraph=(Paragraph) ((GlobalViewTreeNode)item.getData()).data;
				new SyncConfigDialog(paragraph,(GlobalViewTreeNode)item.getData(), mainWindowShell, SWT.CLOSE | SWT.APPLICATION_MODAL,true).open();
			}
		};
		
		this.updataAllSync=new Action("修改所有同步配置") {
			public void run () {
			   Paragraph paragraph=new Paragraph();
			   paragraph.setWarmSyncDetail("");
			   paragraph.setSyncTimeType("");
			   paragraph.setSyncDirectionType("");
			   new SyncConfigDialog(paragraph,(GlobalViewTreeNode)item.getData(),mainWindowShell,SWT.Close | SWT.APPLICATION_MODAL,true,true).open();
			}
		};
		
		this.shutEngine=new Action("关闭引擎") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				node.manager.shutEngine();
			}
		};
		
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
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				treeViewer.refresh(node);
			}
		};

		this.refreshGlobalView = new Action("刷新") {
			public void run() {
				treeViewer.refresh();
			}
		};

		this.connectionProperty = new Action("查看属性") {
			public void run() {
				new ConnectionDialog(Constant.WindowType_Edit, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
						SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};

		this.griDocProperty = new Action("查看属性") {
			public void run() {
				new GriDocDialog(Constant.WindowType_Edit, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
						SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};
		this.sectionProperty = new Action("查看属性") {
			public void run() {
				new SectionDialog(Constant.WindowType_Edit, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
						SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};
		this.paragraphProperty = new Action("查看属性") {
			public void run() {
				if(!((((GlobalViewTreeNode) item.getData()).data instanceof Paragraph3)||((GlobalViewTreeNode) item.getData()).data instanceof Paragraph2))
				{
				new ParagraphDialog(Constant.WindowType_Edit, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
						SWT.CLOSE | SWT.APPLICATION_MODAL).open();
				}
				else
				{
					new ParagraphDialog3(Constant.WindowType_Edit, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
							SWT.CLOSE | SWT.APPLICATION_MODAL).open();
				}
			}
		};
		this.saveData = new Action("保存数据") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				new SavaDataDialog((Paragraph) node.data, node.manager, mainWindowShell,
						SWT.CLOSE | SWT.APPLICATION_MODAL).open();//手动以json文件格式保存数据
			}
		};
		this.previewData = new Action("预览数据") {
			public void run() {
				previewData();
			}
		};
		
		/*this.statistics = new Action("统计") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				new StatsDialog(mainWindowShell, SWT.CLOSE | SWT.APPLICATION_MODAL, 
						(Paragraph) node.data, node.manager.ProcessManager()).open();
			}
		};*/
		
		this.statsConf = new Action("通用统计") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				Paragraph ori = (Paragraph) node.data;
				gri.driver.model.process.Paragraph dest = node.manager.getParagraphByName(ori.getName());
				new StatsConfManageDialog(node.manager.ProcessManager(), dest, mainWindowShell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};
		
		this.process = new Action("使用管道") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				new ProcessManageDialog(node.manager.ProcessManager(), (Paragraph) node.data, node.manager.getCurrentAccount(), mainWindowShell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};
		
		this.block = new Action("区块链") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				new BlockDialog(node.manager, ((Paragraph) node.data).getId(), mainWindowShell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};
		
		this.refreshPreviewData = new Action("刷新段预览") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				if (node.data instanceof Paragraph) {
					Paragraph para = (Paragraph) node.data;
					node.manager.refreshPreviewParagraphData(para.getId());
					System.out.println("已发送服务器端段预览数据刷新请求 [paraID:" + para.getId() + "]");
				}
			}
		};
		this.syncData = new Action("同步数据") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				if (node.data instanceof Paragraph3){
					Paragraph3 paragraph3=(Paragraph3)node.data;
					boolean result = node.manager.forceSyncData(paragraph3.getId());//强制同步数据
					if(result){
						MessageBox box = new MessageBox(mainWindowShell, SWT.ICON_INFORMATION);
						box.setMessage("同步成功");
						box.open();
					}
					else{
						MessageBox box = new MessageBox(mainWindowShell, SWT.ICON_WARNING);
						box.setMessage("同步数据请求已接受，正在等待调度或正在执行中,请耐心等待同步完成");
						box.open();
					}
				}
				else if (node.data instanceof Paragraph) {
					Paragraph paragraph = (Paragraph) node.data;
					boolean result = node.manager.forceSyncData(paragraph.getId());//强制同步数据
					if(result){
						MessageBox box = new MessageBox(mainWindowShell, SWT.ICON_INFORMATION);
						box.setMessage("同步成功");
						box.open();
					}
					else{
						MessageBox box = new MessageBox(mainWindowShell, SWT.ICON_WARNING);
						box.setMessage("同步数据请求已接受，正在等待调度或正在执行中,请耐心等待同步完成");
						box.open();
					}
					
					
					/*if(!(paragraph.getDataDestType()==null||paragraph.getDataDestbase()==null
							||paragraph.getDestAccount()==null||paragraph.getDestIP()==null
							||paragraph.getDestPassword()==null||paragraph.getDestPort()==null)){//若选择了物化
						CacheService cs=new CacheService(paragraph.getCache());
						InputStream input=cs.getInputStream();
						InputStream input2=cs.getInputStream();
						InputStream input3=cs.getInputStream();
						
						String destPath=paragraph.getDataDestType()+"###"+paragraph.getDataDestbase()+"###"+paragraph.getDestAccount()+"###"+
								paragraph.getDestPassword()+"###"+paragraph.getDestIP()+"###"+paragraph.getDestPort();
						
						DestDatabase des=new DestDatabase(destPath);
						int columnCount=des.getColumnCount(input);
						List<Column> columns=des.initColumn(input2, columnCount);
				       boolean create= des.createTable(columns, columnCount,paragraph.getName(),false);
				       boolean insert= des.insert(input3,paragraph.getName());
						if(!(create&&insert)){
							return;}else{
						String sql5="insert into dest_info(paragraph_id,data_dest_type,data_dest_base,dest_port,dest_account,dest_password,dest_IP)values(?,?,?,?,?,?,?)";
						Object[] obj5=new Object[] {paragraph.getId(),paragraph.getDataDestType(),paragraph.getDataDestbase(),
								paragraph.getDestPort(),paragraph.getDestAccount(),
								paragraph.getDestPassword(),paragraph.getDestIP()};
						DBHelper.executeNonQuery(sql5, obj5);
						}
						}*/
					System.out.println("已发送服务器端段数据强制同步请求 [paraID:" + paragraph.getId() + "]");
				}
			}
		};

		this.openConnection = new Action("打开连接") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				Connection con = (Connection) node.data;
				if (con.canConnect()) {
					con.setUsing(true);
					MainWindow.treeViewer_globalView.refresh(node);
				} else {
					MessageBox box = new MessageBox(mainWindowShell);
					box.setMessage("打开连接失败！");
					box.open();
				}
			}
		};

		this.closeConnection = new Action("关闭连接") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				Connection con = (Connection) node.data;
				con.setUsing(false);
				MainWindow.treeViewer_globalView.refresh(node);
			}
		};

		this.newConnection = new Action("新建连接") {
			public void run() {
				new ConnectionDialog(Constant.WindowType_Add, null, mainWindowShell, SWT.CLOSE | SWT.APPLICATION_MODAL)
						.open();
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
//		this.newParagraph2=new Action("新建段2") {
//			public void run() {
//				new ParagraphDialog2(Constant.WindowType_Add, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
//						SWT.CLOSE | SWT.APPLICATION_MODAL).open();
//			}
//		};
		this.newParagraph3=new Action("新建段3") {
			public void run() {
				new ParagraphDialog3(Constant.WindowType_Add, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
						SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};

		this.newFileIncremental=new Action("新增文件增量同步配置") {
			public void run() {
				new FileIncNodeDialog(Constant.WindowType_Add, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
						SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};

		this.newSourceNode=new Action("新建异构数据集成配置"){
			public void run(){
					new SourceNodeDialog(Constant.WindowType_Add, ((GlobalViewTreeNode) item.getData()), mainWindowShell,
							SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		};
		this.deleteConnection = new Action("删除") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				RunTimeData.globalView.remove(node.data);//删除连接
				MainWindow.treeViewer_globalView.refresh();
			}
		};
		this.deleteGriDoc = new Action("删除") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				if (node.manager.deleteGriDoc((GriDoc) node.data))
					MainWindow.treeViewer_globalView.remove(node);
			}
		};
		this.deleteSection = new Action("删除") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				if (node.manager.deleteGriElement(node.root, (Section) node.data))
					MainWindow.treeViewer_globalView.remove(node);
			}
		};
		this.deleteParagraph = new Action("删除") {
			public void run() {
				GlobalViewTreeNode node = (GlobalViewTreeNode) item.getData();
				if (node.manager.deleteGriElement(node.root, (Paragraph) node.data))
					MainWindow.treeViewer_globalView.remove(node);
			}
		};

	}

	private void previewData() {
		if (this.tree.getSelection().length == 0)
			return;
		this.item = this.tree.getSelection()[0];// 选中的节点
		if (item != null) {
			Object obj = item.getData();
			if (obj instanceof GlobalViewTreeNode) {
				final GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
				if (node.data instanceof Paragraph) {
					MainWindow.clearContentComposite();
					TextViewer textViewer = MainWindow.createContentComposite();
					//MainWindow.reLayoutContentComposite();// important!
					textViewer.loadData((Paragraph) node.data, node.manager);
					textViewer.getButton_download().addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							new SavaDataDialog((Paragraph) node.data, node.manager, mainWindowShell,
									SWT.CLOSE | SWT.APPLICATION_MODAL).open();
						}
					});
					textViewer.getButton_upload().addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							new UploadDataDialog((Paragraph) node.data, node.manager, mainWindowShell,
									SWT.CLOSE | SWT.APPLICATION_MODAL).open();
						}
					});
				} else if (node.data instanceof Section || node.data instanceof GriDoc) {
					MainWindow.clearContentComposite(); // [1]
					List<Paragraph> paragraghs = node.manager.getParagraphOfGriElement((GriElement) node.data);
					List<TextViewer> textViewers = new ArrayList<TextViewer>();
					for (int i = 0; i < paragraghs.size(); i++)
						textViewers.add(MainWindow.createContentComposite());// [2]
					MainWindow.reLayoutContentComposite();// [3]important!
					for (int i = 0; i < paragraghs.size(); i++) {
						final Paragraph paragraph = paragraghs.get(i);
						textViewers.get(i).loadData(paragraph, node.manager);// [4]
						textViewers.get(i).getButton_download().addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								new SavaDataDialog(paragraph, node.manager, mainWindowShell,
										SWT.CLOSE | SWT.APPLICATION_MODAL).open();
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
		}
	}
	private void statistics() {
		if (this.tree.getSelection().length == 0)
			return;
		this.item = this.tree.getSelection()[0];// 选中的节点
		if (item != null) {
			Object obj = item.getData();
			if (obj instanceof GlobalViewTreeNode) {
				final GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
				if (node.data instanceof Paragraph) {
					MainWindow.clearContentComposite();
					TextViewer textViewer = MainWindow.createContentComposite();
					//MainWindow.reLayoutContentComposite();// important!
					textViewer.loadData((Paragraph) node.data, node.manager);
					
				} else if (node.data instanceof Section || node.data instanceof GriDoc) {
					/*MainWindow.clearContentComposite(); // [1]
					List<Paragraph> paragraghs = node.manager.getParagraphOfGriElement((GriElement) node.data);
					List<TextViewer> textViewers = new ArrayList<TextViewer>();
					for (int i = 0; i < paragraghs.size(); i++)
						textViewers.add(MainWindow.createContentComposite());// [2]
					MainWindow.reLayoutContentComposite();// [3]important!
					for (int i = 0; i < paragraghs.size(); i++) {
						final Paragraph paragraph = paragraghs.get(i);
						textViewers.get(i).loadData(paragraph, node.manager);// [4]
						textViewers.get(i).getButton_download().addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								new SavaDataDialog(paragraph, node.manager, mainWindowShell,
										SWT.CLOSE | SWT.APPLICATION_MODAL).open();
							}
						});
						textViewers.get(i).getButton_upload().addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								new UploadDataDialog(paragraph, node.manager, mainWindowShell,
										SWT.CLOSE | SWT.APPLICATION_MODAL).open();
							}
						});
					}*/
				}
			}
		}
	}
	
	
	// 双击
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		if (this.tree.getSelection().length == 0)
			return;
		this.item = this.tree.getSelection()[0];// 选中的节点
		if (item != null) {
			Object obj = item.getData();
			if (obj instanceof GlobalViewTreeNode) {
				final GlobalViewTreeNode node = (GlobalViewTreeNode) obj;
				if (node.data instanceof Connection) {
					Connection conn = (Connection) node.data;
					if (!conn.isUsing() && conn.canConnect()) {
						conn.setUsing(true);
						MainWindow.treeViewer_globalView.refresh(node);
					}
				} else if (node.data instanceof Paragraph) {
					MainWindow.clearContentComposite();
					TextViewer textViewer = MainWindow.createContentComposite();
					//MainWindow.reLayoutContentComposite();// important!
					textViewer.loadData((Paragraph) node.data, node.manager);
					textViewer.getButton_download().addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							new SavaDataDialog((Paragraph) node.data, node.manager, mainWindowShell,
									SWT.CLOSE | SWT.APPLICATION_MODAL).open();
						}
					});
					textViewer.getButton_upload().addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							new UploadDataDialog((Paragraph) node.data, node.manager, mainWindowShell,
									SWT.CLOSE | SWT.APPLICATION_MODAL).open();
						}
					});
				} else if (node.data instanceof Section || node.data instanceof GriDoc) {
					MainWindow.clearContentComposite(); // [1]
					List<Paragraph> paragraghs = node.manager.getParagraphOfGriElement((GriElement) node.data);//获取结的所有段
					List<TextViewer> textViewers = new ArrayList<TextViewer>();
					for (int i = 0; i < paragraghs.size(); i++)
						textViewers.add(MainWindow.createContentComposite());// [2]
					MainWindow.reLayoutContentComposite();// [3]important!重新加载预览区布局
					for (int i = 0; i < paragraghs.size(); i++) {
						final Paragraph paragraph = paragraghs.get(i);
						textViewers.get(i).loadData(paragraph, node.manager);// [4]
						textViewers.get(i).getButton_download().addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								new SavaDataDialog(paragraph, node.manager, mainWindowShell,
										SWT.CLOSE | SWT.APPLICATION_MODAL).open();
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
		}
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

}
