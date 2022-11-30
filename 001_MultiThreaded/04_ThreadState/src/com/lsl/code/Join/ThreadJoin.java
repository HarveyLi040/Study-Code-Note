package com.lsl.code.Join;

// 测试join方法 ->想象为插队
public class ThreadJoin implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("线程vip来了" + i);
        }
    }

    public static void main(String[] args) {
        // 启动我们的线程
        ThreadJoin threadJoin = new ThreadJoin();
        Thread thread = new Thread(threadJoin);
        thread.start();
        
        // 主线程
        for (int i = 0; i < 1000; i++) {
            if(i==200){
                try {
                    thread.join();//插队
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("main线程" + i);
        }

    }
}
/*插队现象:
        main线程196
        main线程197
        main线程198
        main线程199
        线程vip来了0
        线程vip来了1
        线程vip来了2*/



