package com.aliware.tianchi;

import com.aliware.tianchi.support.NetUtil;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

/**
 * @author daofeng.xjf
 *
 * 服务端过滤器
 * 可选接口
 * 用户可以在服务端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.PROVIDER)
public class TestServerFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try{
            long startTime = System.currentTimeMillis();
            Result result = invoker.invoke(invocation);
            long endTime = System.currentTimeMillis();
            String id = NetUtil.getAddress(invoker.getUrl().getHost(),invoker.getUrl().getPort());
            //System.out.println("TestServerFilter invoker id=["+ id +"] cost="+(endTime-startTime)+"ms");
            if(IDUtil.getID() == null){
                IDUtil.setID(id);
            }
            return result;
        }catch (Exception e){
            throw e;
        }

    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        return result;
    }

}
