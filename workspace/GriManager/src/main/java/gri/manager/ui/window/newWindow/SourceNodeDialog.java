package gri.manager.ui.window.newWindow;

import Support.SourceInfoHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import gri.manager.newModel.*;
import gri.manager.ui.window.paragraph3.DataSourceDialog;
import gri.manager.ui.window.paragraph3.JoinListDialog;
import gri.manager.util.*;
import net.sf.jsqlparser.statement.insert.Insert;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Row;
import org.eclipse.swt.nebula.widgets.cdatetime.CDT;
import org.eclipse.swt.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.widgets.*;

import gri.driver.manager.ProcessManager;
import gri.driver.model.GriElement;
import gri.driver.model.process.Paragraph;
import gri.driver.model.process.Processor;
import gri.driver.util.DriverConstant;
import gri.engine.core.DataSyncTaskManager;
import gri.engine.dest.Column;
import gri.engine.dest.Pair;
import gri.engine.integrate.DBDao;
import gri.engine.integrate.DataJoin;
import gri.engine.integrate.DataSource;
import gri.engine.integrate.FileUtil;
import gri.engine.integrate.IntegrateMain;
import gri.engine.integrate.JoinHelper;
import gri.engine.integrate.MultiSourceSchemaTransformation;
import gri.engine.integrate.Paragraph2;
import gri.engine.integrate.Paragraph3;
import gri.engine.integrate.SyncHelper;
import gri.engine.util.DBHelper;
import gri.engine.util.MapUtil;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.ui.window.MainWindow;
import gri.manager.ui.window.SyncConfigDialog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.apache.spark.sql.Dataset;


public class SourceNodeDialog extends Dialog {

    public int index = 1;

    protected Object result;
    protected Shell shell;
    private Shell parentShell;
    private int windowType;

    private GlobalViewTreeNode node;
    private Group group_db_connect;
    private Group group_hive_connect;
    private Group group_file_connect;

    private Group group_join_configure;
    private Group group_join_info;

    private TabFolder tabFolder_main;
    private ProcessManager manager;
    //
    private Combo combo_datasoureType;
    private Composite composite_file;
    private Composite composite_db;
    private Composite composite_hive;
    private Composite composite_test;

    //Database
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
    private Text text_partitionColumn;
    private Text text_lowerBound;
    private Text text_upperBound;
    private Text text_partitionCount;
    private Text text_timestamp;
    private CDateTime cDateTime;
    private Text text_whereClause;
    private Combo combo_db_type;
    private Combo combo_db_addSource;

    //Hive
    private Text text_hive_port;
    private Text text_hive_table;
    private Text text_hive_host;
    private Text text_hive_db_name;
    private Text text_hive_timestamp;
    private CDateTime text_hive_cDateTime;
    private Text text_hive_whereClause;
    private Combo combo_hive_addSource;

    //File
    private Combo combo_file_type;
    private Text text_file_host;
    private Text text_file_port;
    private Text text_file_path;
    private Text text_file_timestamp;
    private CDateTime text_file_cDateTime;
    private Text text_file_whereClause;
    private Combo combo_file_addSource;

    //Join
    private Combo combo_join_left;
    private Combo combo_join_right;
    private Text text_join_tmpTable;
    private Combo combo_join_type;
    private Combo combo_join_left_column;
    private Combo combo_join_right_column;
    private Combo combo_join_eq;
    private Combo combo_using;

    private Composite composite_group;
    private Composite composite_sql;
    private Group group_test;


    //Insert
    private Combo combo_insert_datasourceType;
    private Composite composite_insert_db;
    private Composite composite_insert_hive;
    private Composite composite_insert_file;
    private Composite composite_insert_uom;
    private Group group_insert_db_connect;
    private Group group_insert_hive_connect;
    private Group group_insert_file_connect;
    private Group group_insert_uom_connect;
    private Group group_insert_map;


    private Combo combo_insert_db_type;
    private Text text_insert_hive_host;
    private Combo combo_insert_file_type;
    private Text text_insert_db_host;
    private Text text_insert_hive_db_name;
    private Text text_insert_file_host;
    private Text text_insert_db_name;
    private Text text_insert_hive_table;
    private Text text_insert_hive_port;
    private Text text_insert_db_password;
    private Text text_insert_db_port;
    private Text text_insert_db_table;
    private Text text_insert_file_path;
    private Text text_insert_db_username;
    private Text text_insert_file_port;
    private Combo combo_insert_db_mode;
    private Combo combo_insert_hive_mode;
    private Combo combo_insert_file_mode;
    private Combo combo_insert_uom_mode;

    private Button button_confirm_dest;
    private Button button_confirm_insert;

    private Combo combo_source;

    private Text text_insert_uom_brokerList;
    private Text text_insert_uom_topic;
    private Text text_insert_uom_keySer;
    private Text text_insert_uom_valueSer;
    private Text text_insert_uom_partitionCounts;
    private Text text_insert_replication;
    private Combo combo_uom_mode;


    private Button button_enter;
    private Button button_select_enter;
    private Button button_join_enter;

    private Button button_ok;
    private Button button_next;
    private Button button_cancel;
    private Button button_config;
    private Button button_withoutJoin;
    private Button button_ok2;

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

    private boolean addTableName = false;
    private Button check;
    private Button addTable;
    private Composite composite_6;
    private List<Column> columns = new ArrayList<Column>();
    private Table table2;
    private Table table3;
    private Table table4;
    private Table table5;


    private Map<String, String> columnMap = new HashMap<String, String>();
    private Map<String, String> joinMap = new HashMap<String, String>();
    private List<String> joinList = new ArrayList<String>();
    public List<Column> selected_columns = new ArrayList<Column>();
    //    private ScrolledComposite scrolledComposite;
    private Map<String, Map<String, Column>> tableMap = new HashMap<String, Map<String, Column>>();
    private List<Map<String, Map<String, Column>>> list = new ArrayList<Map<String, Map<String, Column>>>();//list存放格式为Map<数据源，Map<目标表字段名称，来源表字段column>>
    public List<Pair<String, String>> wholePairList = new ArrayList<Pair<String, String>>();

    public LinkedHashSet<DataSourceInfo> dataSourceList = new LinkedHashSet<>();
    public ArrayList<String> tableNameList = new ArrayList<>(100);

    public LinkedHashMap<String, DataSourceInfo> nameToSource = new LinkedHashMap<>();
    public LinkedHashMap<String, Dataset<Row>> nameToDataFrame = new LinkedHashMap<>();
    private ArrayList<JoinInfo> joinInfos = new ArrayList<>();
    private LinkedHashMap<String, Dataset<Row>> tempToDataFrame = new LinkedHashMap<>();
    private LinkedHashMap<String, Dataset<Row>> wholeNameToDataFrame = new LinkedHashMap<>();
    private ArrayList<String> insertList = new ArrayList<>();
    private SyncConfig syncConfig = new SyncConfig();


    public SourceNodeDialog(int windowType, GlobalViewTreeNode treeNode, Shell parent, int style) {
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
        tabFolder_main.setBounds(10, 10, 649, 1200 - 280);
        button_cancel.setBounds(450, 940, 80, 27);
        button_ok2.setBounds(540,940,80,27);
        button_next.setBounds(278, 1230 - 280, 80, 27);
        button_ok.setBounds(178, 1230 - 280, 80, 27);
        button_withoutJoin.setBounds(78, 1230 - 280, 80, 27);
    }

    private void createContents() {
        shell = new Shell(getParent(), getStyle());
        System.out.println(shell.getSize().x + "，" + shell.getSize().y);
//        shell.setSize(675,900);
//        if (parentShell != null)
//            shell.setSize(675, 1300 - 280);
        shell.setText("新建段");
        shell.setImage(
                new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/paragraph16.png")));

        Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
        Rectangle shellBounds = shell.getBounds();
        shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
                parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

        tabFolder_main = new TabFolder(shell, SWT.NONE);
        tabFolder_main.setBounds(10, 10, 749, 1500);

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
        text_name.setSize(480, 23);
        text_name.setText("integrate_1");

        Label label_3 = new Label(composite_3, SWT.NONE);
        label_3.setLocation(10, 71);
        label_3.setSize(48, 17);
        label_3.setText("关键词：");

        text_keywords = new Text(composite_3, SWT.BORDER);
        text_keywords.setLocation(94, 68);
        text_keywords.setSize(480, 23);

        Label label_6 = new Label(composite_3, SWT.NONE);
        label_6.setLocation(10, 121);
        label_6.setSize(36, 17);
        label_6.setText("描述：");

        text_description = new Text(composite_3, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        text_description.setLocation(94, 118);
        text_description.setSize(480, 69);

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
        tabItem_data.setText("数据源列表");

        Composite composite_4 = new Composite(tabFolder_main, SWT.NONE);
        tabItem_data.setControl(composite_4);

        TabItem tabItem_join = new TabItem(tabFolder_main, SWT.NONE);
        tabItem_join.setText("数据表连接属性");

        Composite composite_join = new Composite(tabFolder_main, SWT.NONE);
        tabItem_join.setControl(composite_join);

        TabItem tabItem_Insert = new TabItem(tabFolder_main, SWT.NONE);
        tabItem_Insert.setText("数据列映射插入");

        Composite composite_insert = new Composite(tabFolder_main, SWT.NONE);
        tabItem_Insert.setControl(composite_insert);

        Label label = new Label(composite_4, SWT.NONE);
        label.setBounds(20, 13, 60, 17);
        label.setText("数据来源：");

        combo_datasoureType = new Combo(composite_4, SWT.READ_ONLY);
        combo_datasoureType.setBounds(96, 10, 88, 25);
        combo_datasoureType.setItems(new String[]{"数据库", "Hive", "文件"});
        combo_datasoureType.select(0);
        combo_datasoureType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (combo_datasoureType.getSelectionIndex() == 0) {
                    composite_db.setVisible(true);
                    composite_hive.setVisible(false);
                    composite_file.setVisible(false);
                } else if (combo_datasoureType.getSelectionIndex() == 1) {
                    composite_db.setVisible(false);
                    composite_hive.setVisible(true);
                    composite_file.setVisible(false);
                } else if (combo_datasoureType.getSelectionIndex() == 2) {
                    composite_db.setVisible(false);
                    composite_hive.setVisible(false);
                    composite_file.setVisible(true);
                }
            }
        });


        Label labal_insert_datasourceType = new Label(composite_insert, SWT.NONE);
        labal_insert_datasourceType.setBounds(20, 13, 90, 17);
        labal_insert_datasourceType.setText("目标数据源类型：");

