package com.aliware.tianchi.support;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author huaili
 * @Date 2019/7/3 11:25
 * @Description DyRandomTest
 **/
public class DyRandomTest {


    Map<String,String> map = new HashMap<>();

    TreeMap<Double,String> mapRate = new TreeMap<>();

   static CopyOnWriteArrayList<TreeMap<Double,String>> treeMapslist = new CopyOnWriteArrayList();

    static class MonitorInfoBean implements Comparable<MonitorInfoBean> {

        private int coreCount;

        private double rateOfCpu;

        private double score;

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public double getScore(){
            score = 100*coreCount*(1-rateOfCpu/100.00);
            return score;
        }
        public int getCoreCount() {
            return coreCount;
        }

        public void setCoreCount(int coreCount) {
            this.coreCount = coreCount;
        }

        public double getRateOfCpu() {
            return rateOfCpu;
        }

        public void setRateOfCpu(double rateOfCpu) {
            this.rateOfCpu = rateOfCpu;
        }

        @Override
        public int compareTo(MonitorInfoBean o) {
            return this.getScore()>o.getScore()?1:-1;
        }
    }


    public static void updateScoreTreeMap(List<MonitorInfoBean> list){
        Collections.sort(list);
        double totalSocre = 0;
        for(MonitorInfoBean e:list){
            totalSocre += e.getScore();
        }
        TreeMap<Double,String> mapRate = new TreeMap<>();
        double rate = 0;
        for(MonitorInfoBean e:list){
            rate += e.getScore()/totalSocre;
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

        List<MonitorInfoBean> list = new ArrayList<>();

        MonitorInfoBean m1 = new MonitorInfoBean();
        MonitorInfoBean m2 = new MonitorInfoBean();
        MonitorInfoBean m3 = new MonitorInfoBean();

        m1.setCoreCount(1);
        m1.setRateOfCpu(10);
        m1.setName("m1");

        m2.setCoreCount(2);
        m2.setRateOfCpu(10);
        m2.setName("m2");


        m3.setCoreCount(3);
        m3.setRateOfCpu(10);
        m3.setName("m3");


        list.add(m1);
        list.add(m2);
        list.add(m3);

        updateScoreTreeMap(list);

        TreeMap map = new TreeMap();

        map.put("m1",1);
        map.put("m2",1);
        map.put("m3",1);



        //System.out.println(map.floorEntry(90).getValue());

        Random random = new Random();

        double score = random.nextInt(100)/100.00;
        int t1=0,t2=0,t3=0;

        for(int i=0;i<10000;i++){
            score = (double) random.nextInt(100)/100.00;
            String name = treeMapslist.get(0).ceilingEntry(score).getValue();
            System.out.println("score="+name);
            map.put(name,(Integer)map.get(name)+1);
        }


        Iterator iterator = map.entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iterator.next();
            System.out.println(entry.getKey()+":"+entry.getValue());

        }

        int kb = 1024*1024;
        // 可使用内存
        long totalMemory = Runtime.getRuntime().totalMemory() / kb;
        // 剩余内存
        long freeMemory = Runtime.getRuntime().freeMemory() / kb;
        // 最大可使用内存
        long maxMemory = Runtime.getRuntime().maxMemory() / kb;

        System.out.println(totalMemory);
        System.out.println(freeMemory);
        System.out.println(maxMemory);




    }
}
