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
