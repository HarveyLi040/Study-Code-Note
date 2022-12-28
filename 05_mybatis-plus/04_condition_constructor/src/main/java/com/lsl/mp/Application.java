package com.lsl.mp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lsl.mp.mapper") //设置mapper接口的扫描包
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
