package com.ley.springboot.generator.def;

/**
 * table convert
 **/
public class TableConvert {

    /**
     * get nullable
     **/
    public static String getNullable(String nullable) {
        return !"YES".equals(nullable) && !"yes".equals(nullable) && !"y".equals(nullable) && !"Y".equals(nullable)
                ? (!"NO".equals(nullable) && !"N".equals(nullable) && !"no".equals(nullable) && !"n".equals(nullable)
                ? null : "N")
                : "Y";
    }
}