package gri.manager.ui.window;

import gri.driver.manager.ProcessManager;
import gri.driver.model.process.StatsConf;
import gri.driver.model.process.StatsConfDto1;
import gri.manager.ui.composite.StatsTableWindow;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;

import gri.driver.model.process.StatsResult;

public class StatsDialog1 extends Dialog {

	protected Object result;
	protected Shell shell;
	//private Shell parentShell;
	
	public static final int WIDTH = 464;
	public static final int HEIGHT = 542;
	
	private ProcessManager manager;
	private StatsConfDto1 statsConfDto1;
	private StatsConf statsConf;
	private StatsResult statsResult;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public StatsDialog1(Shell parent, int style ,StatsConfDto1 statsConfDto1, ProcessManager manager) {
		super(parent, style);
		//this.parentShell = parent;
		this.statsConfDto1 = statsConfDto1;
		this.statsConf = statsConfDto1.getStatsConf();
		this.manager = manager;
		statsResult = manager.calculate(statsConfDto1.getParagraphId(), statsConf);
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
		shell.setSize(WIDTH+36, HEIGHT+58);
		shell.setText("统计管理");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shell, /*SWT.BORDER |*/ SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(10, 10, 464, 542);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		StatsTableWindow statsConfWindow = new StatsTableWindow(scrolledComposite, SWT.NONE,statsResult);
	}

	public ProcessManager getManager() {
		return manager;
	}

	public void setManager(ProcessManager manager) {
		this.manager = manager;
	}

	public StatsConf getStatsConf() {
		return statsConf;
	}

	public void setStatsConf(StatsConf statsConf) {
		this.statsConf = statsConf;
	}

	public StatsResult getStatsResult() {
		return statsResult;
	}

	public void setStatsResult(StatsResult statsResult) {
		this.statsResult = statsResult;
	}
	
	public Shell getShell(){
		return shell;
	}
}
