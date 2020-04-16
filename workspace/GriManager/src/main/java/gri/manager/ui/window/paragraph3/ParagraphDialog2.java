package gri.manager.ui.window.paragraph3;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import gri.driver.manager.ProcessManager;
import gri.driver.model.GriElement;
import gri.driver.model.process.Paragraph;
import gri.driver.model.process.Processor;
import gri.driver.util.DriverConstant;
import gri.engine.dest.Column;
import gri.engine.util.MapUtil;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.ui.window.SyncConfigDialog;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Composite;

public class ParagraphDialog2 extends Dialog {

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
	private Text text_db_table;

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
	private List<Column> columns=new ArrayList<Column>();
	private Table table;
	private Map<String,String> columnMap=new HashMap<String,String>();
	private Map<String,String> joinMap=new HashMap<String,String>();
	private List<String> joinList=new ArrayList<String>();

	
	public ParagraphDialog2(int windowType,  GlobalViewTreeNode treeNode, Shell parent, int style) {
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
		shell.setText("新建段");
		shell.setImage(
				new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/paragraph16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		tabFolder_main = new TabFolder(shell, SWT.NONE);
		tabFolder_main.setBounds(10, 10, 449, 1500);

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
		tabItem_data.setText("目标数据表");

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
		combo_db_type.select(0);

		Label label_10 = new Label(group_db_connect, SWT.NONE);
		label_10.setText("地址：");
		label_10.setBounds(10, 56, 36, 17);

		text_db_host = new Text(group_db_connect, SWT.BORDER);
		text_db_host.setBounds(83, 53, 109, 23);
		text_db_host.setText("localhost");

		Label label_11 = new Label(group_db_connect, SWT.NONE);
		label_11.setText("端口：");
		label_11.setBounds(200, 26, 47, 17);

		text_db_port = new Text(group_db_connect, SWT.BORDER);
		text_db_port.setBounds(250, 26, 70, 23);
		text_db_port.setText("3306");
		
		Label label_13 = new Label(group_db_connect, SWT.NONE);
		label_13.setText("表名：");
		label_13.setBounds(200, 56, 47, 17);

		text_db_table = new Text(group_db_connect, SWT.BORDER);
		text_db_table.setBounds(250, 56, 70, 23);
		text_db_table.setText("123");

		Label label_12 = new Label(group_db_connect, SWT.NONE);
		label_12.setText("数据库：");
		label_12.setBounds(10, 82, 47, 17);

		text_db_name = new Text(group_db_connect, SWT.BORDER);
		text_db_name.setBounds(83, 79, 109, 23);
		text_db_name.setText("test");

		Label label_14 = new Label(group_db_connect, SWT.NONE);
		label_14.setText("用户名：");
		label_14.setBounds(10, 110, 47, 17);

		text_db_username = new Text(group_db_connect, SWT.BORDER);
		text_db_username.setBounds(83, 107, 109, 23);
		text_db_username.setText("root");

		Label label_15 = new Label(group_db_connect, SWT.NONE);
		label_15.setText("密码：");
		label_15.setBounds(10, 139, 36, 17);

		text_db_password = new Text(group_db_connect, SWT.BORDER | SWT.PASSWORD);
		text_db_password.setBounds(83, 136, 109, 23);
		text_db_password.setText("csasc");
		
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
				String table=text_db_table.getText().trim();
				if (type.equals("") || host.equals("") || port.equals("") || dbName.equals("") || user.equals("")
						|| password.equals("")||table.equals("")) {
					MessageBox box = new MessageBox(shell);
					box.setMessage("请填写完整数据库连接信息！");
					box.open();
					return;
				}
				String tableSource=type+"###"+host+"###"+dbName+"###"+user+"###"+password+"###"+port+"###"+table;
				DBHelper2 db=new DBHelper2(tableSource);
				columns=getColumnList(db);
				refreshTable();
			}
		});
		button_show_field.setText("展示字段");
		button_show_field.setBounds(245,134,73,27);
		
	    Button button_join=new Button(group_db_connect,SWT.NONE);
	    button_join.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		Display.getDefault().asyncExec(new Runnable() {
					public void run()
					{
						JoinDialog dialog = new JoinDialog(Constant.WindowType_Add, shell,
								SWT.CLOSE | SWT.APPLICATION_MODAL);
						dialog.open();
						if(dialog.join_info!=null)
						{
						if(!dialog.join_info.equals("@@@"))
						{
							joinList.add(dialog.join_info);
						}
						}
						MapUtil.showList(joinList);
//						System.out.println(dialog.dataSourcePath);
//						if (dialog.dataSourcePath == null) 
//						{
//							TableItem[] item = table.getSelection();
//							item[0].setChecked(false); // 如果没有子shell没有选择完整数据，则item取消check状态
//							MapUtil.removeMap(columnMap, item[0].getText());
//							MapUtil.removeMap(joinMap, item[0].getText());
//						} 
//						else if (dialog.dataSourcePath.endsWith("###")) 
//						{
//							TableItem[] item = table.getSelection(); // 如果没有子shell没有选择完整数据，则item取消check状态
//							item[0].setChecked(false);
//							MapUtil.removeMap(columnMap, item[0].getText());
//							MapUtil.removeMap(joinMap, item[0].getText());
//						} 
//						else 
//						{
//							TableItem[] item = table.getSelection();
//							MapUtil.putMap(columnMap, item[0].getText(), dialog.dataSourcePath);
//						}
					}
				});
	    	}
	    });
	    button_join.setText("连接选择");
	    button_join.setBounds(245,100,73,27);
	    
	    Button clear_join=new Button(group_db_connect,SWT.NONE);
	    clear_join.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		joinList.clear();
	    	}
	    });
	    clear_join.setText("清空连接");
	    clear_join.setBounds(318,100,73,27);

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
			public void widgetSelected(SelectionEvent e) {
				// 获得所有的行数
				int total = table.getItemCount();
				// 循环所有行
				for (int i = 0; i < total; i++) {
					TableItem item = table.getItem(i);

					// 如果该行为选中状态，改变背景色和前景色，否则颜色设置
					if (table.isSelected(i)) {
						item.setChecked(true);
						item.setBackground(composite_group.getDisplay().getSystemColor(SWT.COLOR_RED));
						item.setForeground(composite_group.getDisplay().getSystemColor(SWT.COLOR_WHITE));
					} else {
						item.setBackground(null);
						item.setForeground(null);
					}
				}
				Display.getDefault().asyncExec(new Runnable() {
					public void run()
					{
						SourceDialog dialog = new SourceDialog(Constant.WindowType_Add, shell,
								SWT.CLOSE | SWT.APPLICATION_MODAL);
						dialog.open();
						System.out.println(dialog.dataSourcePath);
						if (dialog.dataSourcePath == null) 
						{
							TableItem[] item = table.getSelection();
							item[0].setChecked(false); // 如果没有子shell没有选择完整数据，则item取消check状态
							MapUtil.removeMap(columnMap, item[0].getText());
							//MapUtil.removeMap(joinMap, item[0].getText());
						} 
						else if (dialog.dataSourcePath.endsWith("###")) 
						{
							TableItem[] item = table.getSelection(); // 如果没有子shell没有选择完整数据，则item取消check状态
							item[0].setChecked(false);
							MapUtil.removeMap(columnMap, item[0].getText());
							//MapUtil.removeMap(joinMap, item[0].getText());
						} 
						else 
						{
							TableItem[] item = table.getSelection();
							MapUtil.putMap(columnMap, item[0].getText(), dialog.dataSourcePath);
						}
					}
				});
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
//				int total = table.getItemCount();
//				// 循环所有行
//				for (int i = 0; i < total; i++) 
//				{
//					TableItem item = table.getItem(i);
//					if (item.getChecked() == true) 
//					{
//						if (item.getText(4).equals("")) {
//							if(joinMap.containsKey(item.getText(0)))
//							{
//								if(!columnMap.containsKey(item.getText(0)))//处理从1更改为""的情况
//								{
//									columnMap.put(item.getText(0), joinMap.get(item.getText(0)));
//								}
//								MapUtil.removeMap(joinMap, item.getText(0));//处理完后还要删除joinMap
//							}
//							continue;
//						}
//
//						else if (item.getText(4).equals("0"))// 输入0代表连接+插入；
//						{
//							String columnName = item.getText(0);
//							if (joinMap.containsKey(columnName)) // 处理从1更改为0的情况
//							{
//								if (!(joinMap.get(item.getText(0)).equals(""))
//										&& (!columnMap.containsKey(item.getText(0))))
//								{
//									columnMap.put(item.getText(0), joinMap.get(item.getText(0)));
//								}
//							} 
//							else 
//							{
//								joinMap.put(item.getText(0), columnMap.get(item.getText(0)));//第一次即输入0时初始化joinMap
//							}
//						} 
//						else // 输入其他代表只连接不插入
//						{
//							joinMap.put(item.getText(0), columnMap.get(item.getText(0)));
//							MapUtil.removeMap(columnMap, item.getText(0));
//						}
//					}
//				}
				MapUtil.show(columnMap);
				//MapUtil.show2(joinMap);
			}
		});
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
		label_updateDate.setVisible(false);
		text_updateDate.setVisible(false);
		label_dataSize.setVisible(false);
		text_dataSize.setVisible(false);
		label_syncDate.setVisible(false);
		text_syncDate.setVisible(false);
		label_syncState.setVisible(false);
		text_syncState.setVisible(false);
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
	
	private boolean refreshTable() {   
        // 创建表头的字符串数组  
        String[] tableHeader = {"插入列选择", "列所属类型", "precision", "scale","连接列选择"};  
        for (int i = 0; i < tableHeader.length; i++)  
        {  
            TableColumn tableColumn = new TableColumn(table, SWT.NONE);  
            tableColumn.setText(tableHeader[i]);  
            // 设置表头可移动，默认为false  
            tableColumn.setMoveable(true);  
        } 
        for(Column column:columns) {
        	TableItem item=new TableItem(table,SWT.NONE);
        	item.setText(new String[] {column.getColumnName(),column.getColumnTypeName(),column.getPrecision(),column.getScale(),""});
        }
//        TableItem[] items=table.getItems();
//        for (int i = 0; i < items.length; i++)  
//        {  
//            // 第一列设置，创建TableEditor对象  
//            final TableEditor editor = new TableEditor(table);  
//            // 创建一个文本框，用于输入文字  
//            final Text text = new Text(table, SWT.NONE);  
//            // 将文本框当前值，设置为表格中的值  
//            text.setText("");  
//            // 设置编辑单元格水平填充  
//            editor.grabHorizontal = true;  
//            // 关键方法，将编辑单元格与文本框绑定到表格的第一列  
//            editor.setEditor(text, items[i], 4);  
//            // 当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。  
//            // 否则即使改变的文本框的值，对表格中的数据也不会影响  
//            text.addModifyListener(new ModifyListener()  
//            {  
//                public void modifyText(ModifyEvent e)  
//                {  
//                    editor.getItem().setText(4, text.getText());  
//                }  
//  
//            });
//        }
        for (int i = 0; i < tableHeader.length; i++)  
        {  
            table.getColumn(i).pack();  
        }  
        table.setSize(401,250);

		return false;
	}
}
