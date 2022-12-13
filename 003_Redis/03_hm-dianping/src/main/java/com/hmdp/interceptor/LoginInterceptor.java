package com.hmdp.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

/*在拦截器中注入StringRedisTemplate：
     无法使用@Resource等注解在LoginInterceptor中实现注入（拦截器不在spring bean容器中，而是new出来的）
     解决：将要注入的类声明为属性，并提供构造方法，之后在MvcConfig注入对象
     最后在MvcConfig中通过构造方法创建拦截器，这样就为拦截器注入了对象*/
    private StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
/*没有redis之前：
        // 1.获取session
        HttpSession session = request.getSession();

        // 2.获取session中的用户
        Object user = session.getAttribute("user");

        // 3.判断用户是否存在
        if(user == null){
            // 不存在则拦截,并返回401状态码(未授权)
            response.setStatus(401);
            return false;
        }  // 存在：

        // 4.保存用户信息到ThreadLocal
        UserHolder.saveUser((UserDTO) user);
        // 放行
        return true;*/

//利用redis代替sessions实现登录验证：
        // 1.获取请求头中的token
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)) {
            response.setStatus(401);
            return false;
        }
        // 2.基于token获取redis中的用户
        String key = RedisConstants.LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);

        // 判断用户是否存在
        if (userMap.isEmpty()) {
            // 不存在则拦截,并返回401状态码(未授权)
            response.setStatus(401);
            return false;
        }

        // 3.将查询到的Hash数据转为UserDTO对象
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);

        // 保存用户信息到ThreadLocal
        UserHolder.saveUser(userDTO);

        // 4.刷新token有效期
        stringRedisTemplate.expire(key,RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUser();
    }
}
