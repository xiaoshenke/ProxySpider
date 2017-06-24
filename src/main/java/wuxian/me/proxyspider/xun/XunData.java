package wuxian.me.proxyspider.xun;

/**
 * Created by wuxian on 20/6/2017.
 */
public class XunData {

    public String anony;

    public Long createTime;

    public int id;

    public int invalid;

    public String ip;

    public String port;

    public String position;

    public String responsetime;

    public String type;

    public String validatetime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XunData xunData = (XunData) o;

        if (id != xunData.id) return false;
        if (ip != null ? !ip.equals(xunData.ip) : xunData.ip != null) return false;
        return port != null ? port.equals(xunData.port) : xunData.port == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "XunData{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", validatetime='" + validatetime + '\'' +
                '}';
    }
}
