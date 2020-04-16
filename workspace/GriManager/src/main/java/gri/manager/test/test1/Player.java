package gri.manager.test.test1;

import gri.manager.ui.window.ContainerDialog;
import gri.manager.util.Constant;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Player   
{   
  
    protected Shell shell; 
    private String file;
  
    /**  
     * Launch the application  
     * @param args  
     */  
    public static void main(String[] args)   
    {   
        try  
        {   
            Player window = new Player();   
            window.open();   
        }   
        catch (Exception e)   
        {   
            e.printStackTrace();   
        }   
    }   
  
    /**  
     * Open the window  
     */  
    public void open()   
    {   
        final Display display = Display.getDefault();   
        createContents();   
        shell.open();   
        shell.layout();   
        while (!shell.isDisposed())   
        {   
            if (!display.readAndDispatch())   
                display.sleep();   
        }   
    }   
  
    /**  
     * Create contents of the window  
     */  
    protected void createContents()   
    {   
        shell = new Shell();   
        shell.setLayout(new FillLayout());   
        shell.setSize(500, 375);   
        shell.setText("模板播放");   
  
        final WMP composite = new WMP(shell, SWT.NONE);   
//        composite.play("D:/1.swf"); 
        //this.setFile("D://wwj//env//QuickEasyFTPServer//fileDir//22b486bf-0988-430c-ba55-4e2e3720eb88");
        this.setFile("D://wwj//runtime//cache//22b486bf-0988-430c-ba55-4e2e3720eb88");
        composite.play(file);   
  
        //
        /*Button button_addContainer = new Button(shell, SWT.NONE);
		button_addContainer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				composite.pause();
			}
		});
		button_addContainer.setBounds(229, 88, 56, 27);
		button_addContainer.setText("创建空间");*/
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }  
}  
