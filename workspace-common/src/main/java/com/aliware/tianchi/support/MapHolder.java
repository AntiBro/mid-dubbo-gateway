package com.aliware.tianchi.support;

/**
 * @Author huaili
 * @Date 2019/7/12 14:05
 * @Description MapHolder
 **/
public class MapHolder {
    private static volatile Object holder = new Object();

    public Object read(){
        return holder;
    }
    public void write(Object object){
        holder = object;
    }

}
