package cn.lsl.code.summary;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

// 回顾总结线程的创建
// 1.继承Thread
class MyThread extends Thread{
    @Override
    public void run(){
        System.out.println("MyThread");
    }
}

// 2.实现Runnable接口
class MyThreadRunnable implements Runnable{

    @Override
    public void run() {
        System.out.println("MyThreadRunnable");
    }
}
// 3.实现Callable接口
class MyThreadCallable implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        System.out.println("MyThreadCallable");
        return 100;
    }
}

// 回顾总结线程的创建
public class ThreadNew {
    public static void main(String[] args) {

        // 1.继承Thread
        new MyThread().start();

        // 2.实现Runnable接口
        new Thread(new MyThreadRunnable()).start();

        // 3.实现Callable接口
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new MyThreadCallable());
        new Thread(futureTask).start();

        Integer integer = null;
        try {
            integer = futureTask.get();
            System.out.println(integer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
/*
            MyThread
            MyThreadRunnable
            MyThreadCallable
            100*/
