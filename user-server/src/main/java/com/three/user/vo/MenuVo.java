package com.three.user.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2019/04/05.
 * Description:
 */
@Data
public class MenuVo {

    private String id;
    private String parentId;
    private String name;
    private String icon;
    private String url;
    private Integer sort;
    private List<MenuVo> subMenus = new ArrayList<>();
}