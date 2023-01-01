# 使⽤MyBatis完成CRUD

```sh
使用mybatis完成CRUD
什么是CRUD:
            C: Create增
            R: Retrieve查（检索）
            U: Update改
            D: Delete删
```

# 1.insert(Create)

##  1.1 程序演示

分析以下SQL映射文件中SQL语句存在的问题

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

存在的问题是:SQL语句中的值不应该写死，值应该是用户提供的。之前的JDBC代码是这样写的：

```java
// JDBC中使⽤ ? 作为占位符。那么MyBatis中会使⽤什么作为占位符呢？
String sql = "insert into t_car(car_num,brand,guide_price,produce_time,car_
type) values(?,?,?,?,?)";
// ......
// 给 ? 传值。那么MyBatis中应该怎么传值呢？
ps.setString(1,"103");
ps.setString(2,"奔驰E300L");
ps.setDouble(3,50.3);
ps.setString(4,"2022-01-01");
ps.setString(5,"燃油⻋");
```

在MyBatis中可以这样做︰

- 在Java程序中，将数据放到Map集合中
- 在sql语句中使用#{map集合的key}来完成传值，等同于JDBC中的?，#{}就是占位符
- Java程序这样写:

```java
package com.powernode.mybatis;
import com.powernode.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
/**
 * 测试MyBatis的CRUD
 * @author ⽼杜
 * @version 1.0
 * @since 1.0
 */
public class CarMapperTest {
 @Test
 public void testInsertCar(){
 // 准备数据
 Map<String, Object> map = new HashMap<>();
 map.put("k1", "103");
 map.put("k2", "奔驰E300L");
 map.put("k3", 50.3);
 map.put("k4", "2020-10-01");
 map.put("k5", "燃油⻋");
 // 获取SqlSession对象
 SqlSession sqlSession = SqlSessionUtil.openSession();
 // 执⾏SQL语句（使⽤map集合给sql语句传递数据）
 int count = sqlSession.insert("insertCar", map);
 System.out.println("插⼊了⼏条记录：" + count);
 }
}
```

SQL语句(CarMapper.xml文件)这样写：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
 PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
 "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace先随便写-->
