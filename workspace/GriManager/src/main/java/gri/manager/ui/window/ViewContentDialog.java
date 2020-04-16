package gri.manager.ui.window;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import gri.driver.manager.ProcessManager;
import gri.driver.manager.UserManager;
import gri.driver.model.GriElement;
import gri.driver.model.process.Paragraph;
import gri.driver.model.User;
import gri.driver.model.process.Container;
import gri.driver.model.process.Process;
import gri.driver.model.process.Processor;
import gri.driver.model.process.View;
import gri.manager.ui.composite.TableComposite;
import gri.manager.util.Constant;
import gri.manager.util.RunTimeData;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;

public class ViewContentDialog extends Dialog {

	protected Object result;
	private Shell parentShell;
	protected Shell shell;
	private ProcessManager manager;

	private Process process;
	private TableComposite table;
	private Text text_preview_data;
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ViewContentDialog(ProcessManager manager, Process process, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.manager = manager;
		this.process = process;
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
		shell.setSize(359, 480);
		shell.setText("处理结果视图");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		


		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(38, 27, 72, 17);
		label_2.setText("处理器：");
		
		Label lbProcessor = new Label(shell, SWT.NONE);
		lbProcessor.setText(process.getProcessorName());
		lbProcessor.setBounds(126, 24, 88, 25);		
		
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setBounds(38, 60, 72, 17);
		label_3.setText("容器：");

		Label lbContainer = new Label(shell, SWT.NONE);
		lbContainer.setText(process.getContainerName());
		lbContainer.setBounds(126, 57, 88, 25);
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(38, 93, 72, 17);
		label.setText("视图：");

		final Combo comboView = new Combo(shell, SWT.NONE);
		List<View> views=manager.listViewByContainer(process.getContainerId());
		
		String[] viewArr = new String[views.size()];
		for(int i = 0;i<views.size();i++) viewArr[i]= views.get(i).getSubViewName();
		comboView.setItems(viewArr);
		comboView.setBounds(126, 90, 137, 23);
		comboView.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String text = manager.readView(process.getContainerName(), comboView.getText()).toString();
				View view = manager.getView(process.getContainerName(), comboView.getText());
				if(view.getStructure().equals("list")){
					JSONArray json= JSONArray.fromObject(text);
					if(text_preview_data!=null) text_preview_data.dispose();
					if(table!=null) table.dispose();
					table = new TableComposite(shell,SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI, json);	
					table.setBounds(38, 149, 277, 297);
				}
				else{
					if(text_preview_data!=null) text_preview_data.dispose();
					if(table!=null) table.dispose();
					text_preview_data = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);					
					text_preview_data.setText(text);
					text_preview_data.setBounds(38, 149, 277, 260);
				}
			}
		});
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setBounds(38, 126, 72, 17);
		label_4.setText("处理结果：");
		
		/*table = new TableComposite(shell,SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI, new JSONArray());	
		table.setBounds(38, 149, 277, 297);*/
		
				
	}	
}