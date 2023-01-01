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
