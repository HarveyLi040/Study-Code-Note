# 1.MyBatis概述

## 1.1框架

- 在文献中看到的framework被翻译为框架
- Java常用框架:
  -  SSM三大框架:Spring + SpringMVC + MyBatis
  - SpringBoot
  - SpringCloud
- 框架其实就是对通用代码的封装，提前写好了一堆接口和类，我们可以在做项目的时候直接引入这些接口和类(引入框架)，基于这些现有的接口和类进行开发，可以大大提高开发效率。
- 框架一般都以jar包的形式存在(jar包中有class文件以及各种配置文件等)
- SSM三大框架的学习顺序:
  - 方式一: MyBatis、Spring、SpringMVC(建议)
  - 方式二: Spring、MyBatis、SpringMVC

##  1.2 三层架构

![image-20221229230541559](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212292309455.png)

- 表现层(UI)∶直接跟前端打交互（一是接收前端ajax请求，二是返回json数据给前端)
- 业务逻辑层(BLL)︰一是处理表现层转发过来的前端请求（也就是具体业务)，二是将从持久层获取的数据返回到表现层
- 数据访问层(DAL)︰直接操作数据库完成CRUD，并将获得的数据返回到上一层（也就是业务逻辑层)。
- Java持久层框架∶
  - MyBatis
  - Hibernate (实现了JPA规范)o jooQ
  - Guzz
  - Spring Data(实现了JPA规范)
  - ActiveJDBC

##  1.3 JDBC的不足

JDBC不足︰

- SQL语句写死在Java程序中，不灵活。改SQL的话就要改Java代码。违背开闭原则OCP。
- 给?传值是繁琐的。能不能自动化?? ?
- 将结果集封装成Java对象是繁琐的。能不能自动化?? ?

 

示例代码1：

```java
// ......
// sql语句写死在java程序中
String sql = "insert into t_user(id,idCard,username,password,birth,gender,
email,city,street,zipcode,phone,grade) values(?,?,?,?,?,?,?,?,?,?,?,?)";
PreparedStatement ps = conn.prepareStatement(sql);
// 繁琐的赋值：思考⼀下，这种有规律的代码能不能通过反射机制来做⾃动化。
ps.setString(1, "1");
ps.setString(2, "123456789");
ps.setString(3, "zhangsan");
ps.setString(4, "123456");
ps.setString(5, "1980-10-11");
ps.setString(6, "男");
ps.setString(7, "zhangsan@126.com");
ps.setString(8, "北京");
ps.setString(9, "⼤兴区凉⽔河⼆街");
ps.setString(10, "1000000");
ps.setString(11, "16398574152");
ps.setString(12, "A");
// 执⾏SQL
int count = ps.executeUpdate();
// ......
```

 示例代码2：

```java
// ......
// sql语句写死在java程序中
String sql = "select id,idCard,username,password,birth,gender,email,city,s
treet,zipcode,phone,grade from t_user";
PreparedStatement ps = conn.prepareStatement(sql);
ResultSet rs = ps.executeQuery();
List<User> userList = new ArrayList<>();
// 思考以下循环中的所有代码是否可以使⽤反射进⾏⾃动化封装。
while(rs.next()){
 // 获取数据
 String id = rs.getString("id");
 String idCard = rs.getString("idCard");
 String username = rs.getString("username");
 String password = rs.getString("password");
 String birth = rs.getString("birth");
 String gender = rs.getString("gender");
 String email = rs.getString("email");
 String city = rs.getString("city");
 String street = rs.getString("street");
 String zipcode = rs.getString("zipcode");
 String phone = rs.getString("phone");
 String grade = rs.getString("grade");
 // 创建对象
 User user = new User();
 // 给对象属性赋值
 user.setId(id);
 user.setIdCard(idCard);
 user.setUsername(username);
 user.setPassword(password);
 user.setBirth(birth);
 user.setGender(gender);
 user.setEmail(email);
 user.setCity(city);
 user.setStreet(street);
 user.setZipcode(zipcode);
 user.setPhone(phone);
 user.setGrade(grade);
 // 添加到集合
 userList.add(user);
}
// ......
```

