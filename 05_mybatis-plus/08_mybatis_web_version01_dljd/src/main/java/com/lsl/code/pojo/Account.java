package com.lsl.code.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2023/1/1 20:06
 */
@Data
@ToString
public class Account {
    private Long id;
    private String actno;
    private Double balance;
    public Account(){}
    public Account(Long id, String actno, Double balance) {
        this.id = id;
        this.actno = actno;
        this.balance = balance;
    }

}