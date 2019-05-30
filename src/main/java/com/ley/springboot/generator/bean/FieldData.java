package com.ley.springboot.generator.bean;

import lombok.Data;

import java.util.List;

/**
 * field data
 *
 * @author liuenyuan
 **/
@Data
public class FieldData {

    /**
     * table field type
     **/
    private String type;

    /**
     * table field name
     **/
    private String name;

    /**
     * table field comment
     **/
    private String comment;


    /**
     * table field <==> java type
     * **/
    private List<String> importJavaTypes;

}