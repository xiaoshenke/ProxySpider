package wuxian.me.proxyspider;

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

    //Fixme:现在的序列化能力太弱了 返回只能是string....
    public String provide() {
        LogManager.info("ProxyProvider.provide");
        XunData xunData = XunProxyPool.getXunProxy();
        if (xunData == null) {
            return null;
        }

        return GsonProvider.gson().toJson(new Proxy(xunData.ip, Integer.parseInt(xunData.port)));
    }
}
