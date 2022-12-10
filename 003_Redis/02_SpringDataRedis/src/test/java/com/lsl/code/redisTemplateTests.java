package com.lsl.code;

import com.lsl.code.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class redisTemplateTests {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    void testString() {
        redisTemplate.opsForValue().set("name","胡歌");
        Object name = redisTemplate.opsForValue().get("name");
        log.info("name:{}",name);
    }
    
    @Test
    void testSerializer(){
        // 写入数据
        redisTemplate.opsForValue().set("user:100",new User("胡歌",40));

        // 获取数据
        Object user = (User) redisTemplate.opsForValue().get("user:100");
        log.info("user:{}",user);
    }

}
