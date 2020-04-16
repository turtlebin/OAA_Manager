package gri.manager.ui.window;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import org.eclipse.swt.SWT;

import gri.driver.model.AccessPermission;
import gri.manager.model.GlobalViewTreeNode;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

public class EditAccessPermissionDialog extends Dialog {
	private Object result;
	private Shell shell;
	private Shell parentShell;
	private GlobalViewTreeNode node;
	private AccessPermission accessPermission;
	private Button button_write;

	public EditAccessPermissionDialog(GlobalViewTreeNode node, AccessPermission accessPermission, Shell parent,
			int style) {
		super(parent, style);
		this.parentShell = parent;
		this.node = node;
		this.accessPermission = accessPermission;
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
		shell.setSize(292, 177);
		shell.setText("编辑用户权限");
		shell.setImage(new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/edit16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		Button button_ok = new Button(shell, SWT.NONE);
		button_ok.setBounds(62, 93, 60, 27);
		button_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				accessPermission.setReadOnly(!button_write.getSelection());
				if (node.manager.updateAccessPermission(accessPermission))
					GriDocDialog.tableViewer.refresh();
				close();
			}
		});
		button_ok.setText("确定");

		Button button_cancel = new Button(shell, SWT.NONE);
		button_cancel.setBounds(157, 93, 60, 27);
		button_cancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		button_cancel.setText("取消");

		Label label = new Label(shell, SWT.NONE);
		label.setBounds(35, 37, 36, 17);
		label.setText("权限：");

		Button button = new Button(shell, SWT.CHECK);
		button.setEnabled(false);
		button.setSelection(true);
		button.setBounds(107, 37, 46, 17);
		button.setText("读");

		this.button_write = new Button(shell, SWT.CHECK);
		button_write.setText("写");
		button_write.setBounds(175, 37, 46, 17);
		if (this.accessPermission.isReadOnly())
			button_write.setSelection(false);
		else
			button_write.setSelection(true);

	}

	public void close() {
		shell.close();
	}
}
