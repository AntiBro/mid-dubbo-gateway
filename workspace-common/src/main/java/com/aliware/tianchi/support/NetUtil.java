package com.aliware.tianchi.support;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author huaili
 * @Date 2019/7/3 17:05
 * @Description NetUtil
 **/
public class   NetUtil {

    public static String hostToIP(String host){
        InetAddress address = null;
        try {
            address = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address.getHostAddress();

    }

    public static String getAddress(String host,int port){
        return hostToIP(host)+":"+port;
    }
}
