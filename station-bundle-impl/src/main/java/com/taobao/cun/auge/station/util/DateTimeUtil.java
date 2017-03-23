package com.taobao.cun.auge.station.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateTimeUtil {

	private DateTimeUtil() {

	}

	public static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("yyyyMM");

	/**
	 * 两个年月相减，获得相差月数
	 * 
	 * @param Date
	 *            第一个日期（yyyyMM）
	 * @param String
	 *            第二个日期（yyyyMM）
	 * @return int 相差月数
	 * @throws java.text.ParseException
	 */
	public static int getMonthOffset(Date startDate, String endDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);

		int startYear = c.get(Calendar.YEAR);
		int startMonth = c.get(Calendar.MONTH);

		Date d1 = null;
		
		try {
			d1 = MONTH_FORMAT.parse(endDate);
		} catch (ParseException e) {
		}
		c.setTime(d1);
		int endYear = c.get(Calendar.YEAR);
		int endMonth = c.get(Calendar.MONTH);

		int offset;
		if (startYear == endYear) {
			offset = endMonth - startMonth;
		} else {
			offset = 12 * (endYear - startYear) + endMonth - startMonth;
		}
		return offset;
	}

}
