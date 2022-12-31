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
 * @date 2022/12/31 16:38
 */
@SpringBootTest
public class SelectTest {
    @Test
    public void testSelectCarById() {
        // 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执⾏SQL语句
        Object car = sqlSession.selectOne("selectCarById", 1);
        System.out.println(car);
    }

    @Test
    public void testSelectCarByIdTwo() {
        // 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执⾏SQL语句
        Object car = sqlSession.selectOne("selectCarByIdTwo", 125);
        System.out.println(car);
    }

    @Test
    public void testSelectCarAll() {
        // 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执⾏SQL语句
        List<Object> cars = sqlSession.selectList("selectCarAll");
        // 输出结果
        cars.forEach(car -> System.out.println(car));
    }

}
