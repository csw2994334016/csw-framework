package com.three.user.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2019/04/05.
 * Description:
 */
@Data
public class AuthTreeVo {

    private String id;
    private String parentId;
    private String title;
    private Boolean expand = Boolean.FALSE;
    private Boolean checked = Boolean.FALSE;
    private Boolean disabled = Boolean.FALSE;
    private List<AuthTreeVo> children = new ArrayList<>();

    private Integer sort;
}