# 1.SpringSecurity简介

### 1.1. 安全框架概述

什么是安全框架?

解决系统安全问题的框架。如果没有安全框架，我们需要手动处理每个资源的访问控制非常麻烦。使用安全框架，我们可以通过配置的方式实现对资源的访问限制

### 1.2.常用安全框架

- Spring Security: Spring家族一员。是一个能够为基于Spring的企业应用系统提供声明式的安全访问控制解决方案的安全框架。它提供了一组可以在Spring应用上下文中配置的Bean，充分利用了Spring IoC，DI (控制反转Inversion of Control，DI:Dependency Injecton依赖注入)和AOP(面向切面编程）功能，为应用系统提供声明式的安全访问控制功能，减少了为企业系统安全控制编写大量重复代码的工作。

-  Apache Shiro: 一个功能强大且易于使用的Java安全框架,提供了认证,授权,加密,和会话管理

### 1.3.Spring security简介

Spring Security是一个高度自定义的安全框架。利用Spring loC / DI和AOP功能，为系统提供了声明式安全访问控制功能，减少了为系统安全而编写大量重复代码的工作。使用Spring Secruity 的原因有很多，但大部分都是发现了javaEE的 Servlet规范或EJB规范中的安全功能缺乏典型企业应用场景。同时认识到他们在WAR或EAR级别无法移植。因此如果你更换服务器环境，还有大星工作去重新配置你的应用程序。使用Spring Security解决了这些问题，也为你提供许多其他有用的、可定制的安全功能。正如你可能知道的两个应用程序的两个主要区域是"认证"和"授权”(或者访问控制)。这两点也是Spring Security重要核心功能。"认证”，是建立一个他声明的主体的过程(一个"主体"一般是指用户，设备或一些可以在你的应用程序中执行动作的其他系统)，通俗点说就是系统认为用户是否能登录。"授权"指确定一个主体是否允许在你的应用程序执行一个动作的过程。通俗点讲就是系统判断用户是否有权限去做某些事情。


# 2.快速入门

导入SpringSecurity组件：

```xml
       <!-- SpringSecurity组件-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
```

编写表单index.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录表单</title>
</head>
<body>
<form action="/login" method="post">
    用户名：<input type="text" name="username"/><br/>
    密码：<input type="password" name="=password"/><br/>
    <input type="submit" value="登录">
</form>
</body>
</html>
```

编写表单main.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
登录成功！！！
</body>
</html>
```

编写访问控制代码：

```java
package com.lsl.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    /**
     * 登录
     * @return
     */
     @RequestMapping("login")
    public String login(){
         System.out.println("执行登录方法");
         return "redirect:main.html";
     }
}
```

启动项目，访问`http://localhost:8080`，返回的界面并非自定义的登录页面，而是由SpringSecurity提供的登录页面`http://localhost:8080/login`：

![img](https://img-blog.csdnimg.cn/36d986e4d2ac4f6cb2c504323c0218d6.png)

项目启动时会在控制台打印`Using generated security password: a68a8d3c-01a3-4cdd-bcfd-77cb5521fe71` ,在上表中填入用户名`user`,密码`a68a8d3c-01a3-4cdd-bcfd-77cb5521fe71`后返回自定义的登录页面：

![img](https://img-blog.csdnimg.cn/bd7e36b001c049cb91c2fd3ca644f672.png)

SpringSecurity常用类PasswordEncoder 测试：

```java
@Test
void contextLoads() {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String encode = passwordEncoder.encode("123");
    // 打印密码"123"加密后得到的结果
    System.out.println(encode); //$2a$10$BBp139W6cWAhWuA63vINsutEqaoCBH1DbIW7MbFa9dbV7fK42vwES
    // 判断原密码和加密后的密码是否匹配
    boolean matches = passwordEncoder.matches("123",encode);
    System.out.println(matches); // true
}
```



自定义登录逻辑：

PasswordEncoder



```java
package com.lsl.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder getPassword(){
         return new BCryptPasswordEncoder();
    }

}
```

自定义登录逻辑实现：

```java
package com.lsl.code.service;

import jakarta.annotation.Resource;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1、查询数据库判断用户名是否存在，如果不存在就抛出异常UsernameNotFoundException
        if("admin".equals(username)){
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 2、如果用户名存在，就把查询出来的密码（注册时经加密存入数据库）进行解析，或者直接把密码放入构造方法
        String password = passwordEncoder.encode("123");
        return new User(username,password,AuthorityUtils.commaSeparatedStringToAuthorityList( "admin,normal"));
    }
}
```

![img](https://img-blog.csdnimg.cn/9510184e21404270bd7da12166811324.png)
