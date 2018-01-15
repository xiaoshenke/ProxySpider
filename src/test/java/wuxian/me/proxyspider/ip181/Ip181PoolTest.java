package wuxian.me.proxyspider.ip181;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by wuxian on 15/1/2018.
 */
public class Ip181PoolTest {

    @Test
    public void testPoll() {
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            Ip181Pool.put(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", random.nextInt(100) + 10000)));
        }

        List<Proxy> list = Ip181Pool.getProxy(10);
        System.out.println(list.toString());


    }

}