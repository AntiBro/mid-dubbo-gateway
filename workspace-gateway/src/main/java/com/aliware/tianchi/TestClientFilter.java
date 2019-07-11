package com.aliware.tianchi;

import com.aliware.tianchi.support.NetUtil;
import com.aliware.tianchi.support.StatisService;
import com.aliware.tianchi.support.impl.StatisServiceImpl;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author daofeng.xjf
 *
 * 客户端过滤器
 * 可选接口
 * 用户可以在客户端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.CONSUMER)
public class TestClientFilter implements Filter {

    private static StatisService statisService = StatisServiceImpl.create();
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long startTime = System.currentTimeMillis();
        long endtime = 0;
        try{
            Result result = invoker.invoke(invocation);
            return result;
        }catch (Exception e){
            endtime = endtime + 1000;
            throw e;
        }finally {
            endtime = endtime + System.currentTimeMillis();
            long cost = endtime - startTime;
            statisService.addInvokerCostTime(invoker.getUrl(), cost);

        }

    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        return result;
    }
}
