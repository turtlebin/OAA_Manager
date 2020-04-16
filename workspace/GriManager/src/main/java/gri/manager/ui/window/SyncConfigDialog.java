package gri.manager.ui.window;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import gri.driver.model.SyncConfigHolder;
import gri.driver.model.process.Paragraph;
import gri.driver.util.DriverConstant;
import gri.engine.util.DBHelper;
import gri.manager.model.GlobalViewTreeNode;
import gri.manager.ui.table.ConfigTableContentProvider;
import gri.manager.ui.table.ConfigTableLabelProvider;

public class SyncConfigDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Shell parentShell;
	private Composite composite_syncTimeConfig;
	private Composite composite_period;
	private Composite composite_day;
	private Composite composite_month;
	private Composite composite_week;
	private Combo combo_syncDirection;
	private Combo combo_syncTimeType;

	private Combo combo_period;

	private Button button_period;
	private Button button_day;
	private Button button_month;
	private Button button_week;

	private Button button_method_all;
	private Button button_method_increment;

	private Paragraph paragraph;
	private Paragraph tempParagraph;
	private Text text_period;

	private int index = -1;
	private TableViewer tableViewer;
	private Table table_config;

	private boolean update = false;
	private boolean allTables = false;

	private String oldSyncDirection;
	private String oldSyncType;
	private String oldSyncWarmInfo;

	private GlobalViewTreeNode node;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public SyncConfigDialog(Paragraph paragraph, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.paragraph = paragraph;
		this.tempParagraph = paragraph;
	}

	public SyncConfigDialog(Paragraph paragraph, GlobalViewTreeNode node, Shell parent, int style, boolean update) {// 修改单个段同步配置时使用
		super(parent, style);
		this.parentShell = parent;
		this.paragraph = paragraph;
		this.tempParagraph = cloneParagraph(paragraph);
		this.update = update;
		this.oldSyncDirection = this.paragraph.getSyncDirectionType() == null ? ""
				: this.paragraph.getSyncDirectionType();
		this.oldSyncType = this.paragraph.getSyncTimeType() == null ? "" : this.paragraph.getSyncTimeType();
		this.oldSyncWarmInfo = this.paragraph.getWarmSyncDetail() == null ? "" : this.paragraph.getWarmSyncDetail();
		this.node = node;
	}

	public SyncConfigDialog(Paragraph paragraph, GlobalViewTreeNode node, Shell parent, int style, boolean update,
			boolean allTables) {// 修改全部段同步配置时使用
		super(parent, style);
		this.parentShell = parent;
		this.paragraph = paragraph;
		this.tempParagraph = cloneParagraph(paragraph);
		this.update = update;
		this.allTables = allTables;
		this.oldSyncDirection = this.paragraph.getSyncDirectionType() == null ? ""
				: this.paragraph.getSyncDirectionType();
		this.oldSyncType = this.paragraph.getSyncTimeType() == null ? "" : this.paragraph.getSyncTimeType();
		this.oldSyncWarmInfo = this.paragraph.getWarmSyncDetail() == null ? "" : this.paragraph.getWarmSyncDetail();
		this.node = node;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		if (parentShell != null)
			this.initWindow();
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

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getParent().getStyle());
		shell.setSize(467, 501);
		shell.setText("同步配置");

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 441, 447);

		Label label = new Label(composite, SWT.NONE);
		label.setText("同步方向：");
		label.setBounds(10, 7, 61, 17);

		combo_syncDirection = new Combo(composite, SWT.READ_ONLY);
		combo_syncDirection.setItems(new String[] { "正向（数据源 -> 数格引擎）" });
		combo_syncDirection.setBounds(140, 7, 206, 25);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText("同步时机：");
		label_1.setBounds(10, 50, 61, 17);

		combo_syncTimeType = new Combo(composite, SWT.READ_ONLY);
		combo_syncTimeType.setItems(new String[] { "温同步（定时/周期）", "冷同步（手动）" });
		combo_syncTimeType.setBounds(140, 47, 206, 25);

		combo_syncTimeType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (combo_syncTimeType.getSelectionIndex() == 0) {
					composite_syncTimeConfig.setVisible(true);
				} else
					composite_syncTimeConfig.setVisible(false);
			}
		});

		Label label_method = new Label(composite, SWT.NONE);
		label_method.setText("同步方法 ：");
		label_method.setBounds(10, 93, 61, 17);

		button_method_all = new Button(composite, SWT.RADIO);
		button_method_all.setBounds(140, 91, 86, 17);
		button_method_all.setText("全量同步");

		button_method_increment = new Button(composite, SWT.RADIO);
		button_method_increment.setBounds(280, 91, 86, 17);
		button_method_increment.setText("增量同步");

		composite_syncTimeConfig = new Composite(composite, SWT.NONE);
		composite_syncTimeConfig.setBounds(10, 115, 421, 283);

		tableViewer = new TableViewer(composite_syncTimeConfig, SWT.BORDER | SWT.FULL_SELECTION);
		table_config = tableViewer.getTable();
		table_config.setLinesVisible(true);
		table_config.setHeaderVisible(false);
		table_config.setBounds(133, 14, 218, 83);

		table_config.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//
			}
		});
		table_config.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				// 右键
				if (e.button == 3) {
					Menu menu = new Menu(table_config);
					table_config.setMenu(menu);

					MenuItem item_refresh = new MenuItem(menu, SWT.PUSH);
					item_refresh.setText("刷新");
					item_refresh.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(Event event) {
							tableViewer.refresh();
						}
					});

					if (table_config.getSelection().length == 0)
						return;

					MenuItem item_delete = new MenuItem(menu, SWT.PUSH);
					item_delete.setText("删除");
					item_delete.addListener(SWT.Selection, new Listener() {
						@Override
						public void handleEvent(Event event) {
							delete();
						}
					});
				}
			}
		});

		String[] heards = new String[] { "时间详情" };
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.LEFT);
		column.getColumn().setText(heards[0]);
		column.getColumn().setWidth(180);

		tableViewer.setLabelProvider(new ConfigTableLabelProvider());
		tableViewer.setContentProvider(new ConfigTableContentProvider(this.tempParagraph));

		tableViewer.setInput("root");

		Label label_7 = new Label(composite_syncTimeConfig, SWT.NONE);
		label_7.setBounds(24, 14, 88, 17);
		label_7.setText("同步时间配置：");

		button_period = new Button(composite_syncTimeConfig, SWT.RADIO);
		button_period.setBounds(34, 124, 46, 17);
		button_period.setText("间隔");

		button_period.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (button_period.getSelection()) {
					composite_period.setVisible(true);
					index = 0;
				} else {
					composite_period.setVisible(false);
				}
			}
		});

		button_day = new Button(composite_syncTimeConfig, SWT.RADIO);
		button_day.setText("日历");
		button_day.setBounds(34, 168, 46, 17);

		button_day.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (button_day.getSelection()) {
					composite_day.setVisible(true);
					index = 1;
				} else {
					composite_day.setVisible(false);
				}
			}
		});

		button_month = new Button(composite_syncTimeConfig, SWT.RADIO);
		button_month.setText("月历");
		button_month.setBounds(34, 208, 46, 17);

		button_month.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (button_month.getSelection()) {
					composite_month.setVisible(true);
					index = 2;
				} else {
					composite_month.setVisible(false);
				}
			}
		});

		button_week = new Button(composite_syncTimeConfig, SWT.RADIO);
		button_week.setText("周历");
		button_week.setBounds(34, 247, 46, 17);

		button_week.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (button_week.getSelection()) {
					composite_week.setVisible(true);
					index = 3;
				} else {
					composite_week.setVisible(false);
				}
			}
		});

		composite_period = new Composite(composite_syncTimeConfig, SWT.NONE);
		composite_period.setBounds(98, 119, 253, 36);

		Button button_add_period = new Button(composite_period, SWT.NONE);
		button_add_period.setBounds(199, 0, 46, 27);
		button_add_period.addSelectionListener(new SelectionAdapter() {// 添加按钮才会触发paragraph的修改
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					int period = Integer.valueOf(text_period.getText());
					int x = combo_period.getSelectionIndex();
					String CronExpression = "";
					if (0 == x)
						CronExpression = "0/" + period + " * * * * ?";
					else if (1 == x)
						CronExpression = "0 0/" + period + " * * * ?";
					else if (2 == x)
						CronExpression = "0 0 0/" + period + " * * ?";
					if (tempParagraph.getWarmSyncDetail().equals("")) {
						tempParagraph.setWarmSyncDetail(index + "#" + CronExpression);
						System.out.println(CronExpression);
					} else {
						int index1 = Integer.valueOf((tempParagraph.getWarmSyncDetail().split("#")[0]));
						if (index1 != index) {
							showMessage("只能填写同一时间配置！", shell);
							return;
						}
						tempParagraph.setWarmSyncDetail(tempParagraph.getWarmSyncDetail() + "@" + CronExpression);
						System.out.println(CronExpression);
					}
					System.out.println("所有：" + tempParagraph.getWarmSyncDetail());
					tableViewer.refresh();
				} catch (Exception ex) {
					return;
				}
			}
		});
		button_add_period.setText("添加");

		combo_period = new Combo(composite_period, SWT.READ_ONLY);
		combo_period.setBounds(120, 2, 61, 25);
		combo_period.setItems(new String[] { "秒", "分钟", "小时" });
		combo_period.select(0);

		text_period = new Text(composite_period, SWT.BORDER);
		text_period.setBounds(40, 2, 73, 23);

		Label label_9 = new Label(composite_period, SWT.NONE);
		label_9.setBounds(10, 5, 24, 17);
		label_9.setText("每隔");

		composite_day = new Composite(composite_syncTimeConfig, SWT.NONE);
		composite_day.setBounds(98, 160, 253, 36);

		Label label_10 = new Label(composite_day, SWT.NONE);
		label_10.setBounds(10, 5, 24, 17);
		label_10.setText("每天");

		final DateTime dateTime_day = new DateTime(composite_day, SWT.BORDER | SWT.TIME | SWT.LONG);
		dateTime_day.setBounds(40, 2, 88, 24);

		Button button_add_day = new Button(composite_day, SWT.NONE);
		button_add_day.setBounds(199, 2, 46, 27);
		button_add_day.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					int hour = dateTime_day.getHours();
					String hour2 = String.valueOf(hour);
					if (hour < 10)
						hour2 = "0" + hour;

					int minute = dateTime_day.getMinutes();
					String minute2 = String.valueOf(minute);
					if (minute < 10)
						minute2 = "0" + minute;

					int second = dateTime_day.getSeconds();
					String second2 = String.valueOf(second);
					if (second < 10)
						second2 = "0" + second;

					String CronExpression = second2 + " " + minute2 + " " + hour2 + " * * ?";
					if (tempParagraph.getWarmSyncDetail().equals("")) {
						tempParagraph.setWarmSyncDetail(index + "#" + CronExpression);
						System.out.println(CronExpression);
					} else {
						int index1 = Integer.valueOf((tempParagraph.getWarmSyncDetail().split("#")[0]));
						if (index1 != index) {
							showMessage("只能填写同一时间配置！", shell);
							return;
						}
						tempParagraph.setWarmSyncDetail(tempParagraph.getWarmSyncDetail() + "@" + CronExpression);
						System.out.println(CronExpression);
					}
					System.out.println("所有：" + tempParagraph.getWarmSyncDetail());
					tableViewer.refresh();
				} catch (Exception ex) {
					return;
				}
			}
		});
		button_add_day.setText("添加");

		composite_month = new Composite(composite_syncTimeConfig, SWT.NONE);
		composite_month.setBounds(98, 202, 253, 36);

		Label label_11 = new Label(composite_month, SWT.NONE);
		label_11.setBounds(10, 5, 24, 17);
		label_11.setText("每月");

		final Combo combo_month = new Combo(composite_month, SWT.READ_ONLY);
		combo_month.setBounds(40, 2, 34, 25);
		combo_month.setItems(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
				"15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" });
		combo_month.select(0);

		Label label_6 = new Label(composite_month, SWT.NONE);
		label_6.setBounds(82, 5, 17, 17);
		label_6.setText("号");

		final DateTime dateTime_month = new DateTime(composite_month, SWT.BORDER | SWT.TIME | SWT.LONG);
		dateTime_month.setBounds(105, 2, 88, 24);

		Button button_add_month = new Button(composite_month, SWT.NONE);
		button_add_month.setBounds(199, 0, 46, 27);
		button_add_month.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					int day = combo_month.getSelectionIndex() + 1;
					int hour = dateTime_month.getHours();
					int minute = dateTime_month.getMinutes();
					int second = dateTime_month.getSeconds();

					String hour2 = String.valueOf(hour);
					if (hour < 10)
						hour2 = "0" + hour;

					String minute2 = String.valueOf(minute);
					if (minute < 10)
						minute2 = "0" + minute;

					String second2 = String.valueOf(second);
					if (second < 10)
						second2 = "0" + second;

					String CronExpression = second2 + " " + minute2 + " " + hour2 + " " + day + " * ?";
					if (tempParagraph.getWarmSyncDetail().equals("")) {
						tempParagraph.setWarmSyncDetail(index + "#" + CronExpression);
						System.out.println(CronExpression);
					} else {
						int index1 = Integer.valueOf((tempParagraph.getWarmSyncDetail().split("#")[0]));
						if (index1 != index) {
							showMessage("只能填写同一时间配置！", shell);
							return;
						}
						tempParagraph.setWarmSyncDetail(tempParagraph.getWarmSyncDetail() + "@" + CronExpression);
						System.out.println(CronExpression);
					}
					System.out.println("所有：" + tempParagraph.getWarmSyncDetail());
					tableViewer.refresh();
				} catch (Exception ex) {
					return;
				}
			}
		});
		button_add_month.setText("添加");

		composite_week = new Composite(composite_syncTimeConfig, SWT.NONE);
		composite_week.setBounds(98, 244, 253, 36);

		Label label_12 = new Label(composite_week, SWT.NONE);
		label_12.setBounds(10, 5, 24, 17);
		label_12.setText("每周");

		final Combo combo_week = new Combo(composite_week, SWT.READ_ONLY);
		combo_week.setBounds(42, 2, 61, 25);
		combo_week.setItems(new String[] { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" });
		combo_week.select(0);

		final DateTime dateTime_week = new DateTime(composite_week, SWT.BORDER | SWT.TIME | SWT.LONG);
		dateTime_week.setBounds(107, 2, 88, 24);

		Button button_add_week = new Button(composite_week, SWT.NONE);
		button_add_week.setBounds(201, 0, 46, 27);
		button_add_week.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					int week = combo_week.getSelectionIndex() + 1;
					int hour = dateTime_week.getHours();
					int minute = dateTime_week.getMinutes();
					int second = dateTime_week.getSeconds();

					String hour2 = String.valueOf(hour);
					if (hour < 10)
						hour2 = "0" + hour;

					String minute2 = String.valueOf(minute);
					if (minute < 10)
						minute2 = "0" + minute;

					String second2 = String.valueOf(second);
					if (second < 10)
						second2 = "0" + second;

					String CronExpression = second2 + " " + minute2 + " " + hour2 + " ? * " + week;
					if (tempParagraph.getWarmSyncDetail().equals("")) {
						tempParagraph.setWarmSyncDetail(index + "#" + CronExpression);
						System.out.println(CronExpression);
					} else {
						int index1 = Integer.valueOf((tempParagraph.getWarmSyncDetail().split("#")[0]));
						if (index1 != index) {
							showMessage("只能填写同一时间配置！", shell);
							return;
						}
						tempParagraph.setWarmSyncDetail(tempParagraph.getWarmSyncDetail() + "@" + CronExpression);
						System.out.println(CronExpression);
					}
					System.out.println("所有：" + tempParagraph.getWarmSyncDetail());
					tableViewer.refresh();
				} catch (Exception ex) {
					return;
				}
			}
		});
		button_add_week.setText("添加");

		Label label_2 = new Label(composite_syncTimeConfig, SWT.NONE);
		label_2.setBounds(357, 48, 51, 17);
		label_2.setText("提示：");

		Label label_3 = new Label(composite_syncTimeConfig, SWT.NONE);
		label_3.setText("右键可删除");
		label_3.setBounds(357, 64, 64, 17);

		Button button_ok = new Button(composite, SWT.NONE);
		button_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 同步方向
				switch (combo_syncDirection.getSelectionIndex()) {
				case 0:
					tempParagraph.setSyncDirectionType(DriverConstant.SyncDirectionType_0);
					break;
				case 1:
					tempParagraph.setSyncDirectionType(DriverConstant.SyncDirectionType_1);
					break;
				case 2:
					tempParagraph.setSyncDirectionType(DriverConstant.SyncDirectionType_2);
					break;
				default:
					showMessage("请填写同步方向！", shell);
					return;
				}

				// 同步时机
				switch (combo_syncTimeType.getSelectionIndex()) {
				// case 0:
				// if(!update) {
				// paragraph.setSyncTimeType(DriverConstant.SyncTimeType_0);
				// }else {
				// paragraph.setSyncTimeType(DriverConstant.SyncTimeType_0);
				// tempParagraph.setSyncTimeType(DriverConstant.SyncTimeType_0);
				// }
				// break;
				case 0:
					if (tempParagraph.getWarmSyncDetail() == null || tempParagraph.getWarmSyncDetail().equals("")) {
						showMessage("请填写完整温同步时间详情！", shell);
						return;
					}
					tempParagraph.setSyncTimeType(DriverConstant.SyncTimeType_1);
					break;
				case 1:
					tempParagraph.setSyncTimeType(DriverConstant.SyncTimeType_2);
					break;
				default:
					showMessage("请填写同步时机！", shell);
					return;
				}
				if (allTables) {
					updateAllParagraphSync(tempParagraph);
					updateSyncTaskManagerWithoutCheck(tempParagraph);
				} else if (update) {
					updateParagraphSync(tempParagraph);
					updateSyncTaskManager(tempParagraph);
				}
				paragraph.setSyncDirectionType(tempParagraph.getSyncDirectionType());
				paragraph.setSyncTimeType(tempParagraph.getSyncTimeType());
				paragraph.setWarmSyncDetail(tempParagraph.getWarmSyncDetail());
				shell.close();
			}
		});
		button_ok.setText("确定");
		button_ok.setBounds(351, 410, 80, 27);
	}

	private void initWindow() {
		composite_syncTimeConfig.setVisible(false);
		composite_period.setVisible(false);
		composite_day.setVisible(false);
		composite_month.setVisible(false);
		composite_week.setVisible(false);
	}

	private void loadData() {
		if (!paragraph.getSyncDirectionType().equals("")) {
			// 同步方向
			if (paragraph.getSyncDirectionType().equals(DriverConstant.SyncDirectionType_0))
				combo_syncDirection.select(0);
			// else if
			// (paragraph.getSyncDirectionType().equals(DriverConstant.SyncDirectionType_1))
			// combo_syncDirection.select(1);
			// else if
			// (paragraph.getSyncDirectionType().equals(DriverConstant.SyncDirectionType_2))
			// combo_syncDirection.select(2);
		}
		if (!paragraph.getSyncTimeType().equals("")) {
			// 同步时机
			// if (paragraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_0)) {
			// combo_syncTimeType.select(0);
			// } else
			if (paragraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_1)) {
				// 温同步
				composite_syncTimeConfig.setVisible(true);
				combo_syncTimeType.select(0);
				tableViewer.refresh();
			} else if (paragraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_2)) {
				combo_syncTimeType.select(1);
			}
		}
	}

	private void showMessage(String message, Shell shell) {
		MessageBox box = new MessageBox(shell);
		box.setMessage(message);
		box.open();
	}

	private void delete() {
		if (table_config.getSelection().length == 0)
			return;
		TableItem item1 = (TableItem) table_config.getSelection()[0];
		String str = (String) item1.getData();
		if (tempParagraph.getWarmSyncDetail().equals(""))
			return;
		String strs0 = tempParagraph.getWarmSyncDetail().split("#")[0];
		String[] strs = tempParagraph.getWarmSyncDetail().split("#")[1].split("@");

		StringBuilder sb = new StringBuilder();
		for (String s : strs) {
			if (!s.equals(str)) {
				if (sb.toString().equals(""))
					sb.append(s);
				else
					sb.append("@" + s);
			}
		}

		if (sb.toString().equals("")) {
			tempParagraph.setWarmSyncDetail("");

		} else {
			tempParagraph.setWarmSyncDetail(strs0 + "#" + sb.toString());
		}
		tableViewer.refresh();
	}

	private void updateParagraphSync(Paragraph paragraph) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement ps = null;
		String sql = "update paragraph set sync_time_type=?,sync_direction_type=?,warm_sync_detail=? where id=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, paragraph.getSyncTimeType() == null ? "" : paragraph.getSyncTimeType());
			ps.setString(2, paragraph.getSyncDirectionType() == null ? "" : paragraph.getSyncDirectionType());
			ps.setString(3, paragraph.getWarmSyncDetail() == null ? "" : paragraph.getWarmSyncDetail());
			ps.setInt(4, paragraph.getId());
			ps.execute();
			System.out.println("success");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void updateAllParagraphSync(Paragraph paragraph) {
		Connection conn = DBHelper.getConnection();
		PreparedStatement ps = null;
		String sql = "update paragraph set sync_time_type=?,sync_direction_type=?,warm_sync_detail=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, paragraph.getSyncTimeType() == null ? "" : paragraph.getSyncTimeType());
			ps.setString(2, paragraph.getSyncDirectionType() == null ? "" : paragraph.getSyncDirectionType());
			ps.setString(3, paragraph.getWarmSyncDetail() == null ? "" : paragraph.getWarmSyncDetail());
			ps.execute();
			System.out.print("success");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateSyncTaskManager(Paragraph tempParagraph) {
		if ((tempParagraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_1)
				&& oldSyncType.equals(DriverConstant.SyncTimeType_1)
				&& !tempParagraph.getWarmSyncDetail().equals(oldSyncWarmInfo))
				|| (oldSyncType.equals(DriverConstant.SyncTimeType_2)
						&& tempParagraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_1))
				|| (oldSyncType.equals(DriverConstant.SyncTimeType_1)
						&& !tempParagraph.getSyncTimeType().equals(DriverConstant.SyncTimeType_1))) {
			SyncConfigHolder syncHolder = new SyncConfigHolder(tempParagraph, oldSyncType, oldSyncWarmInfo);
			Object result = node.manager.updateSyncTaskManager(syncHolder);
			System.out.println((boolean) result);
		}
	}

	private void updateSyncTaskManagerWithoutCheck(Paragraph tempParagraph) {
		SyncConfigHolder syncHolder = new SyncConfigHolder(tempParagraph, oldSyncType, oldSyncWarmInfo);
		Object result = node.manager.updateSyncTaskManager(syncHolder);
		System.out.println((boolean) result);
	}
	
	private Paragraph cloneParagraph(Paragraph paragraph) {
		Paragraph temp=new Paragraph();
		temp.setSyncDirectionType(paragraph.getSyncDirectionType());
		temp.setSyncTimeType(paragraph.getSyncTimeType());
		temp.setWarmSyncDetail(paragraph.getWarmSyncDetail());
		temp.setId(paragraph.getId());
		return temp;
	}
}
