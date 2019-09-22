package com.three.commonjpa.script.param;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

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
