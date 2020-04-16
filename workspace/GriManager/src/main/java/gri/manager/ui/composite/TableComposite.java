package gri.manager.ui.composite;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.eclipse.swt.widgets.Composite;
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
public class TableComposite extends Composite {
	private JSONArray arr;
	private Table table;
	private Label lbl_pageNo;
	private Label lbl_totalPage;
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TableComposite(Composite parent, int style, JSONArray arr) {
		super(parent, style);
		
		this.arr= arr;
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(0, 0, 420, 256);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		Button btn_pre = new Button(this, SWT.NONE);
		btn_pre.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshTable(Integer.parseInt(lbl_pageNo.getText())-2,10);
			}
		});
		btn_pre.setBounds(10, 263, 27, 27);
		btn_pre.setText("<");
		
		Button btn_next = new Button(this, SWT.NONE);
		btn_next.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshTable(Integer.parseInt(lbl_pageNo.getText()),10);
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
		
		refreshTable(0,10);
		
		this.pack();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	//如第一页的参数是0,10
	public void refreshTable(int pageNo, int pageSize){
		if(arr==null || pageNo<0 || pageNo*pageSize>=arr.size()) return;
		
		if(table.getColumns().length==0){
			JSONObject obj = (JSONObject) arr.get(pageNo*pageSize);//随便获取一个JSONObject
			Iterator it = obj.keys();//获取所有的key值
			while (it.hasNext()) {
				String key = (String)it.next();
				TableColumn tableColumn = new TableColumn(table, SWT.NONE);  
	            tableColumn.setText(key);//设置列名
			}
			lbl_totalPage.setText(Integer.toString((arr.size()-1)/pageSize+1));
		}
		
		lbl_pageNo.setText(Integer.toString(pageNo+1));
		table.removeAll();
		
		int min = (pageNo+1)*pageSize < arr.size() ? (pageNo+1)*pageSize:arr.size();
		for(int i = pageNo*pageSize ; i<min ;i++){//pageNo会随输入refresh的参数改变而改变，但是一个表中固定大小为10行
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
        	table.getColumn(i).pack();//调整窗口大小
        }
		//table.pack();
	}
	
	public void setWidth(int width){
		table.setBounds(0, 0, width, 256);
		this.pack();
	}
}
