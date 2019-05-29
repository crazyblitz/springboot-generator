package com.ley.springboot.generator.bean;

import lombok.Data;

import java.util.List;

/**
 * field data
 **/
@Data
public class FieldData {

    private String type;

    private String name;

    private String comment;

    private List<String> importJavaTypes;

}