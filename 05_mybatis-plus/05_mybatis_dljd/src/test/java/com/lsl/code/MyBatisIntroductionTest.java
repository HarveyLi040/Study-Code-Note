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
