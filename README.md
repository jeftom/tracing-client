## 分布式跟踪系统

### description
> 该项目作为zipkin [distributed tracing system] 的客户端，在业务系统中以dubbo插件的方式扩展了filter拦截点，用以收集dubbo的调用服务过程信息，并异步发送到zipkin collecter，通过中间存储（如es,mysql)，最后以 webui 呈现调用链，依赖树，调用时间，异常信息等。

### 如何使用
0. 项目需要JDK1.7+

1. 加入Maven依赖
```xml
<dependency>
  <groupId>com.bdfint</groupId>
  <artifactId>tracing-client</artifactId>
  <version>0.1</version>
</dependency>
`````````

2. 在dubbo的配置文件中，写入filter作为拦截器
```xml
    <dubbo:consumer filter="braveConsumerFilter" retries = "1" timeout="2000" />
    <dubbo:provider filter="braveProviderFilter" retries = "1" timeout="2000" />
```

3. 由于需要将dubbo的调用信息收集并发送到zipkin的服务端，此处默认了以zipkin.bdfint.com为服务器域名。可以自己配置到相应ip。

4. zipkin 直接到官网下载，或者直接用
```
wget -O zipkin.jar 'https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec'
```
需要jdk1.8，以jar包方式启动即可。
```
java -jar zipkin.jar
```
最后，browse to http://your_host:9411 to find traces!

