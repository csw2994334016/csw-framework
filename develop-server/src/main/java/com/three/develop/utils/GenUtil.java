package com.three.develop.utils;


import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.template.*;
import com.three.common.utils.StringUtil;
import com.three.develop.param.ColumnInfo;
import com.three.develop.param.GenConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
public class GenUtil {

    private static final String TIMESTAMP = "Timestamp";

    private static final String BIG_DECIMAL = "BigDecimal";

    private static final String DATE = "Date";

    private static final String PK = "PRI";

    private static final String STRING = "String";

    private static final String INTEGER = "Integer";

    private static final String LONG = "Long";

    private static final String DOUBLE = "Double";

    /**
     * 生成代码
     *
     * @param genConfig      生成代码的参数配置，如包路径，作者
     * @param columnInfoList 表元数据
     * @param templateList   代码模板
     */
    public static void generateCode(GenConfig genConfig, List<ColumnInfo> columnInfoList, List<String> templateList) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("package", genConfig.getPackPath());
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
            if (STRING.equals(column.getColumnType()) && !"text".equals(column.getColumnKey()) && !PK.equals(column.getColumnKey())) {
                listMap.put("columnLength", 255);
            }
            if (INTEGER.equals(column.getColumnType()) && !"text".equals(column.getColumnKey()) && !PK.equals(column.getColumnKey())) {
                listMap.put("columnLength", 11);
            }
            if (LONG.equals(column.getColumnType()) && !"text".equals(column.getColumnKey()) && !PK.equals(column.getColumnKey())) {
                listMap.put("columnLength", 20);
            }
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
//                listMap.put("columnLength", 0);
                listMap.put("changeColumnType", "datetime");
            }
            if (DOUBLE.equals(column.getColumnType())) {
//                listMap.put("columnLength", 0);
                listMap.put("changeColumnType", "double");
            }
            listMap.put("columnName", column.getColumnName());
            listMap.put("isNullable", column.getIsNullable());
            listMap.put("columnShow", column.getColumnShow());
            listMap.put("changeColumnName", changeColumnName);

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

        // 加载模板
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template/generator/admin/", TemplateConfig.ResourceMode.CLASSPATH));

        // 生成后端代码
        for (String templateName : templateList) {
            Template template = engine.getTemplate(templateName + ".ftl");
            String filePath = getAdminFilePath(templateName, genConfig, genConfig.getClassName());

            File file = null;
            if (filePath != null) {
                file = new File(filePath);
            }

            // 生成代码
            genFile(file, template, map);
        }
    }

    /**
     * 定义后端文件路径以及名称
     */
    private static String getAdminFilePath(String templateName, GenConfig genConfig, String className) {
        String projectPath = System.getProperty("user.dir") + File.separator + genConfig.getModuleName();
        if (StringUtil.isNotBlank(genConfig.getAdminPath()) && FileUtil.exist(genConfig.getAdminPath())) {
            projectPath = genConfig.getAdminPath() + File.separator + genConfig.getModuleName();
        }
        String packagePath = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        if (!ObjectUtils.isEmpty(genConfig.getPackPath())) {
            packagePath += genConfig.getPackPath().replace(".", File.separator) + File.separator;
        }
        if ("Entity".equals(templateName)) {
            return packagePath + "entity" + File.separator + className + ".java";
        }
        if ("Controller".equals(templateName)) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }
        if ("Service".equals(templateName)) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }
        if ("Param".equals(templateName)) {
            return packagePath + "param" + File.separator + className + "Param.java";
        }
        if ("Repository".equals(templateName)) {
            return packagePath + "repository" + File.separator + className + "Repository.java";
        }

        return null;
    }

    private static void genFile(File file, Template template, Map<String, Object> map) throws IOException {
        // 生成目标文件
        Writer writer = null;
        try {
            FileUtil.touch(file);
            writer = new FileWriter(file);
            template.render(map, writer);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
