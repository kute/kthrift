namespace java com.kute.thrift.simple.service

include "Types.thrift"
include "Status.thrift"
include "Exception.thrift"
include "Base.thrift"
include "Domain.thrift"

service  HelloWorldService extends Base.BaseService {

    Domain.ResponseCode sayHello(1:string username),

    map<string, i64> getCounters(),

    set<i32> getSet(),

    list<string> getList(),

    void setOption(1: string key, 2: string value) throws (1: Exception.InvalidOperation e),

    Domain.UserInfo getUser(),

    /**
    * oneway的方式是客户端将消息写入本地的网络缓冲区就直接返回，是否成功发送到服务端是由网络保证的。如果发送的速度慢或者客户端直接退出了，后面的消息就会丢失。
      而非oneway的方式是将消息发送给对方，服务端收到消息后，会给client端发送响应，这样就是同步的发送方式，保证消息能够成功发送给对方。
    **/
    oneway void release()
}