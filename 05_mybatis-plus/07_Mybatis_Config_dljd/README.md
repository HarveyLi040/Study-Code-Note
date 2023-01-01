# MyBatis核⼼配置⽂件详解

# 1.核心配置文件之 mybatis-config.xml

##  1.1 配置文件核心属性简介

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
<environments default="development">
    <environment id="development">
        <transactionManager type="JDBC"/>
        <dataSource type="POOLED">
            <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="url" value="jdbc:mysql://localhost:3306/powernode"/>
            <property name="username" value="root"/>
            <property name="password" value="root"/>
        </dataSource>
    </environment>
</environments>
<mappers>
    <mapper resource="CarMapper.xml"/>
    <mapper resource="CarMapper2.xml"/>
</mappers>
</configuration>
```

- configuration：根标签，表示配置信息
- environments：环境（多个），以“s”结尾表示复数，也就是说mybatis的环境可以配置多个数据源
  - default属性：表示默认使⽤的是哪个环境，default后⾯填写的是environment的id。default的 值只需要和environment的id值⼀致即可

- environment：具体的环境配置（主要包括：事务管理器的配置 + 数据源的配置） 
  - id：给当前环境⼀个唯⼀标识，该标识⽤在environments的default后⾯，⽤来指定默认环境的 选择
- transactionManager：配置事务管理器
  - type属性：指定事务管理器具体使⽤什么⽅式，可选值包括两个
  - JDBC：使⽤JDBC原⽣的事务管理机制。底层原理：事务开启 conn.setAutoCommit(false); ...处理业务...事务提交conn.commit(); 
  - MANAGED：交给其它容器来管理事务，⽐如WebLogic、JBOSS等。如果没有管理事务的 容器，则没有事务。没有事务的含义：只要执⾏⼀条DML语句，则提交⼀次

- dataSource：指定数据源 

  - type属性：⽤来指定具体使⽤的数据库连接池的策略，可选值包括三个 
  - UNPOOLED：采⽤传统的获取连接的⽅式，虽然也实现Javax.sql.DataSource接⼝，但是 并没有使⽤池的思想
    - property可以是： 
      - driver 这是 JDBC 驱动的 Java 类全限定名
      -  url 这是数据库的 JDBC URL 地址
      - username 登录数据库的⽤户名
      - password 登录数据库的密码
      - defaultTransactionIsolationLevel 默认的连接事务隔离级别
      - defaultNetworkTimeout 等待数据库操作完成的默认⽹络超时时间（单位：毫秒） 

  - POOLED：采⽤传统的javax.sql.DataSource规范中的连接池，mybatis中有针对规范的实现
    - property可以是（除了包含UNPOOLED中之外）： 
      - poolMaximumActiveConnections 在任意时间可存在的活动（正在使⽤）连接数 量，默认值：10 
      - poolMaximumIdleConnections 任意时间可能存在的空闲连接数
      - 其它.... 
    - JNDI：采⽤服务器提供的JNDI技术实现，来获取DataSource对象，不同的服务器所能拿到 DataSource是不⼀样。如果不是web或者maven的war⼯程，JNDI是不能使⽤的
      - property可以是（最多只包含以下两个属性）： 
        - initial_context 这个属性⽤来在 InitialContext 中寻找上下⽂（即initialContext.lookup(initial_context)）这是个可选属性，如果忽略，那么将会直接从 InitialContext 中寻找 data_source 属性
        - data_source 这是引⽤数据源实例位置的上下⽂路径。提供了 initial_context 配置时会 在其返回的上下⽂中进⾏查找，没有提供时则直接在 InitialContext 中查找

-  mappers：在mappers标签中可以配置多个sql映射⽂件的路径

-  mapper：配置某个sql映射⽂件的路径

  - resource属性：使⽤相对于类路径的资源引⽤⽅式 
  - url属性：使⽤完全限定资源定位符（URL）⽅式

##   1.2  environment

environment/mybatis-config.xml 配置：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>
    <!--默认使⽤开发环境-->
    <!--<environments default="production">-->
    <!--默认使⽤⽣产环境-->
    <environments default="production">
        <!--开发环境-->
        <environment id="dev">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mp"/>
                <property name="username" value="root"/>
                <property name="password" value="W135246W"/>
            </dataSource>
        </environment>
        <!--⽣产环境-->
        <environment id="production">
            <transactionManager type="JDBC" />
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mp"/>
                <property name="username" value="root"/>
                <property name="password" value="W135246W"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="CarMapper.xml"/>
    </mappers>
</configuration>
```

