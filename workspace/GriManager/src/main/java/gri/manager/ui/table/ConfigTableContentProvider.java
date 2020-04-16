package gri.manager.ui.table;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import gri.driver.model.process.Paragraph;

public class ConfigTableContentProvider implements IStructuredContentProvider {

	private Paragraph paragraph;

	public ConfigTableContentProvider(Paragraph paragraph) {
		this.paragraph = paragraph;
	}

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
		if (element.equals("root")) {
			if (paragraph.getWarmSyncDetail().equals(""))
				return new Object[0];
			return paragraph.getWarmSyncDetail().split("#")[1].split("@");
		}
		return null;
	}

}
