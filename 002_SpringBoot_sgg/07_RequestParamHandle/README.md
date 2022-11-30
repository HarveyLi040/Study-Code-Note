##  1.请求参数处理之<font color=red>常用注解</font>

####  1.1 基本注解@PathVariable、@RequestHeader、@RequestParam、@CookieValue、@RequestBody

- **基本注解使用演示(重点)：**

```java
package com.lsl.code.controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
public class ParamHandleController {

    @GetMapping("car/{id}/owner/{username}")
    public Map<String,Object> getPathVariable(@PathVariable("id") Integer id,
                                              @PathVariable("username") String username,
                                              @PathVariable Map<String,String> pv){
        Map<String,Object>map = new HashMap<>();
        map.put("id",id);
        map.put("username",username);
        map.put("pv",pv);
        return map;
    }
/* 访问http://localhost:8080/car/3/owner/lisi , 返回如下数据：
        {
        "pv": {
            "id": "3",
            "username": "lisi"
        },
        "id": 3,
        "username": "lisi"
        }
* */
    @GetMapping("getRequestHeader")
    public Map<String,Object> getRequestHeader(@RequestHeader("user-agent") String userAgent,
                                               @RequestHeader Map<String,String> headers){
        Map<String,Object>map = new HashMap<>();
        map.put("user-agent",userAgent);
        map.put("headers",headers);
        return map;
    }
/*访问http://localhost:8080/getRequestHeader，返回如下数据：
{
"headers": {
   "host": "localhost:8080",
   "connection": "keep-alive",
   "sec-ch-ua": "\"Microsoft Edge\";v=\"107\", \"Chromium\";v=\"107\", \"Not=A?Brand\";v=\"24\"",
   "sec-ch-ua-mobile": "?0",
   "sec-ch-ua-platform": "\"Windows\"",
   "upgrade-insecure-requests": "1",
   "user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 Edg/107.0.1418.52",
    "sec-fetch-site": "same-origin",
    "sec-fetch-mode": "navigate",
    "sec-fetch-user": "?1",
    "sec-fetch-dest": "document",
    "referer": "http://localhost:8080/",
    "accept-encoding": "gzip, deflate, br",
    "accept-language": "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6",
    "cookie": "Idea-b55d02c2=83fc0e04-858c-4596-9fa2-eb16489b8ba2"
},
  "user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 Edg/107.0.1418.52"
        }

* */
    @GetMapping("getRequestParam")
    public Map<String,Object> getRequestParam(@RequestParam("age") Integer age,
                                              @RequestParam("inters") List<String> inters,
                                              @RequestParam Map<String,String> params){
        Map<String,Object>map = new HashMap<>();
        map.put("age",age);
        map.put("inters",inters);
        map.put("params",params);
        return map;
    }

/* 访问http://localhost:8080/getRequestParam?age=18&inters=basketball&inters=game，返回如下数据：
        {
        "inters": [
                "basketball",
                "game"
        ],
        "params": {
                "age": "18",
                "inters": "basketball"
        },
        "age": 18
        }
        * */
    @GetMapping("getCookieValue")
    public Map<String,Object> getCookieValue(@CookieValue("Idea-b55d02c2") String idea,
                                             @CookieValue("Idea-b55d02c2") Cookie cookie){
        Map<String,Object>map = new HashMap<>();
        map.put("Idea-b55d02c2",idea);
        map.put("cookie",cookie);
        return map;
    }
/*访问http://localhost:8080/getCookieValue，返回如下数据：
    {
    "Idea-b55d02c2": "83fc0e04-858c-4596-9fa2-eb16489b8ba2",
    "cookie": {
            "name": "Idea-b55d02c2",
            "value": "83fc0e04-858c-4596-9fa2-eb16489b8ba2",
            "version": 0,
            "comment": null,
            "domain": null,
            "maxAge": -1,
            "path": null,
            "secure": false,
            "httpOnly": false
    }
    }
    * */
    @PostMapping("getRequestBody")
    public Map getRequestBody(@RequestBody String content){
           Map<String,Object>map = new HashMap<>();
           map.put("content",content);
           return map;
    }
/*1、访问首页，填写如下表单，表单中提取的数据放在请求体中传输
<form action="getRequestBody" method="post">
    用户名：<input name="userName"/> <br>
    邮箱：<input name="email"/>
    <input type="submit" value="提交"/>
</form>
2、访问http://localhost:8080/getRequestBody，返回如下数据：
            {
            "content": "userName=zhangshan&email=2241876453"
            }
            * */
}
```



