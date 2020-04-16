package gri.manager.ui.composite;

import gri.manager.ui.window.MainWindow;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;

/**
 * @Description 
 * Created by erik on 2016-8-15
 */
public class PlayerComposite extends Composite {
	private String fileName;
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PlayerComposite(Composite parent, int style, String fileName) {
		super(parent, style);
		
		this.fileName= fileName;
		
		Composite composite = new Composite(parent , SWT.NONE);
		composite.setBounds(0, 0, 300, 225);
		composite.setLayout(new FillLayout());
		final WMP wmp = new WMP(composite, SWT.NONE);
	    wmp.play(fileName);
		
		
		Button btn_pre = new Button(this, SWT.NONE);
		btn_pre.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wmp.pause();
			}
		});
		btn_pre.setBounds(0, 230, 40, 27);
		btn_pre.setText("<");
		
		parent.pack();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	
}
