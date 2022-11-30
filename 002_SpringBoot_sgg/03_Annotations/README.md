##  1.@Conditional注解

####   1.1 条件装配

- 条件装配:满足Conditional指定的条件，则进行组件注入

- @ConditionalOnBean()可以注解在类上，也可以注解在方法上
- 条件成立时方法返回的值才能被注入容器

####   1.2 演示：@ConditionalOnBean()

```java
package com.lsl.code.config;
import com.lsl.code.bean.Owner;
import com.lsl.code.bean.Pet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ConditionalConfig {

    @Bean
    @ConditionalOnBean(name = "TomCat")
    public Owner owner(){
        Owner owner = new Owner("西部牛仔",30);
        // owner组件依赖于petTomCat组件
        owner.setPet(petTomCat());
        return owner;
    }
    
// 注掉@Bean -> 容器中没有TomCat组件
//  @Bean("TomCat")
    public Pet petTomCat(){
        return new Pet("TomCat",2);
    }
}
```

测试@ConditionalOnBean()

```java
package com.lsl.code;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        System.out.println("容器中是否存在TomCat组件：" + run.containsBean("TomCat"));
        System.out.println("容器中是否存在owner组件：" + run.containsBean("owner"));
    }
}
/*输出：
            容器中是否存在TomCat组件：false
            容器中是否存在owner组件：false*/
```

####  1.3  演示：@ConditionalOnMissingBean()

```java
package com.lsl.code.config;
import com.lsl.code.bean.Owner;
import com.lsl.code.bean.Pet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConditionalOnMissingBean(name = "TomCat")
public class ConditionalConfig {
    @Bean
    public Owner owner(){
        Owner owner = new Owner("西部牛仔",30);
        // owner组件依赖于petTomCat组件
        owner.setPet(petTomCat());
        return owner;
    }
}
```

测试@ConditionalOnMissingBean()

```java
package com.lsl.code;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        System.out.println("容器中是否存在TomCat组件：" + run.containsBean("TomCat"));
        System.out.println("容器中是否存在owner组件：" + run.containsBean("owner"));
        // 2.查看容器里面的组件
//        String[] beanNames = run.getBeanDefinitionNames();
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }
    }
}
/*输出
        容器中是否存在TomCat组件：false
        容器中是否存在owner组件：true*/
```

##   2. @ImportResource()注解

**@ImportResource()注解**

@ImportResource注解用于导入Spring的配置文件，让配置文件(以前的springmvc.xml、applicationContext.xml)里的Bean注入容器并生效

**@ImportResource()注解演示：**

一、声明配置文件：src/main/resources/beans.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="pig" class="com.lsl.code.bean.Pig">
        <property name="name" value="mumu"></property>
    </bean>
    <bean id="dog" class="com.lsl.code.bean.Dog">
        <property name="name" value="wangwang"></property>
    </bean>
</beans>
```

二、@ImportResource()导入配置文件

```java
package com.lsl.code.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
@Configuration
@ImportResource("classpath:beans.xml")
public class ImportResourceConfig {
}
```

三、测试：

```java
package com.lsl.code;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        System.out.println("容器中是否存在pig组件：" + run.containsBean("pig"));
        System.out.println("容器中是否存在dog组件：" + run.containsBean("dog"));
    }
}
/*输出：
        容器中是否存在pig组件：true
        容器中是否存在dog组件：true*/
```

##  3. @ConfigurationProperties()注解——配置绑定

####   3.1 方式1：利用@Component和@ConfigurationProperties()实现配置绑定

一、在application.properties中添加配置

```properties
mycar.brand=BYD
mycar.price=100000
```

二、利用@ConfigurationProperties()注解读取配置

```java
package com.lsl.code.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*只有在容器中的组件，才会拥有spring boot提供的强大功能*/
@Data
@Component
@ConfigurationProperties(prefix = "mycar")
public class MyCar {
    private String brand;
    private Integer price;
}
```

三、测试

```java
package com.lsl.code.controller;
import com.lsl.code.bean.MyCar;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
@RestController
public class CarController {
    @Resource
    MyCar myCar;
    @RequestMapping("/car")
    public MyCar car(){
        return myCar;
    }
}
/*访问http://localhost:8080/car 返回
                    {
                        brand: "BYD",
                        price: 100000
                    }*/
