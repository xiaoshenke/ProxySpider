package wuxian.me.proxyspider.xun;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by wuxian on 20/6/2017.
 */
public class XunProxyPool {

    private XunProxyPool() {
    }

    private static Queue<XunData> xunProxyQueue = new ArrayBlockingQueue<XunData>(50);

    public static boolean put(XunData xunData) {
        if (xunData == null) {
            return false;
        }

        xunProxyQueue.add(xunData);

        return true;
    }

    public static XunData getXunProxy() {
        if (xunProxyQueue.isEmpty()) {
            return null;
        }

        return xunProxyQueue.poll();
    }
}
