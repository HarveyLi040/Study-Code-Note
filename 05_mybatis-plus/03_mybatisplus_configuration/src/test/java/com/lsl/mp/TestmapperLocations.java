package com.lsl.mp;

import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/27 8:14
 */
@SpringBootTest
public class TestmapperLocations {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testFindById() {
        User user = userMapper.findById(2L);
        System.out.println(user);
//      User(id=2, userName=lisi, password=123456, name=李四, age=20, email=test2@itcast.cn, address=null)
    }
}
