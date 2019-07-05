package com.aliware.tianchi.support;

import org.apache.dubbo.rpc.Invoker;

/**
 * @Author huaili
 * @Date 2019/7/4 16:03
 * @Description InvokerWrapper
 **/
public class InvokerWrapper<T> implements Comparable<InvokerWrapper>{

    private Invoker<T> invoker;

    private String invokerId;

    private MonitorInfoBean monitorInfoBean;

    private double rankScore;

    public double getRankScore() {
        return rankScore;
    }

    public void setRankScore(double rankScore) {
        this.rankScore = rankScore;
    }

    public MonitorInfoBean getMonitorInfoBean() {
        return monitorInfoBean;
    }

    public void setMonitorInfoBean(MonitorInfoBean monitorInfoBean) {
        this.monitorInfoBean = monitorInfoBean;
    }

    public Invoker<T> getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    public String getInvokerId() {
        return invokerId;
    }

    public void setInvokerId(String invokerId) {
        this.invokerId = invokerId;
    }

    public static InvokerWrapper buildWrapper(Invoker t){
        InvokerWrapper invokerWrapper = new InvokerWrapper();
        invokerWrapper.setInvoker(t);
        invokerWrapper.setInvokerId(NetUtil.getAddress(t.getUrl().getHost(),t.getUrl().getPort()));
        invokerWrapper.setMonitorInfoBean(new MonitorInfoBean());
        return invokerWrapper;
    }

    @Override
    public int compareTo(InvokerWrapper o) {
        return this.getRankScore()>o.getRankScore()?1:-1;
    }
}
