package wuxian.me.proxyspider.xun;

import com.sun.istack.internal.NotNull;
import okhttp3.Headers;
import wuxian.me.spidercommon.util.FileUtil;
import wuxian.me.spidersdk.anti.UserAgentManager;
import wuxian.me.spidersdk.util.CookieManager;

import static wuxian.me.spidercommon.util.FileUtil.getCurrentPath;

/**
 * Created by wuxian on 20/6/2017.
 */
public class Helper {

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

    public static Headers getHeaderBySpecifyRef(@NotNull String reference, @NotNull String spiderName) {
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

    public static Headers getXunSpiderHeader(@NotNull String reference) {
        builder.set("Host", "www.xdaili.cn");
        return getHeaderBySpecifyRef(reference, "Xun");
    }
}
