package com.lsl.code.service;

import jakarta.annotation.Resource;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1、查询数据库判断用户名是否存在，如果不存在就抛出异常UsernameNotFoundException
        if(!"admin".equals(username)){
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 2、如果用户名存在，就把查询出来的密码（注册时经加密存入数据库）进行解析，或者直接把密码放入构造方法
        String password = passwordEncoder.encode("123");
        return new User(username,password,AuthorityUtils.commaSeparatedStringToAuthorityList( "admin,normal"));
    }
}
