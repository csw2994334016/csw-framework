package com.three.points.service;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

public class Test {

    public static void main(String[] args) {
        Date curDate = new Date();
        Date stD = DateUtil.beginOfDay(curDate);
        Date etD = DateUtil.endOfDay(curDate);
        Date stW = DateUtil.beginOfWeek(curDate);
        Date etW = DateUtil.endOfWeek(curDate);
        Date stM = DateUtil.beginOfMonth(curDate);
        Date etM = DateUtil.endOfMonth(curDate);
        System.out.println(stD);
        System.out.println(etD);
        System.out.println(stW);
        System.out.println(etW);
        System.out.println(stM);
        System.out.println(etM);

    }
}
