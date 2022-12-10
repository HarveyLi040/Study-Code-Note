package com.lsl.code;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
@Slf4j
public class HashTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testHash() {
        stringRedisTemplate.opsForHash().put("user:400", "name", "虎哥");
        stringRedisTemplate.opsForHash().put("user:400", "age", "21");

        // 拿到所有的键值对
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("user:400");
        log.info("entries = {}",entries);
    }
}
