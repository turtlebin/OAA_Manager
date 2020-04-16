

import org.eclipse.swt.SWT;  
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;  
import org.eclipse.swt.layout.FillLayout;  
import org.eclipse.swt.layout.GridLayout;  
import org.eclipse.swt.widgets.Button;  
import org.eclipse.swt.widgets.Composite;  
import org.eclipse.swt.widgets.Display;  
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;  
public class ss {  
	public static void add(ScrolledComposite sc2,Composite composite,int i) {
		Button button=new Button(composite,SWT.PUSH);
		button.setText("测试"+i);
		Label label=new Label(composite,SWT.NULL);
		label.setText("测试"+i);
		sc2.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));  
        composite.layout();  
	}
    public static void main(String[] args) {  
        Display display = new Display();  
        Color red = display.getSystemColor(SWT.COLOR_RED);  
        Color blue = display.getSystemColor(SWT.COLOR_BLUE);  
        Shell shell = new Shell(display);  
        shell.setLayout(new FillLayout());  
        // set the size of the scrolled content - method 1  
        final ScrolledComposite sc1 = new ScrolledComposite(shell, SWT.H_SCROLL  
                | SWT.V_SCROLL | SWT.BORDER);  
        final Composite c1 = new Composite(sc1, SWT.NONE);  
        sc1.setContent(c1);  
        c1.setBackground(red);  
        GridLayout layout = new GridLayout();  
        layout.numColumns = 1;  
        c1.setLayout(layout);  
        Button b1 = new Button(c1, SWT.PUSH);  
        b1.setText("first button");  
        /* 
         * 这是两种用法中的一种，效果是：根据已经存在的组件，计算需要的composite的区域的大小 
         * 即运行结果中红色显示的部分。 
         */  
        c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));  
          
        // set the minimum width and height of the scrolled content - method 2  
        final ScrolledComposite sc2 = new ScrolledComposite(shell, SWT.H_SCROLL  
                | SWT.V_SCROLL | SWT.BORDER);  
        sc2.setExpandHorizontal(true);  
        sc2.setExpandVertical(true);  
        final Composite c2 = new Composite(sc2, SWT.NONE);  
        sc2.setContent(c2);  
        c2.setBackground(blue);  
        layout = new GridLayout();  
        layout.numColumns = 1;  
        c2.setLayout(layout);  
        Button b2 = new Button(c2, SWT.PUSH);  
        b2.setText("first button");  
        /* 
         * 这是第二种用法，整个composite区域是用到的Composite区域 
         */  
        sc2.setMinSize(c2.computeSize(SWT.DEFAULT, SWT.DEFAULT));  
        Button add = new Button(shell, SWT.PUSH);  
        add.setText("add children");  
        final int[] index = new int[] { 0 };  
        add.addListener(SWT.Selection, new Listener() {  
            public void handleEvent(Event e) {  
                index[0]++;  
                Button button = new Button(c1, SWT.PUSH);  
                button.setText("button " + index[0]);  
                // reset size of content so children can be seen - method 1  
                c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));  
                c1.layout();  
                button = new Button(c2, SWT.PUSH);  
                button.setText("button " + index[0]);  
                // reset the minimum width and height so children can be seen -  
                // method 2  
                sc2.setMinSize(c2.computeSize(SWT.DEFAULT, SWT.DEFAULT));  
                c2.layout();  
            }  
        });  
        
        for(int i=0;i<100;i++) {
        	add(sc2,c2,i);
        	
        }
        
        ScrollBar vb = sc2.getVerticalBar();// 控制垂直方向滚动增量  
        vb.setIncrement(10);    
        
        c2.addMouseListener(new MouseAdapter(){  
        
         @Override  
         public void mouseDown(MouseEvent e) {  
          c2.setFocus();  
         }  
        });  
        
        shell.open();  
        while (!shell.isDisposed()) {  
            if (!display.readAndDispatch())  
                display.sleep();  
        }  
        display.dispose();  
    }  
}  