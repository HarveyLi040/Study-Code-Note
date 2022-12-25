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
import java.util.Arrays;
import java.util.List;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/25 22:32
 */

@Slf4j
@SpringBootTest
public class TestSelect {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelectById() {
        //根据id查询数据
        User user = this.userMapper.selectById(2L);
        System.out.println("result = " + user);
    }

    @Test
    public void testSelectBatchIds() {
        //根据id集合批量查询
        List<User> users = this.userMapper.selectBatchIds(Arrays.asList(2L, 3L, 4L));
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testSelectOne() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.eq("name", "李四");
        //根据条件查询一条数据，如果结果超过一条会报错
        User user = this.userMapper.selectOne(wrapper);
        System.out.println(user);

//       User(id=2, userName=lisi, password=123456, name=李四, age=20, email=test2@itcast.cn,address=null)
    }

    @Test
    public void testSelectCount() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.gt("age", 23); //年龄大于23岁
        //根据条件查询数据条数
        Integer count = this.userMapper.selectCount(wrapper);
        System.out.println("count = " + count);

//        count = 2
    }

    @Test
    public void testSelectList01() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.gt("age", 23); //年龄大于23岁

        //根据条件查询数据
        List<User> users = this.userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println("user = " + user);
        }

/*      user = User(id=3, userName=wangwu, password=123456, name=王五, age=28,email=test3@itcast.cn, address=null)
        user = User(id=5, userName=sunqi, password=123456, name=孙七, age=24,email=test5@itcast.cn, address=null)*/
    }

    @Test
    public void testSelectList02() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.like("email","itcast"); //年龄大于23岁

        //根据条件查询数据
        List<User> users = this.userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println("user = " + user);
        }

/*      user = User(id=1, userName=zhangsan, password=null, name=张三, age=18, email=test1@itcast.cn, address=null)
        user = User(id=2, userName=lisi, password=null, name=李四, age=20, email=test2@itcast.cn, address=null)
        user = User(id=3, userName=wangwu, password=null, name=王五, age=28, email=test3@itcast.cn, address=null)
        user = User(id=4, userName=zhaoliu, password=null, name=赵六, age=21, email=test4@itcast.cn, address=null)
        user = User(id=5, userName=sunqi, password=null, name=孙七, age=24, email=test5@itcast.cn, address=null)*/
    }

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