Java代码：

```java
package com.lsl.code;

import com.lsl.code.pojo.Car;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestEnvironment {
    @Test
    public void testEnvironment() throws Exception{
        // 准备数据
        Car car = new Car();
        car.setCarNum("133");
        car.setBrand("丰⽥霸道");
        car.setGuidePrice(50.3);
        car.setProduceTime("2020-01-10");
        car.setCarType("燃油⻋");
        // ⼀个数据库对应⼀个SqlSessionFactory对象
        // 两个数据库对应两个SqlSessionFactory对象，以此类推
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        // 使⽤默认数据库
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("environment/mybatis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        int count = sqlSession.insert("car.insertCar", car);
        System.out.println("插⼊了⼏条记录：" + count);
        // 使⽤指定数据库
        SqlSessionFactory sqlSessionFactory1 = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("environment/mybatis-config.xml"), "dev");
        SqlSession sqlSession1 = sqlSessionFactory1.openSession(true);
        int count1 = sqlSession1.insert("car.insertCar", car);
        System.out.println("插⼊了⼏条记录：" + count1);
    }

}
```

##  1.3  transactionManager

###  1.3.1 演示

transactionManager/mybatis-config.xml配置：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="dev">
        <environment id="dev">
            <transactionManager type="MANAGED"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mp"/>
                <property name="username" value="root"/>
                <property name="password" value="W135246W"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="CarMapper.xml"/>
    </mappers>
</configuration>
```

java测试代码：

```java
package com.lsl.code;

import com.lsl.code.pojo.Car;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2023/1/1 13:40
 */
@SpringBootTest
public class TestTransactionManager {
    @Test
    public void testTransactionManager() throws Exception {
        // 准备数据
        Car car = new Car();
        car.setCarNum("133");
        car.setBrand("丰⽥霸道");
        car.setGuidePrice(50.3);
        car.setProduceTime("2020-01-10");
        car.setCarType("燃油⻋");
        // 获取SqlSessionFactory对象
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("transactionManager/mybatis-config.xml"));
        // 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 执⾏SQL
        int count = sqlSession.insert("insertCar", car);
        System.out.println("插⼊了⼏条记录：" + count);
    }

}
```

###  1.3.2 总结

**当事务管理器是：JDBC** 

- 采⽤JDBC的原⽣事务机制： 

  - 开启事务：conn.setAutoCommit(false); 

  - 处理业务...... 提交事务：conn.commit(); 

**当事务管理器是：MANAGED** 

- 交给容器去管理事务，但⽬前使⽤的是本地程序，没有容器的⽀持，当mybatis找不到容器的⽀持时：没有事务。也就是说只要执⾏⼀条DML语句，则提交⼀次

```sh
transactionManager标签：
    1.作用：配置事务管理器。指定mybatis具体使用什么方式去管理事务
    2.type属性有两个值：
        第一个：JDBC: 使用原生的JDBC代码来管理事务
            conn.setAutoCommit(false);
            ....
            conn.commit();
        第二个：MANAGED：mybatis不再负责事务的管理，将事务管理交给其它的JEE(JavaEE)容器来管理。例如：spring
    3. 大小写无所谓。不缺分大小写。但是不能写其他值。只能是二选一：
        jdbc、managed
    4. 在mybatis中提供了一个事务管理器接口：Transaction
        该接口下有两个实现类：
            JdbcTransaction
            ManagedTransaction
        如果type="JDBC"，那么底层会实例化JdbcTransaction对象
        如果type="MANAGED"，那么底层会实例化ManagedTransaction
```

##  1.4  dataSource

###  1.4.1 type="UNPOOLED"

dataSource/mybatis-config.xml 配置：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="dev">
        <environment id="dev">
            <transactionManager type="JDBC"/>
            <dataSource type="UNPOOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mp"/>
                <property name="username" value="root"/>
                <property name="password" value="W135246W"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="CarMapper.xml"/>
    </mappers>
</configuration>
```

Java代码：

