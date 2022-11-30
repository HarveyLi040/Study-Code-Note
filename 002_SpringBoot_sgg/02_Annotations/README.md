##  1.注解使用：@Configuration 、@Import

####  1.1  @Configuration 、@Import演示

**springBoot创建Bean**

User类

```java
package com.lsl.code.bean;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class User {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

```

Cat类

```java
package com.lsl.code.bean;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Cat {
    private String name;

    public Cat(String name) {
        this.name = name;
    }
}
```

BeanConfig配置类

```java
package com.lsl.code.config;

import com.lsl.code.bean.User;
import com.lsl.code.springbean.bean.Owner;
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
@Import({Owner.class, MDCContextMap.class})
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
    public User user() {
        return new User("zhangsan", 18);
    }

    @Bean
    public Cat cat() {
        return new Cat("黑猫警长");
    }
}
```

获取Bean：

```java
package com.lsl.code;

import com.lsl.code.bean.User;
import com.lsl.code.springbean.bean.Owner;
import com.lsl.code.bean.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // 1.返回IOC容器
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);

        // 2.查看容器里面的组件
        String[] beanNames = run.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
        // 3.从容器中获取主键
        User user01 = run.getBean("user", User.class);
        User user02 = run.getBean("user", User.class);
        System.out.println(user01 == user02); //true -> 注册的组件默认是单实例的
    }

}
/*控制台输出：
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.annotation.internalCommonAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
application
org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory
beanConfig
com.lsl.code.springbean.bean.Owner
org.apache.logging.slf4j.MDCContextMap
user
cat
org.springframework.boot.autoconfigure.AutoConfigurationPackages
org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration
propertySourcesPlaceholderConfigurer
org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration
mbeanExporter
objectNamingStrategy
mbeanServer
org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor
org.springframework.boot.context.internalConfigurationPropertiesBinderFactory
org.springframework.boot.context.internalConfigurationPropertiesBinder
org.springframework.boot.context.properties.BoundConfigurationProperties
org.springframework.boot.context.properties.EnableConfigurationPropertiesRegistrar.methodValidationExcludeFilter
spring.jmx-org.springframework.boot.autoconfigure.jmx.JmxProperties
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration
springApplicationAdminRegistrar
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration$ClassProxyingConfiguration
forceAutoProxyCreatorToUseClassProxying
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration
org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration
applicationAvailability
org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration
org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration
lifecycleProcessor
spring.lifecycle-org.springframework.boot.autoconfigure.context.LifecycleProperties
org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration
spring.info-org.springframework.boot.autoconfigure.info.ProjectInfoProperties
org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration
spring.sql.init-org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties
org.springframework.boot.sql.init.dependency.DatabaseInitializationDependencyConfigurer$DependsOnDatabaseInitializationPostProcessor
org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
taskExecutorBuilder
applicationTaskExecutor
spring.task.execution-org.springframework.boot.autoconfigure.task.TaskExecutionProperties
org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration
scheduledBeanLazyInitializationExcludeFilter
taskSchedulerBuilder
spring.task.scheduling-org.springframework.boot.autoconfigure.task.TaskSchedulingProperties
org.springframework.aop.config.internalAutoProxyCreator
true*/
```

####   1.2总结

**@Configuration**

- 被@Configuration注解的类表示是配置类， 配置类的作用等同于配置文件，配置类本身也是组件

  - Full模式：proxyBeanMethods = true ,保证每个@Bean方法被调用多少次返回的组件都是单实例的

  - Full模式优点：保证单实例

-  Lite模式：proxyBeanMethods = false ,每个@Bean方法被调用多少次返回的组件都是新创建的

  - Lite模式优点：springboot不会判断@Bean注解的方法返回的组件在容器中是否存在，故启动更快

- Lite模式&Full模式最佳实战

  - 配置类组件之间无依赖关系用Lite模式加速容器启动过程，减少判断

  - 配置类组件之间有依赖关系，方法会被调用得到之前单实例组件，用Full模式

**@Import**

- 向容器中导入组件，默认组件的名字就是全类名
- @Import注解高级用法：https://www.bilibili.com/video/BV1gW411W7wy?p=8

**另外，以下注解也可以创建Bean并注入容器**

  @Bean、@Component、@Controller、@Service、@Repository