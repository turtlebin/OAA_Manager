package gri.manager.ui.composite;

import gri.driver.manager.GriDocManager;
import gri.driver.model.PageDto;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;

/**
 * @Description 
 * Created by erik on 2016-8-15
 */
public class PagedTableComposite extends Composite {
	private Shell parentWindow;
	
	private Integer paragraphId;
	private PageDto pageDto;
	private static final Integer PAGE_SIZE = 10;
	private GriDocManager manager;
	
	
	private Table table;
	private Label lbl_pageNo;
	private Label lbl_totalPage;
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PagedTableComposite(Composite parent, Shell parentWindow, int style, int paragraphId, GriDocManager manager) {		
		super(parent, style);
		
		this.paragraphId = paragraphId;
		this.manager = manager;
		this.parentWindow = parentWindow;
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(0, 0, 420, 256);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		Button btn_pre = new Button(this, SWT.NONE);
		btn_pre.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshTable(Integer.parseInt(lbl_pageNo.getText())-1,PAGE_SIZE);
			}
		});
		btn_pre.setBounds(10, 263, 27, 27);
		btn_pre.setText("<");
		
		Button btn_next = new Button(this, SWT.NONE);
		btn_next.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshTable(Integer.parseInt(lbl_pageNo.getText())+1,PAGE_SIZE);
			}
		});
		btn_next.setText(">");
		btn_next.setBounds(164, 263, 27, 27);
		
		lbl_pageNo = new Label(this, SWT.NONE);
		lbl_pageNo.setAlignment(SWT.RIGHT);
		lbl_pageNo.setBounds(53, 268, 33, 17);
		
		Label lblOf = new Label(this, SWT.NONE);
		lblOf.setText("/");
		lblOf.setBounds(92, 268, 5, 17);
		
		lbl_totalPage = new Label(this, SWT.NONE);
		lbl_totalPage.setBounds(106, 268, 33, 17);
		
		refreshTable(1,10);
		
		this.pack();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	//如第一页的参数是0,10
	public void refreshTable(int pageNo, int pageSize){
		if(pageNo<=0 || (pageDto != null && (pageNo-1)*pageSize>=pageDto.getTotalRecord())) return;//如果页号小于等于0或其他异常情况
		
		pageDto = manager.readDbPage(paragraphId, pageNo, pageSize);		
		
		if(pageDto==null){
			MessageBox box = new MessageBox(parentWindow, SWT.ICON_WARNING);
			box.setMessage("数据读取异常，请先同步数据");
			box.open();
			return;
		}
		System.out.println("page,pageNo:"+pageNo+",pageSize:"+pageSize+",totalRecord:"+pageDto.getTotalRecord());
		JSONArray arr = JSONArray.fromObject(pageDto.getData());
		
		//如果未初始化过，则初始化列名
		if(table.getColumns().length==0 && arr.size()>0){
			JSONObject obj = (JSONObject) arr.get(0);
			Iterator it = obj.keys();
			while (it.hasNext()) {
				String key = (String)it.next();
				TableColumn tableColumn = new TableColumn(table, SWT.NONE);  
	            tableColumn.setText(key);
			}
		}
		
		if(pageDto.getTotalRecord() == 0) pageNo=0;
		lbl_pageNo.setText(Integer.toString(pageNo));
		lbl_totalPage.setText(Integer.toString((pageDto.getTotalRecord()-1)/pageSize+1));
		table.removeAll();
		

		for(int i = 0 ; i<arr.size() ;i++){
			TableItem item = new TableItem(table, SWT.NONE);
			JSONObject obj = (JSONObject) arr.get(i);
			Iterator it = obj.keys();
			int j =0;
			String [] strs= new String[obj.size()];
			while (it.hasNext()) {
				String key = (String)it.next();
				strs[j]= obj.getString(key);
				j++;
			}
	        item.setText(strs);
		}
		
		for (int i = 0; i < table.getColumns().length; i++)  
        {  
        	table.getColumn(i).pack();
        }
		System.out.println("table columns length:"+table.getColumns().length);
		//table.pack();
	}
	
	public void setWidth(int width){
		System.out.println("table width:"+width);
		table.setBounds(0, 0, width, 256);
		this.pack();
	}
}
