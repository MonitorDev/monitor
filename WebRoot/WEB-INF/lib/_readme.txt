antlr-2.7.6.jar			HIBERNATE需要它
apache-solr-solrj-1.4.2-dev.jar		solr的基础包，搜索引擎需要它
asm.jar				HIBERNATE需要它
bcprov-jdk15-143.jar			为工程提供RSA的加密方式，如果没有它RSA算法不可以使用。
cglib-2.1.3.jar			cglib包，hibernate需要它支持
commons-collections		apache 集合框架，dbcp需要它
commons-dbcp.jar			apache 连接池，工程中使用它作为首选连接池。暂不用C3P0
commons-digester			apache 用于处理XML的类，工程一般不用它，但DBCP用到了它所以也加进来。
commons-fileupload			apache上传支持，框架上传用的是它
commons-httpclient			apache http支持，上传需要它
commons-io-1.4.jar			apache io支持，上传需要它
commons-logging			apache的日志基础包，多个包依赖于它
commons-pool.jar			apache的池化支持，DBCP用到了它
dfish-security			提供加解密以及MAC信息读取。视图引擎依赖于它
dfish-xmltmpl2.jar			视图引擎包。(重要)现在是2.2版本，可能会不断更新
dom4j-1.6.1.jar			XML解析包，系统中XML依赖于它
dsearchClient.jar			搜索引擎客户端包。
google-gson-stream			谷歌json的包，流的部分
gson-1.7.1.jar			谷歌JSON的主包，远程接口需要
hibernate3.jar			hibernate的包(重要)现在是3.2版本
jaxen-1.1.1.jar			XML基础包，DOM4J需要他来处理XPath.
jta-1.1.jar				JAVA事务控制包。hibernate需要它
jxl.jar				用于操作EXCEL文档
log4j-1.2.15.jar			*工程中需要的包，用于做日志，但正式部署可能要去除，因为一般服务器都有此包
mail.jar				SUN提供的JAVAMAIL的基础包
ojdbc14.jar			ORACLE驱动
pinyin4j-2.5.0.jar			拼音支持包，按拼音排序的时候需要。
postgresql-8.2-505.jdbc3		postgres驱动
quartz-all-1.6.1.jar			定时器的包，结合spring制作定时。业界有名
spring.jar				SPRING框架主包(重要)现在是2.5版本
spring-web.jar			SPRING的web功能支持，spring-webmvc依赖于它
spring-webmvc.jar			SPRIGN的web应用框架。
xmltmpl2_license.jar			视图引擎的证书。