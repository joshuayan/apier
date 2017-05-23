package cn.apier.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by yanjunhua on 15/12/3.
 */
public abstract class DateTimeUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeUtil.class);


    public static Date now()
    {
        return new Date(System.currentTimeMillis());
    }

    public static Date buildBeginOfDay(Date date)
    {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date result = calendar.getTime();
        LOGGER.debug("start of date [{}]:{}", date, result);
        return result;
    }

    public static Date buildEndOfDay(Date date)
    {
        Date startOfDate = buildBeginOfDay(date);
        long newTime = startOfDate.getTime() + 24 * 60 * 60 * 1000 - 1;
        Date result = new Date(newTime);
        LOGGER.debug("end of date [{}]:{}", date, result);
        return result;
    }

    public static void main(String[] args)
    {
        Date date = new Date(System.currentTimeMillis());
        System.out.println("start of date :" + date + "," + formatDate(buildBeginOfDay(date), "yyyy-MM-dd HH:mm:ss"));
        System.out.println("end of date :" + date + "," + formatDate(buildEndOfDay(date), "yyyy-MM-dd HH:mm:ss"));
        LOGGER.debug("start of date [{}]:{}", date, formatDate(buildBeginOfDay(date), "yyyy-MM-dd HH:mm:ss"));
        LOGGER.debug("end of date [{}]:{}", date, formatDate(buildEndOfDay(date), "yyyy-MM-dd HH:mm:ss"));

    }

    public static String formatDate(Date date, String pattern)
    {
        Objects.requireNonNull(pattern);
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String result = dateFormat.format(date);
        LOGGER.debug("format date [{}] with pattern [{}]:{}", date, pattern, result);
        return result;
    }


    public static Date parse(String dateStr, String pattern)
    {
        Date result = null;
        DateFormat dateFormat = new SimpleDateFormat(pattern);

        try
        {
            result = dateFormat.parse(dateStr);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public static Date buildNewDate(Date date, int days)
    {
        return new Date(date.getTime() + days * 24 * 60 * 60 * 1000);
    }
}
