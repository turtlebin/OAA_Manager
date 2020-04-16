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

import gri.driver.manager.GriDocManager;
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

public class BlockDialog extends Dialog {

	protected Object result;
	private Shell parentShell;
	protected Shell shell;
	private GriDocManager manager;
	private Integer paragraphId;
	
	private TableComposite table;
	private Text textBlockName;
	
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public BlockDialog(GriDocManager manager, Integer paragraphId, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.manager = manager;
		this.paragraphId = paragraphId;
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
		shell.setText("区块链");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);


		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(38, 27, 72, 17);
		label_2.setText("所属区块链：");
		
		textBlockName = new Text(shell, SWT.BORDER);
		textBlockName.setBounds(126, 24, 137, 23);	
		textBlockName.setText(manager.ProcessManager().getBlockName(paragraphId));
		
		Button button_edit = new Button(shell, SWT.NONE);
		button_edit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				manager.ProcessManager().setBlockName(paragraphId,textBlockName.getText());		
			}
		});
		button_edit.setBounds(287, 22, 56, 27);
		button_edit.setText("编辑");
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setBounds(38, 57, 72, 17);
		label_4.setText("处理结果：");	
		
		Button button_view = new Button(shell, SWT.NONE);
		button_view.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String text = "[]";
				if(textBlockName.getText()==null || textBlockName.getText().equals("")){
					text = manager.previewParagraphData(paragraphId);
				}
				else{
					text = manager.ProcessManager().readBlock(textBlockName.getText()).toString();
				}
				
				JSONArray json= JSONArray.fromObject(text);
				if(table!=null) table.dispose();
				table = new TableComposite(shell,SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI, json);	
				table.setBounds(38, 87, 277, 297);				
			}
		});
		button_view.setBounds(126, 54, 56, 27);
		button_view.setText("查看");			
	}	
}