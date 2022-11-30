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

        System.out.println("容器中是否存在pig组件：" + run.containsBean("pig"));
        System.out.println("容器中是否存在dog组件：" + run.containsBean("dog"));
        // 2.查看容器里面的组件
//        String[] beanNames = run.getBeanDefinitionNames();
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }
    }
}
/*输出：
        容器中是否存在pig组件：true
        容器中是否存在dog组件：true*/
