package com.ley.springboot.generator.bean;

import com.ley.springboot.generator.DbCodeGenerateFactory;
import com.ley.springboot.generator.def.CodeResourceUtil;
import com.ley.springboot.generator.utils.CloseUtils;
import com.ley.springboot.generator.utils.GeneratorConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * base db create bean
 **/
@Data
@AllArgsConstructor
@Slf4j
public abstract class BaseDbCreateBean {

    private String url;
    private String username;
    private String password;

    protected final String MYSQL_SQL_TABLES = "SHOW TABLES";

    protected final String ORACLE_SQL_TABLES = "select TABLE_NAME from all_tables where owner=  " + "'" + CodeResourceUtil.getUsername().toUpperCase() + "'";

    /**
     * get jdbc connection
     **/
    protected abstract Connection getConnection() throws Exception;


    /**
     * get database tables
     **/
    public List<String> getTables() throws Exception {
        Connection connection = this.getConnection();
        PreparedStatement preparedStatement;
        if (CodeResourceUtil.driverClassName.contains("mysql")) {
            preparedStatement = connection.prepareStatement(this.MYSQL_SQL_TABLES);
        } else {
            preparedStatement = connection.prepareStatement(this.ORACLE_SQL_TABLES);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        try {
            ArrayList<String> tableList = new ArrayList<>();
            while (resultSet.next()) {
                tableList.add(resultSet.getString(1));
            }
            boolean isDebugEnabled = log.isDebugEnabled();
            if (isDebugEnabled) {
                log.debug("database: {},tables: {}", CodeResourceUtil.databaseName,
                        tableList);
            }
            return tableList;
        } catch (Exception e) {
            log.error("get database tables exception message: {}", e.getMessage());
        } finally {
            CloseUtils.closeable(resultSet, preparedStatement, connection);
        }
        return null;
    }


    /**
     * get column primary key list
     *
     * @param tableName table name
     * @return return pk name list
     **/
    public abstract List<String> getColumnPkNameList(String tableName) throws Exception;


    /**
     * get columns except primary key
     *
     * @param tableName
     * @return return table {@link ColumnData}
     **/
    public abstract List<ColumnData> getColumnDatas(String tableName) throws Exception;


    /**
     * get entity import classes
     **/
    public Set<String> getEntityImportClasses(List<ColumnData> columns) {
        Set<String> set = new TreeSet();
        Iterator<ColumnData> iterator = columns.iterator();
        while (iterator.hasNext()) {
            String tp = iterator.next().getDataType();
            if (!tp.matches("java\\.lang\\.[^.]+")) {
                set.add(tp);
            }
            boolean isDebugEnabled = log.isDebugEnabled();
            if (isDebugEnabled) {
                log.debug("entity import class: {}", tp);
            }
        }
        return set;
    }


    /**
     * format field class type
     **/
    protected void formatFieldClassType(ColumnData columnData) {
        String fieldType = columnData.getColumnType();
        String scale = columnData.getScale();
        if ("N".equals(columnData.getNullable())) {
            columnData.setOptionType("required:true");
        }

        if (!"datetime".equals(fieldType) && !"time".equals(fieldType) && !"timestamp".equals(fieldType)) {
            if ("date".equals(fieldType)) {
                columnData.setClassType("easyui-datebox");
            } else if ("int".equals(fieldType)) {
                columnData.setClassType("easyui-numberbox");
            } else if ("number".equals(fieldType)) {
                if (StringUtils.isNotBlank(scale) && Integer.parseInt(scale) > 0) {
                    columnData.setClassType("easyui-numberbox");
                    if (StringUtils.isNotBlank(columnData.getOptionType())) {
                        columnData.setOptionType(columnData.getOptionType() + "," + "precision:2,groupSeparator:\',\'");
                    } else {
                        columnData.setOptionType("precision:2,groupSeparator:\',\'");
                    }
                } else {
                    columnData.setClassType("easyui-numberbox");
                }
            } else if (!"float".equals(fieldType) && !"double".equals(fieldType) && !"decimal".equals(fieldType)) {
                columnData.setClassType("easyui-validatebox");
            } else {
                columnData.setClassType("easyui-numberbox");
                if (StringUtils.isNotBlank(columnData.getOptionType())) {
                    columnData.setOptionType(columnData.getOptionType() + "," + "precision:2,groupSeparator:\',\'");
                } else {
                    columnData.setOptionType("precision:2,groupSeparator:\',\'");
                }
            }
        } else {
            columnData.setClassType("easyui-datetimebox");
        }

        if (columnData.getViewData() != null && columnData.getViewData().getDictionary() != null) {
            columnData.setClassType("easyui-combobox");
        }

    }

    /**
     * format annotation
     **/
    protected void formatAnnotation(ColumnData columnt) {
        String comment = columnt.getColumnComment();
        ViewData vd = new ViewData();
        if (StringUtils.isNotEmpty(comment)) {
            comment = comment.replaceAll("，", ",");
            if (comment.indexOf(",") != -1) {
                vd.setTitle(comment.split(",")[0]);
            } else if (comment.indexOf("，") != -1) {
                vd.setTitle(comment.split("，")[0]);
            } else {
                vd.setTitle(comment);
            }
        }
        columnt.setViewData(vd);
    }

    /**
     * get jdbc type
     **/
    protected String getJdbcType(String javaType) {
        javaType = javaType.toLowerCase();
        String jdbcType = null;
        if (javaType.contains("string")) {
            jdbcType = "VARCHAR";
        } else if (javaType.contains("integer")) {
            jdbcType = "INTEGER";
        } else if (javaType.contains("short")) {
            jdbcType = "SMALLINT";
        } else if (javaType.contains("double")) {
            jdbcType = "DOUBLE  ";
        } else if (javaType.contains("float")) {
            jdbcType = "FLOAT";
        } else if (javaType.contains("bigdecimal")) {
            jdbcType = "DECIMAL";
        } else if (javaType.contains("date")) {
            jdbcType = "TIMESTAMP";
        } else if (javaType.contains("clob")) {
            jdbcType = "CLOB";
        } else if (javaType.contains("blob")) {
            jdbcType = "BLOB";
        } else {
            jdbcType = "VARCHAR";
        }
        return jdbcType;
    }


    /**
     * get java type
     **/
    public String getJavaType(String jdbcType, String precision, String scale) {
        jdbcType = jdbcType.toLowerCase();
        String javaType = null;
        if (!jdbcType.contains("char") && !jdbcType.contains("text")) {
            if (jdbcType.contains("bit")) {
                javaType = "java.lang.Boolean";
            } else if (jdbcType.contains("bigint")) {
                javaType = "java.lang.Long";
            } else if (jdbcType.contains("int")) {
                javaType = "java.lang.Integer";
            } else if (jdbcType.contains("float")) {
                javaType = "java.lang.Float";
            } else if (jdbcType.contains("double")) {
                javaType = "java.lang.Double";
            } else if (jdbcType.contains("number")) {
                if (StringUtils.isNotBlank(scale) && Integer.parseInt(scale) > 0) {
                    javaType = "java.lang.Double";
                } else if (StringUtils.isNotBlank(precision) && Integer.parseInt(precision) > 6) {
                    javaType = "java.lang.Long";
                } else {
                    javaType = "java.lang.Integer";
                }
            } else if (jdbcType.contains("decimal")) {
                javaType = "java.math.BigDecimal";
            } else if (jdbcType.contains("date")) {
                javaType = "java.util.Date";
            } else if (jdbcType.contains("time")) {
                javaType = "java.util.Date";
            } else if (jdbcType.contains("clob")) {
                javaType = "java.sql.Clob";
            } else {
                javaType = "java.lang.Object";
            }
        } else {
            javaType = "java.lang.String";
        }
        return javaType;
    }


    /**
     * get package
     **/
    public void getPackage(int type, String createPath, String content, String packageName, String className,
                           String extendsClassName, String[] importName) throws Exception {
        if (packageName == null) {
            packageName = "";
        }

        StringBuffer sb = new StringBuffer();
        sb.append("package ").append(packageName).append(";\r");
        sb.append("\r");

        for (int temp = 0; temp < importName.length; ++temp) {
            sb.append("import ").append(importName[temp]).append(";\r");
        }

        sb.append("\r");
        sb.append("");
        sb.append("\r");
        sb.append("\rpublic class ").append(className);
        if (extendsClassName != null) {
            sb.append(" extends ").append(extendsClassName);
        }

        if (type == 1) {
            sb.append(" ").append("implements java.io.Serializable {\r");
        } else {
            sb.append(" {\r");
        }

        sb.append("\r\t");
        sb.append("private static final long serialVersionUID = 1L;\r\t");
        String arg9 = className.substring(0, 1).toLowerCase();
        arg9 = arg9 + className.substring(1, className.length());
        if (type == 1) {
            sb.append("private " + className + " " + arg9 + "; // entity ");
        }

        sb.append(content);
        sb.append("\r}");
        System.out.println(sb.toString());
        this.createFile(createPath, "", sb.toString());
    }

    /**
     * format class name
     **/
    public String formatClassName(String name) {
        return this.formatName(this.formatTableName(name));
    }

    /**
     * format table name
     **/
    public String formatTableName(String name) {
        String[] tablePrefixes = CodeResourceUtil.tablePrefix.split(",");
        //转换成小写
        name = name.toLowerCase();
        int length = tablePrefixes.length;
        for (int i = 0; i < length; ++i) {
            String prefix = tablePrefixes[i].toLowerCase();
            if (name.startsWith(prefix)) {
                return name.substring(prefix.length()).toLowerCase();
            }
        }
        boolean isDebugEnabled = log.isDebugEnabled();
        if (isDebugEnabled) {
            log.debug("format table name: {}", name);
        }
        return name.toLowerCase();
    }

    /**
     * format name
     **/
    public String formatName(String name) {
        if (name.contains("_")) {
            name = name.toLowerCase();
        }
        String[] split = name.split("_");
        if (split.length > 1) {
            StringBuffer arg5 = new StringBuffer();

            for (int i = 0; i < split.length; ++i) {
                if (split[i].length() > 0) {
                    String tempTableName = split[i].substring(0, 1).toUpperCase()
                            + split[i].substring(1, split[i].length());
                    arg5.append(tempTableName);
                }
            }

            return arg5.toString();
        } else {
            String tempTables = split[0].substring(0, 1).toUpperCase() + split[0].substring(1, split[0].length());
            return tempTables;
        }
    }


    /**
     * create file
     **/
    public void createFile(String path, String fileName, String str) throws IOException {
        FileWriter writer = new FileWriter(new File(path + fileName));
        try {
            writer.write(new String(str.getBytes(GeneratorConstants.DEFAULT_ENCODING), GeneratorConstants.DEFAULT_ENCODING));
        } catch (Throwable arg13) {
            throw arg13;
        } finally {
            CloseUtils.closeable(writer);
        }

    }


    /**
     * get auto create sql
     **/
    public Map<String, Object> getAutoCreateSql(String tableName) throws Exception {
        HashMap sqlMap = new HashMap();
        List columnDatas = this.getColumnDatas(tableName);
        String columns = this.getColumnSplit(columnDatas);
        String[] columnList = this.getColumnList(columns);
        String columnFields = this.getColumnFields(columns);
        String datas = this.getDataSplit(columnDatas);
        String[] datasList = this.getColumnList(datas);
        String insert = "insert into " + tableName + "(" + "<include refid=\"Base_Column_List\" />" + ")\n values(#{"
                + datas.replaceAll("\\|", "},#{") + "})";
        String update = this.getUpdateSql(tableName, columnList, datasList);
        String updateSelective = this.getUpdateSelectiveSql(tableName, columnDatas);
        String selectById = this.getSelectByIdSql(tableName, columnList, datasList);
        String delete = this.getDeleteSql(tableName, columnList, datasList);
        sqlMap.put("columnList", columnList);
        sqlMap.put("columnFields", columnFields);
        sqlMap.put("insert", insert.replace("#{createTime}", "now()").replace("#{updateTime}", "now()"));
        sqlMap.put("update", update.replace("#{createTime}", "now()").replace("#{updateTime}", "now()"));
        sqlMap.put("delete", delete);
        sqlMap.put("updateSelective", updateSelective);
        sqlMap.put("selectById", selectById);
        return sqlMap;
    }


    /**
     * get delete sql
     **/
    public String getDeleteSql(String tableName, String[] columnsList, String[] datasList) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("delete ");
        sb.append("\t from ").append(tableName).append(" where ");
        sb.append('`').append(columnsList[0]).append('`').append(" = #{").append(datasList[0]).append("}");
        return sb.toString();
    }

