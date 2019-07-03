package com.aliware.tianchi.support.impl;

import com.aliware.tianchi.support.NetUtil;
import com.aliware.tianchi.support.StatisService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.LRUCache;
import org.apache.dubbo.rpc.Invoker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class StatisServiceImpl implements StatisService {

    public static StatisService INSTANCE = new StatisServiceImpl();

    static final int size = 100;

    CopyOnWriteArrayList list = new CopyOnWriteArrayList();

    LRUCache lruCache = new LRUCache(size);

    private StatisServiceImpl(){}

    @Override
    public <T> void initInvokers(List<Invoker<T>> invokers) {
        if(list.isEmpty()){
            for(Invoker invoker:invokers){
                URL url = invoker.getUrl();
                String invokerId = NetUtil.getAddress(url.getHost(),url.getPort());

            }

        }
    }

    @Override
    public void addInvokerCostTime(String invokerId, long cost) {

    }

    @Override
    public Map<String, Double> getStatis() {
        return null;
    }
}
