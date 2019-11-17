package com.three.points.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DateVo {

    private Date startD;

    private Date endD;

    public DateVo(Date startD, Date endD) {
        this.startD = startD;
        this.endD = endD;
    }
}
