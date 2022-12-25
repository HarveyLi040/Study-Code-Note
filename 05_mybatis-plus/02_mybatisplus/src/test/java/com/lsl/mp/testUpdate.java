package com.lsl.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;

import net.minidev.json.writer.UpdaterMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/25 21:13
 */
@Slf4j
@SpringBootTest
public class testUpdate {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testUpdateById() {
        User user = new User();
        user.setId(6L); //主键
        user.setAge(21); //更新的字段

        //根据id更新，更新不为null的字段
        this.userMapper.updateById(user);
    }

    @Test
    public void UpdateById() {
        User user = new User("liubei", "abcd", "刘备", 50, "23983@qq.com");
        user.setId(1L); // 条件 ——> 根据id做更新
        int result = userMapper.updateById(user);
        System.out.println("result:" + result);
        System.out.println("user:"+user);
    }

    @Test
    public void testUpdat01() {
        User user = new User();
        user.setAge(32); //更新的字段

        //更新的条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", 6);

        //执行更新操作
        int result = this.userMapper.update(user, wrapper);
        log.info("user:{}",user);
        log.info("result = {}",result);


        User user01 = new User();
        user01.setPassword("abcd");//更新的字段

        //更新的条件
        QueryWrapper<User> wrapper01 = new QueryWrapper<>();
        wrapper01.eq("user_name", "liubei");

        //执行更新操作
        int result01 = this.userMapper.update(user01, wrapper01);
        log.info("user:{}",user);
        log.info("result = {}",result01);
    }

    @Test
    public void testUpdat02() {

        //更新的条件
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        // 更新的字段、更新的条件
        wrapper.set("age",21).set("password","abc123").eq("user_name","lisi");

        //执行更新操作
        int result = userMapper.update(null, wrapper);
        log.info("result = {}",result);
    }
}
