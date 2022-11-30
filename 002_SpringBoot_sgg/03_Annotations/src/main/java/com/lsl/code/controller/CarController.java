package com.lsl.code.controller;
import com.lsl.code.bean.MyCar;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
@RestController
public class CarController {
    @Resource
    MyCar myCar;
    @RequestMapping("/car")
    public MyCar car(){
        return myCar;
    }
}
/*访问http://localhost:8080/car 返回
                    {
                        brand: "BYD",
                        price: 100000
                    }*/
