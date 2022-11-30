package com.lsl.code;
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
        User user01 = run.getBean("user",User.class);
        User user02 = run.getBean("user",User.class);
        System.out.println(user01==user02); //true -> 注册的组件默认是单实例的
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
com.lsl.code.bean.ImportBean
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
*/
