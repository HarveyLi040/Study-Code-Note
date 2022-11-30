#   1.Thymeleaf

**start依赖**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

**Thymeleaf自动装配**

```java
@EnableConfigurationProperties({ThymeleafProperties.class})
@ConditionalOnClass({TemplateMode.class, SpringTemplateEngine.class})
@Import({ReactiveTemplateEngineConfiguration.class, DefaultTemplateEngineConfiguration.class})
public class ThymeleafAutoConfiguration {}
```

- Thymeleaf自动装配策略

  - 所有thymeleaf的配置值都在 ThymeleafProperties

  - 自动装配 SpringTemplateEngine(模板引擎)

  - 自动装配 ThymeleafViewResolver (视图解析器)

  - 有了以上配置，就可以直接开发页面

```java
// Thymeleaf文件默认放在classpath:/templates/ 路径下
public static final String DEFAULT_PREFIX = "classpath:/templates/";
// Thymeleaf文件是html格式的
public static final String DEFAULT_SUFFIX = ".html";  //xxx.html
```

success.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<h1 th:text="${msg}">Hello world</h1>
<h2>
  <a href="www.atguigu.com" th:href="${link}">去百度</a><br/>
</h2>
</body>
</html>
```

```java
package com.lsl.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafController {
    @GetMapping("/testThymeleaf")
    public String testThymeleaf(Model model){
        // model中的数据会被放在请求域中 request.setAttribute("a",aa)
        model.addAttribute("msg","Hello Thymeleaf");
        model.addAttribute("link","http://www.baidu.com");
        return "success";
    }
}
```
