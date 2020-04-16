package gri.manager.ui.window;


import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * @Description 
 * Created by erik on 2016-9-8
 */
public class AddFieldDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Shell parent;
	private String[] comboNames;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AddFieldDialog(Shell parent, int style,String[] comboNames) {
		super(parent, style);
		this.parent =parent;
		this.comboNames = comboNames;
		setText("增加列");
	}

	/**
	 * Open the dialog.
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
		shell.setSize(231, 173);
		shell.setText(getText());
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(22, 24, 42, 17);
		label.setText("列名：");
		
		final Combo comboName = new Combo(shell, SWT.NONE);
		comboName.setItems(comboNames);
		//comboName.setItems(new String[] {"id", "name", "year", "weight", "height"});
		comboName.setBounds(107, 21, 88, 25);
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("统计运算：");
		label_1.setBounds(22, 67, 60, 17);
		
		final Combo comboFunc = new Combo(shell, SWT.NONE);
		comboFunc.setItems(new String[] {"统计数量", "求和", "平均值", "最大值", "最小值"});
		comboFunc.setBounds(107, 64, 88, 25);
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = comboName.getText()+"的"+comboFunc.getText();
				close();
			}
		});
		button.setBounds(22, 108, 80, 27);
		button.setText("保存");

	}
	
	
	public void close() {
		shell.close();
	}

}
