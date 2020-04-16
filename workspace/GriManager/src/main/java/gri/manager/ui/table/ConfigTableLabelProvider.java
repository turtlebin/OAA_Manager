package gri.manager.ui.table;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ConfigTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	@Override
	public String getColumnText(Object obj, int index) {
		if (obj instanceof String) {
			String CrossExpression = (String) obj;
			if (index == 0) {
				// 0/5 * * * * ?
				// 0 0/5 * * * ?
				// 0 0 0/5 * * ?

				// 44 30 12 * * ?
				// 44 30 12 1 * ?
				// 44 30 12 ? * 1
				if (CrossExpression.endsWith(" * * * * ?")) {
					int index1 = CrossExpression.indexOf('/');
					int index2 = CrossExpression.indexOf(" * * * * ?");
					String str = CrossExpression.substring(index1 + 1, index2);
					return "每隔 " + str + "秒";
				}
				if (CrossExpression.endsWith(" * * * ?")) {
					int index1 = CrossExpression.indexOf('/');
					int index2 = CrossExpression.indexOf(" * * * ?");
					String str = CrossExpression.substring(index1 + 1, index2);
					return "每隔 " + str + "分钟";
				}
				if (CrossExpression.endsWith(" * * ?") && CrossExpression.contains("/")) {
					int index1 = CrossExpression.indexOf('/');
					int index2 = CrossExpression.indexOf(" * * ?");
					String str = CrossExpression.substring(index1 + 1, index2);
					return "每隔 " + str + "小时";
				} else if (CrossExpression.endsWith(" * * ?")) {
					return "每天 " + CrossExpression.substring(6, 8) + "时" + CrossExpression.substring(3, 5) + "分"
							+ CrossExpression.substring(0, 2) + "秒";
				} else if (CrossExpression.endsWith(" * ?")) {
					int index2 = CrossExpression.indexOf(" * ?");
					return "每月 " + CrossExpression.substring(9, index2) + "号 " + CrossExpression.substring(6, 8) + "时"
							+ CrossExpression.substring(3, 5) + "分" + CrossExpression.substring(0, 2) + "秒";
				} else {
					Integer week = Integer.parseInt(CrossExpression.substring(13, 14));
					String w2 = "";
					switch (week) {
					case 1:
						w2 = "星期一";
						break;
					case 2:
						w2 = "星期二";
						break;
					case 3:
						w2 = "星期三";
						break;
					case 4:
						w2 = "星期四";
						break;
					case 5:
						w2 = "星期五";
						break;
					case 6:
						w2 = "星期六";
						break;
					case 7:
						w2 = "星期日";
						break;
					}
					return "每周  " + w2 + " " + CrossExpression.substring(6, 8) + "时" + CrossExpression.substring(3, 5)
							+ "分" + CrossExpression.substring(0, 2) + "秒";
				}
			}
		}
		return null;
	}

}
