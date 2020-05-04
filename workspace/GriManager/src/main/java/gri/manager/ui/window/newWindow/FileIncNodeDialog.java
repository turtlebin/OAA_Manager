package gri.manager.ui.window.newWindow;

import com.alibaba.fastjson.JSON;
import gri.manager.newModel.*;
import gri.manager.ui.window.SyncConfigDialog;
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

import java.io.File;
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
    private Group group_insert_uom_connect;
    private Group group_insert_map;

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

    private Text text_source_host;
    private Text text_source_port;
    private Text text_source_filePath;
    private Text text_dest_host;
    private Text text_dest_port;
    private Text text_dest_filePath;
    private Combo combo_chunkingMethod;
    private Combo combo_operatingMode;


    private Paragraph tempParagraph = new Paragraph("", "", "", "", "", "", "", ""); // windowType_Add

    public static Integer griParagraphId;
    public static Text text_griDataName;

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
        tabFolder_main.setBounds(10, 10, 649, 510);
    }

    private void createContents() {
        shell = new Shell(getParent(), getStyle());
        System.out.println(shell.getSize().x + "，" + shell.getSize().y);
        shell.setText("文件增量同步配置");
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
        tabItem_Insert.setText("文件增量同步配置");

        Composite composite_insert = new Composite(tabFolder_main, SWT.NONE);
        tabItem_Insert.setControl(composite_insert);

        Button button_syncConfig = new Button(composite_insert, SWT.NONE);
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

        group_insert_map = new Group(composite_insert, SWT.NONE);
        group_insert_map.setLocation(20,40);
        group_insert_map.setSize(601, 203);
        group_insert_map.setText("增量同步配置");

        Label label_source_host = new Label(group_insert_map, SWT.NONE);
        label_source_host.setText("源OAA服务器ip地址：");
        label_source_host.setBounds(10, 26, 120, 17);

        text_source_host = new Text(group_insert_map, SWT.BORDER);
        text_source_host.setBounds(138, 23, 129, 25);
        text_source_host.setText("localhost");

        Label label_source_port = new Label(group_insert_map, SWT.NONE);
        label_source_port.setText("源OAA服务器端口号：");
        label_source_port.setBounds(10, 61, 120, 17);

        text_source_port = new Text(group_insert_map, SWT.BORDER);
        text_source_port.setBounds(138, 58, 129, 23);
        text_source_port.setText("8000");

        Label label_source_filePath = new Label(group_insert_map, SWT.NONE);
        label_source_filePath.setText("源文件地址：");
        label_source_filePath.setBounds(10, 92, 120, 17);

        text_source_filePath = new Text(group_insert_map, SWT.BORDER);
        text_source_filePath.setBounds(138, 89, 129, 23);
        text_source_filePath.setText("/home/xhb/data/source.txt");

        Label label_chunkMethod = new Label(group_insert_map, SWT.NONE);
        label_chunkMethod.setText("分块算法选择：");
        label_chunkMethod.setBounds(10, 125, 120, 17);

        combo_chunkingMethod= new Combo(group_insert_map, SWT.BORDER);
        combo_chunkingMethod.setBounds(138, 122, 129, 23);
        combo_chunkingMethod.setItems(new String[]{"Rabin","RAM","AE","LMC"});
        combo_chunkingMethod.select(1);

        Label label_dest_host = new Label(group_insert_map, SWT.NONE);
        label_dest_host.setText("备份OAA服务器ip地址：");
        label_dest_host.setBounds(300, 26, 130, 17);

        text_dest_host = new Text(group_insert_map, SWT.BORDER);
        text_dest_host.setBounds(450, 23, 129, 23);
        text_dest_host.setText("localhost");

        Label label_dest_port = new Label(group_insert_map, SWT.NONE);
        label_dest_port.setText("备份OAA服务器端口号：");
        label_dest_port.setBounds(300, 61, 130, 17);

        text_dest_port = new Text(group_insert_map, SWT.BORDER);
        text_dest_port.setBounds(450, 58, 129, 23);
        text_dest_port.setText("8000");

        Label label_dest_filePath = new Label(group_insert_map, SWT.NONE);
        label_dest_filePath.setText("备份文件地址：");
        label_dest_filePath.setBounds(300, 92, 130, 17);

        text_dest_filePath = new Text(group_insert_map, SWT.BORDER);
        text_dest_filePath.setBounds(450, 89, 129, 23);
        text_dest_filePath.setText("/home/xhb/data/source.txt");

        Label label_operatingMode = new Label(group_insert_map, SWT.NONE);
        label_operatingMode.setText("运行模式：");
        label_operatingMode.setBounds(300, 125, 130, 17);

        combo_operatingMode= new Combo(group_insert_map, SWT.BORDER);
        combo_operatingMode.setBounds(450, 122, 129, 23);
        combo_operatingMode.setItems(new String[]{"CMS-Mode","CMA-Mode"});
        combo_operatingMode.select(0);


        group_insert_uom_connect = new Group(composite_insert, SWT.NONE);
        group_insert_uom_connect.setLocation(20, 263);
        group_insert_uom_connect.setSize(601, 203);
        group_insert_uom_connect.setText("UOM");

        Label label_insert_uom_brokerList = new Label(group_insert_uom_connect, SWT.NONE);
        label_insert_uom_brokerList.setText("服务器列表：");
        label_insert_uom_brokerList.setBounds(10, 26, 100, 17);

        text_insert_uom_brokerList = new Text(group_insert_uom_connect, SWT.BORDER);
        text_insert_uom_brokerList.setBounds(118, 23, 109, 25);
        text_insert_uom_brokerList.setText("localhost:9092");

        Label label_insert_uom_topic = new Label(group_insert_uom_connect, SWT.NONE);
        label_insert_uom_topic.setText("主题：");
        label_insert_uom_topic.setBounds(10, 61, 100, 17);

        text_insert_uom_topic = new Text(group_insert_uom_connect, SWT.BORDER);
        text_insert_uom_topic.setBounds(118, 58, 109, 23);
        text_insert_uom_topic.setText("test");

        Label label_keySer = new Label(group_insert_uom_connect, SWT.NONE);
        label_keySer.setText("Key序列化器：");
        label_keySer.setBounds(10, 92, 100, 17);

        text_insert_uom_keySer = new Text(group_insert_uom_connect, SWT.BORDER);
        text_insert_uom_keySer.setBounds(118, 89, 109, 23);
        text_insert_uom_keySer.setText("test");

        Label label_valueSer = new Label(group_insert_uom_connect, SWT.NONE);
        label_valueSer.setText("Value序列化器：");
        label_valueSer.setBounds(10, 125, 100, 17);

        text_insert_uom_valueSer = new Text(group_insert_uom_connect, SWT.BORDER);
        text_insert_uom_valueSer.setBounds(118, 122, 109, 23);
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



        button_ok2 = new Button(shell, SWT.NONE);
        button_ok2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (checkIntegrity()) {
                    writeFileIncrementalModel();
                    GriElement paragraph = node.manager.addGriElement(node.root, (GriElement) node.data,
                            new Paragraph(text_name.getText().trim(), "", "", tempParagraph.getSyncTimeType(),
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
                }else{
                    showMessage("请完整填写源服务器与备份服务器文件相关参数",shell);
                    return ;
                }
            }
        });
        button_ok2.setBounds(510,530,100,27);
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

    private boolean checkIntegrity(){
        String text_sourceIp=text_source_host.getText().trim();
        String text_sourcePort=text_source_port.getText().trim();
        String text_sourceFilePath=text_source_filePath.getText().trim();
        String text_destIp=text_dest_host.getText().trim();
        String text_destPort=text_dest_port.getText().trim();
        String text_destFilePath=text_dest_filePath.getText().trim();
        String text_chunking_method=combo_chunkingMethod.getText().trim();
        String text_operating_mode=combo_operatingMode.getText().trim();
        if(text_sourceIp.equals("")||text_sourcePort.equals("")||text_sourceFilePath.equals("")||text_destIp.equals("")||text_destPort.equals("")||
        text_destFilePath.equals("")||text_chunking_method.equals("")||text_operating_mode.equals("")){
            return false;
        }
        return true;
    }

    private void writeFileIncrementalModel(){
        String text_sourceIp=text_source_host.getText().trim();
        String text_sourcePort=text_source_port.getText().trim();
        String text_sourceFilePath=text_source_filePath.getText().trim();
        String text_destIp=text_dest_host.getText().trim();
        String text_destPort=text_dest_port.getText().trim();
        String text_destFilePath=text_dest_filePath.getText().trim();
        String text_chunking_method=combo_chunkingMethod.getText().trim();
        String text_operating_mode=combo_operatingMode.getText().trim();
        FileSource source=FileSource.builder().host(text_sourceIp).port(text_sourcePort).filePath(text_sourceFilePath).build();
        FileSource backup= FileSource.builder().host(text_destIp).port(text_destPort).filePath(text_destFilePath).build();
        UOMDestInfo uom=null;
        boolean usingUOM=false;
        String brokerList = text_insert_uom_brokerList.getText().trim();
        String topic = text_insert_uom_topic.getText().trim();
        String keySer = text_insert_uom_keySer.getText().trim();
        String valueSer = text_insert_uom_valueSer.getText().trim();
        String partitionCounts = text_insert_uom_partitionCounts.getText().trim();
        String topicReplication = text_insert_replication.getText().trim();
        if (!(brokerList.equals("") || topic.equals("") || keySer.equals("") || valueSer.equals("") || partitionCounts.equals("")
                || topicReplication.equals(""))) {
            uom= UOMDestInfo.builder().brokerList(brokerList).topic(topic).keySerializer(keySer).valueSerializer(valueSer)
                    .partitionCounts(partitionCounts).replication(topicReplication).build();
            usingUOM=true;
        }
        FileIncrementalModel model=FileIncrementalModel.builder().sourceFileInfo(source).backupFileInfo(backup).CDC_Method(text_chunking_method)
                .operatingMode(text_operating_mode).usingUOM(usingUOM).uomInfo(uom).syncConfig(syncConfig).build();
        FileUtils.write("G:/properties/"+text_name.getText()+".txt", JSON.toJSONString(model));
    }
}




