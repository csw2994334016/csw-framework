package com.three.user.vo;

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

    private String parentId;

    @Builder.Default
    private List<OrgVo> children = new ArrayList<>();
}
