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
    /*
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
