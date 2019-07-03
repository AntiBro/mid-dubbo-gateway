package com.aliware.tianchi.support;

/**
 * @Author huaili
 * @Date 2019/7/3 17:16
 * @Description MonitorInfoBean
 **/
public class MonitorInfoBean implements Comparable<MonitorInfoBean> {

    private int coreCount;

    private double rateOfCpu;

    private double score;

    private String name;

    // MB
    long freeMem;

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
        score = 100*coreCount*(1-rateOfCpu/100.00)*freeMem;
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