## 1.4了解MyBatis

- MyBatis本质上就是对JDBC的封装
- MyBatis在三层架构中负责持久层的，属于持久层框架
- MyBatis的发展历程:【引用百度百科】
  - MyBatis本是apache的一个开源项目iBatis，2010年这个项目由apache software foundation迁移到了google code，并且改名为MyBatis。2013年11月迁移到Github
  - iBATIS一词来源于“internet"和“abatis”的组合，是一个基于Java的持久层框架。iBATIS提供的持久层框架包括SQL Maps和Data Access Objects (DAOs)
- 打开mybatis代码可以看到它的包结构中包含: ibatis

![image-20221229231858891](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212292319364.png)



**ORM:对象关系映射**

![001-ORM思想-对象关系映射](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212292335005.png)

- O (Object) : Java虚拟机中的Java对象

- R (Relational):关系型数据库

- M (Mapping)︰将Java虚拟机中的Java对象映射到数据库表中一行记录，或是将数据库表中一行记录映射成Java虚拟机中的一个Java对象

- ORM图示

  

![image-20221229232121975](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212292321458.png)

![image-20221229232144909](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212292321217.png)

- MyBatis属于半自动化ORM框架
- Hibernate属于全自动化的ORM框架



**MyBatis框架特点:**

- 支持定制化SQL、存储过程、基本映射以及高级映射

- 避免了几乎所有的JDBC代码中手动设置参数以及获取结果集
- 支持XML开发，也支持注解式开发。【为了保证sql语句的灵活，所以mybatis大部分是采用XML方式开发。】
- 将接口和Java的 POJOs(Plain Ordinary Java Object，简单普通的Java对象)映射成数据库中的记录
- 体积小,好学:两个jar包，两个XML配置文件
- 完全做到sql解耦合。
- 提供了基本映射标签。。提供了高级映射标签
- 提供了XML标签，支持动态SQL的编写

##  1.5 MyBatis源码、jar包、文档下载

从github上下载，地址:https://github.com/mybatis/mybatis-3

![image-20221229233050110](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212292330658.png)



![image-20221229233123171](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212292331760.png)



将框架以及框架的源码都下载下来，下载框架后解压，打开mybatis目录

![image-20221229233159938](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212292332705.png)

- 通过以上解压可以看到，框架一般都是以jar包的形式存在。我们的mybatis课程使用maven，所以这个jar我们不需要。
- 官方手册需要

# 2.开发第一个MyBatis程序

##  2.1 开发步骤

**1.resources目录：**

- 放在这个目录当中的，一般都是资源文件，配置文件
- 直接放到resources目录下的资源，等同于放到了类的根路径下

**2.开发步骤**

第一步：建立数据库及数据

```sql
-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `t_car`;
CREATE TABLE `t_car` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `car_num` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '汽车编号',
  `brand` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `guide_price` DECIMAL DEFAULT NULL COMMENT '厂家指导价',
  `produce_time` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产时间',
  `car_type` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '汽车类型',
  PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 109 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

SELECT * FROM `t_car`;
```

第二步：引入依赖

```xml
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
```

第三步：在resources根目录下新建mybatis-config.xml配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>

    <!--开启mybatis对标准日志的实现。-->
    <!--<settings>-->
    <!--<setting name="logImpl" value="STDOUT_LOGGING"/>-->
    <!--<setting name="logImpl" value="SLF4J"/>-->
    <!--</settings>-->
    <environments default="development">
        <environment id="development">
            <!--<transactionManager type="MANAGED"/>-->
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mp"/>
                <property name="username" value="root"/>
                <property name="password" value="135246"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <!--执行XxxMapper.xml文件的路径-->
        <!--resource属性自动会从类的根路径下开始查找资源。-->
        <mapper resource="CarMapper.xml"/>
        <!--resource属性：从类路径当中加载资源。-->
        <!--<mapper resource="com/CarMapper2.xml"/>-->

        <!--url属性：从绝对路径当中加载资源。-->
        <!--语法格式：file:///绝对路径-->
        <!--不建议：因为移植性差。-->
        <!--<mapper url="file:///d:/CarMapper.xml"/>--
    </mappers>
