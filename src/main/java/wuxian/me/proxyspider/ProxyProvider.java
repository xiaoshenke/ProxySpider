package wuxian.me.proxyspider;

import wuxian.me.proxyspider.xun.XunData;
import wuxian.me.proxyspider.xun.XunProxyPool;
import wuxian.me.spidercommon.model.Proxy;
import wuxian.me.spidermaster.framework.master.provider.IProvider;
import wuxian.me.spidermaster.framework.master.provider.Provider;

/**
 * Created by wuxian on 20/6/2017.
 */

@Provider(provide = "proxy")
public class ProxyProvider implements IProvider<Proxy> {

    public ProxyProvider() {
    }

    public Proxy provide() {
        XunData xunData = XunProxyPool.getXunProxy();
        if (xunData == null) {
            return null;
        }

        return new Proxy(xunData.ip, Integer.parseInt(xunData.port));
    }
}
