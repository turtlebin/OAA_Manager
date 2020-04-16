package com.csasc.test;
import org.eclipse.swt.SWT;  
import org.eclipse.swt.graphics.Color;  
import org.eclipse.swt.layout.FillLayout;  
import org.eclipse.swt.ole.win32.OleAutomation;  
import org.eclipse.swt.ole.win32.OleControlSite;  
import org.eclipse.swt.ole.win32.OleFrame;  
import org.eclipse.swt.ole.win32.Variant;  
import org.eclipse.swt.widgets.Composite;  
import org.eclipse.swt.widgets.Display;  
import org.eclipse.swt.widgets.Shell;  
  
public class SwtVideo {  
  
    protected Shell shell;  
  
    /** 
     * Launch the application 
     * @param args 
     */  
    public static void main(String[] args) {  
        try {  
            SwtVideo window = new SwtVideo();  
            window.open();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * Open the window 
     */  
    public void open() {  
        final Display display = Display.getDefault();  
        createContents();  
        shell.open();  
        shell.layout();  
        while (!shell.isDisposed()) {  
            if (!display.readAndDispatch())  
                display.sleep();  
        }  
    }  
  
    /** 
     * Create contents of the window 
     */  
    protected void createContents() {  
        shell = new Shell();  
        shell.setLayout(new FillLayout());  
        shell.setSize(400, 300);  
        shell.setText("SWT Application");  
  
        final Composite composite = new Composite(shell, SWT.NONE);  
        composite.setLayout(new FillLayout());  
          
        OleFrame frame = new OleFrame(composite, SWT.NONE);   
        OleControlSite controlSite = new OleControlSite(frame, SWT.NONE, "WMPlayer.OCX");  
        controlSite.setBackground(new Color(Display.getDefault(), 255, 61, 61));  
  
        OleAutomation auto = new OleAutomation(controlSite);   
  
    //    int result = controlSite.doVerb(OLE.OLEIVERB_SHOW);  
  
        int[] rgdispid = auto.getIDsOfNames(new String[]{"stretchToFit"});  
        int dispIdMember = rgdispid[0];  
        Variant[] rgvarg = new Variant[1];  
        rgvarg[0] = new Variant("True");  
        @SuppressWarnings("unused")
		boolean isX = false;  
  
        rgdispid = auto.getIDsOfNames(new String[]{"uiMode"});  
        dispIdMember = rgdispid[0];  
        rgvarg = new Variant[1];   
        rgvarg[0] = new Variant("none");  
        isX = auto.setProperty(dispIdMember, rgvarg);  
  
        rgdispid = auto.getIDsOfNames(new String[]{"enableContextMenu"});  
        System.out.println("2 rgdispid=" + rgdispid);  
        dispIdMember = rgdispid[0];  
        rgvarg = new Variant[1];  
        rgvarg[0] = new Variant("True");  
        isX = auto.setProperty(dispIdMember, rgvarg);  
  
        rgdispid = auto.getIDsOfNames(new String[]{"enabled"});  
        dispIdMember = rgdispid[0];  
        rgvarg = new Variant[1];  
        rgvarg[0] = new Variant("True");  
        isX = auto.setProperty(dispIdMember, rgvarg);  
  
        rgdispid = auto.getIDsOfNames(new String[]{"fullScreen"});  
        dispIdMember = rgdispid[0];  
        rgvarg = new Variant[1];  
        rgvarg[0] = new Variant("False");  
        isX = auto.setProperty(dispIdMember, rgvarg);  
  
        rgdispid = auto.getIDsOfNames(new String[]{"windowlessVideo"});  
        dispIdMember = rgdispid[0];  
        rgvarg = new Variant[1];  
        rgvarg[0] = new Variant("False");  
        isX = auto.setProperty(dispIdMember, rgvarg);  
  
        rgdispid = auto.getIDsOfNames(new String[]{"URL"});  
        dispIdMember = rgdispid[0];  
        rgvarg = new Variant[1];   
//      rgvarg[0] = new Variant("D://test.avi");  
       // "C:\FFOutput\视频~1.avi"
        rgvarg[0] = new Variant("C:/Users/haha/Desktop/hello.avi");  
        isX = auto.setProperty(dispIdMember, rgvarg);  
  
    }  
  
}  