</configuration>
```

第四步： 在resources根⽬录下新建CarMapper.xml配置⽂件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="fdsafdsa">

    <!--insert语句，id是这条SQL语句的唯一标识。这个id就代表了这条SQL语句。-->
    <insert id="insertCar">
        insert into t_car(id, car_num, brand, guide_price, produce_time, car_type)
        values (null, '1003', '丰田霸道', 30.0, '2000-10-11', '燃油车');
    </insert>

</mapper>
```



> 注意1：sql语句最后结尾可以不写“;” 
>
> 注意2：CarMapper.xml⽂件的名字不是固定的。可以使⽤其它名字
>
> 注意3：CarMapper.xml⽂件的位置也是随意的。这⾥选择放在resources根下，相当于放到了类的根路 径下
>
> 注意4：将CarMapper.xml⽂件路径配置到mybatis-config.xml：
>
> <mapper resource="CarMapper.xml"/>



第五步：编写MyBatisIntroductionTest代码

```java
package com.lsl.code;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/30 1:32
 */
@SpringBootTest
public class MyBatisIntroductionTest {

    @Test
    public void TestMyBatisIntroduction() {

        // 1. 创建SqlSessionFactoryBuilder对象
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

        // 2. 创建SqlSessionFactory对象
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("mybatis-config.xml");
        //一般情况下都是一个数据库对应一个SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is); // 如果使用的事务管理器是JDBC的话，底层实际上会执行：conn.setAutoCommit(false);


        // 3. 创建SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 这种方式实际上是不建议的，因为没有开启事务。
        //SqlSession sqlSession = sqlSessionFactory.openSession(true);

        // 4. 执⾏sql
        int count = sqlSession.insert("insertCar"); // 这个"insertCar"必须是sql的id
        System.out.println("插⼊⼏条数据：" + count);  // 返回值是影响数据库表当中的记录条数

        // 5. 提交（mybatis默认采⽤的事务管理器是JDBC，默认是不提交的，需要⼿动提交。）
        sqlSession.commit();

        // 6. 关闭资源（只关闭是不会提交的）
        sqlSession.close();

    }
}
```

<font color="#FF0000">注意1：默认采⽤的事务管理器是：JDBC       （ JDBC事务默认是不提交的，需要⼿动提交）</font>

##  2.2 第一个MyBatis程序细节