####   1.2 获取request域的属性值：注解@RequestAttribute

- **@RequestAttribute注解用于获取request域中保存的属性值**

- **@RequestAttribute的使用演示（重点）：**

```java
package com.lsl.code.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RequestController {

    @GetMapping("/getRequestAttribute")
    public String goToPage(HttpServletRequest request){
        request.setAttribute("msg","成功了");
        request.setAttribute("code",200);
        return "forward:/success";  //转发到 /success请求
    }
/*访问http://localhost:8080/getRequestAttribute，返回如下数据：
{
    "request_msg": "成功了",
    "annotation_msg": "成功了",
    "annotation_code": 200,
    "request_code": 200
}
* */
    @ResponseBody
    @GetMapping("/success")
    public  Map<String,Object> success(@RequestAttribute("msg") String msg,
                                       @RequestAttribute("code") Integer code,
                                       HttpServletRequest request){

        Map<String,Object> map = new HashMap<>();
        map.put("request_msg",request.getAttribute("msg"));
        map.put("request_code",request.getAttribute("code"));
        map.put("annotation_msg",msg);
        map.put("annotation_code",code);
        return map;
    }
}
```

####   1.3 矩阵变量： 注解@MatrixVariable  

- 矩阵变量需要在SpringBoot中配置才能生效
- 根据RFC3986的规范，矩阵变量应当绑定在路径变量中
- 若是有多个矩阵变量，应当使用英文符号;进行分隔
- 若是一个矩阵变量有多个值，应当使用英文符号,进行分隔，或之命名多个重复的key即可，如：/cars/sell;low=34;brand=byd,audi,yd

**演示（重点）：**

```java
package com.lsl.code.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MatrixVariableController {
    // 矩阵变量语法： /cars/sell;low=34;brand=byd,audi,yd,guazi
    // springboot默认禁用了矩阵变量的功能，需要手动开启
    // 手动开启:原理,对于路径的处理,UrLPathHeLper进行解析
    // removesemicoloncontent（移除分号内容）支持矩阵变量的
    // 矩阵变量需要绑定在路径变量中
    // 矩阵变量必须有urL路径变量才能被解析

    //    /cars/sell;low=34;brand=byd;brand=audi;brand=yd
    @GetMapping("/cars/{path}")
    public Map casrSell(@MatrixVariable("low")  Integer low,
                        @MatrixVariable("brand") List<String> brand,
                        @PathVariable("path") String path){
        Map<String,Object> map = new HashMap<>();
        map.put("low",low);
        map.put("brand",brand);
        map.put("path",path);
        return map;
    }
/*访问http://localhost:8080/cars/sell;low=34;brand=byd;brand=audi;brand=yd，返回如下数据：
        {
            "path": "sell",
            "low": 34,
            "brand": [
                "byd",
                "audi",
                "yd"
            ]
        }

访问http://localhost:8080/cars/sell;low=34;brand=byd,audi,yd，返回如下数据：
        {
            "path": "sell",
            "low": 34,
            "brand": [
                    "byd",
                    "audi",
                    "yd"
            ]
        }
* */
//    在矩阵变量中可以指定获取url路径上的某个值
//    /boss/1;age=20/2;age=10
    @GetMapping("/boss/{bossId}/{empId}")
    public Map bossAndEmpId(@MatrixVariable(value = "age",pathVar = "bossId") Integer bossAge,
                            @MatrixVariable(value = "age",pathVar = "empId") Integer empId){
        Map<String,Object> map = new HashMap<>();
        map.put("bossAge", bossAge);
        map.put("empId", empId);
        return map;
    }
/*访问http://localhost:8080/boss/1;age=20/2;age=10，返回如下数据：
        {
        "empId": 10,
        "bossAge": 20
        }
* */
}
```

**矩阵变量配置：springboot默认禁用了矩阵变量的功能，需要通过配置才能生效**

