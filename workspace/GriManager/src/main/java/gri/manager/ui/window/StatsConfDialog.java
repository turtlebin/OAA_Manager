package gri.manager.ui.window;

import java.util.ArrayList;
import java.util.List;

import gri.driver.manager.GriDocManager;
import gri.driver.manager.ProcessManager;
import gri.driver.model.process.Paragraph;
import gri.driver.model.process.GroupBy;
import gri.driver.model.process.StatsConf;
import gri.driver.model.process.StatsConfDto1;
import gri.driver.model.process.StatsContent;
import gri.manager.ui.composite.StatsConfWindow;
import gri.manager.util.Constant;

import net.sf.json.JSONArray;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;

import gri.driver.model.process.StatsResult;
import gri.driver.util.DriverConstant;

public class StatsConfDialog extends Dialog {

	
	protected Shell shell;
	private Shell parentShell;	
	protected Object result;
	
	private int windowType;
	private Paragraph paragraph;
	private ProcessManager manager;
	private StatsConfDto1 statsConfDto1;
	
	private Combo comboGroupBy;
	private Combo comboOrder;
	private Text txtSource;
	private Text txtParagraphName;
	private Text text_3;
	private org.eclipse.swt.widgets.List lsSelectCol;
	private String[] comboNames;
	

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public StatsConfDialog(Shell parent, int windowType, int style ,Paragraph paragraph, StatsConfDto1 statsConfDto1,ProcessManager manager) {
		super(parent, style);
		this.parentShell = parent;
		this.windowType = windowType;
		this.paragraph = paragraph;
		this.statsConfDto1 = statsConfDto1; 
		this.manager = manager;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
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
		shell = new Shell(getParent(), getStyle());
		shell.setSize(380, 600);
		shell.setText("新建统计");
		if (this.windowType == Constant.WindowType_Edit)
			shell.setText("编辑统计");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);		
		
		
		Group group = new Group(shell, SWT.NONE);
		group.setText("数据源与关键字选择");
		group.setBounds(14, 23, 345, 109);
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(10, 30, 61, 17);
		label.setText("段名：");
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setBounds(195, 30, 61, 17);
		label_1.setText("数据源：");
		
		txtSource = new Text(group, SWT.BORDER);
		txtSource.setEditable(false);
		txtSource.setBounds(262, 30, 73, 23);
		
		Label label_2 = new Label(group, SWT.NONE);
		label_2.setText("统计关键字：");
		label_2.setBounds(10, 68, 73, 17);
		
		Label label_3 = new Label(group, SWT.NONE);
		label_3.setText("排序：");
		label_3.setBounds(195, 68, 61, 17);
		
		txtParagraphName = new Text(group, SWT.BORDER);
		txtParagraphName.setEditable(false);
		txtParagraphName.setBounds(97, 30, 73, 23);
		
		comboGroupBy = new Combo(group, SWT.NONE);
		comboGroupBy.setBounds(97, 66, 73, 25);
		
		comboOrder = new Combo(group, SWT.NONE);
		comboOrder.setItems(new String[] {"升序", "降序"});
		comboOrder.setBounds(262, 66, 73, 25);
		
		Group group_1 = new Group(shell, SWT.NONE);
		group_1.setText("统计列选择");
		group_1.setBounds(14, 155, 345, 154);
		
		Button btnAddColumn = new Button(group_1, SWT.NONE);
		
