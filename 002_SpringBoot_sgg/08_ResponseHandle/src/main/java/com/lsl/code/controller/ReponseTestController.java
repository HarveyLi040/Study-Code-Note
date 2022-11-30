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

    /**
     * 1、浏览器发请求直接返回 xml    [application/xml]        jacksonXmlConverter
     * 2、如果是ajax请求 返回 json   [application/json]      jacksonJsonConverter
     * 3、自定义内容协商：发请求，返回自定义协议数据  [appliaction/My-Type]
         自定义内容协商MyMessageConverter规则：将属性值用分号隔开： 属性值1;属性值2;
     * 步骤：
     * 1、添加自定义的MessageConverter进系统底层
     * 2、系统底层就会统计出所有MessageConverter能操作哪些类型
     * 3、客户端内容协商 [My-Type--->My-Type]
     * 作业：如何以参数的方式进行内容协商
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