```java
package com.lsl.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

// 矩阵变量配置: ————> 两种配置方式

@Configuration(proxyBeanMethods = false)
public class MatrixVariableConfig /*implements WebMvcConfigurer*/ {
/*    @Override
    public void configurePathMatch(PathMatchConfigurer configurer){
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        // 设置为不移除分号;后面的的内容，设置后矩阵变量才可以生效
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }*/
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {
                UrlPathHelper urlPathHelper =  new UrlPathHelper();
                // 设置为不移除分号;后面的的内容，设置后矩阵变量才可以生效
                urlPathHelper.setRemoveSemicolonContent(false);
                configurer.setUrlPathHelper(urlPathHelper);
            }
        };
    }
}
```



**矩阵变量应用场景：**

cookie、 session机制：利用session.set(a,b)在session中存储内容，每个session都有jsessionid ，jsessionid是保存在cookie中的， 每次发请求时cookie携带jsessionid传输。在页面开发中，如果禁用了cookie（禁用cookie则无法携带jsessionid，也就找不到session中存储的内容），该怎么获取session里的内容？

答：使用矩阵变量的方式携带，利用url重写解决cookie被禁用问题。url重写:    /abc;jsesssionid=xxxx       把cookie的值使用矩阵变量的方式进行传递

####   1.4 常用注解底层原理

**1、参数处理原理**

- HandlerMapping中找到能处理请求的Handler (Controller.method()，也就是controller中的方法)
- 为当前Handler找一个适配器HandlerAdapter; RequestMappingHandlerAdapter
- 适配器执行目标方法并确定方法参数的每一个值

**HandlerAdapter**

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/017.png)

- HandlerAdapter支持方法上标注@RequestMapping——RequestMappingHandlerAdapter

- HandlerAdapter支持函数式编程的—— HandlerFunctionAdapter

**2、执行目标方法**

```java
// Actually invoke the handler.    ——>真正执行目标方法
//DispatcherServlet -- doDispatch
mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
```

```java
mav = invokeHandlerMethod(request, response, handlerMethod); //执行目标方法
//ServletInvocableHandlerMethod
Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
//获取方法的参数值
Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);

```

**2.1 、确定目标方法参数值**

```java
============InvocableHandlerMethod==========================
protected Object[] getMethodArgumentValues(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
			Object... providedArgs) throws Exception {

		MethodParameter[] parameters = getMethodParameters();
		if (ObjectUtils.isEmpty(parameters)) {
			return EMPTY_ARGS;
		}

		Object[] args = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			MethodParameter parameter = parameters[i];
			parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
			args[i] = findProvidedArgument(parameter, providedArgs);
			if (args[i] != null) {
				continue;
			}
			if (!this.resolvers.supportsParameter(parameter)) {
				throw new IllegalStateException(formatArgumentError(parameter, "No suitable resolver"));
			}
			try {
				args[i] = this.resolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
			}
			catch (Exception ex) {
				// Leave stack trace for later, exception may actually be resolved and handled...
				if (logger.isDebugEnabled()) {
					String exMsg = ex.getMessage();
					if (exMsg != null && !exMsg.contains(parameter.getExecutable().toGenericString())) {
						logger.debug(formatArgumentError(parameter, exMsg));
					}
				}
				throw ex;
			}
		}
		return args;
	}
```

**2.2、依次便利所有参数解析器，判断哪个支持解析这个参数**

```java
	@Nullable
	private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
		HandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
		if (result == null) {
			for (HandlerMethodArgumentResolver resolver : this.argumentResolvers) {
				if (resolver.supportsParameter(parameter)) {
					result = resolver;
					this.argumentResolverCache.put(parameter, result);
					break;
				}
			}
		}
		return result;
	}
```

调用各自 HandlerMethodArgumentResolver 的 resolveArgument 方法即可

**3、参数解析器**

- 参数解析器HandlerMethodArgumentResolver确定将要执行的目标方法的每一个参数的值是什么
- SpringMVC目标方法能写多少种参数类型，取决于参数解析器



![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/018.png)



判断当前解析器是否支持解析这种参数，支持就调用resolveArgument

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/019.png)

**4、返回值处理器**

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/020.png)





##   2. 请求参数处理之 <font color=red>Servlet APl</font>

springboot除了提供上述注解来获取请求参数之外，springboot还提供原生的Servlet APl来充当参数并获取请求数据（如上面利用HttpServletRequest request传递参数）。常见Servlet APl如下：

