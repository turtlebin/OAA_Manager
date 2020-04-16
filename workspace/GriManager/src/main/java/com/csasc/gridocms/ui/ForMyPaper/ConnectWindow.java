package com.csasc.gridocms.ui.ForMyPaper;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class ConnectWindow {

	protected Shell shell;
	private Text text;
	private Text txta_1;
	private Text txta;
	private Text text_2;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ConnectWindow window = new ConnectWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
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

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(359, 266);
		shell.setText("连接数格引擎");
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(26, 22, 78, 17);
		label.setText("数格引擎号：");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(110, 19, 122, 23);
		
		Button button = new Button(shell, SWT.NONE);
		button.setBounds(251, 17, 47, 27);
		button.setText("发现");
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(14, 55, 95, 17);
		label_1.setText("数格引擎名称：");
		
		txta_1 = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		txta_1.setText("数格引擎A");
		txta_1.setBounds(110, 50, 123, 23);
		
		txta = new Text(shell, SWT.BORDER);
		txta.setBounds(111, 105, 121, 23);
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(58, 140, 36, 17);
		lblNewLabel.setText("密码：");
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(46, 105, 47, 17);
		lblNewLabel_1.setText("用户名：");
		
		text_2 = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		text_2.setBounds(110, 134, 122, 23);
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.setBounds(110, 185, 47, 27);
		button_1.setText("连接");
		
		Button button_2 = new Button(shell, SWT.NONE);
		button_2.setText("取消");
		button_2.setBounds(185, 185, 47, 27);

	}
}
