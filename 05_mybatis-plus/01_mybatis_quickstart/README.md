# 1、了解Mybatis-Plus 

### 1.1、Mybatis-Plus介绍 

MyBatis-Plus（简称 MP）是一个 MyBatis 的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高 效率而生。 官网：https://mybatis.plus/ 或 https://mp.baomidou.com/

### 1.2、代码以及文档 

文档地址：https://mybatis.plus/guide/ 

源码地址：https://github.com/baomidou/mybatis-plus 

### 1.3、特性

 **无侵入**：只做增强不做改变，引入它不会对现有工程产生影响，如丝般顺滑 

**损耗小**：启动即会自动注入基本 CURD，性能基本无损耗，直接面向对象操作 

**强大的CRUD 操作**：内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作， 更有强大的条件构造器，满足各类使用需求 

**支持 Lambda 形式调用**：通过 Lambda 表达式，方便的编写各类查询条件，无需再担心字段写错 

**支持多种数据库**：支持 MySQL、MariaDB、Oracle、DB2、H2、HSQL、SQLite、Postgre、 SQLServer2005、SQLServer 等多种数据库 

**支持主键自动生成**：支持多达 4 种主键策略（内含分布式唯一 ID 生成器 - Sequence），可自由配置，完美解 决主键问题 

**支持 XML 热加载**：Mapper 对应的 XML 支持热加载，对于简单的 CRUD 操作，甚至可以无 XML 启动 

**支持 ActiveRecord 模式**：支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可进行强大的 CRUD 操 作

 **支持自定义全局通用操作**：支持全局通用方法注入（ Write once, use anywhere ） 

**支持关键词自动转义**：支持数据库关键词（order、key......）自动转义，还可自定义关键词 

**内置代码生成器**：采用代码或者 Maven 插件可快速生成 Mapper 、 Model 、 Service 、 Controller 层代码， 支持模板引擎，更有超多自定义配置等您来使用 

**内置分页插件**：基于 MyBatis 物理分页，开发者无需关心具体操作，配置好插件之后，写分页等同于普通 List 查询 

**内置性能分析插件**：可输出 Sql 语句以及其执行时间，建议开发测试时启用该功能，能快速揪出慢查询 

**内置全局拦截插件**：提供全表 delete 、 update 操作智能分析阻断，也可自定义拦截规则，预防误操作 内置 Sql 注入剥离器：支持 Sql 注入剥离，有效预防 Sql 注入攻击 

### 1.4、架构

Mybatis-Plus 架构

![image-20221225002034691](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212250020705.png)

###  1.5 作者

Mybatis-Plus是由baomidou（苞米豆）组织开发并且开源的，目前该组织大概有30人左右

码云地址：https://gitee.com/organizations/baomidou

# 2、快速开始

###  2.1 创建数据库表

对于Mybatis整合MP有常常有三种用法，分别是Mybatis+MP、Spring+Mybatis+MP、Spring Boot+Mybatis+MP

创建数据库表：

```sql
create database mp;
use mp;

-- 创建测试表
CREATE TABLE `tb_user` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`user_name` varchar(20) NOT NULL COMMENT '用户名',
`password` varchar(20) NOT NULL COMMENT '密码',
`name` varchar(30) DEFAULT NULL COMMENT '姓名',
`age` int(11) DEFAULT NULL COMMENT '年龄',
`email` varchar(50) DEFAULT NULL COMMENT '邮箱',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- 插入测试数据
INSERT INTO `tb_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES
('1', 'zhangsan', '123456', '张三', '18', 'test1@itcast.cn');
INSERT INTO `tb_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES
('2', 'lisi', '123456', '李四', '20', 'test2@itcast.cn');
INSERT INTO `tb_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES
('3', 'wangwu', '123456', '王五', '28', 'test3@itcast.cn');
INSERT INTO `tb_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES
('4', 'zhaoliu', '123456', '赵六', '21', 'test4@itcast.cn');
INSERT INTO `tb_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES
('5', 'sunqi', '123456', '孙七', '24', 'test5@itcast.cn');

select * from `tb_user`;
```

###  2.2 引入依赖

```xml
<!-- mybatis-plus插件依赖 -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus</artifactId>
            <version>3.1.1</version>
        </dependency>

        <!-- MySql -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>

        <!-- 连接池 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.11</version>
        </dependency>

        <!--简化bean代码的工具包-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.6.4</version>
        </dependency>
```

###  2.3 Mybatis实现查询User

下面演示通过纯Mybatis与Mybatis-Plus整合,利用Mybatis实现查询User

**第一步，编写mybatis-config.xml文件：**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true" />
    </settings>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://127.0.0.1:3306/mp?useUnicode=true&amp;characterEncoding=utf8&amp;autoReconnect=true&amp;allowMultiQueries=true&amp;useSSL=false"/>
                <property name="username" value="root"/>
                <property name="password" value="W135246W"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="UserMapper.xml"/>
    </mappers>
</configuration>
```

**第二步，编写User实体对象：**

```java
package com.lsl.mp.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String userName;
    private String password;
    private String name;
    private Integer age;
    private String email;
}
```

**第三步，编写UserMapper接口：**

```java
package com.lsl.mp.mapper;

import com.lsl.mp.pojo.User;
import java.util.List;
public interface UserMapper {
    
    List<User> findAll();
    
}
```

**第四步，编写UserMapper.xml文件：**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.lsl.mp.mapper.UserMapper">
    <select id="findAll" resultType="com.lsl.mp.pojo.User">
        select * from tb_user
    </select>
</mapper>
```

**第五步，编写TestMybatis测试用例：**

```java
package com.lsl.mp;

import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
@Slf4j
class ApplicationTests {

    @Test
    void TestFindAll() throws IOException {
        String configFileName = "mybatis-config.xml";
        InputStream resourceAsStream = Resources.getResourceAsStream(configFileName);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 测试查询
        List<User> users = userMapper.findAll();
        for (User user : users) {
           log.info("user:{}",user);
        }
    /*  user:User(id=1, userName=zhangsan, password=123456, name=张三, age=18, email=test1@itcast.cn)
        user:User(id=2, userName=lisi, password=123456, name=李四, age=20, email=test2@itcast.cn)
        user:User(id=3, userName=wangwu, password=123456, name=王五, age=28, email=test3@itcast.cn)
        user:User(id=4, userName=zhaoliu, password=123456, name=赵六, age=21, email=test4@itcast.cn)
        user:User(id=5, userName=sunqi, password=123456, name=孙七, age=24, email=test5@itcast.cn)*/
    }
}
```





