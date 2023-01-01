#  1.在WEB中应用MyBatis(使用MVC架构模式)

## 1.目标

目标:

- 掌握mybatis在web应用中怎么用
- mybatis三大对象的作用域和生命周期 
- ThreadLocal原理及使用
- 巩固MVC架构模式
- 为学习MyBatis的接口代理机制做准备

实现功能:

- 银行账户转账

使用技术:

- HTML+ Servlet+ MyBatis

WEB应用的名称:

- bank

需求描述

- 实现银行转账的功能

## 2. 数据库表的设计和准备数据

```sql
-- ----------------------------
-- Table structure for t_act
-- ----------------------------
DROP TABLE IF EXISTS `t_act`;
CREATE TABLE `t_act` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `actno` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `balance` DECIMAL(8,2) DEFAULT NULL COMMENT '余额',
  PRIMARY KEY (`id`)
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;
```

插入初始数据：

![image-20230101233053681](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202301012330628.png)

## 3.实现步骤

### 步骤一：引入依赖

```xml
   <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <artifactId>logback-classic</artifactId>
                    <groupId>ch.qos.logback</groupId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!--mybatis核⼼依赖-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.10</version>
        </dependency>

        <!--mysql驱动依赖-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.30</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

### 步骤二：引入配置文件

jdbc.properties

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mp
jdbc.username=root
jdbc.password=W135246W
```

mybatis/mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>
    <properties resource="jdbc.properties"/>
    <environments default="powernodeDB">
        <environment id="powernodeDB">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="mapper/AccountMapper.xml"/>
    </mappers>
</configuration>
```

mapper/AccountMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!--sqlMapper.xml文件的编写者，提供者是谁？使用你mybatis框架的java程序员负责提供的。-->
<!--要想使用这种机制：namespace必须是dao接口的全限定名称。-->
<mapper namespace="com.lsl.code.dao.AccountDao">

    <!--要想使用这种机制：id必须是dao接口的方法名。-->
    <select id="selectByActno" resultType="com.lsl.code.pojo.Account">
        select *
        from t_act
        where actno = #{actno}
    </select>

    <update id="updateByActno">
        update t_act
        set balance = #{balance}
        where actno = #{actno}
    </update>
</mapper>
```

log4j.properties

```properties
log4j.rootLogger=DEBUG,A1
log4j.appender.A1=org.apache.log4j.ConsoleAppender
log4j.appender.A1.layout=org.apache.log4j.PatternLayout
log4j.appender.A1.layout.ConversionPattern=[%t] [%c]-[%p] %m%n
```

### 步骤三：前端页面

static/index.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>银⾏账户转账</title>
</head>
<body>
<!--/bank是应⽤的根，部署web应⽤到tomcat的时候⼀定要注意这个名字-->
<form action="/bank/transfer" method="post">
    转出账户：<input type="text" name="fromActno"/><br>
    转⼊账户：<input type="text" name="toActno"/><br>
    转账⾦额：<input type="text" name="money"/><br>
    <input type="submit" value="转账"/>
</form>
</body>
</html>
```

static/success.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>转账报告</title>
</head>
<body>
<h1>转账成功！</h1>
</body>
</html>
```

static/error1.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>转账报告</title>
</head>
<body>
<h1>余额不足！！！</h1>
</body>
</html>
```

static/error2.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>转账报告</title>
</head>
<body>
<h1>转账失败，未知原因！！！</h1>
</body>
</html>
```

### 步骤四：定义pojo类：Account

```java
package com.lsl.code.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2023/1/1 20:06
 */
@Data
@ToString
public class Account {
    private Long id;
    private String actno;
    private Double balance;
    public Account(){}
    public Account(Long id, String actno, Double balance) {
        this.id = id;
        this.actno = actno;
        this.balance = balance;
    }

}
```

### 步骤五：mybatis工具类

```java
package com.lsl.code.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

/**
 * MyBatis工具类
 */
public class SqlSessionUtil {

    // 工具类的构造方法一般都是私有化的。
    // 工具类中所有的方法都是静态的，直接采用类名即可调用。不需要new对象。
    // 为了防止new对象，构造方法私有化。
    private SqlSessionUtil() {
    }

    private static SqlSessionFactory sqlSessionFactory;

    // 类加载时执行
    // SqlSessionUtil工具类在进行第一次加载的时候，解析mybatis-config.xml文件。创建SqlSessionFactory对象。
    static {
        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis/mybatis-config.xml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取会话对象。
     *
     * @return 会话对象
     */
    public static SqlSession openSession() {
        return sqlSessionFactory.openSession();
    }

}
```

### 步骤六：编写AccountDao接⼝，以及AccountDaoImpl实现类

分析dao中⾄少要提供⼏个⽅法，才能完成转账： 

- 转账前需要查询余额是否充⾜：selectByActno
- 转账时要更新账户：update

 **AccountDao**

