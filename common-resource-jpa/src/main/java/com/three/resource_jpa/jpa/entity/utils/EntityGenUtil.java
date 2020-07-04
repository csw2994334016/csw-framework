package com.three.resource_jpa.jpa.entity.utils;


import cn.hutool.extra.template.*;
import com.three.common.utils.StringUtil;
import com.three.resource_jpa.jpa.entity.param.ColumnInfo;
import com.three.resource_jpa.jpa.entity.param.GenConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by csw on 2019/09/19.
 * Description:
 */
@Slf4j
public class EntityGenUtil {

    private static final String TIMESTAMP = "Timestamp";

    private static final String BIG_DECIMAL = "BigDecimal";

    private static final String DATE = "Date";

    public static final String PK = "PRI";

    private static final String STRING = "String";

    private static final String INTEGER = "Integer";

    private static final String LONG = "Long";

    private static final String DOUBLE = "Double";

    public static String generateCode(GenConfig genConfig, List<ColumnInfo> columnInfoList, String templateName) {

        Map<String, Object> map = generateCodeMap(genConfig, columnInfoList);

        // 加载模板
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template/generator/", TemplateConfig.ResourceMode.CLASSPATH));

        // 生成实体
        Template template = engine.getTemplate(templateName + ".ftl");
        return template.render(map);
    }

    /**
     * 生成代码所需属性Map
     *
     * @param genConfig      生成代码的参数配置，如包路径，作者
     * @param columnInfoList 表元数据
     */
    private static Map<String, Object> generateCodeMap(GenConfig genConfig, List<ColumnInfo> columnInfoList) {
        Map<String, Object> map = new HashMap<>();
        map.put("package", genConfig.getPackPath());
        map.put("description", genConfig.getDescription());
        map.put("moduleName", genConfig.getModuleName());
        map.put("menuName", genConfig.getMenuName());
        map.put("controllerUrl", genConfig.getControllerUrl());
        map.put("author", genConfig.getAuthor());
        map.put("date", LocalDate.now().toString());
        map.put("tableName", genConfig.getTableName());
        map.put("className", genConfig.getClassName());
        map.put("changeClassName", StringUtil.toLowerCaseFirstOne(genConfig.getClassName()));
        map.put("hasTimestamp", false);
        map.put("hasBigDecimal", false);
        map.put("hasQuery", false);
        map.put("hasDate", false);
        map.put("strategy", "IDENTITY");

        List<Map<String, Object>> columns = new ArrayList<>();
        List<Map<String, Object>> queryColumns = new ArrayList<>();
        for (ColumnInfo column : columnInfoList) {
            Map<String, Object> listMap = new HashMap<>();
            listMap.put("columnComment", column.getColumnComment());
            listMap.put("columnKey", column.getColumnKey());
            String changeColumnName = StringUtil.toUnderScoreCase(column.getColumnName());
            if (PK.equals(column.getColumnKey())) {
                map.put("pkColumnType", column.getColumnType());
                if (STRING.equals(column.getColumnType())) {
                    map.put("strategy", "GUID");
                }
            }
            if (TIMESTAMP.equals(column.getColumnType())) {
                map.put("hasTimestamp", true);
            }
            if (BIG_DECIMAL.equals(column.getColumnType())) {
                map.put("hasBigDecimal", true);
            }
            if (DATE.equals(column.getColumnType())) {
                map.put("hasDate", true);
            }
            // 字段默认长度
            if (STRING.equals(column.getColumnType()) && !"text".equals(column.getColumnKey()) && !PK.equals(column.getColumnKey())) {
                listMap.put("columnLength", 255);
            }
            if (INTEGER.equals(column.getColumnType()) && !"text".equals(column.getColumnKey()) && !PK.equals(column.getColumnKey())) {
                listMap.put("columnLength", 11);
            }
            if (LONG.equals(column.getColumnType()) && !"text".equals(column.getColumnKey()) && !PK.equals(column.getColumnKey())) {
                listMap.put("columnLength", 20);
            }
            // 字段长度
            if (column.getColumnLength() != null && !"text".equals(column.getColumnKey()) && !PK.equals(column.getColumnKey())) {
                listMap.put("columnLength", column.getColumnLength());
            }
            listMap.put("columnType", column.getColumnType());
            if (STRING.equals(column.getColumnType())) {
                listMap.put("changeColumnType", "varchar");
            }
            if (INTEGER.equals(column.getColumnType())) {
                listMap.put("changeColumnType", "int");
            }
            if (LONG.equals(column.getColumnType())) {
                listMap.put("changeColumnType", "bigint");
            }
            if (DATE.equals(column.getColumnType())) {
                listMap.put("changeColumnType", "datetime");
            }
            if (DOUBLE.equals(column.getColumnType())) {
                listMap.put("changeColumnType", "double");
            }
            listMap.put("columnName", column.getColumnName());
            listMap.put("isNullable", column.getIsNullable());
            listMap.put("columnShow", column.getColumnShow());
            listMap.put("changeColumnName", changeColumnName);
            listMap.put("defaultValue", column.getDefaultValue());

            // 判断是否有查询，如有则把查询的字段set进columnQuery
            if (!StringUtil.isBlank(column.getColumnQuery())) {
                listMap.put("columnQuery", column.getColumnQuery());
                map.put("hasQuery", true);
                queryColumns.add(listMap);
            }
            columns.add(listMap);
        }
        map.put("columns", columns);
        map.put("queryColumns", queryColumns);

        return map;
    }

}
