
namespace java wuxian.me.proxyspider.thrift.proto

struct TProxy {
    1: string ip
    2: i32 port
}

//Todo:带一个len参数
service ProxyService {
  TProxy getProxy(),
}

