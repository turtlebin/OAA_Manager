package gri.manager.ui.window;

import java.io.FileOutputStream;
import java.io.IOException;

import net.sf.json.JSONArray;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

import gri.driver.manager.GriDocManager;
import gri.driver.model.process.Paragraph;
import gri.driver.util.DriverConstant;
import gri.manager.ui.composite.PagedTableComposite;
import gri.manager.ui.composite.PlayerComposite;
import gri.manager.ui.composite.TableComposite;
import gri.manager.ui.composite.WMP;
import gri.manager.util.PathHelper;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;




public class TextViewer extends Composite {
	private Text text_name;
	//private Text text_preview_data;
	private Button button_upload;
	private Button button_download;
	private Text text_keywords;
	private Text text_data_source;
	private Composite composite_down;
	private Shell parentWindow;

	// @Override
	// public void setLayoutData(Object layoutData) {
	// this.setLayoutData(layoutData);
	//
	// }

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public TextViewer(Composite parent,Shell parentWindow, int style) {
		super(parent, SWT.BORDER);
		this.parentWindow = parentWindow;
		setLayout(new GridLayout(5, true));
		Composite composite_up = new Composite(this, SWT.NONE);
		composite_up.setLayout(null);
		GridData gd_composite_up = new GridData(SWT.LEFT, SWT.CENTER, true, false, 5, 1);
		gd_composite_up.heightHint = 155;
		composite_up.setLayoutData(gd_composite_up);

		Label label = new Label(composite_up, SWT.NONE);
		label.setBounds(0, 0, 48, 17);
		label.setText("段名称：");

		Label label_1 = new Label(composite_up, SWT.NONE);
		label_1.setText("关键词：");
		label_1.setBounds(0, 29, 48, 17);

		Label label_2 = new Label(composite_up, SWT.NONE);
		label_2.setText("数据源：");
		label_2.setBounds(0, 58, 48, 17);

		text_name = new Text(composite_up, SWT.READ_ONLY);
		text_name.setBounds(54, 0, 371, 23);

		text_keywords = new Text(composite_up, SWT.READ_ONLY);
		text_keywords.setBounds(54, 29, 371, 23);

		text_data_source = new Text(composite_up, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
		text_data_source.setBounds(54, 58, 371, 43);

		button_upload = new Button(composite_up, SWT.NONE);
		button_upload.setBounds(125, 107, 61, 23);
		button_upload.setText("上传");

		button_download = new Button(composite_up, SWT.NONE);
		button_download.setBounds(54, 107, 61, 23);
		button_download.setText("下载");

		Label label_3 = new Label(composite_up, SWT.NONE);
		label_3.setText("数据预览：");
		label_3.setBounds(0, 135, 61, 17);

		composite_down = new Composite(this, SWT.NONE);
		composite_down.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_composite_down = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1);
		gd_composite_down.heightHint = 320;
		composite_down.setLayoutData(gd_composite_down);

		//text_preview_data = new Text(composite_down, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);

		/*text_preview_data.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				// 右键
				if (e.button == 3) {
					Menu menu = new Menu(text_preview_data);
					text_preview_data.setMenu(menu);

					MenuItem item_clear = new MenuItem(menu, SWT.PUSH);
					item_clear.setText("清空");
					item_clear.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(Event event) {
							MainWindow.clearContentComposite();
							MainWindow.reLayoutContentComposite();
						}
					});
				}
			}
		});*/

	}

	public void setHeight(int h) {

	}

	public Text getText_name() {
		return text_name;
	}

	/*public Text getText_preview_data() {
		return text_preview_data;
	}*/

	public Button getButton_upload() {
		return button_upload;
	}

	public Button getButton_download() {
		return button_download;
	}

	public Text getText_keywords() {
		return text_keywords;
	}

