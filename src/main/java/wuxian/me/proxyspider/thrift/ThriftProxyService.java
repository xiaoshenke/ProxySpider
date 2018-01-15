package wuxian.me.proxyspider.thrift;

import org.apache.thrift.TException;
import wuxian.me.proxyspider.ip181.Ip181Pool;
import wuxian.me.proxyspider.thrift.proto.TProxy;
import wuxian.me.proxyspider.thrift.proto.ProxyService;
import wuxian.me.proxyspider.xun.XunData;
import wuxian.me.proxyspider.xun.XunProxyPool;
//import wuxian.me.spidercommon.log.LogManager;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 13/1/2018.
 */
public class ThriftProxyService implements ProxyService.Iface {

    public TProxy getProxy() throws TException {

        System.out.println("in ThriftProxyService.getProxy");
        XunData xunData = XunProxyPool.getXunProxy();
        if (xunData != null) {
            //java.net.Proxy proxy = new java.net.Proxy(xunData.ip, Integer.parseInt(xunData.port));
            TProxy p = new TProxy(xunData.ip, Integer.parseInt(xunData.port));
            return p;
        }

        java.net.Proxy proxy = Ip181Pool.getProxy();
        if (proxy == null) {
            System.out.println("no proxy available,return null");
            return null;//new TProxy(null, -1);      //thrift不允许返回null
        }
        System.out.println("provide return " + proxy.toString());


        InetSocketAddress ad = (InetSocketAddress) proxy.address();
        return new TProxy(ad.getHostName(), ad.getPort());
    }

    public List<TProxy> getProxy(int num) throws TException {
        List<Proxy> proxys = Ip181Pool.getProxy(num);
        System.out.println("try getProxy num: " + num + " return size: " + proxys.size());
        List<TProxy> list = new ArrayList<TProxy>();
        for (Proxy p : proxys) {
            InetSocketAddress ad = (InetSocketAddress) p.address();
            list.add(new TProxy(ad.getHostName(), ad.getPort()));
        }

        return list;
    }
}
