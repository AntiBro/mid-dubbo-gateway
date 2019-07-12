package com.aliware.tianchi.support.impl;

import com.aliware.tianchi.support.CostTime;
import com.aliware.tianchi.support.NetUtil;
import com.aliware.tianchi.support.StatisService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.LRUCache;
import org.apache.dubbo.rpc.Invoker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 必须是单例模式
 */
public class StatisServiceImpl implements StatisService {

    private static StatisService INSTANCE = new StatisServiceImpl();

    static final int size = 15;

    static final double size_d = size;

    private static final AtomicLong count = new AtomicLong(1);
   // CopyOnWriteArrayList list = new CopyOnWriteArrayList();

   // LRUCache lruCache = new LRUCache(size);
    private static volatile Map<String,LRUCache> map = new ConcurrentHashMap<>();

    private static volatile Map<String,String> cachedAddressMap = new ConcurrentHashMap<>();


    private static final long VALIDATE_PERIOD = 1000;

    private static final double defaultAvgCost = 100;

    private StatisServiceImpl(){}




    public static StatisService create(){
        return INSTANCE;
    }

    @Override
    public <T> void initInvokers(List<Invoker<T>> invokers) {
        if(map.isEmpty()){
            for(Invoker invoker:invokers){
                URL url = invoker.getUrl();
                String invokerId = NetUtil.getAddress(url.getHost(),url.getPort());

                System.out.println("StatisServiceImpl initInvokers"+url.toString());
                map.put(invokerId,new LRUCache(size));
            }
        }
    }

    @Override
    public void addInvokerCostTime(URL url, double cost) {

        CompletableFuture.runAsync(()->{
            String urlId = url.getHost()+":"+url.getPort();
            String invokerId = cachedAddressMap.get(urlId);
            if(invokerId == null){
                invokerId = NetUtil.getAddress(url.getHost(),url.getPort());
                cachedAddressMap.put(urlId,invokerId);
            }
            final LRUCache cache = map.get(invokerId);
            if(cache != null) {
                // CAS 操作 比 获取 系统时间 要快 近 2倍
                cache.put(count.incrementAndGet(), cost);
            }

        });



    }

    boolean validateCostTime(long time){
        //return System.currentTimeMillis()-time<=VALIDATE_PERIOD?true:false;
        return true;
    }

    @Override
    public Map<String, Double> getStatis() {
        Map<String, Double> statisMap = new HashMap<>();

        for(Map.Entry<String,LRUCache> entry:map.entrySet()){
            String invokerId = entry.getKey();
            LRUCache cache = entry.getValue();
            long total = 0;
            int size = 0;
            for(Object cost:cache.values()){
                //double costTime = (double) cost;
                //if(validateCostTime(costTime.getTimeStramp())){
                total+= (double)cost;
                size++;
                //}
            }
            double avgCost = defaultAvgCost;
            if(size>0)
                avgCost = total/size;
           // cache.clear();
            if(avgCost == 0)
                avgCost = defaultAvgCost;
            statisMap.put(invokerId,avgCost);
            //System.out.println("getStatis invokerId="+invokerId+" avgcost="+avgCost);
        }

        return statisMap;
    }
}
