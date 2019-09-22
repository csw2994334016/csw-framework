package com.three.develop.param;

import lombok.*;

import java.util.List;

/**
 * Created by csw on 2019/09/20.
 * Description:
 */
@Builder
@Data
public class GeneratorParam {

    private GenConfig genConfig;

    private List<ColumnInfo> columnInfoList;

    private List<String> templateList;

}
