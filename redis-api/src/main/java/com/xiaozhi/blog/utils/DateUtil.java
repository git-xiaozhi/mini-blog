package com.xiaozhi.blog.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.joda.time.DateTime;



/**
 * @author Steven
 *
 */
public class DateUtil {

	/**
	 * 
	 */
	
	private final static TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");
	
	public final static String DATETIME = "yyyy-MM-dd HH:mm:ss";
	
	public final static String DATE = "yyyy-MM-dd";
	
	public final static String DATETIME2 = "yyyy-MM-dd HH:mm";
    
	private DateUtil() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * 将字符时间解析成Date日期类型
	 * @param timeString
	 * @return
	 */
	public static Date parseTime(String timeString)
	{
		DateTime dateTime = new DateTime(timeString.replace(" ", "T"));
		return dateTime.toDate();
	}
	
	/**
	 * 按格式类型解析时间为字符
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String formatTime(Date time,String pattern)
	{
		return new DateTime(time).toString(pattern);
	}
	
	/**
	 * 当天指定格式的日期字符串
	 * @param format 比如:“yyyy-MM-dd”
	 * @return 
	 */
	public static String getCurrentDate(String format) {
		return new DateTime().toString(format);
	}
	
	/**
	 * 计算一个相对于今天的日期。day大于0，今天以后的日期；否则，今天以前的日期
	 * 返回的Date数据，包括时、分、秒
	 * @param day 距离今天有几天
	 * @return Date
	 */
	public static Date addCurrentDay(int day) {		
		return new DateTime().plusDays(day).toDate();
	}
	
	/**
	 * 计算一个相对于现在时间的日期。hour大于0，今天以后的日期；否则，今天以前的日期
	 * 返回的Date数据，包括时、分、秒
	 * @param day 距离现在有多少小时
	 * @return Date
	 */
	public static Date addCurrentDateHour(int hour) {
		return new DateTime().plusHours(hour).toDate();
	}
	
	/**
	 * 根据生日获取当前的年龄
	 * @param birth
	 * @return
	 */
	public static int getAgeByBirth(String birth){
		DateTime birthday = new DateTime(birth.replace(" ", "T"));
		DateTime today = new DateTime();
		return today.getYear()-birthday.getYear();
	}
	
	
    //得到日期格式当天的显示 HH：mm;当天以前的显示 MM:dd
	public static String getTimeByDate(Date date) {
		Calendar today = Calendar.getInstance();
		today.set(today.HOUR_OF_DAY, 0);
		today.set(today.MINUTE, 0);
		today.set(today.SECOND, 0);

		Calendar calendardate = Calendar.getInstance();
		calendardate.setTime(date);
		
		Boolean bb = calendardate.before(today);
		String dateString = " ";
		if (bb) {
			int mouth = calendardate.get(calendardate.MONTH) + 1;
			//System.out.println(getFormatDate(mouth));
			dateString = getFormatDate(mouth) + "-"+ getFormatDate(calendardate.get(calendardate.DAY_OF_MONTH));
		} else
			dateString = getFormatDate(calendardate.get(calendardate.HOUR_OF_DAY)) + ":"+ getFormatDate(calendardate.get(calendardate.MINUTE));
		return dateString;
	}
	
	private static String getFormatDate(int date){
		return date<10?"0"+date:date+"";
	}	
	
	
	
	//得到两个日期间隔的天数
	public static int getDaysBetween(String beginDate, String endDate)
			throws ParseException

	 { 

	      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	      Date bDate = format.parse(beginDate.toString());

	      Date eDate = format.parse(endDate.toString());

	      Calendar d1 = new GregorianCalendar(); 

	      d1.setTime(bDate); 

	      Calendar d2 = new GregorianCalendar();

	     d2.setTime(eDate);      

	     int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);      

	     int y2 = d2.get(Calendar.YEAR);       

	     if (d1.get(Calendar.YEAR) != y2)

	     {           

	            d1 = (Calendar) d1.clone();           

	           do   {               

	                       days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);//得到当年的实际天数                 

	                      d1.add(Calendar.YEAR, 1);           

	         }    while (d1.get(Calendar.YEAR) != y2); 

	    }      

	     return days;
	 }
	
	
	public static void main(String[] args) {		
		//String today = DateUtil.getTimeByDate(DateUtil.addCurrentDay(4));
		//System.out.println(today);
		//int a =  new BigDecimal(7/4.0).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		//System.out.println(a);
	}
}

