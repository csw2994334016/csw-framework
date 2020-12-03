package com.three.develop.param;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Point {

    private double x;

    private double y;

    private double z;
}
