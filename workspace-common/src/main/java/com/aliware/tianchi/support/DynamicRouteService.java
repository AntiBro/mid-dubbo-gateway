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

    /**
     * 动态路由选择
     * @param <T>
     * @return
     */
    <T> Invoker<T> getInvoker();

    /**
     * 初始化
     * @param invokers
     * @param <T>
     */
    <T> void initInvokersRank(List<Invoker<T>> invokers);

    /**
     *  根据首次的 物理信息更新
     * @param invokerId
     * @param monitorInfoBean
     */
    void updateInvokersRankByProviderMetaInfo(String invokerId,MonitorInfoBean monitorInfoBean);

    /**
     * 周期性的 更新
     * @param statisService
     */
    void scheduleUpdateInvokersRank(StatisService statisService);

}
