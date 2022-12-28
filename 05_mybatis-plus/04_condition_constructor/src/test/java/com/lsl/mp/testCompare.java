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
 * @date 2022/12/28 23:59
 */
@Slf4j
@SpringBootTest
public class testCompare {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testEq() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("password", "123456")
                .ge("age", 20)
                .in("name", "李四", "王五", "赵六");

// 执行的sql语句： SELECT id,user_name,name,age,email FROM tb_user WHERE password = ? AND age >= ? AND name IN (?,?,?)
        List<User> users = this.userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }

//   User(id=2, userName=lisi, password=null, name=李四, age=20, email=test2@itcast.cn, address=null)
//   User(id=3, userName=wangwu, password=null, name=王五, age=28, email=test3@itcast.cn, address=null)
//   User(id=4, userName=zhaoliu, password=null, name=赵六, age=21, email=test4@itcast.cn, address=null)
    }
}
