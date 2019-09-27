package com.three.resource_jpa.jpa.script.param;

import lombok.Builder;
import lombok.Data;

/**
 * Created by csw on 2019/09/07.
 * Description:
 */
@Builder
@Data
public class ScriptParam {

    private Long id;

    private String name;

    private String code;

    private String plainTxt;

    private String version;

    private String remark;
}
