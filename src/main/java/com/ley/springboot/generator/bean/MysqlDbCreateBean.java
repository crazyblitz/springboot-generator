package com.ley.springboot.generator.bean;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.ley.springboot.generator.utils.CloseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * mysql db create bean
 *
 * @author liuenyuan
 **/
@Slf4j
public class MysqlDbCreateBean extends BaseDbCreateBean {


    public MysqlDbCreateBean(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error("get mysql connection exception: {}", e.getMessage());
        }
        Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
        return connection;
    }


    /**
     * get primary key
     **/
    @Override
    public List<String> getColumnPkNameList(String tableName) throws Exception {
        List<String> pkNames = new ArrayList<>(6);
        Connection con = this.getConnection();
        DatabaseMetaData metaData = con.getMetaData();
        ResultSet resultSet = metaData.getPrimaryKeys(null, null, tableName);
        try {
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                pkNames.add(columnName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeable(resultSet, con);
        }
        boolean isDebugEnabled = log.isDebugEnabled();
        if (isDebugEnabled) {
            log.debug("pk names: {}", pkNames);
        }
        return pkNames;
    }


    @Override
    public List<ColumnData> getColumnDatas(String tableName) throws Exception {
        //get column primary key names
        List<String> columnPkNames = this.getColumnPkNameList(tableName);
        Connection con = this.getConnection();
        DatabaseMetaData metaData = con.getMetaData();
        ResultSet rs = metaData.getColumns(null, null, tableName, "%");
        try {
            ArrayList<ColumnData> columnList = new ArrayList();

            while (rs.next()) {
                String name = rs.getString("COLUMN_NAME");
                String javaType = rs.getString("TYPE_NAME");
                String comment = "";
                String precision = "";
                String scale = "";
                String charMaxLength = "";
                String nullAble = "";
                String columnKey = "";
                if (!CollectionUtils.isEmpty(columnPkNames)) {
                    for (String columnPkName : columnPkNames) {
                        if (StringUtils.hasText(columnPkName) && columnPkName.equals(name)) {
                            columnKey = "PRI";
                        }
                    }
                }
                javaType = this.getJavaType(javaType, precision, scale);
                String[] strings = javaType.split("\\.");
                String shortType = strings[strings.length - 1];
                String fc = name.substring(0, 1);
                if (fc.equals(fc.toUpperCase())) {
                    name = name.toLowerCase();
                }

                String upperDataName = this.formatName(name);
                String dataName = upperDataName.substring(0, 1).toLowerCase()
                        + upperDataName.substring(1, upperDataName.length());
                ColumnData columnData = new ColumnData();
                columnData.setMysqlColumnName(name);
                columnData.setDataName(dataName);
                columnData.setUpperDataName(upperDataName);
                columnData.setDataType(javaType);
                columnData.setJdbcType(this.getJdbcType(javaType));
                columnData.setShortDataType(shortType);
                columnData.setColumnType(rs.getString("TYPE_NAME"));
                columnData.setColumnComment(comment);
                columnData.setPrecision(precision);
                columnData.setScale(scale);
                columnData.setCharMaxLength(charMaxLength);
                columnData.setNullAble(nullAble);
                columnData.setColumnKey(columnKey);
                formatAnnotation(columnData);
                columnList.add(columnData);
                boolean isDebugEnabled = log.isDebugEnabled();
                if (isDebugEnabled) {
                    log.debug("column data: {}", columnData);
                }
            }
            return columnList;
        } catch (Exception e) {
            log.error("getColumnDatas() exception message: {}", e.getMessage());
        } finally {
            CloseUtils.closeable(rs, con);
        }
        return null;
    }


}
