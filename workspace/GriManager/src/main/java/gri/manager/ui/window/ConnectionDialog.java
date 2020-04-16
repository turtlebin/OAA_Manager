package gri.manager.ui.window;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import gri.driver.manager.GriDocManager;
import gri.driver.model.Connection;
import gri.engine.util.DBHelper;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.util.Constant;
import gri.manager.util.RunTimeData;

public class ConnectionDialog extends Dialog {

	protected Object result;
	private int window_type;
	private GlobalViewTreeNode node; // 新建连接时为null
	protected Shell shell;
	private Shell parentShell;
	private Text txtEitp;
	private Text txtRoot;
	private Text txtRoot_1;
	private Text text_name;

	public ConnectionDialog(int window_type, GlobalViewTreeNode node, Shell parent, int style) {
		super(parent, style);
		this.window_type = window_type;
		this.parentShell = parent;
		this.node = node;//此处接受一个GlobalViewTreeNode参数和window_type
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

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(300, 348);
		shell.setText("数格引擎 - 新建连接");
		shell.setImage(
				new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/connection16.png")));// 设置图标

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 274, 266);

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("常规");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);

		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setBounds(10, 33, 42, 17);
		label_3.setText("名称：");

		text_name = new Text(composite, SWT.BORDER);//相当于一个EditText
		text_name.setBounds(61, 30, 184, 23);
		text_name.setText("新建连接");

		Label label = new Label(composite, SWT.NONE);	
		label.setBounds(10, 77, 42, 17);
		label.setText("地址：");

		txtEitp = new Text(composite, SWT.BORDER);
		txtEitp.setBounds(61, 74, 184, 23);
		txtEitp.setText("EITP://localhost:9020");

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(10, 122, 42, 17);
		label_1.setText("用户：");

		txtRoot = new Text(composite, SWT.BORDER);
		txtRoot.setBounds(61, 119, 184, 23);
		txtRoot.setText("root");

		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setBounds(10, 169, 42, 17);
		label_2.setText("密码：");

		txtRoot_1 = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		txtRoot_1.setBounds(61, 166, 184, 23);
		txtRoot_1.setText("root");

		Button button_connect = new Button(shell, SWT.NONE);
		button_connect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Connection con = null;
				if (window_type == Constant.WindowType_Edit) {
					con = (Connection) node.data;
				} else if (window_type == Constant.WindowType_Add) {
					con = new Connection(text_name.getText().trim(), txtEitp.getText().trim(),//连接到对应的数据库
							txtRoot.getText().trim(), txtRoot_1.getText().trim());
			}//判断是添加还是编辑，若是添加，获取Text中的数据新建连接
				String message = "";
				if (con.canConnect())
					message = "连接成功！";
				else
					message = "连接失败！";
				MessageBox box = new MessageBox(shell);
				box.setMessage(message);
				box.open();
			}
		});
		button_connect.setBounds(10, 282, 80, 27);
		button_connect.setText("连接测试");

		Button button_ok = new Button(shell, SWT.NONE);
		button_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = text_name.getText().trim();
				String host = txtEitp.getText().trim();
				String user = txtRoot.getText().trim();
				String password = txtRoot_1.getText().trim();
				Connection con = null;
				if (window_type == Constant.WindowType_Add) {
					con = new Connection(name, host.trim(), user, password);
					if(RunTimeData.globalView.size()==1) {
						showMessage("同一时间最多只能存在一个连接，可通过修改属性切换连接",shell);
						return;
					}
					RunTimeData.globalView.add(con);
					MainWindow.treeViewer_globalView.add("root",
							new GlobalViewTreeNode(con, null, new GriDocManager(con)));//在主界面添加一个连接
				} else if (window_type == Constant.WindowType_Edit) {
					con = (Connection) node.data;
					con.setName(name);
					con.setHost(host);
					con.setUser(user);
					con.setPassword(password);
					RunTimeData.globalView.remove(0);
					RunTimeData.globalView.add(con);
					MainWindow.treeViewer_globalView.refresh(node);
				}
				int success;
				if((success=validateSuccess(con))==0) {
				close();
				}else {
					if(success==-1) 
					{
						showMessage("出现未知错误，请确认gridoc数据库是否可正常连接",shell);
						return;
					}
					if(success==1) {
						showMessage("用户名不存在",shell);
						return;
					}
					if(success==2) {
						showMessage("用户名或密码错误，请确认是否输入正确",shell);
						return;
					}
					if(success==3) {
						showMessage("该用户不具有管理员权限，登录失败",shell);
						return;
					}
				}
			}
		});
		button_ok.setBounds(152, 282, 60, 27);
		button_ok.setText("确定");

		Button button_cancel = new Button(shell, SWT.NONE);
		button_cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		button_cancel.setBounds(224, 282, 60, 27);
		button_cancel.setText("取消");
		if (this.window_type == Constant.WindowType_Edit)
			this.loadData();
	}

	private void loadData() {
		shell.setText("数格引擎 - 连接属性");
		Connection con = (Connection) this.node.data;
		text_name.setText(con.getName());
		txtEitp.setText(con.getHost());
		txtRoot.setText(con.getUser());
		txtRoot_1.setText(con.getPassword());
	}

	public void close() {
		shell.close();
	}
	
	private void showMessage(String message, Shell shell) {
		MessageBox box = new MessageBox(shell);
		box.setMessage(message);
		box.open();
	}
	
	
//	private int validateSuccess(Connection conn) {
//	String user=conn.getUser();
//	String password=conn.getPassword();
//	java.sql.Connection connection=DBHelper.getConnection2();
//	String sql="select * from user where username=?";
//	Object[] obj=new Object[]{user};
//	PreparedStatement ps=null;
//	ResultSet rs=null;
//	int success=-1;//失败
//	try {
//		ps=connection.prepareStatement(sql);
//		rs=DBHelper.executeQuery(connection, ps, sql, obj);
//		int i=0;
//		String temp=null;
//		int identity=-1;
//		while(rs.next()) {
//			i++;
//			if((temp=rs.getString("password"))!=null&&temp.equals(password)&&((identity=rs.getInt("identity"))==4)) {
//				success=0;//成功
//			}
//		}
//		if(i==0) {
//			return (success=1);//用户名不存在
//		}
//		if(!temp.equals(password)) 
//		{
//			return (success=2);//密码不匹配用户名
//		}
//		if(identity!=4) {
//			return (success=3);//非管理员用户
//		}
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	return success;//-1时表明出现异常
//}
	
	private int validateSuccess(Connection conn) {
		String user=conn.getUser();
		String password=conn.getPassword();
		java.sql.Connection connection=DBHelper.getConnection();
		String sql="select * from user where account=?";
		Object[] obj=new Object[]{user};
		PreparedStatement ps=null;
		ResultSet rs=null;
		int success=-1;//失败
		try {
			ps=connection.prepareStatement(sql);
			rs=DBHelper.executeQuery(connection, ps, sql, obj);
			int i=0;
			String temp=null;
			while(rs.next()) {
				i++;
				if((temp=rs.getString("password"))!=null&&temp.equals(password)) {
					success=0;//成功
				}
			}
			if(i==0) {
				return (success=1);//用户名不存在
			}
			if(!temp.equals(password)) 
			{
				return (success=2);//密码不匹配用户名
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;//-1时表明出现异常
	}
}
