package com.aliware.tianchi.support;

public class CostTime {

    private Double costTime;
    private Long timeStramp;
    public CostTime(Double costTime,Long timeStramp){
        this.costTime = costTime;
        this.timeStramp = timeStramp;
    }

    public Double getCostTime() {
        return costTime;
    }

    public void setCostTime(Double costTime) {
        this.costTime = costTime;
    }

    public Long getTimeStramp() {
        return timeStramp;
    }

    public void setTimeStramp(Long timeStramp) {
        this.timeStramp = timeStramp;
    }
}
