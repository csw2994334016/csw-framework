package com.three.develop.param;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by csw on 2019/09/19.
 * Description:
 */
@Builder
@Data
public class GenConfig {

    /**
     * 后端代码文件路径
     **/
    private String adminPath;

    /**
     * 作者
     **/
    private String author;

    /**
     * 包路径
     **/
    @NotBlank
    private String packPath;

    /**
     * 模块名/父级菜单
     **/
    private String moduleName;

    /**
     * 菜单名称
     **/
    private String menuName;

    /**
     * 接口路径
     */
    private String controllerUrl;

    /**
     * 实体类名
     */
    private String className;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 是否覆盖
     **/
    @Builder.Default
    private Boolean cover = Boolean.TRUE;
}
