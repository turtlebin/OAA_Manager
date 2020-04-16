package gri.manager.ui.window;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import gri.driver.model.AccessPermission;
import gri.driver.model.GriDoc;
import gri.driver.model.User;
import gri.manager.model.GlobalViewTreeNode;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;

import java.util.Collection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;

public class SelectUserDialog extends Dialog {
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof User) {
				User user = (User) element;
				if (columnIndex == 0) {
					return user.getAccount();
				} else if (columnIndex == 1) {
					return user.getNickname();
				}
			}
			return "";
		}
	}

	private class ContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Collection<?>) {
				Object[] objs = ((Collection<?>) inputElement).toArray(new Object[0]);
				return objs;
			} else
				return null;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	protected Object result;
	protected Shell shell;
	private Shell parentShell;
	private Table table;
	private GlobalViewTreeNode node;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectUserDialog(Shell parent, GlobalViewTreeNode node, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.node = node;
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
		shell.setSize(286, 294);
		shell.setText("添加用户权限");
		shell.setImage(new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/add16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);
		
				Label label_1 = new Label(shell, SWT.NONE);
				label_1.setBounds(10, 10, 48, 17);
				label_1.setText("用户：");

		TableViewer tableViewer = new TableViewer(shell, SWT.FULL_SELECTION);
		this.table = tableViewer.getTable();
		table.setBounds(71, 10, 165, 156);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		Label label = new Label(shell, SWT.NONE);
		label.setBounds(10, 180, 48, 17);
		label.setText("权限：");

		final Combo combo = new Combo(shell, SWT.READ_ONLY);
		combo.setItems(new String[] { "只读", "读写" });
		combo.setBounds(71, 177, 59, 25);
		combo.select(0);

		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelection().length == 0)
					return;
				TableItem item1 = (TableItem) table.getSelection()[0];

				User user = (User) item1.getData();

				boolean succeed = node.manager.addAccessPermission(new AccessPermission(((GriDoc) node.data).getId(),
						user.getId(), user.getAccount(), combo.getText().equals("只读")));

				if (succeed) {
					if (GriDocDialog.tableViewer != null)
						GriDocDialog.tableViewer.refresh();
				}
				shell.close();
			}
		});
		button.setBounds(71, 227, 59, 27);
		button.setText("确定");

		Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_1.setBounds(149, 227, 59, 27);
		button_1.setText("取消");

		String[] heards = new String[] { "用户", "昵称" };

		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tableColumn = column.getColumn();
		tableColumn.setResizable(false);
		column.getColumn().setText(heards[0]);
		column.getColumn().setWidth(74);

		TableViewerColumn column1 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tableColumn1 = column1.getColumn();
		tableColumn1.setResizable(false);
		column1.getColumn().setText(heards[1]);
		column1.getColumn().setWidth(68);

		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setInput(this.node.manager.UserManager().getAllUser());
	}
}
