
namespace java wuxian.me.proxyspider.thrift.proto

struct Proxy {
    1: string ip
    2: i32 port
}

service ProxyService {
  Proxy getProxy(),
}