    /**
     * get select by id sql
     **/
    public String getSelectByIdSql(String tableName, String[] columnsList, String[] datasList) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("select <include refid=\"Base_Column_List\" /> \n");
        sb.append("\t from ").append(tableName).append(" where ");
        sb.append(columnsList[0]).append(" = #{").append(datasList[0]).append("}");
        return sb.toString();
    }

    /**
     * get column fields
     **/
    public String getColumnFields(String columns) throws SQLException {
        String fields = columns;
        log.debug("fields: {}", columns);
        if (columns != null && !"".equals(columns)) {
            fields = columns.replaceAll("\\|", ", ");
        }
        return fields;
    }

    /**
     * get column list
     **/
    public String[] getColumnList(String columns) throws SQLException {
        String[] columnList = columns.split("[|]");
        return columnList;
    }

    /**
     * get update sql
     **/
    public String getUpdateSql(String tableName, String[] columnsList, String[] datasList) throws SQLException {
        StringBuffer sb = new StringBuffer();

        for (int update = 1; update < columnsList.length; ++update) {
            String column = columnsList[update];
            String data = datasList[update];
            if (!"CREATETIME".equals(column.toUpperCase())) {
                if ("UPDATETIME".equals(column.toUpperCase())) {
                    sb.append('`').append(column).append('`').append(" = now()");
                } else {
                    sb.append('`').append(column).append('`').append(" = #{").append(data).append("}");
                }

                if (update + 1 < columnsList.length) {
                    sb.append(",");
                }
            }
        }

        String arg7 = "update " + tableName + " set " + sb.toString() + " where " + columnsList[0] + "=#{"
                + datasList[0] + "}";
        return arg7;
    }


    /**
     * get update selective sql
     **/
    public String getUpdateSelectiveSql(String tableName, List<ColumnData> columnList) throws SQLException {
        StringBuffer sb = new StringBuffer();
        ColumnData cd = (ColumnData) columnList.get(0);
        sb.append("\t<trim  suffixOverrides=\",\" >\n");

        for (int update = 1; update < columnList.size(); ++update) {
            ColumnData data = (ColumnData) columnList.get(update);
            String columnName = data.getMysqlColumnName();
            sb.append("\t<if test=\"").append(data.getDataName()).append(" != null ");
            if ("String" == data.getDataType()) {
                sb.append(" and ").append(data.getDataName()).append(" != \'\'");
            }

            sb.append(" \">\n\t\t");
            sb.append('`').append(columnName + '`' + " = #{" + data.getDataName() + "},\n");
            sb.append("\t</if>\n");
        }

        sb.append("\t</trim>");
        String arg7 = "update " + tableName + " set \n" + sb.toString() + " where " + cd.getMysqlColumnName() + "=#{"
                + cd.getDataName() + "}";
        return arg7;
    }


    /**
     * get column split
     **/
    public String getColumnSplit(List<ColumnData> columnList) throws SQLException {
        StringBuffer commonColumns = new StringBuffer();
        Iterator iterator = columnList.iterator();

        while (iterator.hasNext()) {
            ColumnData data = (ColumnData) iterator.next();
            //get column split
            if (DbCodeGenerateFactory.isMysql()) {
                commonColumns.append(data.getMysqlColumnName() + "|");
            } else {
                commonColumns.append(data.getOracleColumnName() + "|");
            }
        }

        return commonColumns.delete(commonColumns.length() - 1, commonColumns.length()).toString();
    }


    /**
     * get data split
     **/
    public String getDataSplit(List<ColumnData> columnList) throws SQLException {
        StringBuffer commonColumns = new StringBuffer();
        Iterator iterator = columnList.iterator();

        while (iterator.hasNext()) {
            ColumnData data = (ColumnData) iterator.next();
            commonColumns.append(data.getDataName() + "|");
        }

        return commonColumns.delete(commonColumns.length() - 1, commonColumns.length()).toString();
    }

}
