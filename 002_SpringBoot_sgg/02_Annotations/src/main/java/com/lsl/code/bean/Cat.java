package com.lsl.code.bean;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Cat {
    private String name;

    public Cat(String name) {
        this.name = name;
    }
}
