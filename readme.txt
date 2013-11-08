1、系统采用分布式SOA架构 阿里的Dubbo分布式框架
2、数据库redis+mongoDB(两种NOSQL数据库)
3、采用Solr作为检索应用，通过Dubbo框架进行分布式部署。即将索引创建和检索放到一个单独应用中。
4、主微博系统采用Spring+NoSql+FreeMarker+Spring-Security架构而成。
5、微博系统集成了新浪微博开放平台，实现对新浪微博的同步管理部分功能。
6、采用Moosefs作为分布式小文件存储机制，存储微博图片。
7、基于dubbo框架的分布式图片处理服务
8、采用nginx作为图片发布服务器。
9、采用quartz自定义时间触发器功能并用mysql持久化job实现新浪微博定时发送。

需要启动的服务
redis  
mongodb 
solr服务器 
dubbo的注册服务器zookeeper
app-search Solr搜索和索引服务 
app-image-server 图片上传和处理服务
nginx图片发布服务器。
moosefs分布式文件存储引擎（启动master服务和挂接共享目录）
