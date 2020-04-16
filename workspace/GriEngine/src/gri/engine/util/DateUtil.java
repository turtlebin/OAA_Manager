package gri.engine.util;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {

	public DateUtil() {
		// TODO Auto-generated constructor stub
	}

	public static String getCurrentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date now = new Date();
		return dateFormat.format(now);
	}

	public static void main(String[] args) {
		System.out.println(getCurrentDate().toString());
	}
}
