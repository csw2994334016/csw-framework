package ${package}.entity;

import com.three.common.enums.StatusEnum;
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
 * Description: ${menuName}
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
            <#if column.columnKey = 'text'>
    @Lob
            </#if>
            <#if column.columnKey = 'UNI' || column.isNullable == false || column.changeColumnType??>
    @Column(name = "${column.changeColumnName}"<#if column.columnKey = 'UNI'>, unique = true</#if><#if column.isNullable == false>, nullable = false</#if><#if column.columnKey = 'text'>, columnDefinition = "text comment '${column.columnComment}'"</#if><#if column.changeColumnType??>, columnDefinition = "${column.changeColumnType}<#if column.columnLength??>(${column.columnLength})</#if> comment '${column.columnComment}'"</#if>)
            </#if>
    @ApiModelProperty("${column.columnComment}")
    private ${column.columnType} ${column.columnName};<#if column.columnComment != ''> // ${column.columnComment}</#if>

        </#if>
    </#list>

    @Column(name = "remark", columnDefinition = "varchar(500) comment '描述/备注'")
    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

    @Column(nullable = false, columnDefinition = "int(2) default 1 comment '记录状态：1=正常；2=锁定；3=删除'")
    @ApiModelProperty("记录状态：1=正常；2=锁定；3=删除")
    private Integer status = StatusEnum.OK.getCode(); // 记录状态：1=正常；2=锁定；3=删除

    @CreatedDate
    @ApiModelProperty("创建时间")
    private Date createDate; // 创建时间

    @LastModifiedDate
    @ApiModelProperty("修改时间")
    private Date updateDate; // 修改时间
</#if>


}