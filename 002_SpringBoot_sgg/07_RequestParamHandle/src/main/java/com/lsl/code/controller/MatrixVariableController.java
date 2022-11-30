package com.lsl.code.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MatrixVariableController {
    // 矩阵变量语法： /cars/sell;low=34;brand=byd,audi,yd,guazi
    // springboot默认禁用了矩阵变量的功能，需要手动开启
    // 手动开启:原理,对于路径的处理,UrLPathHeLper进行解析
    // removesemicoloncontent（移除分号内容）支持矩阵变量的
    // 矩阵变量需要绑定在路径变量中
    // 矩阵变量必须有urL路径变量才能被解析

    //    /cars/sell;low=34;brand=byd;brand=audi;brand=yd
    @GetMapping("/cars/{path}")
    public Map casrSell(@MatrixVariable("low")  Integer low,
                        @MatrixVariable("brand") List<String> brand,
                        @PathVariable("path") String path){
        Map<String,Object> map = new HashMap<>();
        map.put("low",low);
        map.put("brand",brand);
        map.put("path",path);
        return map;
    }
/*访问http://localhost:8080/cars/sell;low=34;brand=byd;brand=audi;brand=yd，返回如下数据：
        {
            "path": "sell",
            "low": 34,
            "brand": [
                "byd",
                "audi",
                "yd"
            ]
        }

访问http://localhost:8080/cars/sell;low=34;brand=byd,audi,yd，返回如下数据：
        {
            "path": "sell",
            "low": 34,
            "brand": [
                    "byd",
                    "audi",
                    "yd"
            ]
        }
* */
//    在矩阵变量中可以指定获取url路径上的某个值
//    /boss/1;age=20/2;age=10
    @GetMapping("/boss/{bossId}/{empId}")
    public Map bossAndEmpId(@MatrixVariable(value = "age",pathVar = "bossId") Integer bossAge,
                            @MatrixVariable(value = "age",pathVar = "empId") Integer empId){
        Map<String,Object> map = new HashMap<>();
        map.put("bossAge", bossAge);
        map.put("empId", empId);
        return map;
    }
/*访问http://localhost:8080/boss/1;age=20/2;age=10，返回如下数据：
        {
        "empId": 10,
        "bossAge": 20
        }
* */
}
