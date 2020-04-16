package gri.manager.ui.window;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.SWT;

import gri.driver.manager.GriDocManager;
import gri.driver.model.Connection;
import gri.driver.model.GriDoc;
import gri.driver.model.GriElement;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SelectGriDialog extends Dialog {
	protected Object result;
	protected Shell shell;
	private Shell parentShell;
	private GriDocManager manager;
	
	private List<GriDoc> griDocs;
	private List<GriElement> sections;
	private List<GriElement> paragraphs;
	
	private Combo combo_gri_doc;
	private Combo combo_gri_section;
	private Combo combo_gri_para;
	
	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectGriDialog(Shell parent, String host, String user, String password, int style) {
		super(parent, style);
		this.parentShell = parent;
		Connection conn=new Connection("新建连接", "EITP://"+host, user, password);
		manager = new GriDocManager(conn);
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
		shell.setSize(314, 210);
		shell.setText("选择数据：");
		shell.setImage(new Image(getParent().getDisplay(), this.getClass().getResourceAsStream("/icons/add16.png")));

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(38, 27, 72, 17);
		label_1.setText("*文档：");

		combo_gri_doc = new Combo(shell, SWT.NONE);
		griDocs=manager.getAllGriDoc();
		String[] docNames = new String[griDocs.size()];
		for(int i = 0;i<griDocs.size();i++)
			docNames[i]=griDocs.get(i).getName();
		combo_gri_doc.setItems(docNames);
		combo_gri_doc.setBounds(126, 24, 88, 25);				
		combo_gri_doc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GriElement curElement = griDocs.get(combo_gri_doc.getSelectionIndex());				
				sections=manager.getChildren(curElement);
				String[] sectionNames = new String[sections.size()];
				for(int i = 0;i<sections.size();i++)
					sectionNames[i]=sections.get(i).getName();
				combo_gri_section.setItems(sectionNames);
			}
		});
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(38, 60, 72, 17);
		label_2.setText("*节：");
		
		combo_gri_section = new Combo(shell, SWT.NONE);
		combo_gri_section.setBounds(126, 57, 88, 25);				
		combo_gri_section.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GriElement curElement = sections.get(combo_gri_section.getSelectionIndex());				
				paragraphs=manager.getChildren(curElement);
				String[] paragraphNames = new String[paragraphs.size()];
				for(int i = 0;i<paragraphs.size();i++)
					paragraphNames[i]=paragraphs.get(i).getName();
				combo_gri_para.setItems(paragraphNames);
			}
		});
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setBounds(38, 93, 72, 17);
		label_3.setText("*段：");
		
		combo_gri_para = new Combo(shell, SWT.NONE);
		combo_gri_para.setBounds(126, 90, 88, 25);

		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(combo_gri_para.getText().equals("")) {
					MessageBox box = new MessageBox(shell, SWT.ICON_WARNING);
					box.setMessage("必须选择段！");
					box.open();
					return;
				}
					
				ParagraphDialog.griParagraphId=paragraphs.get(combo_gri_para.getSelectionIndex()).getId();	
				String sourceAbs = combo_gri_doc.getText()+"/"+combo_gri_section.getText()+"/"+combo_gri_para.getText();
				ParagraphDialog.text_griDataName.setText(sourceAbs);
				shell.close();
			}
		});
		button.setBounds(72, 141, 59, 27);
		button.setText("确定");

		Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button_1.setBounds(169, 141, 59, 27);
		button_1.setText("取消");
	}

}
