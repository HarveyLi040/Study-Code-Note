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
