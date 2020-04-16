package gri.manager.ui.window;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import gri.driver.manager.ProcessManager;
import gri.driver.model.GriElement;
import gri.driver.model.process.Paragraph;
import gri.driver.model.process.Processor;
import gri.driver.util.DriverConstant;
import gri.engine.core.DataSyncTaskManager;
import gri.engine.integrate.Paragraph3;
import gri.engine.util.DBHelper;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.util.Constant;
import gri.manager.util.SQLParser;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;

public class ParagraphDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Shell parentShell;
	private int windowType;
	private GlobalViewTreeNode node;
	private Group group_db_connect;
	private TabFolder tabFolder_main;
	private ProcessManager manager;
	//
	private Combo combo_datasoureType;
	private Composite composite_file;
	private Composite composite_db;
	private Composite composite_web;
	private Composite composite_para;
	private Composite composite_gri;
	private Composite composite_view;

	private Text text_name;
	private Text text_keywords;
	private Text text_description;
	private Text text_db_port;

	private Text text_db_host;
	private Text text_db_name;
	private Text text_db_sql;
	private Text text_db_username;
	private Text text_db_password;
	private Combo combo_db_type;
	private Text text_filePath;
	private Text text_fileUser;
	private Text text_filePassword;

	private Text text_webURL;
	private Text text_webMethod;
	private Text text_webParam;

	private Button button_ok;
	private Button button_cancel;

	private Label label_updateDate;
	private Label label_dataSize;
	private Label label_syncDate;
	private Label label_syncState;
	private Text text_updateDate;
	private Text text_dataSize;
	private Text text_syncDate;
	private Text text_syncState;
	private Button button_test_ftp;

	private Text text_paragraphData_host;
	private Text text_paragraphData_user;
	private Text text_paragraphPassword;
	public static Text text_paragraphDataName;
	public static Text text_paragraphDataID;
	
	private Text text_griData_host;
	private Text text_griData_user;
	private Text text_griPassword;
	
	private Text text_viewData_host;
	private Text text_viewData_user;
	private Text text_viewPassword;
	private Text text_viewDataName;
	private Combo combo_viewProcessor;
	private List<Processor> processors;
	
	private Paragraph tempParagraph = new Paragraph("", "", "", "", "", "", "", ""); // windowType_Add

	private Text text_kind;
	private Text text_database;
    private Text text_IP;
    private Text text_Account;
    private Text text_Password;
    private Text text_Port;

	public static Integer griParagraphId;
	public static Text text_griDataName;
	
	private boolean addTableName=false;
	private Button check;
	private Button addTable;
	private Composite composite_6;

	
	public ParagraphDialog(int windowType,  GlobalViewTreeNode treeNode, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.node = treeNode;
		this.windowType = windowType;
		this.manager = treeNode.manager.ProcessManager();
	}

	public Object open() {
		createContents();
		if (parentShell != null)
			this.initWindow();
		if (this.windowType != Constant.WindowType_Add)
			this.loadData();
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
		shell = new Shell(getParent(), getStyle());
		shell.setSize(475, 766);
		if (parentShell != null)
			shell.setSize(475, 766 - 280);
		shell.setText("新建段");
		shell.setImage(
				new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/paragraph16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		tabFolder_main = new TabFolder(shell, SWT.NONE);
		tabFolder_main.setBounds(10, 10, 449, 686);

		TabItem tabItem_common = new TabItem(tabFolder_main, SWT.NONE);
		tabItem_common.setText("常规");

		Composite composite_3 = new Composite(tabFolder_main, SWT.NONE);
		tabItem_common.setControl(composite_3);

		Label label_2 = new Label(composite_3, SWT.NONE);
		label_2.setLocation(10, 28);
		label_2.setSize(36, 17);
		label_2.setText("名称：");

		text_name = new Text(composite_3, SWT.BORDER);
		text_name.setLocation(94, 28);
		text_name.setSize(323, 23);

		Label label_3 = new Label(composite_3, SWT.NONE);
		label_3.setLocation(10, 71);
		label_3.setSize(48, 17);
		label_3.setText("关键词：");

		text_keywords = new Text(composite_3, SWT.BORDER);
		text_keywords.setLocation(94, 68);
		text_keywords.setSize(323, 23);

		Label label_6 = new Label(composite_3, SWT.NONE);
		label_6.setLocation(10, 121);
		label_6.setSize(36, 17);
		label_6.setText("描述：");

		text_description = new Text(composite_3, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text_description.setLocation(94, 118);
		text_description.setSize(323, 69);

		label_updateDate = new Label(composite_3, SWT.NONE);
		label_updateDate.setBounds(10, 200, 61, 17);
		label_updateDate.setText("修改时间：");

		text_updateDate = new Text(composite_3, SWT.READ_ONLY);
		text_updateDate.setText("?");
		text_updateDate.setBounds(94, 200, 323, 23);

		label_dataSize = new Label(composite_3, SWT.NONE);
		label_dataSize.setText("数据大小：");
		label_dataSize.setBounds(10, 230, 61, 17);

		text_dataSize = new Text(composite_3, SWT.READ_ONLY);
		text_dataSize.setText("?");
		text_dataSize.setBounds(94, 230, 323, 23);

		label_syncDate = new Label(composite_3, SWT.NONE);
		label_syncDate.setText("同步时间：");
		label_syncDate.setBounds(10, 260, 61, 17);

		text_syncState = new Text(composite_3, SWT.READ_ONLY);
		text_syncState.setText("?");
		text_syncState.setBounds(94, 290, 323, 23);

		label_syncState = new Label(composite_3, SWT.NONE);
		label_syncState.setText("同步状态：");
		label_syncState.setBounds(10, 290, 61, 17);

		text_syncDate = new Text(composite_3, SWT.READ_ONLY);
		text_syncDate.setText("?");
		text_syncDate.setBounds(94, 260, 323, 23);

		TabItem tabItem_data = new TabItem(tabFolder_main, SWT.NONE);
		tabItem_data.setText("数据源");

		Composite composite_4 = new Composite(tabFolder_main, SWT.NONE);
		tabItem_data.setControl(composite_4);

		Label label = new Label(composite_4, SWT.NONE);
		label.setBounds(10, 13, 60, 17);
		label.setText("数据来源：");

//		combo_datasoureType = new Combo(composite_4, SWT.READ_ONLY);
//		combo_datasoureType.setBounds(76, 10, 88, 25);
//		combo_datasoureType.setItems(new String[] { "文件", "数据库", "Web服务", "段引擎" , "数格引擎", "视图"});
//		combo_datasoureType.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				if (combo_datasoureType.getSelectionIndex() == 0) composite_file.setVisible(true);
//				else composite_file.setVisible(false);
//				
//				if (combo_datasoureType.getSelectionIndex() == 1) composite_db.setVisible(true);
//				else composite_db.setVisible(false);
//				
//				if (combo_datasoureType.getSelectionIndex() == 2) composite_web.setVisible(true);
//				else composite_web.setVisible(false);
//				
//				if (combo_datasoureType.getSelectionIndex() == 3) composite_para.setVisible(true);
//				else composite_para.setVisible(false);
//				
//				if (combo_datasoureType.getSelectionIndex() == 4) composite_gri.setVisible(true);
//				else composite_gri.setVisible(false);
//				
//				if (combo_datasoureType.getSelectionIndex() == 5) composite_view.setVisible(true);
//				else composite_view.setVisible(false);
//			}
//		});

		combo_datasoureType = new Combo(composite_4, SWT.READ_ONLY);
		combo_datasoureType.setBounds(76, 10, 88, 25);
		combo_datasoureType.setItems(new String[] { "数据库"});
		combo_datasoureType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (combo_datasoureType.getSelectionIndex() == 0) composite_db.setVisible(true);
				else composite_db.setVisible(false);
				

			}
		});

		
		Button button_syncConfig = new Button(composite_4, SWT.NONE);
		button_syncConfig.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (windowType == Constant.WindowType_Add)
					new SyncConfigDialog(tempParagraph, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
				else
					new SyncConfigDialog((Paragraph) node.data, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		});
		button_syncConfig.setBounds(228, 8, 80, 27);
		button_syncConfig.setText("同步配置");

		composite_file = new Composite(composite_4, SWT.NONE);
		composite_file.setBounds(10, 41, 421, 118);

		Label label_5 = new Label(composite_file, SWT.NONE);
		label_5.setBounds(10, 10, 61, 17);
		label_5.setText("文件路径：");

		text_filePath = new Text(composite_file, SWT.BORDER);
		text_filePath.setBounds(129, 10, 282, 23);

		Label label_8 = new Label(composite_file, SWT.NONE);
		label_8.setBounds(10, 46, 61, 17);
		label_8.setText("用户名：");

		text_fileUser = new Text(composite_file, SWT.BORDER);
		text_fileUser.setBounds(129, 46, 102, 23);

		Label label_13 = new Label(composite_file, SWT.NONE);
		label_13.setBounds(10, 82, 61, 17);
		label_13.setText("密码：");

		text_filePassword = new Text(composite_file, SWT.BORDER);
		text_filePassword.setBounds(129, 82, 102, 23);

		Label label_16 = new Label(composite_file, SWT.NONE);
		label_16.setBounds(237, 49, 61, 17);
		label_16.setText("必要时填写");

		Label label_17 = new Label(composite_file, SWT.NONE);
		label_17.setBounds(237, 85, 61, 17);
		label_17.setText("必要时填写");

		button_test_ftp = new Button(composite_file, SWT.NONE);
		button_test_ftp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = text_filePath.getText().trim();
				String user = text_fileUser.getText().trim();
				String password = text_filePassword.getText().trim();

				if (path.equals("")) {
					MessageBox box = new MessageBox(shell);
					box.setMessage("请填写完整文件连接信息！");
					box.open();
					return;
				}

				String dataSourceConnectPath = DriverConstant.DataSourceType_File + "###" + path + "###" + user + "###"
						+ password;
				MessageBox box = new MessageBox(shell);
				box.setMessage(node.manager.testDataSourceConnect(dataSourceConnectPath) ? "文件服务器连接成功！" : "文件服务器连接失败！");
				box.open();
			}
		});
		button_test_ftp.setText("测试连接");
		button_test_ftp.setBounds(338, 81, 73, 27);

		composite_web = new Composite(composite_4, SWT.NONE);
		composite_web.setBounds(10, 41, 421, 120);

		Label lblWeb = new Label(composite_web, SWT.NONE);
		lblWeb.setBounds(10, 13, 87, 17);
		lblWeb.setText("Web服务地址：");

		text_webURL = new Text(composite_web, SWT.BORDER);
		text_webURL.setBounds(130, 10, 281, 23);
		
		Label lblMethod = new Label(composite_web, SWT.NONE);
		lblMethod.setBounds(10, 46, 87, 17);
		lblMethod.setText("方法名：");

		text_webMethod = new Text(composite_web, SWT.BORDER);
		text_webMethod.setBounds(130, 43, 281, 23);
		
		Label lblWebParam = new Label(composite_web, SWT.NONE);
		lblWebParam.setBounds(10, 79, 87, 17);
		lblWebParam.setText("参数（用,分割）：");

		text_webParam = new Text(composite_web, SWT.BORDER);
		text_webParam.setBounds(130, 76, 281, 23);

		composite_para = new Composite(composite_4, SWT.NONE);
		composite_para.setBounds(10, 41, 421, 153);

		Label label_18 = new Label(composite_para, SWT.NONE);
		label_18.setBounds(10, 13, 95, 17);
		label_18.setText("段引擎地址：");

		text_paragraphData_host = new Text(composite_para, SWT.BORDER);
		text_paragraphData_host.setBounds(131, 10, 215, 23);

		text_paragraphData_user = new Text(composite_para, SWT.BORDER);
		text_paragraphData_user.setBounds(131, 44, 215, 23);

		Label label_27 = new Label(composite_para, SWT.NONE);
		label_27.setText("用户名：");
		label_27.setBounds(10, 47, 95, 17);

		text_paragraphPassword = new Text(composite_para, SWT.BORDER);
		text_paragraphPassword.setBounds(131, 81, 215, 23);

		Label label_28 = new Label(composite_para, SWT.NONE);
		label_28.setText("密码：");
		label_28.setBounds(10, 84, 36, 17);

		text_paragraphDataName = new Text(composite_para, SWT.BORDER | SWT.READ_ONLY);
		text_paragraphDataName.setBounds(131, 118, 215, 23);

		Label label_30 = new Label(composite_para, SWT.NONE);
		label_30.setText("段数据名：");
		label_30.setBounds(10, 121, 95, 17);

		Button button_selectParagraph = new Button(composite_para, SWT.NONE);
		button_selectParagraph.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String host = text_paragraphData_host.getText().trim();
				String user = text_paragraphData_user.getText().trim();
				String password = text_paragraphPassword.getText().trim();

				if ("".equals(host) || "".equals(user) || "".equals(password))
					return;
				new SelectParagraphDialog(shell, host, user, password, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		});
		button_selectParagraph.setBounds(352, 116, 59, 27);
		button_selectParagraph.setText("选择");

		text_paragraphDataID = new Text(composite_para, SWT.BORDER);
		text_paragraphDataID.setBounds(49, 92, 73, 23);
		text_paragraphDataID.setVisible(false);
		
		// 数格引擎的界面
		composite_gri = new Composite(composite_4, SWT.NONE);
		composite_gri.setBounds(10, 41, 421, 153);

		Label label_51 = new Label(composite_gri, SWT.NONE);
		label_51.setBounds(10, 13, 95, 17);
		label_51.setText("数格引擎地址：");

		text_griData_host = new Text(composite_gri, SWT.BORDER);
		text_griData_host.setBounds(131, 10, 215, 23);
		//text_griData_host.setText("localhost:9020");

		Label label_52 = new Label(composite_gri, SWT.NONE);
		label_52.setText("用户名：");
		label_52.setBounds(10, 47, 95, 17);

		text_griData_user = new Text(composite_gri, SWT.BORDER);
		text_griData_user.setBounds(131, 44, 215, 23);
		//text_griData_user.setText("root");
		
		Label label_53 = new Label(composite_gri, SWT.NONE);
		label_53.setText("密码：");
		label_53.setBounds(10, 84, 36, 17);
		
		text_griPassword = new Text(composite_gri, SWT.BORDER);
		text_griPassword.setBounds(131, 81, 215, 23);
		//text_griPassword.setText("root");

		Label label_54 = new Label(composite_gri, SWT.NONE);
		label_54.setText("数据元素：");
		label_54.setBounds(10, 121, 95, 17);
		
		text_griDataName = new Text(composite_gri, SWT.BORDER);
		text_griDataName.setBounds(131, 118, 215, 23);
		text_griDataName.setEditable(false);

		Button button_selectGri = new Button(composite_gri, SWT.NONE);
		button_selectGri.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String host = text_griData_host.getText().trim();
				String user = text_griData_user.getText().trim();
				String password = text_griPassword.getText().trim();

				if ("".equals(host) || "".equals(user) || "".equals(password)){
					MessageBox box = new MessageBox(shell, SWT.ICON_WARNING);
					box.setMessage("选择前需要填好数格引擎信息！");
					box.open();
					return;
				}
				new SelectGriDialog(shell, host, user, password, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}
		});
		button_selectGri.setBounds(352, 116, 59, 27);
		button_selectGri.setText("选择");
		
		// 视图的界面
		composite_view = new Composite(composite_4, SWT.NONE);
		composite_view.setBounds(10, 41, 421, 190);

		Label label_61 = new Label(composite_view, SWT.NONE);
		label_61.setBounds(10, 13, 95, 17);
		label_61.setText("数格引擎地址：");

		text_viewData_host = new Text(composite_view, SWT.BORDER);
		text_viewData_host.setBounds(131, 10, 215, 23);
		//text_griData_host.setText("localhost:9020");

		Label label_62 = new Label(composite_view, SWT.NONE);
		label_62.setText("用户名：");
		label_62.setBounds(10, 47, 95, 17);

		text_viewData_user = new Text(composite_view, SWT.BORDER);
		text_viewData_user.setBounds(131, 44, 215, 23);
		//text_griData_user.setText("root");
		
		Label label_63 = new Label(composite_view, SWT.NONE);
		label_63.setText("密码：");
		label_63.setBounds(10, 84, 36, 17);
		
		text_viewPassword = new Text(composite_view, SWT.BORDER);
		text_viewPassword.setBounds(131, 81, 215, 23);
		//text_griPassword.setText("root");

		Label label_64 = new Label(composite_view, SWT.NONE);
		label_64.setText("视图：");
		label_64.setBounds(10, 121, 95, 17);
		
		text_viewDataName = new Text(composite_view, SWT.BORDER);
		text_viewDataName.setBounds(131, 118, 215, 23);
		text_viewDataName.setEditable(false);

		Button button_selectView = new Button(composite_view, SWT.NONE);
		button_selectView.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String host = text_viewData_host.getText().trim();
				String user = text_viewData_user.getText().trim();
				String password = text_viewPassword.getText().trim();

				if ("".equals(host) || "".equals(user) || "".equals(password)){
					MessageBox box = new MessageBox(shell, SWT.ICON_WARNING);
					box.setMessage("选择前需要填好数格引擎信息！");
					box.open();
					return;
				}
				String sourceText = (String) new SelectViewDialog(shell, host, user, password, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
				if(sourceText!=null) text_viewDataName.setText(sourceText);
			}
		});
		button_selectView.setBounds(352, 116, 59, 27);
		button_selectView.setText("选择");
		
		Label label_65 = new Label(composite_view, SWT.NONE);
		label_65.setText("前置处理器：");
		label_65.setBounds(10, 158, 95, 17);
		
		combo_viewProcessor = new Combo(composite_view, SWT.NONE);
		combo_viewProcessor.setBounds(131, 155, 215, 23);
		processors=manager.listProcessor();
		String[] processorNames = new String[processors.size()+1];
		for(int i = 0;i<processors.size();i++)
			processorNames[i]=processors.get(i).getName();
		processorNames[processors.size()] = "无";
		combo_viewProcessor.setItems(processorNames);
		combo_viewProcessor.select(processors.size());
		
		// db的界面
		
		composite_db = new Composite(composite_4, SWT.NONE);
		composite_db.setBounds(10, 41, 421, 272);

		this.group_db_connect = new Group(composite_db, SWT.NONE);
		group_db_connect.setLocation(10, 10);
		group_db_connect.setSize(401, 173);
		group_db_connect.setText("连接属性");

		Label lblDb = new Label(group_db_connect, SWT.NONE);
		lblDb.setText("类型：");
		lblDb.setBounds(10, 26, 36, 17);

		combo_db_type = new Combo(group_db_connect, SWT.NONE);
		combo_db_type.setItems(new String[] { "MySQL", "SQL Server", "Oracle","Sybase" });
		combo_db_type.setBounds(83, 23, 109, 25);
		combo_db_type.select(3);

		Label label_10 = new Label(group_db_connect, SWT.NONE);
		label_10.setText("地址：");
		label_10.setBounds(10, 56, 36, 17);

		text_db_host = new Text(group_db_connect, SWT.BORDER);
		text_db_host.setBounds(83, 53, 109, 23);
		text_db_host.setText("210.38.176.111");

		Label label_11 = new Label(group_db_connect, SWT.NONE);
		label_11.setText("端口：");
		label_11.setBounds(208, 56, 36, 17);

		text_db_port = new Text(group_db_connect, SWT.BORDER);
		text_db_port.setBounds(250, 53, 47, 23);
		text_db_port.setText("5000");

		Label label_12 = new Label(group_db_connect, SWT.NONE);
		label_12.setText("数据库：");
		label_12.setBounds(10, 82, 47, 17);

		text_db_name = new Text(group_db_connect, SWT.BORDER);
		text_db_name.setBounds(83, 79, 109, 23);
		text_db_name.setText("sulcmis");

		Label label_14 = new Label(group_db_connect, SWT.NONE);
		label_14.setText("用户名：");
		label_14.setBounds(10, 110, 47, 17);

		text_db_username = new Text(group_db_connect, SWT.BORDER);
		text_db_username.setBounds(83, 107, 109, 23);
		text_db_username.setText("xxjs");

		Label label_15 = new Label(group_db_connect, SWT.NONE);
		label_15.setText("密码：");
		label_15.setBounds(10, 139, 36, 17);

		text_db_password = new Text(group_db_connect, SWT.BORDER | SWT.PASSWORD);
		text_db_password.setBounds(83, 136, 109, 23);
		text_db_password.setText("123456");

		Button button_test_db_connect = new Button(group_db_connect, SWT.NONE);
		button_test_db_connect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String type = combo_db_type.getText().trim();
				String host = text_db_host.getText().trim();
				String port = text_db_port.getText().trim();
				String dbName = text_db_name.getText().trim();
				String user = text_db_username.getText().trim();
				String password = text_db_password.getText().trim();

				if (type.equals("") || host.equals("") || port.equals("") || dbName.equals("") || user.equals("")
						|| password.equals("")) {
					MessageBox box = new MessageBox(shell);
					box.setMessage("请填写完整数据库连接信息！");
					box.open();
					return;
				}

				String dataSourceConnectPath = DriverConstant.DataSourceType_Database + "###" + type + "###" + host
						+ "###" + port + "###" + dbName + "###" + user + "###" + password;
				MessageBox box = new MessageBox(shell);
				box.setMessage(node.manager.testDataSourceConnect(dataSourceConnectPath) ? "数据库连接成功！" : "数据库连接失败！");
				box.open();
			}
		});
		button_test_db_connect.setText("测试连接");
		button_test_db_connect.setBounds(318, 134, 73, 27);

		Group group_db_data = new Group(composite_db, SWT.NONE);
		group_db_data.setLocation(10, 189);
		group_db_data.setSize(401, 74);
		group_db_data.setText("数据属性");

		Label lblSql = new Label(group_db_data, SWT.NONE);
		lblSql.setBounds(10, 21, 54, 17);
		lblSql.setText("SQL语句：");

		text_db_sql = new Text(group_db_data, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		text_db_sql.setBounds(83, 18, 308, 46);
        
		TabItem tabItem_materialize = new TabItem(tabFolder_main, SWT.NONE);
		tabItem_materialize.setText("物化");
		
		Composite composite_5 = new Composite(tabFolder_main, SWT.NONE);
		tabItem_materialize.setControl(composite_5);
		
		composite_6 = new Composite(composite_5, SWT.NONE);
		composite_6.setBounds(0, 25, 300, 250);
		composite_6.setVisible(false);
		// 目标IP，端口号，账号，密码，目标数据库名，目标数据库种类
		Label dataDest = new Label(composite_6, SWT.None);
		dataDest.setText("目标数据库种类：");
		dataDest.setBounds(0, 0, 50, 50);

		  text_kind=new Text(composite_6,SWT.BORDER);
		  text_kind.setBounds(120, 0, 150, 20);
		  text_kind.setText("mysql");
		  
		  Label dataBase=new Label(composite_6,SWT.NONE);
		  dataBase.setText("目标数据库： ");
		  dataBase.setBounds(0, 30, 50, 50);
		  
		  text_database=new Text(composite_6,SWT.BORDER);
		  text_database.setBounds(120, 30, 150, 20);
		  text_database.setText("zqxydb2");
		  
		  Label dataIP=new Label(composite_6,SWT.NONE);
		  dataIP.setText("目标IP地址： ");
		  dataIP.setBounds(0, 60, 50, 50);
		  
		  text_IP=new Text(composite_6,SWT.BORDER);
		  text_IP.setBounds(120, 60, 150, 20);
		  text_IP.setText("localhost");
		  
		  Label dataAccount=new Label(composite_6,SWT.NONE);
		  dataAccount.setText("用户名： ");
		  dataAccount.setBounds(0, 90, 50, 50);
		  
		  text_Account=new Text(composite_6,SWT.BORDER);
		  text_Account.setBounds(120, 90, 150, 20);
		  text_Account.setText("root");
		  
		  Label dataPassword=new Label(composite_6,SWT.NONE);
		  dataPassword.setText("密码： ");
		  dataPassword.setBounds(0, 120, 50, 50);
		  
		  text_Password=new Text(composite_6,SWT.BORDER);
		  text_Password.setBounds(120, 120, 150, 20);
		  text_Password.setText("csasc");
		  
		  Label dataPort=new Label(composite_6,SWT.NONE);
		  dataPort.setText("端口号： ");
		  dataPort.setBounds(0, 150, 50, 50);
		  
		  text_Port=new Text(composite_6,SWT.BORDER);
		  text_Port.setBounds(120, 150, 150, 20);
		  text_Port.setText("3306");
		  
		    addTable=new Button(composite_5,SWT.CHECK);
			addTable.setLocation(100, 0);
			addTable.setText("是否添加表名");
			addTable.setVisible(false);
		    addTable.addSelectionListener(new SelectionAdapter(){
		    	@Override
		    	public void widgetSelected(SelectionEvent e){
		    		if(addTable.getSelection()){
		    			addTableName=true;
		    		}else{
		    			addTableName=false;
		    		}
		    	}
		    });
		    
		    check=new Button(composite_5,SWT.CHECK);
			check.setText("是否物化");
			check.setToolTipText("是否物化");
			check.addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent e){
					if(check.getSelection()){
						composite_6.setVisible(true);
						addTable.setVisible(true);
					}else{
						composite_6.setVisible(false);
						addTable.setVisible(false);
					}
				}
			});
			
			
		    addTable.pack();
			check.pack();
			dataDest.pack();
			dataBase.pack();
			dataIP.pack();
			dataAccount.pack();
		    dataPassword.pack();
		    dataPort.pack();
		  tabFolder_main.pack();
		button_ok = new Button(shell, SWT.NONE);
		button_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = text_name.getText().trim();
				if (name.equals("")) {
					showMessage("段名称不能为空！", shell);
					return;
				}
				String keywords = text_keywords.getText().trim();
				String description = text_description.getText().trim();
				String dataSourceType = "";
				String dataSourcePath = "";
				String dataDestType= "";
				String dataDestBase= "";
				String dataDestIP= "";
				String dataDestAccount= "";
				String dataDestPassword= "";
				String dataDestPort= "";
 
				// 数据源
				switch (combo_datasoureType.getSelectionIndex()) {
//				case 0:
//					// 文件
//					dataSourceType = DriverConstant.DataSourceType_File;
//					String filePath = text_filePath.getText().trim();
//					if (filePath.equals("")) {
//						showMessage("文件路径不能为空！", shell);
//						return;
//					}
//					dataSourcePath = filePath + "###" + text_fileUser.getText().trim() + "###"
//							+ text_filePassword.getText().trim();
//					break;
				case 0:
					// 数据库
					dataSourceType = DriverConstant.DataSourceType_Database;
					String type = combo_db_type.getText();
					String host = text_db_host.getText().trim();
					String port = text_db_port.getText().trim();
					String db_name = text_db_name.getText().trim();
					String user = text_db_username.getText().trim();
					String pass = text_db_password.getText().trim();
					String sql = text_db_sql.getText().trim();
					System.out.println(sql);
					if (type.equals("") || host.equals("") || port.equals("") || db_name.equals("") || user.equals("")
							|| pass.equals("") || sql.equals("")) {
						showMessage("请填写完整数据库信息！", shell);
						return;
					}
					if (!SQLParser.isSelectSQL(sql)) {
						showMessage("请填写正确的SQL select语句！", shell);
						return;
					}
					dataSourcePath = type + "###" + host + "###" + port + "###" + db_name + "###" + user + "###" + pass
							+ "###" + sql;
					break;
//				case 2:
//					// Web服务
//					dataSourceType = DriverConstant.DataSourceType_WebService;
//					String webPath = text_webURL.getText().trim();
//					String webMethod = text_webMethod.getText().trim();
//					String webParam = text_webParam.getText().trim();
//					if (webPath.equals("") || webMethod.equals("")) {
//						showMessage("Web服务路径、方法不能为空！", shell);
//						return;
//					}
//					dataSourcePath = webPath+"###"+webMethod+"###"+webParam; // TODO
//					break;
//				case 3:
//					// 段引擎数据
//					dataSourceType = DriverConstant.DataSourceType_ParagraphEngine;
//					String paraHost = text_paragraphData_host.getText().trim();
//					String paraUser = text_paragraphData_user.getText().trim();
//					String paraPassword = text_paragraphPassword.getText().trim();
//					String paraName = text_paragraphDataName.getText().trim();
//					String paraID = text_paragraphDataID.getText().trim();
//
//					if (paraHost.equals("") || paraUser.equals("") || paraPassword.equals("") || paraName.equals("")
//							|| paraID.equals("")) {
//						showMessage("段数据不能为空！", shell);
//						return;
//					}
//					dataSourcePath = paraHost + "###" + paraUser + "###" + paraPassword + "###" + paraName + "###"
//							+ paraID;
//					break;
//				case 4:
//					// 数格引擎数据
//					dataSourceType = DriverConstant.DataSourceType_GriEngine;
//					String griHost = text_griData_host.getText().trim();
//					String griUser = text_griData_user.getText().trim();
//					String griPassword = text_griPassword.getText().trim();
//					String sourceText = text_griDataName.getText().trim();;
//
//					if (griHost.equals("") || griUser.equals("") || griPassword.equals("") || griParagraphId == null) {
//						showMessage("数据信息不能为空！", shell);
//						return;
//					}
//					dataSourcePath = griHost + "###" + griUser + "###" + griPassword + "###" + griParagraphId + "###" + sourceText;
//					break;
//				case 5:
//					// 数格引擎数据
//					dataSourceType = DriverConstant.DataSourceType_View;
//					String viewHost = text_viewData_host.getText().trim();
//					String viewUser = text_viewData_user.getText().trim();
//					String viewPassword = text_viewPassword.getText().trim();
//					String viewSource = text_viewDataName.getText().trim();
//
//					if (viewHost.equals("") || viewUser.equals("") || viewPassword.equals("") ||viewSource.equals("")) {
//						showMessage("数据信息不能为空！", shell);
//						return;
//					}
//					
//					int processorId = 0;
//					if(combo_viewProcessor.getSelectionIndex()<processors.size())
//						processorId = processors.get(combo_viewProcessor.getSelectionIndex()).getId();
//					
//					dataSourcePath = viewHost + "###" + viewUser + "###" + viewPassword + "###"  + viewSource + "###"  +processorId;
//					break;
				default:
					showMessage("请选择数据源并填写详细信息！", shell);
					return;
				}
				if(check.getSelection()){
					dataDestType=text_kind.getText().trim();
					dataDestBase=text_database.getText().trim();
					dataDestIP=text_IP.getText().trim();
					dataDestAccount=text_Account.getText().trim();
					dataDestPassword=text_Password.getText().trim();
					dataDestPort=text_Port.getText().trim();
					
					if(dataDestType.equals("")||dataDestBase.equals("")||dataDestIP.equals("")||dataDestAccount.equals("")||dataDestPassword.equals("")||dataDestPort.equals("")){
						MessageBox box=new MessageBox(shell);
						box.setMessage("所有字段不能为空");
						box.open();
						return;
					}
				}
				if (windowType == Constant.WindowType_Add) {//如果是新建段
					if (tempParagraph.getSyncDirectionType().equals("") || tempParagraph.getSyncTimeType().equals("")) {
						showMessage("请填写同步配置！", shell);
						return;
					}
					if (tempParagraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_1)
							&& tempParagraph.getWarmSyncDetail().equals("")) {
						showMessage("请填写温同步完整时间配置！", shell);
						return;
					}
					// 执行创建
					GriElement paragraph = node.manager.addGriElement(node.root, (GriElement) node.data,
							new Paragraph(name, keywords, description, tempParagraph.getSyncTimeType(),
									tempParagraph.getSyncDirectionType(), tempParagraph.getWarmSyncDetail(),dataSourceType, dataSourcePath, dataDestType, dataDestBase, dataDestPort, dataDestAccount, dataDestPassword, dataDestIP),addTableName);//此处返回了paragraph，并且实际上已经执行了setID方法
				
					if (paragraph == null)
						showMessage("段创建失败！", shell);
					else {
						boolean result = node.manager.forceSyncData(paragraph.getId());//强制同步数据,如果采用上面注释段部分的代码进行同步，
						//则会同时在引擎和管理器执行同一个同步方法，引擎中同步方法的类锁对不同包的类调用无效，因此必须得使用这种方法进行调用
						if(result)
						{
							MainWindow.treeViewer_globalView.add(node,
									new GlobalViewTreeNode(paragraph, node.root, node.manager));// 最后在树节点中添加段
						}
//						else 
//						{
//							String sql = "delete from paragraph where id=?";
//							Object[] obj = new Object[] { paragraph.getId() };
//							if (DBHelper.executeNonQuery(sql, obj) > 0) {
//								if (((Paragraph)paragraph).getSyncTimeType().equals(DriverConstant.SyncTimeType_1)) {
//									DataSyncTaskManager dataSyncTaskManager = new DataSyncTaskManager();
//									dataSyncTaskManager.init();
//									dataSyncTaskManager.shutdown();
//									dataSyncTaskManager.addAllTask();
//									dataSyncTaskManager.addAllTask2();
//									dataSyncTaskManager.addAllTask3();
//								}
//							}
//						}
						shell.close();
					}
					//System.out.println(((Paragraph)paragraph).getDataDestType());
				} else if (windowType == Constant.WindowType_Edit) {//如果是编辑段
					// 执行修改
//					Paragraph paragraph = (Paragraph) node.data;
//					paragraph.setName(name);
//					paragraph.setKeywords(keywords);
//					paragraph.setDescription(description);
//					paragraph.setDataSourceType(dataSourceType);
//					paragraph.setDataSourcePath(dataSourcePath);
//					paragraph.setDataDestbase(dataDestBase);
//					paragraph.setDataDestType(dataDestType);
//					paragraph.setDestAccount(dataDestAccount);
//					paragraph.setDestPassword(dataDestPassword);
//					paragraph.setDestIP(dataDestIP);
//					paragraph.setDestPort(dataDestPort);
//					
//					if (!node.manager.updateGriElement(node.root, paragraph))
//						showMessage("段修改失败！", shell);
//					else {
//						MainWindow.treeViewer_globalView.refresh(node);
//						shell.close();
//					}
					showMessage("Hello!",shell);//不提供编辑段功能，若要编辑段，删除同名段后重新集成
					shell.close();
				}

			}
		});
		button_ok.setBounds(278, 702, 80, 27);
		button_ok.setText("确定");

		button_cancel = new Button(shell, SWT.NONE);
		button_cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_cancel.setBounds(379, 702, 80, 27);
		button_cancel.setText("取消");

	}

	private void loadData() {//如果是编辑段，则加载数据
		button_ok.setVisible(false);
		button_cancel.setText("关闭");
		shell.setText("段属性");
		Paragraph paragraph = (Paragraph) node.data;
		System.out.println(paragraph.getDataDestbase());
		label_updateDate.setVisible(true);
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// 修改时间
		text_updateDate.setText(df.format(paragraph.getUpdateTime()));
		text_updateDate.setVisible(true);

		label_dataSize.setVisible(true);
		text_dataSize.setVisible(true);
		if (paragraph.getDataSize() != null)
			text_dataSize.setText(paragraph.getDataSize() + " 字节");

		label_syncDate.setVisible(true);
		text_syncDate.setVisible(true);
		if (paragraph.getLastSyncTime() != null)
			text_syncDate.setText(df.format(paragraph.getLastSyncTime()));

		label_syncState.setVisible(true);
		text_syncState.setVisible(true);
		if (paragraph.getLastSyncTime() != null) {
			if (paragraph.getLastSyncSucceed())
				text_syncState.setText("成功");
			else
				text_syncState.setText("失败");
		}

		text_name.setText(paragraph.getName());
		text_keywords.setText(paragraph.getKeywords());
		text_description.setText(paragraph.getDescription());
		// 数据源
//		if (paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_File)) {
//			combo_datasoureType.select(0);
//			composite_file.setVisible(true);
//			String[] file_info = paragraph.getDataSourcePath().split("###");
//			text_filePath.setText(file_info[0]);
//			if (file_info.length > 1)
//				text_fileUser.setText(file_info[1]);
//			if (file_info.length > 2)
//				text_filePassword.setText(file_info[2]);
//		} else
			if (paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_Database)) {
			combo_datasoureType.select(0);
			composite_db.setVisible(true);
			String[] db_conn_info = paragraph.getDataSourcePath().split("###");
			combo_db_type.setText(db_conn_info[0]);
			text_db_host.setText(db_conn_info[1]);
			text_db_port.setText(db_conn_info[2]);
			text_db_name.setText(db_conn_info[3]);
			text_db_username.setText(db_conn_info[4]);
			text_db_password.setText(db_conn_info[5]);
			text_db_sql.setText(db_conn_info[6]);
			
//		} else if (paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_WebService)) {
//			combo_datasoureType.select(2);
//			composite_web.setVisible(true);
//			String[] web_info = paragraph.getDataSourcePath().split("###");
//			text_webURL.setText(web_info[0]);
//			text_webMethod.setText(web_info[1]);
//			text_webParam.setText(web_info[2]);
//			// TODO
//		} else if (paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_ParagraphEngine)) {
//			combo_datasoureType.select(3);
//			composite_para.setVisible(true);
//			String[] para_info = paragraph.getDataSourcePath().split("###");
//			text_paragraphData_host.setText(para_info[0]);
//			text_paragraphData_user.setText(para_info[1]);
//			text_paragraphPassword.setText(para_info[2]);
//			text_paragraphDataName.setText(para_info[3]);
//			text_paragraphDataID.setText(para_info[4]);
//		} else if (paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_GriEngine)) {
//			combo_datasoureType.select(4);
//			composite_gri.setVisible(true);
//			String[] para_info = paragraph.getDataSourcePath().split("###");
//			text_griData_host.setText(para_info[0]);
//			text_griData_user.setText(para_info[1]);
//			text_griPassword.setText(para_info[2]);
//			griParagraphId = Integer.parseInt(para_info[3]);
//			text_griDataName.setText(para_info[4]);
//		}
//		else if (paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_View)) {
//			combo_datasoureType.select(5);
//			composite_view.setVisible(true);
//			String[] para_info = paragraph.getDataSourcePath().split("###");
//			text_viewData_host.setText(para_info[0]);
//			text_viewData_user.setText(para_info[1]);
//			text_viewPassword.setText(para_info[2]);
//			text_viewDataName.setText(para_info[3]);
//			Integer processorId=Integer.parseInt(para_info[4]);
//			boolean flag = false;
//			for(int i=0;i<processors.size();i++){
//				if(processors.get(i).getId().equals(processorId)){
//					combo_viewProcessor.select(i);
//					flag =true;
//					break;
//				}
//			}
//			if(!flag) combo_viewProcessor.select(processors.size());
		}
	
		
		if((paragraph.getDataDestbase()!=null)){//注意这里不嫩使用equals方法，否则如果左边为null时会报错
			check.setSelection(true);
			composite_6.setVisible(true);
			addTable.setVisible(true);
			text_kind.setText(paragraph.getDataDestType());
			text_database.setText(paragraph.getDataDestbase());
			text_IP.setText(paragraph.getDestIP());
			text_Account.setText(paragraph.getDestAccount());
			text_Password.setText(paragraph.getDestPassword());
			text_Port.setText(paragraph.getDestPort());
			/*private Text text_kind;
			private Text text_database;
		    private Text text_IP;
		    private Text text_Account;
		    private Text text_Password;
		    private Text text_Port;*/	
		}
		
		if (windowType == Constant.WindowType_Property) {
			button_ok.setVisible(false);
			button_cancel.setText("关闭");
		}

	}

	private void initWindow() {
		label_updateDate.setVisible(false);
		text_updateDate.setVisible(false);
		label_dataSize.setVisible(false);
		text_dataSize.setVisible(false);
		label_syncDate.setVisible(false);
		text_syncDate.setVisible(false);
		label_syncState.setVisible(false);
		text_syncState.setVisible(false);

		composite_file.setVisible(false);
		composite_db.setVisible(false);
		composite_web.setVisible(false);
		composite_para.setVisible(false);
		composite_gri.setVisible(false);
		composite_view.setVisible(false);

		// shell.setSize(475, 706 - 240);
		tabFolder_main.setBounds(10, 10, 449, 686 - 280);
		button_cancel.setBounds(379, 702 - 280, 80, 27);
		button_ok.setBounds(278, 702 - 280, 80, 27);
	}

	private void showMessage(String message, Shell shell) {
		MessageBox box = new MessageBox(shell);
		box.setMessage(message);
		box.open();
	}
}