	public Text getText_data_source() {
		return text_data_source;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void loadData(Paragraph paragraph, GriDocManager manager) {
		text_name.setText(paragraph.getName());
		text_keywords.setText(paragraph.getKeywords());
		String dataSourceType = paragraph.getDataSourceType();
		String dataSourcePath = paragraph.getDataSourcePath();
		String dataSourceText = dataSourcePath;
		if (dataSourceType.equals(DriverConstant.DataSourceType_Database)) {
			String strs[] = dataSourcePath.split("###");
			String host = strs[1];
			String db_name = strs[3];
			String sql = strs[6];//把dataSourcePath分成字符串数组，每一个元素都有特定的含义
			dataSourceText = "主机：" + host + ", 数据库：" + db_name + ", SQL：" + sql;
		} else if (dataSourceType.equals(DriverConstant.DataSourceType_File)) {
			String strs[] = dataSourcePath.split("###");
			dataSourceText = strs[0];
		} else if (dataSourceType.equals(DriverConstant.DataSourceType_ParagraphEngine)) {
			String strs[] = dataSourcePath.split("###");
			String host = strs[0];
			// String user = strs[1];
			// String password = strs[2];
			String fileName = strs[3];
			String fileId = strs[4];
			dataSourceText = "段引擎：" + host + ", 数据段名：" + fileName + ", 数据段ID：" + fileId;
		}else if (dataSourceType.equals(DriverConstant.DataSourceType_GriEngine)) {
			String strs[] = dataSourcePath.split("###");
			String host = strs[0];
			// String user = strs[1];
			// String password = strs[2];
			//String fileId = strs[3];
			String fileName = strs[4];
			dataSourceText = "数格引擎：" + host + ", 数据来源：" + fileName ;
		}
		text_data_source.setText(dataSourceText);

		
		if(dataSourceType.equals(DriverConstant.DataSourceType_GriEngine)){
			gri.driver.model.process.Paragraph realParagraph= manager.getRealParagraph(dataSourcePath);
			dataSourceType = realParagraph.getDataSourceType();//如果数据源来自于格引擎，则先获取真实的段，然后再从真实的段获取数据源种类和路径
			dataSourcePath = realParagraph.getDataSourcePath();
		}
			
		if (dataSourceType.equals(DriverConstant.DataSourceType_Database)) {
			//String text = manager.previewParagraphData(paragraph.getId());
			/*String text = new String(manager.readData1(paragraph));
			if(text.equals("")){
				MessageBox box = new MessageBox(parentWindow, SWT.ICON_WARNING);
				box.setMessage("预览前请先同步数据");
				box.open();
			}				
			else{
				JSONArray json= JSONArray.fromObject(text);
				TableComposite table = new TableComposite(composite_down,SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI, json);
				MainWindow.reLayoutContentComposite();	
			}	*/
			new PagedTableComposite(composite_down, parentWindow, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI, paragraph.getId(), manager);
			MainWindow.reLayoutContentComposite();
		} 
		else if (dataSourceType.equals(DriverConstant.DataSourceType_WebService)) {
			String text = new String(manager.readData1(paragraph));
			Text text_preview_data = new Text(composite_down, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
			MainWindow.reLayoutContentComposite();			
			text_preview_data.setText(text);
		}else if (dataSourceType.equals(DriverConstant.DataSourceType_File)){
			String extention = PathHelper.getExtension(dataSourcePath);
			if(extention.equals("txt") || extention.equals("doc") || extention.equals("ppt")){
				String text = manager.previewParagraphData(paragraph.getId());
				if(text.equals("缓存不存在")){
					System.out.println("enter it");
					MainWindow.reLayoutContentComposite();
					MessageBox box = new MessageBox(parentWindow, SWT.ICON_WARNING);
					box.setMessage("预览前请先同步数据");
					box.open();
				}
				else{
					Text text_preview_data = new Text(composite_down, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);							
					text_preview_data.setText(text);
					text_preview_data.setSize(470, 300);
					MainWindow.reLayoutContentComposite();
				}				
			}
			else if(extention.equals("wmv") || extention.equals("mp3") || extention.equals("mp4") || extention.equals("avi") || extention.equals("flv")){
			    new PlayerDialog(parentWindow, SWT.CLOSE | SWT.APPLICATION_MODAL, paragraph,manager).open();
		        
			}
			else{
				MainWindow.reLayoutContentComposite();
				MessageBox box = new MessageBox(parentWindow, SWT.ICON_WARNING);
				box.setMessage("不支持该格式数据的预览");
				box.open();
			}
		}
		else if (dataSourceType.equals(DriverConstant.DataSourceType_View)){
			//gj:视图数据源前面有些不加引号的文字，无法处理。
			String text = new String(manager.readData1(paragraph));			
			System.out.println("****"+text);
			String text1="["+(text.substring(text.indexOf("{"),text.length()));//设置text以[开头，并且从text的{开始
			JSONArray json= JSONArray.fromObject(text1);//创建json数组
			TableComposite table = new TableComposite(composite_down,SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI, json);//由json数据创建Table
			MainWindow.reLayoutContentComposite();	
		}
	
	}
}
