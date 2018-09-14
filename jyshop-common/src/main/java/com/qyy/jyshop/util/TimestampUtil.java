package com.qyy.jyshop.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Timestamp工具类
 *
 */
public class TimestampUtil {

	/**
	 * 字符转时间
	 * @param strDate 时间
	 * @param formatStr 
	 */
	public static Timestamp getStrToTimestamp(String strDate,String formatStr){
		
		if(StringUtil.isEmpty(strDate))
			return null;
		if(StringUtil.isEmpty(formatStr))
			formatStr="yyyy-MM-dd";
		
		SimpleDateFormat sdf=new SimpleDateFormat(formatStr);
	
		Date date=new Date();
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		return new Timestamp(date.getTime());
	}
	
	/**
	 * 时间转字符
	 * @param ts
	 * @param formatStr
	 * @return
	 */
	public static String getTimestampToStr(Timestamp ts,String formatStr){
		
		if(ts==null)
			ts=new Timestamp(System.currentTimeMillis());
		if(StringUtil.isEmpty(formatStr))
			formatStr="yyyy-MM-dd";
		
		DateFormat sdf = new SimpleDateFormat(formatStr);   
		return sdf.format(ts);

	}
	
	public static Timestamp getNowTime(){
        
        return new Timestamp(System.currentTimeMillis());
    }
	
	public static Timestamp getAfterNDays(Date date, int n) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);
        Date time = calendar.getTime();
        Timestamp timestamp = new Timestamp(time.getTime());
        return timestamp;
    }

	public static void main(String[] args){
		
		System.out.println(TimestampUtil.getTimestampToStr(null,"yyyyMMdd"));
	}
}
