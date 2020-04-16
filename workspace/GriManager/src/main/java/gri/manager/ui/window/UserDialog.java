package gri.manager.ui.window;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import gri.driver.manager.UserManager;
import gri.driver.model.User;
import gri.manager.util.Constant;
import gri.manager.util.RunTimeData;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

public class UserDialog extends Dialog {

	protected Object result;
	private Shell parentShell;
	protected Shell shell;
	public Text text_account;
	public Text text_password;
	public Text text_nickname;

	private UserManager manager;

	private int windowType;
	private User oldUser;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public UserDialog(UserManager manager, int windowType, User oldUser, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.manager = manager;
		this.windowType = windowType;
		this.oldUser = oldUser;
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
		shell.setSize(319, 220);
		shell.setText("新建用户");
		if (this.windowType == Constant.WindowType_Edit)
			shell.setText("编辑用户");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		Label label = new Label(shell, SWT.NONE);
		label.setBounds(38, 27, 36, 17);
		label.setText("帐号：");

		text_account = new Text(shell, SWT.BORDER);
		text_account.setBounds(90, 24, 137, 23);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(38, 60, 36, 17);
		label_1.setText("密码：");

		text_password = new Text(shell, SWT.BORDER);
		text_password.setBounds(90, 57, 137, 23);

		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(38, 94, 36, 17);
		label_2.setText("昵称：");

		text_nickname = new Text(shell, SWT.BORDER);
		text_nickname.setBounds(90, 91, 137, 23);

		Button button_save = new Button(shell, SWT.NONE);
		button_save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String account = text_account.getText().trim();
				String password = text_password.getText().trim();
				String nickname = text_nickname.getText().trim();
				if (account.equals("") || password.equals("") || nickname.equals("")) {
					MessageBox box = new MessageBox(shell, SWT.ICON_WARNING);
					box.setMessage("用户信息必须填写完整！");
					box.open();
				}

				if (windowType == Constant.WindowType_Add) {
					User user = manager.addUser(new User(account, password, nickname));
					if (user != null) {
						RunTimeData.userView.add(user);
						if (UserManageDialog.tableViewer != null)
							UserManageDialog.tableViewer.refresh();
						shell.close();
					}
				} else if (windowType == Constant.WindowType_Edit) {
					if (oldUser.getAccount().equals("root") && !account.equals("root")) {
						MessageBox box = new MessageBox(shell);
						box.setMessage("不能修改root账户名");
						box.open();
						return;
					}
					oldUser.setAccount(account);
					oldUser.setPassword(password);
					oldUser.setNickname(nickname);

					boolean succeed = manager.updateUser(oldUser);
					if (succeed) {
						if (UserManageDialog.tableViewer != null)
							UserManageDialog.tableViewer.refresh();
						shell.close();
					}
				}
			}
		});
		button_save.setBounds(90, 141, 56, 27);
		button_save.setText("确定");

		Button button_cancel = new Button(shell, SWT.NONE);
		button_cancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_cancel.setBounds(171, 141, 56, 27);
		button_cancel.setText("取消");
		if (this.windowType == Constant.WindowType_Edit)
			loadData();
	}

	private void loadData() {
		text_account.setText(this.oldUser.getAccount());
		text_password.setText(this.oldUser.getPassword());
		text_nickname.setText(this.oldUser.getNickname());
	}

}
