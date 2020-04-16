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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Composite;

public class JoinListDialog extends Dialog {

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

	private Paragraph tempParagraph = new Paragraph("", "", "", "", "", "", "", ""); // windowType_Add

	private Text text_kind;
	private Text text_database;
	private Text text_IP;
	private Text text_Account;
	private Text text_Password;
	private Text text_Port;

	public static Integer griParagraphId;
	public static Text text_griDataName;

	private boolean addTableName = false;
	private Button check;
	private Button addTable;
	private Composite composite_6;
	private List<Column> columns = new ArrayList<Column>();
	private Table table;
	private Table table2;
	public String dataSourcePath;
	public String dataSourcePath2;
	public String join_info;
	private List<Column> selected_columns;
	private String tableSource;
	private Composite composite_group;
	private Composite composite_content;

	public Map<String, Map<String, Column>> tableMap = new HashMap<String, Map<String, Column>>();
	public Map<String, Column> columnMap = new HashMap<String, Column>();
	private List<Map<String, Map<String, Column>>> list = new ArrayList<Map<String, Map<String, Column>>>();
	private List<String> sourceList = new ArrayList<String>();
	private List<Pair<String, String>> pairList = new ArrayList<Pair<String, String>>();
	public List<Pair<String,String>> wholePairList=new ArrayList<Pair<String,String>>();

	public JoinListDialog(int windowType, Shell parent, int style, List<Map<String, Map<String, Column>>> list) {
		super(parent, style);
		this.parentShell = parent;
		this.windowType = windowType;
		this.selected_columns = columns;
		this.list = list;
		this.sourceList = getSourceList();
	}
	public JoinListDialog(int windowType, Shell parent, int style, List<Map<String, Map<String, Column>>> list,List<Pair<String,String>> pairList) {
		super(parent, style);
		this.parentShell = parent;
		this.windowType = windowType;
		this.selected_columns = columns;
		this.list = list;
		this.sourceList = getSourceList();
		this.pairList=pairList;
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
		shell.setSize(475, 900);
		if (parentShell != null)
			shell.setSize(475, 1000 - 280);
		shell.setText("各字段对应及连接信息");
		shell.setImage(
				new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/paragraph16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;

		Composite composite_group = new Composite(shell, SWT.NONE);
		composite_group.setBounds(0, 0, 475, 910 - 280);
		// composite_group.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));

		composite_group.setLayout(new FillLayout());

		scrolled = new ScrolledComposite(composite_group, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		scrolled.setBounds(2, 5, 470, 890 - 280);
		composite_content = new Composite(scrolled, SWT.NONE);

		scrolled.setContent(composite_content);
		composite_content.setLayout(gridLayout);

		scrolled.setExpandHorizontal(true);
		scrolled.setExpandVertical(true);
		scrolled.setMinWidth(400);
		scrolled.setMinHeight(800 - 280);

		createContentFromList();
		
		button_ok = new Button(shell, SWT.NONE);
		button_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(windowType==Constant.WindowType_Add) {
				if (wholePairList.size()>0) {//能确定的条件应该是至少有wholePairList.size()>=n-1
				    //MapUtil.showPairList(wholePairList);
					shell.close();
				}
				else
				{
					showMessage("请选择合适数量的连接信息",shell);
					return;
				}
			}else {
				shell.close();
			}
			}
		});

		button_ok.setBounds(278, 950, 80, 27);
		button_ok.setText("确定");
		button_ok.pack();

