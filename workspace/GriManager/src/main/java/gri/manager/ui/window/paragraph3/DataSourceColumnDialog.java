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
import org.eclipse.swt.custom.TableEditor;
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

public class DataSourceColumnDialog extends Dialog {

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
	private List<Column> selected_columns;
	private String tableSource;
	private String type;
	private String host;
	private String dbName;
	private String user;
	private String password;
	private String port;
	private String tableName;
	public String sourceColumn;
	public Column column;

	
	public DataSourceColumnDialog(int windowType,Shell parent, int style,String tableSource,List<Column> columns) {
		super(parent, style);
		this.parentShell = parent;
		this.windowType = windowType;
		this.selected_columns=columns;
		this.tableSource=tableSource;
		this.columns=columns;
	}

	public DataSourceColumnDialog(int windowType,Shell parent, int style,String tableSource) {
		super(parent, style);
		this.parentShell = parent;
		this.windowType = windowType;
		this.selected_columns=columns;
		this.tableSource=tableSource;
	}
	
	public Object open() {
		analyzeTableSource();
		createContents();
		refreshTable(table);
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
		//shell = new Shell(getParent(), 33620064);//这句也行，其中33620064为父shell的styleId值
		shell=new Shell(getParent(),getParent().getStyle());
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
		

		Composite composite_4 = new Composite(tabFolder_main, SWT.NONE);
		tabItem_data.setControl(composite_4);
		
	

		Label label = new Label(composite_4, SWT.NONE);
		label.setBounds(10, 13, 60, 17);
		label.setText("数据来源：");
		
		

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
		
	
	
		// db的界面
		
		composite_db = new Composite(composite_4, SWT.NONE);
		composite_db.setBounds(10, 41, 421, 672);
		composite_db.setVisible(true);
	
		this.group_db_connect = new Group(composite_db, SWT.NONE);
		group_db_connect.setLocation(10, 10);
		group_db_connect.setSize(401, 173);
		group_db_connect.setText("连接属性");
		
	

		Label lblDb = new Label(group_db_connect, SWT.NONE);
		lblDb.setText("类型：");
		lblDb.setBounds(10, 26, 36, 17);
		
	
		combo_db_type = new Combo(group_db_connect, SWT.NONE);
		combo_db_type.setItems(new String[] { "MySQL", "SQL Server", "Oracle", "DB2","Sybase" });
		combo_db_type.setBounds(83, 23, 109, 25);
		switch(type.toUpperCase())
		{
		case("MYSQL"):
			combo_db_type.select(0);
		case("SQL SERVER"):
			combo_db_type.select(1);
		case("ORACLE"):
			combo_db_type.select(2);
		case("DB2"):
			combo_db_type.select(3);
		case("SYBASE"):
			combo_db_type.select(4);
		default:
			combo_db_type.select(0);
		}
		combo_db_type.setEnabled(false);
		


		Label label_10 = new Label(group_db_connect, SWT.NONE);
		label_10.setText("地址：");
		label_10.setBounds(10, 56, 36, 17);
		
		text_db_host = new Text(group_db_connect, SWT.BORDER);
		text_db_host.setBounds(83, 53, 109, 23);
		text_db_host.setText(host);
		text_db_host.setEditable(false);
		

		Label label_11 = new Label(group_db_connect, SWT.NONE);
		label_11.setText("端口：");
		label_11.setBounds(200, 26, 47, 17);
		

		text_db_port = new Text(group_db_connect, SWT.BORDER);
		text_db_port.setBounds(250, 26, 70, 23);
		text_db_port.setText(port);
		text_db_port.setEditable(false);
		
		Label label_13 = new Label(group_db_connect, SWT.NONE);
		label_13.setText("表名：");
		label_13.setBounds(200, 56, 47, 17);
		
	

		text_db_table = new Text(group_db_connect, SWT.BORDER);
		text_db_table.setBounds(250, 56, 70, 23);
		text_db_table.setText(tableName);
		text_db_table.setEditable(false);

		Label label_12 = new Label(group_db_connect, SWT.NONE);
		label_12.setText("数据库：");
		label_12.setBounds(10, 82, 47, 17);
		

		text_db_name = new Text(group_db_connect, SWT.BORDER);
		text_db_name.setBounds(83, 79, 109, 23);
		text_db_name.setText(dbName);
		text_db_name.setEditable(false);
		
	
		Label label_14 = new Label(group_db_connect, SWT.NONE);
		label_14.setText("用户名：");
		label_14.setBounds(10, 110, 47, 17);


		
		text_db_username = new Text(group_db_connect, SWT.BORDER);
		text_db_username.setBounds(83, 107, 109, 23);
		text_db_username.setText(user);
		text_db_username.setEditable(false);

		
		Label label_15 = new Label(group_db_connect, SWT.NONE);
		label_15.setText("密码：");
		label_15.setBounds(10, 139, 36, 17);
		
	

		text_db_password = new Text(group_db_connect, SWT.BORDER | SWT.PASSWORD);
		text_db_password.setBounds(83, 136, 109, 23);
		text_db_password.setText(password);
		text_db_password.setEditable(false);
		
//		Button button_show_firm=new Button(group_db_connect,SWT.NONE);
//		button_show_firm.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				String type = combo_db_type.getText().trim();
//				String host = text_db_host.getText().trim();
//				String port = text_db_port.getText().trim();
//				String dbName = text_db_name.getText().trim();
//				String user = text_db_username.getText().trim();
//				String password = text_db_password.getText().trim();
//				String tableName=text_db_table.getText().trim();
//				if (type.equals("") || host.equals("") || port.equals("") || dbName.equals("") || user.equals("")
//						|| password.equals("")||table.equals("")) {
//					MessageBox box = new MessageBox(shell);
//					box.setMessage("请填写完整数据库连接信息！");
//					box.open();
//					return;
//				}
//				String tableSource=type+"###"+host+"###"+dbName+"###"+user+"###"+password+"###"+port+"###"+tableName;
//				combo_db_type.setEnabled(false);
//				text_db_host.setEditable(false);
//				text_db_port.setEditable(false);
//				text_db_name.setEditable(false);
//				text_db_username.setEditable(false);
//				text_db_password.setEditable(false);
//				text_db_table.setEditable(false);
//			}
//		});
//		button_show_firm.setText("确认");
//		button_show_firm.setBounds(245,134,73,27);
		

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
	
		Group group_db_data = new Group(composite_db, SWT.NONE);
		group_db_data.setLocation(10, 189);
		group_db_data.setSize(401, 250);
		group_db_data.setText("数据属性");
		
		
		GridLayout gridLayout = new GridLayout();  
	    gridLayout.numColumns = 1;  
		final Composite composite_group=new Composite(group_db_data,SWT.NONE);
	    composite_group.setLayout(gridLayout);
	    composite_group.setSize(401,250);
	    

	    GridData gridData = new org.eclipse.swt.layout.GridData();  
        gridData.horizontalAlignment = SWT.FILL;  
        gridData.grabExcessHorizontalSpace = true;  
        gridData.grabExcessVerticalSpace = true;  
        gridData.verticalAlignment = SWT.FILL;  
        
        
    	table = new Table(composite_group, SWT.SINGLE | SWT.FULL_SELECTION | SWT.CHECK);  
		table.setHeaderVisible(true);// 设置显示表头  
        table.setLayoutData(gridData);// 设置表格布局  
        table.setLinesVisible(true);// 设置显示表格线/*  
        
        
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
        
     
        table.addListener(SWT.MouseDoubleClick,new Listener() {//双击取消选择
        	@Override
        	public void handleEvent(Event event) {
        		TableItem[] items=table.getSelection();
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
					int total2=table.getItemCount();
					if(total2==0) {
						showMessage("请填写完整数据库信息及选择数据源列", shell);
						canClose=false;
					}
					for (int i = 0; i < total2; i++)  
	                {  
	                    TableItem item = table.getItem(i); 
	                    sourceColumn=null;
	                    // 如果该行为选中状态，改变背景色和前景色，否则颜色设置  
	                    if (table.isSelected(i)&&(item.getChecked()==true))  
	                    { 
	                    	column=new Column();
	                    	column.setColumnName(item.getText(0));
	                    	column.setColumnTypeName(item.getText(1));
	                    	column.setPrecision(item.getText(2));
	                    	column.setScale(item.getText(3));
	                    	break;	                    	
	                    } 
	                    else if(i==(total2-1)) {
	                    	showMessage("请填写完整数据库信息及选择数据源列", shell);
	                    	canClose=false;
	                    	return;
	                    }
	                }
	                }								
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
				selectSql = "select * from " + db.getTableName();
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
			rs.close();
			connection.close();
			System.out.println("连接已关闭");
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
	private void analyzeTableSource() 
	{
		if(tableSource!=null)
		{
			String[] str=tableSource.split("###");
			type=str[0];
			host=str[1];
			dbName=str[2];
			user=str[3];
			password=str[4];
			port=str[5];
			tableName=str[6];
		}
	}
}
