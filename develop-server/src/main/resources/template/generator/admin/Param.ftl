package ${package}.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
<#if hasDate>
import java.util.Date;
</#if>

/**
 * Created by ${author} on ${date}.
 * Description:
 */
@Builder
@Data
public class ${className}Param {

    private ${pkColumnType} id;

<#if columns??>
    <#list columns as column>
        <#if column.columnName != 'id'>
            <#if column.isNullable == false>
    @NotBlank(message = "${column.columnComment}不可以为空")
            </#if>
    @ApiModelProperty("${column.columnComment}")
    private ${column.columnType} ${column.columnName};<#if column.columnComment != ''> // ${column.columnComment}</#if>
        </#if>

    </#list>
</#if>

    private String remark;

}