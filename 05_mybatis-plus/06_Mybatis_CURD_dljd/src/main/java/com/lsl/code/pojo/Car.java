package com.lsl.code.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * 封装汽车相关信息的pojo类。普通的java类。
 */
@Data
@ToString
public class Car {
    // 数据库表当中的字段应该和pojo类的属性一一对应
    // 建议使用包装类，这样可以防止null的问题
    private Long id;
    private String carNum;
    private String brand;
    private Double guidePrice;
    private String produceTime;
    private String carType;

    public Car(Long id, String carNum, String brand, Double guidePrice, String produceTime, String carType) {
        this.id = id;
        this.carNum = carNum;
        this.brand = brand;
        this.guidePrice = guidePrice;
        this.produceTime = produceTime;
        this.carType = carType;
    }

    public Car() {
    }
}
