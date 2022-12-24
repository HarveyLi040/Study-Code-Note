package com.hmdp;

import com.hmdp.entity.Shop;
import com.hmdp.service.impl.ShopServiceImpl;
import com.hmdp.utils.CacheClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_KEY;

@SpringBootTest
class HmDianPingApplicationTests {

    @Resource
    private ShopServiceImpl shopService;

    @Resource
    private CacheClient cacheClient;

    @Test
    void testShopService(){
         shopService.saveShop2Redis(1L,10L);
    }

    @Test
    void  testCacheClient(){
        Shop shop = shopService.getById(2L);

        cacheClient.setWithLogicalExpire(CACHE_SHOP_KEY + 2L,shop,10L, TimeUnit.SECONDS);

    }


}
