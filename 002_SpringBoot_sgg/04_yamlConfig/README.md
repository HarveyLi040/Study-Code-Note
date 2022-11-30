##   1.yaml配置文件

####  1.1 简介 & 语法 & 数据类型

**yaml配置文件**

YAML 是 "YAML Ain't Markup Language"（YAML 不是一种标记语言）的递归缩写。在开发的这种语言时，YAML 的意思其实是："Yet Another Markup Language"（仍是一种标记语言）

yaml非常适合用来做以数据为中心的配置文件

**基本语法**

- key: value；k v之间有空格
- 大小写敏感
- 使用缩进表示层级关系
- 缩进不允许使用tab，只允许空格
- 缩进的空格数不重要，只要相同层级的元素左对齐即可
- '#'表示注释
- 字符串无需加引号，如果要加，''与""表示字符串内容 会被 转义/不转义

**数据类型** 

- 字面量：单个的、不可再分的值。date、boolean、string、number、null

- 对象：键值对的集合。map、hash、set、object 

- 数组：一组按次序排列的值。array、list、queue

####   1.2 代码演示

**Pet类**

```java
package com.lsl.code.yaml;
import lombok.Data;
@Data
public class Pet {
    private String name;
    private Double weight;
}
```

**Person类**

 

```java
package com.lsl.code.yaml;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
@Data
@ToString
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
    private String userName;
    private Boolean boss;
    private Date birth;
    private Integer age;
    private Pet pet;
    private String[] interests;
    private List<String> animal;
    private Map<String, Object> score;
    private Set<Double> salarys;
    private Map<String, List<Pet>> allPets;
}
```

**application.yml配置文件**

```yaml
person:
  userName : zhangsan
  boss : true
  birth : 1998/12/9
  age : 18
  interests :
    - 篮球
    - 足球
    - 画画
  animal : [cat,dog]
      #  score:
      #    english: 80
      #    math: 90
  score : {english : 80,math : 90}
  salary :
        - 10000
        - 10000
  Pet :
    name : wwDog
    weight : 20
  allPets :
      sick :
        - {name : mmCat,weight : 5}
        - name : mumuPig
          weight : 50
      health :
        - {name: miumiuSheep,weight: 35}
        - {name: guguBird,weight: 2}
```

```xml
application.yml配置文件里的字符串：
#单引号标注的的字符串会将\n作为字符串输出  双引号会将\n作为换行输出
#即双引号不会转义,单引号会转义
```

**YamlController**

```java
package com.lsl.code.controller;
import com.lsl.code.yaml.Person;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
@RestController
public class YamlController {
    @Resource
    private Person person;
    @RequestMapping("/person")
    public Person person(){
        return person;
    }
}
```

**controller测试**

访问`http://localhost:8080/person`，返回如下结果：

<pre>
{
userName: "zhangsan",
boss: true,
birth: "1998-12-08T16:00:00.000+00:00",
age: 18,
pet: {
name: "wwDog",
weight: 20
},
interests: [
"篮球",
"足球",
"画画"
],
animal: [
"cat",
"dog"
],
score: {
english: 80,
math: 90
},
salarys: null,
allPets: {
sick: [
{
name: "mmCat",
weight: 5
},
{
name: "mumuPig",
weight: 50
}
],
health: [
{
name: "miumiuSheep",
weight: 35
},
{
name: "guguBird",
weight: 2
}
]
}
}    
</pre>



####   1.3配置提示 

自定义的类和配置文件绑定一般没有提示,可以在pom.xml中加上spring-boot-configuration-processor注解。但是提示仅仅用于开发阶段使用，所以打包的时候要忽略，故添加`<exclude>`标签做一个排除，让spring-boot-configuration-processor不出现在打包后的包中

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
 <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-configuration-processor</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

```

   
