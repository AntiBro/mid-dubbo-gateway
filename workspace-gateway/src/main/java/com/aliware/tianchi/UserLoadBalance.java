package com.aliware.tianchi;

import com.aliware.tianchi.support.DynamicRouteService;
import com.aliware.tianchi.support.impl.DynamicRouteServiceImpl;
import com.aliware.tianchi.support.impl.StatisServiceImpl;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author daofeng.xjf
 *
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {


    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        DynamicRouteService dynamicRouteService = DynamicRouteServiceImpl.create();

        //System.out.println("当前的Invoker 个数:"+invokers.size());

        dynamicRouteService.initInvokersRank(invokers);
        dynamicRouteService.scheduleUpdateInvokersRank(StatisServiceImpl.create());
        return dynamicRouteService.getInvoker();
    }
}
