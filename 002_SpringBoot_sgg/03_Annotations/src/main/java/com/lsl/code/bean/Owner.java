package com.lsl.code.bean;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Owner {
       private String name;
       private int age;
       private Pet pet;
       public Owner(String name,int age){
              this.name = name;
              this.age = age;
       }
}
