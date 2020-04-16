package gri.manager.ui.window;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import gri.driver.model.GriElement;
import gri.driver.model.Section;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.util.Constant;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;

public class SectionDialog extends Dialog {
	private int windowType;
	private Object result;
	private Shell shell;
	private Shell parentShell;
	private Text text_name;
	private GlobalViewTreeNode node;
	private Text text_updateTime;
	private Label label_updateTime;

	private Button button_ok;
	private Button button_cancel;

	public SectionDialog(int windowType, GlobalViewTreeNode node, Shell parent, int style) {//新建节
		super(parent, style);
		this.parentShell = parent;
		this.node = node;
		this.windowType = windowType;
	}

	public Object open() {
		createContents();
		if (parentShell != null)
			this.initWindow();
		if (this.windowType != Constant.WindowType_Add)
			this.loadData();
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
		shell.setSize(292, 263);
		shell.setText("新建节");
		shell.setImage(
				new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/section16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);
		
				TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
				tabFolder.setBounds(10, 10, 266, 182);
				
						TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
						tabItem.setText("常规");
						
								Composite composite = new Composite(tabFolder, SWT.NONE);
								tabItem.setControl(composite);
								
										Label lblNewLabel_1 = new Label(composite, SWT.NONE);
										lblNewLabel_1.setBounds(25, 32, 36, 17);
										lblNewLabel_1.setText("名称：");
										
												text_name = new Text(composite, SWT.BORDER);
												text_name.setBounds(67, 29, 181, 23);
														
																label_updateTime = new Label(composite, SWT.NONE);
																label_updateTime.setText("修改时间：");
																label_updateTime.setBounds(0, 72, 61, 17);
												
														text_updateTime = new Text(composite, SWT.READ_ONLY);
														text_updateTime.setText("?");
														text_updateTime.setBounds(67, 72, 181, 23);

		button_ok = new Button(shell, SWT.NONE);
		button_ok.setBounds(150, 198, 60, 27);
		button_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = text_name.getText().trim();
				if (name.equals("")) {
					MessageBox box = new MessageBox(shell);
					box.setMessage("节名称不能为空！");
					box.open();
					return;
				}
				if (windowType == Constant.WindowType_Add) {
					Section newSection = (Section) node.manager.addGriElement(node.root, (GriElement) node.data,
							new Section(name));
					if (newSection == null) {
						MessageBox box2 = new MessageBox(shell);
						box2.setMessage("节创建失败！");
						box2.open();
						return;
					}
					close();
					MainWindow.treeViewer_globalView.add(node,
							new GlobalViewTreeNode(newSection, node.root, node.manager));
				} else if (windowType == Constant.WindowType_Edit) {
					Section sec = (Section) node.data;
					sec.setName(name);
					if (node.manager.updateGriElement(node.root, sec)) {
						close();
						MainWindow.treeViewer_globalView.refresh(node);
					} else {
						MessageBox box2 = new MessageBox(shell);
						box2.setMessage("节修改失败！");
						box2.open();
					}
				}
			}
		});
		button_ok.setText("确定");

		button_cancel = new Button(shell, SWT.NONE);
		button_cancel.setBounds(216, 198, 60, 27);
		button_cancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		button_cancel.setText("取消");

	}

	private void loadData() {//如果windowType != Constant.WindowType_Add，则调用这个方法，否则调用上述的方法
		shell.setText("节属性");
		Section sec = (Section) node.data;
		text_name.setText(sec.getName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// 修改时间
		text_updateTime.setText(sdf.format(sec.getUpdateTime()));
		label_updateTime.setVisible(true);
		text_updateTime.setVisible(true);

		if (windowType == Constant.WindowType_Property) {
			button_ok.setVisible(false);
			button_cancel.setText("关闭");
		}
	}

	private void initWindow() {
		label_updateTime.setVisible(false);
		text_updateTime.setVisible(false);
	}

	public void close() {
		shell.close();
	}
}
