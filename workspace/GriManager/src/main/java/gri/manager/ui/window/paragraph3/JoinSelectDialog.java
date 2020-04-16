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
import gri.engine.dest.Pair;
import gri.engine.util.DataSourceConnectTool;
import gri.engine.util.MapUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Composite;

public class JoinSelectDialog extends Dialog {

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
	private ScrolledComposite scrolled;
	//private Pair<String,String> joinPair=new Pair<String,String>();

	
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
	private Composite composite_group;
	private Composite composite_content;
	private Button[] buttons;
	private Table[] tables;
	private String columnName;
	private String sourcePath;
	
	public Map<String,Map<String,Column>> tableMap=new HashMap<String,Map<String,Column>>();
	public Map<String,Column> columnMap=new HashMap<String,Column>();
	private List<Map<String,Map<String,Column>>> list=new ArrayList<Map<String,Map<String,Column>>>();
	private List<String> remainedSourceList=new ArrayList<String>();
	public List<Pair<String,String>> pairList=new ArrayList<Pair<String,String>>();
 	
	public JoinSelectDialog(int windowType,Shell parent, 
			int style,List<String> remainedSourceList,String columnName,String sourcePath) {
		super(parent, style);
		this.parentShell = parent;
		this.windowType = windowType;
		this.selected_columns=columns;
		this.remainedSourceList=remainedSourceList;
		this.columnName=columnName;
		this.sourcePath=sourcePath;
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
		shell = new Shell(getParent(), getParent().getStyle());
		shell.setSize(475,900);
		if (parentShell != null)
			shell.setSize(475, 940 - 280);
		shell.setText("其他数据源");
		shell.setImage(
				new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/paragraph16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);
		
		GridLayout gridLayout=new GridLayout();
		gridLayout.numColumns=1;
		
		Composite composite_group=new Composite(shell,SWT.NONE);
		composite_group.setBounds(0,0,475,900-280);
		//composite_group.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		
		composite_group.setLayout(new FillLayout());
		
		scrolled=new ScrolledComposite(composite_group,SWT.H_SCROLL|SWT.V_SCROLL|SWT.BORDER);
		scrolled.setBounds(2, 5, 470, 890-280);
		composite_content=new Composite(scrolled,SWT.NONE);
		
		scrolled.setContent(composite_content);
		composite_content.setLayout(gridLayout);
		
		scrolled.setExpandHorizontal(true);
		scrolled.setExpandVertical(true);
		scrolled.setMinWidth(400);
		scrolled.setMinHeight(800-280);
		
		createContentFromRemainedList();
		
		button_ok = new Button(shell, SWT.NONE);
		button_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//boolean canClose = false;
				for(int i=0;i<buttons.length;i++)
				{
					if(buttons[i].getSelection())
					{			
						int total=tables[i].getItemCount();
						int b=-3;
						for(int j=0;j<total;j++)
						{
							TableItem item=tables[i].getItem(j);
							if(tables[i].isSelected(j)&&item.getChecked())
							{
							    Pair<String,String> joinPair=new Pair<String,String>();//此处joinPair应在循环体内初始化，因为list保存的是引用，如果不在循环体内初始化，则list只会保存joinPair的最后值。
								joinPair.setFirst(sourcePath+"@@@"+columnName);
								joinPair.setSecond(buttons[i].getText()+"@@@"+item.getText(0));
								pairList.add(joinPair);
								break;
							}
							b=j;
						}
						if(b==total-1)
						{
							buttons[i].setSelection(false);//如果b==total-1，则说明table[i]没有一项被选中，则取消button的选择
						}
					}
					else
					{
						continue;
					}
				}
				MapUtil.showPairList(pairList);
				if (pairList.size()>0) {
					shell.close();
				}
				else if(pairList.size()==0)
				{
					showMessage("请选择至少一个连接信息",shell);
				}
					
			}
			}
		);
		
		button_ok.setBounds(278, 630, 80, 27);
		//button_ok.setSize(80,27);
		button_ok.setText("确定");
		
		
		button_cancel = new Button(shell, SWT.NONE);
		button_cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pairList.clear();
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
		shell.setSize(475, 950 - 240);
		button_cancel.setBounds(379, 910 - 280, 80, 27);
		button_ok.setBounds(278, 910 - 280, 80, 27);
		
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
			switch(db.getType().toUpperCase()) {
			case "MYSQL":selectSql = "select * from `" + db.getTableName() + "` limit 1";break;
			case "ORACLE":selectSql = "select * from " + db.getTableName()+" where rownum<5";break;
			case "SYBASE":selectSql="set rowcount 1 select * from "+db.getTableName();break;
			case "SQLSERVER":selectSql="select top 2 * from "+db.getTableName();break;
			default:selectSql="";
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
	
	private boolean refreshTable(final Table table) {   
        // 创建表头的字符串数组  
        String[] tableHeader = {"插入列选择", "列所属类型", "precision", "scale","对应"};  
        for (int i = 0; i < tableHeader.length; i++)  
        {  
            TableColumn tableColumn = new TableColumn(table, SWT.NONE);  
            tableColumn.setText(tableHeader[i]);  
            // 设置表头可移动，默认为false  
            tableColumn.setMoveable(true);  
        }  
        for(Column column:selected_columns) {
        	TableItem item=new TableItem(table,SWT.NONE);
        	
        	item.setText(new String[] {column.getColumnName(),column.getColumnTypeName(),column.getPrecision(),column.getScale()});
        }
        TableItem[] items=table.getItems();
        for(int i=0;i<items.length;i++)
        {
        	final TableEditor editor=new TableEditor(table);
        	final Button button=new Button(table,SWT.PUSH);
        	final int a=i;
        	button.setText("对应");
        	editor.grabHorizontal=true;
        	editor.setEditor(button,items[i],4);
        	button.addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent event) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							if (tableSource == null) {
								showMessage("请确认要对应的数据源表信息！", shell);
								editor.getItem().setChecked(false);
								return;
							}
							editor.getItem().setChecked(true);
							table.setSelection(a);
							DataSourceColumnDialog dialog = new DataSourceColumnDialog(Constant.WindowType_Add, shell,
									SWT.Close | SWT.APPLICATION_MODAL, tableSource);
							dialog.open();
							if(dialog.column!=null)
							{
								columnMap.put(editor.getItem().getText(0),dialog.column);
								tableMap.put(tableSource, columnMap);
								editor.getItem().setForeground(composite_group.getDisplay().getSystemColor(SWT.COLOR_GRAY));
							}
						}
					});
				}
        	});
        }
        for (int i = 0; i < tableHeader.length; i++)  
        {  
            table.getColumn(i).pack();  
        }  
        table.setSize(401,250);

		return false;
	}
	
	private void createContentFromRemainedList()
	{
		int size=remainedSourceList.size();
		buttons=new Button[size];
		tables=new Table[size];
		int i=0;
		for(String tableSource:remainedSourceList)
		{
			createContentFromMap(tableSource,buttons,tables,i);
			i++;
		}
	}

	private void createContentFromMap(String tableSource,Button[] buttons,Table[] tables,int i) {
		buttons[i]=new Button(composite_content,SWT.CHECK);
		buttons[i].setText(tableSource);
		buttons[i].setSize(390,30);
		
		final Button tempButton=buttons[i];
		
		DBHelper2 db=new DBHelper2(tableSource);
		columns=getColumnList(db);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;

		tables[i] = new Table(composite_content, SWT.CHECK|SWT.FULL_SELECTION|SWT.SINGLE|SWT.BORDER);
		tables[i].setHeaderVisible(true);
		tables[i].setLayoutData(gridData);
		tables[i].setLinesVisible(true);
		
		final Table tempTable=tables[i];
		tempTable.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event event)
			{
				TableItem[] items=tempTable.getSelection();
				items[0].setChecked(false);
			}
		});
		
		tempTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int total=tempTable.getItemCount();
			    for(int i=0;i<total;i++)
			    {
			    	TableItem item=tempTable.getItem(i);
			    	if(tempTable.isSelected(i))
			    	{
			    		item.setChecked(true);
			    		tempButton.setSelection(true);
			    	}
			    	else
			    	{
			    		item.setChecked(false);
			    	}
			    }		
			}
		});
		
		tempButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event)
			{
				if(!tempButton.getSelection())
				{
				   int total=tempTable.getItemCount();
				   for(int i=0;i<total;i++)
				   {
					   TableItem item=tempTable.getItem(i);
					   if(item.getChecked())
					   {
						   item.setChecked(false);
					   }
				   }
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		String[] tableHeader = {"数据源列", "数据类型", "precision", "scale" };
		for (int a = 0; a < tableHeader.length; a++) {
			TableColumn tableColumn = new TableColumn(tables[i], SWT.NONE);
			tableColumn.setText(tableHeader[a]);
			// 设置表头可移动，默认为false
			tableColumn.setMoveable(true);
		}
		   for(Column column:columns) {
	        	TableItem item=new TableItem(tables[i],SWT.NONE);
	        	item.setText(new String[] {column.getColumnName(),column.getColumnTypeName(),column.getPrecision(),column.getScale(),""});
	        }
		for (int a = 0; a < tableHeader.length; a++) {
			tables[i].getColumn(a).pack();
		}
		tables[i].setSize(390, 200);

		GridData dataNull = new GridData();// 设置空行
		dataNull.verticalSpan = 5;
		Label label3 = new Label(composite_content, SWT.NONE);
		label3.setLayoutData(dataNull);
		label3.setVisible(false);

		composite_content.layout();
		scrolled.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
}
