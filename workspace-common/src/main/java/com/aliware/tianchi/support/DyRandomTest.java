package com.aliware.tianchi.support;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author huaili
 * @Date 2019/7/3 11:25
 * @Description DyRandomTest
 **/
public class DyRandomTest {


    Map<String,String> map = new HashMap<>();

    TreeMap<Double,String> mapRate = new TreeMap<>();

   static CopyOnWriteArrayList<TreeMap<Double,String>> treeMapslist = new CopyOnWriteArrayList();




    public static void updateScoreTreeMap(List<MonitorInfoBean> list){
        Collections.sort(list);
        double totalSocre = 0;
        for(MonitorInfoBean e:list){
            totalSocre += e.getCalacScore();
        }
        TreeMap<Double,String> mapRate = new TreeMap<>();
        double rate = 0;
        for(MonitorInfoBean e:list){
            rate += e.getCalacScore()/totalSocre;
            mapRate.put(rate,e.getName());
        }
        if(treeMapslist.isEmpty()){
            treeMapslist.add(mapRate);
        }
        else {
            treeMapslist.set(0,mapRate);
        }
    }

    public static String getInvokerName(double rand){
        TreeMap<Double,String> map = treeMapslist.get(0);
        return map.floorEntry(rand).getValue();
    }

    private void init(){
        map.put("1","aaa");
        map.put("2","bbb");
        map.put("3","ccc");
    }
    public static void main(String[] args){

//        Random random = new Random();
//
//        double score = random.nextInt(100)/100.00;
//        int t1=0,t2=0,t3=0;
//
//
//
//
//        int kb = 1024*1024;
//        // 可使用内存
//        long totalMemory = Runtime.getRuntime().totalMemory() / kb;
//        // 剩余内存
//        long freeMemory = Runtime.getRuntime().freeMemory() / kb;
//        // 最大可使用内存
//        long maxMemory = Runtime.getRuntime().maxMemory() / kb;
//
//        System.out.println(totalMemory);
//        System.out.println(freeMemory);
//        System.out.println(maxMemory);
//
//        //System.out.println(MonitorUtil.getCpuRate());
//
//        MonitorInfoBean mm = new MonitorInfoBean();
//        mm.setCoreCount(4);
//        mm.setFreeMem(220);
//        mm.setAvgCost(1000);
//
//        System.out.println(mm.getCalacScore());


        int count = 10;
        int k = 10;

        AtomicLong atomicLong = new AtomicLong();
        while(k>0) {
            long startTimeNao = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                //ThreadLocalRandom.current().nextDouble(100.999);
               // System.nanoTime();
                Runtime.getRuntime().availableProcessors();
                //Runtime.getRuntime().freeMemory();
            }
            long endTimeNao = System.currentTimeMillis();
            System.out.println("nanao cost Time=" + (endTimeNao - startTimeNao));

            long startTimeCAS = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                atomicLong.incrementAndGet();
            }
            long endTimeCAS = System.currentTimeMillis();
            System.out.println("cas cost Time=" + (endTimeCAS - startTimeCAS));


            k--;
        }

       // System.out.println(atomicLong.get());

    }
}
