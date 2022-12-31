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
/*  //修改前
    @Test
    public void testNamespace(){
        // 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执⾏SQL语句
        List<Object> cars = sqlSession.selectList("selectCarAll");
        // 输出结果
        cars.forEach(car -> System.out.println(car));
    }
*/

    //修改后
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

}
