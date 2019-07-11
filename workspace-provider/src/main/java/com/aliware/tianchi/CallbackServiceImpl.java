package com.aliware.tianchi;

import com.aliware.tianchi.support.MonitorInfoBean;
import com.aliware.tianchi.support.MonitorUtil;
import com.google.gson.Gson;
import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端回调服务
 * 可选接口
 * 用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {

    public CallbackServiceImpl() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!listeners.isEmpty()) {
                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                        try {

                            MonitorInfoBean monitorInfoBean = MonitorUtil.getMonitorInfoBean();
                            if(IDUtil.getID() != null ){
                                Gson gson = new Gson();
                                monitorInfoBean.setProviderId(IDUtil.getID());
                                entry.getValue().receiveServerMsg(gson.toJson(monitorInfoBean)); // send notification for change

                            }
                           // entry.getValue().receiveServerMsg(System.getProperty("quota") + " " + new Date().toString());
                        } catch (Throwable t1) {
                            //listeners.remove(entry.getKey());
                        }
                    }
                }
            }
        }, 0, 25);
    }

    private Timer timer = new Timer();

    /**
     * key: listener type
     * value: callback listener
     */
    private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();

    @Override
    public void addListener(String key, CallbackListener listener) {
        listeners.put(key, listener);
        MonitorInfoBean monitorInfoBean = MonitorUtil.getMonitorInfoBean();
        if(IDUtil.getID() != null ){
            Gson gson = new Gson();
            monitorInfoBean.setProviderId(IDUtil.getID());
            listener.receiveServerMsg(gson.toJson(monitorInfoBean)); // send notification for change

        }
    }
}
