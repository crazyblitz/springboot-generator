package com.ley.springboot.generator.bean;

import lombok.Data;

/**
 * column data
 *
 * @author liuenyuan
 **/
@Data
public class ColumnData {

    public static final String OPTION_REQUIRED = "required:true";

    public static final String OPTION_NUMBER_INSEX = "precision:2,groupSeparator:\',\'";

    private String mysqlColumnName;

    private String oracleColumnName;

    private String dataName;

    private String upperDataName;

    private String jdbcType;

    private String dataType;

    private String shortDataType;

    private String columnComment;

    private String columnType;

    private String charMaxLength = "";

    private String nullAble;

    private String scale;

    private String precision;

    private String classType = "";

    private String optionType = "";

    private String columnKey;

    private ColumnCommentViewData columnCommentViewData;

}