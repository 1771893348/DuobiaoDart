package com.duobiao.mainframedart.util;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Author:Admin
 * Time:2019/8/27 18:09
 * 描述：
 * https://blog.csdn.net/qq_35022307/article/details/81503780
 */
public class HotFixEngine {
    public static final String DEX_OPT_DIR = "optimize_dex";//dex的优化路径
    public static final String DEX_BASECLASSLOADER_CLASS_NAME = "dalvik.system.BaseDexClassLoader";
    public static final String Dex_ELEMENTS_FIELD = "dexElements";//pathList中的dexElements字段
    public static final String DEX_PATHLIST_FIELD = "pathList";//BaseClassLoader中的pathList字段
    public static final String FIX_DEX_PATH = "fix_dex";//fixDex存储的路径

    /**
     * 获得pathList中的dexElements
     * @param obj
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public Object getDexElements(Object obj) throws NoSuchFieldException,IllegalAccessException{
        return getField(obj,obj.getClass(),Dex_ELEMENTS_FIELD);
    }

    /***
     * fix
     * @param context
     * @param dexFile
     */
    public void loadDex(Context context, File dexFile){
        if (context == null){
            Log.d("wgw_=====>","context==null");
            return;
        }
        File fixDir = context.getDir(FIX_DEX_PATH,Context.MODE_PRIVATE);
        //mrege and fix
        mergeDex(context,fixDir,dexFile);
    }

    /**
     * 获取指定classloader 中的pathList字段的值（DexPathList）
     * @param classLoader
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public Object getDexPathListField(Object classLoader) throws ClassNotFoundException,NoSuchFieldException,IllegalAccessException{
        return getField(classLoader,Class.forName(DEX_BASECLASSLOADER_CLASS_NAME),DEX_PATHLIST_FIELD);
    }

    /**
     *获取一个字段的值
     * @param obj
     * @param aClass
     * @param dex_elements_field
     * @return
     */
    private Object getField(Object obj, Class<?> aClass, String dex_elements_field)throws NoSuchFieldException,IllegalAccessException {
        Field field = aClass.getDeclaredField(dex_elements_field);
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * 为指定对象中的字段重新赋值
     * @param obj
     * @param claz
     * @param filed
     * @param value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void setFiledValue(Object obj,Class<?> claz,String filed,Object value) throws NoSuchFieldException, IllegalAccessException{
        Field field = claz.getDeclaredField(filed);
        field.setAccessible(true);
        field.set(obj,value);
    }

    /**
     * 合并dex
     * @param context
     * @param fixDir
     * @param dexFile
     */
    private void mergeDex(Context context, File fixDir, File dexFile) {
        try{
            //创建dex的optimize路径
            File optimizeDir = new File(fixDir.getAbsolutePath(),DEX_OPT_DIR);
            if (!optimizeDir.exists()){
                optimizeDir.mkdir();
            }
            //加载自身Apk的dex，通过PathClassLoader
            PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
            //找到dex并通过DexClassLoader去加载
            //dex文件路径，优化输出路径，null,父加载器
            DexClassLoader dexClassLoader = new DexClassLoader(dexFile.getAbsolutePath(),optimizeDir.getAbsolutePath(),null,pathClassLoader);
            //获取app自身的BaseDexClassLoader中的pathList字段
            Object appDexPathList = getDexPathListField(pathClassLoader);
            //获取补丁的BaseDexClassLoader中的pathList字段
            Object fixDexPathList = getDexPathListField(dexClassLoader);

            Object appDexElements = getDexElements(appDexPathList);
            Object fixDexElements = getDexElements(fixDexPathList);

            //合并两个elements的数据，将修复的dex插入到数组最前面
            Object finalElements = combineArray(fixDexElements,appDexElements);
            //给app 中的dex pathList 中的dexElements 重新赋值
            setFiledValue(appDexPathList,appDexPathList.getClass(),Dex_ELEMENTS_FIELD,finalElements);
            Log.d("wgw_====>","fix success");
        } catch (Exception e){
            e.printStackTrace();
            Log.d("wgw_===>",e.getMessage());
        }

    }

    /**
     *两个数组合并
     * @param fixDexElements
     * @param appDexElements
     * @return
     */
    private Object combineArray(Object fixDexElements, Object appDexElements) {
        Class<?> loadClass = fixDexElements.getClass().getComponentType();
        int i = Array.getLength(fixDexElements);
        int j = i+Array.getLength(appDexElements);
        Object result = Array.newInstance(loadClass,j);
        for (int k = 0;k < j; ++k){
            if (k < i){
                Array.set(result,k,Array.get(fixDexElements,k));
            } else {
                Array.set(result,k,Array.get(appDexElements,k - i));
            }
        }
        return result;
    }


}
