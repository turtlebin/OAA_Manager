package gri.manager.ui.window;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;

import gri.driver.manager.GriDocManager;
import gri.driver.model.process.Paragraph;
import gri.driver.model.process.Processor;
import gri.manager.ui.composite.WMP;
import gri.manager.util.Constant;
import gri.manager.util.PathHelper;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;

public class PlayerDialog extends Dialog {

	protected Object result;
	private Shell parentShell;
	protected Shell shell;
	
	private String fileName;
	private Paragraph paragraph;
	private GriDocManager manager;
	
	
	private Boolean playing;
	private Button button_play;


	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public PlayerDialog(Shell parent, int style, Paragraph paragraph, GriDocManager manager) {
		super(parent, style);
		this.parentShell = parent;
		this.paragraph= paragraph;
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
		if(!download1()){
			MessageBox box = new MessageBox(shell, SWT.ICON_WARNING);
			box.setMessage("访问的文件不存在！");
			box.open();
			return;
		}
		System.out.println("step5");
		shell = new Shell();   
        //shell.setLayout(new FillLayout(SWT.VERTICAL));   
        shell.setSize(500, 375);   
        shell.setText("多媒体播放");   
		
        final WMP wmp = new WMP(shell, SWT.NONE);
        System.out.println(fileName);
        System.out.println("step6");
        wmp.play(fileName); 
        wmp.setBounds(0, 0, 480, 300);
        playing = true;
        System.out.println("step7");
        
        button_play = new Button(shell, SWT.NONE);
        button_play.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(playing){
					wmp.pause();
					button_play.setText("开始播放");
				}
				else{
					wmp.play();
					button_play.setText("暂停播放");
				}
				playing = !playing;
				
			}
		});
        button_play.setBounds(10, 310, 56, 27);
        button_play.setText("暂停播放");
        
        shell.addShellListener(new ShellAdapter() {
            public void shellClosed(ShellEvent e) {
                wmp.dispose();
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
            }
        });
	}
	
	//从数个引擎缓存下载
	private boolean download1(){
		byte[] bytes = manager.readData1(paragraph);	
		String extention = PathHelper.getExtension(paragraph.getDataSourcePath());
		fileName = "./tmp/"+paragraph.getCache()+"."+extention;
		
		//读取文件到本地				
        try {
        	FileOutputStream fos = new FileOutputStream(fileName);  		  
			fos.write(bytes);
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return true;
	}
	
	//直接从数据源下载，用于流式播放
	private boolean download2(){
		
		Download download = new Download();
		download.start();
		
		//等待连接成功
		synchronized(download){
            //条件不满足，继续等待
            while(download.success==0){
                try {
                	download.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }    
		
		if(download.success<0) return false;
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	class Download extends Thread {
		private String host;
		private int port;
		private String username;
		private String password;
		private String ftpFilePath;
		private Integer success=0;
		
		public Download(){
			analyzeParameter();
		}
		
		private boolean analyzeParameter() {
			String strs[] = paragraph.getDataSourcePath().split("###");
			String urlStr = strs[0];
			this.username = strs.length > 1 ? strs[1] : "anonymous";
			this.password = strs.length > 2 ? strs[2] : "anonymous";
			URL url = null;
			try {
				url = new URL(urlStr);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			}
			this.host = url.getHost();
			this.port = url.getPort() == -1 ? 21 : url.getPort();
			this.ftpFilePath = url.getFile();
			return true;
		}
		
	    public void run() {
	    	System.out.println("step1");
	    	FTPClient ftpClient = new FTPClient();
			try {
				synchronized(this){
						
					ftpClient.connect(this.host, this.port);
					ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);// 重要
					if (!ftpClient.login(this.username, this.password)){
						success =-1;
						this.notify();
						return;
					}
					
					ftpClient.enterLocalPassiveMode();
					ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
					int reply=ftpClient.getReplyCode();
					if(!FTPReply.isPositiveCompletion(reply))
					{
						ftpClient.disconnect();
						success =-1;
						this.notify();
						return;
					}
				
					success =1;
					this.notify();
		        }
				System.out.println("step2");
				
				this.ftpFilePath = new String(this.ftpFilePath.getBytes("GBK"), "ISO-8859-1");// 重要
				
				String extention = PathHelper.getExtension(paragraph.getDataSourcePath());
				fileName = "./tmp/"+paragraph.getCache()+"."+extention;
				FileOutputStream out = new FileOutputStream(fileName);  
				
				InputStream in=ftpClient.retrieveFileStream(this.ftpFilePath); 
				byte[] byteArray=new byte[4096];
				int read=0;
				System.out.println("step3");
				while((read=in.read(byteArray))!=-1){
					out.write(byteArray,0,read);
				}
				System.out.println("step4");
				//这句很重要  要多次操作这个ftp的流的通道,要等他的每次命令完成
				ftpClient.completePendingCommand();
				out.flush();
				out.close();
				ftpClient.logout();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	    }
	}
}
