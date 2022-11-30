## 1.线程优先级

- Java提供一个线程调度器来监控程序中启动后进入就绪状态的所有线程，线程调度器按照优先级决定应该调度哪个线程来执行
- 线程的优先级用数字表示，范围从1~10
  - Thread.MIN_PRIORITY=1;
  - Thread.MAX_PRIORITY = 10;
  - Thread.NORM_PRIORITY = 5;
- 使用以下方式改变或获取优先级
  - getPriority() 
  - setPriority(int xxx)

- 切记 -> 先设置优先级，再启动

```java
package com.lsl.code;
// 测试线程的优先级
class MyPriority implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "-->" + Thread.currentThread().getPriority());
    }
}

public class TestPriority {
    // 主线程默认优先级
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "-->" + Thread.currentThread().getPriority());
        MyPriority myPriority = new MyPriority();
        Thread t1 =new Thread(myPriority,"t1");
        Thread t2 =new Thread(myPriority,"t2");
        Thread t3 =new Thread(myPriority,"t3");
        Thread t4 =new Thread(myPriority,"t4");
        Thread t5 =new Thread(myPriority,"t5");
        Thread t6 =new Thread(myPriority,"t6");
        // 切记 -> 先设置优先级，再启动
        t1.setPriority(Thread.MIN_PRIORITY);
        t1.start();

        t2.setPriority(4);
        t2.start();

        t3.setPriority(Thread.NORM_PRIORITY);
        t3.start();

        t4.setPriority(Thread.MAX_PRIORITY);
        t4.start();

        // 不在范围 -> 报错
        t5.setPriority(-1);
        t5.start();

        // 不在范围 -> 报错
        t6.setPriority(11);
        t6.start();
    }
}

/* 输出：
        main-->5
        t3-->5
        t4-->10
        t2-->4
        t1-->1*/
```



> 优先级低只是意味着获得调度的概率低.并不是优先级低就不会被调用了.这都是看CPU的调度