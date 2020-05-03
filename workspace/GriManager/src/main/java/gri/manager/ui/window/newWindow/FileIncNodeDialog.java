package gri.manager.ui.window.newWindow;

import gri.manager.newModel.*;
import gri.manager.util.*;
import org.eclipse.swt.widgets.*;
import gri.driver.model.GriElement;
import gri.driver.model.process.Paragraph;
import gri.engine.dest.Column;
import gri.engine.dest.Pair;
import gri.engine.integrate.FileUtil;
import gri.engine.integrate.IntegrateMain;
import gri.engine.integrate.Paragraph2;
import gri.engine.integrate.Paragraph3;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.ui.window.MainWindow;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class FileIncNodeDialog extends Dialog {

    public int index = 1;

    protected Object result;
    protected Shell shell;
    private Shell parentShell;
    private int windowType;

    private GlobalViewTreeNode node;

    private TabFolder tabFolder_main;
    //Database
    private Text text_name;
    private Text text_keywords;
    private Text text_description;

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
    private Combo combo_source;

    private Text text_insert_uom_brokerList;
    private Text text_insert_uom_topic;
    private Text text_insert_uom_keySer;
    private Text text_insert_uom_valueSer;
    private Text text_insert_uom_partitionCounts;
    private Text text_insert_replication;
    private Combo combo_uom_mode;
    private Button button_ok2;

    private Label label_updateDate;
    private Label label_dataSize;
    private Label label_syncDate;
    private Label label_syncState;
    private Text text_updateDate;
    private Text text_dataSize;
    private Text text_syncDate;
    private Text text_syncState;
    public static Text text_paragraphDataName;
    public static Text text_paragraphDataID;

    private Paragraph tempParagraph = new Paragraph("", "", "", "", "", "", "", ""); // windowType_Add

    public static Integer griParagraphId;
    public static Text text_griDataName;

    private List<Map<String, Map<String, Column>>> list = new ArrayList<Map<String, Map<String, Column>>>();//list存放格式为Map<数据源，Map<目标表字段名称，来源表字段column>>
    public List<Pair<String, String>> wholePairList = new ArrayList<Pair<String, String>>();

    private ArrayList<String> insertList = new ArrayList<>();
    private SyncConfig syncConfig = new SyncConfig();


    public FileIncNodeDialog(int windowType, GlobalViewTreeNode treeNode, Shell parent, int style) {
        super(parent, style);
        this.parentShell = parent;
        this.node = treeNode;
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

    private void initWindow() {
        label_updateDate.setVisible(false);
        text_updateDate.setVisible(false);
        label_dataSize.setVisible(false);
        text_dataSize.setVisible(false);
        label_syncDate.setVisible(false);
        text_syncDate.setVisible(false);
        label_syncState.setVisible(false);
        text_syncState.setVisible(false);
        // shell.setSize(475, 706 - 240);
        tabFolder_main.setBounds(10, 10, 649, 680);
    }

    private void createContents() {
        shell = new Shell(getParent(), getStyle());
        System.out.println(shell.getSize().x + "，" + shell.getSize().y);
        shell.setText("新建段");
        shell.setImage(
                new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/paragraph16.png")));

        Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
        Rectangle shellBounds = shell.getBounds();
        shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
                parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

        tabFolder_main = new TabFolder(shell, SWT.NONE);

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


        TabItem tabItem_Insert = new TabItem(tabFolder_main, SWT.NONE);
        tabItem_Insert.setText("数据列映射插入");

        Composite composite_insert = new Composite(tabFolder_main, SWT.NONE);
        tabItem_Insert.setControl(composite_insert);

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


        button_ok2 = new Button(composite_insert, SWT.NONE);
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
        button_ok2.setBounds(510,610,80,27);
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
    }

    private void showMessage(String message, Shell shell) {
        MessageBox box = new MessageBox(shell);
        box.setMessage(message);
        box.open();
    }

    private InsertMapInfo createInsertInfo(InsertMapInfo.InsertMapInfoBuilder builder) {
        builder.dataSource(combo_source.getText());
        InsertMapInfo insertMapInfo = null;
        switch (combo_insert_datasourceType.getSelectionIndex()) {
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
}




