package cn.suse.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_FORMAT_YYYY_MM = "yyyy-MM";
	public static final String PATTERN_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String PATTERN_FORMAT_YYYYMMDD = "yyyy/MM/dd";
	public static final String PATTERN_FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String PATTERN_STR_YYYYMM = "yyyyMM";
	public static final String PATTERN_STR_YYYYMMDD = "yyyyMMdd";
	public static final String PATTERN_STR_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String PATTERN_STR_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	public static final String PATTERN_YMD_CHINA = "yyyy年M月d日";
	public static final String PATTERN_YMD_CHINA_DOUBLE = "yyyy年MM月dd日";
	//线程不安全，不建议这么用，枷锁效率略低，换成下面的方式
	//private static SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DEFAULT);
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PATTERN_DEFAULT);

	public static String format(LocalDateTime date) throws Exception {
	    return dtf.format(date);
	}

	public static LocalDateTime parse(String strDate) {
	    return LocalDateTime.parse(strDate, dtf);
	}
	
	public static LocalDateTime date2LocalDateTime(Date date){
		return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.systemDefault());
	}
	
	public static Date localDateTime2Date(LocalDateTime dateTimeFromDate){
		return Date.from(dateTimeFromDate.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static LocalDateTime calendar2LocalDateTime(Calendar calendar){
		return LocalDateTime.ofInstant(calendar.toInstant(), ZoneOffset.systemDefault());
	}
	
	public static boolean compare(LocalDateTime dateTimeFromDate1, LocalDateTime dateTimeFromDate2){
		return dateTimeFromDate1.isBefore(dateTimeFromDate2);
		
	}
	
	public static long interval(LocalDateTime dateTimeFromDate1, LocalDateTime dateTimeFromDate2){
		Duration duration = Duration.between(dateTimeFromDate1, dateTimeFromDate2);
		return duration.toDays();
	}
	
	public static LocalDateTime add(LocalDateTime dateTimeFromDate, long days){
		return dateTimeFromDate.plusDays(days);
	}
	
	public static LocalDateTime add(LocalDateTime dateTimeFromDate, long count, ChronoUnit unit){
		return dateTimeFromDate.plus(count, unit);
	}
	
	public static LocalDateTime add(LocalDateTime dateTimeFromDate, int years, int months, int days){
		return dateTimeFromDate.plus(Period.of(years, months, days));
	}
	
	public static LocalDateTime reduce(LocalDateTime dateTimeFromDate, long days){
		return dateTimeFromDate.minusDays(days);
	}
	
	public static LocalDateTime reduce(LocalDateTime dateTimeFromDate, long count, ChronoUnit unit){
		return dateTimeFromDate.minus(count, unit);
	}
	
	public static LocalDateTime reduce(LocalDateTime dateTimeFromDate, int years, int months, int days){
		return dateTimeFromDate.minus(Period.of(years, months, days));
	}
	
	public static boolean isLeapYear(LocalDate dateFromDate){
	    return dateFromDate.isLeapYear();
	}
}