        combo_insert_datasourceType = new Combo(composite_insert, SWT.READ_ONLY);
        combo_insert_datasourceType.setBounds(121, 10, 88, 25);
        combo_insert_datasourceType.setItems(new String[]{"数据库", "Hive", "文件", "UOM"});
        combo_insert_datasourceType.select(0);
        combo_insert_datasourceType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (combo_insert_datasourceType.getSelectionIndex() == 0) {
                    composite_insert_db.setVisible(true);
                    composite_insert_hive.setVisible(false);
                    composite_insert_file.setVisible(false);
                    composite_insert_uom.setVisible(false);
                } else if (combo_insert_datasourceType.getSelectionIndex() == 1) {
                    composite_insert_db.setVisible(false);
                    composite_insert_hive.setVisible(true);
                    composite_insert_file.setVisible(false);
                    composite_insert_uom.setVisible(false);
                } else if (combo_insert_datasourceType.getSelectionIndex() == 2) {
                    composite_insert_db.setVisible(false);
                    composite_insert_hive.setVisible(false);
                    composite_insert_file.setVisible(true);
                    composite_insert_uom.setVisible(false);
                } else if (combo_insert_datasourceType.getSelectionIndex() == 3) {
                    composite_insert_db.setVisible(false);
                    composite_insert_hive.setVisible(false);
                    composite_insert_file.setVisible(false);
                    composite_insert_uom.setVisible(true);
                }
            }
        });

        Label label_insert_source = new Label(composite_insert, SWT.NONE);
        label_insert_source.setBounds(230, 13, 75, 17);
        label_insert_source.setText("数据源选择：");

        combo_source = new Combo(composite_insert, SWT.READ_ONLY);
        combo_source.setBounds(310, 10, 90, 17);

        button_confirm_dest = new Button(composite_insert, SWT.NONE);
        button_confirm_dest.setBounds(430, 10, 90, 27);
        button_confirm_dest.setText("确认选择");
        button_confirm_dest.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (checkInsertIntegrity()) {
                    LinkedHashMap<String, Dataset<Row>> map = wholeNameToDataFrame.size() == 0 ? nameToDataFrame : wholeNameToDataFrame;
                    Dataset<Row> source = map.get(combo_source.getText());
                    Col[] colsSource = SourceInfoHelper.getSourceInfo(source);
                    Col[] colsDest = generateDataSourceInfo2();
                    refreshInsertMapTable(combo_insert_datasourceType, colsSource, colsDest);
                }

            }
        });

        // db的界面
        composite_insert_db = new Composite(composite_insert, SWT.NONE);
        composite_insert_db.setBounds(10, 41, 591, 230);

        composite_insert_hive = new Composite(composite_insert, SWT.NONE);
        composite_insert_hive.setBounds(10, 41, 591, 230);

        composite_insert_file = new Composite(composite_insert, SWT.NONE);
        composite_insert_file.setBounds(10, 41, 591, 230);

        composite_insert_uom = new Composite(composite_insert, SWT.NONE);
        composite_insert_uom.setBounds(10, 41, 591, 230);

        group_insert_map = new Group(composite_insert, SWT.NONE);
        group_insert_map.setLocation(20, 273);
        group_insert_map.setSize(571, 323);
        group_insert_map.setText("数据列映射插入信息");

        group_insert_db_connect = new Group(composite_insert_db, SWT.NONE);
        group_insert_db_connect.setLocation(10, 10);
        group_insert_db_connect.setSize(571, 203);
        group_insert_db_connect.setText("数据库");

        group_insert_hive_connect = new Group(composite_insert_hive, SWT.NONE);
        group_insert_hive_connect.setLocation(10, 10);
        group_insert_hive_connect.setSize(571, 203);
        group_insert_hive_connect.setText("Hive");

        group_insert_file_connect = new Group(composite_insert_file, SWT.NONE);
        group_insert_file_connect.setLocation(10, 10);
        group_insert_file_connect.setSize(571, 203);
        group_insert_file_connect.setText("文件");

        group_insert_uom_connect = new Group(composite_insert_uom, SWT.NONE);
        group_insert_uom_connect.setLocation(10, 10);
        group_insert_uom_connect.setSize(571, 203);
        group_insert_uom_connect.setText("UOM");

        Label label_insert_db_type = new Label(group_insert_db_connect, SWT.NONE);
        label_insert_db_type.setText("类型：");
        label_insert_db_type.setBounds(10, 26, 56, 17);

        combo_insert_db_type = new Combo(group_insert_db_connect, SWT.NONE);
        combo_insert_db_type.setItems(new String[]{"MySQL", "SQL Server", "Oracle"});
        combo_insert_db_type.setBounds(83, 23, 109, 25);
        combo_insert_db_type.select(0);

        Label label_insert_db_host = new Label(group_insert_db_connect, SWT.NONE);
        label_insert_db_host.setText("地址：");
        label_insert_db_host.setBounds(10, 61, 36, 17);

        text_insert_db_host = new Text(group_insert_db_connect, SWT.BORDER);
        text_insert_db_host.setBounds(83, 58, 109, 23);
        text_insert_db_host.setText("localhost");

        Label label_insert_db_name = new Label(group_insert_db_connect, SWT.NONE);
        label_insert_db_name.setText("数据库：");
        label_insert_db_name.setBounds(10, 92, 47, 17);

        text_insert_db_name = new Text(group_insert_db_connect, SWT.BORDER);
        text_insert_db_name.setBounds(83, 89, 109, 23);
        text_insert_db_name.setText("test");

        Label label_insert_db_username = new Label(group_insert_db_connect, SWT.NONE);
        label_insert_db_username.setText("用户名：");
        label_insert_db_username.setBounds(330, 92, 47, 17);

        text_insert_db_username = new Text(group_insert_db_connect, SWT.BORDER);
        text_insert_db_username.setBounds(400, 89, 109, 23);
        text_insert_db_username.setText("root");

        Label label_insert_db_password = new Label(group_insert_db_connect, SWT.NONE);
        label_insert_db_password.setText("密码：");
        label_insert_db_password.setBounds(10, 125, 47, 17);

        text_insert_db_password = new Text(group_insert_db_connect, SWT.BORDER | SWT.PASSWORD);
        text_insert_db_password.setBounds(83, 122, 109, 23);
        text_insert_db_password.setText("csasc");

        Label label_insert_db_mode = new Label(group_insert_db_connect, SWT.NONE);
        label_insert_db_mode.setText("写入模式：");
        label_insert_db_mode.setBounds(330, 125, 60, 17);

        combo_insert_db_mode = new Combo(group_insert_db_connect, SWT.BORDER | SWT.PASSWORD);
        combo_insert_db_mode.setBounds(400, 122, 109, 23);
        combo_insert_db_mode.setItems(new String[]{"覆盖", "追加"});
        combo_insert_db_mode.select(0);

        Label label_insert_db_port = new Label(group_insert_db_connect, SWT.NONE);
        label_insert_db_port.setText("端口：");
        label_insert_db_port.setBounds(330, 26, 47, 17);

        text_insert_db_port = new Text(group_insert_db_connect, SWT.BORDER);
        text_insert_db_port.setBounds(400, 23, 109, 23);
        text_insert_db_port.setText("3306");

        Label label_insert_db_table = new Label(group_insert_db_connect, SWT.NONE);
        label_insert_db_table.setText("表名：");
        label_insert_db_table.setBounds(330, 61, 47, 17);

        text_insert_db_table = new Text(group_insert_db_connect, SWT.BORDER);
        text_insert_db_table.setBounds(400, 58, 109, 23);
        text_insert_db_table.setText("type");


        Label label_insert_uom_brokerList = new Label(group_insert_uom_connect, SWT.NONE);
        label_insert_uom_brokerList.setText("服务器列表：");
        label_insert_uom_brokerList.setBounds(10, 26, 80, 17);

        text_insert_uom_brokerList = new Text(group_insert_uom_connect, SWT.BORDER);
        text_insert_uom_brokerList.setBounds(98, 23, 109, 25);
        text_insert_uom_brokerList.setText("localhost:9092");

        Label label_insert_uom_topic = new Label(group_insert_uom_connect, SWT.NONE);
        label_insert_uom_topic.setText("主题：");
        label_insert_uom_topic.setBounds(10, 61, 80, 17);

        text_insert_uom_topic = new Text(group_insert_uom_connect, SWT.BORDER);
        text_insert_uom_topic.setBounds(98, 58, 109, 23);
        text_insert_uom_topic.setText("test");

        Label label_keySer = new Label(group_insert_uom_connect, SWT.NONE);
        label_keySer.setText("Key序列化器：");
        label_keySer.setBounds(10, 92, 80, 17);

        text_insert_uom_keySer = new Text(group_insert_uom_connect, SWT.BORDER);
        text_insert_uom_keySer.setBounds(98, 89, 109, 23);
        text_insert_uom_keySer.setText("test");

        Label label_valueSer = new Label(group_insert_uom_connect, SWT.NONE);
        label_valueSer.setText("Value序列化器：");
        label_valueSer.setBounds(10, 125, 80, 17);

        text_insert_uom_valueSer = new Text(group_insert_uom_connect, SWT.BORDER);
        text_insert_uom_valueSer.setBounds(98, 122, 109, 23);
        text_insert_uom_valueSer.setText("root");

        Label label_topic_partitionCounts = new Label(group_insert_uom_connect, SWT.NONE);
        label_topic_partitionCounts.setText("主题分区数量：");
        label_topic_partitionCounts.setBounds(300, 26, 80, 17);

        text_insert_uom_partitionCounts = new Text(group_insert_uom_connect, SWT.BORDER);
        text_insert_uom_partitionCounts.setBounds(400, 23, 109, 23);
        text_insert_uom_partitionCounts.setText("1");

        Label label_topic_replication = new Label(group_insert_uom_connect, SWT.NONE);
        label_topic_replication.setText("主题复制因子：");
        label_topic_replication.setBounds(300, 61, 80, 17);

        text_insert_replication = new Text(group_insert_uom_connect, SWT.BORDER);
        text_insert_replication.setBounds(400, 58, 109, 23);
        text_insert_replication.setText("1");

        Label label_uom_mode = new Label(group_insert_uom_connect, SWT.NONE);
        label_uom_mode.setText("写入模式：");
        label_uom_mode.setBounds(300, 92, 80, 17);

        combo_uom_mode = new Combo(group_insert_uom_connect, SWT.BORDER);
        combo_uom_mode.setBounds(400, 89, 109, 23);
        combo_uom_mode.setItems(new String[]{"覆盖", "追加"});
        combo_uom_mode.select(0);


        Label label_insert_hive_host = new Label(group_insert_hive_connect, SWT.NONE);
        label_insert_hive_host.setText("地址：");
        label_insert_hive_host.setBounds(10, 26, 36, 17);

        text_insert_hive_host = new Text(group_insert_hive_connect, SWT.BORDER);
        text_insert_hive_host.setBounds(83, 23, 109, 25);
        text_insert_hive_host.setText("master");

        Label label_insert_hive_db_name = new Label(group_insert_hive_connect, SWT.NONE);
        label_insert_hive_db_name.setText("数据库:");
        label_insert_hive_db_name.setBounds(10, 71, 50, 17);

        text_insert_hive_db_name = new Text(group_insert_hive_connect, SWT.BORDER);
        text_insert_hive_db_name.setBounds(83, 68, 109, 23);
        text_insert_hive_db_name.setText("default");

        Label label_insert_hive_table = new Label(group_insert_hive_connect, SWT.NONE);
        label_insert_hive_table.setText("表名：");
        label_insert_hive_table.setBounds(330, 26, 47, 17);

        text_insert_hive_table = new Text(group_insert_hive_connect, SWT.BORDER);
        text_insert_hive_table.setBounds(400, 23, 109, 23);
        text_insert_hive_table.setText("test");

        Label label_insert_hive_port = new Label(group_insert_hive_connect, SWT.NONE);
        label_insert_hive_port.setText("端口:");
        label_insert_hive_port.setBounds(330, 71, 47, 17);

        text_insert_hive_port = new Text(group_insert_hive_connect, SWT.BORDER);
        text_insert_hive_port.setBounds(400, 68, 109, 23);
        text_insert_hive_port.setText("9083");

        Label label_insert_hive_mode = new Label(group_insert_hive_connect, SWT.NONE);
        label_insert_hive_mode.setText("写入模式:");
        label_insert_hive_mode.setBounds(10, 116, 67, 17);

        combo_insert_hive_mode = new Combo(group_insert_hive_connect, SWT.NONE);
        combo_insert_hive_mode.setBounds(83, 113, 109, 23);
        combo_insert_hive_mode.setItems(new String[]{"覆盖", "追加"});
        combo_insert_hive_mode.select(0);

        Label label_insert_file_type = new Label(group_insert_file_connect, SWT.NONE);
        label_insert_file_type.setText("类型：");
        label_insert_file_type.setBounds(10, 26, 36, 17);

        combo_insert_file_type = new Combo(group_insert_file_connect, SWT.NONE);
        combo_insert_file_type.setItems(new String[]{"csv", "tsv", "parquet"});
        combo_insert_file_type.setBounds(83, 23, 109, 25);
        combo_insert_file_type.select(0);

        Label label_insert_file_host = new Label(group_insert_file_connect, SWT.NONE);
        label_insert_file_host.setText("地址：");
        label_insert_file_host.setBounds(10, 71, 50, 17);

        text_insert_file_host = new Text(group_insert_file_connect, SWT.BORDER);
        text_insert_file_host.setBounds(83, 68, 109, 23);
        text_insert_file_host.setText("master");

        Label label_insert_file_path = new Label(group_insert_file_connect, SWT.NONE);
        label_insert_file_path.setText("文件路径：");
        label_insert_file_path.setBounds(330, 26, 60, 17);

        text_insert_file_path = new Text(group_insert_file_connect, SWT.BORDER);
        text_insert_file_path.setBounds(400, 23, 109, 23);
        text_insert_file_path.setText("/user/xhb");

        Label label_insert_file_port = new Label(group_insert_file_connect, SWT.NONE);
        label_insert_file_port.setText("端口:");
        label_insert_file_port.setBounds(330, 71, 47, 17);

        text_insert_file_port = new Text(group_insert_file_connect, SWT.BORDER);
        text_insert_file_port.setBounds(400, 68, 109, 23);
        text_insert_file_port.setText("9000");

        Label label_insert_file_mode = new Label(group_insert_file_connect, SWT.NONE);
        label_insert_file_mode.setText("写入模式:");
        label_insert_file_mode.setBounds(10, 116, 67, 17);

        combo_insert_file_mode = new Combo(group_insert_file_connect, SWT.NONE);
        combo_insert_file_mode.setBounds(83, 113, 109, 23);
        combo_insert_file_mode.setItems(new String[]{"覆盖", "追加"});
        combo_insert_file_mode.select(0);


        Button button_add_source = new Button(composite_4, SWT.NONE);
        button_add_source.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean checkIntegrity = checkIntegrity();
                if (checkIntegrity) {
                    if (combo_datasoureType.getSelectionIndex() == 0) {//DB
                        if (combo_db_addSource.getSelectionIndex() == 0) {
                            composite_group.setVisible(false);
                            composite_sql.setVisible(true);
                        } else if (combo_db_addSource.getSelectionIndex() == 1) {
                            composite_group.setVisible(true);
                            composite_sql.setVisible(false);
                            Col[] cols = generateDataSourceInfo();
                            refreshTable(cols);
                        }
                    } else if (combo_datasoureType.getSelectionIndex() == 1) {//Hive
                        if (combo_hive_addSource.getSelectionIndex() == 0) {
                            composite_group.setVisible(false);
                            composite_sql.setVisible(true);
                        } else if (combo_hive_addSource.getSelectionIndex() == 1) {
                            composite_group.setVisible(true);
                            composite_sql.setVisible(false);
                            Col[] cols = generateDataSourceInfo();
                            refreshTable(cols);
                        }
                    } else if (combo_datasoureType.getSelectionIndex() == 2) {//File
                        if (combo_file_addSource.getSelectionIndex() == 0) {
                            composite_group.setVisible(false);
                            composite_sql.setVisible(true);
                        } else if (combo_file_addSource.getSelectionIndex() == 1) {
                            composite_group.setVisible(true);
                            composite_sql.setVisible(false);
                            Col[] cols = generateDataSourceInfo();
                            refreshTable(cols);
                        }
                    }
                }
            }
        });
        button_add_source.setText("添加数据源");
        button_add_source.setBounds(330, 10, 80, 27);

        Button button_test_db_connect = new Button(composite_4, SWT.NONE);
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
        button_test_db_connect.setBounds(410, 10, 80, 27);

        Button button_syncConfig = new Button(composite_4, SWT.NONE);
        button_syncConfig.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (windowType == Constant.WindowType_Add)
                    new SyncConfigDialog(tempParagraph, syncConfig, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
                else
                    new SyncConfigDialog((Paragraph) node.data, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
            }
        });
        button_syncConfig.setBounds(490, 10, 80, 27);
        button_syncConfig.setText("同步配置");

        // db的界面
        composite_db = new Composite(composite_4, SWT.NONE);
        composite_db.setBounds(10, 41, 591, 200);

        composite_hive = new Composite(composite_4, SWT.NONE);
        composite_hive.setBounds(10, 41, 591, 200);

        composite_file = new Composite(composite_4, SWT.NONE);
        composite_file.setBounds(10, 41, 591, 200);

        group_db_connect = new Group(composite_db, SWT.NONE);
        group_db_connect.setLocation(10, 10);
        group_db_connect.setSize(571, 173);
        group_db_connect.setText("连接属性");

        group_hive_connect = new Group(composite_hive, SWT.NONE);
        group_hive_connect.setLocation(10, 10);
        group_hive_connect.setSize(571, 173);
        group_hive_connect.setText("连接属性");

        group_file_connect = new Group(composite_file, SWT.NONE);
        group_file_connect.setLocation(10, 10);
        group_file_connect.setSize(571, 173);
        group_file_connect.setText("连接属性");

        group_join_configure = new Group(composite_join, SWT.NONE);
        group_join_configure.setLocation(20, 30);
        group_join_configure.setSize(591, 220);
        group_join_configure.setText("数据间连接配置");

        group_join_info = new Group(composite_join, SWT.NONE);
        group_join_info.setLocation(20, 273);
        group_join_info.setSize(591, 323);
        group_join_info.setText("数据表连接信息");

        Label label_left_table = new Label(group_join_configure, SWT.NONE);
        label_left_table.setText("左表选择：");
        label_left_table.setBounds(10, 26, 66, 17);

        combo_join_left = new Combo(group_join_configure, SWT.NONE);
        combo_join_left.setItems(getCurrentTableList());
        combo_join_left.setBounds(123, 23, 109, 25);
        combo_join_left.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                combo_join_left_column.setItems(getCurrentTableColumns(combo_join_left.getText().trim()));
            }
        });

        Label label_join_tmpTable = new Label(group_join_configure, SWT.NONE);
        label_join_tmpTable.setText("临时表名称：");
        label_join_tmpTable.setBounds(10, 66, 66, 17);

        text_join_tmpTable = new Text(group_join_configure, SWT.BORDER);
        text_join_tmpTable.setBounds(123, 63, 109, 23);

        Label label_join_left_column = new Label(group_join_configure, SWT.NONE);
        label_join_left_column.setText("左表连接列选择：");
        label_join_left_column.setBounds(10, 106, 90, 17);

        combo_join_left_column = new Combo(group_join_configure, SWT.BORDER);
        combo_join_left_column.setBounds(123, 103, 109, 23);

        Label label_join_eq = new Label(group_join_configure, SWT.NONE);
        label_join_eq.setText("连接符号选择：");
        label_join_eq.setBounds(10, 149, 90, 17);

        combo_join_eq = new Combo(group_join_configure, SWT.BORDER);
        combo_join_eq.setBounds(123, 142, 109, 23);
        combo_join_eq.setItems(new String[]{"===", ">=", "<=", ">", "<"});
        combo_join_eq.select(0);

        Label label_right_table = new Label(group_join_configure, SWT.NONE);
        label_right_table.setText("右表选择：");
        label_right_table.setBounds(326, 26, 66, 17);

        combo_join_right = new Combo(group_join_configure, SWT.NONE);
        combo_join_right.setItems(getCurrentTableList());
        combo_join_right.setBounds(439, 23, 109, 25);
        combo_join_right.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                combo_join_right_column.setItems(getCurrentTableColumns(combo_join_right.getText().trim()));
            }
        });

        Label label_join_type = new Label(group_join_configure, SWT.NONE);
        label_join_type.setText("连接类型：");
        label_join_type.setBounds(326, 66, 66, 17);

        combo_join_type = new Combo(group_join_configure, SWT.BORDER);
        combo_join_type.setBounds(439, 63, 109, 23);
        combo_join_type.setItems(new String[]{"left", "right", "inner", "full"});
        combo_join_type.select(0);

        Label label_join_right_column = new Label(group_join_configure, SWT.NONE);
        label_join_right_column.setText("右表连接列选择：");
        label_join_right_column.setBounds(326, 106, 90, 17);

        combo_join_right_column = new Combo(group_join_configure, SWT.BORDER);
        combo_join_right_column.setBounds(439, 103, 109, 23);

        Label label_using = new Label(group_join_configure, SWT.NONE);
        label_using.setText("使用using连接：");
        label_using.setBounds(326, 145, 90, 17);

        combo_using = new Combo(group_join_configure, SWT.BORDER);
        combo_using.setBounds(439, 142, 109, 23);
        combo_using.setItems(new String[]{"false", "true"});
        combo_using.select(1);

        Button button_add_join = new Button(group_join_configure, SWT.NONE);
        button_add_join.setBounds(460, 180, 90, 27);
        button_add_join.setText("添加连接信息");
        button_add_join.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (checkJoinInfoIntegrity()) {
                    System.out.println(combo_using.getText().trim());
                    JoinInfo info = JoinInfo.builder().left(combo_join_left.getText().trim()).right(combo_join_right.getText().trim())
                            .using(combo_using.getText().trim()).tempTableName(text_join_tmpTable.getText().trim())
                            .joinType(combo_join_type.getText().trim()).build();
                    String s = "";
                    String join = combo_join_left_column.getText().trim() + combo_join_eq.getText().trim() + combo_join_right_column.getText().trim();
                    if (joinInfos.contains(info)) {
                        JoinInfo temp = joinInfos.get(joinInfos.indexOf(info));
                        if (!temp.getJoinInfo().contains(join)) {
                            s = temp.getJoinInfo() + "&&" + join;
                            temp.setJoinInfo(s);
                        }
                        temp.setLeft(combo_join_left.getText().trim());
                        temp.setRight(combo_join_right.getText().trim());
                        temp.setJoinType(combo_join_type.getText().trim());
                        temp.setUsing(combo_using.getText().trim());
                    } else {
                        info.setJoinInfo(join);
                        joinInfos.add(info);
                    }
                    initTempTableDataframe();
                    combo_join_left.setItems(getCurrentTableList());
                    combo_join_right.setItems(getCurrentTableList());
                    combo_source.setItems(getCurrentTableList());
                    refreshJoinTable(joinInfos);
                }
            }
        });

        Label lblDb = new Label(group_db_connect, SWT.NONE);
        lblDb.setText("类型：");
        lblDb.setBounds(10, 26, 36, 17);

        combo_db_type = new Combo(group_db_connect, SWT.NONE);
        combo_db_type.setItems(new String[]{"MySQL", "SQL Server", "Oracle"});
        combo_db_type.setBounds(83, 23, 109, 25);
        combo_db_type.select(0);

        Label label_hive_host = new Label(group_hive_connect, SWT.NONE);
        label_hive_host.setText("地址：");
        label_hive_host.setBounds(10, 26, 36, 17);

        text_hive_host = new Text(group_hive_connect, SWT.BORDER);
        text_hive_host.setBounds(83, 23, 109, 25);
        text_hive_host.setText("master");

        Label label_file_type = new Label(group_file_connect, SWT.NONE);
        label_file_type.setText("类型：");
        label_file_type.setBounds(10, 26, 36, 17);

        combo_file_type = new Combo(group_file_connect, SWT.NONE);
        combo_file_type.setItems(new String[]{"csv", "tsv", "parquet"});
        combo_file_type.setBounds(83, 23, 109, 25);
        combo_file_type.select(0);

        Label label_10 = new Label(group_db_connect, SWT.NONE);
        label_10.setText("地址：");
        label_10.setBounds(10, 56, 36, 17);

        text_db_host = new Text(group_db_connect, SWT.BORDER);
        text_db_host.setBounds(83, 53, 109, 23);
        text_db_host.setText("localhost");

        Label label_hive_db_name = new Label(group_hive_connect, SWT.NONE);
        label_hive_db_name.setText("数据库:");
        label_hive_db_name.setBounds(10, 56, 36, 17);

        text_hive_db_name = new Text(group_hive_connect, SWT.BORDER);
        text_hive_db_name.setBounds(83, 53, 109, 23);
        text_hive_db_name.setText("default");

        Label label_file_host = new Label(group_file_connect, SWT.NONE);
        label_file_host.setText("地址：");
        label_file_host.setBounds(10, 56, 36, 17);

        text_file_host = new Text(group_file_connect, SWT.BORDER);
        text_file_host.setBounds(83, 53, 109, 23);
        text_file_host.setText("master");

        Label label_12 = new Label(group_db_connect, SWT.NONE);
        label_12.setText("数据库：");
        label_12.setBounds(10, 82, 47, 17);

        text_db_name = new Text(group_db_connect, SWT.BORDER);
        text_db_name.setBounds(83, 79, 109, 23);
        text_db_name.setText("test");

        Label label_hive_table = new Label(group_hive_connect, SWT.NONE);
        label_hive_table.setText("表名：");
        label_hive_table.setBounds(10, 82, 47, 17);

        text_hive_table = new Text(group_hive_connect, SWT.BORDER);
        text_hive_table.setBounds(83, 79, 109, 23);
        text_hive_table.setText("test");

        Label label_file_path = new Label(group_file_connect, SWT.NONE);
        label_file_path.setText("文件路径：");
        label_file_path.setBounds(10, 82, 55, 17);

        text_file_path = new Text(group_file_connect, SWT.BORDER);
        text_file_path.setBounds(83, 79, 109, 23);
        text_file_path.setText("/user/xhb");

        Label label_14 = new Label(group_db_connect, SWT.NONE);
        label_14.setText("用户名：");
        label_14.setBounds(10, 110, 47, 17);

        text_db_username = new Text(group_db_connect, SWT.BORDER);
        text_db_username.setBounds(83, 107, 109, 23);
        text_db_username.setText("root");

        Label label_hive_port = new Label(group_hive_connect, SWT.NONE);
        label_hive_port.setText("端口:");
        label_hive_port.setBounds(10, 110, 47, 17);

        text_hive_port = new Text(group_hive_connect, SWT.BORDER);
        text_hive_port.setBounds(83, 107, 109, 23);
        text_hive_port.setText("9083");

        Label label_file_port = new Label(group_file_connect, SWT.NONE);
        label_file_port.setText("端口:");
        label_file_port.setBounds(10, 110, 47, 17);

        text_file_port = new Text(group_file_connect, SWT.BORDER);
        text_file_port.setBounds(83, 107, 109, 23);
        text_file_port.setText("9000");

        Label label_15 = new Label(group_db_connect, SWT.NONE);
        label_15.setText("密码：");
        label_15.setBounds(10, 139, 36, 17);

        text_db_password = new Text(group_db_connect, SWT.BORDER | SWT.PASSWORD);
        text_db_password.setBounds(83, 136, 109, 23);
        text_db_password.setText("csasc");

        Label label_hive_whereClause = new Label(group_hive_connect, SWT.NONE);
        label_hive_whereClause.setText("过滤条件：");
        label_hive_whereClause.setBounds(10, 139, 55, 17);

        text_hive_whereClause = new Text(group_hive_connect, SWT.BORDER | SWT.PASSWORD);
        text_hive_whereClause.setBounds(83, 136, 109, 23);

        Label label_file_whereClause = new Label(group_file_connect, SWT.NONE);
        label_file_whereClause.setText("过滤条件：");
        label_file_whereClause.setBounds(10, 139, 36, 17);

        text_file_whereClause = new Text(group_file_connect, SWT.BORDER | SWT.PASSWORD);
        text_file_whereClause.setBounds(83, 136, 109, 23);

        Label label_11 = new Label(group_db_connect, SWT.NONE);
        label_11.setText("端口：");
        label_11.setBounds(200, 26, 47, 17);

        text_db_port = new Text(group_db_connect, SWT.BORDER);
        text_db_port.setBounds(250, 23, 109, 23);
        text_db_port.setText("3306");

        Label label_13 = new Label(group_db_connect, SWT.NONE);
        label_13.setText("表名：");
        label_13.setBounds(200, 56, 47, 17);

        text_db_table = new Text(group_db_connect, SWT.BORDER);
        text_db_table.setBounds(250, 53, 109, 23);
        text_db_table.setText("type");


        Label label_hive_timestampColumn = new Label(group_hive_connect, SWT.NONE);
        label_hive_timestampColumn.setText("时间戳列：");
        label_hive_timestampColumn.setBounds(250, 26, 55, 17);

        text_hive_timestamp = new Text(group_hive_connect, SWT.BORDER);
        text_hive_timestamp.setBounds(320, 23, 109, 23);

        Label label_file_timestampColumn = new Label(group_file_connect, SWT.NONE);
        label_file_timestampColumn.setText("时间戳列：");
        label_file_timestampColumn.setBounds(250, 26, 55, 17);

        text_file_timestamp = new Text(group_file_connect, SWT.BORDER);
        text_file_timestamp.setBounds(320, 23, 109, 23);


        Label label_hive_time = new Label(group_hive_connect, SWT.NONE);
        label_hive_time.setText("时间：");
        label_hive_time.setBounds(250, 56, 47, 17);

        text_hive_cDateTime = new CDateTime(group_hive_connect, CDT.DATE_SHORT | CDT.TIME_SHORT | CDT.DROP_DOWN);
        text_hive_cDateTime.setPattern("yyyy-MM-dd HH:mm:ss");
        text_hive_cDateTime.setBounds(320, 56, 145, 23);

        Label label_file_time = new Label(group_file_connect, SWT.NONE);
        label_file_time.setText("时间：");
        label_file_time.setBounds(250, 56, 47, 17);

        text_file_cDateTime = new CDateTime(group_file_connect, CDT.DATE_SHORT | CDT.TIME_SHORT | CDT.DROP_DOWN);
        text_file_cDateTime.setPattern("yyyy-MM-dd HH:mm:ss");
        text_file_cDateTime.setBounds(320, 56, 145, 23);

        Label label_partitionColumn = new Label(group_db_connect, SWT.NONE);
        label_partitionColumn.setText("分区列:");
        label_partitionColumn.setBounds(200, 82, 47, 23);

        Label label_hive_addSource = new Label(group_hive_connect, SWT.NONE);
        label_hive_addSource.setText("添加方法:");
        label_hive_addSource.setBounds(250, 86, 55, 17);

        combo_hive_addSource = new Combo(group_hive_connect, SWT.NONE);
        combo_hive_addSource.setItems(new String[]{"编写Sql语句", "选择字段"});
        combo_hive_addSource.setBounds(320, 83, 109, 25);
        combo_hive_addSource.select(0);

        Label label_file_addSource = new Label(group_file_connect, SWT.NONE);
        label_file_addSource.setText("添加方法:");
        label_file_addSource.setBounds(250, 86, 55, 17);

        combo_file_addSource = new Combo(group_file_connect, SWT.NONE);
        combo_file_addSource.setItems(new String[]{"编写Sql语句", "选择字段"});
        combo_file_addSource.setBounds(320, 83, 109, 25);
        combo_file_addSource.select(0);

        text_partitionColumn = new Text(group_db_connect, SWT.BORDER);
        text_partitionColumn.setBounds(250, 79, 109, 23);

        Label label_lowerBound = new Label(group_db_connect, SWT.NONE);
        label_lowerBound.setText("下界:");
        label_lowerBound.setBounds(200, 110, 47, 23);

        text_lowerBound = new Text(group_db_connect, SWT.BORDER);
        text_lowerBound.setBounds(250, 107, 109, 23);

        Label label_upperBound = new Label(group_db_connect, SWT.NONE);
        label_upperBound.setText("上界:");
        label_upperBound.setBounds(200, 139, 47, 23);

        text_upperBound = new Text(group_db_connect, SWT.BORDER);
        text_upperBound.setBounds(250, 136, 109, 23);

        Label label_partitionCount = new Label(group_db_connect, SWT.NONE);
        label_partitionCount.setText("分区数量:");
        label_partitionCount.setBounds(370, 26, 55, 23);

        text_partitionCount = new Text(group_db_connect, SWT.BORDER);
        text_partitionCount.setBounds(436, 23, 109, 23);

        Label label_whereClause = new Label(group_db_connect, SWT.NONE);
        label_whereClause.setText("过滤条件:");
        label_whereClause.setBounds(370, 55, 55, 23);

        text_whereClause = new Text(group_db_connect, SWT.BORDER);
        text_whereClause.setBounds(436, 52, 109, 23);

        Label label_timestampColumn = new Label(group_db_connect, SWT.NONE);
        label_timestampColumn.setText("时间戳列:");
        label_timestampColumn.setBounds(370, 84, 55, 23);

        text_timestamp = new Text(group_db_connect, SWT.BORDER);
        text_timestamp.setBounds(436, 81, 109, 23);

        Label label_time = new Label(group_db_connect, SWT.NONE);
        label_time.setText("时间:");
        label_time.setBounds(370, 113, 40, 23);

        cDateTime = new CDateTime(group_db_connect, CDT.DATE_SHORT | CDT.TIME_SHORT | CDT.DROP_DOWN);
        cDateTime.setPattern("yyyy-MM-dd HH:mm:ss");
        cDateTime.setBounds(411, 113, 139, 23);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse("2020-04-16 20:15:31");
        } catch (Exception e) {

        }
        cDateTime.setSelection(date);

        Label label_addSource = new Label(group_db_connect, SWT.NONE);
        label_addSource.setText("添加方法:");
        label_addSource.setBounds(370, 139, 55, 23);

        combo_db_addSource = new Combo(group_db_connect, SWT.NONE);
        combo_db_addSource.setItems(new String[]{"编写Sql语句", "选择字段"});
        combo_db_addSource.setBounds(436, 136, 109, 25);
        combo_db_addSource.select(0);

        Group group_db_data = new Group(composite_4, SWT.NONE);
        group_db_data.setLocation(20, 239);
        group_db_data.setSize(571, 300);
        group_db_data.setText("添加数据源");

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;

        composite_group = new Composite(group_db_data, SWT.NONE);
        composite_group.setVisible(false);
        composite_group.setLayout(gridLayout);
        composite_group.setBounds(5, 20, 560, 270);

        composite_sql = new Composite(group_db_data, SWT.NONE);
        composite_sql.setVisible(false);
        composite_sql.setBounds(5, 20, 560, 270);

        Label label_sql = new Label(composite_sql, SWT.NONE);
        label_sql.setBounds(10, 18, 54, 17);
        label_sql.setText("SQL语句：");

        text_db_sql = new Text(composite_sql, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        text_db_sql.setBounds(10, 43, 530, 170);

        button_join_enter = new Button(group_join_info, SWT.NONE);
        button_join_enter.setVisible(true);
        button_join_enter.setText("确认连接属性");
        button_join_enter.setBounds(470, 285, 90, 30);
        button_join_enter.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
            }
        });
        button_join_enter.setVisible(false);

        button_select_enter = new Button(composite_group, SWT.NONE);
        button_select_enter.setVisible(true);
        button_select_enter.setText("确认添加");
        button_select_enter.setBounds(470, 240, 70, 30);
        button_select_enter.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ArrayList<Col2> cols = getColumnList();
                if (cols.size() == 0) {
                    showMessage("请选择至少一列数据：", shell);
                    return;
                }
                addDataSource();
                refreshTable_source();
            }
        });

        button_confirm_insert = new Button(group_insert_map, SWT.NONE);
        button_confirm_insert.setVisible(false);
        button_confirm_insert.setText("确认");
        button_confirm_insert.setBounds(470, 280, 90, 30);
        button_confirm_insert.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (checkTableIntegrity()) {
                    InsertMapInfo.InsertMapInfoBuilder builder = InsertMapInfo.builder();
                    InsertMapInfo insertMapInfo = createInsertInfo(builder);
                    String s = JSONObject.toJSONString(insertMapInfo);
                    getIntegrateModel(insertMapInfo);

                }
            }
        });

        button_enter = new Button(composite_sql, SWT.NONE);
        button_enter.setVisible(true);
        button_enter.setText("确认添加");
        button_enter.setBounds(470, 230, 70, 30);
        button_enter.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (text_db_sql.getText().trim().equals("")) {
                    showMessage("Sql语句不能为空", shell);
                    return;
                }
                addDataSource();
                refreshTable_source();
            }
        });

        button_config = new Button(composite_group, SWT.NONE);
        button_config.setVisible(false);
        button_config.setText("配置字段");
        button_config.setBounds(330, 235, 70, 30);
        button_config.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                final int total = table2.getItemCount();
                // 循环所有行
                boolean hasSelected = false;
                if (!selected_columns.isEmpty()) {
                    selected_columns.clear();
                }
                for (int i = 0; i < total; i++) {
                    TableItem item = table2.getItem(i);
                    if (item.getChecked()) {
                        hasSelected = true;
                        Column col = new Column();
                        col.setColumnName(item.getText(0));
                        col.setColumnTypeName(item.getText(1));
                        col.setPrecision(item.getText(2));
                        col.setScale(item.getText(3));
                        selected_columns.add(col);
                    }
                }
                if (!hasSelected) {
                    MessageBox box = new MessageBox(shell);
                    box.setMessage("选择要配置的字段信息");
                    box.open();
                    return;
                }
                Display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                        DataSourceDialog dialog = new DataSourceDialog(Constant.WindowType_Add, shell,
                                SWT.CLOSE | SWT.APPLICATION_MODAL, selected_columns);
                        dialog.open();
                        if (dialog.columnMap.size() != selected_columns.size()) {
                            return;
                        }

                        List<Integer> selectedIndex = new ArrayList<Integer>();
                        for (int i = 0; i < total; i++) {

                            TableItem item = table2.getItem(i);
                            if (item.getChecked()) {
                                selectedIndex.add(i);
                            }
                        }
                        int[] a = MapUtil.getIntArray(selectedIndex);
                        table2.remove(a);
                        MapUtil.show3(dialog.tableMap);
                        tableMap = dialog.tableMap;
