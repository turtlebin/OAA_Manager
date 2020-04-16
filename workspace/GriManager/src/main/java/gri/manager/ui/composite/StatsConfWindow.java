package gri.manager.ui.composite;

import gri.driver.manager.GriDocManager;
import gri.driver.manager.ProcessManager;
import gri.driver.model.process.Paragraph;
import gri.manager.ui.window.AddFieldDialog;
import gri.manager.ui.window.StatsDialog;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import gri.driver.model.process.GroupBy;
import gri.driver.model.process.StatsContent;
import gri.driver.model.process.StatsConf;
import gri.driver.model.process.StatsResult;

import gri.driver.util.DriverConstant;

/**
 * @Description 
 * Created by erik on 2016-9-8
 */
public class StatsConfWindow extends Composite {
	private static final Logger LOGGER = LoggerFactory.getLogger(StatsConfWindow.class);
	
	private StatsDialog main;
	private final Combo comboGroupBy;
	private Text txtSource;
	private Text txtParagraphName;
	private Text text_3;
	private org.eclipse.swt.widgets.List lsSelectCol;
	private String[] comboNames;
	private StatsConf statsConf;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public StatsConfWindow(final Composite parent, int style, final StatsDialog main) {
		super(parent, style);
		
		this.main=main;
		this.setSize(StatsDialog.WIDTH, StatsDialog.HEIGHT);
		final StatsConfWindow temp_this= this;
		
		Group group = new Group(this, SWT.NONE);
		group.setText("数据源与关键字选择");
		group.setBounds(0, 23, 345, 109);
		
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
		//comboGroupBy.setItems(new String[] {"id", "name", "year", "weight", "height"});
		comboGroupBy.setBounds(97, 66, 73, 25);
		
		Combo combo_1 = new Combo(group, SWT.NONE);
		combo_1.setItems(new String[] {"升序", "降序"});
		combo_1.setBounds(262, 66, 73, 25);
		
		Group group_1 = new Group(this, SWT.NONE);
		group_1.setText("统计列选择");
		group_1.setBounds(0, 155, 345, 154);
		
		Button btnAddColumn = new Button(group_1, SWT.NONE);
		
		btnAddColumn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String result = (String) new AddFieldDialog(main.getShell() ,SWT.CLOSE | SWT.APPLICATION_MODAL, comboNames).open();
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
		
		Group group_2 = new Group(this, SWT.NONE);
		group_2.setText("非统计列选择");
		group_2.setBounds(0, 336, 345, 154);
		
		text_3 = new Text(group_2, SWT.BORDER);
		text_3.setBounds(0, 30, 264, 114);
		
		Button button_2 = new Button(group_2, SWT.NONE);
		button_2.setText("增加");
		button_2.setBounds(276, 54, 59, 27);
		
		Button button_3 = new Button(group_2, SWT.NONE);
		button_3.setText("删除");
		button_3.setBounds(276, 94, 59, 27);
		
		Button btnGo = new Button(this, SWT.NONE);		

		btnGo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	
				calculate();
                new StatsTableWindow(parent, SWT.NONE,main.getStatsResult());  
                temp_this.dispose();
			}
		});
		btnGo.setBounds(368, 169, 96, 27);
		btnGo.setText("立即统计");
		
		Button button_5 = new Button(this, SWT.NONE);
		button_5.setText("保存为常用统计");
		button_5.setBounds(368, 219, 96, 27);
		
		Button button_6 = new Button(this, SWT.NONE);
		button_6.setText("删除常用统计");
		button_6.setBounds(368, 271, 96, 27);
		
		Button button_7 = new Button(this, SWT.NONE);
		button_7.setText("退出");
		button_7.setBounds(368, 463, 96, 27);
		
		loadData();
	}
	
	public void loadData(){
		Paragraph paragraph= main.getParagraph();
		txtParagraphName.setText(paragraph.getName());
		String[] paths = paragraph.getDataSourcePath().split("###");
		txtSource.setText(paths[0]+":"+paths[1]+":"+paths[2]+":"+paths[3]);
		
		if(comboNames == null){
			ProcessManager manager = main.getManager();
			comboNames = manager.colList(paragraph.getId());
		}
		comboGroupBy.setItems(comboNames);
		
		//如果存在，则加载统计配置
		statsConf = main.getStatsConf();
		if(statsConf==null){
			main.setStatsConf(new StatsConf());
			statsConf = main.getStatsConf();
		}
		else {
			if(statsConf.getGroupBys().size()>0){
				GroupBy groupBy=statsConf.getGroupBys().get(0);
				for(int i = 0; i<comboNames.length; i++)
					if(groupBy.getFieldName().equals(comboNames[i])) comboGroupBy.select(i);
			}
			
			String[] statsType =new String[] {"统计数量", "求和", "平均值", "最大值", "最小值"};
			String txtColumnText="";
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
		
		ProcessManager manager = main.getManager();
		StatsResult statsResult=manager.calculate(main.getParagraph().getId(),statsConf);
		System.out.println(statsConf);
		
		main.setStatsResult(statsResult); 
		
		LOGGER.info("statsResult : "+JSONArray.fromObject(statsResult.getData()).toString());
	}
	
	private int getStatsType(String str){
		if(str.indexOf("统计数量")>=0) return 1;
		else if(str.indexOf("求和")>=0) return 2;
		else if(str.indexOf("平均值")>=0) return 3;
		else if(str.indexOf("最大值")>=0) return 4;
		//最小值
		else return 5;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
