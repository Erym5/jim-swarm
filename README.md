# jim-swarm
## demo简介
### 上游项目
[KT-Chat](https://github1s.com/KimTou/KT-Chat)(数据库业务)、
[crazy-IM](https://gitee.com/crazymaker/crazy_tourist_circle__im)(netty实现分布式架构)、<br>
1、改进Netty长连接模块。采用分布式思想重新进行架构，如：建立Netty、ConcurrentHashMap二级缓存；对Netty实例内部路由；采用异步类解决Nacos服务全局监听问题。
<br>2、对网络协议重新划分，使用protobuf序列化协议，划分Netty内部节点路由的协议栈及Netty与客户端的协议栈。

该项目主要基于上游项目进行整合，了解cloud开发流程，以及netty的分布式架构思想。实现登录测试，私聊测试
![img.png](img.png)
![img_1.png](img_1.png)
#### 私聊
![img_2.png](img_2.png)

### 感谢
[KT-Chat](https://github1s.com/KimTou/KT-Chat)(数据库业务)、
[crazy-IM](https://gitee.com/crazymaker/crazy_tourist_circle__im)(netty实现分布式架构)、
