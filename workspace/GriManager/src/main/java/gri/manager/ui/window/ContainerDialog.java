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
import gri.driver.model.process.Processor;
import gri.manager.util.Constant;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;

public class ContainerDialog extends Dialog {

	protected Object result;
	private Shell parentShell;
	protected Shell shell;
	public Text textName;
	public Combo comboUser;
	public Combo comboProcessor;

	private ProcessManager manager;

	private int windowType;
	private Container oldContainer;
	private List<Processor> processors;
	private String account;
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ContainerDialog(ProcessManager manager, int windowType, String account, Container oldContainer, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.manager = manager;
		this.windowType = windowType;
		this.oldContainer = oldContainer;
		this.account =account;
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
		shell.setSize(319, 180);
		shell.setText("新建容器空间");
		if (this.windowType == Constant.WindowType_Edit)
			shell.setText("编辑容器空间");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		Label label = new Label(shell, SWT.NONE);
		label.setBounds(38, 27, 72, 17);
		label.setText("*容器名：");

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
		comboProcessor.setBounds(126, 57, 88, 25);		
		
		Button button_save = new Button(shell, SWT.NONE);
		button_save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			
				Container container = null;
				if (windowType == Constant.WindowType_Add) container = new Container();
				else if (windowType == Constant.WindowType_Edit) container = oldContainer;
				
				if (textName.getText().trim().equals("") && comboProcessor.getText().equals("")) {
					MessageBox box = new MessageBox(shell, SWT.ICON_WARNING);
					box.setMessage("容器空间信息必须填写完整！");
					box.open();
					return;
				}
				
				container.setName(textName.getText().trim());
				container.setProcessorId(processors.get(comboProcessor.getSelectionIndex()).getId());
				container.setUserName(account);
				
				

				if (windowType == Constant.WindowType_Add) {
					boolean result = manager.addContainer(container);
					if(result) shell.close();
					/*if (user != null) {
						RunTimeData.userView.add(user);
						if (UserManageDialog.tableViewer != null)
							UserManageDialog.tableViewer.refresh();
						shell.close();
					}*/
				} else if (windowType == Constant.WindowType_Edit) {
					boolean result = manager.updateContainer(container);
					if(result) shell.close();
				}
			}
		});
		button_save.setBounds(86, 106, 56, 27);
		button_save.setText("确定");

		Button button_cancel = new Button(shell, SWT.NONE);
		button_cancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_cancel.setBounds(158, 106, 56, 27);
		button_cancel.setText("取消");
		
		if (this.windowType == Constant.WindowType_Edit)
			loadData();
	}

	private void loadData() {
		textName.setText(this.oldContainer.getName());
		
		for(int i = 0; i<processors.size();i++)
			if(processors.get(i).getName().equals(this.oldContainer.getProcessorName()))
				comboProcessor.select(i);	
	}
}
