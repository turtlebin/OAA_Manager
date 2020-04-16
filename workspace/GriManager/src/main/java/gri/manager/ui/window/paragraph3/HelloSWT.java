package gri.manager.ui.window.paragraph3;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import javax.swing.Scrollable;
 import javax.swing.text.StyleConstants.ColorConstants;
 
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
 import org.eclipse.swt.widgets.Label;
 import org.eclipse.swt.widgets.Shell;
 import org.eclipse.swt.widgets.Text;
 
 public class HelloSWT {
	 private static Composite composite;
	 private static Composite composite_test;
	
	 public static void add(Composite shell_1) {
		
     Composite composite=new Composite(shell_1,SWT.NONE);
             Button button = new Button(composite,SWT.HORIZONTAL);
             button.setText("Button"+1);
    
     button.setSize(100, 50);

         
	 }
	 private static boolean addComposite() {
			Composite composite=new Composite(composite_test,SWT.NULL);
			composite.setSize(380,100);
		
			Label label=new Label(composite,SWT.NULL);
			label.setText("数据源：数据库：数据表");
			label.setSize(390,30);
			
			GridData gridData = new org.eclipse.swt.layout.GridData();  
		    gridData.horizontalAlignment = SWT.FILL;  
		    gridData.grabExcessHorizontalSpace = true;  
		    gridData.grabExcessVerticalSpace = true;  
		    gridData.verticalAlignment = SWT.FILL;  
	        Table table=new Table(composite,SWT.BORDER);
	        table.setHeaderVisible(true);// 设置显示表头  
	        table.setLayoutData(gridData);// 设置表格布局  
	        table.setLinesVisible(true);// 设置显示表格线/*  
	        // 创建表头的字符串数组  
	        String[] tableHeader = {"姓名", "性别", "电话", "电子邮件"};  
	        for (int i = 0; i < tableHeader.length; i++)  
	        {  
	            TableColumn tableColumn = new TableColumn(table, SWT.NONE);  
	            tableColumn.setText(tableHeader[i]);  
	            // 设置表头可移动，默认为false  
	            tableColumn.setMoveable(true);  
	        }  
	        TableItem item = new TableItem(table, SWT.NONE);  
	        item.setText(new String[]{"张三", "男", "123", ""});  
	        // 设置图标  
	        // item.setImage( ImageFactory.loadImage(  
	        // table.getDisplay(),ImageFactory.ICON_BOY));  
	  
	        for (int i = 0; i < 115; i++)  
	        {  
	            item = new TableItem(table, SWT.NONE);  
	            item.setText(new String[]{"李四", "男", "4582", ""});  
	        }  
	        for (int i = 0; i < tableHeader.length; i++)  
	        {  
	            table.getColumn(i).pack();  
	        }  
	        table.setSize(390,200);



			return true;
		}
	 
	 public static void initButton(Button[] buttons)
	 {
		 
		 buttons[0]=new Button(composite,SWT.PUSH);
		 buttons[0].setText("你好");
	 }
	 
     public static void main(String[] args) {
         Display display = new Display();
         Color color =  new Color(display,255,0,0);
         
         //create a shell
         Shell shell_1 = new Shell(display);
         shell_1.setText("This is a shell in main function()");
         shell_1.setBounds(100,100,400,200);
         
         GridLayout gridLayout = new GridLayout();  
 	    gridLayout.numColumns = 1;  
 	    
 	    composite=new Composite(shell_1,SWT.NULL);
 	    composite.setLayout(gridLayout);
 	    composite.setSize(400,200);
 	    
 	    final Button[] buttons=new Button[10];
 	    
 	    initButton(buttons);
 	    System.out.println(buttons[0].getText());
 	    
// 	    for(int i=0;i<10;i++)
// 	    {
// 	    	buttons[i]=new Button(composite,SWT.CHECK);
// 	    	final Button button=buttons[i];
// 	    	final int a=i;
// 	    	buttons[i].setText("按钮"+i);
// 	 	    buttons[i].pack();
// 	 	    buttons[i].addSelectionListener(new SelectionListener() {
// 	 	    	@Override
// 	 	    	public void widgetSelected(SelectionEvent event)
// 	 	    	{
// 	 	    		if(button.getSelection())
// 	 	    		{
// 	 	    			if(a==0) {
// 	 	    			button.setSelection(true);
// 	 	    			System.out.println(true);
// 	 	    			}
// 	 	    			else {
// 	 	    				System.out.println(buttons[0].getSelection());
// 	 	    			}
// 	 	    		}
// 	 	    		else
// 	 	    		{
// 	 	    			if(a==0) {
// 	 	    			System.out.println(false);
// 	 	    			}
// 	 	    			else {
// 	 	    				System.out.println(buttons[0].getSelection());
// 	 	    			}
// 	 	    		}
// 	 	    	}
//
//				@Override
//				public void widgetDefaultSelected(SelectionEvent arg0) {
//					// TODO Auto-generated method stub
//					
//				}
// 	 	    });
// 	 	  
// 	    }
 	    composite.layout();

// 	    Group group_test=new Group(shell_1,SWT.NONE);
// 	    group_test.setBounds(10,10,380,180);
// 	    group_test.setText("测试");
// 	    group_test.setLayout(new FillLayout());
// 	     
// 	    ScrolledComposite scrolledComposite=new ScrolledComposite(group_test,SWT.H_SCROLL|SWT.V_SCROLL|SWT.BORDER);
// 	    scrolledComposite.setBounds(10,10,360,160);
// 	    
// 	    composite_test=new Composite(scrolledComposite,SWT.NONE);
// 	    
// 	    scrolledComposite.setContent(composite_test);
// 	    composite_test.setBackground(Display.getCurrent().getSystemColor (SWT.COLOR_WHITE));// White color
// 	 
// 	    
// 	    GridLayout gridLayout2=new GridLayout();
// 	    gridLayout2.numColumns=1;
// 	    composite_test.setLayout(gridLayout2);
// 	    //composite_test.setSize(360,100);
// 	    //composite_test.setSize(composite_test.computeSize(SWT.DEFAULT, SWT.DEFAULT));
// 	    composite_test.layout();
//   
//         scrolledComposite.setExpandHorizontal(true);
//         scrolledComposite.setExpandVertical(true);
//         scrolledComposite.setMinWidth(360);
//         scrolledComposite.setMinHeight(160);
//         scrolledComposite.setMinSize(360, 160);
//   
//         
//         scrolledComposite.setMinSize(group_test.computeSize(SWT.DEFAULT, SWT.DEFAULT));//加了这句之后有问题
//         scrolledComposite.layout();
//
//
//         //shell_1.setLayout(new RowLayout());
//         FillLayout layout = new FillLayout();
//         GridLayout grid=new GridLayout();
//         grid.numColumns=1;
//        // RowLayout row=new RowLayout();
//         shell_1.setLayout(grid);
//         for(int i=0;i<10;i++) {
//         addComposite();
//         }
//         
         shell_1.open();
         
         
         while(!shell_1.isDisposed()){
             if(!display.readAndDispatch())
                 display.sleep();
         }
         
         //dispose the resource
         display.beep();
         color.dispose();
         display.dispose();
     }
 }