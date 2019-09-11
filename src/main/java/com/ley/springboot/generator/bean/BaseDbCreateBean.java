package com.ley.springboot.generator.bean;

import com.ley.springboot.generator.DbCodeGenerateFactory;
import com.ley.springboot.generator.def.CodeResourceUtil;
import com.ley.springboot.generator.type.JavaType;
import com.ley.springboot.generator.type.JdbcType;
import com.ley.springboot.generator.utils.CloseUtils;
import com.ley.springboot.generator.utils.GeneratorConstants;
import com.ley.springboot.generator.utils.ResourceKeyConstants;
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
 *
 * @author liuenyuan
 **/
@Data
@AllArgsConstructor
@Slf4j
public abstract class BaseDbCreateBean {

    private String url;
    private String username;
    private String password;

    /**
     * mysql get tables sql
     **/
    protected static final String MYSQL_SQL_TABLES = "SHOW TABLES";

    /**
     * oracle get tables sql
     **/
    protected static final String ORACLE_SQL_TABLES = "select TABLE_NAME from all_tables where owner=  " + "'" + CodeResourceUtil.getUsername().toUpperCase() + "'";

    /**
     * get jdbc connection
     *
     * @return
     * @throws Exception
     **/
    protected abstract Connection getConnection() throws Exception;


