# 1.SpringBoot + Mybatis + MP

使用SpringBoot将进一步的简化MP的整合，需要注意的是，由于使用SpringBoot需要继承parent，所以需要重新创 建工程，并不是创建子Module

## 1.1 快速入门

数据库表：

```sql
create database mp;
use mp;

-- 创建测试表
-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`user_name` varchar(20) NOT NULL COMMENT '用户名',
`password` varchar(20) NOT NULL COMMENT '密码',
`name` varchar(30) DEFAULT NULL COMMENT '姓名',
`age` int(11) DEFAULT NULL COMMENT '年龄',
`email` varchar(50) DEFAULT NULL COMMENT '邮箱',
PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;
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



**一、导入依赖**

```xml
  <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!--简化代码的工具包-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!--mybatis-plus的springboot支持-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.1.1</version>
        </dependency>

        <!--mysql驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
```

**二、log4j.properties**

```properties
log4j.rootLogger=DEBUG,A1
log4j.appender.A1=org.apache.log4j.ConsoleAppender
log4j.appender.A1.layout=org.apache.log4j.PatternLayout
log4j.appender.file.encoding=UTF-8
log4j.appender.A1.layout.ConversionPattern=[%t] [%c]-[%p] %m%n
```

**三、编写application.properties**

```properties
spring.application.name = mybatisplus-mp-springboot
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/mp?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=123456
```

**四、编写pojo**

```java
package com.lsl.mp.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userName;
    private String password;
    private String name;
    private Integer age;
    private String email;

    public User(String userName, String password, String name, Integer age, String email) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.age = age;
        this.email = email;
    }
}
```

**五、编写mapper**

```java
package com.lsl.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lsl.mp.pojo.User;

public interface UserMapper extends BaseMapper<User> {
}
```

**六、编写启动类**

```java
package com.lsl.mp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.lsl.mp.mapper") //设置mapper接口的扫描包
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

**七、编写测试用例**

```java
package com.lsl.mp;

import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
public class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        List<User> userList = userMapper.selectList(null);
        for (User user : userList) {
            log.info("user:{}",user);
        }
    }
        /*  user:User(id=1, userName=zhangsan, password=123456, name=张三, age=18, email=test1@itcast.cn)
        user:User(id=2, userName=lisi, password=123456, name=李四, age=20, email=test2@itcast.cn)
        user:User(id=3, userName=wangwu, password=123456, name=王五, age=28, email=test3@itcast.cn)
        user:User(id=4, userName=zhaoliu, password=123456, name=赵六, age=21, email=test4@itcast.cn)
        user:User(id=5, userName=sunqi, password=123456, name=孙七, age=24, email=test5@itcast.cn)*/
}
```

# 2. 通用CRUD

##  2.1 插入操作 

插入代码演示

```java
package com.lsl.mp;

import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
@Slf4j
@SpringBootTest
public class TestUserMapper {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        User user01 = new User("liubei", "abcd", "刘备", 50, "23983@qq.com");
        User user02 = new User("zhangfei", "abck", "张飞", 42, "239sdds3@qq.com");

        // 返回受影响的行数
        int result01 = userMapper.insert(user01);
        int result02 = userMapper.insert(user02);

        // 获取自增长的id（写入数据库后会回填对象）
        System.out.println("id01->" + user01.getId());
        System.out.println("id02->" + user02.getId());
    }
    
/*  注意：为防止id自增长出现错误，要在id字段上加上注解@TableId(type = IdType.AUTO) //指定id类型为自增长

    @TableId(type = IdType.AUTO) //指定id类型为自增长
    private Long id;*/
}
```

**@TableField 注解**

在MP中通过@TableField注解可以指定字段的一些属性，常常解决的问题有2个： 

- 对象中的属性名和字段名不一致的问题（非驼峰）

-  对象中的属性字段在表中不存在的问题 

**@TableField注解使用：**

- 假设user对象的mail字段和数据库表`tb_user`的email字段不一致，就可以在mail属性上添加注解` @TableField(value = "email")`
- 假设user对象的有属性address，但是数据库表`tb_user`中不存在address字段，如果user对象中给address复制，那么在数据库插入时就会报错。此时就需要在address字段上添加注解` @TableField(exist = false)`
- 如果查询时不希望返回某些字段的值，也可以在属性上添加`@TableField(select = false)`

