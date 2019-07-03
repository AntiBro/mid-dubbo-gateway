package com.aliware.tianchi.support;

import java.util.Map;

public interface StatisService {


    /**
     *
     * @param invokerId  {ip:port}
     * @param cost
     */
    void addInvokerCostTime(String invokerId,long cost);


    /**
     *  需要定时执行
     *  100ms 统计一次
     *  计算 统计耗时
     * @return
     */
    Map<String,Double> getStatis();
}
