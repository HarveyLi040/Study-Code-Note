package com.lsl.code.config;

import com.lsl.code.bean.ImportBean;
import com.lsl.code.bean.User;
import com.lsl.code.bean.Cat;
import org.apache.logging.slf4j.MDCContextMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/*@Configuration
        告诉SpringBoot这是一个配置类
        配置类的作用等同于配置文件
        配置类（此处的BeanConfig）本身也是组件*/

/*     如果@Configuration(proxyBeanMethods = true)，代理对象调用方法，
       SpringBoot总会检查这个组件是否在容器中有 ——>  保持组件单实例*/


/*proxyBeanMethods：代理bean的方法
       Full模式：proxyBeanMethods = true ,保证每个@Bean方法被调用多少次返回的组件都是单实例的
               Full模式优点：保证单实例
               如果组件被依赖（其他组件还要使用它），则采用Full模式，将proxyBeanMethods 设置为 true
       Lite模式：proxyBeanMethods = false ,每个@Bean方法被调用多少次返回的组件都是新创建的
               Lite模式优点：springboot不会判断@Bean注解的方法返回的组件在容器中是否存在，故启动更快
               如果不依赖容器中的组件，则采用Lite模式,将proxyBeanMethods 设置为 false*/

@Configuration(proxyBeanMethods = true)
@Import({ImportBean.class, MDCContextMap.class})
/* @Import({Owner.class, MDCContextMap.class})
 *      给容器中自动创建出这两个类型的组件、默认组件的名字就是全类名
 */
public class BeanConfig {
/*@Bean给容器中添加组件:
        以方法名作为组件的id
        返回类型就是组件类型
        返回的值，就是组件在容器中的实例

        配置类里面使用@Bean标注在方法上给容器注册的组件默认是单实例的
        外部无论对配置类中的这个组件注册方法调用多少次获取的都是之前注册容器中的单实例对象
        proxyBeanMethods :代理bean的方法*/
    @Bean
    public User user(){
     return new User("zhangsan",18);
    }

    @Bean
    public Cat cat(){
        return new Cat("黑猫警长");
    }
}

