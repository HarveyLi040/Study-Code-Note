package com.lsl.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/29 0:44
 */
@SpringBootTest
@Slf4j
public class TestLogical {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testWrapper() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name","李四").or().eq("age", 24);
//  SELECT id,user_name,password,name,age,email FROM tb_user WHERE name = ? OR age = ?


        List<User> users = this.userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
// User(id=2, userName=lisi, password=null, name=李四, age=20, email=test2@itcast.cn, address=null)
// User(id=5, userName=sunqi, password=null, name=孙七, age=24, email=test5@itcast.cn, address=null)

    }

}
