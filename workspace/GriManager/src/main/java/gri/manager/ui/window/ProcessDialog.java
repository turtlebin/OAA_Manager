package gri.manager.ui.window;

import java.util.List;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import gri.driver.manager.ProcessManager;
import gri.driver.model.process.Container;
import gri.driver.model.process.Process;
import gri.driver.model.process.Processor;
import gri.manager.util.Constant;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;

public class ProcessDialog extends Dialog {

	protected Object result;
	private Shell parentShell;
	protected Shell shell;
	public Label lbParagraph;
	public Combo comboProcessor;
	public Combo comboContainer;
	public Text textConfig;

	private ProcessManager manager;

	private int windowType;
	private Process oldProcess;
	private List<Processor> processors;
	private List<Container> containers;
	private String account;
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ProcessDialog(ProcessManager manager, int windowType, String account, Process oldProcess, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.manager = manager;
		this.windowType = windowType;
		this.oldProcess = oldProcess;
		this.account = account;
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
		shell.setSize(269, 250);
		shell.setText("添加处理");
		if (this.windowType == Constant.WindowType_Edit)
			shell.setText("编辑处理");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		Label label = new Label(shell, SWT.NONE);
		label.setBounds(13, 27, 72, 17);
		label.setText("*段名：");

		lbParagraph = new Label(shell, SWT.NONE);
		lbParagraph.setText(oldProcess.getParagraphName());
		lbParagraph.setBounds(101, 24, 137, 23);


		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(13, 60, 72, 17);
		label_2.setText("*处理器：");
		
		comboProcessor = new Combo(shell, SWT.NONE);
		processors=manager.listProcessor();
		String[] processorNames = new String[processors.size()];
		for(int i = 0;i<processors.size();i++)
			processorNames[i]=processors.get(i).getName();
		comboProcessor.setItems(processorNames);
		comboProcessor.setBounds(101, 57, 88, 25);		
		
		comboProcessor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshComboContainer();
			}
		});
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setBounds(13, 93, 72, 17);
		label_3.setText("*容器空间：");

		comboContainer = new Combo(shell, SWT.NONE);
		comboContainer.setBounds(101, 90, 88, 25);
		
		Button button_addContainer = new Button(shell, SWT.NONE);
		button_addContainer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new ContainerDialog(manager, Constant.WindowType_Add, account, null, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
				refreshComboContainer();
			}
		});
		button_addContainer.setBounds(204, 88, 56, 27);
		button_addContainer.setText("创建空间");
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setBounds(13, 126, 72, 17);
		label_4.setText("处理配置：");

		textConfig = new Text(shell, SWT.BORDER);
		textConfig.setBounds(101, 123, 88, 25);
		
		Button button_browse = new Button(shell, SWT.NONE);
		button_browse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new ContainerDialog(manager, Constant.WindowType_Add, account, null, shell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
				refreshComboContainer();
			}
		});
		button_browse.setBounds(204, 121, 56, 27);
		button_browse.setText("浏览");
		
		Button button_save = new Button(shell, SWT.NONE);
		button_save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			
				Process process = oldProcess;
				
				if (comboContainer.getText().equals("")) {
					MessageBox box = new MessageBox(shell, SWT.ICON_WARNING);
					box.setMessage("处理信息必须填写完整！");
					box.open();
					return;
				}
				
				process.setContainerId(containers.get(comboContainer.getSelectionIndex()).getId());
				process.setConfig(textConfig.getText().trim());
				
				

				if (windowType == Constant.WindowType_Add) {
					boolean result = manager.addProcess(process);
					if(result) shell.close();
				} else if (windowType == Constant.WindowType_Edit) {
					boolean result = manager.updateProcess(process);
					if(result) shell.close();
				}
			}
		});
		button_save.setBounds(61, 168, 56, 27);
		button_save.setText("确定");

		Button button_cancel = new Button(shell, SWT.NONE);
		button_cancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_cancel.setBounds(123, 168, 56, 27);
		button_cancel.setText("取消");
		
		if (this.windowType == Constant.WindowType_Edit)
			loadData();
	}

	private void loadData() {			
		for(int i = 0; i<processors.size();i++)
			if(processors.get(i).getName().equals(oldProcess.getProcessorName()))
				comboProcessor.select(i);	
		
		containers=manager.listContianerByProcessor(account,oldProcess.getProcessorId());
		String[] containerNames = new String[containers.size()];
		int containerSelectedIndex = -1;
		for(int i = 0;i<containers.size();i++){
			containerNames[i]=containers.get(i).getName();
			if(containerNames[i].equals(oldProcess.getContainerName())) containerSelectedIndex = i;
		}
		comboContainer.setItems(containerNames);
		if(containerSelectedIndex!=-1)
			comboContainer.select(containerSelectedIndex);
		
		textConfig.setText(oldProcess.getConfig());
	}
	
	private void refreshComboContainer(){
		Integer curProcessorId = processors.get(comboProcessor.getSelectionIndex()).getId();
		containers=manager.listContianerByProcessor(account,curProcessorId);
		String[] containerNames = new String[containers.size()];
		for(int i = 0;i<containers.size();i++)
			containerNames[i]=containers.get(i).getName();					
		comboContainer.setItems(containerNames);
	}
}