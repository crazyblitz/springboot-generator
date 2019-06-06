package com.ley.springboot.generator.type;

/**
 * 类描述: java type
 *
 * @author liuenyuan
 * @date 2019/6/6 19:57
 * @describe
 */
public enum JavaType {

    /**
     * string
     **/
    STRING("string"),

    /**
     * integer
     **/
    INTEGER("integer"),

    /**
     * double
     **/
    DOUBLE("double"),


    /**
     * short
     **/
    SHORT("short"),


    /**
     * date
     **/
    DATE("date"),


    /**
     * blob
     **/
    BLOB("blob"),


    /**
     * clob
     **/
    CLOB("clob"),


    /**
     * float
     **/
    FLOAT("float"),


    /**
     * big decimal
     **/
    BIGDECIMAL("bigdecimal");

    JavaType(String javaType) {
        this.javaType = javaType;
    }

    private String javaType;

    public String getJavaType() {
        return javaType;
    }
}
