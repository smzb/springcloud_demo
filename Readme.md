前言:最近对Spring Cloud进行了系统的学习，在之前的项目中使用过阿里开源的Dubbo微服务框架；对比两者，Spring Cloud的功能可能更为全面一些，当然了具体采用哪种架构还是要根据项目的实际状况来决定，本篇文章主要记录Spring Cloud的一些知识点，参考了一些博客，会在文章中说明，写的不对的地方还希望大家共同来探讨，共同进步。  
GitHub地址
# 一.服务架构的演变史
该模块部分内容引用[服务端高并发分布式架构演进之路](https://segmentfault.com/a/1190000018626163#comment-area),推荐阅读。
## 1.1单体架构
![](https://user-gold-cdn.xitu.io/2020/1/6/16f7b1944cb33359?w=579&h=210&f=png&s=48046)
以淘宝作为例子。在网站最初时，应用数量与用户数都较少，可以把Tomcat和数据库部署在同一台服务器上。浏览器往www.taobao.com发起请求时，首先经过DNS服务器（域名系统）把域名转换为实际IP地址10.102.4.1，浏览器转而访问该IP对应的Tomcat。但是随着用户数的增长，Tomcat和数据库之间竞争资源，单机性能不足以支撑业务。
## 1.2集群架构
![](https://user-gold-cdn.xitu.io/2020/1/6/16f7b1ce67515247?w=401&h=330&f=png&s=61444)
随着用户量增加，单台服务器已经不能满足需求，在架构层面可以使用集群模式来缓解一定的压力，在多台服务器上分别部署Tomcat，使用反向代理软件（Nginx）把请求均匀分发到每个Tomcat中。此处假设Tomcat最多支持100个并发，Nginx最多支持50000个并发，那么理论上Nginx把请求分发到500个Tomcat上，就能抗住50000个并发。
## 1.3分布式微服务架构
![](https://user-gold-cdn.xitu.io/2020/1/6/16f7b20d20c1ec1c?w=682&h=536&f=png&s=178812)
如用户管理、订单、支付、鉴权等功能在多个应用中都存在，那么可以把这些功能的代码单独抽取出来形成一个单独的服务来管理，开发起来也很方便，每一个服务一个人来负责，或者一个人来负责多个服务，这样的服务就是所谓的微服务，应用和服务之间通过HTTP、TCP或RPC请求等多种方式来访问公共服务，每个单独的服务都可以由单独的团队来管理。此外，可以通过Dubbo、SpringCloud等框架实现服务治理、限流、熔断、降级等功能，提高服务的稳定性和可用性。
# 二.引入Spring Cloud
## 2.1 什么是Spring Cloud
Spring Cloud 是一系列框架的有序集合。我们能够使用基于 Spring Boot 设计的 Spring Cloud 方便快速的搭建起自己的可靠、协调一致的分布式系统
## 2.2 为什么要用Spring Cloud
常见的微服务框架有Dubbo、Spring Cloud,那么为什么要使用Spring Cloud来进行微服务的开发，原因主要有以下几个
* 产出于 Spring 大家族，Spring 在企业级开发框架中无人能敌，来头很大，可以保证后续的更新、完善。比如 Dubbo，目前基本不怎么维护。
* Spinrg Cloud基于Spring Boot来开发，我们都知道，Spring Boot可以个很快的来开发一个单独的微服务，所以应用起来更容易上手。
* 作为一个微服务治理的大家伙，考虑的很全面，几乎服务治理的方方面面都考虑到了，方便开发开箱即用。
* Spring Cloud 活跃度很高，教程很丰富，遇到问题很容易找到解决方案。

# 三.Spring Cloud的基础功能
## 3.1 Spring Cloud Eureka服务治理
这里我们会用到Spring Cloud Netflix，该项目是Spring Cloud的子项目之一，主要内容是对Netflix公司一系列开源产品的包装，它为Spring Boot应用提供了自配置的Netflix OSS整合。通过一些简单的注解，开发者就可以快速的在应用中配置一下常用模块并构建庞大的分布式系统。它主要提供的模块包括：服务发现（Eureka），断路器（Hystrix），智能路有（Zuul），客户端负载均衡（Ribbon）等。我们这里的核心内容就是服务发现模块：Eureka

服务治理模块是Spring Cloud的核心模块，我们开发的微服务很重要的一点就是无状态性，那么这些微服务内部之间是怎么来通信的？我们如何知道目前有多少个微服务在运行等等。就像在dubbo中的注册中心一样，Spring Cloud也提供了注册中心也就是我们要说的Eureka，Spring Cloud Eureka分为Server端和Client端，每一个Client启动的时候会将自己注册到Server中。  
$\color{red}{基于Spring Boot2.2.2.RELEASE、Spring Cloud Hoxton.SR1开发，一定要注意对应版本}$  
**Eureka Server**  pom.xml中引入

```
 <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.2.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
     <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

```
通过@EnableEurekaServer来声明一个Server

```
@SpringBootApplication
@EnableEurekaServer
public class MsesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsesApplication.class, args);
    }

}
```
yml文件配置为

```
spring:
  application:
    name: eureka-server

eureka:
  server:
    #关闭eureka的自我保护机制
    enable-self-preservation: false

  client:
    #是否从Eureka Server中拉取注册信息
    fetch-registry: false
    #是否将自己注册到Eureka Server中
    register-with-eureka: false
    service-url:
      defaultZone: http://localhost:8000/eureka/
```
启动之后访问yml文件中配置的端口即可看到对应的界面，我们可以可看到在界面中目前没有启动过的微服务实例

![](https://user-gold-cdn.xitu.io/2020/1/6/16f7b465582740c3?w=1902&h=591&f=png&s=63580)
接下来我们创建一个微服务，作为Eureka Client注册到Eureka中
pom中引入

```
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
            <version>1.4.7.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka-server</artifactId>
            <version>1.4.7.RELEASE</version>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```
yml中加入

```
spring:
  application:
    name: serviceD
    
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8000/eureka/
```
启动类加上

```
@SpringBootApplication
@EnableEurekaClient
public class MssdApplication {

    public static void main(String[] args) {
        SpringApplication.run(MssdApplication.class, args);
    }

}

```
启动成功之后，我们会发现访问Eureka Server端的页面时候，就可以看到SERVICED已经注册上去了。
## 3.2 Spring Cloud Ribbon负载均衡
Ribbon 是一个基于 HTTP 和 TCP 客户端的负载均衡器。Ribbon 可以在通过客户端中配置的 ribbonServerList 服务端列表去轮询访问以达到均衡负载的作用。

当 Ribbon 与 Eureka 联合使用时，ribbonServerList 会被 DiscoveryEnabledNIWSServerList 重写，扩展成从 Eureka 注册中心中获取服务端列表。同时它也会用 NIWSDiscoveryPing 来取代 IPing，它将职责委托给 Eureka 来确定服务端是否已经启动

按照3.1中的步骤，我们再创建一个SERVIVEA，并且将SERVIVEA的服务分别以不同的端口来启动，此时我们去访问Eureka Server的时候会发现SERVIVEA有两个单元运行
![](https://user-gold-cdn.xitu.io/2020/1/6/16f7b5aa4d2b6f72?w=1900&h=356&f=png&s=56246)

我们再去创建一个消费者SERVIVEB来调用SERVIVEA的接口，具体创建步骤和3.1中的一样  
在启动类中加入

```

@SpringBootApplication
@EnableDiscoveryClient
public class MssbApplication {

    public static void main(String[] args) {
        SpringApplication.run(MssbApplication.class, args);
    }

    /**
     * 默认使用轮巡机制负载均衡
     *
     * @param builder
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}

```
创建controller来调用SERVICEA的接口

```
@RestController
@RequestMapping("/class")
public class ClassController {
    private static final String REST_URL_PREFIX = "http://SERVICEA";

    @Resource
    private RestTemplate restTemplate;


    @GetMapping("/getUserClass")
    public String getUserClass() {
        return restTemplate.getForEntity(REST_URL_PREFIX + "/user/getUser", String.class).getBody();
    }
}

```
启动SERVIVEB访问多次class/getUserClass，我们会发现SERVIVEA的两个实列是被轮询调用的。

未完待续！！！！

## 3.3 Spring Cloud Hystrix服务容错保护
## 3.4 Spring Cloud Fegin声明式服务调用
Feign是一个声明式的Web Service客户端，它使得编写Web Serivce客户端变得更加简单。我们只需要使用Feign来创建一个接口并用注解来配置它既可完成。它具备可插拔的注解支持，包括Feign注解和JAX-RS注解。Feign也支持可插拔的编码器和解码器。Spring Cloud为Feign增加了对Spring MVC注解的支持，还整合了Ribbon和Eureka来提供均衡负载的HTTP客户端实现。
## 3.5 Spring Cloud Zuul服务网关
## 3.6 Spring Cloud Config分布式配置中心
## 3.7 Spring Cloud Bus消息总线
## 3.8 Spring Cloud Sleuth分布式链路追踪
## 3.9 Spring Cloud Stream消息驱动的微服务
## 3.10 总结
# 四.Spring Cloud版本与Spring Boot版本



参考资料  
* [从架构演进的角度聊聊Spring Cloud都做了些什么？](http://www.ityouknow.com/springcloud/2017/11/02/framework-and-springcloud.html)  
* [你想了解的「SpringCloud」都在这里](http://www.wmyskxz.com/2019/06/09/ni-xiang-liao-jie-de-springcloud-du-zai-zhe-li/#toc-heading-6)
* [服务端高并发分布式架构演进之路](https://segmentfault.com/a/1190000018626163#comment-area)
* [Spring Cloud构建微服务架构](http://blog.didispace.com/springcloud1/)
