package gri.manager.ui.window;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import gri.driver.model.AccessPermission;
import gri.driver.model.GriDoc;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.util.Constant;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class GriDocDialog extends Dialog {
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof AccessPermission) {
				AccessPermission accessPermission = (AccessPermission) element;
				if (columnIndex == 0)
					return accessPermission.getUserAccount();
				else if (columnIndex == 1)
					return accessPermission.isReadOnly() ? "只读" : "读写";
			}
			return "";
		}
	}

	private class ContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement.equals("root"))
				return node.manager.queryAccessPermission((GriDoc) node.data).toArray(new Object[0]);
			else
				return null;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	private int windowType;
	protected Object result;
	protected Shell shell;
	private Shell parentShell;
	private GlobalViewTreeNode node;
	private Text text_name;
	private Text text_updateTime;
	private Label label_updateTime;
	private Table table;
	public static TableViewer tableViewer;
	private Button button_add;
	private Button button_edit;
	private Button button_delete;

	private Button button_ok;
	private Button button_cancel;

	public GriDocDialog(int windowType, GlobalViewTreeNode node, Shell parent, int style) {//新建格文档窗口
		super(parent, style);
		this.windowType = windowType;
		this.parentShell = parent;
		this.node = node;
	}

	public Object open() {
		createContents();
		if (parentShell != null)
			this.initWindow();
		if (this.windowType != Constant.WindowType_Add)
			this.loadData();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(308, 367);
		shell.setText("新建格文档");
		shell.setImage(new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/gridoc16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 282, 285);

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("常规");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(29, 30, 48, 17);
		label.setText("名称：");

		text_name = new Text(composite, SWT.BORDER);
		text_name.setBounds(77, 27, 152, 23);

		label_updateTime = new Label(composite, SWT.NONE);
		label_updateTime.setText("修改时间：");
		label_updateTime.setBounds(10, 91, 61, 17);

		text_updateTime = new Text(composite, SWT.READ_ONLY);
		text_updateTime.setText("?");
		text_updateTime.setBounds(77, 91, 187, 23);

		TabItem tabItem_1 = new TabItem(tabFolder, 0);
		tabItem_1.setText("安全");

		Composite composite_security = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite_security);

		tableViewer = new TableViewer(composite_security, SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setBounds(10, 10, 254, 191);

		String[] heards = new String[] { "用户", "权限" };

		TableViewerColumn column1 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tableColumn_1 = column1.getColumn();
		tableColumn_1.setResizable(false);
		column1.getColumn().setText(heards[0]);
		column1.getColumn().setWidth(130);

		TableViewerColumn column2 = new TableViewerColumn(tableViewer, SWT.CENTER);
		TableColumn tableColumn2 = column2.getColumn();
		tableColumn2.setResizable(false);
		column2.getColumn().setText(heards[1]);
		column2.getColumn().setWidth(80);

		button_add = new Button(composite_security, SWT.NONE);
		button_add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new SelectUserDialog(shell, node, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		});
		button_add.setText("添加");
		button_add.setBounds(10, 218, 36, 27);
		// 编辑权限
		button_edit = new Button(composite_security, SWT.NONE);
		button_edit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelection().length == 0)
					return;
				TableItem item1 = (TableItem) table.getSelection()[0];
				new EditAccessPermissionDialog(node, (AccessPermission) item1.getData(), shell,
						SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		});
		button_edit.setText("编辑");
		button_edit.setBounds(52, 218, 36, 27);

		// 删除权限
		button_delete = new Button(composite_security, SWT.NONE);
		button_delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelection().length == 0)
					return;
				TableItem item1 = (TableItem) table.getSelection()[0];
				boolean succeed = node.manager.deleteAccessPermission((AccessPermission) item1.getData());
				if (succeed)
					GriDocDialog.tableViewer.refresh();
			}
		});
		button_delete.setText("删除");
		button_delete.setBounds(94, 218, 36, 27);

		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(new ContentProvider());

		//

		button_ok = new Button(shell, SWT.NONE);
		button_ok.setBounds(152, 301, 60, 27);
		button_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String gridocName = text_name.getText().trim();
				if (gridocName.equals("")) {
					MessageBox box = new MessageBox(shell);
					box.setMessage("格文档名称不能为空！");
					box.open();
					return;
				}
				if (windowType == Constant.WindowType_Add) {
					GriDoc newGriDoc = node.manager.addGriDoc(new GriDoc(gridocName));
					if (newGriDoc == null) {
						MessageBox box2 = new MessageBox(shell);
						box2.setMessage("格文档创建失败！");
						box2.open();
						return;
					}
					close();
					MainWindow.treeViewer_globalView.add(node,
							new GlobalViewTreeNode(newGriDoc, newGriDoc, node.manager));
				} else if ((windowType == Constant.WindowType_Edit)) {
					GriDoc gridoc = (GriDoc) node.data;
					gridoc.setName(gridocName);
					if (!node.manager.updateGriDoc(gridoc)) {
						MessageBox box = new MessageBox(shell);
						box.setMessage("格文档修改失败！");
						box.open();
						return;
					}
					close();
					MainWindow.treeViewer_globalView.refresh(node);
				}
			}
		});
		button_ok.setText("确定");

		button_cancel = new Button(shell, SWT.NONE);
		button_cancel.setBounds(232, 301, 60, 27);
		button_cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		button_cancel.setText("取消");
	}

	private void loadData() {
		shell.setText("格文档属性");
		GriDoc gridoc = (GriDoc) node.data;
		text_name.setText(gridoc.getName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// 修改时间
		text_updateTime.setText(sdf.format(gridoc.getUpdateTime()));
		label_updateTime.setVisible(true);
		text_updateTime.setVisible(true);
		tableViewer.setInput("root");

		if (windowType == Constant.WindowType_Edit) {
			button_add.setVisible(true);
			button_edit.setVisible(true);
			button_delete.setVisible(true);
		}
		if (windowType == Constant.WindowType_Property) {
			button_ok.setVisible(false);
			button_cancel.setText("关闭");
		}

	}

	private void initWindow() {
		label_updateTime.setVisible(false);
		text_updateTime.setVisible(false);
		button_add.setVisible(false);
		button_edit.setVisible(false);
		button_delete.setVisible(false);
	}

	/**
	 * Close the dialog
	 */
	public void close() {
		shell.close();
	}

}
