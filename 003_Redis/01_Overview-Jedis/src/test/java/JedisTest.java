import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class JedisTest {
    private Jedis jedis;

    @BeforeEach
    void setUp(){
        // 1.建立连接(主机名 端口号)
        jedis = new Jedis("192.168.54.133",6379);

        // 2.设置密码
        jedis.auth("123456");

        // 3.选择库
        jedis.select(0);
    }

    @Test
    void testString(){
        // 存入数据
        String result = jedis.set("name","lisi");
        System.out.println("result = " + result);  // result = OK

        // 获取数据
        String name = jedis.get("name");
        System.out.println("name = " + name); // name = lisi
    }

    @Test
    void testHash(){
        // 插入hash数据
        jedis.hset("user:1","name","Jack");
        jedis.hset("user:1","age","21");

        // 获取数据
        Map<String,String> map = jedis.hgetAll("user:1");
        System.out.println(map);  // {name=Jack, age=21}
    }
    
    @AfterEach
    void tearDown(){
        if(jedis != null){
            jedis.close();
        }
    }
}
