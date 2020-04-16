package gri.manager.ui.window;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import csAsc.EIB.Engine.Authentication;
import csAsc.EIB.Engine.EITPLocation;
import csAsc.EIB.Engine.Engine;
import csAsc.EIB.Engine.Order;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;

public class SelectParagraphDialog extends Dialog {
	
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof String) {
				String name2id = (String) element;
				if (columnIndex == 0) {
					return name2id.split("&")[0];
				} else if (columnIndex == 1) {
					return name2id.split("&")[1];
				} else if (columnIndex == 2) {
					return lists.get(name2id);
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

	private String host;
	private String user;
	private String password;

	private Map<String, String> lists = new HashMap<String, String>();

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectParagraphDialog(Shell parent, String host, String user, String password, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.host = host;
		this.user = user;
		this.password = password;
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
	@SuppressWarnings("unchecked")
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(314, 294);
		shell.setText("选择段数据");
		shell.setImage(new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/add16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(10, 10, 48, 17);
		label_1.setText("段：");

		TableViewer tableViewer = new TableViewer(shell, SWT.FULL_SELECTION);
		this.table = tableViewer.getTable();
		table.setBounds(64, 10, 206, 186);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelection().length == 0)
					return;
				TableItem item1 = (TableItem) table.getSelection()[0];
				String name2id = (String) item1.getData();

				ParagraphDialog.text_paragraphDataName.setText(name2id.split("&")[1]);
				ParagraphDialog.text_paragraphDataID.setText(name2id.split("&")[0]);

				shell.close();
			}
		});
		button.setBounds(71, 228, 59, 27);
		button.setText("确定");

		Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_1.setBounds(179, 228, 59, 27);
		button_1.setText("取消");

		String[] heards = new String[] { "ID", "名称", "类型" };

		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tableColumn = column.getColumn();
		tableColumn.setResizable(false);
		column.getColumn().setText(heards[0]);
		column.getColumn().setWidth(66);

		TableViewerColumn column1 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tableColumn1 = column1.getColumn();
		tableColumn1.setResizable(false);
		column1.getColumn().setText(heards[1]);
		column1.getColumn().setWidth(66);

		TableViewerColumn column2 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tableColumn2 = column2.getColumn();
		tableColumn2.setResizable(false);
		column2.getColumn().setText(heards[2]);
		column2.getColumn().setWidth(66);

		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(new ContentProvider());

		Object result = sendRequest(this.host, this.user, this.password, "publisher", "list all paragraph", "");//此处调用sendRequest方法发送EITP请求
		if (result != null) {
			this.lists = (HashMap<String, String>) result;
			tableViewer.setInput(this.lists.keySet());
		}

	}

	// 发送EITP请求
	private Object sendRequest(String paragraphHost, String paragraphUser, String paragraphPasswrod,
			String paragraphEngineName, String operate, Object resquestData) {
		Order response = new Order(); // 响应结果
		Engine eitp_engine = new Engine();
		eitp_engine.start();

		// 发送请求
		int ret = eitp_engine.once(new EITPLocation(paragraphHost),
				new Authentication(Authentication.AT_BASIC, new String[] { paragraphUser, paragraphPasswrod }),
				new Order(paragraphEngineName, paragraphUser, operate, resquestData), response);

		eitp_engine.stop();

		if (ret == 0) {
			return response.data;
		} else {
			return null;
		}
	}

}
