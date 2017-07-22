package wuxian.me.proxyspider.ip181;

import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidercommon.model.Proxy;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by wuxian on 20/6/2017.
 */
public class Ip181Pool {

    private Ip181Pool() {
    }

    private static Queue<Proxy> proxyQueue = new ArrayBlockingQueue<Proxy>(100);
    private static Set<Proxy> xunDataSet = new HashSet<Proxy>();

    public static void clear() {
        proxyQueue.clear();
    }

    public static boolean put(Proxy proxy) {
        if (proxy == null || xunDataSet.contains(proxy)) {
            return false;
        }
        proxyQueue.add(proxy);
        xunDataSet.add(proxy);
        return true;
    }

    public static Proxy getProxy() {
        LogManager.info("Ip181 pool,current size:" + proxyQueue.size());
        if (proxyQueue.isEmpty()) {
            LogManager.info("Ip181Pool is empty");
            //Helper.dispatchXunSpider();
            return null;
        }


        return proxyQueue.poll();
    }
}
