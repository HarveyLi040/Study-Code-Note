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
/*1、访问首页，填写如下表单
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
