package com.duobiao.mainframedart.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Author:Admin
 * Time:2019/9/6 10:28
 * 描述：
 */
public class Funs {
    public static Class<?> analysisClassInfo(Object object){
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }
}
