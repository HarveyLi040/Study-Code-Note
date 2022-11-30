package com.lsl.code.controller_01;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class HelloControllerOne {
      @ResponseBody
      @RequestMapping("/helloOne")
      public String handleRequest(){
          return "Hello,Spring Boot2";
      }
}
//访问http://localhost:8080/helloOne 返回Hello,Spring Boot2