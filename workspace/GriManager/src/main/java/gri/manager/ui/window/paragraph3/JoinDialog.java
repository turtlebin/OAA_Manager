package gri.manager.ui.window.paragraph3;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import gri.driver.manager.GriDocManager;
import gri.driver.manager.ProcessManager;
import gri.driver.model.GriElement;
import gri.driver.model.process.Paragraph;
import gri.driver.model.process.Processor;
import gri.driver.util.DriverConstant;
import gri.engine.dest.Column;
import gri.engine.util.DataSourceConnectTool;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.util.Constant;
import gri.manager.util.DBHelper2;
import gri.manager.util.SQLParser;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Composite;

public class JoinDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Shell parentShell;
	private int windowType;
	private GlobalViewTreeNode node;
	private Group group_db_connect;
	private Group group_db_connect2;
	private TabFolder tabFolder_main;
	private TabFolder tabFolder_main2;
	private ProcessManager manager;
	//
	private Combo combo_datasoureType;
	private Combo combo_datasourceType2;
	private Composite composite_file;
	private Composite composite_db;
	private Composite composite_db2;
	private Composite composite_web;
	private Composite composite_para;
	private Composite composite_gri;
	private Composite composite_view;

	private Text text_name;
	private Text text_keywords;
	private Text text_description;
	private Text text_db_port;
	private Text text_db_table;
	private Text text_db_port2;
	private Text text_db_table2;

	private Text text_db_host;
	private Text text_db_name;
	private Text text_db_sql;
	private Text text_db_username;
	private Text text_db_password;
	private Text text_db_host2;
	private Text text_db_name2;
	private Text text_db_sql2;
	private Text text_db_username2;
	private Text text_db_password2;
	private Combo combo_db_type;
	private Combo combo_db_type2;
	private Text text_filePath;
	private Text text_fileUser;
	private Text text_filePassword;

	private Text text_webURL;
	private Text text_webMethod;
	private Text text_webParam;

	private Button button_ok;
	private Button button_ok2;
	private Button button_cancel;
	private Button button_cancel2;

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
	private List<Column> columns=new ArrayList<Column>();
	private Table table;
	private Table table2;
	public String dataSourcePath;
	public String dataSourcePath2;
	public String join_info;

	
	public JoinDialog(int windowType,Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.windowType = windowType;
	}

	public Object open() {
		createContents();
		if (parentShell != null)
			this.initWindow();
		if (this.windowType != Constant.WindowType_Add)
			this.loadData();
		shell.open();
		shell.layout();
		shell.pack();
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
		shell.setSize(475,900);
		if (parentShell != null)
			shell.setSize(475, 900 - 280);
		shell.setText("数据源");
		shell.setImage(
				new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/paragraph16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		tabFolder_main = new TabFolder(shell, SWT.NONE);
		tabFolder_main.setBounds(10, 10, 449, 750);
		

		TabItem tabItem_data = new TabItem(tabFolder_main, SWT.NONE);
		tabItem_data.setText("连接表1");
		TabItem tabItem_data2=new TabItem(tabFolder_main,SWT.NONE);
		tabItem_data2.setText("连接表2");

		Composite composite_4 = new Composite(tabFolder_main, SWT.NONE);
		tabItem_data.setControl(composite_4);
		
		Composite composite_5=new Composite(tabFolder_main,SWT.NONE);
		tabItem_data2.setControl(composite_5);

		Label label = new Label(composite_4, SWT.NONE);
		label.setBounds(10, 13, 60, 17);
		label.setText("数据来源：");
		
		Label label2= new Label(composite_5, SWT.NONE);
		label2.setBounds(10, 13, 60, 17);
		label2.setText("数据来源：");

		combo_datasoureType = new Combo(composite_4, SWT.READ_ONLY);
		combo_datasoureType.setBounds(76, 10, 88, 25);
		combo_datasoureType.setItems(new String[] {"数据库"});
		combo_datasoureType.select(0);
		combo_datasoureType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (combo_datasoureType.getSelectionIndex() == 0)  
					composite_db.setVisible(true);
				
			}
		});	
		
		combo_datasourceType2 = new Combo(composite_5, SWT.READ_ONLY);
		combo_datasourceType2.setBounds(76, 10, 88, 25);
		combo_datasourceType2.setItems(new String[] {"数据库"});
		combo_datasourceType2.select(0);
		combo_datasourceType2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (combo_datasourceType2.getSelectionIndex() == 0)  
					composite_db.setVisible(true);
				
			}
		});	
	
		// db的界面
		
		composite_db = new Composite(composite_4, SWT.NONE);
		composite_db.setBounds(10, 41, 421, 672);
		composite_db.setVisible(true);
		
		composite_db2 = new Composite(composite_5, SWT.NONE);
		composite_db2.setBounds(10, 41, 421, 672);
		composite_db2.setVisible(true);

		this.group_db_connect = new Group(composite_db, SWT.NONE);
		group_db_connect.setLocation(10, 10);
		group_db_connect.setSize(401, 173);
		group_db_connect.setText("连接属性");
		
		this.group_db_connect2 = new Group(composite_db2, SWT.NONE);
		group_db_connect2.setLocation(10, 10);
		group_db_connect2.setSize(401, 173);
		group_db_connect2.setText("连接属性");

		Label lblDb = new Label(group_db_connect, SWT.NONE);
		lblDb.setText("类型：");
		lblDb.setBounds(10, 26, 36, 17);
		
		Label lblDb2 = new Label(group_db_connect2, SWT.NONE);
		lblDb2.setText("类型：");
		lblDb2.setBounds(10, 26, 36, 17);

		combo_db_type = new Combo(group_db_connect, SWT.NONE);
		combo_db_type.setItems(new String[] { "MySQL", "SQL Server", "Oracle", "DB2","Sybase" });
		combo_db_type.setBounds(83, 23, 109, 25);
		combo_db_type.select(0);
		
		combo_db_type2 = new Combo(group_db_connect2, SWT.NONE);
		combo_db_type2.setItems(new String[] { "MySQL", "SQL Server", "Oracle", "DB2","Sybase" });
		combo_db_type2.setBounds(83, 23, 109, 25);
		combo_db_type2.select(0);

		Label label_10 = new Label(group_db_connect, SWT.NONE);
		label_10.setText("地址：");
		label_10.setBounds(10, 56, 36, 17);
		
		Label label_20 = new Label(group_db_connect2, SWT.NONE);
		label_20.setText("地址：");
		label_20.setBounds(10, 56, 36, 17);

		text_db_host = new Text(group_db_connect, SWT.BORDER);
		text_db_host.setBounds(83, 53, 109, 23);
		text_db_host.setText("localhost");
		
		text_db_host2 = new Text(group_db_connect2, SWT.BORDER);
		text_db_host2.setBounds(83, 53, 109, 23);
		text_db_host2.setText("localhost");

		Label label_11 = new Label(group_db_connect, SWT.NONE);
		label_11.setText("端口：");
		label_11.setBounds(200, 26, 47, 17);
		
		Label label_21 = new Label(group_db_connect2, SWT.NONE);
		label_21.setText("端口：");
		label_21.setBounds(200, 26, 47, 17);

		text_db_port = new Text(group_db_connect, SWT.BORDER);
		text_db_port.setBounds(250, 26, 70, 23);
		text_db_port.setText("3306");
		
		text_db_port2 = new Text(group_db_connect2, SWT.BORDER);
		text_db_port2.setBounds(250, 26, 70, 23);
		text_db_port2.setText("3306");
		
		Label label_13 = new Label(group_db_connect, SWT.NONE);
		label_13.setText("表名：");
		label_13.setBounds(200, 56, 47, 17);
		
		Label label_23 = new Label(group_db_connect2, SWT.NONE);
		label_23.setText("表名：");
		label_23.setBounds(200, 56, 47, 17);

		text_db_table = new Text(group_db_connect, SWT.BORDER);
		text_db_table.setBounds(250, 56, 70, 23);
		text_db_table.setText("123");

		text_db_table2 = new Text(group_db_connect2, SWT.BORDER);
		text_db_table2.setBounds(250, 56, 70, 23);
		text_db_table2.setText("123");
		
		Label label_12 = new Label(group_db_connect, SWT.NONE);
		label_12.setText("数据库：");
		label_12.setBounds(10, 82, 47, 17);
		
		Label label_22 = new Label(group_db_connect2, SWT.NONE);
		label_22.setText("数据库：");
		label_22.setBounds(10, 82, 47, 17);

		text_db_name = new Text(group_db_connect, SWT.BORDER);
		text_db_name.setBounds(83, 79, 109, 23);
		text_db_name.setText("test");
		
		text_db_name2 = new Text(group_db_connect2, SWT.BORDER);
		text_db_name2.setBounds(83, 79, 109, 23);
		text_db_name2.setText("test");

		Label label_14 = new Label(group_db_connect, SWT.NONE);
		label_14.setText("用户名：");
		label_14.setBounds(10, 110, 47, 17);

		Label label_24 = new Label(group_db_connect2, SWT.NONE);
		label_24.setText("用户名：");
		label_24.setBounds(10, 110, 47, 17);
		
		text_db_username = new Text(group_db_connect, SWT.BORDER);
		text_db_username.setBounds(83, 107, 109, 23);
		text_db_username.setText("root");

		text_db_username2 = new Text(group_db_connect2, SWT.BORDER);
		text_db_username2.setBounds(83, 107, 109, 23);
		text_db_username2.setText("root");
		
		Label label_15 = new Label(group_db_connect, SWT.NONE);
		label_15.setText("密码：");
		label_15.setBounds(10, 139, 36, 17);
		
		Label label_25 = new Label(group_db_connect2, SWT.NONE);
		label_25.setText("密码：");
		label_25.setBounds(10, 139, 36, 17);

		text_db_password = new Text(group_db_connect, SWT.BORDER | SWT.PASSWORD);
		text_db_password.setBounds(83, 136, 109, 23);
		text_db_password.setText("csasc");
		
		text_db_password2 = new Text(group_db_connect2, SWT.BORDER | SWT.PASSWORD);
		text_db_password2.setBounds(83, 136, 109, 23);
		text_db_password2.setText("csasc");
		
		Button button_show_field=new Button(group_db_connect,SWT.NONE);
		button_show_field.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String type = combo_db_type.getText().trim();
				String host = text_db_host.getText().trim();
				String port = text_db_port.getText().trim();
				String dbName = text_db_name.getText().trim();
				String user = text_db_username.getText().trim();
				String password = text_db_password.getText().trim();
				String tableName=text_db_table.getText().trim();
				if (type.equals("") || host.equals("") || port.equals("") || dbName.equals("") || user.equals("")
						|| password.equals("")||table.equals("")) {
					MessageBox box = new MessageBox(shell);
					box.setMessage("请填写完整数据库连接信息！");
					box.open();
					return;
				}
				String tableSource=type+"###"+host+"###"+dbName+"###"+user+"###"+password+"###"+port+"###"+tableName;
				DBHelper2 db=new DBHelper2(tableSource);
				columns=getColumnList(db);
				refreshTable(table);
			}
		});
		button_show_field.setText("展示字段");
		button_show_field.setBounds(245,134,73,27);
		
		Button button_show_field2=new Button(group_db_connect2,SWT.NONE);
		button_show_field2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String type = combo_db_type2.getText().trim();
				String host = text_db_host2.getText().trim();
				String port = text_db_port2.getText().trim();
				String dbName = text_db_name2.getText().trim();
				String user = text_db_username2.getText().trim();
				String password = text_db_password2.getText().trim();
				String tableName=text_db_table2.getText().trim();
				if (type.equals("") || host.equals("") || port.equals("") || dbName.equals("") || user.equals("")
						|| password.equals("")||table.equals("")) {
					MessageBox box = new MessageBox(shell);
					box.setMessage("请填写完整数据库连接信息！");
					box.open();
					return;
				}
				String tableSource=type+"###"+host+"###"+dbName+"###"+user+"###"+password+"###"+port+"###"+tableName;
				DBHelper2 db=new DBHelper2(tableSource);
				columns=getColumnList(db);
				refreshTable(table2);
			}
		});
		button_show_field2.setText("展示字段");
		button_show_field2.setBounds(245,134,73,27);

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
				box.setMessage(new DataSourceConnectTool().canConnect(dataSourceConnectPath) ? "数据库连接成功！" : "数据库连接失败！");
				box.open();
			}
		});
		button_test_db_connect.setText("测试连接");
		button_test_db_connect.setBounds(318, 134, 73, 27);
		
		Button button_test_db_connect2 = new Button(group_db_connect2, SWT.NONE);
		button_test_db_connect2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String type = combo_db_type2.getText().trim();
				String host = text_db_host2.getText().trim();
				String port = text_db_port2.getText().trim();
				String dbName = text_db_name2.getText().trim();
				String user = text_db_username2.getText().trim();
				String password = text_db_password2.getText().trim();

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
				box.setMessage(new DataSourceConnectTool().canConnect(dataSourceConnectPath) ? "数据库连接成功！" : "数据库连接失败！");
				box.open();
			}
		});
		button_test_db_connect2.setText("测试连接");
		button_test_db_connect2.setBounds(318, 134, 73, 27);

		Group group_db_data = new Group(composite_db, SWT.NONE);
		group_db_data.setLocation(10, 189);
		group_db_data.setSize(401, 250);
		group_db_data.setText("数据属性");
		
		Group group_db_data2 = new Group(composite_db2, SWT.NONE);
		group_db_data2.setLocation(10, 189);
		group_db_data2.setSize(401, 250);
		group_db_data2.setText("数据属性");
		
		GridLayout gridLayout = new GridLayout();  
	    gridLayout.numColumns = 1;  
		final Composite composite_group=new Composite(group_db_data,SWT.NONE);
	    composite_group.setLayout(gridLayout);
	    composite_group.setSize(401,250);
	    
	    GridLayout gridLayout2 = new GridLayout();  
	    gridLayout2.numColumns = 1;  
		final Composite composite_group2=new Composite(group_db_data2,SWT.NONE);
	    composite_group2.setLayout(gridLayout2);
	    composite_group2.setSize(401,250);
	    
	    GridData gridData = new org.eclipse.swt.layout.GridData();  
        gridData.horizontalAlignment = SWT.FILL;  
        gridData.grabExcessHorizontalSpace = true;  
        gridData.grabExcessVerticalSpace = true;  
        gridData.verticalAlignment = SWT.FILL;  
        
        GridData gridData2 = new org.eclipse.swt.layout.GridData();  
        gridData2.horizontalAlignment = SWT.FILL;  
        gridData2.grabExcessHorizontalSpace = true;  
        gridData2.grabExcessVerticalSpace = true;  
        gridData2.verticalAlignment = SWT.FILL;  
        
    	table = new Table(composite_group, SWT.SINGLE | SWT.FULL_SELECTION | SWT.CHECK);  
		table.setHeaderVisible(true);// 设置显示表头  
        table.setLayoutData(gridData);// 设置表格布局  
        table.setLinesVisible(true);// 设置显示表格线/*  
        
        table2 = new Table(composite_group2, SWT.SINGLE | SWT.FULL_SELECTION | SWT.CHECK);  
		table2.setHeaderVisible(true);// 设置显示表头  
        table2.setLayoutData(gridData);// 设置表格布局  
        table2.setLinesVisible(true);// 设置显示表格线/*  
        
        table.addSelectionListener(new SelectionAdapter()  
        {  
            public void widgetSelected(SelectionEvent e)  
            {  
                // 获得所有的行数  
                int total = table.getItemCount();  
                // 循环所有行  
                for (int i = 0; i < total; i++)  
                {  
                    TableItem item = table.getItem(i); 
                    
                    // 如果该行为选中状态，改变背景色和前景色，否则颜色设置  
                    if (table.isSelected(i))  
                    {   item.setChecked(true);
                        item.setBackground(composite_group.getDisplay().getSystemColor(  
                                SWT.COLOR_RED));  
                        item.setForeground(composite_group.getDisplay().getSystemColor(  
                                SWT.COLOR_WHITE));  
                    }  
                    else  
                    {   
                    	item.setChecked(false);
                        item.setBackground(null);  
                        item.setForeground(null);  
                    }  
                }
               
            }  
  
        }); 
        
        table2.addSelectionListener(new SelectionAdapter()  
        {  
            public void widgetSelected(SelectionEvent e)  
            {  
                // 获得所有的行数  
                int total = table2.getItemCount();  
                // 循环所有行  
                for (int i = 0; i < total; i++)  
                {  
                    TableItem item = table2.getItem(i); 
                    
                    // 如果该行为选中状态，改变背景色和前景色，否则颜色设置  
                    if (table2.isSelected(i))  
                    {   item.setChecked(true);
                        item.setBackground(composite_group.getDisplay().getSystemColor(  
                                SWT.COLOR_RED));  
                        item.setForeground(composite_group.getDisplay().getSystemColor(  
                                SWT.COLOR_WHITE));  
                    }  
                    else  
                    {   
                    	item.setChecked(false);
                        item.setBackground(null);  
                        item.setForeground(null);  
                    }  
                }
               
            }  
  
        }); 
        
        table.addListener(SWT.MouseDoubleClick,new Listener() {//双击取消选择
        	@Override
        	public void handleEvent(Event event) {
        		TableItem[] items=table.getSelection();
        		items[0].setChecked(false);
        	}		
        });
        
        table2.addListener(SWT.MouseDoubleClick,new Listener() {//双击取消选择
        	@Override
        	public void handleEvent(Event event) {
        		TableItem[] items=table2.getSelection();
        		items[0].setChecked(false);
        	}		
        });

