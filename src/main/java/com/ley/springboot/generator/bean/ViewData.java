package com.ley.springboot.generator.bean;

import lombok.Data;

/**
 * view data
 **/
@Data
public class ViewData {

    private String title;

    private boolean filter = false;

    private boolean searchResult = false;

    private boolean form = false;

    private String formatter;

    private String dictionary;

}