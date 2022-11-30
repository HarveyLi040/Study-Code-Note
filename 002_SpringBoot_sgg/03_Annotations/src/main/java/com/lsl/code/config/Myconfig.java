package com.lsl.code.config;
import com.lsl.code.bean.MyCar;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@EnableConfigurationProperties(MyCar.class)
//为MyCar开启配置绑定
    //1、开启Car配置绑定功能
    //2、把这个Car这个组件自动注册到容器中
public class Myconfig {
}
