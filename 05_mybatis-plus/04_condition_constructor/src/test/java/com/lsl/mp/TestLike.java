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
 * @date 2022/12/29 0:13
 */
@Slf4j
@SpringBootTest
public class TestLike {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testWrapper() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();


        wrapper.like("name", "赵");
//        SELECT id,user_name,name,age,email FROM tb_user WHERE name LIKE ?
        List<User> users = this.userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
// User(id=4, userName=zhaoliu, password=null, name=赵六, age=21, email=test4@itcast.cn, address=null)
    }

}
