package com.lsl.code.ViewThreadState;

// 观察测试线程的状态
public class ViewState {
    public static void main(String[] args) {
        Thread thread = new Thread(()->{
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(".......");
        });
        Thread.State state= thread.getState();
        System.out.println(state);// NEW

        // 观察启动后
        thread.start();//启动
        state = thread.getState();
        System.out.println(state);// RUNNABLE
        // 只要线程未终止，就输出状态
        while(state!=Thread.State.TERMINATED){
            try {
                Thread.sleep(1000);
                state = thread.getState(); // 更新线程状态
                System.out.println(state);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
/*
        NEW
        RUNNABLE
        TIMED_WAITING
        TIMED_WAITING
        .......
        TERMINATED*/
