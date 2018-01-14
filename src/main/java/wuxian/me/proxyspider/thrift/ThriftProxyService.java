package wuxian.me.proxyspider.thrift;

import org.apache.thrift.TException;
import wuxian.me.proxyspider.ip181.Ip181Pool;
import wuxian.me.proxyspider.thrift.proto.TProxy;
import wuxian.me.proxyspider.thrift.proto.ProxyService;
import wuxian.me.proxyspider.xun.XunData;
import wuxian.me.proxyspider.xun.XunProxyPool;
import wuxian.me.spidercommon.log.LogManager;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 13/1/2018.
 */
public class ThriftProxyService implements ProxyService.Iface {

    public TProxy getProxy() throws TException {

        LogManager.info("in ThriftProxyService.getProxy");
        XunData xunData = XunProxyPool.getXunProxy();
        if (xunData != null) {
            //java.net.Proxy proxy = new java.net.Proxy(xunData.ip, Integer.parseInt(xunData.port));
            TProxy p = new TProxy(xunData.ip, Integer.parseInt(xunData.port));
            return p;
        }

        java.net.Proxy proxy = Ip181Pool.getProxy();
        if (proxy == null) {
            LogManager.info("no proxy available,return null");
            return null;//new TProxy(null, -1);      //thrift不允许返回null
        }
        LogManager.info("provide return " + proxy.toString());


        InetSocketAddress ad = (InetSocketAddress) proxy.address();
        return new TProxy(ad.getHostName(), ad.getPort());
    }

    //TODO:
    public List<TProxy> getProxy(int num) throws TException {
        TProxy p = getProxy();

        List<TProxy> list = new ArrayList<TProxy>();
        if (p != null) {
            list.add(p);
        }
        return list;
    }
}
