package com.hyx.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

public abstract class DateUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    private static final Map<String,LinkedList<SimpleDateFormat>> simpleDateFormats=new ConcurrentSkipListMap<>();

	private static long stime=System.currentTimeMillis()/1000;

    private final static String[] dateFormats = new String[]{
    		"yyyy-MM-dd HH:mm:ss.S","yyyy-MM-dd HH:mm:ss.SS","yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH", "yyyy-MM-dd", "yyyy-MM",
    		"yyyy/MM/dd HH:mm:ss.S","yyyy/MM/dd HH:mm:ss.SS","yyyy/MM/dd HH:mm:ss.SSS", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH", "yyyy/MM/dd", "yyyy/MM", 
    		"yyyy.MM.dd HH:mm:ss.S","yyyy.MM.dd HH:mm:ss.SS","yyyy.MM.dd HH:mm:ss.SSS", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM.dd HH", "yyyy.MM.dd", "yyyy.MM"};


    private final static SimpleDateFormat yMdHm = getDateFormat("y年M月d日H时m分");
    private final static SimpleDateFormat ymd = getDateFormat("y年M月d日");
    private final static SimpleDateFormat md = getDateFormat("M月d日");
    
    static {
    	register(dateFormats);
    }
    
    public static final void register(String ... patterns) {
    	for (String pattern : patterns) {
    		if(pattern!=null) {
    			pattern=pattern.trim();
    			releaseDateFormat(pattern, getDateFormat(pattern));
    		}
		}
	}
    /**
     * 从池中获取一个时间转化对象
     * @param pattern
     * @return
     */
    private static synchronized SimpleDateFormat getDateFormat(String pattern) {
    	if(simpleDateFormats.containsKey(pattern)&&!simpleDateFormats.get(pattern).isEmpty()) {
    		return simpleDateFormats.get(pattern).pop();
    	}
    	return new SimpleDateFormat(pattern);
	}
    
    /**
     * 释放一个时间转化对象到池中
     * @param pattern
     * @param dateFormat
     */
    private  static synchronized void releaseDateFormat(String pattern,SimpleDateFormat dateFormat) {
    	if(!simpleDateFormats.containsKey(pattern)) {
    		simpleDateFormats.put(pattern, new LinkedList<>());
    	}
    	simpleDateFormats.get(pattern).push(dateFormat);
	}
    /**
     * 获取date当天的开始时间00:00:00
     *
     * @param date
     * @return
     */
    public static final Date initToDayBegin(Date date) {
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    /**
     * 获取date当月的开始时间00:00:00
     *
     * @param date
     * @return
     */
    public static final Date initToMonthBegin(Date date) {
        if (date == null)
            return null;
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
     * 获取date当天的结束时间23:59:59
     *
     * @param date
     * @return
     */
    public static final Date initToDayEnd(Date date) {
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 时间格式化，支持常用的时间格式
     *
     * @param source
     * @return
     */
    public static final Date parse(String source) {
        if (StringUtils.isEmpty(source)) {
        	return null;
        }
        source=source.trim();
       Set<String> keys = simpleDateFormats.keySet();
       for (String pattern : keys) {
	    	if(		pattern.length()==source.length()
	    			&&pattern.indexOf('-')==source.indexOf('-')
	    			&&pattern.indexOf('.')==source.indexOf('.')
	    			&&pattern.indexOf('/')==source.indexOf('/')) {
    			SimpleDateFormat dateFormat = getDateFormat(pattern);
    			try {
    				return dateFormat.parse(source);
				}catch (Exception e) {
	    			logger.debug("参数{}转化为日期{}失败", source, pattern);
	    		} finally {
					releaseDateFormat(pattern, dateFormat);
				}
	    	}
        }
        Long time= NumberUtils.parseLong(source);
        if(time==null) {
        	  return null;
        }
		if (stime < time) {
			return new Date(time);
		}else{
			return new Date(time * 1000);
		}
    }


    public static final String format(Integer dateInt, String pattern) {
        return format(int2Date(dateInt), pattern);
    }

    public static final String formatByLong(Long dateLong, String pattern){
        if(null == dateLong){
            return null;
        }
        return format(int2Date((int)(dateLong / 1000)), pattern);
    }

    /**
     * 以指定的格式格式化时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static final String format(Date date, String pattern) {
        if (date == null)
            return null;
        if (StringUtils.isEmpty(pattern))
            return format(date);
        SimpleDateFormat dateFormat = getDateFormat(pattern);
        try {
        	return dateFormat.format(date);
		} finally {
			releaseDateFormat(pattern, dateFormat);
		}
    }

    /**
     * 格式化时间 ，时间格式 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static final String format(Date date) {
        if (date == null)
            return null;
        String pattern="yyyy-MM-dd";
        SimpleDateFormat dateFormat = getDateFormat(pattern);
        try {
        	return dateFormat.format(date);
		} finally {
			releaseDateFormat(pattern, dateFormat);
		}
    }


    /**
     * 把Date对象转化为Integer并且除以1000
     *
     * @param date
     * @return
     */
    public static final Integer date2Int(Date date) {
        if (null == date) {
            return null;
        }
        return (int) (date.getTime() / 1000);
    }

    /**
     * 把Date对象转化为Integer并且除以1000
     *
     * @param dateInt
     * @return
     */
    public static final Date int2Date(Integer dateInt) {
        if (null == dateInt) {
            return null;
        }
        return new Date(new BigInteger(dateInt.toString()).multiply(new BigInteger("1000")).longValue());
    }

    /**
     * 获取传入时间的年份
     *
     * @param date
     * @return
     */
    public static final int getYearByDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //获取年份
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取year年前(后)的时间
     * year > 0 后
     * year < 0 前
     *
     * @param date
     * @return
     */
    public static final Date addYear(Date date, int year) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, year);
        return c.getTime();
    }

    /**
     * 获取month年前(后)的时间
     * month > 0 后
     * month < 0 前
     *
     * @param date
     * @return
     */
    public static final  Date addMonth(Date date, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, month);
        return c.getTime();
    }

    public static Date addSecond(Date date, long second){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, (int)second);
        return calendar.getTime();
    }

    /**
     * 获取周得第一天
     *
     * @param week
     * @return
     */
    @SuppressWarnings("deprecation")
    public static final Date getWeekStart(Integer week) {
        Date d = new Date();
        int date = d.getDate() - d.getDay() + (week == null ? 0 : week * 7);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.DATE, date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取周得最后一天
     *
     * @param week
     * @return
     */
    @SuppressWarnings("deprecation")
    public static final Date getWeekEnd(Integer week) {
        Date d = new Date();
        int date = d.getDate() - d.getDay() + (week == null ? 0 : week * 7) + 7;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.DATE, date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 99);
        return cal.getTime();
    }

    /**
     * 距离当前的时间差，以刚刚，XX分钟前，XX小时前，昨天，前天，XX天前，X月X日，XXXX年X月X日
     *
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String beforeNow(Date date) {
        if (date == null) {
            return null;
        }
        long between_count = (System.currentTimeMillis() - date.getTime()) / 1000;
        if (between_count < -60) {
            return yMdHm.format(date);
        }
        if (between_count < 60) {
            return "刚刚";
        }
        if ((between_count = between_count / 60) < 60) {
            return between_count + "分钟前";
        }
        if ((between_count = between_count / 60) < 24) {
            return between_count + "小时前";
        }
        if ((between_count = between_count / 24) == 1) {
            return "昨天";
        }
        if (between_count == 2) {
            return "前天";
        }
        if (between_count < 28) {
            return between_count + "天前";
        }
        if (date.getYear() == new Date().getYear()) {
            return md.format(date);
        } else {
            return ymd.format(date);
        }
    }

    /**
     * 获取间隔天数
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static final long getIntervalDays(long beginTime, long endTime) {
        return (endTime - beginTime) / (3600 * 24);
    }

    /**
     * 时间增加天数
     *
     * @param days
     * @return
     */
    public static final String addDay(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return format(c.getTime(), "yyyyMMdd");
    }
    /**
     * 获取时间戳
     *
     * @param days
     * @return
     * @throws ParseException
     */
    public static final long getTimestamp(int days) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTimeInMillis() / 1000;
    }
    /**
     * 计算间隔天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final int intervalDays(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate).getDays();
    }
    
    public static void main(String[] args) {
		System.err.println(parse("2019-09-08 22:12:30.200"));
	}
}