```sh
1. resources目录：
    放在这个目录当中的，一般都是资源文件，配置文件。
    直接放到resources目录下的资源，等同于放到了类
    的根路径下。

2. 开发步骤
* 第一步：打包方式jar
* 第二步：引入依赖
    - mybatis依赖
    - mysql驱动依赖
* 第三步：编写mybatis核心配置文件：mybatis-config.xml
    注意：
        第一：这个文件名不是必须叫做mybatis-config.xml，可以用其他的名字。只是大家都采用这个名字
        第二：这个文件存放的位置也不是固定的，可以随意，但一般情况下，会放到类的根路径下
* 第四步：编写XxxxMapper.xml文件
        在这个配置文件当中编写SQL语句。
        这个文件名也不是固定的，放的位置也不是固定，我们这里给它起个名字，叫做：CarMapper.xml
        把它暂时放到类的根路径下。
* 第五步：在mybatis-config.xml文件中指定XxxxMapper.xml文件的路径：
        <mapper resource="CarMapper.xml"/>
        注意：resource属性会自动从类的根路径下开始查找资源。

* 第六步：编写MyBatis程序。(使用mybatis的类库，编写mybatis程序，连接数据库，做增删改查就行了。)
        在MyBatis当中，负责执行SQL语句的那个对象叫做什么呢？
            SqlSession
        SqlSession是专门用来执行SQL语句的，是一个Java程序和数据库之间的一次会话。
        要想获取SqlSession对象，需要先获取SqlSessionFactory对象，通过SqlSessionFactory工厂来生产SqlSession对象。
        怎么获取SqlSessionFactory对象呢？
            需要首先获取SqlSessionFactoryBuilder对象。
            通过SqlSessionFactoryBuilder对象的build方法，来获取一个SqlSessionFactory对象。

        mybatis的核心对象包括：
            SqlSessionFactoryBuilder
            SqlSessionFactory
            SqlSession

        SqlSessionFactoryBuilder --> SqlSessionFactory --> SqlSession

3. 从 XML 中构建 SqlSessionFactory
        通过官方的这句话，你能想到什么呢？
            第一：在MyBatis中一定是有一个很重要的对象，这个对象是：SqlSessionFactory对象。
            第二：SqlSessionFactory对象的创建需要XML。
        XML是什么？
            它一定是一个配置文件。

4. mybatis中有两个主要的配置文件：
        其中一个是：mybatis-config.xml，这是核心配置文件，主要配置连接数据库的信息等。（一个）
        另一个是：XxxxMapper.xml，这个文件是专门用来编写SQL语句的配置文件。（一个表一个）
            t_user表，一般会对应一个UserMapper.xml
            t_student表，一般会对应一个StudentMapper.xml

5. 关于第一个程序的小细节
        * mybatis中sql语句的结尾";"可以省略。
        * Resources.getResourceAsStream
            小技巧：以后凡是遇到resource这个单词，大部分情况下，这种加载资源的方式就是从类的根路径下开始加载。（开始查找）
            优点：采用这种方式，从类路径当中加载资源，项目的移植性很强。项目从windows移植到linux，代码不需要修改，因为这个资源文件一直都在类路径当中。
        * InputStream is = new FileInputStream("d:\\mybatis-config.xml");
            采用这种方式也可以。
            缺点：可移植性太差，程序不够健壮。可能会移植到其他的操作系统当中。导致以上路径无效，还需要修改java代码中的路径。这样违背了OCP原则。
        * 已经验证了：
            mybatis核心配置文件的名字，不一定是：mybatis-config.xml。可以是其它名字。
            mybatis核心配置文件存放的路径，也不一定是在类的根路径下。可以放到其它位置。但为了项目的移植性，健壮性，最好将这个配置文件放到类路径下面。
        * InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("mybatis-config.xml");
            ClassLoader.getSystemClassLoader() 获取系统的类加载器。
            系统类加载器有一个方法叫做：getResourceAsStream
            它就是从类路径当中加载资源的。
            通过源代码分析发现：
                InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
                底层的源代码其实就是：
                InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("mybatis-config.xml");
        * CarMapper.xml文件的名字是固定的吗？CarMapper.xml文件的路径是固定的吗？
            都不是固定的。
            <mapper resource="CarMapper.xml"/> resource属性：这种方式是从类路径当中加载资源。
            <mapper url="file:///d:/CarMapper.xml"/> url属性：这种方式是从绝对路径当中加载资源。

6. 关于mybatis的事务管理机制。（深度剖析）
        * 在mybatis-config.xml文件中，可以通过以下的配置进行mybatis的事务管理
            <transactionManager type="JDBC"/>
        * type属性的值包括两个：
            JDBC(jdbc)
            MANAGED(managed)
            type后面的值，只有以上两个值可选，不区分大小写。
        * 在mybatis中提供了两种事务管理机制：
            第一种：JDBC事务管理器
            第二种：MANAGED事务管理器
        * JDBC事务管理器：
            mybatis框架自己管理事务，自己采用原生的JDBC代码去管理事务：
                conn.setAutoCommit(false); 开启事务。
                ....业务处理...
                conn.commit(); 手动提交事务
            使用JDBC事务管理器的话，底层创建的事务管理器对象：JdbcTransaction对象。

            如果你编写的代码是下面的代码：
                SqlSession sqlSession = sqlSessionFactory.openSession(true);
                表示没有开启事务。因为这种方式压根不会执行：conn.setAutoCommit(false);
                在JDBC事务中，没有执行conn.setAutoCommit(false);那么autoCommit就是true。
                如果autoCommit是true，就表示没有开启事务。只要执行任意一条DML语句就提交一次。

        * MANAGED事务管理器：
            mybatis不再负责事务的管理了。事务管理交给其它容器来负责。例如：spring。
            我不管事务了，你来负责吧。

            对于我们当前的单纯的只有mybatis的情况下，如果配置为：MANAGED
            那么事务这块是没人管的。没有人管理事务表示事务压根没有开启。

            没有人管理事务就是没有事务。

        * JDBC中的事务：
            如果你没有在JDBC代码中执行：conn.setAutoCommit(false);的话，默认的autoCommit是true。

        * 重点：
            以后注意了，只要你的autoCommit是true，就表示没有开启事务。
            只有你的autoCommit是false的时候，就表示开启了事务。
```

