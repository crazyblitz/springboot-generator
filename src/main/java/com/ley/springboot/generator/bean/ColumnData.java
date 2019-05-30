package com.ley.springboot.generator.bean;

import lombok.Data;

/**
 * column data
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

    private String charmaxLength = "";

    private String nullable;

    private String scale;

    private String precision;

    private String classType = "";

    private String optionType = "";

    private String columnKey;

    private ViewData viewData;

}