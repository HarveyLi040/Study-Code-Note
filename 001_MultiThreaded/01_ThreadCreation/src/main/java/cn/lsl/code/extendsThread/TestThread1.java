package cn.lsl.code.extendsThread;
/**
 * 创建线程方式一:继承Thread类，重写run()方法，调用start开启线程
 * 自定义线程类继承Thread类
 *         1.重写run()方法，编写线程执行体
 *         2.创建线程对象
 *         3.调用start()方法启动线程
 * 线程不一定立即执行，CPU安排调度
 */
public class TestThread1 extends Thread{
    @Override
    public void run(){
        //run方法线程体
        for(int i=0;i<200;i++){
            System.out.println("我在看代码---"+i);
        }
    }

    //main线程->主线程
    public static void main(String[] args) {
        // 创建一个线程对象
        TestThread1 testThread1 =new TestThread1();
        // 调用start()方法开启线程
        testThread1.start();
        for (int i = 0; i < 200; i++) {
            System.out.println("我在学习多线程---"+i);
        }
    }
}
/**
 * 现象：主线程和子线程交替执行
 * 总结:注意,线程开启不一定立即执行,由CPU调度执行
 */
