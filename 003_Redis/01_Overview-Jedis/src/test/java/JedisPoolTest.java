import com.lsl.code.util.JedisConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class JedisPoolTest {
    private Jedis jedis;

    @BeforeEach
    void setUp(){
        // 获取连接
        jedis = JedisConnectionFactory.getJedis();
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
            jedis.close();  // 并非真正的关闭，而是将连接归还到连接池
        }
    }

/* jedis.close() 源码：
      public void close() {
        if (this.dataSource != null) {
            JedisPoolAbstract pool = this.dataSource;
            this.dataSource = null;
            if (this.isBroken()) {
                pool.returnBrokenResource(this);
            } else {
                pool.returnResource(this);
            }
        } else {
            super.close();
        }
    }*/
}
