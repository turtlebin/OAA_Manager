package gri.manager.ui.window;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import gri.driver.manager.ProcessManager;
import gri.driver.model.process.Paragraph;
import gri.driver.model.process.GroupBy;
import gri.driver.model.process.StatsConf;
import gri.driver.model.process.StatsConfDto1;
import gri.driver.model.process.StatsContent;
import gri.manager.util.Constant;
import gri.manager.util.RunTimeData;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class StatsConfManageDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Shell parentShell;
	private ProcessManager manager;

	private Table table;
	public static TableViewer tableViewer;
	private List<StatsConfDto1> statsConfDto1s;
	private Paragraph paragraph;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public StatsConfManageDialog(ProcessManager manager, Paragraph paragraph, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.manager = manager;
		this.paragraph = paragraph;
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
		shell.setSize(353, 347);
		shell.setText("常用统计");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		tableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		this.table = tableViewer.getTable();
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//
			}
		});
		this.table.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				// 右键
				if (e.button == 3) {
					Menu menu = new Menu(table);
					table.setMenu(menu);

					MenuItem item_refresh = new MenuItem(menu, SWT.PUSH);
					item_refresh.setText("刷新");
					item_refresh.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(Event event) {
							statsConfDto1s = manager.listStatsConf(paragraph.getId());
							tableViewer.refresh();
						}
					});

					if (table.getSelection().length == 0)
						return;

					MenuItem item_edit = new MenuItem(menu, SWT.PUSH);
					item_edit.setText("编辑");
					item_edit.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(Event event) {
							edit();
						}
					});

					MenuItem item_delete = new MenuItem(menu, SWT.PUSH);
					item_delete.setText("删除");
					item_delete.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(Event event) {
							delete();
						}
					});
					
					MenuItem item_view = new MenuItem(menu, SWT.PUSH);
					item_view.setText("查看统计");
					item_view.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(Event event) {
							view();
						}
					});
				}
			}
		});
		this.table.setBounds(10, 70, 327, 239);
		this.table.setHeaderVisible(true);
		this.table.setLinesVisible(true);

		ToolBar toolBar = new ToolBar(shell, SWT.FLAT);
		toolBar.setBounds(10, 8, 238, 56);

		ToolItem toolItem_add = new ToolItem(toolBar, SWT.PUSH);
		toolItem_add.setText("添加");
		toolItem_add.setImage(new Image(toolBar.getDisplay(), this.getClass().getResourceAsStream("/icons/add24.png")));
		toolItem_add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				add();
			}
		});
		ToolItem toolItem_edit = new ToolItem(toolBar, SWT.PUSH);
		toolItem_edit.setText("编辑");
		toolItem_edit
				.setImage(new Image(toolBar.getDisplay(), this.getClass().getResourceAsStream("/icons/edit24.png")));
		toolItem_edit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				edit();
			}
		});

		ToolItem toolItem_delete = new ToolItem(toolBar, SWT.PUSH);
		toolItem_delete.setText("删除");
		toolItem_delete
				.setImage(new Image(toolBar.getDisplay(), this.getClass().getResourceAsStream("/icons/delete24.png")));
		toolItem_delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				delete();
			}
		});

		ToolItem toolItem_refresh = new ToolItem(toolBar, SWT.PUSH);
		toolItem_refresh.setText("刷新");
		toolItem_refresh
				.setImage(new Image(toolBar.getDisplay(), this.getClass().getResourceAsStream("/icons/refresh24.png")));
		toolItem_refresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				statsConfDto1s = manager.listStatsConf(paragraph.getId());
				tableViewer.refresh();
			}
		});
		
		ToolItem toolItem_view = new ToolItem(toolBar, SWT.PUSH);
		toolItem_view.setText("查看统计");
		toolItem_view
				.setImage(new Image(toolBar.getDisplay(), this.getClass().getResourceAsStream("/icons/state24.png")));
		toolItem_view.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				view();
			}
		});

		statsConfDto1s = manager.listStatsConf(paragraph.getId());

		String[] heards = new String[] { "统计名", "统计关键字", "统计列"};
		Integer[] widths = new Integer[] { 80, 80, 160};
		for (int i = 0; i < heards.length; i++) {
			TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.LEFT);
			column.getColumn().setText(heards[i]);
			column.getColumn().setWidth(widths[i]);
		}
		tableViewer.setLabelProvider(new StatsConfTableLabelProvider());
		tableViewer.setContentProvider(new StatsConfTableContentProvider());

		tableViewer.setInput("root");

	}

	private void delete() {
		if (table.getSelection().length == 0){
			MessageBox box = new MessageBox(shell, SWT.ICON_WARNING);
			box.setMessage("请选择要操作的记录！");
			box.open();
			return;
		}
		TableItem item1 = (TableItem) table.getSelection()[0];
		StatsConfDto1 statsConfDto1 = (StatsConfDto1) item1.getData();
		MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
		messageBox.setText("提示");
		messageBox.setMessage("确定删除："+statsConfDto1.getStatsConf().getViewName()+"？");
		if (messageBox.open() == SWT.YES) {
			boolean succeed = manager.delStatsConf(statsConfDto1.getProcessId());
			if (succeed) {
				statsConfDto1s = manager.listStatsConf(paragraph.getId());
				tableViewer.refresh();
			}
		}
	}

	private void add() {
		new StatsConfDialog(shell, Constant.WindowType_Add, SWT.CLOSE | SWT.APPLICATION_MODAL, paragraph, null, manager).open();
		statsConfDto1s = manager.listStatsConf(paragraph.getId());
		tableViewer.refresh();
	}

	private void edit() {
		if (table.getSelection().length == 0){
			MessageBox box = new MessageBox(shell, SWT.ICON_WARNING);
			box.setMessage("请选择要操作的记录！");
			box.open();
			return;
		}
		TableItem item1 = (TableItem) table.getSelection()[0];
		StatsConfDto1 statsConfDto1 = (StatsConfDto1) item1.getData();
		new StatsConfDialog(shell, Constant.WindowType_Edit, SWT.CLOSE | SWT.APPLICATION_MODAL, paragraph, statsConfDto1, manager).open();
		statsConfDto1s = manager.listStatsConf(paragraph.getId());
		tableViewer.refresh();
	}
	
	private void view() {
		if (table.getSelection().length == 0){
			MessageBox box = new MessageBox(shell, SWT.ICON_WARNING);
			box.setMessage("请选择要操作的记录！");
			box.open();
			return;
		}
		TableItem item1 = (TableItem) table.getSelection()[0];
		StatsConfDto1 statsConfDto1 = (StatsConfDto1) item1.getData();
		new StatsDialog1(shell, SWT.CLOSE | SWT.APPLICATION_MODAL, statsConfDto1, manager).open();
		statsConfDto1s = manager.listStatsConf(paragraph.getId());
		tableViewer.refresh();
	}
	
	public class StatsConfTableContentProvider implements IStructuredContentProvider {

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public Object[] getElements(Object element) {
			if (element.equals("root"))
				return statsConfDto1s.toArray(new StatsConfDto1[0]);
			else
				return null;
		}
	}
	
	public class StatsConfTableLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object arg0, int arg1) {
			return null;
		}

		@Override
		public String getColumnText(Object obj, int index) {
			
			if (obj instanceof StatsConfDto1) {
				StatsConfDto1 statsConfDto1 = (StatsConfDto1) obj;
				if (index == 0)
					return statsConfDto1.getStatsConf().getViewName();
				else if (index == 1){
					List<GroupBy> groupBys = statsConfDto1.getStatsConf().getGroupBys();
					String groupByAbs = "";
					for(GroupBy groupBy : groupBys) groupByAbs+=groupBy.getFieldName()+",";
					if(!groupByAbs.equals("")) groupByAbs = groupByAbs.substring(0,groupByAbs.length()-1);
					return groupByAbs;
				}				
				else if (index == 2){
					List<StatsContent> statsContents = statsConfDto1.getStatsConf().getStatsContents();
					String statsContentAbs = "";
					for(StatsContent statsContent : statsContents) statsContentAbs+=statsContent.toString1()+",";
					if(!statsContentAbs.equals("")) statsContentAbs = statsContentAbs.substring(0,statsContentAbs.length()-1);
					return statsContentAbs;
				}
			}
			return null;
		}

	}
	
}