##  2.3 MyBatis程序改进

采用正规的方式，写一个完整、严密的MyBatis程序

```java
package com.lsl.code;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 采用正规的方式，写一个完整版的MyBatis程序
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/30 2:30
 */
@SpringBootTest
public class MyBatisCompleteTest {

    @Test
    public void TestMyBatisComplete(){
        SqlSession sqlSession = null;
        try {
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"));
            // 开启会话（底层会开启事务）
            sqlSession = sqlSessionFactory.openSession();
            // 执行SQL语句，处理相关业务
            int count = sqlSession.insert("insertCar");
            System.out.println(count);
            // 执行到这里，没有发生任何异常，提交事务。终止事务。
            sqlSession.commit();
        } catch (Exception e) {
            // 最好回滚事务
            if (sqlSession != null) {
                sqlSession.rollback();
            }
            e.printStackTrace();
        } finally {
            // 关闭会话（释放资源）
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
```

##  2.4  引⼊⽇志框架logback

- 引⼊⽇志框架的⽬的是为了看清楚mybatis执⾏的具体sql

-  启⽤标准⽇志组件，只需要在mybatis-config.xml⽂件中添加以下配置：【可参考mybatis⼿册】

```xml
<settings>
 <setting name="logImpl" value="STDOUT_LOGGING" />
</settings>
```

完整mybatis-config.xml文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--开启mybatis对标准日志的实现。-->
    <!--<settings>-->
    <!--<setting name="logImpl" value="STDOUT_LOGGING"/>-->
    <!--<setting name="logImpl" value="SLF4J"/>-->
    <!--</settings>-->
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING" />
    </settings>
    <environments default="development">
        <environment id="development">
            <!--<transactionManager type="MANAGED"/>-->
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mp"/>
                <property name="username" value="root"/>
                <property name="password" value="W135246W"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <!--执行XxxMapper.xml文件的路径-->
        <!--resource属性自动会从类的根路径下开始查找资源。-->
        <mapper resource="CarMapper.xml"/>
        <!--resource属性：从类路径当中加载资源。-->
        <!--<mapper resource="com/CarMapper2.xml"/>-->

        <!--url属性：从绝对路径当中加载资源。-->
        <!--语法格式：file:///绝对路径-->
        <!--不建议：因为移植性差。-->
        <!--<mapper url="file:///d:/CarMapper.xml"/>-->
    </mappers>
</configuration>
```

标准⽇志也可以⽤，但是配置不够灵活，可以集成其他的⽇志组件，例如：log4j，logback等

- logback是⽬前⽇志框架中性能较好的，较流⾏的，所以我们选它

- 引⼊logback的步骤： 
  - 第⼀步：引⼊logback相关依赖

```xml
<dependency>
 <groupId>ch.qos.logback</groupId>
 <artifactId>logback-classic</artifactId>
 <version>1.2.11</version>
 <scope>test</scope>
