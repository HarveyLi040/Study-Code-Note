package com.lsl.code.bean;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Pet {
    private String name;
    private int age;
    public Pet(String name,int age){
        this.name = name;
        this.age = age;
    }
}