```

####   3.2 方式二：@EnableConfigurationProperties + @ConfigurationProperties + @Configuration

一、在application.properties中添加配置

```properties
mycar.brand=BYD
mycar.price=100000
```

二、利用@ConfigurationProperties()注解读取配置

```java
package com.lsl.code.bean;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
/*只有在容器中的组件，才会拥有spring boot提供的强大功能*/
@Data
@ConfigurationProperties(prefix = "mycar")
public class MyCar {
    private String brand;
    private Integer price;
}
```

三、在配置类上添加@EnableConfigurationProperties(MyCar.class)

```java
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
```

四、测试：

```java
package com.lsl.code.controller;
import com.lsl.code.bean.MyCar;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
@RestController
public class CarController {
    @Resource
    MyCar myCar;
    @RequestMapping("/car")
    public MyCar car(){
        return myCar;
    }
}
/*访问http://localhost:8080/car 返回
                    {
                        brand: "BYD",
                        price: 100000
                    }*/
```

##   4.@SpringBootApplication自动配置原理

**@SpringBootApplication主要的三个注解：**

- **@SpringBootConfiguration代表当前类是一个配置类**

- **@ComponentScan() 包扫描**

- **@EnableAutoConfiguration**
  - @Import({AutoConfigurationImportSelector.class}) //给容器中批量导入组件

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
public @interface SpringBootApplication {
    @AliasFor(
        annotation = EnableAutoConfiguration.class
    )
    Class<?>[] exclude() default {};

    @AliasFor(
        annotation = EnableAutoConfiguration.class
    )
    String[] excludeName() default {};

    @AliasFor(
        annotation = ComponentScan.class,
        attribute = "basePackages"
    )
    String[] scanBasePackages() default {};

    @AliasFor(
        annotation = ComponentScan.class,
        attribute = "basePackageClasses"
    )
    Class<?>[] scanBasePackageClasses() default {};

    @AliasFor(
        annotation = ComponentScan.class,
        attribute = "nameGenerator"
    )
    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

    @AliasFor(
        annotation = Configuration.class
    )
    boolean proxyBeanMethods() default true;
}
```

 **@EnableAutoConfiguration注解**

- 利用getAutoConfigurationEntry(annotationMetadata);给容器中批量导入一些组件
- 调用List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes)获取到所有需要导入到容器中的配置类
- 利用工厂加载 Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader)；得到所有的组件
- 从META-INF/spring.factories位置来加载一个文件
  - 默认扫描当前系统中所有META-INF/spring.factories位置的文件
  - spring-boot-autoconfigure-2.3.4.RELEASE.jar包里面也有META-INF/spring.factories

```Java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
//利用Registrar给容器中导入一系列组件
@Import({AutoConfigurationImportSelector.class}) //给容器中导入一个组件
public @interface EnableAutoConfiguration {
    String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

    Class<?>[] exclude() default {};

    String[] excludeName() default {};
}
```

**AutoConfigurationImportSelector类的核心方法getAutoConfigurationEntry —> 获取所有自动配置的集合**

```java
protected AutoConfigurationImportSelector.AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
    if (!this.isEnabled(annotationMetadata)) {
        return EMPTY_ENTRY;
    } else {
        AnnotationAttributes attributes = this.getAttributes(annotationMetadata);
        List<String> configurations = this.getCandidateConfigurations(annotationMetadata, attributes);
        configurations = this.removeDuplicates(configurations);
        Set<String> exclusions = this.getExclusions(annotationMetadata, attributes);
        this.checkExcludedClasses(configurations, exclusions);
        configurations.removeAll(exclusions);
        configurations = this.getConfigurationClassFilter().filter(configurations);
        this.fireAutoConfigurationImportEvents(configurations, exclusions);
        return new AutoConfigurationImportSelector.AutoConfigurationEntry(configurations, exclusions);
    }
}
```

虽然127个场景的所有自动配置启动的时候默认全部加载 ，但是按照条件装配规则（@Conditional），最终会按需配置

**总结:**

-  SpringBoot先加载所有的自动配置类xxxxxAutoConfiguration
- 每个自动配置类按照条件进行生效，默认都会绑定配置文件指定的值。默认值从xxxProperties里面拿，xxxProperties和配置文件进行了绑定
- 生效的配置类就会给容器中装配很多组件
- 只要容器中有这些组件，相当于这些功能就有了
- 定制化配置
  - 用户直接利用@Bean注解注入组件替换底层的组件
  - 用户去看特定组件获取的配置文件，根据情况修改配置    

注解xxxAutoConfiguration导入若干组件 —> 组件从xxxProperties里面获取具体配置值 —> xxxProperties 可从application.properties获取值

