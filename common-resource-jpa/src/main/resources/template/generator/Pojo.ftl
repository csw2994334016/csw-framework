package ${package};

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
<#if hasTimestamp>
import java.sql.Timestamp;
</#if>
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>

/**
 * Created by ${author} on ${date}.
 * Description: ${description}
 */

@Data
public class ${className} implements Serializable {
<#if columns??>

    <#if strategy = 'IDENTITY'>
    @ApiModelProperty("主键ID")
    private Long id;
    </#if>
    <#if strategy = 'GUID'>
    @ApiModelProperty("主键ID")
    private String id;
    </#if>

    <#list columns as column>
        <#if column.columnName != 'id'>
    @ApiModelProperty("${column.columnComment}")
    private ${column.columnType} ${column.columnName};<#if column.columnComment != ''> // ${column.columnComment}</#if>

        </#if>
    </#list>
</#if>
}