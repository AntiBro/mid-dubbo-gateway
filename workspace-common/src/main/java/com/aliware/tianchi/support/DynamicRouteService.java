package com.aliware.tianchi.support;

import org.apache.dubbo.rpc.Invoker;

import java.util.List;

/**
 * @Author huaili
 * @Date 2019/7/3 17:11
 * @Description DynamicRouteService
 **/
public interface DynamicRouteService {

    <T> void addInvoker(List<Invoker<T>> invokers);

    void sortInvokers();

    <T> Invoker<T> getInvoker();

    void updateInvokersRank(String invokerId,MonitorInfoBean monitorInfoBean);
}
