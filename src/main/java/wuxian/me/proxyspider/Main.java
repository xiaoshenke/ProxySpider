package wuxian.me.proxyspider;

import wuxian.me.proxyspider.ip181.Ip181Spider;
import wuxian.me.proxyspider.xun.XunProxySpider;
import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidermaster.biz.agent.provider.ProviderScanner;
import wuxian.me.spidersdk.IJobManager;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.anti.Fail;
import wuxian.me.spidersdk.manager.JobManagerFactory;

import java.net.URLEncoder;

/**
 * Created by wuxian on 20/6/2017.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        ProviderScanner.performCheckAndCollect(ProxyProvider.class);

        JobManagerConfig.init();

        JobManagerConfig.isAgent = false;
        JobManagerConfig.isMaster = false;
        IJobManager jobManager = JobManagerFactory.getJobManager();
        jobManager.start();

        //Helper.dispatchXunSpider();

        Helper.dispatchSpider(new Ip181Spider());
    }
}
