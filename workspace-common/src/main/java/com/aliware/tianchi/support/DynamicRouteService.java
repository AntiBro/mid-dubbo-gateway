package com.aliware.tianchi.support;

import org.apache.dubbo.rpc.Invoker;

import java.util.List;

/**
 * @Author huaili
 * @Date 2019/7/3 17:11
 * @Description DynamicRouteService
 **/
public interface DynamicRouteService {
    void sortInvokers();

    <T> Invoker<T> getInvoker();

    <T> void initInvokersRank(List<Invoker<T>> invokers);

    void updateInvokersRankByProviderMetaInfo(String invokerId,MonitorInfoBean monitorInfoBean);

    /**
     * 周期性的 更新
     * @param statisService
     */
    void scheduleUpdateInvokersRank(StatisService statisService);

}
