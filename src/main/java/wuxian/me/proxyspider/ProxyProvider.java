package wuxian.me.proxyspider;

import wuxian.me.proxyspider.ip181.Ip181Pool;
import wuxian.me.proxyspider.xun.XunData;
import wuxian.me.proxyspider.xun.XunProxyPool;
import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidercommon.model.Proxy;
import wuxian.me.spidermaster.biz.provider.IProvider;
import wuxian.me.spidermaster.biz.provider.Provider;
import wuxian.me.spidermaster.framework.common.GsonProvider;

/**
 * Created by wuxian on 20/6/2017.
 */

@Provider(provide = "proxy")
public class ProxyProvider implements IProvider<String> {

    public ProxyProvider() {
    }

    public String provide() {
        LogManager.info("in func ProxyProvider.provide");
        XunData xunData = XunProxyPool.getXunProxy();
        if (xunData != null) {
            Proxy proxy = new Proxy(xunData.ip, Integer.parseInt(xunData.port));
            LogManager.info("provide return " + proxy.toString());

            return GsonProvider.gson().toJson(proxy);
        }

        Proxy proxy = Ip181Pool.getProxy();
        if (proxy == null) {
            LogManager.info("no proxy available,return null");

            return null;
        }
        LogManager.info("provide return " + proxy.toString());

        return GsonProvider.gson().toJson(proxy);
    }
}