```java
package com.lsl.mp.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/25 14:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userName;

    // 查询时不返回该字段的值
    @TableField(select = false)
    private String password;
    private String name;
    private Integer age;

    // 指定数据库表中的字段名
    @TableField(value = "email")
    private String email;

    // 此字段在数据库表中不存在
    @TableField(exist = false)
    private String address;
}
```

效果：

![image-20221225165215326](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212251652007.png)

##  2.2 更新操作

在MP中，更新操作有2种，一种是根据id更新，另一种是根据条件更新

###  2.2.1 根据id更新

```java
package com.lsl.mp;

import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
@Slf4j
@SpringBootTest
public class testUpdateById {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testUpdateById() {
        User user = new User();
        user.setId(6L); //主键
        user.setAge(21); //更新的字段

        //根据id更新，更新不为null的字段
        this.userMapper.updateById(user);
    }

    @Test
    public void UpdateById() {
        User user = new User("liubei", "abcd", "刘备", 50, "23983@qq.com");
        user.setId(1L); // 条件 ——> 根据id做更新
        int result = userMapper.updateById(user);
        System.out.println("result:" + result);
        System.out.println("user:"+user);
    }
}
```

###  2.2.2 根据条件更新

方法定义：

```java
/**
* 根据 whereEntity 条件，更新记录
*
* @param entity 实体对象 (set 条件值,可以为 null)
* @param updateWrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
*/
int update(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T>
updateWrapper);
```

测试用例：

```java
    @Resource
    private UserMapper userMapper;

    @Test
    public void testUpdate() {
        User user = new User();
        user.setAge(32); //更新的字段

        //更新的条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", 6);

        //执行更新操作
        int result = this.userMapper.update(user, wrapper);
        log.info("user:{}",user);
        log.info("result = {}",result);


        User user01 = new User();
        user01.setPassword("abcd");//更新的字段

        //更新的条件
        QueryWrapper<User> wrapper01 = new QueryWrapper<>();
        wrapper01.eq("user_name", "liubei");

        //执行更新操作
        int result01 = this.userMapper.update(user01, wrapper01);
        log.info("user:{}",user);
        log.info("result = {}",result01);
    }

    @Test
    public void testUpdat02() {

        //更新的条件
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        // 更新的字段、更新的条件
        wrapper.set("age",21).set("password","abc123").eq("user_name","lisi");

        //执行更新操作
        int result = userMapper.update(null, wrapper);
        log.info("result = {}",result);
    }
```

##  2.3 删除操作

### 2.3.1 deleteById

方法定义：

```java
/**
* 根据 ID 删除
*
* @param id 主键ID
*/
int deleteById(Serializable id);
```

测试：

```java
    @Resource
    private UserMapper userMapper;

    @Test
    public void testDeleteById(){
        // 根据id删除数据
        int res = userMapper.deleteById(6L);
        log.info("res = {}",res);
    }
```

###  2.3.2 deleteByMap

方法定义：

```java
/**
* 根据 columnMap 条件，删除记录
*
* @param columnMap 表字段 map 对象
*/
int deleteByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);
```

测试用例：

> 根据map中的条件去删除数据，多条件之间是and的关系

```java
package com.lsl.mp;
import com.lsl.mp.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
public class TestDelete {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testDeleteById() {
        // 根据id删除数据
        int res = userMapper.deleteById(6L);
        log.info("res = {}", res);
    }

    @Test
    public void testDeleteByMap() {
        Map<String, Object> map = new HashMap<>();
        // 根据map中的条件去删除数据，多条件之间是and的关系
        map.put("user_name", "liubei");
        map.put("password", "abcd");
        int res = userMapper.deleteByMap(map);
        System.out.println("res = " + res);
    }
}
```

###  2.3.3 delete

> 根据包装的条件做删除

方法定义：

```java
/**
* 根据 entity 条件，删除记录
*
* @param wrapper 实体对象封装操作类（可以为 null）
*/
int delete(@Param(Constants.WRAPPER) Wrapper<T> wrapper);
```

测试用例：

