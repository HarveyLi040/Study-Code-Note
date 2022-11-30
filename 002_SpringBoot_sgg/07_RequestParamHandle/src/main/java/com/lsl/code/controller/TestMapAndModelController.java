package com.lsl.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestMapAndModelController {
    @GetMapping("/TestMapAndModel")
    public String testParam(Map<String,Object> map,
                            Model model,
                            HttpServletRequest request,
                            HttpServletResponse response){
         map.put("map","_map_");
         model.addAttribute("model","_model_");
         return "forward:/succeed";
    }
/*访问http://localhost:8080/TestMapAndModel，返回如下数据
    {
        "model": "_model_",
         "map": "_map_"
    }*/
    @ResponseBody
    @GetMapping("/succeed")
    public  Map<String,Object> success(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("map",request.getAttribute("map"));
        map.put("model",request.getAttribute("model"));
        return map;
    }
}
