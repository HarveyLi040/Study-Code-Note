package com.lsl.code.controller_03;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//@RestController 等价于 @Controller加@ResponseBody
@RestController
public class HelloControllerThree {
      @RequestMapping("/helloThree")
      public String handleRequest(){
          return "Hello,Spring Boot2";
      }
}
// 访问http://localhost:8080/helloThree 返回 Hello,Spring Boot2
