package com.lsl.code.controller;
import com.lsl.code.yaml.Person;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
@RestController
public class YamlController {
    @Resource
    private Person person;
    @RequestMapping("/person")
    public Person person(){
        return person;
    }
}
