package com.aliware.tianchi;

import com.aliware.tianchi.support.DynamicRouteService;
import com.aliware.tianchi.support.MonitorInfoBean;
import com.aliware.tianchi.support.MonitorUtil;
import com.aliware.tianchi.support.impl.DynamicRouteServiceImpl;
import org.apache.dubbo.rpc.listener.CallbackListener;

/**
 * @author daofeng.xjf
 *
 * 客户端监听器
 * 可选接口
 * 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 *
 */
public class CallbackListenerImpl implements CallbackListener {

    private static DynamicRouteService dynamicRouteService = DynamicRouteServiceImpl.create();

    @Override
    public void receiveServerMsg(String msg) {
       // Gson gson = new Gson();
//        MonitorInfoBean monitorInfoBean = MonitorUtil.parseStr(msg);
//        dynamicRouteService.updateInvokersRankByProviderMetaInfo(monitorInfoBean.getProviderId(),monitorInfoBean);
        //System.out.println("receive msg from server :" + msg);
    }

}
