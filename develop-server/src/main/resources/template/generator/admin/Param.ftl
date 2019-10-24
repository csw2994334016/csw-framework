package ${package}.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
<#if hasDate>
import java.util.Date;
</#if>

/**
 * Created by ${author} on ${date}.
 * Description:
 */
@Data
public class ${className}Param {

    private ${pkColumnType} id;

<#if columns??>
    <#list columns as column>
        <#if column.columnName != 'id'>
            <#if column.isNullable == false>
                <#if column.columnType == 'Integer' || column.columnType == 'Long' || column.columnType == 'Double' || column.columnType == 'Date'>
    @NotNull(message = "${column.columnComment}不可以为空")
                </#if>
                <#if column.columnType == 'String'>
    @NotBlank(message = "${column.columnComment}不可以为空")
                </#if>
            </#if>
    @ApiModelProperty("${column.columnComment}")
    private ${column.columnType} ${column.columnName};<#if column.columnComment != ''> // ${column.columnComment}</#if>
        </#if>

    </#list>
</#if>

    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

}