package com.aliware.tianchi.support;

import sun.misc.Contended;

/**
 * @Author huaili
 * @Date 2019/7/3 17:16
 * @Description MonitorInfoBean
 **/
public class MonitorInfoBean implements Comparable<MonitorInfoBean> {

    private String providerId;

    private int coreCount = 1;

    private double rateOfCpu;

    private double score;

    private String name;

    // MB
    @Contended
    private long freeMem;

    @Contended
    private double avgCost = 1000L;


    public double getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(double avgCost) {
        this.avgCost = avgCost;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public long getFreeMem() {
        return freeMem;
    }

    public void setFreeMem(long freeMem) {
        this.freeMem = freeMem;
    }

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
        score = coreCount*freeMem/avgCost;
       // System.out.println("MonitorInfoBean score="+score+"  coreCount="+coreCount+"  freemMm="+freeMem+"  avgCost="+avgCost);
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
