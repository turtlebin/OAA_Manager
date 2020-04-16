package gri.manager.ui.composite;

import gri.driver.model.process.StatsResult;
import gri.driver.util.DriverConstant;
import gri.manager.ui.graph.BarComposite;
import gri.manager.ui.graph.GraphComposite;
import gri.manager.ui.graph.LineComposite;
import gri.manager.ui.graph.PieComposite;
import gri.manager.ui.window.StatsDialog;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;



/**
 * @Description 
 * Created by erik on 2016-9-8
 */
public class StatsGraphWindow extends Composite {
	private StatsResult statsResult;
	private GraphComposite curComposite;
	public static final int GRAPH_WIDTH=355;
	public static final int GRAPH_HEIGHT=319;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public StatsGraphWindow(final Composite parent, int style, StatsResult statsResult) {
		super(parent, style);
		
		
		
		this.statsResult=statsResult;
		final StatsGraphWindow temp_this = this;
		this.setSize(StatsDialog.WIDTH, StatsDialog.HEIGHT);
		
		Label label = new Label(this, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		label.setAlignment(SWT.CENTER);
		label.setBounds(149, 10, 95, 25);
		label.setText("统计图展示");
		
		Label label_1 = new Label(this, SWT.NONE);
		label_1.setText("统计图类型：");
		label_1.setBounds(0, 55, 73, 17);
		
		final Combo comboType = new Combo(this, SWT.NONE);
		comboType.setItems(new String[] {"饼图", "条形图", "折线图"});
		comboType.setBounds(80, 52, 73, 25);
		
		Label lblX = new Label(this, SWT.NONE);
		lblX.setText("X轴：");
		lblX.setBounds(186, 55, 28, 17);
		
		Combo combo_1 = new Combo(this, SWT.NONE);
		String [] xs = new String [1];
		xs[0] = statsResult.getRowSummary();
		combo_1.setItems(xs);
		combo_1.setBounds(220, 52, 73, 25);
		
		Label lblY = new Label(this, SWT.NONE);
		lblY.setText("Y轴：");
		lblY.setBounds(327, 55, 28, 17);
		
		final Combo comboY = new Combo(this, SWT.NONE);
		comboY.setItems(lsToStrs(statsResult.getColName()));
		comboY.setBounds(360, 52, 73, 25);
		
		Label label_2 = new Label(this, SWT.NONE);
		label_2.setText("统计时间范围：");
		label_2.setBounds(0, 99, 84, 17);
		
		final DateTime dateTime = new DateTime(this, SWT.BORDER);
		dateTime.setBounds(100, 96, 88, 24);
		
		Label label_3 = new Label(this, SWT.NONE);
		label_3.setBounds(214, 99, 28, 17);
		label_3.setText("到");
		
		final DateTime dateTime_1 = new DateTime(this, SWT.BORDER);
		dateTime_1.setBounds(258, 96, 88, 24);
		
		final ScrolledComposite scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		final Composite content = new Composite( scrolledComposite, SWT.NONE );  		  
		scrolledComposite.setContent( content );  
		content.setLayout( new FillLayout() ); 
		
		
		scrolledComposite.setBounds(0, 145, GRAPH_WIDTH, GRAPH_HEIGHT);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		
		Button button = new Button(this, SWT.NONE);
		button.setBounds(384, 145, 80, 27);
		button.setText("打印");
		
		Button btnSave = new Button(this, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog file =new FileDialog(parent.getShell(), SWT.SAVE);
				file.setFilterExtensions(new String[] { "*.png" });
				String filePath = file.open();
				curComposite.saveAsFile(filePath);
			}
		});
		btnSave.setText("另存为");
		btnSave.setBounds(384, 197, 80, 27);
		
		/*Button button_2 = new Button(this, SWT.NONE);
		button_2.setBounds(384, 301, 80, 27);
		button_2.setText("退出")*/;
		
