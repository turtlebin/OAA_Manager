package gri.manager.ui.window;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import gri.driver.manager.GriDocManager;
import gri.driver.model.process.Paragraph;
import gri.driver.util.DriverConstant;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

public class TextViewerDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Shell parentShell;

	private TextViewer textViewer;

	private Paragraph paragraph;
	private GriDocManager manager;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public TextViewerDialog(Paragraph paragraph, GriDocManager manager, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.paragraph = paragraph;
		this.manager = manager;
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
		shell = new Shell(getParent(), SWT.SHELL_TRIM | SWT.BORDER);
		shell.setSize(538, 459);
		shell.setText("段数据预览");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		this.textViewer = new TextViewer(shell, shell, SWT.NONE);

		this.loadData();
	}

	private void loadData(  ) {
		textViewer.loadData(paragraph, manager);
		/*this.textViewer.getText_name().setText(paragraph.getName());

		this.textViewer.getText_keywords().setText(paragraph.getKeywords());

		String dataSourceType = paragraph.getDataSourceType();
		String dataSourcePath = paragraph.getDataSourcePath();

		if (dataSourceType.equals(DriverConstant.DataSourceType_Database)) {
			String strs[] = dataSourcePath.split("###");
			String host = strs[1];
			String db_name = strs[3];
			String sql = strs[6];
			dataSourcePath = "主机：" + host + ", 数据库：" + db_name + ", SQL：" + sql;
		} else if (dataSourceType.equals(DriverConstant.DataSourceType_File)) {
			String strs[] = dataSourcePath.split("###");
			dataSourcePath = strs[0];
		}
		this.textViewer.getText_data_source().setText(dataSourcePath);

		String text = manager.previewParagraphData(paragraph.getId());
		this.textViewer.getText_preview_data().setText(text);*/
	}

}