    /**
     * get database tables
     **/
    public List<String> getTables() throws Exception {
        Connection connection = this.getConnection();
        PreparedStatement preparedStatement;
        if (CodeResourceUtil.getDiverName().contains(ResourceKeyConstants.DATABASE_TYPE_MYSQL)) {
            preparedStatement = connection.prepareStatement(MYSQL_SQL_TABLES);
        } else {
            preparedStatement = connection.prepareStatement(ORACLE_SQL_TABLES);
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
     * @throws Exception
     **/
    public abstract List<String> getColumnPkNameList(String tableName) throws Exception;


    /**
     * get columns except primary key
     *
     * @param tableName
     * @return table {@link ColumnData}
     * @throws Exception
     **/
    public abstract List<ColumnData> getColumnDataList(String tableName) throws Exception;


    /**
     * get entity import classes
     **/
    public Set<String> getEntityImportClasses(List<ColumnData> columns) {
        Set<String> set = new TreeSet<>();
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
     * format annotation
     **/
    void formatAnnotation(ColumnData columnt) {
        String comment = columnt.getColumnComment();
        ColumnCommentViewData vd = new ColumnCommentViewData();
        if (StringUtils.isNotEmpty(comment)) {
            comment = comment.replaceAll("，", ",");
            if (comment.contains(",")) {
                vd.setTitle(comment.split(",")[0]);
            } else if (comment.contains("，")) {
                vd.setTitle(comment.split("，")[0]);
            } else {
                vd.setTitle(comment);
            }
        }
        columnt.setColumnCommentViewData(vd);
    }

    /**
     * get jdbc type
     **/
    String getJdbcType(String javaType) {
        javaType = javaType.toLowerCase();
        String jdbcType;
        if (javaType.contains(JavaType.STRING.getJavaType())) {
            jdbcType = JdbcType.VARCHAR.getJdbcType().toUpperCase();
        } else if (javaType.contains(JavaType.INTEGER.getJavaType())) {
            jdbcType = JdbcType.INTEGER.getJdbcType().toUpperCase();
        } else if (javaType.contains(JavaType.SHORT.getJavaType())) {
            jdbcType = JdbcType.SMALLINT.getJdbcType().toUpperCase();
        } else if (javaType.contains(JavaType.DOUBLE.getJavaType())) {
            jdbcType = JdbcType.DOUBLE.getJdbcType().toUpperCase();
        } else if (javaType.contains(JavaType.FLOAT.getJavaType())) {
            jdbcType = JdbcType.FLOAT.getJdbcType().toUpperCase();
        } else if (javaType.contains(JavaType.BIGDECIMAL.getJavaType())) {
            jdbcType = JdbcType.DECEMAL.getJdbcType().toUpperCase();
        } else if (javaType.contains(JavaType.DATE.getJavaType())) {
            jdbcType = JdbcType.TIMESTAMP.getJdbcType().toUpperCase();
        } else if (javaType.contains(JavaType.CLOB.getJavaType())) {
            jdbcType = JdbcType.CLOB.getJdbcType().toUpperCase();
        } else if (javaType.contains(JavaType.BLOB.getJavaType())) {
            jdbcType = JdbcType.BLOB.getJdbcType().toUpperCase();
        } else {
            jdbcType = JdbcType.VARCHAR.getJdbcType().toUpperCase();
        }
        return jdbcType;
    }


    /**
     * get java type
     **/
    String getJavaType(String jdbcType, String precision, String scale) {
        jdbcType = jdbcType.toLowerCase();
        String javaType;
        if (!jdbcType.contains(JdbcType.CHAR.getJdbcType()) && !jdbcType.contains(JdbcType.TEXT.getJdbcType())) {
            if (jdbcType.contains(JdbcType.BIT.getJdbcType())) {
                javaType = "java.lang.Boolean";
            } else if (jdbcType.contains(JdbcType.BIGINT.getJdbcType())) {
                javaType = "java.lang.Long";
            } else if (jdbcType.contains(JdbcType.INT.getJdbcType())) {
                javaType = "java.lang.Integer";
            } else if (jdbcType.contains(JdbcType.FLOAT.getJdbcType())) {
                javaType = "java.lang.Float";
            } else if (jdbcType.contains(JdbcType.DOUBLE.getJdbcType())) {
                javaType = "java.lang.Double";
            } else if (jdbcType.contains(JdbcType.NUMBER.getJdbcType())) {
                if (StringUtils.isNotBlank(scale) && Integer.parseInt(scale) > 0) {
                    javaType = "java.lang.Double";
                } else if (StringUtils.isNotBlank(precision) && Integer.parseInt(precision) > 6) {
                    javaType = "java.lang.Long";
                } else {
                    javaType = "java.lang.Integer";
                }
            } else if (jdbcType.contains(JdbcType.DECEMAL.getJdbcType())) {
                javaType = "java.math.BigDecimal";
            } else if (jdbcType.contains(JdbcType.DATE.getJdbcType())) {
                javaType = "java.util.Date";
            } else if (jdbcType.contains(JdbcType.TIME.getJdbcType())) {
                javaType = "java.util.Date";
            } else if (jdbcType.contains(JdbcType.TIMESTAMP.getJdbcType())) {
                javaType = "java.util.Date";
            } else if (jdbcType.contains(JdbcType.CLOB.getJdbcType())) {
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

        StringBuffer buffer = new StringBuffer();
        buffer.append("package ").append(packageName).append(";\r");
        buffer.append("\r");

        for (int temp = 0; temp < importName.length; ++temp) {
            buffer.append("import ").append(importName[temp]).append(";\r");
        }

        buffer.append("\r");
        buffer.append("\r");
        buffer.append("\rpublic class ").append(className);
        if (extendsClassName != null) {
            buffer.append(" extends ").append(extendsClassName);
        }

        if (type == 1) {
            buffer.append(" ").append("implements java.io.Serializable {\r");
        } else {
            buffer.append(" {\r");
        }

        buffer.append("\r\t");
        buffer.append("private static final long serialVersionUID = 1L;\r\t");
        String result = className.substring(0, 1).toLowerCase();
        result = result + className.substring(1, className.length());
        if (type == 1) {
            buffer.append("private " + className + " " + result + "; // entity ");
        }

        buffer.append(content);
        buffer.append("\r}");
        System.out.println(buffer.toString());
        this.createFile(createPath, "", buffer.toString());
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
    private String formatTableName(String name) {
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
    String formatName(String name) {
        if (name.contains("_")) {
            name = name.toLowerCase();
        }
        String[] split = name.split("_");
        if (split.length > 1) {
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < split.length; ++i) {
                if (split[i].length() > 0) {
                    int len = split[i].length();
                    String tempTableName = split[i].substring(0, 1).toUpperCase()
                            + split[i].substring(1, len);
                    buffer.append(tempTableName);
                }
            }

            return buffer.toString();
        } else {
            int len = split[0].length();
            return split[0].substring(0, 1).toUpperCase() + split[0].substring(1, len);
        }
    }


    /**
     * create file
     **/
    private void createFile(String path, String fileName, String str) throws IOException {
        FileWriter writer = new FileWriter(new File(path + fileName));
        try {
            writer.write(new String(str.getBytes(GeneratorConstants.DEFAULT_ENCODING), GeneratorConstants.DEFAULT_ENCODING));
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            CloseUtils.closeable(writer);
        }

    }


    /**
     * get auto create sql
     **/
    public Map<String, Object> getAutoCreateSql(String tableName) throws Exception {
        HashMap<String, Object> sqlMap = new HashMap<>(8);
        List<ColumnData> columnDataList = this.getColumnDataList(tableName);
        String columns = this.getColumnSplit(columnDataList);
        String[] columnList = this.getColumnList(columns);
        String columnFields = this.getColumnFields(columns);
        String dataSplit = this.getDataSplit(columnDataList);
        String[] dataSplitList = this.getColumnList(dataSplit);
        String insert = "insert into " + tableName + "(" + "<include refid=\"Base_Column_List\" />" + ")\n values(#{"
                + dataSplit.replaceAll("\\|", "},#{") + "})";
        String update = this.getUpdateSql(tableName, columnList, dataSplitList);
        String updateSelective = this.getUpdateSelectiveSql(tableName, columnDataList);
        String selectById = this.getSelectByIdSql(tableName, columnList, dataSplitList);
        String delete = this.getDeleteSql(tableName, columnList, dataSplitList);
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
    private String getDeleteSql(String tableName, String[] columnsList, String[] datasList) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("delete ");
        buffer.append("\t from ").append(tableName).append(" where ");
        buffer.append('`').append(columnsList[0]).append('`').append(" = #{").append(datasList[0]).append("}");
        return buffer.toString();
    }

    /**
     * get select by id sql
     **/
    private String getSelectByIdSql(String tableName, String[] columnsList, String[] datasList) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select <include refid=\"Base_Column_List\" /> \n");
        buffer.append("\t from ").append(tableName).append(" where ");
        buffer.append(columnsList[0]).append(" = #{").append(datasList[0]).append("}");
        return buffer.toString();
    }

    /**
     * get column fields
     **/
    private String getColumnFields(String columns) throws SQLException {
        String fields = columns;
        boolean isDebugEnabled = log.isDebugEnabled();
        if (isDebugEnabled) {
            log.debug("fields: {}", columns);
        }
        if (columns != null && !"".equals(columns)) {
            fields = columns.replaceAll("\\|", ", ");
        }
        return fields;
    }

    /**
     * get column list
     **/
    private String[] getColumnList(String columns) throws SQLException {
        return columns.split("[|]");
    }

    /**
     * get update sql
     **/
    private String getUpdateSql(String tableName, String[] columnsList, String[] datasList) throws SQLException {
        StringBuffer buffer = new StringBuffer();

        for (int update = 1; update < columnsList.length; ++update) {
            String column = columnsList[update];
            String data = datasList[update];
            if (!"CREATETIME".equals(column.toUpperCase())) {
                if ("UPDATETIME".equals(column.toUpperCase())) {
                    buffer.append('`').append(column).append('`').append(" = now()");
                } else {
                    buffer.append('`').append(column).append('`').append(" = #{").append(data).append("}");
                }

                if (update + 1 < columnsList.length) {
                    buffer.append(",");
                }
            }
        }

        String sql = "update " + tableName + " set " + buffer.toString() + " where " + columnsList[0] + "=#{"
                + datasList[0] + "}";
        return sql;
    }


    /**
     * get update selective sql
     **/
    private String getUpdateSelectiveSql(String tableName, List<ColumnData> columnList) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        ColumnData cd = columnList.get(0);
        buffer.append("\t<trim  suffixOverrides=\",\" >\n");

        for (int update = 1; update < columnList.size(); ++update) {
            ColumnData data = (ColumnData) columnList.get(update);
            String columnName = data.getMysqlColumnName();
            buffer.append("\t<if test=\"").append(data.getDataName()).append(" != null ");
            if ("String" == data.getDataType()) {
                buffer.append(" and ").append(data.getDataName()).append(" != \'\'");
            }

            buffer.append(" \">\n\t\t");
            buffer.append('`').append(columnName + '`' + " = #{" + data.getDataName() + "},\n");
            buffer.append("\t</if>\n");
        }

        buffer.append("\t</trim>");
        String sql = "update " + tableName + " set \n" + buffer.toString() + " where " + cd.getMysqlColumnName() + "=#{"
                + cd.getDataName() + "}";
        return sql;
    }


    /**
     * get column split
     **/
    private String getColumnSplit(List<ColumnData> columnList) throws SQLException {
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
    private String getDataSplit(List<ColumnData> columnList) throws SQLException {
        StringBuffer commonColumns = new StringBuffer();
        Iterator iterator = columnList.iterator();

        while (iterator.hasNext()) {
            ColumnData data = (ColumnData) iterator.next();
            commonColumns.append(data.getDataName() + "|");
        }

        return commonColumns.delete(commonColumns.length() - 1, commonColumns.length()).toString();
    }

}
