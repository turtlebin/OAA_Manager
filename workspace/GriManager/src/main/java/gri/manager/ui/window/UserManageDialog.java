package gri.manager.ui.window;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import gri.driver.manager.UserManager;
import gri.driver.model.User;
import gri.manager.ui.table.UserTableContentProvider;
import gri.manager.ui.table.UserTableLabelProvider;
import gri.manager.util.Constant;
import gri.manager.util.RunTimeData;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class UserManageDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Shell parentShell;
	private UserManager manager;

	private Table table;
	public static TableViewer tableViewer;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public UserManageDialog(UserManager manager, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.manager = manager;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
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

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(353, 347);
		shell.setText("用户管理");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		tableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		this.table = tableViewer.getTable();
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//
			}
		});
		this.table.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				// 右键
				if (e.button == 3) {
					Menu menu = new Menu(table);
					table.setMenu(menu);

					MenuItem item_refresh = new MenuItem(menu, SWT.PUSH);
					item_refresh.setText("刷新");
					item_refresh.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(Event event) {
							RunTimeData.userView = manager.getAllUser();
							tableViewer.refresh();
						}
					});

					if (table.getSelection().length == 0)
						return;

					MenuItem item_edit = new MenuItem(menu, SWT.PUSH);
					item_edit.setText("编辑");
					item_edit.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(Event event) {
							edit();
						}
					});

					MenuItem item_delete = new MenuItem(menu, SWT.PUSH);
					item_delete.setText("删除");
					item_delete.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(Event event) {
							delete();
						}
					});
				}
			}
		});
		this.table.setBounds(10, 70, 327, 239);
		this.table.setHeaderVisible(true);
		this.table.setLinesVisible(true);

		ToolBar toolBar = new ToolBar(shell, SWT.FLAT);
		toolBar.setBounds(10, 8, 238, 56);

		ToolItem toolItem_add = new ToolItem(toolBar, SWT.PUSH);
		toolItem_add.setText("添加");
		toolItem_add.setImage(new Image(toolBar.getDisplay(), this.getClass().getResourceAsStream("/icons/add24.png")));
		toolItem_add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				add();
			}
		});
		ToolItem toolItem_edit = new ToolItem(toolBar, SWT.PUSH);
		toolItem_edit.setText("编辑");
		toolItem_edit
				.setImage(new Image(toolBar.getDisplay(), this.getClass().getResourceAsStream("/icons/edit24.png")));
		toolItem_edit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				edit();
			}
		});

		ToolItem toolItem_delete = new ToolItem(toolBar, SWT.PUSH);
		toolItem_delete.setText("删除");
		toolItem_delete
				.setImage(new Image(toolBar.getDisplay(), this.getClass().getResourceAsStream("/icons/delete24.png")));
		toolItem_delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				delete();
			}
		});

		ToolItem toolItem_refresh = new ToolItem(toolBar, SWT.PUSH);
		toolItem_refresh.setText("刷新");
		toolItem_refresh
				.setImage(new Image(toolBar.getDisplay(), this.getClass().getResourceAsStream("/icons/refresh24.png")));
		toolItem_refresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RunTimeData.userView = manager.getAllUser();
				tableViewer.refresh();
			}
		});

		RunTimeData.userView = manager.getAllUser();

		String[] heards = new String[] { "用户", "密码", "昵称" };
		for (int i = 0; i < 3; i++) {
			TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.LEFT);
			column.getColumn().setText(heards[i]);
			column.getColumn().setWidth(80);
		}
		tableViewer.setLabelProvider(new UserTableLabelProvider());
		tableViewer.setContentProvider(new UserTableContentProvider());

		tableViewer.setInput("root");

	}

	private void delete() {
		if (table.getSelection().length == 0)
			return;
		TableItem item1 = (TableItem) table.getSelection()[0];
		User user = (User) item1.getData();
		if (user.getAccount().equals("root")) {
			MessageBox box = new MessageBox(shell);
			box.setMessage("不能删除root账户");
			box.open();
			return;
		}
		MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
		messageBox.setText("提示");
		messageBox.setMessage("确定删除：" + user.getAccount() + "？");
		if (messageBox.open() == SWT.YES) {
			boolean succeed = manager.deleteUser(user);
			if (succeed) {
				RunTimeData.userView.remove(user);
				Table parent = item1.getParent();
				if (parent != null) {
					table.remove(table.getSelectionIndices());
				}
			}
		}
	}

	private void add() {
		new UserDialog(manager, Constant.WindowType_Add, null, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
	}

	private void edit() {
		if (table.getSelection().length == 0)
			return;
		TableItem item1 = (TableItem) table.getSelection()[0];
		User user = (User) item1.getData();
		new UserDialog(manager, Constant.WindowType_Edit, user, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
	}

}
