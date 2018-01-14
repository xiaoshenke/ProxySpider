
namespace java wuxian.me.proxyspider.thrift.proto

struct TProxy {
    1: string ip
    2: i32 port
}

service ProxyService {
  list<TProxy> getProxy(1:i32 num),
}

