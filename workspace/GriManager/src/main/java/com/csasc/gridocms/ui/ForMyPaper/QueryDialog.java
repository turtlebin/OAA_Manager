package com.csasc.gridocms.ui.ForMyPaper;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

public class QueryDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Shell parentShell;
	private Text text;
	private Text text_1;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public QueryDialog(Shell parent, int style) {
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
		shell.setSize(305, 311);
		shell.setText("查询");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox b = new MessageBox(shell, SWT.OK);
				b.setMessage("已经是最新版本！");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				b.open();
				return;
			}
		});
		btnNewButton.setBounds(170, 241, 60, 27);
		btnNewButton.setText("关闭");

		Button button_ok = new Button(shell, SWT.NONE);
		button_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_ok.setBounds(79, 241, 60, 27);
		button_ok.setText("查询");
		
		Button button = new Button(shell, SWT.RADIO);
		button.setBounds(10, 10, 97, 17);
		button.setText("按属性查询");
		
		Group group = new Group(shell, SWT.NONE);
		group.setBounds(10, 29, 279, 84);
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(10, 21, 36, 17);
		label.setText("属性：");
		
		Combo combo = new Combo(group, SWT.NONE);
		combo.setItems(new String[] {"全部", "名称", "关键词", "简介"});
		combo.setBounds(52, 17, 76, 25);
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setBounds(24, 54, 25, 17);
		label_1.setText("值：");
		
		text = new Text(group, SWT.BORDER);
		text.setBounds(52, 51, 180, 23);
		
		Button button_1 = new Button(shell, SWT.RADIO);
		button_1.setText("按内容查询");
		button_1.setBounds(10, 128, 97, 17);
		
		Group group_1 = new Group(shell, SWT.NONE);
		group_1.setBounds(10, 151, 279, 84);
		
		Label label_3 = new Label(group_1, SWT.NONE);
		label_3.setText("内容：");
		label_3.setBounds(10, 22, 36, 17);
		
		text_1 = new Text(group_1, SWT.BORDER | SWT.V_SCROLL);
		text_1.setBounds(52, 19, 215, 52);
		button_ok.setFocus();
	}
}
