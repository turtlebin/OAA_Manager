package gri.manager.ui.window;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import gri.driver.manager.GriDocManager;
import gri.manager.util.RunTimeData;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Combo;

public class QueryDialog extends Dialog {
	private Object result;
	private Shell shell;
	private Shell parentShell;
	private GriDocManager manager;
	private Text text_value;

	public QueryDialog(GriDocManager manager, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.manager = manager;
		RunTimeData.workingGriDocManager = manager;
	}

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

	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(392, 275);
		shell.setText("格文档查询");
		shell.setImage(new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/query16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 366, 180);

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 10, 43, 17);
		label.setText("属性：");

		final Combo combo_attr = new Combo(composite, SWT.READ_ONLY);
		combo_attr.setItems(new String[] { "名称", "关键词", "描述", "全文检索" });
		combo_attr.setBounds(69, 7, 88, 25);
		combo_attr.select(0);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(10, 60, 43, 17);
		label_1.setText("内容：");

		text_value = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text_value.setBounds(69, 57, 287, 105);

		Button button_query = new Button(shell, SWT.NONE);
		button_query.setBounds(250, 205, 60, 27);
		button_query.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String attr = combo_attr.getText();
				String value = text_value.getText().trim();
				if (attr.equals("") || value.equals("")) {
					MessageBox box = new MessageBox(shell);
					box.setMessage("请填写查询信息！");
					box.open();
					return;
				}
				RunTimeData.queryView = manager.query(attr, value);
				if (MainWindow.tabFolder_view != null) {
					MainWindow.tabFolder_view.setSelection(2);
				}
				if (MainWindow.treeViewer_queryView != null)
					MainWindow.treeViewer_queryView.refresh();
				shell.close();
			}
		});
		button_query.setText("查询");

		Button button_cancel = new Button(shell, SWT.NONE);
		button_cancel.setBounds(316, 205, 60, 27);
		button_cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_cancel.setText("取消");

	}

}
