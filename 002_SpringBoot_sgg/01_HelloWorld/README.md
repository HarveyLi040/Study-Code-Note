##  1.Spring与SpringBoot

####  1.1 Spring生态圈

- spring官网：https://spring.io/projects/spring-boot

- spring能做什么：Spring的生态覆盖了web开发、数据访问、安全控制、分布式、消息服务、移动开发、批处理

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/001whatspringcando.png)



- springboot是一个“高层框架”，spring boot的底层是spring Framework,springboot 简化了spring的配置
- spring推出spring5之后有了重大升级，导致springboot2也发生重大升级，spring5中的一个重大升级就是引入了响应式编程。springboot2提供了两套技术方案：Reactive Stack和Servlet Stack（本课程第一阶段掌握Servlet Stack技术栈，第二阶段掌握Reactive Stack技术栈）：



![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/002.png)

- 内部源码设计：spring5内部源码基于JDK8实现，JDK8中引入了**接口的默认实现**，在spring5的底层设计中大量地使用适配器模式。基于Java8的一些新特性，如:接口默认实现。重新设计源码架构

#### 1.2 Spring Boot诞生时代背景

Spring Boot诞生时代背景：微服务、分布式、云原生

**微服务**

- 微服务是一种架构风格
- —个应用拆分为一组小型服务
- 每个服务运行在自己的进程内，也就是可独立部署和升级·服务之间使用轻量级HTTP交互
- 服务围绕业务功能拆分
- 可以由全自动部署机制独立部署
- 去中心化，服务自治。服务可以使用不同的语言、不同的存储技术

**分布式**

- 微服务服务的出现导致大型应用被拆分，继而导致分布式的出现

- 分布式的困难：
  - 远程调用
  - 服务发现
  - 负载均衡
  - 服务容错
  - 配置管理
  - 服务监控
  - 链路追踪
  - 日志管理
  - 任务调度

**云原生 Cloud Native**

- 云原生的困难
- 服务开发好以后，又涉及到部署和应用，这就要考虑原生应用如何上云。原生应用如何上云，Cloud Native

- 上云的困难

  - 服务自愈

  - 弹性伸缩

  - 服务隔离

  - 自动化部署

  - 灰度发布

  - 流量治理

- 上云的解决 —> 可关注大厂学院课程，学习路线如下：
  1. 初识云原生
  2. 深入Docker—容器化技术
  3. 掌握星际级容器编排Kubernetes
  4. Devops—实战企业CI/CD，构建企业云平台
  5. 拥抱新一代架构service Mesh与serverless
  6. 云上架构与场景方案实战

#### 1.3 Spring Boot特点

**springboot优点**

- Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run"  —> 能快速创建出生产级别的Spring应用
- Create stand-alone Spring applications  —>  创建独立Spring应用
- Embed Tomcat, Jetty or Undertow directly (no need to deploy WAR files) —> 内嵌web服务器
- Provide opinionated 'starter' dependencies to simplify your build configuration —> 自动starter依赖，简化构建配置
- Automatically configure Spring and 3rd party libraries whenever possible —> 自动配置Spring以及第三方功能
- Provide production-ready features such as metrics, health checks, and externalized configuration —> 提供生产级别的监控、健康检查及外部化配置
- Absolutely no code generation and no requirement for XML configuration —>无代码生成、无需编写XML

**SpringBoot缺点**

- 版本迭代快，需要时刻关注变化·封装太深，内部原理复杂，不容易精通

**spring与Spring Boot关系**

-  SpringBoot是整合Spring技术栈的一站式框架
- SpringBoot是简化Spring技术栈的快速开发脚手架

**SpringBoot官方文档架构**

- 官方文档地址：https://spring.io/projects/spring-boot#learn

 <table align="center">
    <tr>
    官方文档
        <th ><img src="https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/003.png" ><b>官方文档</b></th>
    </tr>
    <tr>
    </table>

 <table align="center">
    <tr>
    官方文档目录
    <th ><img src="https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/004.png" ></th>
    </tr>
    <tr>
    </table>

 <table align="center">
    官方文档目录
    <th ><img src="https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/005.png" ></th>
    </tr>
    </table>

 <table align="center">
    查看版本新特性：https://github.com/spring-projects/spring-boot/wiki#release-notes
    <th><img src="https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/006.png" >             </th>
    </tr>
    </table>



