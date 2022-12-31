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
}
