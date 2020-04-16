package gri.engine.reflect;

public class MethodsHolder {

	public MethodsHolder() {
		// TODO Auto-generated constructor stub
	}

	private String test(String name) {
		return name+"test";
	}
	
	private String getStatusName(String num) {
		int position = -1;
		int number = Integer.parseInt(num);
		String statusName = null;
		if (number == 0) {
			statusName = "未知";
		} else if ((number & (number - 1)) == 0) {
			while (number != 0) {
				number = number >> 1;
				position++;
			}
			statusName = MapGetter.getStatusMap().get(position);
		} else {
			while(number!=0) {
				position++;
				if((number&1)!=0) {
					statusName=statusName==null?MapGetter.getStatusMap().get(position):statusName+MapGetter.getStatusMap().get(position);
					statusName+=",";
				}
				number=number>>1;
			}
			statusName=statusName.substring(0, statusName.length()-1);
		}
		return statusName;
	}
	
}
