/*
 *  DateUtils.java
 *  
 *  
 */
package com.rongji.websiteMonitor.common.util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.rongji.dfish.engines.xmltmpl.Week;

/**
 * 日期相关的工具类
 * 
 * @author hqj
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

	public static final String Format_Date = "yyyy-MM-dd";
	public static final String Format_Time = "HH:mm:ss";
	public static final String Format_DateTime1 = "yyyy-MM-dd HH:mm:ss";
	public static final String Format_DateTime2 = "yyyy-MM-dd HH:mm";
	public static final SimpleDateFormat sdfd = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat sdft = new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat sdfdt = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
	public static final String[] WEEK_STRING = new String[]{"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};

	/**
	 * 一天的毫秒数
	 */
	public static final long MIS_OF_DAY = 24 * 60 * 60 * 1000l;

	/**
	 * 按预定的格式返回当前日期
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		return sdfd.format(new Date());
	}

	/*
	 * public static String getCurrentDateTime() { return sdfdt.format(new
	 * Date()); }
	 */

	/**
	 * 返回给定的日期时间格式对应的日期时间
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrentDate(String format) {
		SimpleDateFormat t = new SimpleDateFormat(format);
		return t.format(new Date());
	}

	/**
	 * 按预定的格式返回当前时间
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		return sdft.format(new Date());
	}

	/**
	 * 返回给定的日期时间格式对应的日期时间
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrentTime(String format) {
		SimpleDateFormat t = new SimpleDateFormat(format);
		return t.format(new Date());
	}

	/**
	 * 
	 * @return
	 */
	public static String getCurrentDateTime() {
		String format = "yyyy-MM-dd HH:mm:ss";
		return getCurrentDateTime(format);
	}

	/**
	 * 
	 * @return
	 */
	public static int getDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		return cal.get(7);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(7);
	}

	/**
	 * 
	 * @return
	 */
	public static int getDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(5);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(5);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int getMaxDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getActualMaximum(5);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getFirstDayOfMonth(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parse(date));
		cal.set(5, 1);
		return sdfd.format(cal.getTime());
	}

	/**
	 * 
	 * @return
	 */
	public static int getDayOfYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(6);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(6);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parse(date));
		return cal.get(7);
	}

	public static int getDayOfMonth(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parse(date));
		return cal.get(5);
	}

	public static int getDayOfYear(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parse(date));
		return cal.get(6);
	}

	public static String getCurrentDateTime(String format) {
		SimpleDateFormat t = new SimpleDateFormat(format);
		return t.format(new Date());
	}

	public static String toString(Date date) {
		if (date == null)
			return "";

		return sdfd.format(date);
	}

	public static String toDateTimeString(Date date) {
		if (date == null)
			return "";
		return sdfdt.format(date);
	}

	public static String toString(Date date, String format) {
		if (date == null)
			return "";
		SimpleDateFormat t = new SimpleDateFormat(format);
		return t.format(date);
	}

	public static String toTimeString(Date date) {
		if (date == null)
			return "";

		return sdft.format(date);
	}

	public static int compare(String date1, String date2) {
		return compare(date1, date2, "yyyy-MM-dd");
	}

	public static int compareTime(String time1, String time2) {
		return compareTime(time1, time2, "HH:mm:ss");
	}

	public static int compare(String date1, String date2, String format) {
		Date d1 = parse(date1);
		Date d2 = parse(date2);
		return d1.compareTo(d2);
	}

	public static int compareTime(String time1, String time2, String format) {
		String[] arr1 = time1.split(":");
		String[] arr2 = time2.split(":");
		if (arr1.length < 2)
			throw new RuntimeException("错误的时间值:" + time1);

		if (arr2.length < 2)
			throw new RuntimeException("错误的时间值:" + time2);

		int h1 = Integer.parseInt(arr1[0]);
		int m1 = Integer.parseInt(arr1[1]);
		int h2 = Integer.parseInt(arr2[0]);
		int m2 = Integer.parseInt(arr2[1]);
		int s1 = 0;
		int s2 = 0;
		if (arr1.length == 3)
			s1 = Integer.parseInt(arr1[2]);

		if (arr2.length == 3)
			s2 = Integer.parseInt(arr2[2]);

		if ((h1 < 0) || (h1 > 23) || (m1 < 0) || (m1 > 59) || (s1 < 0)
				|| (s1 > 59))
			throw new RuntimeException("错误的时间值:" + time1);

		if ((h2 < 0) || (h2 > 23) || (m2 < 0) || (m2 > 59) || (s2 < 0)
				|| (s2 > 59))
			throw new RuntimeException("错误的时间值:" + time2);

		if (h1 != h2)
			return ((h1 > h2) ? 1 : -1);

		if (m1 == m2) {
			if (s1 == s2)
				return 0;

			return ((s1 > s2) ? 1 : -1);
		}

		return ((m1 > m2) ? 1 : -1);
	}

	public static boolean isTime(String time) {
		String[] arr = time.split(":");
		if (arr.length < 2)
			return false;
		try {
			int h = Integer.parseInt(arr[0]);
			int m = Integer.parseInt(arr[1]);
			int s = 0;
			if (arr.length == 3)
				s = Integer.parseInt(arr[2]);

			if ((h >= 0) && (h <= 23) && (m >= 0) && (m <= 59) && (s >= 0)
					&& (s <= 59))
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isDate(String date) {
		String[] arr = date.split("-");
		if (arr.length < 3)
			return false;
		try {
			int y = Integer.parseInt(arr[0]);
			int m = Integer.parseInt(arr[1]);
			int d = Integer.parseInt(arr[2]);
			if ((y >= 0) && (m <= 12) && (m >= 0) && (d >= 0) && (d <= 31))
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isWeekend(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int t = cal.get(7);

		return ((t == 7) || (t == 1));
	}

	public static boolean isWeekend(String str) {
		return isWeekend(parse(str));
	}

	public static Date parse(String str) {
		if (Utils.isEmpty(str))
			return null;
		try {
			return sdfd.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parse(String str, String format) {
		if (Utils.isEmpty(str))
			return null;
		try {
			SimpleDateFormat t = new SimpleDateFormat(format);
			return t.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parseDateTime(String str) {
		if (Utils.isEmpty(str))
			return null;

		if (str.length() == 10)
			return parse(str);
		try {
			return sdfdt.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parseDateTime(String str, String format) {
		if (Utils.isEmpty(str))
			return null;
		try {
			SimpleDateFormat t = new SimpleDateFormat(format);
			return t.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date addMinute(Date date, int count) {
		return new Date(date.getTime() + 60000L * count);
	}

	public static Date addHour(Date date, int count) {
		return new Date(date.getTime() + 3600000L * count);
	}

	public static Date addDay(Date date, int count) {
		return new Date(date.getTime() + 86400000L * count);
	}

	public static Date addWeek(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(3, count);
		return c.getTime();
	}

	public static Date addMonth(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(2, count);
		return c.getTime();
	}

	public static Date addYear(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(1, count);
		return c.getTime();
	}

	/**
	 * 合并日期和时间
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	public static Calendar mergeDateTime(Date date, Time time) {
		Calendar cal = Calendar.getInstance();
		if (date != null)
			cal.setTime(date);
		if (time != null) {
			Calendar temp = Calendar.getInstance();
			temp.setTime(time);
			cal.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
			cal.set(Calendar.SECOND, temp.get(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND, temp.get(Calendar.MILLISECOND));
		}
		return cal;
	}

	/**
	 * 返回两个日期相差的天数
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int diff_in_date(Date d1, Date d2) {
		return (int) (d1.getTime() - d2.getTime()) / 86400000;
	}

	/**
	 * 获取某天开始的那一刻
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static Calendar getDateBegin(int year, int month, int date) {
		Calendar begin_time = Calendar.getInstance();
		begin_time.set(Calendar.YEAR, year);
		begin_time.set(Calendar.MONTH, month - 1);
		begin_time.set(Calendar.DATE, date);
		begin_time.set(Calendar.HOUR_OF_DAY, 0);
		begin_time.set(Calendar.MINUTE, 0);
		begin_time.set(Calendar.SECOND, 0);
		begin_time.set(Calendar.MILLISECOND, 0);
		return begin_time;
	}

	/**
	 * 清除日历的时间字段
	 * 
	 * @param cal
	 */
	public static void resetTime(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * 获取某天开始的那一刻
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateBegin(Date date) {
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(date);		
		 cal.set(Calendar.HOUR_OF_DAY, 0);
		 cal.set(Calendar.MINUTE, 0);
		 cal.set(Calendar.SECOND, 0);
		 cal.set(Calendar.MILLISECOND, 0);		
		return cal.getTime();

	}

	/**
	 * 获取某天结束那一刻
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);

		return cal.getTime();
	
	}

	/**
	 * 
	 * @param month
	 *            yyyy-MM
	 * @return
	 */
	public static Date getMonthBegin(String monStr) {
		String[] args = monStr.split("-");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.valueOf(args[0]));
		cal.set(Calendar.MONTH, Integer.valueOf(args[1]) - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	/**
	 * 每月的开始日期
	 * @param date
	 * @return
	 */
	public static Date getMonthBegin(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 
	 * @param month
	 *            yyyy-MM
	 * @return
	 */
	public static Date getMonthEnd(String monStr) {
		String[] args = monStr.split("-");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.valueOf(args[0]));
		cal.set(Calendar.MONTH, Integer.valueOf(args[1]));
		cal.set(Calendar.DAY_OF_MONTH, 0);

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	
	/**
	 * 每月最后一天
	 * @param month
	 *            yyyy-MM
	 * @return
	 */
	public static Date getMonthEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.roll(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * 一年的开始时刻
	 * 
	 * @param year
	 * @return
	 */
	public static Date getYearBegin(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 一年的结束时刻
	 * 
	 * @param year
	 * @return
	 */
	public static Date getYearEnd(int year) {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();

	}

	/**
	 * 返回日期所在的周数 每周的开始是周一，结束是周日，并且，如果一个周有4天 或以上在某个年份，那么这周就算是这年的。
	 * 
	 * @param date
	 *            yyyy-MM-dd
	 * @return
	 */
	public static int weekOfyear(String date) {
		try {
			return weekOfyear(sdfd.parse(date));
		} catch (ParseException e) {
			return weekOfyear(new Date());
		}
	}

	/**
	 * 返回日期所在的周数 每周的开始是周一，结束是周日，并且，如果一个周有4天或 以上在某个年份，那么这周就算是这年的。
	 * 
	 * @param date
	 * @return
	 */
	public static int weekOfyear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek(4);
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 取得某周的开始时间 每周的开始是周一，结束是周日，并且，如果一个周有4天或 以上在某个年份，那么这周就算是这年的。
	 * 
	 * @param year
	 * @param num
	 * @return
	 */
	public static Date firstDayOfWeek(int year, int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek(4);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.WEEK_OF_YEAR, num);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static Date[] getStartAndEnd(Date date, int type) {
		Date[] result = new Date[2];
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		Week week = new Week(date);
		switch (type) {
		case 0:
			result[0] = DateUtils.getDateBegin(date);
			result[1] = DateUtils.getDateEnd(date);
			break;
		case 1:
			result[0] = DateUtils.getDateBegin(week.getBeginDate());
			result[1] = DateUtils.getDateEnd(week.getEndDate());
			break;
		case 2:
			result[0] = DateUtils.getMonthBegin(ca.get(Calendar.YEAR) + "-"
					+ (ca.get(Calendar.MONTH) + 1));
			result[1] = DateUtils.getMonthEnd(ca.get(Calendar.YEAR) + "-"
					+ (ca.get(Calendar.MONTH) + 1));
			break;
		case 3:
			result[0] = DateUtils.getYearBegin(ca.get(Calendar.YEAR));
			result[0] = DateUtils.getYearEnd(ca.get(Calendar.YEAR));
		}
		return null;
	}
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	public static List<Long> getDateBetween2Date(String start_date,String end_date,boolean isRemoveY) {
		
		
		List<Long> result  = new ArrayList<Long>();
		try {
			start_date = start_date.trim();
			end_date = end_date.trim();
			if(Utils.isEmpty(start_date)||Utils.isEmpty(end_date)){
				return result;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			long recAfter = diffDate(sdf.parse(start_date),sdf.parse(end_date));
			if(recAfter<0){
				String temp = start_date;
				start_date = end_date;
				end_date = temp;
				recAfter = -1*recAfter;
			}
			if(isRemoveY){
				result.add(Long.parseLong(start_date.substring(4)));
			}else{
				result.add(Long.parseLong(start_date));
			}
			
			String tempStart_date = start_date;
			while(recAfter>0){
				tempStart_date = turnDate(tempStart_date, "yyyyMMdd", 1);
				if(isRemoveY){
					result.add(Long.parseLong(tempStart_date.substring(4)));
				}else{
					result.add(Long.parseLong(tempStart_date));
				}
				
				recAfter--;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result;
	}

	private static long diffDate(Date parse, Date parse2) {
		long days = 0;
		long day1 = parse.getTime();
		long day2 = parse2.getTime();
		days = (day2 - day1) / (24*3600*1000);
		    return days;
	}
	
	public static String turnDate(String showDate,String format,int interDay) throws ParseException{
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(showDate));
		cal.roll(java.util.Calendar.DAY_OF_YEAR,interDay);
		String next = sdf.format(cal.getTime());
		return next;
	}
	
	
	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(DateUtils.getMonthBegin(new Date())));
		System.out.println(sdf.format(DateUtils.getMonthEnd(new Date())));
		System.out.println(sdf.format(DateUtils.getMonthEnd("2012-01")));
		Calendar ca = Calendar.getInstance(); //2011-12-05
		ca.setTime(sdf.parse("2011-12-31"));
		//ca.add(Calendar.MONTH, 4); // 2011-04-05
		ca.roll(Calendar.MONTH, -6);
		System.out.println(sdf.format(ca.getTime()));

	}
}