<mapper namespace="car">
 <insert id="insertCar">
 insert into t_car(car_num,brand,guide_price,produce_time,car_typ
e) values(#{k1},#{k2},#{k3},#{k4},#{k5})
 </insert>
</mapper>
```

\#{} 的⾥⾯必须填写map集合的key，不能随便写。运⾏测试程序，查看数据库：

![image-20221231133416389](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311354146.png)



如果#{}⾥写的是map集合中不存在的key会有什么问题？修改CarMapper.xml：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
 PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
 "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="car">
 <insert id="insertCar">
 insert into t_car(car_num,brand,guide_price,produce_time,car_typ
e) values(#{kk},#{k2},#{k3},#{k4},#{k5})
 </insert>
</mapper>
```

运⾏程序：

![image-20221231133824357](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311354461.png)

![image-20221231133841392](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311355459.png)

通过测试，看到程序并没有报错。正常执⾏。不过 #{kk} 的写法导致⽆法获取到map集合中的数据，最终 导致数据库表car_num插⼊了NULL



在以上sql语句中，可以看到#{k1} #{k2} #{k3} #{k4} #{k5}的可读性太差，为了增强可读性，可以将 Java程序做如下修改：

```java
Map<String, Object> map = new HashMap<>();
// 让key的可读性增强
map.put("carNum", "103");
map.put("brand", "奔驰E300L");
map.put("guidePrice", 50.3);
map.put("produceTime", "2020-10-01");
map.put("carType", "燃油⻋");
```

SQL语句做如下修改，这样可以增强程序的可读性：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
 PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
 "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="car">
 <insert id="insertCar">
 insert into t_car(car_num,brand,guide_price,produce_time,car_type)
values(#{carNum},#{brand},#{guidePrice},#{produceTime},#{carType})
 </insert>
</mapper>
```

运⾏程序，查看数据库表：

![image-20221231134135625](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311355181.png)

使⽤Map集合可以传参，那使⽤pojo（简单普通的java对象）可以完成传参吗？测试⼀下： ●

- 第⼀步：定义⼀个pojo类Car，提供相关属性

```java
package com.powernode.mybatis.pojo;
/**
 * 封装汽车相关信息的pojo类。普通的java类。
 */
public class Car {
    // 数据库表当中的字段应该和pojo类的属性一一对应。
    // 建议使用包装类，这样可以防止null的问题。
    private Long id;
    private String carNum;
    private String brand;
    private Double guidePrice;
    private String produceTime;
    private String carType;

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", carNum='" + carNum + '\'' +
                ", brand='" + brand + '\'' +
                ", guidePrice=" + guidePrice +
                ", produceTime='" + produceTime + '\'' +
                ", carType='" + carType + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarNum() {
        return carNum;
    }

    /*public String getXyz() {
        return carNum;
    }*/

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getGuidePrice() {
        return guidePrice;
    }

    public void setGuidePrice(Double guidePrice) {
        this.guidePrice = guidePrice;
    }

    public String getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(String produceTime) {
        this.produceTime = produceTime;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public Car(Long id, String carNum, String brand, Double guidePrice, String produceTime, String carType) {
        this.id = id;
        this.carNum = carNum;
        this.brand = brand;
        this.guidePrice = guidePrice;
        this.produceTime = produceTime;
        this.carType = carType;
    }
    public Car() {
    }
}
```

第二步：java程序

```java
@Test
public void testInsertCarByPOJO(){
 // 创建POJO，封装数据
 Car car = new Car();
 car.setCarNum("103");
 car.setBrand("奔驰C200");
 car.setGuidePrice(33.23);
 car.setProduceTime("2020-10-11");
 car.setCarType("燃油⻋");
 // 获取SqlSession对象
 SqlSession sqlSession = SqlSessionUtil.openSession();
 // 执⾏SQL，传数据
 int count = sqlSession.insert("insertCarByPOJO", car);
 System.out.println("插⼊了⼏条记录" + count);
}
```

第三步：SQL语句(修改CarMapper.xml如下)：

```xml
<insert id="insertCarByPOJO">
 <!--#{} ⾥写的是POJO的属性名-->
 insert into t_car(car_num,brand,guide_price,produce_time,car_type) values
(#{carNum},#{brand},#{guidePrice},#{produceTime},#{carType})
</insert>
```

运⾏程序，查看数据库表：

![image-20221231134644756](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311547192.png)



\#{} ⾥写的是POJO的属性名，如果写成其他的会有问题吗？修改CarMapper.xml 如下:

```xml
<insert id="insertCarByPOJO">
 insert into t_car(car_num,brand,guide_price,produce_time,car_type) values
(#{a},#{brand},#{guidePrice},#{produceTime},#{carType})
</insert>
```

运⾏程序，出现了以下异常：

![image-20221231154207981](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311547271.png)

错误信息中描述：在Car类中没有找到a属性的getter⽅法。 修改POJO类Car的代码，只将getCarNum()⽅法名修改为getA()，其他代码不变，如下：

![image-20221231154231073](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311547936.png)

再运⾏程序，查看数据库表中数据：

![image-20221231154252986](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311547552.png)

**经过测试得出结论：**  

如果采⽤map集合传参，#{} ⾥写的是map集合的key，如果key不存在不会报错，数据库表中会插 ⼊NULL。 如果采⽤POJO传参，#{} ⾥写的是get⽅法的⽅法名去掉get之后将剩下的单词⾸字⺟变⼩写（例 如：getAge对应的是#{age}，getUserName对应的是#{userName}），如果这样的get⽅法不存在会报错。 注意：其实传参数的时候有⼀个属性parameterType，这个属性⽤来指定传参的数据类型，不过这个属 性是可以省略的

```xml
    <insert id="insertCar" parameterType="java.util.Map">
        insert into t_car(car_num, brand, guide_price, produce_time, car_type)
        values (#{carNum}, #{brand}, #{guidePrice}, #{produceTime}, #{carType})
    </insert>
    <insert id="insertCarByPOJO" parameterType="com.powernode.mybatis.pojo.Car">
        insert into t_car(car_num, brand, guide_price, produce_time, car_type)
        values (#{carNum}, #{brand}, #{guidePrice}, #{produceTime}, #{carType})
    </insert>
```

##  1.2 总结

```sh
2. insert
    <insert id="insertCar">
        insert into t_car(id,car_num,brand,guide_price,produce_time,car_type)
        values(null,'1003','丰田霸道',30.0,'2000-10-11','燃油车');
    </insert>
    这样写的问题是？
        值 显然是写死到配置文件中的
        这个在实际开发中是不存在的
        一定是前端的form表单提交过来数据。然后将值传给sql语句

    例如：JDBC的代码是怎么写的？
        String sql = "insert into t_car(id,car_num,brand,guide_price,produce_time,car_type) values(null,?,?,?,?,?)";
        ps.setString(1, xxx);
        ps.setString(2, yyy);
        ....

    在JDBC当中占位符采用的是?，在mybatis当中是什么呢？
        和?等效的写法是：#{}
        在mybatis当中不能使用?占位符，必须使用 #{} 来代替JDBC当中的 ?
        #{} 和 JDBC当中的 ? 是等效的。

    java程序中使用Map可以给SQL语句的占位符传值：
        Map<String, Object> map = new HashMap<>();
        map.put("k1", "1111");
        map.put("k2", "比亚迪汉");
        map.put("k3", 10.0);
        map.put("k4", "2020-11-11");
        map.put("k5", "电车");

        insert into t_car(id,car_num,brand,guide_price,produce_time,car_type) values(null,#{k1},#{k2},#{k3},#{k4},#{k5});
        注意：#{这里写什么？写map集合的key，如果key不存在，获取的是null}

        一般map集合的key起名的时候要见名知意。
            map.put("carNum", "1111");
            map.put("brand", "比亚迪汉2");
            map.put("guidePrice", 10.0);
            map.put("produceTime", "2020-11-11");
            map.put("carType", "电车");

            insert into t_car(id,car_num,brand,guide_price,produce_time,car_type) values(null,#{carNum},#{brand},#{guidePrice},#{produceTime},#{carType});

    java程序中使用POJO类给SQL语句的占位符传值：
        Car car = new Car(null, "3333", "比亚迪秦", 30.0, "2020-11-11", "新能源");

        注意：占位符#{}，大括号里面写：pojo类的属性名
        insert into t_car(id,car_num,brand,guide_price,produce_time,car_type)
        values(null,#{carNum},#{brand},#{guidePrice},#{produceTime},#{carType})

        把SQL语句写成这个德行：
        insert into t_car(id,car_num,brand,guide_price,produce_time,car_type)
                values(null,#{xyz},#{brand},#{guidePrice},#{produceTime},#{carType})
        出现了什么问题呢？
            There is no getter for property named 'xyz' in 'class com.powernode.mybatis.pojo.Car'
            mybatis去找：Car类中的getXyz()方法去了。没找到。报错了。

        怎么解决的？
            可以在Car类中提供一个getXyz()方法。这样问题就解决了。

        通过这个测试，得出一个结论：
            严格意义上来说：如果使用POJO对象传递值的话，#{}这个大括号中到底写什么？
                写的是get方法的方法名去掉get，然后将剩下的单词首字母小写，然后放进去。
                例如：getUsername() --> #{username}
                例如：getEmail() --> #{email}
                ....
        也就是说mybatis在底层给?传值的时候，先要获取值，怎么获取的？
            调用了pojo对象的get方法。例如：car.getCarNum()，car.getCarType()，car.getBrand()
```

#  2.delete(Delete)

##  2.1 程序演示

需求：根据car_num进⾏删除

先在CarMapper.xml中添加SQL语句：

```xml
    <delete id="deleteByCarNum">
        delete from t_car where car_num = #{carNum}
    </delete>
```

Java程序这样写：

```java
    @Test
    public void testDeleteByCarNum(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执行SQL语句
        int count = sqlSession.delete("deleteByCarNum",1111);
        System.out.println(count);
        sqlSession.commit();
        sqlSession.close();
    }
```

运行结果：

![image-20221231161654312](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311616935.png)

注意：当占位符只有⼀个的时候，${} ⾥⾯的内容可以随便写

##  2.2 总结

```sh
3. delete
    * 需求：根据id删除数据
        将id=59的数据删除。

    实现：
        int count = sqlSession.delete("deleteById", 59);
        <delete id="deleteById">
            delete from t_car where id = #{fdsfd}
        </delete>
        注意：如果占位符只有一个，那么#{}的大括号里可以随意。但是最好见名知意。
```

# 3.update（Update）

##  3.1 程序演示

需求：修改id=34的Car信息，car_num为102，brand为⽐亚迪汉，guide_price为30.23，produce_time 为2018-09-10，car_type为电⻋ 

修改前：

![image-20221231162119758](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311621997.png)

SQL语句如下(在CarMapper.xml中添加)：

```xml
    <update id="updateCarByPOJO">
        update t_car
        set car_num      = #{carNum},
            brand        = #{brand},
            guide_price  = #{guidePrice},
            produce_time = #{produceTime},
            car_type     = #{carType}
        where id = #{id}
    </update>

    <update id="updateById">
        update t_car
        set car_num=#{carNum},
            brand=#{brand},
            guide_price=#{guidePrice},
            produce_time=#{produceTime},
            car_type=#{carType}
        where id = #{id}
    </update>
```

Java代码如下：

```java
package com.lsl.code;

import com.lsl.code.pojo.Car;
import com.lsl.code.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/31 16:24
 */
@SpringBootTest
public class UpdateTest {

    @Test
    public void testUpdateCarByPOJO() {
        // 准备数据
        Car car = new Car();
        car.setId(34L);
        car.setCarNum("102");
        car.setBrand("⽐亚迪汉");
        car.setGuidePrice(30.23);
        car.setProduceTime("2018-09-10");
        car.setCarType("电⻋");
        // 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执⾏SQL语句
        int count = sqlSession.update("updateCarByPOJO", car);
//  update t_car set car_num = ?, brand = ?, guide_price = ?, produce_time = ?, car_type = ? where id = ?
        System.out.println("更新了⼏条记录：" + count);
    }
    
    @Test
    public void testUpdateById() {
        SqlSession sqlSession = SqlSessionUtil.openSession();

        // 准备数据
        Car car = new Car(4L, "9999", "凯美瑞", 30.3, "1999-11-10", "燃油车");

        // 执行SQL语句
        int count = sqlSession.update("updateById", car);
        System.out.println(count);

        sqlSession.commit();
        sqlSession.close();
    }
}
```

运行结果：

![image-20221231162636019](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311626833.png)

当然了，如果使⽤map传数据也是可以的

##  3.2 总结

```sh
4. update
    * 需求：根据id修改某条记录

    实现：
        <update id="updateById">
            update t_car set
                 car_num=#{carNum},
                 brand=#{brand},
                 guide_price=#{guidePrice},
                 produce_time=#{produceTime},
                 car_type=#{carType}
            where
                id = #{id}
        </update>

        Car car = new Car(4L, "9999", "凯美瑞", 30.3, "1999-11-10", "燃油车");
        int count = sqlSession.update("updateById", car);
```

# 4.select（Retrieve）

select语句和其它语句不同的是：查询会有⼀个结果集。来看mybatis是怎么处理结果集的！

## 4.1 查询⼀条数据

需求：查询id为1的Car信息 

SQL语句如下：

```xml
    <select id="selectCarById">
        select *
        from t_car
        where id = #{id}
    </select>
```

Java程序如下：

```java
    @Test
    public void testSelectCarById() {
        // 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执⾏SQL语句
        Object car = sqlSession.selectOne("selectCarById", 1);
        System.out.println(car);
    }
```

运⾏结果出现异常：

```java
org.apache.ibatis.exceptions.PersistenceException: 
### Error querying database.  Cause: org.apache.ibatis.executor.ExecutorException: A query was run and no Result Maps were found for the Mapped Statement 'fdsafdsa.selectCarById'.  It's likely that neither a Result Type nor a Result Map was specified.
### The error may exist in CarMapper.xml
### The error may involve fdsafdsa.selectCarById
### The error occurred while handling results
### SQL: select *         from t_car         where id = ?
### Cause: org.apache.ibatis.executor.ExecutorException: A query was run and no Result Maps were found for the Mapped Statement 'fdsafdsa.selectCarById'.  It's likely that neither a Result Type nor a Result Map was specified.
```

以上的异常⼤致的意思是：对于⼀个查询语句来说，你需要指定它的“结果类型”或者“结果映射”。 所以说，你想让mybatis查询之后返回⼀个Java对象的话，⾄少你要告诉mybatis返回⼀个什么类型的 Java对象，可以在标签中添加resultType属性，⽤来指定查询要转换的类型：

```xml
    <select id="selectCarById" resultType="com.lsl.code.pojo.Car">
        select *
        from t_car
        where id = #{id}
    </select>
```

![image-20221231164623401](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311646505.png)

运⾏后之前的异常不再出现了，这说明添加了resultType属性之后，解决了之前的异常，可以看出 resultType是不能省略的。 仔细观察控制台的⽇志信息，不难看出，结果查询出了⼀条。并且每个字段都查询到值了：Row: 1, 100, 宝⻢520Li, 41.00, 2022-09-01, 燃油⻋ 但是奇怪的是返回的Car对象，只有id和brand两个属性有值，其它属性的值都是null，这是为什么呢？我们来观察⼀下查询结果列名和Car类的属性名是否能⼀⼀对应： 查询结果集的列名：id, car_num, brand, guide_price, produce_time, car_type Car类的属性名：id, carNum, brand, guidePrice, produceTime, carType 通过观察发现：只有id和brand是⼀致的，其他字段名和属性名对应不上，这是不是导致null的原因呢？ 我们尝试在sql语句中使⽤as关键字来给查询结果列名起别名试试：



修改CarMapper.xml如下：

```xml
    <select id="selectCarByIdTwo" resultType="com.lsl.code.pojo.Car">
        select id,
               car_num      as carNum,
               brand,
               guide_price  as guidePrice,
               produce_time as produceTime,
               car_type     as carType
        from t_car
        where id = #{id}
    </select>
```

java代码测试：

```java
    @Test
    public void testSelectCarByIdTwo() {
        // 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执⾏SQL语句
        Object car = sqlSession.selectOne("selectCarByIdTwo", 125);
        System.out.println(car);
    }
```

运行结果如下：

![image-20221231165511786](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311655824.png)

通过测试得知，如果当查询结果的字段名和java类的属性名对应不上的话，可以采⽤as关键字起别名， 当然还有其它解决⽅案，我们后⾯再看

##  4.2 查询多条数据

需求：查询所有的Car信息

在CarMapper.xml中添加SQL语句：

```xml
<!--虽然结果是List集合，但是resultType属性需要指定的是List集合中元素的类型。-->
<select id="selectCarAll" resultType="com.lsl.code.pojo.Car">
    <!--记得使⽤as起别名，让查询结果的字段名和java类的属性名对应上。-->
    select id,
    car_num as carNum,
    brand, guide_price as guidePrice,
    produce_time as produceTime,
    car_type as carType
    from t_car
</select>
```

Java代码如下：

```java
@Test
public void testSelectCarAll() {
    // 获取SqlSession对象
    SqlSession sqlSession = SqlSessionUtil.openSession();
    // 执⾏SQL语句
    List<Object> cars = sqlSession.selectList("selectCarAll");
    // 输出结果
    cars.forEach(car -> System.out.println(car));
}
```

运⾏结果如下：

![image-20221231170047275](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311700320.png)

##  4.3 总结

```sh
5. select（查一个，根据主键查询的话，返回的结果一定是一个。）
    * 需求：根据id查询。

    实现：
        <select id="selectById" resultType="com.powernode.mybatis.pojo.Car">
            select * from t_car where id = #{id}
        </select>

        Object car = sqlSession.selectOne("selectById", 1);

    需要特别注意的是：
        select标签中resultType属性，这个属性用来告诉mybatis，查询结果集封装成什么类型的java对象。你需要告诉mybatis。
        resultType通常写的是：全限定类名。

    Car{id=1, carNum='null', brand='宝马520Li', guidePrice=null, produceTime='null', carType='null'}
    输出结果有点不对劲：
        id和brand属性有值。
        其他属性为null。

    carNum以及其他的这几个属性没有赋上值的原因是什么？
        select * from t_car where id = 1
        执行结果：
        +----+---------+-----------+-------------+--------------+----------+
        | id | car_num | brand     | guide_price | produce_time | car_type |
        +----+---------+-----------+-------------+--------------+----------+
        |  1 | 1001    | 宝马520Li |       10.00 | 2020-10-11   | 燃油车   |
        +----+---------+-----------+-------------+--------------+----------+
        car_num、guide_price、produce_time、car_type这是查询结果的列名。
        这些列名和Car类中的属性名对不上。
        Car类的属性名：
        carNum、guidePrice、produceTime、carType

        那这个问题怎么解决呢？
            select语句查询的时候，查询结果集的列名是可以使用as关键字起别名的。

        <select id="selectById" resultType="com.powernode.mybatis.pojo.Car">
            select
                id,car_num as carNum,brand,guide_price as guidePrice,
                produce_time as produceTime,
                car_type as carType
            from
                t_car
            where
                id = #{id}
        </select>
        起别名之后：
        +----+--------+-----------+------------+-------------+---------+
        | id | carNum | brand     | guidePrice | produceTime | carType |
        +----+--------+-----------+------------+-------------+---------+
        |  1 | 1001   | 宝马520Li |      10.00 | 2020-10-11  | 燃油车  |
        +----+--------+-----------+------------+-------------+---------+



6. select（查所有的）

    <select id="selectAll" resultType="com.powernode.mybatis.pojo.Car">
        select
            id,car_num as carNum,brand,guide_price as guidePrice,
            produce_time as produceTime,
            car_type as carType
        from
            t_car
    </select>

    List<Object> cars = sqlSession.selectList("selectAll");

    注意：resultType还是指定要封装的结果集的类型。不是指定List类型，是指定List集合中元素的类型。
    selectList方法：mybatis通过这个方法就可以得知你需要一个List集合。它会自动给你返回一个List集合
```



# 5. 关于SQL Mapper的namespace

##  5.1 程序演示

在SQL Mapper配置⽂件中标签的namespace属性可以翻译为命名空间，这个命名空间主要是 为了防⽌sqlId冲突的

创建CarMapper2.xml⽂件，代码如下：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="car2">
    <select id="selectCarAll" resultType="com.lsl.code.pojo.Car">
        select id,
               car_num      as carNum,
               brand,
               guide_price  as guidePrice,
               produce_time as produceTime,
               car_type     as carType
        from t_car
    </select>
</mapper>
```

不难看出，CarMapper.xml和CarMapper2.xml⽂件中都有 id="selectCarAll" 

将CarMapper2.xml配置到mybatis-config.xml⽂件中

```xml
<mappers>
    <mapper resource="CarMapper.xml"/>
    <mapper resource="CarMapper2.xml"/>
</mappers>
```

编写Java代码如下：

```java
package com.lsl.code;

import com.lsl.code.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/31 18:12
 */
@SpringBootTest
public class NamespaceTest {
    @Test
    public void testNamespace(){
        // 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执⾏SQL语句
        List<Object> cars = sqlSession.selectList("selectCarAll");
        // 输出结果
        cars.forEach(car -> System.out.println(car));
    }
}
```

运⾏报错，结果如下：

```java
org.apache.ibatis.exceptions.PersistenceException: 
### Error querying database.  Cause: java.lang.IllegalArgumentException: selectCarAll is ambiguous in Mapped Statements collection (try using the full name including the namespace, or rename one of the entries)
### Cause: java.lang.IllegalArgumentException: selectCarAll is ambiguous in Mapped Statements collection (try using the full name including the namespace, or rename one of the entries)
```

修改Java代码如下：

```java
    @Test
    public void testNamespace() {
        // 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执⾏SQL语句
        //List<Object> cars = sqlSession.selectList("car.selectCarAll");
        List<Object> cars = sqlSession.selectList("car2.selectCarAll");
        // 输出结果
        cars.forEach(car -> System.out.println(car));
    }
```

运行结果如下：

![image-20221231181743173](https://cdn.jsdelivr.net/gh/HarveyLi040/images/D:%5Cgithub%5Cimages202212311818438.png)

##  5.2 总结

```sh
7. 在sql mapper.xml文件当中有一个namespace,这个属性是用来指定命名空间的。用来防止id重复。
怎么用？
    在xml文件中：
        <mapper namespace="aaaaaaaaa">
            <select id="selectAll" resultType="com.powernode.mybatis.pojo.Car">
                select
                    id,car_num as carNum,brand,guide_price as guidePrice,
                    produce_time as produceTime,
                    car_type
                from
                    t_car
            </select>
        </mapper>
    在java程序中的写法：
        List<Object> cars = sqlSession.selectList("aaaaaaaaa.selectAll");

    实际上，本质上，mybatis中的sqlId的完整写法：
        namespace.id
```

