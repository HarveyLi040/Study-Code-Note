package com.lsl.code;

import com.lsl.code.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/31 16:01
 */
@SpringBootTest
public class DeleteTest {

    @Test
    public void testDeleteById(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执行SQL语句
        //int count = sqlSession.delete("deleteById", 59);
        int count = sqlSession.delete("deleteById", 136);
        System.out.println(count);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testDeleteByCarNum(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 执行SQL语句
        int count = sqlSession.delete("deleteByCarNum",1111);
        System.out.println(count);
        sqlSession.commit();
        sqlSession.close();
    }
}
