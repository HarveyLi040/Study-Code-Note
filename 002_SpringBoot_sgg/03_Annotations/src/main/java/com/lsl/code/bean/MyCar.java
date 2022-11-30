package com.lsl.code.bean;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
/*只有在容器中的组件，才会拥有spring boot提供的强大功能*/
@Data
@ConfigurationProperties(prefix = "mycar")
public class MyCar {
    private String brand;
    private Integer price;
}
