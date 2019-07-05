package com.aliware.tianchi.support.impl;

import com.aliware.tianchi.support.DynamicRouteService;
import com.aliware.tianchi.support.InvokerWrapper;
import com.aliware.tianchi.support.MonitorInfoBean;
import com.aliware.tianchi.support.StatisService;
import org.apache.dubbo.rpc.Invoker;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author huaili
 * @Date 2019/7/4 15:52
 * @Description DynamicRouteServiceImpl
 **/
public class DynamicRouteServiceImpl implements DynamicRouteService {

    static volatile boolean init = false;

    static volatile boolean startTask = false;

    static final int BOUND = 100;

    static final double SIZE_D = 100.00;

    static final int PERIOD = 1000;

    static volatile CopyOnWriteArrayList<TreeMap<Double, InvokerWrapper>> rankCache = new CopyOnWriteArrayList();

    List<InvokerWrapper> cacheinvokerList = new ArrayList<>();

    static volatile ConcurrentHashMap<String,InvokerWrapper> rankInfoMap = new ConcurrentHashMap<>();

    private Timer timer = new Timer();


    private static DynamicRouteService INSTANCE = new DynamicRouteServiceImpl();

    public static DynamicRouteService create(){
        return INSTANCE;
    }

    private DynamicRouteServiceImpl(){

    }

    @Override
    public void sortInvokers() {

    }

    @Override
    public <T> Invoker<T> getInvoker() {
        double score = ThreadLocalRandom.current().nextInt(BOUND)/SIZE_D;

        InvokerWrapper invokerWrapper = rankCache.get(0).ceilingEntry(score).getValue();

        //System.out.println("select invoker id = "+invokerWrapper.getInvokerId());
        return invokerWrapper.getInvoker();
    }

    @Override
    public <T> void initInvokersRank(List<Invoker<T>> invokers) {
        if(!init){
            TreeMap<Double,InvokerWrapper> map = new TreeMap<>();
            double score = 1.00/invokers.size();
            double ceilScore = 0;
            for(Invoker invoker:invokers){
                ceilScore+= score;
                InvokerWrapper invokerWrapper = InvokerWrapper.buildWrapper(invoker);
                map.put(ceilScore,InvokerWrapper.buildWrapper(invoker));
                rankInfoMap.put(invokerWrapper.getInvokerId(),invokerWrapper);
            }
            rankCache.add(map);

            StatisServiceImpl.create().initInvokers(invokers);

            init = true;
        }
    }

    @Override
    public void updateInvokersRankByProviderMetaInfo(String invokerId, MonitorInfoBean monitorInfoBean) {
        InvokerWrapper invokerWrapper = rankInfoMap.get(invokerId);
        if(invokerWrapper != null){
            // update free memory for invoker
            MonitorInfoBean monitorInfoBean1 = invokerWrapper.getMonitorInfoBean();
            monitorInfoBean1.setFreeMem(monitorInfoBean.getFreeMem());
            monitorInfoBean1.setCoreCount(monitorInfoBean.getCoreCount());

        }
    }

    /**
     *  定时 计算 rank 排名
     *  3秒一次
     * @param statisService
     */
    @Override
    public void scheduleUpdateInvokersRank(StatisService statisService) {
        if(!startTask){
            startTask = true;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    // cal rank
                    Map<String,Double> statisResult = statisService.getStatis();
                    for(Map.Entry<String,Double> entry:statisResult.entrySet()){
                        InvokerWrapper invokerWrapper = rankInfoMap.get(entry.getKey());
                        if(invokerWrapper!=null){
                            invokerWrapper.getMonitorInfoBean().setAvgCost(entry.getValue());
                        }

                    }

                    double total = 0;
                    for(Map.Entry<String,InvokerWrapper> entry:rankInfoMap.entrySet()){
                        total+= entry.getValue().getMonitorInfoBean().getScore();
                    }

                    //System.out.println("scheduleAtFixedRate total score="+total);
                    TreeMap<Double, InvokerWrapper> treeMap = new TreeMap<>();
                    cacheinvokerList.clear();

                    for(Map.Entry<String,InvokerWrapper> entry:rankInfoMap.entrySet()){
                        double rank = entry.getValue().getMonitorInfoBean().getScore()/total;
                        entry.getValue().setRankScore(rank);
                        cacheinvokerList.add(entry.getValue());
                    }
                    Collections.sort(cacheinvokerList);

                    total = 0;
                    for(InvokerWrapper invokerWrapper:cacheinvokerList){
                        total+=invokerWrapper.getRankScore();
                        treeMap.put(total,invokerWrapper);
                       // System.out.println("rank score="+total+" invokerId="+invokerWrapper.getInvokerId());
                    }
                    if(rankCache.isEmpty()){
                        rankCache.add(treeMap);
                    } else {
                        rankCache.set(0,treeMap);
                    }
                }
            },0,PERIOD);


        }
    }
}