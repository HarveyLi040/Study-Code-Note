package com.hmdp.interceptor;
import com.hmdp.utils.UserHolder;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.依据ThreadLocal中是否有用户，判断是否需要拦截
        if (UserHolder.getUser() == null) {
            // 没有，需要拦截，返回状态码
            response.setStatus(401);
            return false;
        }
        // 有用户，则放行
        return true;
    }
}
