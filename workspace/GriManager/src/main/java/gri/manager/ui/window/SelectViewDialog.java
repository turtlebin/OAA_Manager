package gri.manager.ui.window;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.SWT;

import gri.driver.manager.GriDocManager;
import gri.driver.manager.ProcessManager;
import gri.driver.model.Connection;
import gri.driver.model.process.Container;
import gri.driver.model.process.View;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SelectViewDialog extends Dialog {
	protected Object result;
	protected Shell shell;
	private Shell parentShell;
	private ProcessManager manager;
	private String account;
	
	private List<Container> containers;
	private List<View> views;
	
	private Combo combo_container;
	private Combo combo_view;
	
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectViewDialog(Shell parent, String host, String user, String password, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.account = user;
		Connection conn=new Connection("新建连接", "EITP://"+host, user, password);
		manager = new GriDocManager(conn).ProcessManager();
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
	@SuppressWarnings("unchecked")
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(314, 180);
		shell.setText("选择数据：");
		shell.setImage(new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/add16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(38, 27, 72, 17);
		label_1.setText("*容器：");

		combo_container = new Combo(shell, SWT.NONE);
		containers=manager.listContainer(account);
		String[] containerNames = new String[containers.size()];
		for(int i = 0;i<containers.size();i++)
			containerNames[i]=containers.get(i).getName();
		combo_container.setItems(containerNames);
		combo_container.setBounds(126, 24, 88, 25);				
		combo_container.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Container container = containers.get(combo_container.getSelectionIndex());				
				views=manager.listViewByContainer(container.getId());
				String[] viewNames = new String[views.size()];
				for(int i = 0;i<views.size();i++)
					viewNames[i]=views.get(i).getSubViewName();
				combo_view.setItems(viewNames);
			}
		});
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(38, 60, 72, 17);
		label_2.setText("*视图：");
		
		combo_view = new Combo(shell, SWT.NONE);
		combo_view.setBounds(126, 57, 88, 25);
				
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(combo_container.getText().equals("") || combo_view.getText().equals("")) {
					MessageBox box = new MessageBox(shell, SWT.ICON_WARNING);
					box.setMessage("必须选择段！");
					box.open();
					return;
				}
					
				result = combo_container.getText()+"-"+combo_view.getText();
				shell.close();
			}
		});
		button.setBounds(72, 95, 59, 27);
		button.setText("确定");

		Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_1.setBounds(169, 95, 59, 27);
		button_1.setText("取消");
	}

}
