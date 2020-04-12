package ${package}.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
<#if hasDate>
import java.util.Date;
</#if>

/**
 * Created by ${author} on ${date}.
 * Description:
 */
@Data
public class ${className}Param {

    @ApiModelProperty("主键ID")
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

    @Size(max = 500, message = "描述/备注不超过500个字")
    @ApiModelProperty("描述/备注(不超过500个字)")
    private String remark; // 描述/备注

}