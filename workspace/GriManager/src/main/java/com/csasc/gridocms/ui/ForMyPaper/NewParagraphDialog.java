package com.csasc.gridocms.ui.ForMyPaper;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Combo;

public class NewParagraphDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Text text_6;
	private Text text_7;
	private Text text_8;
	private Text text_9;
	private Text text_10;
	private Text text_11;
	private Text text_12;
	private Text text_13;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewParagraphDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
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
		shell.setSize(596, 613);
		shell.setText("新建段");
		
		Group group = new Group(shell, SWT.NONE);
		group.setBounds(5, 6, 579, 535);
		
		Group group_1 = new Group(group, SWT.NONE);
		group_1.setText("基本信息");
		group_1.setBounds(10, 10, 555, 131);
		
		Label label = new Label(group_1, SWT.NONE);
		label.setText("名称：");
		label.setBounds(10, 27, 36, 17);
		
		text = new Text(group_1, SWT.BORDER);
		text.setBounds(52, 24, 120, 23);
		
		Label label_1 = new Label(group_1, SWT.NONE);
		label_1.setText("关键词：");
		label_1.setBounds(178, 27, 48, 17);
		
		text_1 = new Text(group_1, SWT.BORDER);
		text_1.setBounds(232, 24, 134, 23);
		
		Label label_2 = new Label(group_1, SWT.NONE);
		label_2.setText("注：多个关键词用空格/逗号分隔");
		label_2.setBounds(372, 27, 180, 17);
		
		Label label_3 = new Label(group_1, SWT.NONE);
		label_3.setText("简介：");
		label_3.setBounds(10, 64, 36, 17);
		
		text_2 = new Text(group_1, SWT.BORDER | SWT.V_SCROLL);
		text_2.setBounds(52, 64, 492, 58);
		
		Group group_2 = new Group(group, SWT.NONE);
		group_2.setText("数据信息");
		group_2.setBounds(10, 147, 555, 377);
		
		TabFolder tabFolder = new TabFolder(group_2, SWT.NONE);
		tabFolder.setBounds(10, 29, 535, 213);
		
		TabItem tabItem = new TabItem(tabFolder, 0);
		tabItem.setText("文件");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_2);
		
		Label label_6 = new Label(composite_2, SWT.NONE);
		label_6.setText("文件路径：");
		label_6.setBounds(22, 30, 61, 17);
		
		text_3 = new Text(composite_2, SWT.BORDER);
		text_3.setBounds(91, 27, 220, 23);
		
		Button button_6 = new Button(composite_2, SWT.NONE);
		button_6.setText("…");
		button_6.setBounds(328, 25, 38, 27);
		
		TabItem tabItem_1 = new TabItem(tabFolder, 0);
		tabItem_1.setText("数据库");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite_3);
		
		Group group_4 = new Group(composite_3, SWT.NONE);
		group_4.setText("连接信息");
		group_4.setBounds(10, 10, 330, 173);
		
		Label label_7 = new Label(group_4, SWT.NONE);
		label_7.setText("数据库类型：");
		label_7.setBounds(10, 24, 70, 17);
		
		Combo combo = new Combo(group_4, SWT.NONE);
		combo.setItems(new String[] {"mysql", "sqlserver", "mongodb", "derby", "sqlite"});
		combo.setBounds(86, 21, 76, 25);
		
		text_4 = new Text(group_4, SWT.BORDER);
		text_4.setBounds(271, 49, 47, 23);
		
		text_5 = new Text(group_4, SWT.BORDER);
		text_5.setBounds(86, 49, 131, 23);
		
		Label label_8 = new Label(group_4, SWT.NONE);
		label_8.setText("数据库地址：");
		label_8.setBounds(10, 52, 70, 17);
		
		Label label_9 = new Label(group_4, SWT.NONE);
		label_9.setText("用户名：");
		label_9.setBounds(33, 85, 47, 17);
		
		text_6 = new Text(group_4, SWT.BORDER);
		text_6.setBounds(86, 82, 162, 23);
		
		Label label_10 = new Label(group_4, SWT.NONE);
		label_10.setText("密码：");
		label_10.setBounds(44, 115, 36, 17);
		
		text_7 = new Text(group_4, SWT.BORDER | SWT.PASSWORD);
		text_7.setBounds(86, 112, 162, 23);
		
		Label label_11 = new Label(group_4, SWT.NONE);
		label_11.setText("数据库名称：");
		label_11.setBounds(10, 144, 70, 17);
		
		text_8 = new Text(group_4, SWT.BORDER);
		text_8.setBounds(86, 141, 162, 23);
		
		Button button_7 = new Button(group_4, SWT.NONE);
		button_7.setText("测试连接");
		button_7.setBounds(256, 136, 64, 27);
		
		Label label_12 = new Label(group_4, SWT.NONE);
		label_12.setText("端口：");
		label_12.setBounds(229, 52, 36, 17);
		
		Group group_5 = new Group(composite_3, SWT.NONE);
		group_5.setText("数据信息");
		group_5.setBounds(346, 10, 171, 173);
		
		Label label_13 = new Label(group_5, SWT.NONE);
		label_13.setText("SQL语句：");
		label_13.setBounds(10, 29, 59, 17);
		
		text_9 = new Text(group_5, SWT.BORDER | SWT.V_SCROLL);
		text_9.setBounds(10, 52, 151, 111);
		
		TabItem tabItem_2 = new TabItem(tabFolder, 0);
		tabItem_2.setText("Web服务");
		
		Composite composite_4 = new Composite(tabFolder, SWT.NONE);
		tabItem_2.setControl(composite_4);
		
		Label label_14 = new Label(composite_4, SWT.NONE);
		label_14.setText("Web服务地址：");
		label_14.setBounds(27, 20, 90, 17);
		
		text_10 = new Text(composite_4, SWT.BORDER);
		text_10.setBounds(121, 18, 258, 23);
		
		Label label_15 = new Label(composite_4, SWT.NONE);
		label_15.setText("参数：");
		label_15.setBounds(78, 63, 38, 17);
		
		text_11 = new Text(composite_4, SWT.BORDER | SWT.V_SCROLL);
		text_11.setBounds(123, 66, 256, 95);
		
		Label label_16 = new Label(composite_4, SWT.NONE);
		label_16.setText("注：key-value形式");
		label_16.setBounds(385, 95, 106, 17);
		
		Label label_17 = new Label(composite_4, SWT.NONE);
		label_17.setText("一行表示一个参数");
		label_17.setBounds(385, 117, 105, 17);
		
		Label label_18 = new Label(composite_4, SWT.NONE);
		label_18.setText("注：参数形式 {key}");
		label_18.setBounds(385, 20, 110, 17);
		
		TabItem tabItem_3 = new TabItem(tabFolder, 0);
		tabItem_3.setText("段引擎");
		
		Composite composite_5 = new Composite(tabFolder, SWT.NONE);
		tabItem_3.setControl(composite_5);
		
		Label label_19 = new Label(composite_5, SWT.NONE);
		label_19.setText("段引擎号：");
		label_19.setBounds(28, 22, 61, 17);
		
		Label label_20 = new Label(composite_5, SWT.NONE);
		label_20.setText("数据名：");
		label_20.setBounds(39, 56, 48, 17);
		
		text_12 = new Text(composite_5, SWT.BORDER);
		text_12.setBounds(95, 19, 225, 23);
		
		Button button_8 = new Button(composite_5, SWT.NONE);
		button_8.setText("发现");
		button_8.setBounds(336, 17, 44, 27);
		
		text_13 = new Text(composite_5, SWT.BORDER);
		text_13.setBounds(93, 53, 227, 23);
		
		Button button_9 = new Button(composite_5, SWT.NONE);
		button_9.setText("预览");
		button_9.setBounds(336, 50, 44, 27);
		
		Group group_3 = new Group(group_2, SWT.NONE);
		group_3.setText("同步策略");
		group_3.setBounds(10, 248, 535, 118);
		
		Composite composite = new Composite(group_3, SWT.NONE);
		composite.setBounds(185, 37, 335, 65);
		
		Label label_4 = new Label(composite, SWT.NONE);
		label_4.setText("禁用数据缓冲功能");
		label_4.setBounds(113, 23, 99, 17);
		
		Composite composite_1 = new Composite(group_3, SWT.NONE);
		composite_1.setBounds(12, 19, 156, 89);
		
		Button button = new Button(composite_1, SWT.RADIO);
		button.setText("禁用数据缓冲");
		button.setSelection(true);
		button.setBounds(6, 5, 97, 17);
		
		Button button_1 = new Button(composite_1, SWT.RADIO);
		button_1.setText("热（实时）同步");
		button_1.setBounds(6, 24, 119, 17);
		
		Button button_2 = new Button(composite_1, SWT.RADIO);
		button_2.setText("温（周期/定时）同步");
		button_2.setBounds(6, 44, 140, 17);
		
		Button button_3 = new Button(composite_1, SWT.RADIO);
		button_3.setText("冷（手动）同步");
		button_3.setBounds(6, 65, 140, 17);
		
		Label label_5 = new Label(group_3, SWT.NONE);
		label_5.setText("同步详情：");
		label_5.setBounds(185, 19, 61, 17);
		
		Button button_4 = new Button(shell, SWT.NONE);
		button_4.setText("确定");
		button_4.setBounds(194, 549, 80, 27);
		
		Button button_5 = new Button(shell, SWT.NONE);
		button_5.setText("取消");
		button_5.setBounds(314, 549, 80, 27);

	}

}
