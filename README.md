# 权限系统(upms)

&emsp;&emsp;**此项目是拥有单登录的权限管理系统，使用的技术包括sping springmvc mybatis mysql  redis shiro  zookeeper dubbo bootstrap jquery等**<br />
&emsp;&emsp;虽然学习Spring、SpringMVC已经有一段时间了，基本的知识点都有所学习，但是由于缺少完整的项目实践，总感觉没有什么收获。所以从<a href="https://gitee.com/">码云</a>上搜索相关项目，发现了Zheng这个项目，使用了Spring、SpringMVC及Mybatis，刚好是学习的内容，通过阅读源码后，我抽取了其中的权限系统，并自己码了一遍。实现之后，感觉收获颇多，在这里也跟大家分享所学到的知识，更多分享请戳[个人博客](https://leecp8.github.io)




> &emsp;&emsp;<a href="https://gitee.com/shuzheng/zheng">zheng</a>项目是基于Spring+SpringMVC+Mybatis分布式敏捷开发系统架构，提供整套公共微服务服务模块：内容管理、支付中心、用户管理（包括第三方）、微信平台、存储系统、配置中心、日志分析、任务和通知等，支持服务治理、监控和追踪，努力为中小型企业打造全方位J2EE企业级开发解决方案。

## 一 概述

&emsp;&emsp;由于是自学，再加上这个<a href="https://gitee.com/shuzheng/zheng">zheng</a>项目涉及到分布式架构的问题，所以在实现的过程中，去除了目前无法理解的知识如：处理高并发的服务降级。<br />
&emsp;&emsp;我实现的权限系统（Upms-system）只包括单点登录(shiro)、分布式服务（zookeeper+dubbo)这两个部分。

## 二 流程

###  2.1 系统开发上步骤

 > 1. 前端文件的整理
 > 1. 依赖框架及工具的下载、整理及测试
 > 1. 开发环境搭建
 > 1. 编程

### 2.2 系统部署

&emsp;&emsp;直接布署在tomcat容器中即可或者使用Jetty。

## 三 实现

### 1. 前端文件的整理

&emsp;&emsp;通过readme.md文件，可以得知前端相关文件被放在zheng-admin这个子模块中，所以直接将这个目录下的文件进行整理即可。后来才发现作者也抽取了这个框架模板文件，[here](https://gitee.com/shuzheng/zhengAdmin)

``` plain
目录结构如下
├── resources -- 前端资源文件
|    ├── dependence -- 第三方框架及插件
|    |    ├── bootstrap -- bootstrap前端框架
|    |    ├── plugins -- 相关依赖插件
|    ├── include -- CSS、JS 依赖引用集合
|    ├── site -- 系统特定的样式、Js、图片、自定义插件
|    |    ├── css -- 自定义样式文件目录
|    |    ├── js -- 自定义脚本文件目录 
|    |    ├── image -- 系统所需图片目录
|    |    └── plugin -- 自定义插件目录
```

### 2. 依赖框架及工具的下载、整理及测试

此系统需要用到的框架有<br />
[Spring容器](http://projects.spring.io/spring-framework/)<br />
[SpringMVC](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#mvc)<br />
[Shiro权限框架](http://shiro.apache.org/)<br />
[Mybatis持久化框架](http://www.mybatis.org/mybatis-3/zh/index.html)<br />
[Zookeeper服务调度框架](http://zookeeper.apache.org/)<br />
[Dubbo分布式服务框架](http://dubbo.io/)<br />
[Redis缓存数据库](https://redis.io/)<br />

---

* 当然这个系统用到的框架还不只这些，实际开发我们并不需要都去下载，在开发过程中，使用[Maven](http://maven.apache.org/)即可自动载入这些框架的依赖包。这里提供链接是方便大家去官网了解自己感兴趣的知识。
* **注意**，这其中有两个框架一定要下载的，就是[Zookeeper服务调度框架](http://zookeeper.apache.org/)和[Redis缓存数据库](https://redis.io/)这两个框架，因为需要开启相关的服务进程。

### 3. 开发环境搭建

&emsp;&emsp;使用Maven构建聚合项目即可

``` plain
目录结构如下
├── upms -- 用户权限管理系统
|    ├── upms-common -- upms系统公共模块
|    ├── upms-dao -- upms Dao文件及Model实体文件，使用Mybatis的Generator工具自动产生
|    ├── upms-client -- 集成upms依赖包，提供单点认证、授权、统一会话管理
|    ├── upms-api -- 远程服务接口
|    ├── upms-service -- 远程服务实现
|    └── upms-admin -- 权限系统管理界面，包含前端资源文件（即1中所整理的资源文件）
```

### 4. 编程

&emsp;&emsp;接下来就是具体的模块开发了，这里就不展开，后面会写成多篇文章对权限系统使用的插件还有具体的代码细节进行介绍。包括启动相关的介绍，[请戳这里](https://leecp8.github.io)

## 四 总结

&emsp;&emsp;整个项目编写完后，感觉收获还是挺多的，所以在这里也推荐大家，可以去了解这个项目[Zheng](https://gitee.com/shuzheng/zheng)，如果大家不想解整个项目，只对这个权限系统感兴趣的话，也可以直接从我的[github](https://github.com/LeeCP8/upms/tree/master/upms-admin/src/main/webapp/resources)那里直接clone这些资源文件，然后自己去开发。

---

*本人不是科班出身，是一个正在努力自学，希望能够转行的平凡人。如果文中有什么理解得不对的地方，请谅解，谢谢！*

### 导读

&emsp;&emsp;[Zheng](https://gitee.com/shuzheng/zheng) 这个项目里有许多相关开源框架的链接，大家感兴趣可以去看看咯。
