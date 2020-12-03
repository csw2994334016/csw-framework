package com.three.develop.param;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class Car {

    private String agvId;

    private List<Road> roadList = new ArrayList<>();
}