```java
    // delete用法一：
    @Test
    public void testDelete01(){
        // 根据包装的条件做删除
        QueryWrapper<User> wrapper= new QueryWrapper<>();
        wrapper.eq("user_name","zhangfei").eq("password","123456");

        int res = userMapper.delete(wrapper);
        log.info("res = {}",res);
    }

    // delete用法二：
    @Test
    public void testDelete02(){

        User user = new User();
        user.setPassword("123456");
        user.setUserName("sunqi");

        QueryWrapper<User>wrapper = new QueryWrapper<>(user);
        // 根据包装的条件做删除
        int res = userMapper.delete(wrapper);
        log.info("res = {}",res);
    }
```

###  2.3.4 deleteBatchIds

> 根据id做批量删除

方法定义：

```java
/**
* 删除（根据ID 批量删除）
*
* @param idList 主键ID列表(不能为 null 以及 empty)
*/
int deleteBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable>
idList);

```

测试用例：

```
    @Test
    public void testDeleteBatchIds() {
        // 根据id做批量删除
        int res = userMapper.deleteBatchIds(Arrays.asList(2L, 3L,4L));
    }
```

##  2.4 查询操作

MP提供了多种查询操作，包括根据id查询、批量查询、查询单条数据、查询列表、分页查询等操作

###  2.4.1 selectById

> 根据id查询数据

方法定义：

```java
/**
* 根据 ID 查询
*
* @param id 主键ID
*/
T selectById(Serializable id);
```

测试用例：

```java
    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelectById() {
        //根据id查询数据
        User user = this.userMapper.selectById(2L);
        System.out.println("result = " + user);
    }
```

###  2.4.2 selectBatchIds

> 根据id集合批量查询

方法定义：

```java
/**
* 查询（根据ID 批量查询）
*
* @param idList 主键ID列表(不能为 null 以及 empty)
*/
List<T> selectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable>
idList);
```

测试用例：

```java
    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelectBatchIds() {
        //根据id集合批量查询
        List<User> users = this.userMapper.selectBatchIds(Arrays.asList(2L, 3L, 4L));
        for (User user : users) {
            System.out.println(user);
        }
    }
```

###  2.4.3 selectOne

> 根据条件查询一条数据，如果结果超过一条会报错

方法定义：

```java
/**
* 根据 entity 条件，查询一条记录
*
* @param queryWrapper 实体对象封装操作类（可以为 null）
*/
T selectOne(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
```

测试用例：

```java
    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelectOne() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.eq("name", "李四");
        //根据条件查询一条数据，如果结果超过一条会报错
        User user = this.userMapper.selectOne(wrapper);
        System.out.println(user);

//       User(id=2, userName=lisi, password=123456, name=李四, age=20, email=test2@itcast.cn,address=null)
    }
```

###  2.4.4 selectCount

> 根据条件查询数据条数

方法定义：

```java
/**
* 根据 Wrapper 条件，查询总记录数
*
* @param queryWrapper 实体对象封装操作类（可以为 null）
*/
Integer selectCount(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
```

测试用例：

```java
    @Resource
    private UserMapper userMapper;
    
    @Test
    public void testSelectCount() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.gt("age", 23); //年龄大于23岁
        //根据条件查询数据条数
        Integer count = this.userMapper.selectCount(wrapper);
        System.out.println("count = " + count);

//        count = 2
    }
```

###  2.4.5 selectList

>根据条件查询数据

方法定义：

```java
/**
* 根据 entity 条件，查询全部记录
*
* @param queryWrapper 实体对象封装操作类（可以为 null）
*/
List<T> selectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
```

测试用例：

```java
    @Test
    public void testSelectList01() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.gt("age", 23); //年龄大于23岁

        //根据条件查询数据
        List<User> users = this.userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println("user = " + user);
        }

/*      user = User(id=3, userName=wangwu, password=123456, name=王五, age=28,email=test3@itcast.cn, address=null)
        user = User(id=5, userName=sunqi, password=123456, name=孙七, age=24,email=test5@itcast.cn, address=null)*/
    }

    @Test
    public void testSelectList02() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.like("email","itcast"); //年龄大于23岁

        //根据条件查询数据
        List<User> users = this.userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println("user = " + user);
        }
        
/*      user = User(id=1, userName=zhangsan, password=null, name=张三, age=18, email=test1@itcast.cn, address=null)
        user = User(id=2, userName=lisi, password=null, name=李四, age=20, email=test2@itcast.cn, address=null)
        user = User(id=3, userName=wangwu, password=null, name=王五, age=28, email=test3@itcast.cn, address=null)
        user = User(id=4, userName=zhaoliu, password=null, name=赵六, age=21, email=test4@itcast.cn, address=null)
        user = User(id=5, userName=sunqi, password=null, name=孙七, age=24, email=test5@itcast.cn, address=null)*/
    }

```

