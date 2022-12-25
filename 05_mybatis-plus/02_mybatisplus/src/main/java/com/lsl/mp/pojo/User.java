package com.lsl.mp.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2022/12/25 14:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userName;

    // 查询时不返回该字段的值
    @TableField(select = false)
    private String password;
    private String name;
    private Integer age;

    // 指定数据库表中的字段名
    @TableField(value = "email")
    private String email;

    // 此字段在数据库表中不存在
    @TableField(exist = false)
    private String address;

    public User(String userName, String password, String name, Integer age, String email) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.age = age;
        this.email = email;
    }

}
