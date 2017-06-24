package wuxian.me.proxyspider;

import wuxian.me.proxyspider.xun.XunProxySpider;
import wuxian.me.spidersdk.IJobManager;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.manager.JobManagerFactory;

/**
 * Created by wuxian on 20/6/2017.
 */
public class Main {

    public static void main(String[] args) {

        JobManagerConfig.init();

        IJobManager jobManager = JobManagerFactory.getJobManager();
        jobManager.start();

        IJob job = JobProvider.getJob();
        job.setRealRunnable(new XunProxySpider());
        //jobManager.putJob(job);
    }
}