		button_cancel = new Button(shell, SWT.NONE);
		button_cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wholePairList.clear();
				shell.close();
			}
		});
		button_cancel.setBounds(379, 920, 80, 27);
		button_cancel.setText("取消");

	}

	private void loadData() {// 如果是编辑段，则加载数据

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

	private void createContentFromList() {
		int i=0;
        Table[] tables=new Table[list.size()];
		for (Map<String, Map<String, Column>> map : list) {
			createContentFromMap(map,tables,i);
			i++;
		}
	}

	private void createContentFromMap(Map<String, Map<String, Column>> map,final Table[] tables,final int index) {
		Label label = new Label(composite_content, SWT.None);
		final String source = MapUtil.getFirstKeyOrNull(map);
		label.setText(source);
		label.setSize(390, 30);

		String[][] strs = MapUtil.getStringArray(map.get(source));
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;

		Table table = new Table(composite_content, SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLayoutData(gridData);
		table.setLinesVisible(true);

		String[] tableHeader = { "目标列", "数据源列", "数据类型", "precision", "scale" };
		for (int i = 0; i < tableHeader.length; i++) {
			TableColumn tableColumn = new TableColumn(table, SWT.NONE);
			tableColumn.setText(tableHeader[i]);
			// 设置表头可移动，默认为false
			tableColumn.setMoveable(true);
		}
		for (int i = 0; i < strs.length; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(strs[i]);
		}
		for (int i = 0; i < tableHeader.length; i++) {
			table.getColumn(i).pack();
		}
		table.setSize(390, 200);

		GridData data = new GridData();
		data.horizontalIndent = 370;

		final Button button = new Button(composite_content, SWT.PUSH | SWT.RIGHT);
		button.setText("连接字段");
		button.setLayoutData(data);
		button.pack();

		if(this.windowType==Constant.WindowType_Add)
		{
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						sourceList.remove(source);
						SourceJoinDialog dialog = new SourceJoinDialog(Constant.WindowType_Add, shell,
								SWT.APPLICATION_MODAL | SWT.NULL, source, sourceList);
						dialog.open();
						if (dialog.pairList.size() == 0) {
							sourceList.add(source);
						} else {
							pairList = dialog.pairList;
							wholePairList.addAll(pairList);
							button.setEnabled(false);
							tables[index].removeAll();

							for (Pair<String, String> pair : pairList)
							{
								String[] strs2 = { pair.getFirst().split("@@@")[1], pair.getSecond().split("@@@")[0],
										pair.getSecond().split("@@@")[1] };
								TableItem item = new TableItem(tables[index], SWT.NONE);
								item.setText(strs2);
							}
							for (int i = 0; i < 3; i++) {
								tables[index].getColumn(i).pack();
							}
							tables[index].setSize(390, 200);
							tables[index].layout();
							tables[index].setVisible(true);
							composite_content.pack();
							composite_content.layout();
							scrolled.layout();
							scrolled.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
						}
					}
				});
			}
		});
		}
		Label label2 = new Label(composite_content, SWT.None);
		label2.setText("字段连接列表");
		label2.pack();

		tables[index] = new Table(composite_content, SWT.BORDER);
		tables[index].setHeaderVisible(true);
		tables[index].setLayoutData(gridData);
		tables[index].setLinesVisible(true);

		String[] tableHeader2 = { "数据源1字段", "数据源2", "数据源2字段" };
		for (int i = 0; i < tableHeader2.length; i++) {
			TableColumn tableColumn = new TableColumn(tables[index], SWT.NONE);
			tableColumn.setText(tableHeader2[i]);
			// 设置表头可移动，默认为false
			tableColumn.setMoveable(true);
		}
		
		for(int i=0;i<5;i++)//不明白原因，tables[index]中必须先赋予一定数量的TableItem才可以正常显示
		{
			TableItem item=new TableItem(tables[index],SWT.NULL);
			item.setText(new String[]{"123","456","789"});
		}
		for (int i = 0; i < 3; i++) {
			tables[index].getColumn(i).pack();
		}
		tables[index].setVisible(false);
		tables[index].setSize(390, 200);

		GridData dataNull = new GridData();// 设置空行
		dataNull.verticalSpan = 5;
		Label label3 = new Label(composite_content, SWT.NONE);
		label3.setLayoutData(dataNull);
		label3.setVisible(false);

		tables[index].layout();
		composite_content.pack();
		composite_content.layout();
		scrolled.layout();
		scrolled.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		if (this.windowType == Constant.WindowType_Edit) {
			tables[index].removeAll();
			button.setVisible(false);
			for (Pair<String, String> pair : pairList) {
				if (pair.getFirst().split("@@@")[0].equalsIgnoreCase(source)) {
					String[] strs2 = { pair.getFirst().split("@@@")[1], pair.getSecond().split("@@@")[0],
							pair.getSecond().split("@@@")[1] };
					TableItem item = new TableItem(tables[index], SWT.NONE);
					item.setText(strs2);
				}
			}
			for (int i = 0; i < 3; i++) {
				tables[index].getColumn(i).pack();
			}
			tables[index].setSize(390, 200);
			tables[index].layout();
			tables[index].setVisible(true);
			composite_content.pack();
			composite_content.layout();
			scrolled.layout();
			scrolled.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}

	}
	


	private List<String> getSourceList() {
		List<String> sourceList = new ArrayList<String>();
		for (Map<String, Map<String, Column>> map : list) {
			for (Map.Entry<String, Map<String, Column>> entry : map.entrySet()) {
				sourceList.add(entry.getKey());
			}
		}
		// System.out.println("list的大小为："+list.size());
		return sourceList;
	}
}
