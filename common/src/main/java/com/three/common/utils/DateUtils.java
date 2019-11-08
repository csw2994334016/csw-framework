package com.three.common.utils;

import java.util.Date;

public class DateUtils {

    /**
     * 给定时间，所在月份第一天
     *
     * @param date
     * @return
     */
    public static String getMonthFirstDay(Date date) {
        int year = cn.hutool.core.date.DateUtil.year(date);
        int month = cn.hutool.core.date.DateUtil.month(date) + 1;
        return year + "-" + month + "-01";
    }
}
