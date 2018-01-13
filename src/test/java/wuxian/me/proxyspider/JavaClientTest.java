package wuxian.me.proxyspider;

import org.junit.Test;
import wuxian.me.proxyspider.thrift.JavaClient;
import wuxian.me.proxyspider.thrift.ThriftServer;

/**
 * Created by wuxian on 13/1/2018.
 */
public class JavaClientTest {

    @Test
    public void test() throws Exception {

        ThriftServer.main(null);
        Thread.sleep(2000);
        JavaClient.main(null);
        while (true) {

        }
    }

}