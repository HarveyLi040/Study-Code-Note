package com.lsl.code;

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
 * @date 2023/1/1 14:23
 */
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
