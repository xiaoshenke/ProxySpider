package wuxian.me.proxyspider.ip181;

import okhttp3.HttpUrl;
import okhttp3.Request;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.proxyspider.Helper;
import wuxian.me.proxyspider.ProxySpiderCallback;
import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidercommon.model.HttpUrlNode;
import wuxian.me.spidercommon.model.Proxy;
import wuxian.me.spidercommon.util.ParsingUtil;
import wuxian.me.spidercommon.util.StringUtil;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.SpiderCallback;
import wuxian.me.spidersdk.anti.BytesCharsetDetector;
import wuxian.me.spidersdk.anti.MaybeBlockedException;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import static wuxian.me.spidercommon.util.ParsingUtil.firstChildIfNullThrow;
import static wuxian.me.spidercommon.util.ParsingUtil.matchedInteger;
import static wuxian.me.spidercommon.util.ParsingUtil.matchedString;

/**
 * Created by wuxian on 22/7/2017.
 * http://www.ip181.com/
 */
public class Ip181Spider extends BaseSpider {

    private static String URL = "http://www.ip181.com/";

    public static HttpUrlNode toUrlNode(Ip181Spider spider) {
        HttpUrlNode node = new HttpUrlNode();
        node.baseUrl = URL;
        return node;
    }

    public static Ip181Spider fromUrlNode(HttpUrlNode node) {
        if (node.baseUrl.contains(URL)) {
            return new Ip181Spider();
        }
        return null;
    }

    protected SpiderCallback getCallback() {
        return new ProxySpiderCallback(this);
    }

    protected Request buildRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL)
                .newBuilder();

        Request request = new Request.Builder()
                .headers(Helper.getIp181SpiderHeader("http://www.ip181.com/"))
                .url(urlBuilder.build().toString())
                .build();
        return request;
    }

    private void parseTime(String data) throws MaybeBlockedException, ParserException {

        Parser parser = new Parser(data);
        parser.setEncoding("gb2312");
        HasAttributeFilter filter = new HasAttributeFilter("class", "gx");
        Node node = firstChildIfNullThrow(parser.extractAllNodesThatMatch(filter));

        LogManager.info("time: " + StringUtil.removeAllBlanks(node.toPlainTextString()));
        Integer minute = matchedInteger(MINUTE_PATTERN, node.toPlainTextString());
        Integer second = matchedInteger(SECONDE, node.toPlainTextString());

        if (minute == null && second == null) {
            throw new MaybeBlockedException();
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
            throw new MaybeBlockedException();
        }

        LogManager.info("we will dispatch another Ip181Spider after " + time + " seconds");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                LogManager.info("Ip181Spider now awaken,try get current Ip181 web proxies");
                Helper.dispatchSpider(new Ip181Spider());
            }
        }, time * 1000);

    }

    private void parseIPList(String data) throws MaybeBlockedException, ParserException {
        Parser parser = new Parser(data);
        parser.setEncoding("gb2312");
        HasAttributeFilter filter = new HasAttributeFilter("class", "table table-hover panel-default panel ctable");
        Node node = firstChildIfNullThrow(parser.extractAllNodesThatMatch(filter));

        NodeList list = ParsingUtil.childrenOfType(node.getChildren(), TableRow.class);
        for (int i = 0; i < list.size(); i++) {
            parseIP(list.elementAt(i));
        }
    }

    private void parseIP(Node node) throws MaybeBlockedException
            , ParserException {

        String s = node.toPlainTextString().trim();
        String ip = matchedString(IP_PATTERN, s);
        if (ip == null) {
            return;
        }
        int index = s.indexOf(ip);
        if (index == -1) {
            return;
        }

        Ip181Pool.put(new Proxy(matchedString(IP_PATTERN, s), matchedInteger(PORT_PATTERN, s.substring(index + ip.length()))));

    }

    public static final String REG_IP = "[0-9.]+(?=\\s)";
    public static final Pattern IP_PATTERN = Pattern.compile(REG_IP);

    public static final String REG_PORT = "[0-9]+(?=\\s)";
    public static final Pattern PORT_PATTERN = Pattern.compile(REG_PORT);

    public static final String REG_SECOND = "[0-9]+(?=秒)";
    public static final Pattern SECONDE = Pattern.compile(REG_SECOND);

    public static final String REG_MINUTE = "(?<=，)[0-9]+(?=分钟)";
    public static final Pattern MINUTE_PATTERN = Pattern.compile(REG_MINUTE);


    @Override
    public int parseRealData(String data) {
        try {
            parseTime(data);

            Ip181Pool.clear();

            parseIPList(data);

        } catch (MaybeBlockedException e) {
            return BaseSpider.RET_MAYBE_BLOCK;

        } catch (ParserException e) {
            return BaseSpider.RET_PARSING_ERR;

        }
        return BaseSpider.RET_SUCCESS;
    }

    protected boolean checkBlockAndFailThisSpider(String s) {
        LogManager.error(s);
        return true;
    }

    public String name() {
        return "Ip181Spider";
    }

    public String hashString() {
        return name() + System.currentTimeMillis();
    }

}