//                        addComposite(tableMap);
                        list.add(tableMap);
                    }
                });
            }
        });

        group_test = new Group(composite_4, SWT.NONE);
        group_test.setBounds(20, 545, 571, 340);
        group_test.setText("数据源列表");
        GridLayout gridLayout3 = new GridLayout();
        gridLayout3.numColumns = 1;
        group_test.setLayout(gridLayout3);

        button_next = new Button(shell, SWT.NONE);
        if (this.windowType == Constant.WindowType_Add) {
            button_next.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (list.size() == 0) {
                        showMessage("请先确定目标表与各数据源字段的对应关系", shell);
                        return;
                    }
                    Display.getDefault().asyncExec(new Runnable() {
                        public void run() {
                            JoinListDialog dialog = new JoinListDialog(Constant.WindowType_Add, shell,
                                    SWT.Close | SWT.APPLICATION_MODAL, list);
                            dialog.open();
                            if (dialog.wholePairList.size() > 0) {
                                wholePairList = dialog.wholePairList;
                            }
                        }
                    });
                }
            });
        } else {//如果为编辑段
            button_next.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (list.size() == 0) {
                        showMessage("请先确定目标表与各数据源字段的对应关系", shell);
                        return;
                    }
                    Display.getDefault().asyncExec(new Runnable() {
                        public void run() {
                            JoinListDialog dialog = new JoinListDialog(Constant.WindowType_Edit, shell,
                                    SWT.Close | SWT.APPLICATION_MODAL, list, wholePairList);
                            dialog.open();
                        }
                    });
                }
            });
        }
        button_next.setBounds(278, 1702, 80, 27);
        button_next.setVisible(false);
        button_next.setText("下一步");

        button_withoutJoin = new Button(shell, SWT.None);
        button_withoutJoin.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (windowType == Constant.WindowType_Add) {

                    String type = combo_db_type.getText().trim();
                    String host = text_db_host.getText().trim();
                    String port = text_db_port.getText().trim();
                    String dbName = text_db_name.getText().trim();
                    String user = text_db_username.getText().trim();
                    String password = text_db_password.getText().trim();
                    String table = text_db_table.getText().trim();
                    String tableDest = type + "###" + host + "###" + dbName + "###" + user + "###" + password + "###"
                            + port + "###" + table;
                    if (tempParagraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_1)
                            && tempParagraph.getWarmSyncDetail().equals("")) {
                        showMessage("请填写温同步完整时间配置！", shell);
                        return;
                    }
                    if (wholePairList.size() > 0) {
                        showMessage("请选择连接集成", shell);
                        return;
                    }
                    if (list.size() == 1) {
                        IntegrateMain main = null;
                        main = new IntegrateMain(list, tableDest);// 序列化时只需要序列化这个对象即可
//						FileUtil.WriteMain(main, table);
                        FileUtil.WriteMainToJson(main, table);

                    } else if (list.size() == 0) {
                        showMessage("请先填写至少一项的数据源对应信息", shell);
                        return;
                    } else {
                        showMessage("请选择连接信息，且使用连接集成", shell);
                        return;
                    }

                    GriElement paragraph = node.manager.addGriElement(node.root, (GriElement) node.data,
                            new Paragraph2(table, "", "", tempParagraph.getSyncTimeType(),
                                    tempParagraph.getSyncDirectionType(), tempParagraph.getWarmSyncDetail(), type,
                                    tableDest, "", "", "", "", "", ""),
                            false);// 此处返回了paragraph，并且实际上已经执行了setID方法
                    if (paragraph == null)
                        showMessage("段创建失败！", shell);
                    else {
                        boolean result = node.manager.forceSyncData(paragraph.getId());//强制同步数据,如果采用上面注释段部分的代码进行同步，
                        //则会同时在引擎和管理器执行同一个同步方法，引擎中同步方法的类锁对不同包的类调用无效，因此必须得使用这种方法进行调用
                        if (result) {
                            MainWindow.treeViewer_globalView.add(node,
                                    new GlobalViewTreeNode(paragraph, node.root, node.manager));// 最后在树节点中添加段
                        }
                        shell.close();
                    }
                } else {
                    showMessage("Hello!", shell);
                    shell.close();
                }
            }
        });
        button_withoutJoin.setVisible(false);
        button_withoutJoin.setText("无连接集成");

        button_ok = new Button(shell, SWT.None);
        button_ok.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (windowType == Constant.WindowType_Add) {
                    String type = combo_db_type.getText().trim();
                    String host = text_db_host.getText().trim();
                    String port = text_db_port.getText().trim();
                    String dbName = text_db_name.getText().trim();
                    String user = text_db_username.getText().trim();
                    String password = text_db_password.getText().trim();
                    String table = text_db_table.getText().trim();
                    String tableDest = type + "###" + host + "###" + dbName + "###" + user + "###" + password + "###"
                            + port + "###" + table;
                    if (tempParagraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_1)
                            && tempParagraph.getWarmSyncDetail().equals("")) {
                        showMessage("请填写温同步完整时间配置！", shell);
                        return;
                    }
