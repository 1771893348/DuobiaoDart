package com.duobiao.mainframedart.util;

/**
 * Author:Admin
 * Time:2019/7/23 16:21
 * 描述：
 */
public class ObjectUtils {
    public static boolean isEmpty(Object object){
        boolean flag = false;
        if (object == null){
            flag = true;
        }
        return  flag;
    }
    public static boolean isStringEmpty(String object){
        boolean flag = false;
        if (object == null || object.equals("")){
            flag = true;
        }
        return  flag;
    }
}
