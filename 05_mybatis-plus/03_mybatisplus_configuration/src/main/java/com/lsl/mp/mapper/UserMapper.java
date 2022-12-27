package com.lsl.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lsl.mp.pojo.User;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/25 14:37
 */
public interface UserMapper extends BaseMapper<User> {

    User findById(Long id);
}