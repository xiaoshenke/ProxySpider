package wuxian.me.proxyspider.xun;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by wuxian on 20/6/2017.
 */
public class XunProxyPool {

    private XunProxyPool() {
    }

    private static Queue<XunData> xunProxyQueue = new ArrayBlockingQueue<XunData>(50);
    private static Set<XunData> xunDataSet = new HashSet<XunData>();

    public static boolean put(XunData xunData) {
        if (xunData == null || xunDataSet.contains(xunData)) {
            return false;
        }

        xunProxyQueue.add(xunData);
        xunDataSet.add(xunData);
        return true;
    }

    //TODO
    public static XunData getXunProxy() {

        return null;

        /*
        LogManager.info("xunproxy pool,current size:" + xunProxyQueue.size());
        if (xunProxyQueue.isEmpty()) {
            LogManager.info("xunPool is empty,try to dispatch XunSpider");
            Helper.dispatchXunSpider();
            return null;
        }

        return xunProxyQueue.poll();
        */
    }
}