```java
package com.lsl.code;

import com.lsl.code.pojo.Car;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class TestdataSourceUNPOOLED {

    @Test
    public void testDataSource() throws Exception{
        // 准备数据
        Car car = new Car();
        car.setCarNum("133");
        car.setBrand("丰⽥霸道");
        car.setGuidePrice(50.3);
        car.setProduceTime("2020-01-10");
        car.setCarType("燃油⻋");
        // 获取SqlSessionFactory对象
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("dataSource/mybatis-config.xml"));
        // 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        // 执⾏SQL
        int count = sqlSession.insert("insertCar", car);
        System.out.println("插⼊了⼏条记录：" + count);
        // 关闭会话
        sqlSession.close();
    }
}
```

当type是UNPOOLED，控制台输出：

![image-20230101140856410](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202301011408809.png)

###  1.4.2 type="POOLED"

修改配置⽂件dataSource/mybatis-config.xml 中的配置：

```xml
<dataSource type="POOLED">
```

Java测试程序不需要修改，直接执⾏，看控制台输出：

![image-20230101141019790](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202301011410127.png)

通过测试得出：UNPOOLED不会使⽤连接池，每⼀次都会新建JDBC连接对象。POOLED会使⽤数据库连接池。【这个连接池是mybatis⾃⼰实现的。】

###  1.4.3 type="JNDI"

**JNDI的⽅式：**

修改配置⽂件dataSource/mybatis-config.xml 中的配置：

```xml
<dataSource type="JNDI">
```

JNDI的⽅式：表示对接JNDI服务器中的连接池。这种⽅式给了我们可以使⽤第三⽅连接池的接⼝。如果 想使⽤dbcp、c3p0、druid（德鲁伊）等，需要使⽤这种⽅式。 

###  1.4.4 type="POOLED"时的属性

再重点说⼀下type="POOLED"的时候，它的属性有哪些？

配置⽂件dataSourcePOOLED/mybatis-config.xml中的配置：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="dev">
        <environment id="dev">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mp"/>
                <property name="username" value="root"/>
                <property name="password" value="W135246W"/>
                <!--最⼤连接数-->
                <property name="poolMaximumActiveConnections" value="3"/>
                <!--最多空闲数量-->
                <property name="poolMaximumIdleConnections" value="1"/>
                <!--强⾏回归池的时间-->
                <property name="poolMaximumCheckoutTime" value="20000"/>
                <!--这是⼀个底层设置，如果获取连接花费了相当⻓的时间，连接池会打印状
               态⽇志并重新尝试获取⼀个连接（避免在误配置的情况下⼀直失败且不打印⽇志），默认值：20000
               毫秒（即 20 秒）。-->
                <property name="poolTimeToWait" value="20000"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="CarMapper.xml"/>
    </mappers>
</configuration>
```

poolMaximumActiveConnections:最大的活动的连接数量。默认值10

poolMaximumldleConnections:最大的空闲连接数量。默认值5

poolMaximumCheckoutTime:强行回归池的时间。默认值20秒

poolTimeToWait:当无法获取到空闲连接时，每隔20秒打印一次日志，避免因代码配置有误，导致傻等。(时长是可以配置的)

当然，还有其他属性。对于连接池来说，以上几个属性比较重要

最大的活动的连接数量就是连接池连接数量的上限。默认值10，如果有10个请求正在使用这10个连接，第11个请求只能等待空闲连接

最⼤的空闲连接数量。默认值5，如何已经有了5个空闲连接，当第6个连接要空闲下来的时候，连接池会 选择关闭该连接对象。来减少数据库的开销

 需要根据系统的并发情况，来合理调整连接池最⼤连接数以及最多空闲数量。充分发挥数据库连接池的 性能。【可以根据实际情况进⾏测试，然后调整⼀个合理的数量。】 

下图是默认配置：

![image-20230101142217880](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202301011422598.png)

在以上配置的基础之上，可以编写java程序测试：

```java
package com.lsl.code;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestdataSourcePOOLED {
    @Test
    public void testPool() throws Exception{
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("dataSourcePOOLED/mybatis-config.xml"));
        for (int i = 125; i < 130; i++) {
            SqlSession sqlSession = sqlSessionFactory.openSession();
            Object selectCarByCarNum = sqlSession.selectOne("selectCarByCarNum");
            System.out.println(selectCarByCarNum);
        }
    }
}
```

CarMapper.xml配置：

```xml
    <select id="selectCarByCarNum" resultType="com.lsl.code.pojo.Car">
        select id, car_num carNum, brand, guide_price guidePrice, produce_time produceTime, car_type carType
        from t_car
        where car_num = '100'
    </select>
