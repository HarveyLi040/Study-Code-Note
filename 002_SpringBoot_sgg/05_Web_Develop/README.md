##   1.web开发

####   1.1 静态资源

**默认静态资源访问路径**

- 只要将静态资源放在类路径下：

  -  `/static`

  - `/public` 

  - `/resources`  

  - `/META-INF/resources`

- 然后访问特定路径即可访问静态资源 ： 当前项目根路径/ + 静态资源名 

**案例**

1、在静态资源目录下存放图片：

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/008.png)

2、测试:      访问静态资源，访问路径: 当前项目根路径/ + 静态资源名 。访问如下路径得到静态资源：

- `http://localhost:8080/nobug.jpg`

- `http://localhost:8080/dinic.png`

- `http://localhost:8080/Hacker.jpg`

- `http://localhost:8080/acm.png`

**静态资源访问原理探究**

创建一个controller，将访问路径设为`http://localhost:8080/nobug.jpg`。此时访问该路径，是进入controller还是得到静态资源？

```java
package com.lsl.code.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TestController {
        @RequestMapping("/nobug.jpg")
       public String Test(){
           return "访问了Controller,而不是静态资源";
       }
}
```

访问`http://localhost:8080/nobug.jpg` ，发现并未得到静态资源，而是进入了Controller：

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/009.png)

原因：`默认情况`下，静态被映射到`/**`上，请求进来，先去找Controller看能不能处理。不能处理的所有请求又都交给静态资源处理器，静态资源也找不到则响应404页面



**修改静态资源访问路径—静态资源访问前缀**

`默认情况`下，静态资源被映射到`/**`上，但是可以配置`spring.mvc.static-path-pattern`属性对其进行修改。例如，将所有资源定位到`/resources/**`，可以在application.properties配置文件中添加如下配置：

```properties
spring.mvc.static-path-pattern=/resources/**
```

测试：访问`http://localhost:8080/resources/dinic.png`返回静态资源图片dinic.png



**修改默认的静态资源存放目录**

通过配置`spring.web.resources.static-locations`属性，修改静态资源存放目录：

```properties
#将静态资源存放目录修改为/haha/
spring.web.resources.static-locations=classpath:/haha/
```

在haha目录下存放图片：

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/010.png)

测试：访问`http://localhost:8080/resources/erfentu.png`，返回静态资源图片

####   1.2 欢迎页

**方式一：静态资源路径下存放index.html**

在静态资源路径(/haha/)下存放index.html文件：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>欢迎查看我的的笔记！</h1>
</body>
</html>
```

注释掉静态资源前缀：

```properties
#spring.mvc.static-path-pattern=/resources/**
spring.web.resources.static-locations=classpath:/haha/
```

访问`http://localhost:8080/`，浏览器显示欢迎页

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/011.png)

注：实现“访问`http://localhost:8080/`，浏览器显示欢迎页”的前提是“可以配置静态资源路径，但是不可以配置静态资源的访问前缀。否则导致index.html不能被默认访问”

####   1.3 自定义Favicon

将图标重命名为`favicon.ico`,并放置到静态资源目录下即可。访问网站每一页时都会显示图标

注：要注释掉静态资源前缀，否则会导致 Favicon 功能出现异常

```properties
#spring.mvc.static-path-pattern=/resources/**
spring.web.resources.static-locations=classpath:/haha/
```



