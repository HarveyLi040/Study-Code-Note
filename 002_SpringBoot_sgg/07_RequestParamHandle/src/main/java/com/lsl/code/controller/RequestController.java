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
