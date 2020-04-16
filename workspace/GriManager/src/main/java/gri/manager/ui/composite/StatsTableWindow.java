package gri.manager.ui.composite;


import gri.driver.model.process.StatsResult;
import gri.manager.ui.window.StatsDialog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.SWT;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 
 * Created by erik on 2016-9-8
 */
public class StatsTableWindow extends Composite {
	private static final Logger LOGGER = LoggerFactory.getLogger(StatsTableWindow.class);
	private StatsResult statsResult;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public StatsTableWindow(final Composite parent, int style, StatsResult statsResult) {
		super(parent, style);
		
		this.statsResult=statsResult;
		this.setSize(StatsDialog.WIDTH, StatsDialog.HEIGHT);
		
		Label label = new Label(this, SWT.NONE);
		label.setBounds(0, 23, 82, 17);
		label.setText("统计结果展示");
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(0, 42, 64, 64);
		
		List<List<Double>> data = statsResult.getData();
		
		JSONArray arr = new JSONArray();
		
		/*for(int i= 0; i<data.size();i++){
			JSONObject obj = new JSONObject();
			obj.accumulate("",statsResult.getRowName().get(i) );
			for(int j =0 ;j<data.get(i).size();j++)
				obj.accumulate(statsResult.getColName().get(j), data.get(i).get(j));
			arr.add(obj);
		}*/
		
		
		int secondDimenSize = data.size();
		if(secondDimenSize>0) 
			for(int i= 0; i<data.get(0).size();i++){
				JSONObject obj = new JSONObject();
				obj.accumulate("",statsResult.getRowName().get(i) );
				for(int j =0 ;j<secondDimenSize;j++)
					obj.accumulate(statsResult.getColName().get(j), data.get(j).get(i));
				arr.add(obj);
			}
		
		System.out.println(arr.toString());
		
		TableComposite tableComposite = new TableComposite(composite, SWT.NONE,arr);
		tableComposite.setWidth(300);
		composite.pack();
		
		Button btnGo = new Button(this, SWT.NONE);
		final StatsTableWindow temp_this= this;
		final StatsResult tempStatsResult = statsResult;
		btnGo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new StatsGraphWindow(parent, SWT.NONE,tempStatsResult);  
                temp_this.dispose();
			}
		});
		btnGo.setBounds(362, 42, 80, 27);
		btnGo.setText("统计图");
		
		
		Button btnSave = new Button(this, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String [][] data = new String[tempStatsResult.getColName().size()+1][tempStatsResult.getRowName().size()+1];
				//System.out.println(main.getStatsResult().getRowName().size()+","+main.getStatsResult().getColName().size()+","+main.getStatsResult().getData().size());
				data[0][0]="";
				for(int i =0 ;i<tempStatsResult.getRowName().size();i++)
					data[0][i+1]= tempStatsResult.getRowName().get(i);
				for(int i =0 ;i<tempStatsResult.getColName().size();i++)
					data[i+1][0]= tempStatsResult.getColName().get(i);
				for(int i =0 ;i<tempStatsResult.getRowName().size();i++)
					for(int j =0 ;j<tempStatsResult.getColName().size();j++)
					data[j+1][i+1]= Double.toString(tempStatsResult.getData().get(j).get(i));
				//System.out.println(main.getStatsResult().getRowName().size()+":"+main.getStatsResult().getColName().size());
				FileDialog file =new FileDialog(parent.getShell(), SWT.SAVE);
				file.setFilterExtensions(new String[] { "*.txt" });
				String filePath = file.open();
				try {
					FileOutputStream out = new FileOutputStream(filePath, false);
					
					out.write(JSONArray.fromObject(data).toString().getBytes());
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSave.setBounds(362, 94, 80, 27);
		btnSave.setText("导出");
		
		
		/*Button btnBack = new Button(this, SWT.NONE);
		btnBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new StatsConfWindow(parent, SWT.NONE,main);  
                temp_this.dispose();
			}
		});
		btnBack.setText("后退");
		btnBack.setBounds(362, 146, 80, 27);*/
		
		/*Button button_3 = new Button(this, SWT.NONE);
		button_3.setBounds(362, 198, 80, 27);
		button_3.setText("退出");*/

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
