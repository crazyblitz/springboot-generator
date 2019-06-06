package com.ley.springboot.generator.type;

/**
 * 类描述: jdbc jdbcType
 *
 * @author liuenyuan
 * @date 2019/6/6 19:57
 * @describe
 */
public enum JdbcType {

    /**
     * char
     **/
    CHAR("char"),


    /**
     * varchar
     **/
    VARCHAR("varchar"),

    /**
     * text
     **/
    TEXT("text"),

    /**
     * big int
     **/
    BIGINT("bigint"),


    /**
     * integer
     **/
    INTEGER("integer"),


    /**
     * small int
     **/
    SMALLINT("smallint"),

    /**
     * int
     **/
    INT("int"),


    /**
     * float
     **/
    FLOAT("float"),


    /**
     * double
     **/
    DOUBLE("double"),


    /**
     * number
     **/
    NUMBER("number"),


    /**
     * decimal
     **/
    DECEMAL("decimal"),

    /**
     * date
     **/
    DATE("date"),


    /**
     * time
     **/
    TIME("time"),


    /**
     * bit
     **/
    BIT("bit"),

    /**
     * timestamp
     **/
    TIMESTAMP("timestamp"),


    /**
     * blob
     **/
    BLOB("blob"),

    /**
     * clob
     **/
    CLOB("clob");


    JdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    private String jdbcType;

    public String getJdbcType() {
        return jdbcType;
    }
}
