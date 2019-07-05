package com.aliware.tianchi;

/**
 * @Author huaili
 * @Date 2019/7/4 15:05
 * @Description TODO
 **/
public class IDUtil {
    private static volatile String id;

    public static void setID(String newId){
        id = newId;
    }
    public static String getID(){
        return id;
    }
}
