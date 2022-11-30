package com.lsl.code.config;

import com.lsl.code.Bean.Pet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

// 矩阵变量配置: ————> 两种配置方式

@Configuration(proxyBeanMethods = false)
public class MatrixVariableConfig /*implements WebMvcConfigurer*/ {
/*    @Override
    public void configurePathMatch(PathMatchConfigurer configurer){
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        // 设置为不移除分号;后面的的内容，设置后矩阵变量才可以生效
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }*/
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {
                UrlPathHelper urlPathHelper =  new UrlPathHelper();
                // 设置为不移除分号;后面的的内容，设置后矩阵变量才可以生效
                urlPathHelper.setRemoveSemicolonContent(false);
                configurer.setUrlPathHelper(urlPathHelper);
            }

            @Override
            public void addFormatters(FormatterRegistry registry){
                 registry.addConverter(new Converter<String, Pet>() {
                     @Override
                     public Pet convert(String source){
                         if(!StringUtils.isEmpty(source)){
                             Pet pet = new Pet();
                             String[] split = source.split(",");
                             pet.setName(split[0]);
                             pet.setAge(split[1]);
                             return pet;
                         }
                         return null;
                     }
                 });
            }
        };

    }
}
