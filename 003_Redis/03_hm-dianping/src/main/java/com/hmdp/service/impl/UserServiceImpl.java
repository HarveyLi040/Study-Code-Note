package com.hmdp.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static com.hmdp.utils.SystemConstants.USER_NICK_NAME_PREFIX;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Result sendCode(String phone, HttpSession session) {

        // 1.校验手机号(利用正则表达式校验手机号格式)，如果不符合就返回错误信息，如果符合就生成验证码
        if(RegexUtils.isPhoneInvalid(phone)){
            // 不符合则返回错误信息
            return Result.fail("手机号格式错误");
        }   // 符合:

        // 2.生成验证码并保存验证码到session
        String code = RandomUtil.randomNumbers(6);
        session.setAttribute("code",code);

        // 3.发送验证码(此处并不实现，仅模拟。一般公司中会有一个发送验证码的服务，只需调用即可)
        log.info("发送短信验证码成功，验证码：{}",code);

        // 4.整个流程成功则返回ok
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        // 1.校验手机号（每个请求都要校验）
        String phone = loginForm.getPhone();
        if(RegexUtils.isPhoneInvalid(phone)){
            return Result.fail("手机号格式错误");
        }

        // 2.校验验证码，不一致就报错，一致则根据手机号查询用户
        Object cashecode = session.getAttribute("code");
        String code = loginForm.getCode();
        if(cashecode == null || (!cashecode.toString().equals(code))){
            return  Result.fail("验证码错误");
        }

        /* 3.根据手机号查询用户(select * from tb_user where phone = ?) , 用户不存在就创建用户并保存
               UserServiceImpl extends ServiceImpl<UserMapper, User>
               UserServiceImpl 继承ServiceImpl<UserMapper, User>后 自带 query()方法 */
        User user = query().eq("phone", phone).one();

        if(user == null){
           // 如果用户不存在，创建用户并保存
          user = createUserWithPhone(phone);
        }

        // 4.保存用户信息到session中（用户存不存在都要做）
        session.setAttribute("user",user);
        // 直接返回登录成功而不用返回用户凭证，因为session自带sessionID，起到凭证的作用
        return Result.ok();
    }

    /**
     * 根据手机号创建用户
     * @param phone
     * @return
     */
    private User createUserWithPhone(String phone) {
        // 1.创建用户
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(8));
        // 2.利用MyBatis-plus提供的save()方法 保存用户
        save(user);
        return user;
    }
}
