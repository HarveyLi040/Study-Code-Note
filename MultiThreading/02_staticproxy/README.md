## 1.静态代理

**代理的场景：**

- 你:真实角色
- 婚庆公司:代理你，帮你处理结婚的事结婚:实现都实现结婚接口即可

**代码演示 -> 实现静态代理对比Thread：**



```java
package com.lsl.code;
interface Marry{
    public void happyMarry();
}

class You implements Marry{
    @Override
    public void happyMarry() {
        System.out.println("我要结婚了，好hi呦");
    }
}

// 代理角色->婚庆公司，帮助你结婚
class WeddingCompany implements Marry{
    // 婚庆需要有你这个人 ， 代理对象需要代理一个真实对象
    private Marry you;
    public WeddingCompany(Marry you){
        this.you = you;
    }
    @Override
    public void happyMarry() {
        before();
        this.you.happyMarry();// 真实对象
        after();
    }

    private void before() {
        System.out.println("结婚之前，布置洞房");
    }
    private void after() {
        System.out.println("结婚之后，催你收钱");
    }
}

// 静态代理
public class StaticProxy {
    public static void main(String[] args) {
        //代理对象  代理 真实对象
        You you = new You();
        // you.happyMarry();
        new WeddingCompany(you).happyMarry();
    }
}
```

**对比 -> 线程中的静态代理模式：**

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/004.png)

- 线程类和Thread 类都实现了 Runnable 接口,而Runnable 接口中有run() 方法
- You类和WeddingCompany都实现了Marry接口，而Marry接口中有happyMarry方法

**静态代理模式总结：**

- 真实对象和代理对象都要实现同一个接口
- 代理对象要代理真实角色
- 好处:
  - 代理对象可以做很多真实对象做不了的事情
  -  真实对象专注做自己的事情