```

![image-20230101144109386](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202301011441823.png)

###  1.4.5 总结

```sh
dataSource配置：
    1.dataSource被称为数据源
    2.dataSource作用是什么？为程序提供Connection对象。（但凡是给程序提供Connection对象的，都叫做数据源）
    3.数据源实际上是一套规范。JDK中有这套规范：javax.sql.DataSource（这个数据源的规范，这套接口实际上是JDK规定的）
    4.我们自己也可以编写数据源组件，只要实现javax.sql.DataSource接口就行了。实现接口当中所有的方法。这样就有了自己的数据源
    比如你可以写一个属于自己的数据库连接池（数据库连接池是提供连接对象的，所以数据库连接池就是一个数据源）
    5.常见的数据源组件有哪些呢【常见的数据库连接池有哪些呢】？
        阿里巴巴的德鲁伊连接池：druid
        c3p0
        dbcp
        ....
    6. type属性用来指定数据源的类型，就是指定具体使用什么方式来获取Connection对象：
        type属性有三个值：必须是三选一
        type="[UNPOOLED|POOLED|JNDI]"
        UNPOOLED：不使用数据库连接池技术。每一次请求过来之后，都是创建新的Connection对象
        POOLED：使用mybatis自己实现的数据库连接池
        JNDI：集成其它第三方的数据库连接池

        JNDI是一套规范。谁实现了这套规范呢？大部分的web容器都实现了JNDI规范：
            例如：Tomcat、Jetty、WebLogic、WebSphere，这些服务器(容器)都实现了JNDI规范
        JNDI是：java命名目录接口。Tomcat服务器实现了这个规范
```

# 1.5 properties

properties/mybatis-config.xml配置文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>

    <!--java.util.Properties类。是一个Map集合。key和value都是String类型-->
    <!--在properties标签中可以配置很多属性-->
    <!--<properties>-->
    <!--这是其中的一个属性-->
    <!--<property name="属性名" value="属性值"/>-->
    <!--<property name="jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
    <property name="jdbc.url" value="jdbc:mysql://localhost:3306/powernode"/>
    <property name="jdbc.username" value="root"/>
    <property name="jdbc.password" value="root"/>-->
    <!--</properties>-->

    <!--resource,一定是从类路径下开始查找资源-->
    <properties resource="jdbc.properties"/>

    <!--从绝对路径当中加载资源。绝对路径怎么写？file:///路径-->
    <!--<properties url="file:///d:/jdbc.properties" />-->

    <environments default="powernodeDB">
        <environment id="powernodeDB">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
                <property name="poolMaximumActiveConnections" value="10"/>
                <!--每隔2秒打印日志，并且尝试获取连接对象-->
                <property name="poolTimeToWait" value="2000"/>
                <!--强行让某个连接空闲，超时时间的设置-->
                <property name="poolMaximumCheckoutTime" value="10000"/>
                <!--最多的空闲数量-->
                <property name="poolMaximumIdleConnections" value="5"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="CarMapper.xml"/>
    </mappers>
</configuration>
```

jdbc.properties配置：

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mp
jdbc.username=root
jdbc.password=W135246W
```

测试：

```java
package com.lsl.code;

import com.lsl.code.pojo.Car;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2023/1/1 14:58
 */
@SpringBootTest
public class TestProperties {
    @Test
    public void TestProperties() throws Exception {
        // 准备数据
        Car car = new Car();
        car.setCarNum("133");
        car.setBrand("丰⽥霸道");
        car.setGuidePrice(50.3);
        car.setProduceTime("2020-01-10");
        car.setCarType("燃油⻋");
        // 获取SqlSessionFactory对象
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("properties/mybatis-config.xml"));
        // 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 执⾏SQL
        int count = sqlSession.insert("insertCar", car);
        System.out.println("插⼊了⼏条记录：" + count);
    }

}
```

