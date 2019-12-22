package com.three.user.vo;

import io.swagger.annotations.ApiModelProperty;
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
public class OrgVo {

    private String title;

    private String id;

    private String key;

    private String parentId;

    private String parentName;

    @ApiModelProperty("组织机构编码")
    private String orgCode; // 组织机构编码

    @Builder.Default
    private List<OrgVo> children = new ArrayList<>();

    private Integer sort;
}
