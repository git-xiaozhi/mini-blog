1、系统全局上采用分布式SOA架构 阿里的Dubbo分布式架构
2、数据库是redis+mongoDB(两种NOSQL数据库)
2、采用Solr作为检索应用，通过Dubbo框架进行分布式部署。即将索引创建和检索放到一个单独应用中。
3、主微博系统采用Spring+Mybatis+FreeMarker+Spring-Security架构而成。
4、微博系统集成了新浪微博开放平台，实现对新浪微博的同步管理部分功能。