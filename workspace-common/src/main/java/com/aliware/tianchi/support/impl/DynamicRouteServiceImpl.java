package com.aliware.tianchi.support.impl;

import com.aliware.tianchi.support.*;
import org.apache.dubbo.rpc.Invoker;

import java.util.*;
import java.util.concurrent.*;

/**
 * @Author huaili
 * @Date 2019/7/4 15:52
 * @Description DynamicRouteServiceImpl
 **/
public class DynamicRouteServiceImpl implements DynamicRouteService {

    static volatile Boolean init = false;

    static volatile boolean startTask = false;

    static final int BOUND = 10;

    static final double BOUND_D = 0.99;

    static final int PERIOD = 200;

    static volatile  MapHolder mapholder = new MapHolder();

    static volatile CopyOnWriteArrayList<TreeMap<Double, InvokerWrapper>> rankCache = new CopyOnWriteArrayList();

    //List<InvokerWrapper> cacheinvokerList = new ArrayList<>();

    List<Invoker> list = new ArrayList<>();

    static volatile ConcurrentHashMap<String,InvokerWrapper> rankInfoMap = new ConcurrentHashMap<>();

   // private Timer timer = new Timer();

    private static ScheduledExecutorService scheduledExecutorService;


    private static volatile DynamicRouteService INSTANCE = new DynamicRouteServiceImpl();

    static StatisService statisService = StatisServiceImpl.create();

   // static Map<String, AtomicLong > countTotal = new HashMap<>();

    public static DynamicRouteService create(){
        return INSTANCE;
    }

    private DynamicRouteServiceImpl(){
        scheduledExecutorService = Executors.newScheduledThreadPool(1);

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                // cal rank
                Map<String, Double> statisResult = statisService.getStatis();
                if(statisResult.isEmpty()){
                    return;
                }

                for(Map.Entry<String,Double> entry:statisResult.entrySet()){
                    InvokerWrapper invokerWrapper = rankInfoMap.get(entry.getKey());
                    if(invokerWrapper!=null){
                        invokerWrapper.getMonitorInfoBean().setAvgCost(entry.getValue());
                    }

                }

                double total = 0;
                for(Map.Entry<String,InvokerWrapper> entry:rankInfoMap.entrySet()){
                    total+= entry.getValue().getMonitorInfoBean().getCalacScore();
                }

                TreeMap<Double, InvokerWrapper> treeMap = new TreeMap<>();

                List<InvokerWrapper> cacheinvokerList = new ArrayList<>();

                for(Map.Entry<String,InvokerWrapper> entry:rankInfoMap.entrySet()){
                    double rank = entry.getValue().getMonitorInfoBean().getScore()/total;
                    entry.getValue().setRankScore(rank);
                    //System.out.println("invoker= "+entry.getValue().getInvokerId()+" rank"+rank);
                    cacheinvokerList.add(entry.getValue());
                }
                //Collections.sort(cacheinvokerList);

                total = 0;
                for(InvokerWrapper invokerWrapper:cacheinvokerList){
                    total+=invokerWrapper.getRankScore();
                    treeMap.put(total,invokerWrapper);
                }
                mapholder.write(treeMap);

               // System.out.println("copyList size="+rankInfoMap.size());

//                for(Map.Entry<String,AtomicLong> entry:countTotal.entrySet()){
//                    System.out.println("invokerId= "+entry.getKey()+"  shotCount="+entry.getValue().get());
//                }
            }


        },800,PERIOD,TimeUnit.MILLISECONDS);

    }

    @Override
    public void sortInvokers() {

    }

    @Override
    public <T> Invoker<T> getInvoker() {
        double score = ThreadLocalRandom.current().nextDouble(BOUND_D);
        TreeMap<Double, InvokerWrapper> map = (TreeMap) mapholder.read();
        InvokerWrapper invokerWrapper = map.ceilingEntry(score).getValue();
        return invokerWrapper.getInvoker();
    }

    @Override
    public <T> void initInvokersRank(List<Invoker<T>> invokers) {
        if(!init){
            synchronized (init) {
                if(!init) {
                    TreeMap<Double, InvokerWrapper> map = new TreeMap<>();
                    double score = 1.00 / invokers.size();
                    double ceilScore = 0;
                    for (Invoker invoker : invokers) {
                        ceilScore += score;
                        InvokerWrapper invokerWrapper = InvokerWrapper.buildWrapper(invoker);
                        map.put(ceilScore, InvokerWrapper.buildWrapper(invoker));
                        rankInfoMap.put(invokerWrapper.getInvokerId(), invokerWrapper);
                    }
                    //rankCache.add(map);
                    mapholder.write(map);

                    statisService.initInvokers(invokers);

                    list.addAll(invokers);

                    init = true;
                }
            }
        }
    }

    @Override
    public void updateInvokersRankByProviderMetaInfo(String invokerId, MonitorInfoBean monitorInfoBean) {
        InvokerWrapper invokerWrapper = rankInfoMap.get(invokerId);
        if(invokerWrapper != null){
            // update free memory for invoker
            MonitorInfoBean monitorInfoBean1 = invokerWrapper.getMonitorInfoBean();
            if(monitorInfoBean.getFreeMem()>0)
                monitorInfoBean1.setFreeMem(monitorInfoBean.getFreeMem());
            if(monitorInfoBean.getCoreCount()>0)
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
        if(!startTask && init){
            startTask = true;

        }
    }
}
