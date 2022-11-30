package com.lsl.code;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// 测试线程池
public class TestThreadPoll {
    public static void main(String[] args) {
        // 1.创建服务，创建线程池
        // newFixedThreadPool 参数为：线程池的大小
        ExecutorService service = Executors.newFixedThreadPool(10);

        // 执行
        service.execute(new MyThread());
        service.execute(new MyThread());
        service.execute(new MyThread());
        service.execute(new MyThread());

        // 2.关闭连接
        service.shutdown();
    }
}
class MyThread implements Runnable{
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            System.out.println(Thread.currentThread().getName()+ "--->" + i);
        }
    }
}