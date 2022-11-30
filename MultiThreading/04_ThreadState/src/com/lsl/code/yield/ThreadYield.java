package com.lsl.code.yield;

//测试礼让线程
//礼让不一定成功,看cPU心情
public class ThreadYield {
}

class MyYield implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "线程开始执行");
        Thread.yield();// 礼让
        System.out.println(Thread.currentThread().getName() + "线程执行结束");
    }

    public static void main(String[] args) {
        MyYield myYield = new MyYield();
        new Thread(myYield,"a").start();
        new Thread(myYield,"b").start();
    }
}
/*礼让失败输出：
        a线程开始执行
        a线程执行结束
        b线程开始执行
        b线程执行结束*/

/*礼让成功输出：
        b线程开始执行
        a线程开始执行
        b线程执行结束
        a线程执行结束
*/
