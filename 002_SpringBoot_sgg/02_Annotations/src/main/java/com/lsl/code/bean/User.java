package com.lsl.code.bean;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class User {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
