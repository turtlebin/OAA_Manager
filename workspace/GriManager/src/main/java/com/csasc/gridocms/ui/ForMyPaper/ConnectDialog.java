package com.csasc.gridocms.ui.ForMyPaper;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class ConnectDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ConnectDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
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
		shell.setSize(345, 251);
		shell.setText("连接数格引擎");
		
		Label label = new Label(shell, SWT.NONE);
		label.setText("数格引擎号：");
		label.setBounds(22, 15, 78, 17);
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(106, 12, 122, 23);
		
		Button button = new Button(shell, SWT.NONE);
		button.setText("发现");
		button.setBounds(247, 10, 47, 27);
		
		text_1 = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		text_1.setText("数格引擎A");
		text_1.setBounds(106, 43, 123, 23);
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("数格引擎名称：");
		label_1.setBounds(10, 48, 95, 17);
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setText("用户名：");
		label_2.setBounds(42, 98, 47, 17);
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(107, 98, 121, 23);
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setText("密码：");
		label_3.setBounds(54, 133, 36, 17);
		
		text_3 = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		text_3.setBounds(106, 127, 122, 23);
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.setText("连接");
		button_1.setBounds(106, 178, 47, 27);
		
		Button button_2 = new Button(shell, SWT.NONE);
		button_2.setText("取消");
		button_2.setBounds(181, 178, 47, 27);

	}

}
