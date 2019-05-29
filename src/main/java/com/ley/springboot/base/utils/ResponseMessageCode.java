package com.ley.springboot.base.utils;

/**
 * response message code enum
 *
 * @author 2036185346@qq.com
 **/
public enum ResponseMessageCode {

    /**
     * response success
     **/
    SUCCESS("0"),

    /**
     * response error
     **/
    ERROR("-1"),

    /**
     * response valid error
     **/
    VALID_ERROR("r0001"),

    /**
     * save record success
     **/
    SAVE_SUCCESS("r0002"),

    /**
     * update record success
     **/
    UPDATE_SUCCESS("r0003"),

    /**
     * remove success
     **/
    REMOVE_SUCCESS("r0004");

    private String code;

    ResponseMessageCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}