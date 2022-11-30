# 1.数据响应与内容协商

### 1.2 数据响应

**常见的数据响应方式：**

- **响应页面**适合一些非前后端分离的项目，**响应数据**适用于开发前后端分离的项目

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/026.png)

SpringMVC支持的返回值类型：

```
ModelAndView
Model
View
ResponseEntity 
ResponseBodyEmitter
StreamingResponseBody
HttpEntity
HttpHeaders
Callable
DeferredResult
ListenableFuture
CompletionStage
WebAsyncTask
有 @ModelAttribute 且为对象类型的
```



####  1.2.1 响应JSON（实现：jackson.jar+@ResponseBody）

引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- web场景自动引入了json场景-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-json</artifactId>
    <scope>compile</scope>
</dependency>
```

利用@ResponseBody注解给前端响应JSON数据

```java
package com.lsl.code.controller;

import com.lsl.code.Bean.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class ReponseTestController {

    @ResponseBody
    @GetMapping("/testResponseBodyAndJSON")
    public Person getPerson(){
        Person person = new Person();
        person.setAge(20);
        person.setBirth(new Date());
        person.setUserName("zhangshan");
        return person;
    }
/*访问http://localhost:8080/testResponseBodyAndJSON，返回如下数据：
        {
            "userName": "zhangshan",
                "age": 20,
                "birth": "2022-11-24T19:49:43.736+00:00",
                "pet": null
        }
*/
}
```

响应JSON数据原理：**返回值处理器**

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/027.png)



```java
try {
			this.returnValueHandlers.handleReturnValue(
					returnValue, getReturnValueType(returnValue), mavContainer, webRequest);
		}
```



```java
	@Override
	public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

		HandlerMethodReturnValueHandler handler = selectHandler(returnValue, returnType);
		if (handler == null) {
			throw new IllegalArgumentException("Unknown return value type: " + returnType.getParameterType().getName());
		}
		handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
	}
```



```java
RequestResponseBodyMethodProcessor  	
@Override
	public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
			throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {

		mavContainer.setRequestHandled(true);
		ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
		ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);

		// Try even with null return value. ResponseBodyAdvice could get involved.
        // 使用消息转换器进行写出操作
		writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
	}
