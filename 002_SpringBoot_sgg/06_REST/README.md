##  1.请求参数处理

####   1.1 Rest风格使用

**Rest风格**

- Rest风格支持（*使用**HTTP**请求方式动词来表示对资源的操作*）

- - 以前：/getUser  获取用户   /deleteUser 删除用户   /editUser  修改用户      /saveUser 保存用户
  - 现在： /user    GET-获取用户    DELETE-删除用户    PUT-修改用户      POST-保存用户



**案例：结合表单实现GET 、 POST 、 PUT 、 DELETE 请求**

- 在配置文件application.properties中配置`spring.mvc.hiddenmethod.filter.enabled=true`,开启form表单的REST风格支持

```properties
spring.mvc.hiddenmethod.filter.enabled=true  #开启页面表单的Rest功能
```

- 在index.html欢迎页编写form表单

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<h1>测试REST风格:</h1>
<form action="/user" method="get">
  <input value="REST-GET 提交" type="submit"/>
</form>
<form action="/user" method="post">
  <input value="REST-POST 提交" type="submit"/>
</form>
<form action="/user" method="post">
  <input name="_method" type="hidden" value="PUT"/>
  <input value="REST-PUT 提交" type="submit"/>
</form>
<form action="/user" method="post">
  <input name="_method" type="hidden" value="delete"/>
  <input value="REST-DELETE 提交" type="submit"/>
</form>
<hr/>
</body>
</html>
```

- 编写请求处理类RESTController

controller写法一：

```java
package com.lsl.code.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public String getUser(){
        return "GET";
    }

    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public String saveUser(){
        return "POST";
    }

    @RequestMapping(value = "/user",method = RequestMethod.PUT)
    public String putUser(){
        return "PUT";
    }

    @RequestMapping(value = "/user",method = RequestMethod.DELETE)
    public String deleteUser(){
        return "DELETE";
    }
}
```

controller写法二：

```java
package com.lsl.code.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class RESTController {
//    @RequestMapping(value = "/user",method = RequestMethod.GET)
    @GetMapping("/user")
    public String getUser(){
        return "GET";
    }

//    @RequestMapping(value = "/user",method = RequestMethod.POST)
    @PostMapping("/user")
    public String saveUser(){
        return "POST";
    }


//    @RequestMapping(value = "/user",method = RequestMethod.PUT)
    @PutMapping("/user")
    public String putUser(){
        return "PUT";
    }

//    @RequestMapping(value = "/user",method = RequestMethod.DELETE)
    @DeleteMapping("/user")
    public String deleteUser(){
        return "DELETE";
    }
}
```

测试：

- 访问根路径`http://localhost:8080/`可以看到欢迎页

- 点击`REST-PUT 提交`按钮跳转到链接`http://localhost:8080/user`，页面返回`POST`



<table align="center">
   <tr>
   <td>
       <b>欢迎页</b>
       <img src="https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/012.png" >
   </td>
   <td>
        <b>跳转页</b>
       <img src="https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/013.png" > 
   </td>
   </tr>
</table>



####  1.2 Rest原理

**Rest原理（表单提交要使用REST的时候）**

- 表单提交要带上`_method=PUT` 

- **请求过来被**HiddenHttpMethodFilter拦截
- 判断请求是否正常且是POST，若是，则进行如下操作
  - 获取到_method的值
  - 兼容以下请求：**PUT**、**DELETE**、**PATCH**
  - **原生request（post），包装模式requesWrapper重写了getMethod方法，返回的是传入的值。**
  - 过滤器链放行的时候用wrapper。以后的方法调用getMethod是调用requesWrapper

**Rest使用客户端工具**

如PostMan可直接发送Put、delete等方式请求，无需Filter。也不用修改`spring.mvc.hiddenmethod.filter.enabled`配置



**扩展点:如何将_method 换成其他值**

通过配置类返回一个`HiddenHttpMethodFilter`类型的Bean，将`_method`改成其他值

```java
package com.lsl.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration(proxyBeanMethods = false)
public class webconfig {
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        HiddenHttpMethodFilter methodFilter = new HiddenHttpMethodFilter();
        methodFilter.setMethodParam("_my");
        return methodFilter;
    }
}
```

同时修改index.html内容使其与新配置匹配

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<h1>测试REST风格:</h1>
<form action="/user" method="get">
  <input value="REST-GET 提交" type="submit"/>
</form>
<form action="/user" method="post">
  <input value="REST-POST 提交" type="submit"/>
</form>
<form action="/user" method="post">
  <input name="_my" type="hidden" value="PUT"/>
  <input value="REST-PUT 提交" type="submit"/>
</form>
<form action="/user" method="post">
  <input name="_my" type="hidden" value="delete"/>
  <input value="REST-DELETE 提交" type="submit"/>
</form>
<hr/>
</body>
</html>
```

####   1.3  请求映射原理

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/014.png)

SpringMVC请求映射功能分析都从`org.springframework.web.servlet.DispatcherServlet类`  ->  `doDispatch（）方法`

```java
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpServletRequest processedRequest = request;
		HandlerExecutionChain mappedHandler = null;
		boolean multipartRequestParsed = false;
	WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
	try {
		ModelAndView mv = null;
		Exception dispatchException = null;
		try {
			processedRequest = checkMultipart(request);
			multipartRequestParsed = (processedRequest != request);

			// 找到当前请求使用哪个Handler（Controller的方法）处理
			mappedHandler = getHandler(processedRequest);
            
            //HandlerMapping：处理器映射 /xxx请求 ->> xxxx处理器
```



![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/015.png)

**RequestMappingHandlerMapping**：保存了所有@RequestMapping 和handler的映射规则

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/016.png)



所有的请求映射都在HandlerMapping中

- SpringBoot自动配置欢迎页的 WelcomePageHandlerMapping 。访问 /能访问到index.html

- SpringBoot自动配置了默认 的 RequestMappingHandlerMapping

- 请求进来，挨个尝试所有的HandlerMapping看是否有请求信息

- - 如果有就找到这个请求对应的handler
  - 如果没有就是下一个 HandlerMapping

- 我们需要一些自定义的映射处理，也可以自己给容器中放**HandlerMapping**。自定义 **HandlerMapping**

  