WebRequest、ServletRequest、MultipartRequest、 HttpSession、javax.servlet.http.PushBuilder、Principal、InputStream、Reader、HttpMethod、Locale、TimeZone、ZoneId

**请求参数处理时，ServletRequestMethodArgumentResolver 可以解析以上的部分参数**



## 3.请求参数处理之<font color=red>复杂参数处理</font>

####   3.1 Map、Model用法演示

除了基本注解和Servlet APl外，还可以利用下面的类处理请求参数

- **Map**、**Model、**Errors/BindingResult、**RedirectAttributes**、**ServletResponse（response）**、SessionStatus、UriComponentsBuilder、ServletUriComponentsBuilder

- **map、model里面的数据会被放在request的请求域** ——等价于request.setAttribute
- RedirectAttributes用于在重定向中携带数据

**重点：演示Map、Model：**

```java
package com.lsl.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestMapAndModelController {
    @GetMapping("/TestMapAndModel")
    public String testParam(Map<String,Object> map,
                            Model model,
                            HttpServletRequest request,
                            HttpServletResponse response){
         map.put("map","_map_");
         model.addAttribute("model","_model_");
         return "forward:/succeed";
    }
/*访问http://localhost:8080/TestMapAndModel，返回如下数据
    {
        "model": "_model_",
         "map": "_map_"
    }*/
    @ResponseBody
    @GetMapping("/succeed")
    public  Map<String,Object> success(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("map",request.getAttribute("map"));
        map.put("model",request.getAttribute("model"));
        return map;
    }
}
```

####  3.2 Map、Model相关原理

**<font color=red>下面讲解springboot是如何将map、model里面的数据放在request的请求域？</font>**

- **Map、Model类型的参数**，会返回 mavContainer.getModel（）

- BindingAwareModelMap 是Model 也是Map，**mavContainer**.getModel(); 获取值



![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/021.png)





![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/022.png)



将所有的数据都放在 **ModelAndViewContainer**；包含要去的页面地址View。还包含Model数据。

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/023.png)



重要的类及方法：

- **processDispatchResult**(processedRequest, response, mappedHandler, mv, dispatchException);

- renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);

 

```java
	InternalResourceView：
    @Override
	protected void renderMergedOutputModel(
			Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	// Expose the model object as request attributes.
	exposeModelAsRequestAttributes(model, request);

	// Expose helpers as request attributes, if any.
	exposeHelpers(request);

	// Determine the path for the request dispatcher.
	String dispatcherPath = prepareForRendering(request, response);

	// Obtain a RequestDispatcher for the target resource (typically a JSP).
	RequestDispatcher rd = getRequestDispatcher(request, dispatcherPath);
	if (rd == null) {
		throw new ServletException("Could not get RequestDispatcher for [" + getUrl() +
				"]: Check that the corresponding file exists within your web application archive!");
	}

	// If already included or response already committed, perform include, else forward.
	if (useInclude(request, response)) {
		response.setContentType(getContentType());
		if (logger.isDebugEnabled()) {
			logger.debug("Including [" + getUrl() + "]");
		}
		rd.include(request, response);
	}

	else {
		// Note: The forwarded resource is supposed to determine the content type itself.
		if (logger.isDebugEnabled()) {
			logger.debug("Forwarding to [" + getUrl() + "]");
		}
		rd.forward(request, response);
	}
}
```

```java
/* 上面源码的关键点： 
    // Expose the model object as request attributes.
    暴露模型作为请求域属性
    exposeModelAsRequestAttributes(model, request);*/
```

原理所在：遍历model中的所有数据挨个放在请求域中

```
protected void exposeModelAsRequestAttributes(Map<String, Object> model,
                                              HttpServletRequest request) throws Exception {

    //遍历model中的所有数据挨个放在请求域中
    model.forEach((name, value) -> {
        if (value != null) {
            request.setAttribute(name, value);
        }
        else {
            request.removeAttribute(name);
        }
    });
}
```



## 4.请求参数处理之<font color=red>自定义参数对象</font>

####  4.1 代码演示

可以自动类型转换与格式化，可以级联封装

 **Person类**

