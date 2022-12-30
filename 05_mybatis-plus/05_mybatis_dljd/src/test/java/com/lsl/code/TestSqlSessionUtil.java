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
