package wuxian.me.proxyspider;

import okhttp3.Headers;
import wuxian.me.proxyspider.xun.XunProxySpider;
import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidercommon.util.FileUtil;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.IJobManager;
import wuxian.me.spidersdk.anti.UserAgentManager;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.manager.JobManagerFactory;
import wuxian.me.spidersdk.util.CookieManager;

import java.util.concurrent.atomic.AtomicLong;

import static wuxian.me.spidercommon.util.FileUtil.getCurrentPath;

/**
 * Created by wuxian on 20/6/2017.
 */
public class Helper {

    private static AtomicLong SCHEDULE_TIME = new AtomicLong(-1);

    public static void recordTime() {
        SCHEDULE_TIME = new AtomicLong(System.currentTimeMillis());
    }

    public static boolean shouldDispatchAotherRequest() {
        if (SCHEDULE_TIME.get() == -1) {
            return true;
        }

        if (System.currentTimeMillis() - SCHEDULE_TIME.get() >= 1000 * 60 * 3) {
            return true;
        }

        return false;
    }

    public static void dispatchSpider(BaseSpider spider) {
        IJob job = JobProvider.getJob();
        job.setRealRunnable(spider);
        jobManager().putJob(job);
    }

    public static void dispatchXunSpider() {
        if (shouldDispatchAotherRequest()) {
            //LogManager.info("success dispatchXunSpider");
            XunProxySpider spider = new XunProxySpider();
            dispatchSpider(spider);
            recordTime();
        }
    }

    private static IJobManager sJobManager;

    private static IJobManager jobManager() {
        if (sJobManager == null) {
            synchronized (Helper.class) {
                sJobManager = JobManagerFactory.getJobManager();
            }
        }
        return sJobManager;
    }

    private static final String HEADER_REFERER = "Referer";
    private static Headers.Builder builder;

    static {
        builder = new Headers.Builder();
        builder.add("Cookie", "");
        builder.add("Connection", "keep_alive");
        builder.add("Host", "www.lagou.com");
        builder.add(HEADER_REFERER, "abd");
        builder.add("User-Agent", "ab");
    }

    private Helper() {
    }

    public static String getCookieFilePath(String spiderName) {
        return getCurrentPath() + "/cookie/cookies_" + spiderName;
    }

    public static Headers getHeaderBySpecifyRef(String reference, String spiderName) {
        if (!CookieManager.containsKey(spiderName)) {
            if (FileUtil.checkFileExist(getCookieFilePath(spiderName))) {
                String content = FileUtil.readFromFile(getCookieFilePath(spiderName));
                if (content != null && content.length() != 0) {
                    CookieManager.put(spiderName, content);
                }
            }
        }
        builder.set("Cookie", CookieManager.get(spiderName));
        builder.set(HEADER_REFERER, reference);
        builder.set("User-Agent", UserAgentManager.getAgent());
        return builder.build();
    }

    public static Headers getXunSpiderHeader(String reference) {
        builder.set("Host", "www.xdaili.cn");
        return getHeaderBySpecifyRef(reference, "Xun");
    }

    public static Headers getIp181SpiderHeader(String reference) {
        builder.set("Host", "www.ip181.com");
        return getHeaderBySpecifyRef(reference, "ip181");
    }
}
