package com.aliware.tianchi.support;

import org.apache.dubbo.rpc.Invoker;

import java.util.List;
import java.util.Map;

public interface StatisService {

    <T> void initInvokers(List<Invoker<T>> invokers);
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