</dependency>
```

第⼆步：引⼊logback相关配置⽂件（⽂件名叫做logback.xml或logback-test.xml，放到类路径 当中）4

```xml
<?xml version="1.0" encoding="UTF-8"?>

<configuration debug="false">
    <!-- 控制台输出 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>[%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>

    <!--mybatis log configure-->
    <logger name="com.apache.ibatis" level="TRACE"/>
    <logger name="java.sql.Connection" level="DEBUG"/>
    <logger name="java.sql.Statement" level="DEBUG"/>
    <logger name="java.sql.PreparedStatement" level="DEBUG"/>

    <!-- 日志输出级别,logback日志级别包括五个：TRACE < DEBUG < INFO < WARN < ERROR -->
    <root level="DEBUG">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="FILE"/>
    </root>

</configuration>
```

再次执⾏单元测试⽅法testInsertCar，查看控制台是否有sql语句输出

![image-20221230223755551](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212302238901.png)

**mybatis集成日志组件:**

```sh
7. 关于mybatis集成日志组件。让我们调试起来更加方便。

    * mybatis常见的集成的日志组件有哪些呢？
        SLF4J（沙拉风）：沙拉风是一个日志标准，其中有一个框架叫做logback，它实现了沙拉风规范。
        LOG4J
        LOG4J2
        STDOUT_LOGGING
        ....

        注意：log4j log4j2 logback都是同一个作者开发的。

    * 其中STDOUT_LOGGING是标准日志，mybatis已经实现了这种标准日志。mybatis框架本身已经实现了这种标准。
    只要开启即可。怎么开启呢？在mybatis-config.xml文件中使用settings标签进行配置开启。
        <settings>
            <setting name="logImpl" value="STDOUT_LOGGING"/>
        </settings>
        这个标签在编写的时候要注意，它应该出现在environments标签之前。注意顺序。当然，不需要记忆这个顺序。
        因为有dtd文件进行约束呢。我们只要参考dtd约束即可。

        这种实现也是可以的，可以看到一些信息，比如：连接对象什么时候创建，什么时候关闭，sql语句是怎样的。
        但是没有详细的日期，线程名字，等。如果你想使用更加丰富的配置，可以集成第三方的log组件。

    * 集成logback日志框架。
        logback日志框架实现了slf4j标准。(沙拉风：日志门面。日志标准。)
        第一步：引入logback的依赖。
            <dependency>
                <groupId>ch.qos.logback</groupId>
                <artifactId>logback-classic</artifactId>
                <version>1.2.11</version>
            </dependency>
        第二步：引入logback所必须的xml配置文件。
            这个配置文件的名字必须叫做：logback.xml或者logback-test.xml，不能是其它的名字。
            这个配置文件必须放到类的根路径下。不能是其他位置。
            主要配置日志输出相关的级别以及日志具体的格式
```

##  2.5 MyBatis⼯具类SqlSessionUtil的封装

每⼀次获取SqlSession对象代码太繁琐，封装⼀个⼯具类

```java
package com.lsl.code.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionUtil {
    private static SqlSessionFactory sqlSessionFactory;

    /**
     * 类加载时初始化sqlSessionFactory对象
     */
    static {
        try {
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 每调⽤⼀次openSession()可获取⼀个新的会话，该会话⽀持⾃动提交。
     *
     * @return 新的会话对象
     */
    public static SqlSession openSession() {
        return sqlSessionFactory.openSession(true);
    }
}
```

测试工具类，将testlnsertCar()改造

```java
package com.lsl.code;

import com.lsl.code.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/30 23:05
 */
@SpringBootTest
public class TestSqlSessionUtil {
    @Test
    public void testInsertCar() {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执⾏SQL
        int count = sqlSession.insert("insertCar");
        System.out.println("插⼊了⼏条记录:" + count);
        sqlSession.close();
    }
}
```