		final StatsResult tempStatsResult =statsResult;
		Button btnGo = new Button(this, SWT.NONE);
		btnGo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StatsResult theStatsResult = new StatsResult();
				theStatsResult.setTitle(tempStatsResult.getTitle());
				theStatsResult.setType(comboType.getSelectionIndex()+1);
				theStatsResult.setRowSummary(tempStatsResult.getRowSummary());
				theStatsResult.setRowName(tempStatsResult.getRowName());
				
				//选出待查看的结果
				int index = comboY.getSelectionIndex();				
				theStatsResult.setColSummary(tempStatsResult.getColName().get(index));
				
				List<String> newColName = new ArrayList<String>();
				newColName.add(tempStatsResult.getColName().get(index));
				theStatsResult.setColName(newColName);
				
				List<List<Double>> newData = new ArrayList<List<Double>>();
				List<Double> temp = new  ArrayList<Double>();
				newData.add(temp);
				if(tempStatsResult.getData().size()>0){
					for(int i = 0 ;i<tempStatsResult.getData().get(index).size();i++){
						temp.add(tempStatsResult.getData().get(index).get(i));
						System.out.print(tempStatsResult.getData().get(index).get(i)+",");
					}
					System.out.println("");
				}				
				theStatsResult.setData(newData);
								
				GraphComposite tempComposite = null;
				if(theStatsResult.getRowName().size()==12){					
					SimpleDateFormat format1 = new SimpleDateFormat("yyyy,MM");
					Date fromDate =null;
					Date toDate =null;
					try {
						fromDate=format1.parse(dateTime.getYear()+","+(dateTime.getMonth()+1));
						toDate=format1.parse(dateTime_1.getYear()+","+(dateTime_1.getMonth()+1));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					List<String> newRowName = new ArrayList<String>();					
					List<List<Double>> theData = new ArrayList<List<Double>>();
					
					for(int i=0;i<theStatsResult.getColName().size();i++)
						theData.add(new ArrayList<Double>());
					for(int i=0;i< theStatsResult.getRowName().size();i++){
						SimpleDateFormat format = new SimpleDateFormat("yyyy");
						Date date =null;
						try {
							date = format.parse(theStatsResult.getRowName().get(i));
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if(date.compareTo(fromDate)>=0 && date.compareTo(toDate)<=0)
							newRowName.add(theStatsResult.getRowName().get(i));
						for(int j=0;j<theStatsResult.getColName().size();j++)
							theData.get(j).add(theStatsResult.getData().get(j).get(i));
					}
					theStatsResult.setRowName(newRowName);
					theStatsResult.setData(theData);
				}
				if(theStatsResult.getType()==DriverConstant.GraphType_Pie){
					scrolledComposite.setMinSize(GRAPH_WIDTH-30,GRAPH_HEIGHT-30);
					tempComposite = new PieComposite(content, theStatsResult);
				}
				else if(theStatsResult.getType()==DriverConstant.GraphType_Bar){					
					scrolledComposite.setMinSize(GRAPH_WIDTH/5*theStatsResult.getRowName().size(),GRAPH_HEIGHT-30);
					tempComposite = new BarComposite(content, theStatsResult);
				}
				else if(theStatsResult.getType()==DriverConstant.GraphType_Line){					
					scrolledComposite.setMinSize(GRAPH_WIDTH/5*theStatsResult.getRowName().size(),GRAPH_HEIGHT-30);
					tempComposite = new LineComposite(content, theStatsResult);
				}
					
				if(curComposite!=null) curComposite.dispose();
				curComposite=tempComposite;
				
			}
		});
		btnGo.setText("统计");
		btnGo.setBounds(384, 93, 80, 27);
		
		Button btnBack = new Button(this, SWT.NONE);
		btnBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new StatsTableWindow(parent, SWT.NONE,tempStatsResult);  
                temp_this.dispose();
			}
		});
		btnBack.setText("后退");
		btnBack.setBounds(384, 249, 80, 27);

	}
	private String[] lsToStrs(List<String> ls){
		String[] strs = new String[ls.size()];
		for(int i = 0;i<ls.size();i++){
			strs[i]=ls.get(i);
		}
		return strs;
	}
	
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
