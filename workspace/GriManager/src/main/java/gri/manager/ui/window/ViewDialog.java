package gri.manager.ui.window;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import gri.driver.manager.ProcessManager;
import gri.driver.model.process.View;
import gri.driver.model.process.Processor;
import gri.driver.model.process.View;
import gri.manager.util.Constant;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;

public class ViewDialog extends Dialog {

	protected Object result;
	private Shell parentShell;
	protected Shell shell;
	public Text textName;	
	public Combo comboProcessor;
	public Combo comboIsVirtual;
	public Combo comboStore;
	public Combo comboStructure;

	private ProcessManager manager;

	private int windowType;
	private View view;
	private List<Processor> processors;
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ViewDialog(ProcessManager manager, int windowType, View view, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.manager = manager;
		this.windowType = windowType;
		this.view = view;
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
		shell.setSize(319, 260);
		shell.setText("新建视图空间");
		if (this.windowType == Constant.WindowType_Edit)
			shell.setText("编辑视图空间");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		Label label = new Label(shell, SWT.NONE);
		label.setBounds(38, 27, 72, 17);
		label.setText("*视图名：");

		textName = new Text(shell, SWT.BORDER);
		textName.setBounds(126, 24, 137, 23);

		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(38, 60, 72, 17);
		label_2.setText("*所属处理器：");
		
		comboProcessor = new Combo(shell, SWT.NONE);
		processors=manager.listProcessor();
		String[] processorNames = new String[processors.size()];		
		for(int i = 0;i<processors.size();i++)
			processorNames[i]=processors.get(i).getName();
		comboProcessor.setItems(processorNames);
		comboProcessor.setBounds(126, 57, 137, 23);
		if(view != null){
			//comboProcessor.setEditable(false);
			for(int i = 0;i<processors.size();i++)
				if(processors.get(i).getId() == view.getProcessorId()){
					comboProcessor.select(i);
					break;
				}
		}		
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setBounds(38, 93, 72, 17);
		label_3.setText("*虚拟视图：");
		
		comboIsVirtual = new Combo(shell, SWT.NONE);
		comboIsVirtual.setItems(new String[]{"是","否"});
		comboIsVirtual.setBounds(126, 90, 88, 25);
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setBounds(38, 126, 72, 17);
		label_4.setText("视图存储：");
		
		comboStore = new Combo(shell, SWT.NONE);
		comboStore.setItems(new String[]{"mysql","redis","file"});
		comboStore.setBounds(126, 123, 88, 25);
		
		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setBounds(38, 159, 72, 17);
		label_5.setText("*视图结构：");
		
		comboStructure = new Combo(shell, SWT.NONE);
		comboStructure.setItems(new String[]{"list","map","object"});
		comboStructure.setBounds(126, 156, 88, 25);
		
		Button button_save = new Button(shell, SWT.NONE);
		button_save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			
				if(view==null) view = new View();
				
				if (textName.getText().trim().equals("") && comboProcessor.getText().equals("") && comboIsVirtual.getText().equals("") && comboStructure.getText().equals("")) {
					MessageBox box = new MessageBox(shell, SWT.ICON_WARNING);
					box.setMessage("视图空间信息必须填写完整！");
					box.open();
					return;
				}
				
				view.setSubViewName(textName.getText().trim());
				view.setProcessorId(processors.get(comboProcessor.getSelectionIndex()).getId());
				view.setVirtual(comboIsVirtual.getText().equals("是"));
				view.setStore(comboStore.getText());
				view.setStructure(comboStructure.getText());
				
				

				if (windowType == Constant.WindowType_Add) {
					boolean result = manager.addView(view);
					if(result) shell.close();
				} else if (windowType == Constant.WindowType_Edit) {
					boolean result = manager.updateView(view);
					if(result) shell.close();
				}
			}
		});
		button_save.setBounds(86, 192, 56, 27);
		button_save.setText("确定");

		Button button_cancel = new Button(shell, SWT.NONE);
		button_cancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_cancel.setBounds(158, 192, 56, 27);
		button_cancel.setText("取消");
		
		if (this.windowType == Constant.WindowType_Edit)
			loadData();
	}

	private void loadData() {
		textName.setText(view.getSubViewName());
		
		for(int i = 0; i<processors.size();i++)
			if(processors.get(i).getName().equals(view.getProcessorName()))
				comboProcessor.select(i);	
		
		Map<String, Integer> viewStoreMap = new HashMap<String, Integer>(){{
			put("mysql", 0);put("redis", 1);put("file", 2);
	    }};
		comboStore.select(viewStoreMap.get(view.getStore()));
		
		Map<String, Integer> viewStructureMap = new HashMap<String, Integer>(){{
			put("list", 0);put("map", 1);put("object", 2);
	    }};
		comboStructure.select(viewStructureMap.get(view.getStructure()));
		
		comboIsVirtual.select(view.isVirtual() ? 0:1);
	}
}
