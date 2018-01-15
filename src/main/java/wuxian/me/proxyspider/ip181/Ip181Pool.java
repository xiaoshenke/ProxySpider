package wuxian.me.proxyspider.ip181;

//import wuxian.me.spidercommon.log.LogManager;
//import wuxian.me.spidercommon.model.Proxy;

import java.net.Proxy;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by wuxian on 20/6/2017.
 */
public class Ip181Pool {

    private static Object lock = new Object();

    private Ip181Pool() {
    }

    private static Queue<Proxy> proxyQueue = new ArrayBlockingQueue<Proxy>(100);
    private static Set<Proxy> xunDataSet = new HashSet<Proxy>();

    public static void clear() {
        proxyQueue.clear();
    }

    public static boolean put(Proxy proxy) {
        synchronized (lock) {
            if (proxy == null || xunDataSet.contains(proxy)) {
                return false;
            }
            proxyQueue.add(proxy);
            xunDataSet.add(proxy);
            return true;
        }

    }

    public static List<Proxy> getProxy(int num) {
        synchronized (lock) {
            System.out.println("Ip181 pool,current size:" + proxyQueue.size());
            if (proxyQueue.isEmpty()) {
                System.out.println("Ip181Pool is empty");
                return new ArrayList<Proxy>();
            }

            int real = num >= proxyQueue.size() ? proxyQueue.size() : num;
            List<Proxy> list = new ArrayList<Proxy>(real);
            for (int i = 0; i < real; i++) {
                list.add(proxyQueue.poll());
            }

            return list;
        }
    }

    public static Proxy getProxy() {
        System.out.println("Ip181 pool,current size:" + proxyQueue.size());
        if (proxyQueue.isEmpty()) {
            System.out.println("Ip181Pool is empty");
            //Helper.dispatchXunSpider();
            return null;
        }


        return proxyQueue.poll();
    }
}
