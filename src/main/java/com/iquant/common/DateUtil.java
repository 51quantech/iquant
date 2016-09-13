package com.iquant.common;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yonggangli on 2016/9/1.
 */
public class DateUtil {
	private final static Logger log = LogManager.getLogger(DateUtil.class);
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
	public static final String DEFAULT_DATE_FORMATE = "yyyyMMddHHmmss";
	public static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_Day = "yyyyMMdd";
	public static final String DATE_FORMAT_2 = "yyyy-MM-dd";

	/**
	 * 取得某日期时间的特定表示格式的字符串
	 *
	 * @param format
	 *            时间格式
	 * @param date
	 *            某日期（Date）
	 * @return 某日期的字符串
	 */
	public static synchronized String getDate2Str(String format, Date date) {
		simpleDateFormat.applyPattern(format);
		return simpleDateFormat.format(date);
	}

	public static String getDay() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}

	public static Date getTimeMin(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String da = sdf.format(date);
		da = da + " 00:00:00";
		Date d = getStrToDate(DATE_FORMAT_1, da);
		return d;
	}

	public static Date getTimeMax(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String da = sdf.format(date);
		da = da + " 23:59:59";
		Date d = getStrToDate(DATE_FORMAT_1, da);
		return d;
	}

	public static Date getTimeMin() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String da = sdf.format(date);
		da = da + " 00:00:00";
		Date d = getStrToDate(DATE_FORMAT_1, da);
		return d;
	}

	public static Date getTimeMax() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String da = sdf.format(date);
		da = da + " 23:59:59";
		Date d = getStrToDate(DATE_FORMAT_1, da);
		return d;
	}

	/**
	 * 将特定格式的时间字符串转化为Date类型
	 *
	 * @param format
	 *            时间格式
	 * @param str
	 *            某日期的字符串
	 * @return 某日期（Date）
	 */
	public static synchronized Date getStrToDate(String format, String str) {
		simpleDateFormat.applyPattern(format);
		ParsePosition parseposition = new ParsePosition(0);
		return simpleDateFormat.parse(str, parseposition);
	}

	public static String date2String(Date date) {
		return getDate2Str(DEFAULT_DATE_FORMATE, date);
	}

	public static String dateFormatString(Date date) {
		return getDate2Str(DATE_FORMAT_1, date);
	}

	public static String date2String(Date date, String format) {
		return getDate2Str(format, date);
	}

	/**
	 * 检测字符串是否为日期
	 *
	 * @param dateTime
	 *            时间字符串
	 * @param pattern
	 *            Eg "yyyy-MM-dd"
	 * @return 返回结果
	 */
	public static boolean isDateTime(String dateTime, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		ParsePosition pos = new ParsePosition(0);
		Date dt = df.parse(dateTime, pos);
		return !(dt == null);
	}

	public static XMLGregorianCalendar getXMLGregorianCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		XMLGregorianCalendar xmlCalendar = null;
		try {
			DatatypeFactory dtf = DatatypeFactory.newInstance();
			xmlCalendar = dtf.newXMLGregorianCalendar(
					calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH) + 1,
					calendar.get(Calendar.DAY_OF_MONTH),
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE),
					calendar.get(Calendar.SECOND),
					calendar.get(Calendar.MILLISECOND),
					calendar.get(Calendar.ZONE_OFFSET) / (1000 * 60));
		} catch (Exception e) {
			log.error("getXMLGregorianCalendar error!", e);
		}
		return xmlCalendar;
	}

	public static Date getDateFromXmlGregorianCalendar(XMLGregorianCalendar da) {
		if (da != null) {
			int year = da.getYear();
			int month = da.getMonth();
			int day = da.getDay();
			int hour = da.getHour();
			int minute = da.getMinute();
			int second = da.getSecond();
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month - 1, day, hour, minute, second);
			Date date = calendar.getTime();
			return date;
		} else {
			return null;
		}

	}

	public static boolean passTime(Date tempDate, int hour) {
		return !(tempDate == null || hour <= 0)
				&& tempDate.before(getLimitDate(hour));
	}

	/**
	 * 得到n小时前时间
	 *
	 * @param hour
	 *            小时数
	 * @return Date
	 */
	public static Date getLimitDate(int hour) {
		Calendar cl = Calendar.getInstance();
		Long clTemp = cl.getTimeInMillis() - hour * 60 * 60 * 1000;
		cl.setTimeInMillis(clTemp);
		return cl.getTime();
	}

	/**
	 * 得到n天前日期
	 *
	 * @param day
	 *            日期
	 * @return Date
	 */
	public static Date getBeforeDate(int day) throws Exception {
		Calendar cl = Calendar.getInstance();
		Long clTemp = cl.getTimeInMillis() - day * 24 * 60 * 60 * 1000L;
		cl.setTimeInMillis(clTemp);
		return getFormatDate(cl.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 得到n天前日期的开始时刻
	 *
	 * @param day
	 *            日期
	 * @return Date
	 */
	public static Date getBeforeDayStartDate(int day) {
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.DAY_OF_MONTH, -day);
		cl.set(Calendar.HOUR_OF_DAY, 0);
		cl.set(Calendar.MINUTE, 0);
		cl.set(Calendar.SECOND, 0);
		return cl.getTime();
	}

	/**
	 * 得到n天前日期的结束时刻
	 *
	 * @param day
	 *            日期
	 * @return Date
	 */
	public static Date getBeforeDayEndDate(int day) {
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.DAY_OF_MONTH, -day);
		cl.set(Calendar.HOUR_OF_DAY, 23);
		cl.set(Calendar.MINUTE, 59);
		cl.set(Calendar.SECOND, 59);
		return cl.getTime();
	}

	/**
	 * 得到n分钟前时间
	 * 
	 * @param minute
	 *            小时数
	 * @return Date
	 */
	public static Date getLimitDateByMinute(int minute) {
		Calendar cl = Calendar.getInstance();
		Long clTemp = cl.getTimeInMillis() - minute * 60 * 1000;
		cl.setTimeInMillis(clTemp);
		return cl.getTime();
	}

	// 判断是不是日期型 true代表是日期型，false代表失败
	public static boolean isValidDate(String sDate) {
		String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
		String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
				+ "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
				+ "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
				+ "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		if ((sDate != null)) {
			Pattern pattern = Pattern.compile(datePattern1);
			Matcher match = pattern.matcher(sDate);
			if (match.matches()) {
				pattern = Pattern.compile(datePattern2);
				match = pattern.matcher(sDate);
				return match.matches();
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 计算时间差 传入的是date类型
	 * 
	 * @param date1
	 * @param date2
	 * @param type
	 *            返回类型
	 * @return
	 */
	public static String dateIntervalForDate(Date date1, Date date2, String type) {
		try {
			if (date1 != null && !"".equals(date1) && !"null".equals(date1)
					&& date2 != null && !"".equals(date2)
					&& !"null".equals(date2)) {
				// 转换后调用时间差算法
				return coreDateInterval(date1, date2, type);
			}
		} catch (Exception e) {
			log.error("计算时间差 error", e);
		}
		return "";
	}

	/**
	 * 计算时间差 传入的类型如果是字符串，则用此方法转换成date类型，再调用时间差算法
	 * 
	 * @param date1
	 * @param date2
	 * @param type
	 *            返回类型
	 * @return
	 */
	public static String dateIntervalForString(String date1, String date2,
			String type) {
		try {
			if (date1 != null && !"".equals(date1) && !"null".equals(date1)
					&& date2 != null && !"".equals(date2)
					&& !"null".equals(date2)) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date3 = sdf.parse(date1);
				Date date4 = sdf.parse(date2);
				// 转换后调用时间差算法
				return coreDateInterval(date3, date4, type);
			}
		} catch (Exception e) {
			log.error("计算时间差 error", e);
		}
		return "";
	}

	/**
	 * 计算时间差核心方法
	 * 
	 * @param args
	 */
	public static String coreDateInterval(Date date1, Date date2, String type) {
		long l = date2.getTime() - date1.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		if ("1".equals(type)) {
			return day + "天" + hour + "小时" + min + "分" + s + "秒";
		} else if ("2".equals(type)) {
			return day * 24 + hour + ":" + min + ":" + s;
		} else {
			return "";
		}
	}

	/**
	 * 计算当前时间到凌晨时间差
	 * 
	 * @param args
	 */
	public static int getBetweenDayMs() {
		Calendar c = Calendar.getInstance();
		Date date = new Date();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		int ms = (int) (c.getTime().getTime() - date.getTime()) / 1000;
		return ms;
	}

	public static Date getPreMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1); // 得到前一天
		cal.add(Calendar.MONTH, -1); // 得到前一个月
		return cal.getTime();
	}

	public static Date getDatePreMonthFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, -1); // 得到前一个月
		return cal.getTime();
	}

	public static Date getDateNextMonthFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1); // 得到后一个月
		return cal.getTime();
	}

	public static Date getDateThisMonthFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1); // 得到前一天
		return cal.getTime();
	}

	public static Date getDateNumMonthFirstDay(Date date, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, num); // 得到后一个月
		return cal.getTime();
	}

	public static int getMonthDiff(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		return Math.abs((cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR))
				* 12 + cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH));
	}

	public static String default_format = "yyyy-MM-dd HH:mm:ss";

	public static String getDate2String(Date date, String format) {
		SimpleDateFormat sformat = new SimpleDateFormat(format);
		return sformat.format(date);
	}

	public static Date getDateFromString(String date) throws Exception {
		SimpleDateFormat sformat = new SimpleDateFormat(default_format);
		return sformat.parse(date);
	}

	public static Date getString2Date(String date, String format)
			throws Exception {
		SimpleDateFormat sformat = new SimpleDateFormat(format);
		return sformat.parse(date);
	}

	public static Date getFormatDate(Date date, String format) throws Exception {
		String dateStr = getDate2String(date, format);
		return getString2Date(dateStr, format);
	}

	public static int getDateMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}
}
