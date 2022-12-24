package com.lsl.mp.mapper;

import com.lsl.mp.pojo.User;
/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/25 0:53
 */
import java.util.List;
public interface UserMapper {
    List<User> findAll();
}
