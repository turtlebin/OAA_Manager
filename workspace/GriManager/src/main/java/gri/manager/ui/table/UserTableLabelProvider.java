package gri.manager.ui.table;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import gri.driver.model.User;

public class UserTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	@Override
	public String getColumnText(Object obj, int index) {
		if (obj instanceof User) {
			User user = (User) obj;
			if (index == 0)
				return user.getAccount();
			if (index == 1)
				return user.getPassword();
			if (index == 2)
				return user.getNickname();
		}
		return null;
	}

}
