package com.aliware.tianchi;

import com.aliware.tianchi.support.MonitorUtil;
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

                            if(IDHolder.getID() != null ){
                                entry.getValue().receiveServerMsg(MonitorUtil.getMonitorInfoMsg(IDHolder.getID())); // send notification for change

                            }
                           // entry.getValue().receiveServerMsg(System.getProperty("quota") + " " + new Date().toString());
                        } catch (Throwable t1) {
                            //listeners.remove(entry.getKey());
                        }
                    }
                }
            }
        }, 0, 100);
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
       // MonitorInfoBean monitorInfoBean = MonitorUtil.getMonitorInfoBean();
        if(IDHolder.getID() != null ){
//            Gson gson = new Gson();
//            monitorInfoBean.setProviderId(IDHolder.getID());
            listener.receiveServerMsg(MonitorUtil.getMonitorInfoMsg(IDHolder.getID())); // send notification for change

        }
    }
}