```java
package com.lsl.code.Bean;

import lombok.Data;
import java.util.Date;

@Data
public class Person {
    private String userName;
    private Integer age;
    private Date birth;
    private Pet pet;
}

/** 对应表单
 *     姓名： <input name="userName"/> <br/>
 *     年龄： <input name="age"/> <br/>
 *     生日： <input name="birth"/> <br/>
 *     宠物姓名：<input name="pet.name"/><br/>
 *     宠物年龄：<input name="pet.age"/>
 */
```

**Pet类**

```java
package com.lsl.code.Bean;
import lombok.Data;
@Data
public class Pet {
    private String name;
    private String age;
}
```

**测试**

```java
package com.lsl.code.controller;

import com.lsl.code.Bean.Person;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class POJOController {
    @PostMapping("/saveuser")
    public Person saveUser(Person person){
          return person;
    }
}
/**
 Person类对应表单：
         <form action="/saveuser" method="post">
         姓名： <input name="userName" value="zhangsan"/> <br/>
         年龄： <input name="age" value="18"/> <br/>
         生日： <input name="birth" value="2019/12/10"/> <br/>
         宠物姓名：<input name="pet.name" value="阿猫"/><br/>
         宠物年龄：<input name="pet.age" value="5"/>
                 <input type="submit" value="保存"/>
         </form>
测试：在首页填写完上述表单后，点击提交，返回如下数据：
         {
         "userName": "zhangsan",
         "age": 18,
         "birth": "2019-12-09T16:00:00.000+00:00",
         "pet": {
                 "name": "阿猫",
                 "age": "5"
         }
         }
 */
```

####  4.2 自定义参数绑定原理

**原理：WebDataBinder 利用它里面的 Converters 将请求数据转成指定的数据类型。再次封装到JavaBean中**

**WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);**

**WebDataBinder :web数据绑定器，将请求参数的值绑定到指定的JavaBean里面**



![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/024.png)







![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/025.png)

GenericConversionService：在设置每一个值的时候，找它里面的所有converter那个可以将这个数据类型（request带来参数的字符串）转换到指定的类型（JavaBean -- Integer）

####  4.3 自定义 Converter

自定义 Converter：也可以给WebDataBinder里面放自己的Converter，实现自定义数据绑定

private static final class StringToNumber<T extends Number> implements Converter<String, T>

**案例：修改默认Converter,实现对Pet参数自定义绑定**

- 实现方式：在配置类中实现public void addFormatters(FormatterRegistry registry)

```java
package com.lsl.code.config;

import com.lsl.code.Bean.Pet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

// 矩阵变量配置: ————> 两种配置方式

@Configuration(proxyBeanMethods = false)
public class MatrixVariableConfig{
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {
                UrlPathHelper urlPathHelper =  new UrlPathHelper();
                // 设置为不移除分号;后面的的内容，设置后矩阵变量才可以生效
                urlPathHelper.setRemoveSemicolonContent(false);
                configurer.setUrlPathHelper(urlPathHelper);
            }

            @Override
            public void addFormatters(FormatterRegistry registry){
                 registry.addConverter(new Converter<String, Pet>() {
                     @Override
                     public Pet convert(String source){
                         if(!StringUtils.isEmpty(source)){
                             Pet pet = new Pet();
                             String[] split = source.split(",");
                             pet.setName(split[0]);
                             pet.setAge(split[1]);
                             return pet;
                         }
                         return null;
                     }
                 });
            }
        };
    }
}
```



```java
package com.lsl.code.controller;

import com.lsl.code.Bean.Person;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class POJOController {
    // 自定义Converter
    @PostMapping("/testConverter")
    public Person testConverter(Person person){
        return person;
    }
/*  测试：
        <form action="/testConverter" method="post">
        姓名： <input name="userName" value="zhangsan"/> <br/>
        年龄： <input name="age" value="18"/> <br/>
        生日： <input name="birth" value="2019/12/10"/> <br/>
        宠物： <input name="pet" value="啊猫,3"/>
        <input type="submit" value="保存"/>
        </form>
     测试：在首页填写完上述表单后，点击提交，返回如下数据：
            {
            "userName": "zhangsan",
            "age": 18,
            "birth": "2019-12-09T16:00:00.000+00:00",
            "pet": {
                "name": "啊猫",
                "age": "3"
            }
            }
*/

}
```