//					IntegrateMain main = null;
                    if (list.size() > 0 && wholePairList.size() > 0) {
                        IntegrateMain main = null;
                        main = new IntegrateMain(list, wholePairList, tableDest);// 序列化时只需要序列化这个对象即可
//						FileUtil.WriteMain(main, table);
                        FileUtil.WriteMainToJson(main, table);

                    } else {
                        showMessage("请先填写完整的数据源对应信息及相关连接信息", shell);
                        return;
                    }

                    GriElement paragraph = node.manager.addGriElement(node.root, (GriElement) node.data,
                            new Paragraph3(table, "", "", tempParagraph.getSyncTimeType(),
                                    tempParagraph.getSyncDirectionType(), tempParagraph.getWarmSyncDetail(), type,
                                    tableDest, "", "", "", "", "", ""),
                            false);// 此处返回了paragraph，并且实际上已经执行了setID方法
                    if (paragraph == null)
                        showMessage("段创建失败！", shell);
                    else {
                        boolean result = node.manager.forceSyncData(paragraph.getId());//强制同步数据,如果采用上面注释段部分的代码进行同步，
                        //则会同时在引擎和管理器执行同一个同步方法，引擎中同步方法的类锁对不同包的类调用无效，因此必须得使用这种方法进行调用
                        if (result) {
                            MainWindow.treeViewer_globalView.add(node,
                                    new GlobalViewTreeNode(paragraph, node.root, node.manager));// 最后在树节点中添加段
                        }

                        shell.close();
                    }
                } else {
                    showMessage("Hello!", shell);
                    shell.close();
                }
            }
        });
        button_ok.setVisible(false);
        button_ok.setText("连接集成");

        button_cancel = new Button(shell, SWT.NONE);
        button_cancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                nameToSource.clear();
                TableItem[] items = table3.getItems();
                for (int i = 0; i < items.length; i++) {
                    JSONObject json = JSON.parseObject(items[i].getText(1));
                    if (json.getString("sourceType").toString().equalsIgnoreCase("Database")) {
                        DBSourceInfo sourceInfo = JSON.parseObject(items[i].getText(1), DBSourceInfo.class);
                        sourceInfo.setAlias(items[i].getText(0));
                        nameToSource.put(items[i].getText(0), sourceInfo);
                    } else if (json.getString("sourceType").toString().equalsIgnoreCase("Hive")) {
                        HiveSourceInfo sourceInfo = JSON.parseObject(items[i].getText(1), HiveSourceInfo.class);
                        sourceInfo.setAlias(items[i].getText(0));
                        nameToSource.put(items[i].getText(0), JSON.parseObject(items[i].getText(1), HiveSourceInfo.class));
                    } else if (json.getString("sourceType").toString().equalsIgnoreCase("File")) {
                        FileSourceInfo sourceInfo = JSON.parseObject(items[i].getText(1), FileSourceInfo.class);
                        sourceInfo.setAlias(items[i].getText(0));
                        nameToSource.put(items[i].getText(0), JSON.parseObject(items[i].getText(1), FileSourceInfo.class));
                    }
                }
                initNameToDF();
                combo_join_left.setItems(getCurrentTableList());
                combo_join_right.setItems(getCurrentTableList());
                combo_source.setItems(getCurrentTableList());
            }
        });
        button_cancel.setText("取消");

        button_ok2 = new Button(shell, SWT.NONE);
        button_ok2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                GriElement paragraph = node.manager.addGriElement(node.root, (GriElement) node.data,
                        new Paragraph3(text_name.getText().trim(), "", "", tempParagraph.getSyncTimeType(),
                                tempParagraph.getSyncDirectionType(), tempParagraph.getWarmSyncDetail(), "",
                                "", "", "", "", "", "", ""),
                        false);// 此处返回了paragraph，并且实际上已经执行了setID方法
                if (paragraph == null)
                    showMessage("段创建失败！", shell);
                else {
                    boolean result = node.manager.forceSyncData(paragraph.getId());//强制同步数据,如果采用上面注释段部分的代码进行同步，
                    //则会同时在引擎和管理器执行同一个同步方法，引擎中同步方法的类锁对不同包的类调用无效，因此必须得使用这种方法进行调用
                    if (result) {
                        MainWindow.treeViewer_globalView.add(node,
                                new GlobalViewTreeNode(paragraph, node.root, node.manager));// 最后在树节点中添加段
                    }
                    shell.close();
                }
            }
        });
        button_ok2.setText("确认完成配置");

    }

    private void loadData() {// 如果是编辑段，则加载数据
        shell.setText("段3属性");
        Paragraph paragraph;
        if (node.data instanceof Paragraph3) {
            paragraph = (Paragraph3) node.data;
        } else {
            paragraph = (Paragraph2) node.data;
        }
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
        combo_datasoureType.select(1);
        composite_db.setVisible(true);
        String[] db_conn_info = paragraph.getDataSourcePath().split("###");
        combo_db_type.setText(db_conn_info[0]);
        text_db_host.setText(db_conn_info[1]);
        text_db_name.setText(db_conn_info[2]);
        text_db_username.setText(db_conn_info[3]);
        text_db_password.setText(db_conn_info[4]);
        text_db_port.setText(db_conn_info[5]);
        text_db_table.setText(db_conn_info[6]);

        List<String> files = FileUtil.findFilebyName(Constant.propertiesFolder, paragraph.getName());
//		IntegrateMain main=FileUtil.ReadMain(Constant.propertiesFolder+files.get(0));
        if (files != null && files.size() != 0) {
            IntegrateMain main = FileUtil.ReadMainFromJson(Constant.propertiesFolder + files.get(0));
            list = main.getList();
            wholePairList = main.getWholePairList();
//		if(wholePairList.size()==0) {
//			button_next.setVisible(false);
//		}
            for (Map<String, Map<String, Column>> map : list) {
//                addComposite(map);
            }
        } else {
            showMessage("配置文件读取失败，请确认配置文件是否存在且在正确路径上", shell);
        }
        if (windowType == Constant.WindowType_Property) {
            button_ok.setVisible(false);
            button_cancel.setText("关闭");
        }
        button_config.setVisible(false);

    }

    private void showMessage(String message, Shell shell) {
        MessageBox box = new MessageBox(shell);
        box.setMessage(message);
        box.open();
    }

    private boolean refreshTable_source() {
        //刷新时需要先dispose控件，否则不会刷新。
        if (table3 != null) {
            table3.dispose();
        }
        initTable_source();
        TableItem[] tableItems = table3.getItems();
        for (int i = 0; i < tableItems.length; i++) {
            tableItems[i].dispose();//释放
        }
        String[] tableHeader = {"数据源别名        ", "数据源信息                   "};
        for (int i = 0; i < tableHeader.length; i++) {
            TableColumn tableColumn = new TableColumn(table3, SWT.NONE);
            tableColumn.setText(tableHeader[i]);
            tableColumn.setMoveable(true);
        }
        DataSourceInfo[] infos = new DataSourceInfo[dataSourceList.size()];
        dataSourceList.toArray(infos);
        for (int i = 0; i < infos.length; i++) {
            DataSourceInfo info = infos[i];
            TableItem item = new TableItem(table3, SWT.NONE);
            System.out.println(JSON.toJSONString(info));
            item.setText(new String[]{"", JSON.toJSONString(info)});
        }

        for (int i = 0; i < infos.length; i++) {
            TableEditor editor = new TableEditor(table3);
            Text text = new Text(table3, SWT.NONE);
            editor.grabHorizontal = true;
            editor.setEditor(text, table3.getItem(i), 0);
            text.addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent modifyEvent) {
                    editor.getItem().setText(0, text.getText());
                }
            });
        }
        for (int i = 0; i < 2; i++) {
            table3.getColumn(i).pack();
        }
        table3.setSize(550, 300);
        index++;
        return true;
    }

    private boolean refreshInsertMapTable(Combo combo, Col[] colSrc, Col[] colDes) {
        button_confirm_insert.setVisible(true);
        if (table5 != null) {
            table5.dispose();
        }
        initTable_InsertMap();
        String[] tableHeader = null;
        if (combo.getSelectionIndex() == 0) {
            tableHeader = new String[]{"目标表的映射目标列     ", "目标列数据类型     ", "映射至目标表的数据源列    ", "数据源列数据类型    "};
            ;
        } else {
            tableHeader = new String[]{"映射至目标表的数据源列                             ", "数据源列数据类型                       "};
        }
        for (int i = 0; i < tableHeader.length; i++) {
            TableColumn tableColumn = new TableColumn(table5, SWT.NONE);
            tableColumn.setText(tableHeader[i]);
            tableColumn.setMoveable(true);
        }

        if (combo.getSelectionIndex() != 0) {
            for (int i = 0; i < colSrc.length; i++) {
                TableItem item = new TableItem(table5, SWT.NONE);
                item.setText(new String[]{colSrc[i].getName(), colSrc[i].getType().typeName()});
            }
        } else {
            for (int i = 0; i < colDes.length; i++) {
                TableItem item = new TableItem(table5, SWT.NONE);
                item.setText(new String[]{colDes[i].getName(), colDes[i].getType().typeName(), "", ""});
            }
        }
        if (combo.getSelectionIndex() == 0) {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < colSrc.length; i++) {
                map.put(colSrc[i].getName(), colSrc[i].getType().typeName());
            }

            for (int i = 0; i < colDes.length; i++) {
                TableEditor editor = new TableEditor(table5);
                Combo src = new Combo(table5, SWT.NONE);
                src.setItems(Arrays.stream(colSrc).map(x -> x.getName()).toArray(String[]::new));
                editor.grabHorizontal = true;
                editor.setEditor(src, table5.getItem(i), 2);
                src.addModifyListener(new ModifyListener() {
                    @Override
                    public void modifyText(ModifyEvent modifyEvent) {
                        editor.getItem().setText(2, src.getText());
                        editor.getItem().setText(3, map.get(src.getText()));
                    }
                });
            }
        }

        for (int i = 0; i < tableHeader.length; i++) {
            table5.getColumn(i).pack();
        }
        table5.setSize(550, 260);
        return true;
    }

    private boolean checkTableIntegrity() {
        TableItem[] items = table5.getItems();
        MessageBox box = new MessageBox(shell);
        if (combo_insert_datasourceType.getSelectionIndex() == 0) {
            insertList.clear();
            for (int i = 0; i < items.length; i++) {
                if (items[i].getChecked()) {
                    if (items[i].getText(2).equals("") || items[i].getText(3).equals("")) {
                        box.setMessage("参数填写不完整，请选择数据源列");
                        box.open();
                        insertList.clear();
                        return false;
                    }
                    insertList.add(items[i].getText(2) + "->" + items[i].getText(0));
                }
            }
        } else {
            insertList.clear();
            for (int i = 0; i < items.length; i++) {
                if (items[i].getChecked()) {
                    insertList.add(items[i].getText(0));
                }
            }
        }
        return true;
    }

    private boolean refreshJoinTable(ArrayList<JoinInfo> list) {
        button_join_enter.setVisible(true);
        if (table4 != null) {
            table4.dispose();
        }
        initTable_join();
        String[] tableHeader = {"临时表名称          ", "临时表生成规则                                                                         "};
        for (int i = 0; i < 2; i++) {
            TableColumn tableColumn = new TableColumn(table4, SWT.NONE);
            tableColumn.setText(tableHeader[i]);
            tableColumn.setMoveable(true);
        }
        for (int i = 0; i < list.size(); i++) {
            TableItem item = new TableItem(table4, SWT.NONE);
            item.setText(new String[]{list.get(i).getTempTableName(), JSON.toJSONString(list.get(i))});
        }
        for (int i = 0; i < tableHeader.length; i++) {
            table4.getColumn(i).pack();
        }
        table4.setSize(550, 260);
        return true;
    }


    private boolean refreshTable(Col[] cols) {
        if (table2 != null) {
            table2.dispose();
        }
        initTable();
        String[] tableHeader = {"数据源列选择                      ", "数据源列原数据类型                   ", "指定数据类型                     "};
        for (int i = 0; i < tableHeader.length; i++) {
            TableColumn tableColumn = new TableColumn(table2, SWT.NONE);
            tableColumn.setWidth(200);
            tableColumn.setText(tableHeader[i]);
            // 设置表头可移动，默认为false
            tableColumn.setMoveable(true);
        }
        for (int i = 0; i < cols.length; i++) {
            TableItem item = new TableItem(table2, SWT.NONE);
            item.setText(new String[]{cols[i].getName(), cols[i].getType().typeName(), ""});
        }

        for (int i = 0; i < cols.length; i++) {
            TableEditor editor = new TableEditor(table2);
            Text text = new Text(table2, SWT.NONE);
            text.setText("可编辑单元格");
            editor.grabHorizontal = true;
            editor.setEditor(text, table2.getItem(i), 2);
            text.addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent modifyEvent) {
                    editor.getItem().setText(2, text.getText());
                }
            });
        }
        for (int i = 0; i < tableHeader.length; i++) {
            table2.getColumn(i).pack();
        }
        table2.setSize(550, 220);
        return true;
    }


    private void initTable_source() {
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;

        table3 = new Table(group_test, SWT.SINGLE | SWT.FULL_SELECTION);
        table3.setVisible(true);
        table3.setHeaderVisible(true);// 设置显示表头
        table3.setLayoutData(gridData);// 设置表格布局
        table3.setLinesVisible(true);// 设置显示表格线/*
        table3.setLocation(10, 25);

        table3.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                // 获得所有的行数
                int total = table3.getItemCount();
                // 循环所有行
                for (int i = 0; i < total; i++) {
                    TableItem item = table3.getItem(i);

                    // 如果该行为选中状态，改变背景色和前景色，否则颜色设置
                    if (table3.isSelected(i)) {
                        item.setBackground(composite_group.getDisplay().getSystemColor(SWT.COLOR_RED));
                        item.setForeground(composite_group.getDisplay().getSystemColor(SWT.COLOR_WHITE));
                    } else {
                        item.setBackground(null);
                        item.setForeground(null);
                    }
                }
            }

        });
        table3.addListener(SWT.MouseDoubleClick, new Listener() {//双击取消选择
            @Override
            public void handleEvent(Event event) {
                TableItem[] items = table3.getSelection();
            }
        });// 创建表头的字符串数组
    }

    private void initTable_InsertMap() {
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;

        table5 = new Table(group_insert_map, SWT.SINGLE | SWT.FULL_SELECTION | SWT.CHECK);
        table5.setVisible(true);
        table5.setHeaderVisible(true);// 设置显示表头
        table5.setLayoutData(gridData);// 设置表格布局
        table5.setLinesVisible(true);// 设置显示表格线/*
        table5.setLocation(20, 20);

        table5.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                // 获得所有的行数
                int total = table5.getItemCount();
                // 循环所有行
                for (int i = 0; i < total; i++) {
                    TableItem item = table5.getItem(i);

                    // 如果该行为选中状态，改变背景色和前景色，否则颜色设置
                    if (table5.isSelected(i)) {
                        item.setChecked(true);
                        item.setBackground(group_insert_map.getDisplay().getSystemColor(SWT.COLOR_RED));
                        item.setForeground(group_insert_map.getDisplay().getSystemColor(SWT.COLOR_WHITE));
                    } else {
                        item.setBackground(null);
                        item.setForeground(null);
                    }
                }
            }

        });
        table5.addListener(SWT.MouseDoubleClick, new Listener() {//双击取消选择
            @Override
            public void handleEvent(Event event) {
                TableItem[] items = table5.getSelection();
                items[0].setChecked(false);
            }
        });// 创建表头的字符串数组
    }

    private void initTable_join() {
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;

        table4 = new Table(group_join_info, SWT.SINGLE | SWT.FULL_SELECTION);
        table4.setVisible(true);
        table4.setHeaderVisible(true);// 设置显示表头
        table4.setLayoutData(gridData);// 设置表格布局
        table4.setLinesVisible(true);// 设置显示表格线/*
        table4.setLocation(20, 20);

        table4.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                // 获得所有的行数
                int total = table4.getItemCount();
                // 循环所有行
                for (int i = 0; i < total; i++) {
                    TableItem item = table4.getItem(i);

                    // 如果该行为选中状态，改变背景色和前景色，否则颜色设置
                    if (table4.isSelected(i)) {
                        item.setChecked(true);
                        item.setBackground(group_join_info.getDisplay().getSystemColor(SWT.COLOR_RED));
                        item.setForeground(group_join_info.getDisplay().getSystemColor(SWT.COLOR_WHITE));
                    } else {
                        item.setBackground(null);
                        item.setForeground(null);
                    }
                }
            }

        });
        table4.addListener(SWT.MouseDoubleClick, new Listener() {//双击取消选择
            @Override
            public void handleEvent(Event event) {
                TableItem[] items = table4.getSelection();
                items[0].setChecked(false);
            }
        });// 创建表头的字符串数组
    }

    private void initTable() {
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;

        table2 = new Table(composite_group, SWT.SINGLE | SWT.FULL_SELECTION | SWT.CHECK);
        table2.setVisible(true);
        table2.setHeaderVisible(true);// 设置显示表头
        table2.setLayoutData(gridData);// 设置表格布局
        table2.setLinesVisible(true);// 设置显示表格线/*
        table2.setLocation(2, 10);

        table2.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                // 获得所有的行数
                int total = table2.getItemCount();
                // 循环所有行
                for (int i = 0; i < total; i++) {
                    TableItem item = table2.getItem(i);

                    // 如果该行为选中状态，改变背景色和前景色，否则颜色设置
                    if (table2.isSelected(i)) {
                        item.setChecked(true);
                        item.setBackground(composite_group.getDisplay().getSystemColor(SWT.COLOR_RED));
                        item.setForeground(composite_group.getDisplay().getSystemColor(SWT.COLOR_WHITE));
                    } else {
                        item.setBackground(null);
                        item.setForeground(null);
                    }
                }
            }

        });
        table2.addListener(SWT.MouseDoubleClick, new Listener() {//双击取消选择
            @Override
            public void handleEvent(Event event) {
                TableItem[] items = table2.getSelection();
                items[0].setChecked(false);
            }
        });// 创建表头的字符串数组
    }

    private boolean checkJoinInfoIntegrity() {
        if (combo_join_left.getText().trim().equals("") || combo_join_right.getText().trim().equals("")
                || text_join_tmpTable.getText().trim().equals("") || combo_join_type.getText().trim().equals("")
                || combo_join_left_column.getText().trim().equals("") || combo_join_right_column.getText().trim().equals("")) {
            showMessage("连接信息未配置完全", shell);
            return false;
        }
        return true;
    }

    private boolean checkInsertIntegrity() {
        MessageBox box = new MessageBox(shell);
        if (combo_source.getText().equals("")) {
            box.setMessage("请选择指定的数据源");
            box.open();
            return false;
        }
        if (combo_insert_datasourceType.getSelectionIndex() == 0) {//DB
            String type = combo_insert_db_type.getText().trim();
            String host = text_insert_db_host.getText().trim();
            String port = text_insert_db_port.getText().trim();
            String dbName = text_insert_db_name.getText().trim();
            String user = text_insert_db_username.getText().trim();
            String password = text_insert_db_password.getText().trim();
            String tableName = text_insert_db_table.getText().trim();
            if (type.equals("") || host.equals("") || port.equals("") || dbName.equals("") || user.equals("")
                    || password.equals("") || tableName.equals("")) {
                box.setMessage("请填写完整目标数据库连接信息！");
                box.open();
                return false;
            }
        } else if (combo_insert_datasourceType.getSelectionIndex() == 1) {//Hive
            String host = text_insert_hive_host.getText().trim();
            String port = text_insert_hive_port.getText().trim();
            String dbName = text_insert_hive_db_name.getText().trim();
            String tableName = text_insert_hive_table.getText().trim();
            if (host.equals("") || port.equals("") || dbName.equals("") || tableName.equals("")) {
                box.setMessage("请填写完整目标Hive连接信息！");
                box.open();
                return false;
            }
        } else if (combo_insert_datasourceType.getSelectionIndex() == 2) {//File
            String type = combo_insert_file_type.getText().trim();
            String host = text_insert_file_host.getText().trim();
            String port = text_insert_file_port.getText().trim();
            String path = text_insert_file_path.getText().trim();
            if (host.equals("") || port.equals("") || type.equals("") || path.equals("")) {
                box.setMessage("请填写完整目标文件源连接信息！");
                box.open();
                return false;
            }
        } else if (combo_insert_datasourceType.getSelectionIndex() == 3) {//UOM
            String brokerList = text_insert_uom_brokerList.getText().trim();
            String topic = text_insert_uom_topic.getText().trim();
            String keySer = text_insert_uom_keySer.getText().trim();
            String valueSer = text_insert_uom_valueSer.getText().trim();
            String partitionCounts = text_insert_uom_partitionCounts.getText().trim();
            String topicReplication = text_insert_replication.getText().trim();
            if (brokerList.equals("") || topic.equals("") || keySer.equals("") || valueSer.equals("") || partitionCounts.equals("")
                    || topicReplication.equals("")) {
                box.setMessage("请填写完整目标UOM连接信息");
                box.open();
                return false;
            }
        }
        return true;
    }

    private boolean checkIntegrity() {
        if (combo_datasoureType.getSelectionIndex() == 0) {//DB
            String type = combo_db_type.getText().trim();
            String host = text_db_host.getText().trim();
            String port = text_db_port.getText().trim();
            String dbName = text_db_name.getText().trim();
            String user = text_db_username.getText().trim();
            String password = text_db_password.getText().trim();
            String tableName = text_db_table.getText().trim();
            if (type.equals("") || host.equals("") || port.equals("") || dbName.equals("") || user.equals("")
                    || password.equals("") || tableName.equals("")) {
                MessageBox box = new MessageBox(shell);
                box.setMessage("请填写完整数据库连接信息！");
                box.open();
                return false;
            }
        } else if (combo_datasoureType.getSelectionIndex() == 1) {//Hive
            String host = text_hive_host.getText().trim();
            String port = text_hive_port.getText().trim();
            String dbName = text_hive_db_name.getText().trim();
            String tableName = text_hive_table.getText().trim();
            if (host.equals("") || port.equals("") || dbName.equals("") || tableName.equals("")) {
                MessageBox box = new MessageBox(shell);
                box.setMessage("请填写完整Hive连接信息！");
                box.open();
                return false;
            }
        } else if (combo_datasoureType.getSelectionIndex() == 2) {//File
            String type = combo_file_type.getText().trim();
            String host = text_file_host.getText().trim();
            String port = text_file_port.getText().trim();
            String path = text_file_path.getText().trim();
            if (host.equals("") || port.equals("") || type.equals("") || path.equals("")) {
                MessageBox box = new MessageBox(shell);
                box.setMessage("请填写完整文件源连接信息！");
                box.open();
                return false;
            }
        }
        return true;
    }

    private Col[] generateDataSourceInfo2() {
        String dataSource = "";
        String sourceType = "";
        if (combo_insert_datasourceType.getSelectionIndex() == 0) {//DB
            String type = combo_insert_db_type.getText().trim();
            String host = text_insert_db_host.getText().trim();
            String port = text_insert_db_port.getText().trim();
            String dbName = text_insert_db_name.getText().trim();
            String user = text_insert_db_username.getText().trim();
            String password = text_insert_db_password.getText().trim();
            String tableName = text_insert_db_table.getText().trim();
            sourceType = "Database";
            dataSource = type + "###" + host + "###" + port + "###" + dbName + "###" + user + "###" + password + "###" + tableName;
        } else {
            return null;
        }
        return new SourceHelper().getSourceInfo(sourceType, dataSource);
    }

    private Col[] generateDataSourceInfo() {
        String dataSource = "";
        String sourceType = "";
        if (combo_datasoureType.getSelectionIndex() == 0) {//DB
            String type = combo_db_type.getText().trim();
            String host = text_db_host.getText().trim();
            String port = text_db_port.getText().trim();
            String dbName = text_db_name.getText().trim();
            String user = text_db_username.getText().trim();
            String password = text_db_password.getText().trim();
            String tableName = text_db_table.getText().trim();
            sourceType = "Database";
            dataSource = type + "###" + host + "###" + port + "###" + dbName + "###" + user + "###" + password + "###" + tableName;
        } else if (combo_datasoureType.getSelectionIndex() == 1) {//Hive
            String host = text_hive_host.getText().trim();
            String port = text_hive_port.getText().trim();
            String dbName = text_hive_db_name.getText().trim();
            String tableName = text_hive_table.getText().trim();
            sourceType = "Hive";
            dataSource = host + "###" + port + "###" + dbName + "###" + tableName;
        } else if (combo_datasoureType.getSelectionIndex() == 2) {//File
            String type = combo_file_type.getText().trim();
            String host = text_file_host.getText().trim();
            String port = text_file_port.getText().trim();
            String path = text_file_path.getText().trim();
            sourceType = "File";
            dataSource = type + "###" + host + "###" + port + "###" + path;
        }

        return new SourceHelper().getSourceInfo(sourceType, dataSource);
    }

    private boolean addDataSource() {
        DataSourceInfo info = null;
        if (combo_datasoureType.getSelectionIndex() == 0) {//DB
            String type = combo_db_type.getText().trim();
            String host = text_db_host.getText().trim();
            String port = text_db_port.getText().trim();
            String dbName = text_db_name.getText().trim();
            String userName = text_db_username.getText().trim();
            String password = text_db_password.getText().trim();
            String tableName = text_db_table.getText().trim();
            String partitionColumn = text_partitionColumn.getText().trim();
            int lowerBound = text_lowerBound.getText().trim().equals("") ? -1 : Integer.parseInt(text_lowerBound.getText().trim());
            int upperBound = text_upperBound.getText().trim().equals("") ? -1 : Integer.parseInt(text_upperBound.getText().trim());
            int partitionCount = text_partitionCount.getText().trim().equals("") ? -1 : Integer.parseInt(text_partitionCount.getText().trim());
            String whereClause = text_whereClause.getText().trim();
            String timeStampColumn = text_timestamp.getText().trim();
            String time = cDateTime.getText().trim();
            String addSource = combo_db_addSource.getText().trim();
            String sql = text_db_sql.getText().trim();
            ArrayList<Col2> cols = null;
            if (addSource.equals("选择字段")) {
                cols = getColumnList();
            }
            info = DBSourceInfo.builder().type(type).host(host).port(port).dbName(dbName).userName(userName).password(password).tableName(tableName)
                    .partitionColumn(partitionColumn).lowerBound(lowerBound).upperBound(upperBound).partitionCount(partitionCount).whereClause(whereClause)
                    .timeStampColumn(timeStampColumn).time(time).addSource(addSource).sql(sql).colList(cols).build();
        } else if (combo_datasoureType.getSelectionIndex() == 1) {//Hive
            String host = text_hive_host.getText().trim();
            String port = text_hive_port.getText().trim();
            String dbName = text_hive_db_name.getText().trim();
            String tableName = text_hive_table.getText().trim();
            String whereClause = text_hive_whereClause.getText().trim();
            String timeStampColumn = text_hive_timestamp.getText().trim();
            String time = text_hive_cDateTime.getText().trim();
            String addSource = combo_hive_addSource.getText().trim();
            String sql = text_db_sql.getText().trim();
            ArrayList<Col2> cols = null;
            if (addSource.equals("选择字段")) {
                cols = getColumnList();
            }
            info = HiveSourceInfo.builder().host(host).port(port).dbName(dbName).tableName(tableName).whereClause(whereClause)
                    .timeStampColumn(timeStampColumn).time(time).addSource(addSource).sql(sql).colList(cols).build();
        } else if (combo_datasoureType.getSelectionIndex() == 2) {//File
            String type = combo_file_type.getText().trim();
            String host = text_file_host.getText().trim();
            String port = text_file_port.getText().trim();
            String path = text_file_path.getText().trim();
            String whereClause = text_file_whereClause.getText().trim();
            String timeStampColumn = text_file_timestamp.getText().trim();
            String time = text_file_cDateTime.getText().trim();
            String addSource = combo_file_addSource.getText().trim();
            String sql = text_db_sql.getText().trim();
            ArrayList<Col2> cols = null;
            if (addSource.equals("选择字段")) {
                cols = getColumnList();
            }
            info = FileSourceInfo.builder().type(type).host(host).port(port).path(path).whereClause(whereClause)
                    .timeStampColumn(timeStampColumn).time(time).addSource(addSource).sql(sql).colList(cols).build();
        }
        dataSourceList.add(info);
        return true;
    }

    private ArrayList<Col2> getColumnList() {
        ArrayList<Col2> cols = new ArrayList<>();
        int count = table2.getItemCount();
        for (int i = 0; i < count; i++) {
            TableItem item = table2.getItem(i);
            if (item.getChecked()) {
                Col2 col = new Col2(item.getText(0), item.getText(1), item.getText(2));
                cols.add(col);
            }
        }
        return cols;
    }

    private String[] getCurrentTableList() {
        LinkedHashMap<String, Dataset<Row>> map = wholeNameToDataFrame.size() == 0 ? nameToDataFrame : wholeNameToDataFrame;
        String[] list = new String[map.size()];
        int i = 0;
        for (Map.Entry<String, Dataset<Row>> entry : map.entrySet()) {
            list[i] = entry.getKey();
            i++;
        }
        return list;
    }

    private String[] getCurrentTableColumns(String tableName) {
        LinkedHashMap<String, Dataset<Row>> map = wholeNameToDataFrame.size() == 0 ? nameToDataFrame : wholeNameToDataFrame;
        Dataset<Row> df = map.get(tableName);
        return df.schema().fieldNames();
    }

    private void initNameToDF() {
        nameToDataFrame.clear();
        for (Map.Entry<String, DataSourceInfo> entry : nameToSource.entrySet()) {
            nameToDataFrame.put(entry.getKey(), new DataFrameGetter(entry.getValue()).getDataFrame());
        }
    }

    private void initTempTableDataframe() {
        tempToDataFrame.clear();
        wholeNameToDataFrame.clear();
        wholeNameToDataFrame.putAll(nameToDataFrame);
        for (JoinInfo info : joinInfos) {
            Dataset<Row> df = new DataFrameGetter().getJoinDataFrame(info, wholeNameToDataFrame);
            tempToDataFrame.put(info.getTempTableName(), df);
            wholeNameToDataFrame.put(info.getTempTableName(), df);
        }
    }

    private InsertMapInfo createInsertInfo(InsertMapInfo.InsertMapInfoBuilder builder) {
        builder.dataSource(combo_source.getText());
        InsertMapInfo insertMapInfo = null;
        switch (combo_insert_datasourceType.getSelectionIndex()) {
            case 0:
                DBDestInfo dbDestInfo = DBDestInfo.builder()
                        .type(combo_insert_db_type.getText().trim())
                        .host(text_insert_db_host.getText().trim())
                        .port(text_insert_db_port.getText().trim())
                        .dbName(text_insert_db_name.getText().trim())
                        .userName(text_insert_db_username.getText().trim())
                        .password(text_insert_db_password.getText().trim())
                        .tableName(text_insert_db_table.getText().trim())
                        .mode(combo_insert_db_mode.getText().trim())
                        .build();
                insertMapInfo = builder.destInfo(dbDestInfo).insertColumnsMap(insertList).build();
                break;
            case 1:
                HiveDestInfo hiveDestInfo = HiveDestInfo.builder()
                        .host(text_insert_hive_host.getText().trim())
                        .port(text_insert_hive_port.getText().trim())
                        .dbName(text_hive_db_name.getText().trim())
                        .tableName(text_hive_table.getText().trim())
                        .mode(combo_insert_hive_mode.getText().trim())
                        .build();
                insertMapInfo = builder.destInfo(hiveDestInfo).selectedColumns(insertList).build();
                break;
            case 2:
                FileDestInfo fileDestInfo = FileDestInfo.builder()
                        .type(combo_insert_file_type.getText().trim())
                        .host(text_insert_file_host.getText().trim())
                        .port(text_insert_file_port.getText().trim())
                        .path(text_file_path.getText().trim())
                        .mode(combo_insert_file_mode.getText().trim())
                        .build();
                insertMapInfo = builder.destInfo(fileDestInfo).selectedColumns(insertList).build();
                break;
            case 3:
                UOMDestInfo uomDestInfo = UOMDestInfo.builder()
                        .brokerList(text_insert_uom_brokerList.getText().trim())
                        .topic(text_insert_uom_topic.getText().trim())
                        .keySerializer(text_insert_uom_keySer.getText().trim())
                        .valueSerializer(text_insert_uom_valueSer.getText().trim())
                        .partitionCounts(text_insert_uom_partitionCounts.getText().trim())
                        .replication(text_insert_replication.getText().trim())
                        .mode(combo_insert_file_mode.getText().trim())
                        .build();
                insertMapInfo = builder.destInfo(uomDestInfo).selectedColumns(insertList).build();
                break;
            default:
                return null;
        }
        return insertMapInfo;
    }

    public String getIntegrateModel(InsertMapInfo insertMapInfo) {
        IntegrateModel model = new IntegrateModel();
        ArrayList<String> sourceList = new ArrayList<>();
        for (Map.Entry entry : nameToSource.entrySet()) {
            sourceList.add(JSONObject.toJSONString(entry.getValue()));
        }
        model.setSourceList(sourceList);
        model.setJoinInfos(joinInfos);
        model.setInsertMapInfo(insertMapInfo);
        model.setSyncConfig(syncConfig);

        String s = JSONObject.toJSONString(model);
        s = s.replace("\"{", "{");
        s = s.replace("}\"", "}");
        s = s.replace("\\", "");
        System.out.println(s);
        FileUtils.write("G:/properties/test.txt", s);
        return s;
    }

}




