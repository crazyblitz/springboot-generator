package com.ley.springboot.generator.def;

/**
 * table convert
 *
 * @author liuenyuan
 **/
public class TableConvert {

    /**
     * get nullAble
     **/
    public static String getNullable(String nullAble) {
        return !"YES".equals(nullAble) && !"yes".equals(nullAble) && !"y".equals(nullAble) && !"Y".equals(nullAble)
                ? (!"NO".equals(nullAble) && !"N".equals(nullAble) && !"no".equals(nullAble) && !"n".equals(nullAble)
                ? null : "N")
                : "Y";
    }
}