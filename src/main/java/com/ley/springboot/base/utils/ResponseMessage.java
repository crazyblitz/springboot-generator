package com.ley.springboot.base.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * response message
 *
 * @author 2036185346@qq.com
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage<T> {

    /**
     * response code
     **/
    private String responseCode;

    /**
     * response message
     **/
    private String responseMessage;

    /**
     * response data
     **/
    private T data;


    /**
     * response is ok
     **/
    private boolean ok;
}