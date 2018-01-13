package wuxian.me.proxyspider.thrift;

import org.apache.thrift.TException;
import wuxian.me.proxyspider.ip181.Ip181Pool;
import wuxian.me.proxyspider.thrift.proto.Proxy;
import wuxian.me.proxyspider.thrift.proto.ProxyService;
import wuxian.me.proxyspider.xun.XunData;
import wuxian.me.proxyspider.xun.XunProxyPool;
import wuxian.me.spidercommon.log.LogManager;

/**
 * Created by wuxian on 13/1/2018.
 */
public class ThriftProxyService implements ProxyService.Iface {

    public Proxy getProxy() throws TException {

        LogManager.info("in ThriftProxyService.getProxy");
        XunData xunData = XunProxyPool.getXunProxy();
        if (xunData != null) {
            wuxian.me.spidercommon.model.Proxy proxy = new wuxian.me.spidercommon.model.Proxy(xunData.ip, Integer.parseInt(xunData.port));
            Proxy p = new Proxy(proxy.ip, proxy.port);
            return p;
        }

        wuxian.me.spidercommon.model.Proxy proxy = Ip181Pool.getProxy();
        if (proxy == null) {
            LogManager.info("no proxy available,return null");
            return new Proxy(null, -1);      //thrift不允许返回null
        }
        LogManager.info("provide return " + proxy.toString());

        return new Proxy(proxy.ip, proxy.port);
    }
}
