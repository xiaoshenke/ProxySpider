package wuxian.me.proxyspider.ip181;

import jdk.nashorn.internal.runtime.ParserException;
import wuxian.me.easyexecution.biz.crawler.BaseCrawerJob;
import wuxian.me.easyexecution.biz.crawler.util.*;


import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import wuxian.me.proxyspider.FetchProxyManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.regex.Pattern;

import static wuxian.me.easyexecution.biz.crawler.util.ParsingUtil.firstChildIfNullThrow;
import static wuxian.me.easyexecution.biz.crawler.util.ParsingUtil.matchedInteger;
import static wuxian.me.easyexecution.biz.crawler.util.ParsingUtil.matchedString;

/**
 * Created by wuxian on 15/1/2018.
 */
public class Ip181Crawler extends BaseCrawerJob {

    private static String URL = "http://www.ip181.com/";

    static {
        //URL = "http://www.baidu.com";
    }
    
    
    public void run() throws Exception {

        Map<String, String> pro = new HashMap<String,String>();
        pro.put("Host", "www.ip181.com");
        pro.put("Referer","http://www.ip181.com/");
        pro.put("Cookie", CookieManager.get("ip181", FileUtil.getCurrentPath() + "/cookie/ip181_cookie"));
        pro.put("Connection", "keep-alive");
        pro.put("User-Agent", UserAgentManager.getAgent());

        NetworkUtil.NetResponse res = null;
        boolean success = true;

        try {
            res = NetworkUtil.sendHttpRequest(URL, "GET", pro);
        } catch (IOException e) {
            success = false;
            e.printStackTrace();
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }

        if (!success) {
            //TODO:
            return;
        }

        if (res.retCode != 200) {
            //TODO:
            return;
        }

        try {
            parseRealData(res.body);
        } catch (ParserException e) {
            ;
        }
    }
    

    private void parseTime(String data) throws org.htmlparser.util.ParserException{

        Parser parser = new Parser(data);
        parser.setEncoding("gb2312");
        HasAttributeFilter filter = new HasAttributeFilter("class", "gx");
        Node node = firstChildIfNullThrow(parser.extractAllNodesThatMatch(filter));

        System.out.println("time: " + node.toPlainTextString());
        Integer minute = matchedInteger(MINUTE_PATTERN, node.toPlainTextString());
        Integer second = matchedInteger(SECONDE, node.toPlainTextString());

        if (minute == null && second == null) {
            //throw new MaybeBlockedException();
            return;
        }

        long time = 0;
        if (minute != null) {
            time += 60 * minute;
        }
        if (second != null) {
            time += second;
        }

        time = 10 * 60 - time;
        if (time < 0) {
            //throw new MaybeBlockedException();
            return;
        }

        System.out.println("we will dispatch another Ip181Spider after " + time + " seconds");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Ip181Spider now awaken,try get current Ip181 web proxies");
                //Helper.dispatchSpider(new Ip181Spider());
                FetchProxyManager.dispatchSpider();
            }
        }, time * 1000);

    }

    private List<Proxy> parseIPList(String data) throws org.htmlparser.util.ParserException {
        Parser parser = new Parser(data);
        parser.setEncoding("gb2312");
        HasAttributeFilter filter = new HasAttributeFilter("class", "table table-hover panel-default panel ctable");
        Node node = firstChildIfNullThrow(parser.extractAllNodesThatMatch(filter));

        NodeList list = ParsingUtil.childrenOfType(node.getChildren(), TableRow.class);

        List<Proxy> proxyList = new ArrayList<Proxy>();
        for (int i = 0; i < list.size(); i++) {
            Proxy proxy = parseIP(list.elementAt(i));

            if (proxy != null && !proxyList.contains(proxy)) {
                proxyList.add(proxy);
            }
        }
        return proxyList;
    }

    private Proxy parseIP(Node node) throws ParserException {

        String s = node.toPlainTextString().trim();
        String ip = matchedString(IP_PATTERN, s);
        if (ip == null) {
            return null;
        }
        int index = s.indexOf(ip);
        if (index == -1) {
            return null;
        }

        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(matchedString(IP_PATTERN, s),
                matchedInteger(PORT_PATTERN, s.substring(index + ip.length()))));

        //return new Proxy(matchedString(IP_PATTERN, s), matchedInteger(PORT_PATTERN, s.substring(index + ip.length())));
    }

    public static final String REG_IP = "[0-9.]+(?=\\s)";
    public static final Pattern IP_PATTERN = Pattern.compile(REG_IP);

    public static final String REG_PORT = "[0-9]+(?=\\s)";
    public static final Pattern PORT_PATTERN = Pattern.compile(REG_PORT);

    public static final String REG_SECOND = "[0-9]+(?=秒)";
    public static final Pattern SECONDE = Pattern.compile(REG_SECOND);

    public static final String REG_MINUTE = "(?<=，)[0-9]+(?=分钟)";
    public static final Pattern MINUTE_PATTERN = Pattern.compile(REG_MINUTE);

    
    public int parseRealData(String data) {
        try {
            parseTime(data);
            List<Proxy> list = parseIPList(data);

            if (list.size() == 0) {
                //throw new MaybeBlockedException();
                return -1;
            }

            Ip181Pool.clear();
            for (Proxy p : list) {
                Ip181Pool.put(p);
            }

        } catch (org.htmlparser.util.ParserException e) {
            return -1;

        }
        return 1;
    }
}