###  2.4.6 selectPage

>根据条件查询数据

方法定义：

```java
/**
* 根据 entity 条件，查询全部记录（并翻页）
*
* @param page 分页查询条件（可以为 RowBounds.DEFAULT）
* @param queryWrapper 实体对象封装操作类（可以为 null）
*/
IPage<T> selectPage(IPage<T> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
```

配置分页插件：

```java
package com.lsl.mp.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.lsl.mp.mapper") //设置mapper接口的扫描包
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
```

测试用例：

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
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
public class TestSelect {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelectPage() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.gt("age", 20); //年龄大于20岁
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
        user = User(id=3, userName=wangwu, password=123456, name=王五,age=28,email=test3@itcast.cn, address=null)*/
    }

}
```



# 3.SQL注入的原理

前面我们已经知道，MP在启动后会将BaseMapper中的一系列的方法注册到meppedStatements中，那么究竟是如 何注入的呢？流程又是怎么样的？下面我们将一起来分析下。 在MP中，ISqlInjector负责SQL的注入工作，它是一个接口，AbstractSqlInjector是它的实现类，实现关系如下：

![image-20221226000811487](C:\Users\22418\AppData\Roaming\Typora\typora-user-images\image-20221226000811487.png)



在AbstractSqlInjector中，主要是由inspectInject()方法进行注入的，如下：

```java
    public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
        Class<?> modelClass = this.extractModelClass(mapperClass);
        if (modelClass != null) {
            String className = mapperClass.toString();
            Set<String> mapperRegistryCache = GlobalConfigUtils.getMapperRegistryCache(builderAssistant.getConfiguration());
            if (!mapperRegistryCache.contains(className)) {
                List<AbstractMethod> methodList = this.getMethodList();
                if (CollectionUtils.isNotEmpty(methodList)) {
                    TableInfo tableInfo = TableInfoHelper.initTableInfo(builderAssistant, modelClass);
                    methodList.forEach((m) -> {
                        m.inject(builderAssistant, mapperClass, modelClass, tableInfo);
                    });
                } else {
                    logger.debug(mapperClass.toString() + ", No effective injection method was found.");
                }

                mapperRegistryCache.add(className);
            }
        }

    }
```



在实现方法中， methodList.forEach(m -> m.inject(builderAssistant, mapperClass, modelClass, tableInfo)); 是关键，循环遍历方法，进行注入。

 最终调用抽象方法injectMappedStatement进行真正的注入：

```java
/**
* 注入自定义 MappedStatement
*
* @param mapperClass mapper 接口
* @param modelClass mapper 泛型
* @param tableInfo 数据库表反射信息
* @return MappedStatement
*/
public abstract MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?
> modelClass, TableInfo tableInfo);

```

查看该方法的实现：

![image-20221226001327281](C:\Users\22418\AppData\Roaming\Typora\typora-user-images\image-20221226001327281.png)

以SelectById为例查看：

```java
public class SelectById extends AbstractMethod {
    public SelectById() {
    }

    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.LOGIC_SELECT_BY_ID;
        SqlSource sqlSource = new RawSqlSource(this.configuration, String.format(sqlMethod.getSql(), this.sqlSelectColumns(tableInfo, false), tableInfo.getTableName(), tableInfo.getKeyColumn(), tableInfo.getKeyProperty(), tableInfo.getLogicDeleteSql(true, false)), Object.class);
        return this.addSelectMappedStatement(mapperClass, sqlMethod.getMethod(), sqlSource, modelClass, tableInfo);
    }
}
```

可以看到，生成了SqlSource对象，再将SQL通过addSelectMappedStatement方法添加到meppedStatements中

![image-20221226001536077](C:\Users\22418\AppData\Roaming\Typora\typora-user-images\image-20221226001536077.png)
