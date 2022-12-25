package com.lsl.mp;

import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/25 15:16
 */
@Slf4j
@SpringBootTest
public class TestInsert {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        User user01 = new User("liubei", "abcd", "刘备", 50, "23983@qq.com");
        User user02 = new User("zhangfei", "abck", "张飞", 42, "239sdds3@qq.com");

        // 返回受影响的行数
        int result01 = userMapper.insert(user01);
        int result02 = userMapper.insert(user02);

        // 获取自增长的id（写入数据库后会回填对象）
        System.out.println("id01->" + user01.getId());
        System.out.println("id02->" + user02.getId());
    }

/*  注意：为防止id自增长出现错误，要在id字段上加上注解@TableId(type = IdType.AUTO) //指定id类型为自增长

    @TableId(type = IdType.AUTO) //指定id类型为自增长
    private Long id;*/


    @Test
    public void testSelectById() {
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }



}
