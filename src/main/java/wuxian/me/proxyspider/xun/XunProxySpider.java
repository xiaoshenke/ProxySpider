package wuxian.me.proxyspider.xun;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.Request;
import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidercommon.model.HttpUrlNode;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.SpiderCallback;
import wuxian.me.spidersdk.anti.MaybeBlockedException;

import java.util.List;

/**
 * Created by wuxian on 20/6/2017.
 */
public class XunProxySpider extends BaseSpider {

    private static String URL = "http://www.xdaili.cn/ipagent//freeip/getFreeIps";

    public static HttpUrlNode toUrlNode(XunProxySpider spider) {
        HttpUrlNode node = new HttpUrlNode();
        node.baseUrl = URL;
        node.httpGetParam.put("page", "1");
        node.httpGetParam.put("rows", "10");
        return node;
    }

    public static XunProxySpider fromUrlNode(HttpUrlNode node) {
        if (node.baseUrl.contains(URL)) {
            return new XunProxySpider();
        }
        return null;
    }

    protected SpiderCallback getCallback() {
        return new XunSpiderCallback(this);
    }

    protected Request buildRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL)
                .newBuilder();
        urlBuilder.addQueryParameter("page", "1")
                .addQueryParameter("rows", "10");

        Request request = new Request.Builder()
                .headers(Helper.getXunSpiderHeader("http://www.xdaili.cn/freeproxy.html"))
                .url(urlBuilder.build().toString())
                .build();
        return request;
    }

    public int parseRealData(String data) {
        LogManager.info(data);

        JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject) parser.parse(data);

        try {
            if (obj.get("rows").isJsonNull()) {
                throw new MaybeBlockedException();
            }
            JsonArray array = obj.getAsJsonArray("rows");

            List<XunData> list = new Gson().fromJson(array, new TypeToken<List<XunData>>() {
            }.getType());
            if (list == null || list.size() == 0) {
                throw new MaybeBlockedException();
            }

            for (XunData xunData : list) {
                XunProxyPool.put(xunData);
            }

        } catch (MaybeBlockedException e) {
            return BaseSpider.RET_MAYBE_BLOCK;
        }

        return BaseSpider.RET_SUCCESS;
    }

    protected boolean checkBlockAndFailThisSpider(String s) {
        return true;
    }

    public String name() {
        return "XunProxySpider";
    }

    public String hashString() {
        return name() + System.currentTimeMillis();
    }
}
