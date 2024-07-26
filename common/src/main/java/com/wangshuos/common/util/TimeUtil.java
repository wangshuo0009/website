package com.sg.bjftviewprotect.system.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName TimeUtil
 * @Author wangshuo
 * @Date 2024/5/27 09:06
 * @Version 1.0
 **/
public class TimeUtil {
    public static DateTimeFormatter yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * @param dateTime 指定时间
     * 获取某个时间的当年开始时间
     */
    public static LocalDateTime getStartDateTimeOfYear(LocalDateTime dateTime) {
        return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), 1, 0, 0);
    }

    /**
     * 日期转时间
     * @param dateTime 需要转换的时间 yyyy-MM-dd HH:mm:ss
     */
    public static LocalDateTime parse(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime,formatter);
    }
    /**
     * 日期转时间
     * @param dateTime 需要转换的时间 yyyy-MM-dd
     */
    public static LocalDateTime parseYYYYMMDD(String dateTime) {
        dateTime = dateTime.substring(0, 10) + " 00:00:00";
        return parse(dateTime);
    }


}