```

**返回值解析器原理(处理逻辑)：**

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/028.png)

1.返回值处理器判断是否支持这种类型返回值 supportsReturnType

SpringMVC支持的返回值类型：

```
ModelAndView
Model
View
ResponseEntity 
ResponseBodyEmitter
StreamingResponseBody
HttpEntity
HttpHeaders
Callable
DeferredResult
ListenableFuture
CompletionStage
WebAsyncTask
有 @ModelAttribute 且为对象类型的
@ResponseBody 注解 ---> RequestResponseBodyMethodProcessor；
```



2.返回值处理器调用 handleReturnValue 进行处理

3.RequestResponseBodyMethodProcessor 用于处理返回值标了@ResponseBody 注解的

- - 1. 利用 MessageConverters 进行处理 将数据写为json

- - - 1、内容协商（浏览器默认会以请求头的方式告诉服务器他能接受什么样的内容类型）
    - 2、服务器最终根据自己自身的能力，决定服务器能生产出什么样内容类型的数据，
    - 3、SpringMVC会挨个遍历所有容器底层的 HttpMessageConverter ，看谁能处理？

- - - - 1、得到MappingJackson2HttpMessageConverter可以将对象写为json
      - 2、利用MappingJackson2HttpMessageConverter将对象转为json再写出去。

4.HTTPMessageConverter原理

- MessageConverter规范

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/030.png)

- HttpMessageConverter: 看是否支持将 此 Class类型的对象，转为MediaType类型的数据

- 例子：Person对象转为JSON。或者 JSON转为Person

- 默认的MessageConverter

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/031.png)

- 0 - 只支持Byte类型的
- 1 - 支持String类型
- 2 - 支持String
- 3 - 支持Resource
- 4 - 支持ResourceRegion
- 5 - 支持DOMSource.class  SAXSource.class 、StAXSource.class 、StreamSource.class 、 Source.class
- 6 - 支持MultiValueMap
- 7 - 支持true 
- 8 - 支持true
- 9 - 支持注解方式xml处理的

最终 MappingJackson2HttpMessageConverter  把对象转为JSON（利用底层的jackson的objectMapper转换的）

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/032.png)

# 2.内容协商

###   2.1 内容协商代码演示

**内容协商：**

- 根据客户端接收能力不同，返回不同媒体类型的数据

- 浏览器发送请求的时候告知服务端自己能接收那些形式的响应数据，浏览器默认会以请求头的方式告诉服务器他能接受什么样的内容类型

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/029.png)

**引入XML依赖：**

```xml
 <dependency>
            <groupId>com.fasterxml.jackson.dataformat</groupId>
            <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```

**开启浏览器参数方式内容协商功能：**

- 为了方便内容协商，开启基于请求参数的内容协商功能

```properties
#开启请求参数内容协商模式
spring.mvc.contentnegotiation.favor-parameter=true  
```

**自定义 MessageConverter：**

- 实现自定义MessageConverter，返回自定义的媒体类型数据

- 自定义内容协商MyMessageConverter规则

  ```java
  
  package com.lsl.code.converter;
  
  import com.lsl.code.Bean.Person;
  import org.springframework.http.HttpInputMessage;
  import org.springframework.http.HttpOutputMessage;
  import org.springframework.http.MediaType;
  import org.springframework.http.converter.HttpMessageConverter;
  import org.springframework.http.converter.HttpMessageNotReadableException;
  import org.springframework.http.converter.HttpMessageNotWritableException;
  
  import java.io.IOException;
  import java.io.OutputStream;
  import java.util.List;
  
  /**
   *  自定义内容协商MyMessageConverter规则：将属性值用分号隔开： 属性值1;属性值2;
   */
  public class  MyMessageConverter implements HttpMessageConverter<Person> {
  
      @Override
      public boolean canRead(Class<?> clazz, MediaType mediaType) {
          return false;
      }
  
      @Override
      public boolean canWrite(Class<?> clazz, MediaType mediaType) {
          return clazz.isAssignableFrom(Person.class);
      }
  
      /**
       * 服务器要统计所有MessageConverter都能写出哪些内容类型
       * application/My-Type
       * @return
       */
      @Override
      public List<MediaType> getSupportedMediaTypes() {
          return MediaType.parseMediaTypes("application/My-Type");
      }
  
      @Override
      public Person read(Class<? extends Person> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
          return null;
      }
  
      @Override
      public void write(Person person, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
          //自定义协议数据的写出
          String data = person.getUserName()+";"+person.getAge()+";"+person.getBirth();
  
          //写出去
          OutputStream body = outputMessage.getBody();
          body.write(data.getBytes());
      }
  }
  ```

- 相关配置：指定支持解析哪些参数对应的哪些媒体类型

  ```java
  package com.lsl.code.config;
  
  import com.lsl.code.converter.MyMessageConverter;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.http.MediaType;
  import org.springframework.http.converter.HttpMessageConverter;
  import org.springframework.web.accept.HeaderContentNegotiationStrategy;
  import org.springframework.web.accept.ParameterContentNegotiationStrategy;
  import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
  import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
  import java.util.*;
  
  @Configuration(proxyBeanMethods = false)
  public class ConvertConfig{
      //1、WebMvcConfigurer定制化SpringMVC的功能
      @Bean
      public WebMvcConfigurer webMvcConfigurer(){
          return new WebMvcConfigurer() {
              /**
               * 自定义内容协商策略
               * @param configurer
               */
              @Override
              public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
                  //Map<String, MediaType> mediaTypes
                  Map<String, MediaType> mediaTypes = new HashMap<>();
                  mediaTypes.put("json",MediaType.APPLICATION_JSON);
                  mediaTypes.put("xml",MediaType.APPLICATION_XML);
                  mediaTypes.put("gg",MediaType.parseMediaType("application/My-Type"));
                  //指定支持解析哪些参数对应的哪些媒体类型
                  ParameterContentNegotiationStrategy parameterStrategy = new ParameterContentNegotiationStrategy(mediaTypes);
  //                parameterStrategy.setParameterName("ff");
  
                  HeaderContentNegotiationStrategy headeStrategy = new HeaderContentNegotiationStrategy();
  
                  configurer.strategies(Arrays.asList(parameterStrategy,headeStrategy));
              }
              @Override
              public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                  converters.add(new MyMessageConverter());
              }
          };
      }
  }
  ```

  **测试内容协商：**

  ```java
  package com.lsl.code.controller;
  import com.lsl.code.Bean.Person;
  import org.springframework.stereotype.Controller;
  import org.springframework.web.bind.annotation.GetMapping;
  import org. springframework.web.bind.annotation.ResponseBody;
  import java.util.Date;
  @Controller
  public class ReponseTestController {
      /**
         实现多协议数据兼容。json、xml、My-Type
              0、@ResponseBody 响应数据出去 调用 RequestResponseBodyMethodProcessor 处理
              1、Processor 处理方法返回值。通过 MessageConverter 处理
              2、所有 MessageConverter 合起来可以支持各种媒体类型数据的操作（读、写）
              3、内容协商找到最终的 messageConverter；
       * 1、浏览器发请求直接返回 xml    [application/xml]        jacksonXmlConverter
       * 2、如果是ajax请求 返回 json   [application/json]      jacksonJsonConverter
       * 3、自定义内容协商：发请求，返回自定义协议数据  [appliaction/My-Type]
           自定义内容协商MyMessageConverter规则：将属性值用分号隔开： 属性值1;属性值2;
       * 步骤：
       * 1、添加自定义的MessageConverter进系统底层
       * 2、系统底层就会统计出所有MessageConverter能操作哪些类型
       * 3、客户端内容协商 [My-Type--->My-Type]
       * @return
       */
      @ResponseBody
      @GetMapping("/testConverter")
      public Person testConverter(){
          Person person = new Person();
          person.setAge(20);
          person.setBirth(new Date());
          person.setUserName("zhangshan");
          return person;
      }
  /*1、在请求头中令Accept属性为application/My-Type，访问http://localhost:8080/testConverter，返回
          zhangshan;20;Mon Nov 28 21:51:08 CST 2022
    2、在请求头中令Accept属性为application/json，访问http://localhost:8080/testConverter，返回
          {
              "userName": "zhangshan",
              "age": 20,
              "birth": "2022-11-28T13:54:00.442+00:00",
              "pet": null
          }
    3、在请求头中令Accept属性为application/xml，访问http://localhost:8080/testConverter，返回
         <Person>
              <userName>zhangshan</userName>
              <age>20</age>
              <birth>2022-11-28T13:55:10.384+00:00</birth>
              <pet/>
          </Person>
  * */
  }
  ```

- Accept属性为application/json:

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/033.png)



- Accept属性为application/xml

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/034.png)



- Accept属性为application/My-Type

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/springboot_sgg/img/035.png)





