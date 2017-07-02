package wuxian.me.proxyspider;

import wuxian.me.proxyspider.xun.Helper;
import wuxian.me.spidermaster.biz.agent.provider.ProviderScanner;
import wuxian.me.spidersdk.IJobManager;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.manager.JobManagerFactory;

/**
 * Created by wuxian on 20/6/2017.
 */
public class Main {

    public static void main(String[] args) {

        ProviderScanner.performCheckAndCollect(ProxyProvider.class);

        JobManagerConfig.init();

        IJobManager jobManager = JobManagerFactory.getJobManager();
        jobManager.start();

        Helper.dispatchXunSpider();
    }
}
