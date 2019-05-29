package com.ley.springboot.generator.utils;

import com.ley.springboot.generator.DbCodeGenerateFactory;

/**
 * code utility class
 **/
public class CodeUtils {

    /**
     * code generate by table name
     *
     * @see DbCodeGenerateFactory#codeGenerate(String, String)
     **/
    public static void codeGenerate(String tableName, String entityPackge) {
        DbCodeGenerateFactory.codeGenerate(tableName, entityPackge);
    }

    /**
     * code generate by database all tables
     *
     * @see DbCodeGenerateFactory#codeGenerateList(String)
     **/
    public static void codeGenerate(String entityPackge) {
        try {
            DbCodeGenerateFactory.codeGenerateList(entityPackge);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * code generate by table list
     *
     * @see DbCodeGenerateFactory#codeGenerateList(String[], String)
     **/
    public static void codeGenerate(String[] tableList, String entityPackge) {
        DbCodeGenerateFactory.codeGenerateList(tableList, entityPackge);
    }


    public static void main(String[] args) {
        codeGenerate("sys");
    }

}
