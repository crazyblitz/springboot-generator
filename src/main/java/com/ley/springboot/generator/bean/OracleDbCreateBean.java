package com.ley.springboot.generator.bean;


import com.ley.springboot.generator.utils.CloseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OracleDbCreateBean extends BaseDbCreateBean {


    public OracleDbCreateBean(String url, String username, String password) {
        super(url, username, password);
    }


    @Override
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException arg1) {
            log.error("get Oracle connection exception: {}", arg1.getMessage());
        }
        Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
        log.info("连接Oracle数据库成功!");
        return connection;
    }


    @Override
    public List<String> getColumnPkNameList(String tableName) throws Exception {
        String sql = "select cu.column_name from user_cons_columns cu, user_constraints au where cu.constraint_name = au.constraint_name and au.constraint_type = \'P\' and au.table_name = \'"
                + tableName.toUpperCase() + "\'";
        List<String> pkNames = new ArrayList<>(6);
        log.info("oracle get column pk name sql: {}", sql);
        Connection con = this.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        try {
            while (rs.next()) {
                pkNames.add(rs.getString("COLUMN_NAME"));
            }
            boolean isDebugEnabled = log.isDebugEnabled();
            if (isDebugEnabled) {
                log.debug("oracle,table name: {},pk names: {}", tableName, pkNames);
            }
        } finally {
            CloseUtils.closeable(rs, ps, con);
        }
        return pkNames;
    }

    @Override
    public List<ColumnData> getColumnDatas(String tableName) throws Exception {
        String sqlColumns = "SELECT atc.COLUMN_NAME, atc.DATA_TYPE, atc.DATA_PRECISION, atc.DATA_SCALE FROM user_tab_cols atc WHERE table_name=\'"
                + tableName.toUpperCase() + "\'";
        //get column primary key names
        List<String> columnPkNames = this.getColumnPkNameList(tableName);
        Connection con = this.getConnection();
        PreparedStatement ps = con.prepareStatement(sqlColumns);
        ResultSet rs = ps.executeQuery();
        log.debug("sqlColumns: {}", sqlColumns);
        try {
            ArrayList<ColumnData> columnList = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString(1);
                String type = rs.getString(2);
                String comment = "";
                String precision = rs.getString(3);
                String scale = rs.getString(4);
                String charmaxLength = "";
                String nullable = "";
                String columnKey = "";
                if (!CollectionUtils.isEmpty(columnPkNames)) {
                    for (String columnPkName : columnPkNames) {
                        if (StringUtils.hasText(columnPkName) && columnPkName.equals(name)) {
                            columnKey = "PRI";
                        }
                    }
                }
                type = this.getJavaType(type, precision, scale);
                String[] strings = type.split("\\.");
                String shortType = strings[strings.length - 1];
                String fc = name.substring(0, 1);
                if (fc.equals(fc.toUpperCase())) {
                    name = name.toLowerCase();
                }
                String upperDataName = this.formatName(name);
                String dataName = upperDataName.substring(0, 1).toLowerCase()
                        + upperDataName.substring(1, upperDataName.length());
                ColumnData columnData = new ColumnData();
                columnData.setOracleColumnName("\"" + name.toUpperCase() + "\"");
                columnData.setDataName(dataName);
                columnData.setUpperDataName(upperDataName);
                columnData.setDataType(type);
                columnData.setJdbcType(this.getJdbcType(type));
                columnData.setShortDataType(shortType);
                columnData.setColumnType(rs.getString(2));
                columnData.setColumnComment(comment);
                columnData.setPrecision(precision);
                columnData.setScale(scale);
                columnData.setCharmaxLength(charmaxLength);
                columnData.setNullable(nullable);
                columnData.setColumnKey(columnKey);
                formatAnnotation(columnData);
                formatFieldClassType(columnData);
                columnList.add(columnData);
            }
            boolean isDebugEnabled = log.isDebugEnabled();
            if (isDebugEnabled) {
                log.debug("columnList: {}", columnList);
            }
            return columnList;
        } catch (Exception e) {
            log.error("get column datas exception: {}", e.getMessage());
        } finally {
            CloseUtils.closeable(rs, ps, con);
        }
        return null;
    }


}