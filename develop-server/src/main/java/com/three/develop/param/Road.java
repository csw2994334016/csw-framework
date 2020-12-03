package com.three.develop.param;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Road {

    private long time;

    private Point point;

    private double duration;

    private double rotate;
}