//		Label lblSql = new Label(group_db_data, SWT.NONE);
//		lblSql.setBounds(10, 21, 54, 17);
//		lblSql.setText("SQL语句：");
//
//		text_db_sql = new Text(group_db_data, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
//		text_db_sql.setBounds(83, 18, 308, 146);
			
		button_ok = new Button(shell, SWT.NONE);
		button_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String dataSourceType = "";
				dataSourcePath = "";
				String dataDestType = "";
				String dataDestBase = "";
				String dataDestIP = "";
				String dataDestAccount = "";
				String dataDestPassword = "";
				String dataDestPort = "";
				boolean canClose=true;

				// 数据源
				switch (combo_datasoureType.getSelectionIndex()) {
				case 0:
					// 数据库
					dataSourceType = DriverConstant.DataSourceType_Database;
					String type = combo_db_type.getText();
					String host = text_db_host.getText().trim();
					String port = text_db_port.getText().trim();
					String db_name = text_db_name.getText().trim();
					String user = text_db_username.getText().trim();
					String pass = text_db_password.getText().trim();
					String tableName =text_db_table.getText().trim();
					String type2 = combo_db_type2.getText();
					String host2 = text_db_host2.getText().trim();
					String port2 = text_db_port2.getText().trim();
					String db_name2 = text_db_name2.getText().trim();
					String user2 = text_db_username2.getText().trim();
					String pass2 = text_db_password2.getText().trim();
					String tableName2 =text_db_table2.getText().trim();
					if (type.equals("") || host.equals("") || port.equals("") || db_name.equals("") || user.equals("")
							|| pass.equals("")||tableName.equals("")||type2.equals("") || host2.equals("") 
							|| port2.equals("") || db_name2.equals("") || user2.equals("")
							|| pass2.equals("")||tableName2.equals("")) {
						showMessage("请填写完整数据库信息！", shell);
						canClose=false;
						return;
					}
					dataSourcePath = type + "###" + host + "###" + port + "###" + db_name + "###" + user + "###" + pass + "###"+tableName +"###";
					dataSourcePath2 = type2 + "###" + host2 + "###" + port2 + "###" + db_name2 + "###" + user2 + "###" + pass2 + "###"+tableName2 +"###";

					int total2=table.getItemCount();
					if(total2==0) {
						showMessage("请填写完整数据库信息及选择数据源列", shell);
						canClose=false;
					}
					
					int total3=table2.getItemCount();
					if(total3==0) {
						showMessage("请填写完整数据库信息及选择数据源列", shell);
						canClose=false;
					}
					
					for (int i = 0; i < total2; i++)  
	                {  
	                    TableItem item = table.getItem(i); 
	                    String sourceColumn=null;
	                    // 如果该行为选中状态，改变背景色和前景色，否则颜色设置  
	                    if (table.isSelected(i)&&(item.getChecked()==true))  
	                    { 
	                    	sourceColumn=item.getText(0);	       
	                    	dataSourcePath+=sourceColumn;
	                    	break;	                    	
	                    } 
	                    else if(i==(total2-1)) {
	                    	showMessage("请填写完整数据库信息及选择数据源列", shell);
	                    	canClose=false;
	                    	return;
	                    }
	                }
					for (int i = 0; i < total3; i++)  
	                {  
	                    TableItem item = table2.getItem(i); 
	                    String sourceColumn=null;
	                    // 如果该行为选中状态，改变背景色和前景色，否则颜色设置  
	                    if (table2.isSelected(i)&&(item.getChecked()==true))  
	                    { 
	                    	sourceColumn=item.getText(0);	       
	                    	dataSourcePath2+=sourceColumn;
	                  
	                    	break;	                    	
	                    } 
	                    else if(i==(total3-1)) {
	                    	showMessage("请填写完整数据库信息及选择数据源列", shell);
	                    	canClose=false;
	                    	return;
	                    }
	                }					
					break;
				default:
					showMessage("请选择数据源并填写详细信息！", shell);
					canClose=false;
					return;
				}
				join_info=dataSourcePath+"@@@"+dataSourcePath2;	
				if(canClose) 
				{
					shell.close();
				}
			}
			}
		);
		
		button_ok.setBounds(278, 1702, 80, 27);
		button_ok.setText("确定");
		
		
		button_cancel = new Button(shell, SWT.NONE);
		button_cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_cancel.setBounds(379, 1702, 80, 27);
		button_cancel.setText("取消");
	
	}

	private void loadData() {//如果是编辑段，则加载数据
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
		if (paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_File)) {
			combo_datasoureType.select(0);
			composite_file.setVisible(true);
			String[] file_info = paragraph.getDataSourcePath().split("###");
			text_filePath.setText(file_info[0]);
			if (file_info.length > 1)
				text_fileUser.setText(file_info[1]);
			if (file_info.length > 2)
				text_filePassword.setText(file_info[2]);
		} else if (paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_Database)) {
			combo_datasoureType.select(1);
			composite_db.setVisible(true);
			String[] db_conn_info = paragraph.getDataSourcePath().split("###");
			combo_db_type.setText(db_conn_info[0]);
			text_db_host.setText(db_conn_info[1]);
			text_db_port.setText(db_conn_info[2]);
			text_db_name.setText(db_conn_info[3]);
			text_db_username.setText(db_conn_info[4]);
			text_db_password.setText(db_conn_info[5]);
			text_db_sql.setText(db_conn_info[6]);
			
		} else if (paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_WebService)) {
			combo_datasoureType.select(2);
			composite_web.setVisible(true);
			String[] web_info = paragraph.getDataSourcePath().split("###");
			text_webURL.setText(web_info[0]);
			text_webMethod.setText(web_info[1]);
			text_webParam.setText(web_info[2]);
			// TODO
		} else if (paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_ParagraphEngine)) {
			combo_datasoureType.select(3);
			composite_para.setVisible(true);
			String[] para_info = paragraph.getDataSourcePath().split("###");
			text_paragraphData_host.setText(para_info[0]);
			text_paragraphData_user.setText(para_info[1]);
			text_paragraphPassword.setText(para_info[2]);
			text_paragraphDataName.setText(para_info[3]);
			text_paragraphDataID.setText(para_info[4]);
		} else if (paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_GriEngine)) {
			combo_datasoureType.select(4);
			composite_gri.setVisible(true);
			String[] para_info = paragraph.getDataSourcePath().split("###");
			text_griData_host.setText(para_info[0]);
			text_griData_user.setText(para_info[1]);
			text_griPassword.setText(para_info[2]);
			griParagraphId = Integer.parseInt(para_info[3]);
			text_griDataName.setText(para_info[4]);
		}
		else if (paragraph.getDataSourceType().equals(DriverConstant.DataSourceType_View)) {
			combo_datasoureType.select(5);
			composite_view.setVisible(true);
			String[] para_info = paragraph.getDataSourcePath().split("###");
			text_viewData_host.setText(para_info[0]);
			text_viewData_user.setText(para_info[1]);
			text_viewPassword.setText(para_info[2]);
			text_viewDataName.setText(para_info[3]);
			Integer processorId=Integer.parseInt(para_info[4]);
			boolean flag = false;
			for(int i=0;i<processors.size();i++){
				if(processors.get(i).getId().equals(processorId)){
					combo_viewProcessor.select(i);
					flag =true;
					break;
				}
			}
			if(!flag) combo_viewProcessor.select(processors.size());
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
		
		composite_db.setVisible(true);

		// shell.setSize(475, 706 - 240);
		tabFolder_main.setBounds(10, 10, 449, 800 - 280);
		button_cancel.setBounds(379, 816 - 280, 80, 27);
		button_ok.setBounds(278, 816 - 280, 80, 27);
		
	}

	private void showMessage(String message, Shell shell) {
		MessageBox box = new MessageBox(shell);
		box.setMessage(message);
		box.open();
	}
	
	private List<Column> getColumnList(DBHelper2 db) {
		try {
			Class.forName(db.getDriver());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Connection connection = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		Statement state = null;
		List<Column> columns = new ArrayList<Column>();
		try {
			connection = DriverManager.getConnection(db.getUrl(), db.getUserName(), db.getPassword());
			state = connection.createStatement();
			String selectSql;
			if (db.getType().equalsIgnoreCase("MYSQL")) {
				selectSql = "select * from `" + db.getTableName() + "` limit 1";
			} else {
				selectSql = "select * from `" + db.getTableName() + "`";
			}
			rs = state.executeQuery(selectSql);
			rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			if (columnCount > 0) {
				for (int i = 1; i <= columnCount; i++) {
					Column column = new Column();
					column.setColumnName(rsmd.getColumnName(i));
					column.setColumnTypeName(rsmd.getColumnTypeName(i));
					column.setPrecision(rsmd.getPrecision(i) + "");// int转string
					column.setScale(rsmd.getScale(i) + "");
					columns.add(column);
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return columns;
	}
	
	private boolean refreshTable(Table table) {   
        // 创建表头的字符串数组  
        String[] tableHeader = {"插入列选择", "列所属类型", "precision", "scale"};  
        for (int i = 0; i < tableHeader.length; i++)  
        {  
            TableColumn tableColumn = new TableColumn(table, SWT.NONE);  
            tableColumn.setText(tableHeader[i]);  
            // 设置表头可移动，默认为false  
            tableColumn.setMoveable(true);  
        } 
        for(Column column:columns) {
        	TableItem item=new TableItem(table,SWT.NONE);
        	item.setText(new String[] {column.getColumnName(),column.getColumnTypeName(),column.getPrecision(),column.getScale()});
        }
        for (int i = 0; i < tableHeader.length; i++)  
        {  
            table.getColumn(i).pack();  
        }  
        table.setSize(401,250);

		return false;
	}
}
