package com.lsl.code;

import com.lsl.code.pojo.Car;
import com.lsl.code.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/31 13:26
 */
@SpringBootTest
public class InsertTest {
    @Test
    public void testInsertCar01() {
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


    @Test
    public void testInsertCar02() {
        SqlSession sqlSession = SqlSessionUtil.openSession();

        // 前端传过来数据了。
        // 这个对象我们先使用Map集合进行数据的封装。
        Map<String, Object> map = new HashMap<>();
        /*map.put("k1", "1111");
        map.put("k2", "比亚迪汉");
        map.put("k3", 10.0);
        map.put("k4", "2020-11-11");
        map.put("k5", "电车");*/
//  对应mapper中的sql语句： insert into t_car(id,car_num,brand,guide_price,produce_time,car_type) values(null,#{k1},#{k2},#{k3},#{k4},#{k5});


        map.put("carNum", "1111");
        map.put("brand", "比亚迪汉2");
        map.put("guidePrice", 10.0);
        map.put("produceTime", "2020-11-11");
        map.put("carType", "电车");
// 对应mapper中的sql语句：
// insert into t_car(id,car_num,brand,guide_price,produce_time,car_type) values(null,#{carNum},#{brand},#{guidePrice},#{produceTime},#{carType})
        // 执行SQL语句
        // insert方法的参数：
        // 第一个参数：sqlId，从CarMapper.xml文件中复制。
        // 第二个参数：封装数据的对象。
        int count = sqlSession.insert("insertCar", map);
        System.out.println(count);

        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testInsertCarByPOJO(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 封装数据
        Car car = new Car(null, "3333", "比亚迪秦", 30.0, "2020-11-11", "新能源");
        // 执行SQL
        int count = sqlSession.insert("insertCar", car); // ORM
        System.out.println(count);

        sqlSession.commit();
        sqlSession.close();
    }
}
