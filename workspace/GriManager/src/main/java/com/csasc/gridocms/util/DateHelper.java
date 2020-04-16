package com.csasc.gridocms.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

	/**
	 * 当前时间字符串 yyyy-MM-dd HHmm
	 * 
	 * @return
	 */
	public static String getCurrentYMDHM() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HHmm");
		return df.format(new Date());
	}

	/**
	 * 当前时间字符串 HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentHMS() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(new Date());
	}

	/**
	 * 当前时间字符串 HH_mm_ss
	 * 
	 * @return
	 */
	public static String getCurrentH_M_S() {
		SimpleDateFormat df = new SimpleDateFormat("HH_mm_ss");
		return df.format(new Date());
	}
}