## 2. Spring Boot入门

#### 2.1 Hello World 入门案例

入门案例: 浏览器发送请求，响应Hello,Spring Boot2

- 实现一：将@ResponseBody放到方法上

```java
@Controller
public class HelloControllerOne {
      @ResponseBody
      @RequestMapping("/helloOne")
      public String handleRequest(){
          return "Hello,Spring Boot2";
      }
}
//访问http://localhost:8080/helloOne 返回Hello,Spring Boot2
```

- 写法二：将@ResponseBody放到类上

```java
@Controller
@ResponseBody
public class HelloControllerTwo {
      @RequestMapping("/helloTwo")
      public String handleRequest(){
          return "Hello,Spring Boot2";
      }
}
//http://localhost:8080/helloTwo 返回Hello,Spring Boot2
```

- 写法三：用@RestController替换@Controller和@ResponseBody

```java
//@RestController 等价于 @Controller加@ResponseBody
@RestController
public class HelloControllerThree {
      @RequestMapping("/helloThree")
      public String handleRequest(){
          return "Hello,Spring Boot2";
      }
}
// 访问http://localhost:8080/helloThree 返回 Hello,Spring Boot2
```

####  2.2 Springnoot依赖管理特性

**依赖管理特性**

- Spring Boot项目一般都有统一的父级依赖spring-boot-starter-parent，它用来提供相关的maven默认依赖，使用它之后，常用的包依赖可以省去version版本号标签

```xml
<parent>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-parent</artifactId>
   <version>2.3.4.RELEASE</version>
   <relativePath/> <!-- lookup parent from repository -->
</parent>
```

- spring-boot-starter-parent还有一个父项目依赖spring-boot-dependencies

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-dependencies</artifactId>
  <version>2.3.4.RELEASE</version>
</parent>
```

- spring-boot-dependencies中声明了开发中常用jar包依赖的版本：



![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/007.png)

- 修改默认版本号：可以通过`<properties>`标签修改默认的版本，如此处mysql驱动的版本为8.0.21，可以到maven仓库中找到对应版本后利用`<properties>`标签进行修改
  - 查看spring-boot-dependencies里面规定当前依赖的版本 用的 key
  -    在当前项目里面重写`<properties>`配置

```xml
<mysql.version>8.0.21</mysql.version>
```

```xml
<properties>
    <mysql.version>5.1.43</mysql.version>
</properties>
```

**SpringBoot的starter(场景启动器)**

- Spring Boot将常用的功能场景抽取出来，做成场景启动器。只要引入这些starters，相关场景所依赖的全部组件就会被导入，仅需要少量的配置就可以使用相应的功能

- spring-boot-starter-* ： * 表示某种场景。只要引入starter，这个场景需要的所有常规依赖都自动引入。* -spring-boot-starter： 第三方提供的简化开发的场景启动器

- SpringBoot[支持的所有场景](https://docs.spring.io/spring-boot/docs/current/reference/html/using-spring-boot.html#using-boot-starter)，所有场景启动器最底层的依赖是spring-boot-starter

  

  ```xml
  <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter</artifactId>
     <version>2.3.4.RELEASE</version>
     <scope>compile</scope>
  </dependency>
  ```




**自动配置特性**

- 自动配置SpringMVC
  - 引入SpringMVC全套组件
  - 自动配好SpringMVC常用组件（功能）

- 自动配置Web常见功能，如：字符编码问题
  - SpringBoot配置好了所有web开发的常见场景

- 自动配置包扫描
  - 主程序所在包及其下面的所有子包里面的组件都会被默认扫描进来
  - 无需以前的包扫描配置
  - 想要改变扫描路径,可以通过下面注解中的任意一个注解指定扫描路径
    - @SpringBootApplication(scanBasePackages="com.atguigu")
    - @ComponentScan


```java
@SpringBootApplication等同于如下三个注解
        @SpringBootConfiguration
        @EnableAutoConfiguration
        @ComponentScan("com.lsl.code")
```

- 任何配置都拥有默认值
  - 默认配置最终都映射到某个类上，如：MultipartProperties
  - 配置文件的值最终会绑定每个类上，这个类会在容器中创建对象
  - SpringBoot所有的自动配置功能都在 spring-boot-autoconfigure 包里面
  - 按需加载所有自动配置项：引入了哪些场景这个场景的自动配置才会开启
