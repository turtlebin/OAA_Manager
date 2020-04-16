package gri.manager.ui.table;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import gri.driver.model.User;
import gri.manager.util.RunTimeData;

public class UserTableContentProvider implements IStructuredContentProvider {

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
		if (element.equals("root"))
			return RunTimeData.userView.toArray(new User[0]);
		else
			return null;
	}

}
