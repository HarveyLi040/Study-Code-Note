package com.lsl.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/28 22:53
 */
@Slf4j
@SpringBootTest
public class TestAllEq {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testAllEq01() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "李四");
        params.put("age", "20");
        params.put("password", null);

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.allEq(params);
//执行的sql语句： SELECT id,user_name,name,age,email FROM tb_user WHERE password IS NULL AND name = ? AND age = ?
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
//        没有查到数据
    }

    @Test
    public void testAllEq02() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "李四");
        params.put("age", "20");
        params.put("password", null);

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.allEq(params, false);
//执行的sql语句： SELECT id,user_name,name,age,email FROM tb_user WHERE name = ? AND age = ?
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
//        User(id=2, userName=lisi, password=null, name=李四, age=20, email=test2@itcast.cn, address=null)
    }

    @Test
    public void testAllEq03() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "李四");
        params.put("age", "20");
        params.put("password", "123456");

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.allEq((k, v) -> (k.equals("age") || k.equals("id")), params);
//执行的sql语句：   SELECT id,user_name,name,age,email FROM tb_user WHERE age = ?
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
// User(id=2, userName=lisi, password=null, name=李四, age=20, email=test2@itcast.cn, address=null)
    }

}