```java
package com.lsl.code.dao;


import com.lsl.code.pojo.Account;

/**
 * 账户的DAO对象。负责t_act表中数据的CRUD.
 * 强调一下：DAO对象中的任何一个方法和业务不挂钩。没有任何业务逻辑在里面。
 * DAO中的方法就是做CRUD的。所以方法名大部分是：insertXXX deleteXXX updateXXX selectXXX
 */
public interface AccountDao {
    /**
     * 根据账号获取账户信息
     * @param actno 账号
     * @return 账户信息
     */
    Account selectByActno(String actno);
    /**
     * 更新账户信息
     * @param act 账户信息
     * @return 1表示更新成功，其他值表示失败
     */
    int update(Account act);
}
```

**AccountDaoImpl**

```java
package com.lsl.code.dao.impl;

import com.lsl.code.dao.AccountDao;
import com.lsl.code.pojo.Account;
import com.lsl.code.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

public class AccountDaoImpl implements AccountDao {

    @Override
    public Account selectByActno(String actno) {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        Account act = (Account) sqlSession.selectOne("selectByActno", actno
        );
        sqlSession.close();
        return act;
    }

    @Override
    public int update(Account act) {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        int count = sqlSession.update("update", act);
        sqlSession.commit();
        sqlSession.close();
        return count;
    }
}
```

### 步骤七：AccountDaoImpl中编写了mybatis代码，需要编写SQL映射⽂件了

mapper/AccountMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="account">
    <select id="selectByActno" resultType="com.lsl.code.pojo.Account">
        select *
        from t_act
        where actno = #{actno}
    </select>
    <update id="update">
        update t_act
        set balance = #{balance}
        where actno = #{actno}
    </update>
</mapper>
```

### 步骤八：编写AccountService接⼝以及AccountServiceImpl

**AccountService接⼝**

```java
package com.lsl.code.service;

import com.lsl.code.exception.MoneyNotEnoughException;
import com.lsl.code.exception.TransferException;

/**
 * 注意：业务类当中的业务方法的名字在起名的时候，最好见名知意，能够体现出具体的业务是做什么的。
 * 账户业务类
 *
 * @author 22418
 * @version 1.0
 * @description: 账户业务类
 * @date 2023/1/1 20:20
 */
public interface AccountService {
    /**
     * 账户转账业务。
     *
     * @param fromActno 转出账号
     * @param toActno   转入账号
     * @param money     转账金额
     */
    void transfer(String fromActno, String toActno, double money) throws MoneyNotEnoughException, TransferException;
}
```

**AccountServiceImpl**

```java
package com.lsl.code.service.impl;

import com.lsl.code.dao.AccountDao;
import com.lsl.code.dao.impl.AccountDaoImpl;
import com.lsl.code.exception.MoneyNotEnoughException;
import com.lsl.code.exception.TransferException;
import com.lsl.code.pojo.Account;
import com.lsl.code.service.AccountService;

public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao = new AccountDaoImpl();

    @Override
    public void transfer(String fromActno, String toActno, double money) throws MoneyNotEnoughException, TransferException {
        // 查询转出账户的余额
        Account fromAct = accountDao.selectByActno(fromActno);
        if (fromAct.getBalance() < money) {
            throw new MoneyNotEnoughException("对不起，您的余额不⾜。");
        }
        try {
            // 程序如果执⾏到这⾥说明余额充⾜
            // 修改账户余额
            Account toAct = accountDao.selectByActno(toActno);
            fromAct.setBalance(fromAct.getBalance() - money);
            toAct.setBalance(toAct.getBalance() + money);
            // 更新数据库
            accountDao.update(fromAct);
            accountDao.update(toAct);
        } catch (Exception e) {
            throw new TransferException("转账失败，未知原因！");
        }
    }
}
```

### 步骤九：编写AccountController

**AccountController**

```java
package com.lsl.code.controller;

import com.lsl.code.exception.MoneyNotEnoughException;
import com.lsl.code.exception.TransferException;
import com.lsl.code.service.AccountService;
import com.lsl.code.service.impl.AccountServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping("/bank")
public class AccountController {

    // 为了让这个对象在其他方法中也可以用。声明为实例变量
    private AccountService accountService = new AccountServiceImpl();

    @PostMapping("/transfer")
    public void transfer(@RequestParam("fromActno") String fromActno,
                         @RequestParam("toActno") String toActno,
                         @RequestParam("money") double money,
                         HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // 调用service的转账方法完成转账。（调业务层）
            accountService.transfer(fromActno, toActno, money);
            // 程序能够走到这里，表示转账一定成功了
            // 调用View完成展示结果
            response.sendRedirect(request.getContextPath() + "/success.html");
        } catch (MoneyNotEnoughException e) {
            response.sendRedirect(request.getContextPath() + "/error1.html");
        } catch (TransferException e) {
            response.sendRedirect(request.getContextPath() + "/error2.html");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/error2.html");
        }

    }

}
```

### 步骤十：测试

启动服务器，打开浏览器，输⼊地址：`http://localhost:8080`

![image-20230102002057694](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202301020021002.png)





![image-20230102002202006](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202301020022292.png)

# 2. MyBatis对象作用域以及事务问题

MyBatis核心对象的作用域
sqlSessionFactoryBuilder
这个类可以被实例化、使用和丢弃，一旦创建了SqlSessionFactory，就不再需要它了。因此SqlSessionFactoryBuilder实例的最佳作用域是方法作用域（也就是局部方法变量)。你可以重用
