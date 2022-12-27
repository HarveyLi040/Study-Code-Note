# 配置

在MP中有大量的配置，其中有一部分是Mybatis原生的配置，另一部分是MP的配置，详情：https://mybatis.plus/config/

下面们对常用的配置做讲解

# 1.基本配置

##  1.1 configLocation

MyBatis 配置文件位置，如果您有单独的 MyBatis 配置，请将其路径配置到 configLocation 中。 MyBatis Configuration 的具体内容请参考MyBatis 官方文档 

在Spring MVC中指定全局配置文件：

```xml
<bean id="sqlSessionFactory"
class="com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean">
<property name="configLocation" value="classpath:mybatis-config.xml"/>
</bean>
```

在Spring Boot中指定全局配置文件：

```properties
# 指定全局配置文件
mybatis-plus.config-location=classpath:mybatis-config.xml
```

**测试配置是否生效：**

- 在resources目录下添加`mybatis-config.xml`,并通过该文件配置分页插件`PaginationInterceptor`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <plugins>
        <plugin interceptor="com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor"></plugin>
    </plugins>
</configuration>
```

- 测试配置是否生效：

```java
package com.lsl.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
public class TestconfigLocation {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelectPage() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.gt("age", 20); //年龄大于20岁

        // 查询第一页，查询一条数据
        Page<User> page = new Page<>(1, 1);
        //根据条件查询数据
        IPage<User> iPage = this.userMapper.selectPage(page, wrapper);
        System.out.println("数据总条数：" + iPage.getTotal());
        System.out.println("总页数：" + iPage.getPages());
        List<User> users = iPage.getRecords();
        for (User user : users) {
            System.out.println("user = " + user);
        }

/*      数据总条数：3
        总页数：3
        user = User(id=3, userName=wangwu, password=123456, name=王五, age=28,email=test3@itcast.cn, address=null)*/
    }
}
```

##  1.2 mapperLocations

MyBatis Mapper 所对应的 XML 文件位置，如果您在 Mapper 中有自定义方法（XML 中有自定义实现），需要进行 该配置，告诉 Mapper 所对应的 XML 文件位置

Spring MVC：

```xml
<bean id="sqlSessionFactory"
class="com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean">
<property name="mapperLocations" value="classpath*:mybatis/*.xml"/>
</bean>
```

Spring Boot：

```properties
# 指定Mapper.xml文件路径
# Maven 多模块项目的扫描路径需以 classpath*: 开头 （即加载多个 jar 包下的 XML 文件）
mybatis-plus.mapper-locations = classpath*:mybatis/*.xml
```

**配置演示：**

- 在`application.properties`中添加配置

```properties
mybatis-plus.mapper-locations = classpath*:mybatis/*.xml
```

- 自定义查询方法`User findById(Long id);`

```java
package com.lsl.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lsl.mp.pojo.User;

public interface UserMapper extends BaseMapper<User> {
    User findById(Long id);
}
```

- 在`resources/mybatis`路径下添加`UserMapper.xml`文件，编写自定义的`findById`：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.lsl.mp.mapper.UserMapper">
    <select id="findById" resultType="com.lsl.mp.pojo.User">
        select * from tb_user where id = #{id}
    </select>
</mapper>
```

- 测试

```java
package com.lsl.mp;

import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
@SpringBootTest
public class TestmapperLocations {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testFindById() {
        User user = userMapper.findById(2L);
        System.out.println(user);
//      User(id=2, userName=lisi, password=123456, name=李四, age=20, email=test2@itcast.cn, address=null)
    }
}
```

可以看到执行了自定义的sql语句：

![image-20221227084117162](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212270843831.png)

## 1.3 typeAliasesPackage

MyBaits 别名包扫描路径，通过该属性可以给包中的类注册别名，注册后在 Mapper 对应的 XML 文件中可以直接使 用类名，而不用使用全限定的类名（即 XML 中调用的时候不用包含包名）

Spring Boot：

```properties
mybatis-plus.type-aliases-package=com.lsl.mp.pojo
```

Spring MVC：

```xml
<bean id="sqlSessionFactory"
class="com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean">
<property name="typeAliasesPackage"
value="com.baomidou.mybatisplus.samples.quickstart.entity"/>
</bean>
```

配置后mapper文件可以直接使用类名：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.lsl.mp.mapper.UserMapper">
    <select id="findById" resultType="User">
        select * from tb_user where id = #{id}
    </select>
</mapper>
```