		btnAddColumn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String result = (String) new AddFieldDialog(shell ,SWT.CLOSE | SWT.APPLICATION_MODAL, comboNames).open();
				if(result!=null) addField(result);
			}
		});
		btnAddColumn.setBounds(276, 54, 59, 27);
		btnAddColumn.setText("增加");
		
		Button btnDelColumn = new Button(group_1, SWT.NONE);
		btnDelColumn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lsSelectCol.remove(lsSelectCol.getSelectionIndices());
			}
		});
		btnDelColumn.setText("删除");
		btnDelColumn.setBounds(276, 94, 59, 27);
		
		lsSelectCol = new org.eclipse.swt.widgets.List(group_1, SWT.BORDER | SWT.MULTI);
		lsSelectCol.setBounds(0, 30, 264, 114);
		
		Group group_2 = new Group(shell, SWT.NONE);
		group_2.setText("非统计列选择");
		group_2.setBounds(14, 336, 345, 154);
		
		text_3 = new Text(group_2, SWT.BORDER);
		text_3.setBounds(0, 30, 264, 114);
		
		Button button_2 = new Button(group_2, SWT.NONE);
		button_2.setText("增加");
		button_2.setBounds(276, 54, 59, 27);
		
		Button button_3 = new Button(group_2, SWT.NONE);
		button_3.setText("删除");
		button_3.setBounds(276, 94, 59, 27);
		
		Button btnGo = new Button(shell, SWT.NONE);		

		btnGo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	
				calculate();//保存统计配置
                shell.dispose();
			}
		});
		btnGo.setBounds(14, 516, 96, 27);
		btnGo.setText("保存");
		
		Button button_7 = new Button(shell, SWT.NONE);
		button_7.setText("退出");
		button_7.setBounds(154, 516, 96, 27);
		
		loadData();
	}

	public void loadData(){
		txtParagraphName.setText(paragraph.getName());
		String[] paths = paragraph.getDataSourcePath().split("###");
		txtSource.setText(paths[0]+":"+paths[1]+":"+paths[2]+":"+paths[3]);
		
		if(comboNames == null){
			comboNames = manager.colList(paragraph.getId());
		}
		comboGroupBy.setItems(comboNames);
		
		//如果存在，则加载统计配置
		if(windowType==Constant.WindowType_Edit){
			StatsConf statsConf = statsConfDto1.getStatsConf();
			if(statsConf.getGroupBys().size()>0){
				GroupBy groupBy=statsConf.getGroupBys().get(0);
				for(int i = 0; i<comboNames.length; i++)
					if(groupBy.getFieldName().equals(comboNames[i])) comboGroupBy.select(i);
			}
			comboOrder.select(0);
			
			lsSelectCol.removeAll();
			for(int i =0;i<statsConf.getStatsContents().size();i++){
				StatsContent statsContents = statsConf.getStatsContents().get(i);
				lsSelectCol.add(statsContents.toString1());
			}
		}
	}
	
	public void addField(String append){
		lsSelectCol.add(append);		
	}
	
	public void calculate(){
		//JSONArray data = ToJson.fromMysql(txtSource.getText());
		if(windowType==Constant.WindowType_Add){
			statsConfDto1 = new StatsConfDto1();
			statsConfDto1.setStatsConf(new StatsConf());
			statsConfDto1.setParagraphId(paragraph.getId());
		}
		
		StatsConf statsConf = statsConfDto1.getStatsConf();
		statsConf.setViewName("统计结果");
		statsConf.setGraphType(DriverConstant.GraphType_Bar);
		statsConf.setRowSummary(comboGroupBy.getText());
		statsConf.setColSummary("");
		
		statsConf.setGroupBys(new ArrayList<GroupBy>());
		List<GroupBy> groupBys = statsConf.getGroupBys();
		GroupBy groupBy= new GroupBy();
		groupBy.setFieldName(comboGroupBy.getText());
		groupBy.setOperType(DriverConstant.GroupByType_Enum);
		groupBy.setOperParam("");
		groupBys.add(groupBy);
		
		statsConf.setStatsContents(new ArrayList<StatsContent>());
		List<StatsContent> statsContents = statsConf.getStatsContents();
		String[] selected = lsSelectCol.getItems();
		for(String append : selected){
			String[] splits = append.split("的");
			if(splits.length>1){				
				StatsContent statsContent = new StatsContent();
				statsContent.setFieldName(splits[0]);
				statsContent.setOperType(getStatsType(splits[1]));
				statsContents.add(statsContent);
			}
		}
		
		if(windowType==Constant.WindowType_Add)	manager.addStatsConf(statsConfDto1);
		else if(windowType==Constant.WindowType_Edit) manager.updateStatsConf(statsConfDto1);
	}
	
	private int getStatsType(String str){
		if(str.indexOf("统计数量")>=0) return 1;
		else if(str.indexOf("求和")>=0) return 2;
		else if(str.indexOf("平均值")>=0) return 3;
		else if(str.indexOf("最大值")>=0) return 4;
		//最小值
		else return 5;
	}
}
