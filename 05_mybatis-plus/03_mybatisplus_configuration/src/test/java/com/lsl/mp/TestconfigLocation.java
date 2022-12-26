package com.lsl.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * @date 2022/12/27 7:24
 */
@Slf4j
@SpringBootTest
public class TestconfigLocation {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelectPage() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.gt("age", 20); //年龄大于20岁

        // 查询第一页，查询一条数据
        Page<User> page = new Page<>(1, 1);
        //根据条件查询数据
        IPage<User> iPage = this.userMapper.selectPage(page, wrapper);
        System.out.println("数据总条数：" + iPage.getTotal());
        System.out.println("总页数：" + iPage.getPages());
        List<User> users = iPage.getRecords();
        for (User user : users) {
            System.out.println("user = " + user);
        }

/*      数据总条数：3
        总页数：3
        user = User(id=3, userName=wangwu, password=123456, name=王五, age=28,email=test3@itcast.cn, address=null)*/
    }
}
