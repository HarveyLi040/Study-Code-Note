package com.lsl.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsl.mp.mapper.UserMapper;
import com.lsl.mp.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
public class TestDelete {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testDeleteById() {
        // 根据id删除数据
        int res = userMapper.deleteById(6L);
        log.info("res = {}", res);
    }

    @Test
    public void testDeleteByMap() {
        Map<String, Object> map = new HashMap<>();
        // 根据map中的条件去删除数据，多条件之间是and的关系
        map.put("user_name", "liubei");
        map.put("password", "abcd");
        int res = userMapper.deleteByMap(map);
        System.out.println("res = " + res);
    }

    // delete用法一：
    @Test
    public void testDelete01() {
        // 根据包装的条件做删除
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", "zhangfei").eq("password", "123456");

        int res = userMapper.delete(wrapper);
        log.info("res = {}", res);
    }

    // delete用法二：
    @Test
    public void testDelete02() {

        User user = new User();
        user.setPassword("123456");
        user.setUserName("sunqi");

        QueryWrapper<User> wrapper = new QueryWrapper<>(user);
        // 根据包装的条件做删除
        int res = userMapper.delete(wrapper);
        log.info("res = {}", res);
    }

    @Test
    public void testDeleteBatchIds() {
        // 根据id做批量删除
        int res = userMapper.deleteBatchIds(Arrays.asList(2L, 3L,4L));
    }
}