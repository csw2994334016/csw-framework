package com.three.points.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateUtil;
import com.three.common.utils.StringUtil;
import com.three.points.vo.DateVo;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Test {

    public static void main(String[] args) {
        Date curDate = new Date();
        Date stD = DateUtil.beginOfDay(curDate);
        Date etD = DateUtil.endOfDay(curDate);
        Date stW = DateUtil.beginOfWeek(curDate);
        Date etW = DateUtil.endOfWeek(curDate);
        Date stM = DateUtil.beginOfMonth(curDate);
        Date etM = DateUtil.endOfMonth(curDate);
        Date stY = DateUtil.beginOfYear(curDate);
        Date etY = DateUtil.endOfYear(curDate);
        System.out.println(stD);
        System.out.println(etD);
        System.out.println(stW);
        System.out.println(etW);
        System.out.println(stM);
        System.out.println(etM);
        System.out.println(stY);
        System.out.println(etY);
        System.out.println(StringUtil.getStrToIdSet1("222"));

//        String statisticsFlag = "2";
//        Date date = new Date();
//        Date st, et;
//        Map<String, DateVo> dayMap = new LinkedHashMap<>();
//        Map<String, Integer> awardValueMap = new LinkedHashMap<>();
//        Map<String, Integer> deductValueMap = new LinkedHashMap<>();
//        Map<String, Integer> empCountValueMap = new LinkedHashMap<>();
//        if ("2".equals(statisticsFlag)) { // 月统计
//            st = DateUtil.beginOfYear(date);
//            et = DateUtil.endOfYear(date);
//            // 一年有多少个月
//            for (int i = DateUtil.month(st); i <= DateUtil.month(et); i++) {
//                Date date1 = DateUtil.offsetMonth(st, i);
//                DateVo dateVo = new DateVo(DateUtil.beginOfMonth(date1), DateUtil.endOfMonth(date1));
//                dayMap.put(String.format("%02d", i + 1), dateVo);
//                awardValueMap.put(String.format("%02d", i + 1), 0);
//                deductValueMap.put(String.format("%02d", i + 1), 0);
//                empCountValueMap.put(String.format("%02d", i + 1), 0);
//            }
//        } else if ("3".equals(statisticsFlag)) { // 年统计
//            st = DateUtil.parse("2019-01-01 00:00:00");
//            et = DateUtil.endOfYear(date);
//            // 系统使用了几年
//            for (int i = DateUtil.year(st); i <= DateUtil.year(et); i++) {
//                Date date1 = DateUtil.parse(i + "-01-01 00:00:00");
//                DateVo dateVo = new DateVo(DateUtil.beginOfYear(date1), DateUtil.endOfYear(date1));
//                dayMap.put(i + "", dateVo);
//                awardValueMap.put(i + "", 0);
//                deductValueMap.put(i + "", 0);
//                empCountValueMap.put(i + "", 0);
//            }
//        } else { // 默认日统计
//            st = DateUtil.beginOfMonth(date);
//            et = DateUtil.endOfMonth(date);
//            // 一个月有多少天
//            for (int i = DateUtil.dayOfMonth(st); i <= DateUtil.dayOfMonth(et); i++) {
//                Date date1 = DateUtil.offsetDay(st, i - 1);
//                DateVo dateVo = new DateVo(DateUtil.beginOfDay(date1), DateUtil.endOfDay(date1));
//                dayMap.put(String.format("%02d", i), dateVo);
//                awardValueMap.put(String.format("%02d", i), 0);
//                deductValueMap.put(String.format("%02d", i), 0);
//                empCountValueMap.put(String.format("%02d", i), 0);
//            }
//        }

        System.out.println(StringUtil.getStrToIdSet1("222"));

    }
}
