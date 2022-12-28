# 条件构造器

#  1. 条件构造器

##  1.1 简介

在MP中，Wrapper接口的实现类关系如下：

![img](https://img-blog.csdnimg.cn/a33551c5113a4c8a997686223085de53.png)

可以看到，AbstractWrapper和AbstractChainWrapper是重点实现，接下来我们重点学习AbstractWrapper以及其 子类

**说明:** 

> QueryWrapper(LambdaQueryWrapper) 和 UpdateWrapper(LambdaUpdateWrapper) 的父类 用于生成 sql 的 where 条件, entity 属性也用于生成 sql 的 where 条件 注意: entity 生成的 where 条件与 使用各个 api 生成 的 where 条件没有任何关联行为



官网文档地址：https://mybatis.plus/guide/wrapper.html

##  1.2 allEq

```java
allEq(Map<R, V> params)
allEq(Map<R, V> params, boolean null2IsNull)
allEq(boolean condition, Map<R, V> params, boolean null2IsNull)
```

全部eq(或个别isNull) 

- 个别参数说明: 

  - params : key 为数据库字段名, value 为字段值 

  - null2IsNull : 为 true 则在 map 的 value 为 null 时调用 isNull 方法,为 false 时则忽略 value 为 null 的 

> 例1: allEq({id:1,name:"老王",age:null}) ---> id = 1 and name = '老王' and age is null 
>
> 例2: allEq({id:1,name:"老王",age:null}, false) ---> id = 1 and name = '老王'



```java
allEq(BiPredicate<R, V> filter, Map<R, V> params)
allEq(BiPredicate<R, V> filter, Map<R, V> params, boolean null2IsNull)
allEq(boolean condition, BiPredicate<R, V> filter, Map<R, V> params, boolean
null2IsNull)
```

个别参数说明: 

- filter : 过滤函数,是否允许字段传入比对条件中 params 与 null2IsNull : 同上 

> 例1: allEq((k,v) -> k.indexOf("a") > 0, {id:1,name:"老王",age:null}) ---> name = '老王' and age is null 
>
> 例2: allEq((k,v) -> k.indexOf("a") > 0, {id:1,name:"老王",age:null}, false) ---> name = '老王'



测试:

```java
package com.lsl.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/28 22:53
 */
@Slf4j
@SpringBootTest
public class TestAllEq {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testAllEq01() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "李四");
        params.put("age", "20");
        params.put("password", null);

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.allEq(params);
//执行的sql语句： SELECT id,user_name,name,age,email FROM tb_user WHERE password IS NULL AND name = ? AND age = ?
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
//        没有查到数据
    }

    @Test
    public void testAllEq02() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "李四");
        params.put("age", "20");
        params.put("password", null);

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.allEq(params, false);
//执行的sql语句： SELECT id,user_name,name,age,email FROM tb_user WHERE name = ? AND age = ?
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
//        User(id=2, userName=lisi, password=null, name=李四, age=20, email=test2@itcast.cn, address=null)
    }

    @Test
    public void testAllEq03() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "李四");
        params.put("age", "20");
        params.put("password", "123456");

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.allEq((k, v) -> (k.equals("age") || k.equals("id")), params);
//执行的sql语句：   SELECT id,user_name,name,age,email FROM tb_user WHERE age = ?
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
// User(id=2, userName=lisi, password=null, name=李四, age=20, email=test2@itcast.cn, address=null)
    }

}
```

##  1.3 基本比较操作

**eq** 

- 等于 = 

**ne** 

- 不等于 <> 

**gt** 

- 大于 > 

**ge** 

- 大于等于 >= 

**lt** 

- 小于 < 

**le** 

- 小于等于 <= 

**between** 

- BETWEEN 值1 AND 值2 

**notBetween** 

- NOT BETWEEN 值1 AND 值2 

**in** 

- 字段 IN (value.get(0), value.get(1), ...)

**notIn** 

- 字段 NOT IN (v0, v1, ...)

**测试：**

```java
package com.lsl.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/28 23:59
 */
@Slf4j
@SpringBootTest
public class testCompare {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testEq() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("password", "123456")
                .ge("age", 20)
                .in("name", "李四", "王五", "赵六");

// 执行的sql语句： SELECT id,user_name,name,age,email FROM tb_user WHERE password = ? AND age >= ? AND name IN (?,?,?)
        List<User> users = this.userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }

//   User(id=2, userName=lisi, password=null, name=李四, age=20, email=test2@itcast.cn, address=null)
//   User(id=3, userName=wangwu, password=null, name=王五, age=28, email=test3@itcast.cn, address=null)
//   User(id=4, userName=zhaoliu, password=null, name=赵六, age=21, email=test4@itcast.cn, address=null)
    }
}
```

## 1.4 模糊查询

**like** 

- LIKE '%值%' 
- 例: `like("name", "王") ---> name like '%王%' `

**notLike** 

- NOT LIKE '%值%'
- 例: `notLike("name", "王") ---> name not like '%王%' `

**likeLeft** 

- LIKE '%值' 

- 例: `likeLeft("name", "王") ---> name like '%王' `

**likeRight** 

- LIKE '值%' 

- 例: `likeRight("name", "王") ---> name like '王%'`

测试·：

```java
package com.lsl.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
public class TestLike {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testWrapper() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();


        wrapper.like("name", "赵");
//        SELECT id,user_name,name,age,email FROM tb_user WHERE name LIKE ?
        List<User> users = this.userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
// User(id=4, userName=zhaoliu, password=null, name=赵六, age=21, email=test4@itcast.cn, address=null)
    }

}
```



##  1.5 排序

**orderBy** 

- 排序：ORDER BY 字段, ...
- 例: `orderBy(true, true, "id", "name") ---> order by id ASC,name ASC`

**orderByAsc** 

- 排序：ORDER BY 字段, ... ASC 
- 例: `orderByAsc("id", "name") ---> order by id ASC,name ASC`

**orderByDesc** 

- 排序：ORDER BY 字段, ... DESC 

- 例: `orderByDesc("id", "name") ---> order by id DESC,name DESC`

**测试**

```java
package com.lsl.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/29 0:37
 */
@SpringBootTest
@Slf4j
public class TestOrderBy {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testWrapper() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("age");
//执行的sql语句： SELECT id,user_name,name,age,email FROM tb_user ORDER BY age DESC 
        
        List<User> users = this.userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
        
//  User(id=3, userName=wangwu, password=null, name=王五, age=28, email=test3@itcast.cn, address=null)
//  User(id=5, userName=sunqi, password=null, name=孙七, age=24, email=test5@itcast.cn, address=null)
//  User(id=4, userName=zhaoliu, password=null, name=赵六, age=21, email=test4@itcast.cn, address=null)
//  User(id=2, userName=lisi, password=null, name=李四, age=20, email=test2@itcast.cn, address=null)
//  User(id=1, userName=zhangsan, password=null, name=张三, age=18, email=test1@itcast.cn, address=null)    
     
    }
}
```

##  1.6 逻辑查询

**or** 

- 拼接 OR 

- 主动调用 or 表示紧接着下一个方法不是用 and 连接!(不调用 or 则默认为使用 and 连接)

**and** 

- AND 嵌套 

- 例: and(i -> i.eq("name", "李白").ne("status", "活着")) ---> and (name = '李白' and status <> '活着')

测试用例：

```java
package com.lsl.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
public class TestLogical {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testWrapper() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name","李四").or().eq("age", 24);
//  SELECT id,user_name,password,name,age,email FROM tb_user WHERE name = ? OR age = ?


        List<User> users = this.userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
// User(id=2, userName=lisi, password=null, name=李四, age=20, email=test2@itcast.cn, address=null)
// User(id=5, userName=sunqi, password=null, name=孙七, age=24, email=test5@itcast.cn, address=null)

    }

}
```

## 1.6 select（查询部分字段）

在MP查询中，默认查询所有的字段，如果有需要也可以通过select方法进行指定字段。

```java
package com.lsl.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/29 0:55
 */
@SpringBootTest
@Slf4j
public class TestSelect {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testWrapper() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "李四")
                .or()
                .eq("age", 24)
                .select("id", "name", "age");
        
//SELECT id,name,age FROM tb_user WHERE name = ? OR age = ?
        List<User> users = this.userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }
}

```

