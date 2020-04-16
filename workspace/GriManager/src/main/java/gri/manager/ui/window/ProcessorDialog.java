package gri.manager.ui.window;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import gri.driver.manager.ProcessManager;
import gri.driver.model.process.Processor;
import gri.manager.util.Constant;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;

public class ProcessorDialog extends Dialog {

	protected Object result;
	private Shell parentShell;
	protected Shell shell;
	public Text textName;
	public Text textClassName;
	public Combo comboState;
	public Text textConfigStruct;
	public Text textViewClass;

	private ProcessManager manager;

	private int windowType;
	private Processor oldProcessor;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ProcessorDialog(ProcessManager manager, int windowType, Processor oldProcessor, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.manager = manager;
		this.windowType = windowType;
		this.oldProcessor = oldProcessor;
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
		shell.setSize(319, 280);
		shell.setText("新建处理器");
		if (this.windowType == Constant.WindowType_Edit)
			shell.setText("编辑处理器");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		Label label = new Label(shell, SWT.NONE);
		label.setBounds(38, 27, 72, 17);
		label.setText("*处理器名：");

		textName = new Text(shell, SWT.BORDER);
		textName.setBounds(126, 24, 137, 23);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(38, 60, 72, 17);
		label_1.setText("*处理器程序：");

		textClassName = new Text(shell, SWT.BORDER);
		textClassName.setBounds(126, 57, 137, 23);

		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(38, 93, 72, 17);
		label_2.setText("*运行状态：");
		
		comboState = new Combo(shell, SWT.NONE);
		comboState.setItems(new String[]{"运行","停止"});
		comboState.setBounds(126, 90, 88, 25);
	
				
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setText("配置结构：");
		label_3.setBounds(38, 126, 72, 17);
		
		textConfigStruct = new Text(shell, SWT.BORDER);
		textConfigStruct.setBounds(126, 123, 97, 23);
		
		Button btn = new Button(shell, SWT.NONE);
		btn.setBounds(227, 123, 32, 23);
		btn.setText("浏览");
		
		Label label_7 = new Label(shell, SWT.NONE);
		label_7.setText("虚拟视图类：");
		label_7.setBounds(38, 159, 72, 17);
		
		textViewClass = new Text(shell, SWT.BORDER);
		textViewClass.setBounds(126, 156, 137, 23);
		
		Button button_save = new Button(shell, SWT.NONE);
		button_save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			
				Processor processor = null;
				if (windowType == Constant.WindowType_Add) processor = new Processor();
				else if (windowType == Constant.WindowType_Edit) processor = oldProcessor;
				
				
				processor.setName(textName.getText().trim());
				processor.setClassName(textClassName.getText().trim());
				processor.setState(comboState.getText());
				processor.setConfigStruct(textConfigStruct.getText().trim());
				processor.setViewClass(textViewClass.getText().trim());
				
				
				if (processor.getName().equals("") || processor.getClassName().equals("") || processor.getState().equals("")) {
					MessageBox box = new MessageBox(shell, SWT.ICON_WARNING);
					box.setMessage("处理器信息必须填写完整！");
					box.open();
					return;
				}

				if (windowType == Constant.WindowType_Add) {
					boolean result = manager.addProcessor(processor);
					if(result) shell.close();
					/*if (user != null) {
						RunTimeData.userView.add(user);
						if (UserManageDialog.tableViewer != null)
							UserManageDialog.tableViewer.refresh();
						shell.close();
					}*/
				} else if (windowType == Constant.WindowType_Edit) {
					boolean result = manager.updateProcessor(processor);
					if(result) shell.close();
				}
			}
		});
		button_save.setBounds(85, 201, 56, 27);
		button_save.setText("确定");

		Button button_cancel = new Button(shell, SWT.NONE);
		button_cancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_cancel.setBounds(171, 201, 56, 27);
		button_cancel.setText("取消");
		
		if (this.windowType == Constant.WindowType_Edit)
			loadData();
	}

	private void loadData() {
		textName.setText(this.oldProcessor.getName());
		textClassName.setText(oldProcessor.getClassName());
		
		Map<String, Integer> stateMap = new HashMap<String, Integer>(){{
			put("运行", 0);put("停止", 1);
	    }};
	    comboState.select(stateMap.get(oldProcessor.getState()));
		
		textConfigStruct.setText(oldProcessor.getConfigStruct());
		
		textViewClass.setText(this.oldProcessor.getViewClass());
	}
}
