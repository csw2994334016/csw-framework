package com.three.points.vo;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2019/09/26.
 * Description:
 */
@Data
@Builder
public class EventTypeVo {

    private String title;

    private String id;

    private String parentId;

    private String parentName;

    @Builder.Default
    private List<EventTypeVo> children = new ArrayList<>();

    private Integer sort;

    private String remark;
}
