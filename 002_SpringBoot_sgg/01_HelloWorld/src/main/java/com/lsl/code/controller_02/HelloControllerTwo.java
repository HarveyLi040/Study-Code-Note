package com.lsl.code.controller_02;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class HelloControllerTwo {
      @RequestMapping("/helloTwo")
      public String handleRequest(){
          return "Hello,Spring Boot2";
      }
}
//http://localhost:8080/helloTwo 返回Hello,Spring Boot2