# question—community  校园问答社区[https://www.bewithu.net/](https://www.bewithu.net/)
### :elephant: 功能描述
*类似于知乎的问答交流系统：用户可以在网站上发表问题，文章，评论，发送私信交流。还提供了包括点赞，关注，timeline(feeds)推送，站内信消息通知，站内全文搜索等功能。*
*项目已上线试运行：您可以通过访问[https://www.bewithu.net/](https://www.bewithu.net/)来体验该项目。*

### :rabbit: 项目技术栈
#### SpringBoot+MyBatis+Velocity+Redis+Solr
##### SpringBoot
* maven 配置项目依赖
* intercepter 拦截器权限控制
* ioc 对象管理和配置
* aop 日志记录 (slf4j)
* spring事务的使用
* 项目主要层次结构：Controller+Service+Dao+Model

##### MyBatis
* SpringBoot整合MyBatis
* 注解配置普通SQL操作，结合xml配置动态复杂SQL

##### Velocity
* 选用此模板引擎进行界面渲染（有点老，1.5之后不支持，后期可改成freemaker）
* 调用Velocity的模板语言展示数据
* 构建宏函数，提供前端个性化展示模块

##### Redis
* 缓存（set存储实体的点赞数实现赞踩功能,srtset存储关注数实现排序）
* 异步消息队列实现功能解耦，消峰，加快响应（list实现消息队列，定义多种事件（评论，发布问题，点赞等），触发时加入消息队列，多线程构造消费者取出事件，实现异步处理）
* session共享（用来配合Nginx实现负载均衡）（实现中....）

#### Solr
* 配置中文分词IKAnalyzer
* 导入项目的数据库，使用solrj连接Solr服务，实现全站搜索
* 实时根据数据库变动更新数据库索引


### :bulb:项目的一些小亮点：
这个项目是自己目前为止做的最用心的项目了，在业务代码之外也自己也实现了一些觉得能拿得出手功能和技术，也是对自己能力的一次肯定吧。

* **Redis实现消息队列**
在项目结构设计之初，就考虑到了项目中有很多的事件机制需要使用消息队列来优化性能。一开始考虑的是使用MQ,Kafka等著名的消息队列。但在系统编写过程中由于社区的对象仅面向学院的师生，系统也没有设计成分布式的系统。就放弃了原先的想法。选用Redis来自己实现简单消息队列，反而能有更好的实时性，最后效果也不错。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191125163714444.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwODQzNjM5,size_16,color_FFFFFF,t_70)
* **前缀（trie）树实现敏感词过滤**
通过先对一个文本敏感词集合文件，构建出来包含敏感词前缀树。再对前缀树进行匹配，对用户在系统中的信息实现了敏感词的过滤，也包括一些html标签和sql语句的过滤。心得是在真实的系统开发中，也是要用到很多算法的。

* **结合不完全聚类算法，对热点内容进行排序展示** 
通常对于页面内容的排序是有现成的排序算法的。在知乎和各种论坛上，传统的做法是构造一个类似于Score = (P-1)/(T+2)^G 的公式，使分子为内容实体的点赞或关注数，分母为发布实体距离现在的时间跨度，但通常这一类热点都是十分相似的内容，导致在首页推送的内容没有新鲜感。
刚好在我以前研究机器学习的时候提出了一种类似微博热搜的不完全聚类算法。相关细节和实现在我的仓库中：[https://github.com/WeiKangJian/-IncompleteClustering](https://github.com/WeiKangJian/-IncompleteClustering)
于是我将此聚类算法的模块引入了我的这个web项目中，但该聚类算法的启动需要一部分真实的数据集，目前项目还没有足够的用户数据。于是目前将该部分仅封装一个接口包含在项目中，还未真实的使用此模块来进行排序和推荐内容。待后期用户数据足够，可进行此模块的热点聚集。

