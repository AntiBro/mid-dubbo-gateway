package com.aliware.tianchi.support.impl;

import com.aliware.tianchi.support.CostTime;
import com.aliware.tianchi.support.NetUtil;
import com.aliware.tianchi.support.StatisService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.LRUCache;
import org.apache.dubbo.rpc.Invoker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 必须是单例模式
 */
public class StatisServiceImpl implements StatisService {

    private static StatisService INSTANCE = new StatisServiceImpl();

    static final int size = 30;

    static final double size_d = size;

    private  static AtomicLong count = new AtomicLong(1);
   // CopyOnWriteArrayList list = new CopyOnWriteArrayList();

   // LRUCache lruCache = new LRUCache(size);
    private Map<String,LRUCache> map = new ConcurrentHashMap<>();

    private Map<String, Double> statisMap = new ConcurrentHashMap<>();

    private static final long VALIDATE_PERIOD = 100;

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
    public void addInvokerCostTime(String invokerId, double cost) {
        final LRUCache cache = map.get(invokerId);
        if(cache != null) {
           // synchronized (cache) {
            long  id = count.incrementAndGet();
            cache.put(count.incrementAndGet(), new CostTime(cost,System.currentTimeMillis()));
            System.out.println("current costId="+id);
          //  }
        }
    }

    boolean validateCostTime(long time){
        return System.currentTimeMillis()-time<=VALIDATE_PERIOD?true:false;
    }

    @Override
    public Map<String, Double> getStatis() {
        for(Map.Entry<String,LRUCache> entry:map.entrySet()){
            String invokerId = entry.getKey();
            LRUCache cache = entry.getValue();
            long total = 0;
            int size = 0;
            for(Object cost:cache.values()){
                CostTime costTime = (CostTime) cost;
                if(validateCostTime(costTime.getTimeStramp())){
                    total+= (double)costTime.getCostTime();
                    size++;
                }
            }
            double avgCost = 1000;
            if(size>0)
                avgCost = total/size;
           // cache.clear();
            if(avgCost == 0)
                avgCost = 1000;
            statisMap.put(invokerId,avgCost);
            //System.out.println("getStatis invokerId="+invokerId+" avgcost="+avgCost);
        }

        return statisMap;
    }
}
