package wuxian.me.proxyspider;

import wuxian.me.easyexecution.biz.crawler.sougou.WechatJob;
import wuxian.me.easyexecution.core.event.Event;
import wuxian.me.easyexecution.core.event.EventHandler;
import wuxian.me.easyexecution.core.event.EventType;
import wuxian.me.easyexecution.core.executor.JobRunnerManager;
import wuxian.me.proxyspider.ip181.Ip181Crawler;

import java.net.PortUnreachableException;

/**
 * Created by wuxian on 15/1/2018.
 */
public class FetchProxyManager {

    private boolean started = false;
    private static JobRunnerManager manager = new JobRunnerManager();

    public FetchProxyManager(){

        manager.addEventHandler(new EventHandler() {

            public void handleEvent(Event event) {
                if (event.getType() == EventType.JOB_FINISHED) {
                    Object o = event.getData();
                    if (o instanceof Ip181Crawler) {
                        //TODO
                    }
                }
            }
        });

    }

    public void start() {
        if(started) {
            return;
        }
        started = true;

        try{
            manager.submitJob(new Ip181Crawler());
        } catch (Exception e) {

        }
    }

    public static void dispatchSpider() {
        try{
            manager.submitJob(new Ip181Crawler());
        } catch (Exception e) {

        }
    }
}
