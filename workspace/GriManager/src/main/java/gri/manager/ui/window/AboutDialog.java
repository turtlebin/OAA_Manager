package gri.manager.ui.window;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class AboutDialog extends Dialog {//关于对话框

	protected Object result;
	protected Shell shell;
	private Shell parentShell;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public AboutDialog(Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		setText("SWT Dialog");
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
		shell.setSize(326, 173);//shell相当于是一个窗口
		shell.setText("关于");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();//Returns a rectangle describing the receiver's size and location relative to its parent (or its display if its parent is null),
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,//使子窗口在父窗口中居中
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		Label lblgridocManager = new Label(shell, SWT.NONE);
		lblgridocManager.setBounds(46, 29, 253, 17);
		lblgridocManager.setText("格文档管理器（GriDoc Manager） v1.0");

		Button btnNewButton = new Button(shell, SWT.NONE);//new Button(parent,style)
		btnNewButton.addSelectionListener(new SelectionAdapter() {//检查更新按钮
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox b = new MessageBox(shell, SWT.OK);
				b.setMessage("已经是最新版本！");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				b.open();
				return;
			}
		});
		btnNewButton.setBounds(85, 95, 60, 27);
		btnNewButton.setText("检查更新");

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(67, 59, 219, 17);
		lblNewLabel.setText("华南理工大学华系研究所 版权所有");

		Button button_ok = new Button(shell, SWT.NONE);
		button_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_ok.setBounds(170, 95, 60, 27);
		button_ok.setText("确定");
		button_ok.setFocus();
	}
}
