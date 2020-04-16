package gri.manager.ui.window;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import gri.driver.manager.GriDocManager;
import gri.driver.model.process.Paragraph;
import gri.driver.util.DriverConstant;
import gri.manager.util.DataHelper;
import gri.manager.util.PathHelper;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;

public class SavaDataDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Shell parentShell;
	private Paragraph paragraph;
	private GriDocManager manager;
	private Text text_file;

	public SavaDataDialog(Paragraph paragraph, GriDocManager manager, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.paragraph = paragraph;
		this.manager = manager;
	}

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

	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(366, 261);
		shell.setText("保存数据到本地文件");
		shell.setImage(
				new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/download16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 340, 167);

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("常规");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 30, 36, 17);
		label.setText("文件：");

		text_file = new Text(composite, SWT.BORDER);
		text_file.setBounds(52, 27, 212, 23);

		Button button_choose = new Button(composite, SWT.NONE);
		button_choose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog file = new FileDialog(shell);
				String extensionFilter = getExtensionFilter();//获取过滤器包含的扩展名
				file.setFilterExtensions(new String[] { extensionFilter });//设置过滤器包含的扩展名
				String filePath = file.open();
				if (filePath != null) {
					String extension = file.getFilterExtensions()[0];
					if (extension.equals("*.*"))//如果文件过滤器不过滤文件格式
						extension = "";
					else {
						int index = extension.lastIndexOf('.');
						if (index != -1)
							extension = extension.substring(index, extension.length());//获取扩展名
						else
							extension = "";
					}
					if (!filePath.toLowerCase().endsWith(extension))
						filePath += extension;//到此处的操作为止是获取文件路径
					text_file.setText(filePath);
				}
			}
		});
		button_choose.setBounds(270, 25, 48, 27);
		button_choose.setText("选择");

		Button button_ok = new Button(shell, SWT.NONE);
		button_ok.setBounds(224, 183, 60, 27);
		button_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final String filePath = text_file.getText().trim();
				if (filePath.equals(""))
					return;
				new Thread() {
					public void run() {
						boolean succeed = new DataHelper().saveData(filePath, manager, paragraph);//段在此之前应该就已经以json格式存储
						if (succeed) {
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									MessageBox box = new MessageBox(MainWindow.shell);
									box.setMessage("段[" + paragraph.getName() + "]数据保存成功！");
									box.open();
								}
							});
						} else {
							Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run() {
									MessageBox box = new MessageBox(MainWindow.shell);
									box.setMessage("段[" + paragraph.getName() + "]数据保存失败！");
									box.open();
								}
							});
						}
					}
				}.start();
				close();
			}
		});
		button_ok.setText("确定");

		Button button_cancel = new Button(shell, SWT.NONE);
		button_cancel.setBounds(290, 183, 60, 27);
		button_cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		button_cancel.setText("取消");
	}

	private String getExtensionFilter() {
		String extensionFilter = "*.*";
		String dsType = paragraph.getDataSourceType();
		if (dsType.equals(DriverConstant.DataSourceType_File))
			extensionFilter = "*." + PathHelper.getExtension(paragraph.getDataSourcePath());
		else if (dsType.equals(DriverConstant.DataSourceType_Database))
			extensionFilter = "*.txt";
		else if (dsType.equals(DriverConstant.DataSourceType_WebService))
			extensionFilter = "*.html";
		else if (dsType.equals(DriverConstant.DataSourceType_ParagraphEngine))
			extensionFilter = "*.*";
		return extensionFilter;
	}

	/**
	 * Close the dialog
	 */
	public void close() {
		shell.close();
	}
}
