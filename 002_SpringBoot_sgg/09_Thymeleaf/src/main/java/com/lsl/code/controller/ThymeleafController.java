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
