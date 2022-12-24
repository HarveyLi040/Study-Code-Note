package com.lsl.mp;

import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
@Slf4j
class ApplicationTests {

    @Test
    void TestFindAll() throws IOException {
        String configFileName = "mybatis-config.xml";
        InputStream resourceAsStream = Resources.getResourceAsStream(configFileName);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 测试查询
        List<User> users = userMapper.findAll();
        for (User user : users) {
           log.info("user:{}",user);
        }
    /*  user:User(id=1, userName=zhangsan, password=123456, name=张三, age=18, email=test1@itcast.cn)
        user:User(id=2, userName=lisi, password=123456, name=李四, age=20, email=test2@itcast.cn)
        user:User(id=3, userName=wangwu, password=123456, name=王五, age=28, email=test3@itcast.cn)
        user:User(id=4, userName=zhaoliu, password=123456, name=赵六, age=21, email=test4@itcast.cn)
        user:User(id=5, userName=sunqi, password=123456, name=孙七, age=24, email=test5@itcast.cn)*/
    }

}
