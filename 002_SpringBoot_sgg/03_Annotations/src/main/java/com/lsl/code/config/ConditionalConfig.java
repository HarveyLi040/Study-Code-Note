package com.lsl.code.config;
import com.lsl.code.bean.Owner;
import com.lsl.code.bean.Pet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
//@ConditionalOnBean(name = "TomCat")
@ConditionalOnMissingBean(name = "TomCat")
public class ConditionalConfig {

    @Bean
    public Owner owner(){
        Owner owner = new Owner("西部牛仔",30);
        // owner组件依赖于petTomCat组件
        owner.setPet(petTomCat());
        return owner;
    }

//    @Bean("TomCat")
    public Pet petTomCat(){
        return new Pet("TomCat",2);
    }
}
