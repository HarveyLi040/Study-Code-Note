## 1.Lambda表达式概述


- 入是希腊字母表中排序第十一位的字母，英语名称为Lambda

- 为什么要使用lambda表达式

  - 避免匿名内部类定义过多

  - 可以让你的代码看起来很简洁

  - 去掉了一堆没有意义的代码，只留下核心的逻辑

- 语法：

  - (params) ->expression[表达式]

  - [[(params) -> statement[语句]

  - [(params) -> { statements }

- 例子：

  - a-> System.out.println ("i like lambda--→"+a) ;

  - new Thread (()->System.out.println("多线程学习。。。。")) .start();

- Lamda表达式实质属于函数式编程的概念
  - 理解Functional lnterface(函数式接口)是学习Java8 lambda表达式的关键所在
  - 对于函数式接口，可以通过lambda表达式来创建该接口的对象
  - 函数式接口的定义:
    - 任何接口，如果只包含唯一一个抽象方法，那么它就是一个函数式接口

```java
public interface Runnable {
    public abstract void run();
}
```

## 2.lambda表达式推导(演化)

**1.各种class类实现接口的写法(可以视为lambda表达式的一种演化)：**

- 接口指向实现类->静态内部类->局部内部类->匿名内部类->lambda表达式简化

```java
package com.lsl.code;

// 定义一个函数式接口
interface Like{
    void lambda();
}

// 实现类
class ILike implements Like{
    @Override
    public void lambda() {
        System.out.println("I like lambda1");
    }
}

// 推导lamda表达式
public class TestLambda {

    // 静态内部类
    static class ILike2 implements Like{
        @Override
        public void lambda() {
            System.out.println("I like lambda2");
        }
    }

    public static void main(String[] args) {
        // 1.new一个接口指向实现类
        Like like1 = new ILike();
        like1.lambda();

       // 2.静态内部类
        Like like2 = new ILike2();
        like2.lambda();

        // 3.局部内部类
        class ILike3 implements Like{
            @Override
            public void lambda() {
                System.out.println("I like lambda3");
            }
        }
        Like like3 = new ILike3();
        like3.lambda();

        // 4.匿名内部类，没有类的名称,必须惜助接口或者父类
        Like like4 = new Like() {
             @Override
             public void  lambda(){
                 System.out.println("I like lambda4");
             }
        };
        like4.lambda();

        // 5.利用lambda表达式简化
        Like like5 = ()->{
            System.out.println("I like lambda5");
        };
        like5.lambda();
    }
}
/** 输出:
 * I like lambda1
 * I like lambda2
 * I like lambda3
 * I like lambda4
 * I like lambda5
 */
```



## 3.lambda表达式语法



```java
package com.lsl.code;

interface Love{
    void love(int a);
}
class Ilove implements Love{
    @Override
    public void love(int a) {
        System.out.println("I love you-->" + a);
    }
}

public class TestLambdaTwo {
    public static void main(String[] args) {
        Love love1 = new Ilove();
        love1.love(1);

        Love love2 = new Ilove(){
            @Override
            public void love(int a){
                System.out.println("I love you-->" + a);
            }
        };
        love2.love(2);
        // 简化: 采用lambda表达式
        Love love3 = (int a)->{
                System.out.println("I love you-->" + a);
        };
        love3.love(3);

        // 简化: 去掉参数类型
        Love love4 = (a)->{
            System.out.println("I love you-->" + a);
        };
        love4.love(4);

        // 简化: 去掉参数的括号
        Love love5 = a->{
            System.out.println("I love you-->" + a);
        };
        love5.love(5);

        // 简化: 去掉函数体的花括号
        Love love6 = a-> System.out.println("I love you-->" + a);
        love6.love(6);
    }
}

//总结:
//    lambda表达式只能有一行代码的情况下才能简化成为一行
//    如果有多行，那么就用代码块包裹。前提是接口为函数式接口
//    多个参数也可以去掠参数类型，要去掠就都去控,必须加上括号
```
