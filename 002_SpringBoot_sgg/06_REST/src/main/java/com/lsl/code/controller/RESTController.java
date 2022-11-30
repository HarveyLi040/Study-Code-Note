package com.lsl.code.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class RESTController {
//    @RequestMapping(value = "/user",method = RequestMethod.GET)
    @GetMapping("/user")
    public String getUser(){
        return "GET";
    }

//    @RequestMapping(value = "/user",method = RequestMethod.POST)
    @PostMapping("/user")
    public String saveUser(){
        return "POST";
    }


//    @RequestMapping(value = "/user",method = RequestMethod.PUT)
    @PutMapping("/user")
    public String putUser(){
        return "PUT";
    }

//    @RequestMapping(value = "/user",method = RequestMethod.DELETE)
    @DeleteMapping("/user")
    public String deleteUser(){
        return "DELETE";
    }
}

