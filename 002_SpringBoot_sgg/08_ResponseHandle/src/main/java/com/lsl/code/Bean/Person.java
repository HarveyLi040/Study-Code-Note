package com.lsl.code.Bean;

import lombok.Data;
import java.util.Date;
@Data
public class Person {
    private String userName;
    private Integer age;
    private Date birth;
    private Pet pet;
}