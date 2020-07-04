package ${package};

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
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

@Getter
@Setter
@Entity
@Table(name = "${tableName}")
@EntityListeners(AuditingEntityListener.class)
public class ${className} implements Serializable {
<#if columns??>

    <#if strategy = 'IDENTITY'>
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键ID")
    private Long id;
    </#if>
    <#if strategy = 'GUID'>
    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    @ApiModelProperty("主键ID")
    private String id;
    </#if>


    <#list columns as column>
        <#if column.columnName != 'id'>
            <#if column.columnKey?? && column.columnKey = 'text'>
    @Lob
            </#if>
            <#if column.columnKey?? && column.columnKey = 'UNI' || column.isNullable == false || column.changeColumnType??>
    @Column(name = "${column.changeColumnName}"<#if column.columnKey?? && column.columnKey = 'UNI'>, unique = true</#if><#if column.isNullable == false>, nullable = false</#if><#if column.columnKey?? && column.columnKey = 'text'>, columnDefinition = "text comment '${column.columnComment}'"<#else>, columnDefinition = "${column.changeColumnType}<#if column.columnLength??>(${column.columnLength})</#if><#if column.defaultValue??> default ${column.defaultValue}</#if> comment '${column.columnComment}'"</#if>)
            </#if>
            <#if column.columnName = 'createDate'>
    @CreatedDate
            </#if>
            <#if column.columnName = 'updateDate'>
    @LastModifiedDate
            </#if>
    @ApiModelProperty("${column.columnComment}")
    private ${column.columnType} ${column.columnName}<#if column.defaultValue??> = ${column.defaultValue}</#if>;

        </#if>
    </#list>
</#if>
}