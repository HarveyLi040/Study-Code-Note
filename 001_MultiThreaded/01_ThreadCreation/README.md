## 1.线程简介

**普通方法调用和多线程**

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/001.png)

**Process与Thread**

- 说起进程，就不得不说下程序。程序是指令和数据的有序集合，其本身没有任何运行的含义，是一个静态的概念
- 而进程则是执行程序的一次执行过程，它是一个动态的概念。是系统资源分配的单位
- 通常在一个进程中可以包含若干个线程，当然一个进程中至少有一个线程，不然没有存在的意义。线程是CPU调度和执行的的单位
- **注意:**很多多线程是模拟出来的，真正的多线程是指有多个cpu，即多核，如服务器。如果是模拟出来的多线程，即在一个cpu的情况下，在同一个时间点，cpu只能执行一个代码，因为切换的很快，所以就有同时执行的错觉

**核心概念：**

- 线程就是独立的执行路径
- 在程序运行时，即使没有自己创建线程，后台也会有多个线程，如主线程，gc线程;main()称之为主线程，为系统的入口，用于执行整个程序
- 在一个进程中，如果开辟了多个线程，线程的运行由调度器安排调度，调度器是与操作系统紧密相关的，先后顺序是不能人为的干预的
- 对同一份资源操作时，会存在资源抢夺的问题，需要加入并发控制;线程会带来额外的开销，如cpu调度时间，并发控制开销
- 每个线程在自己的工作内存交互，内存控制不当会造成数据不一致

## 2.线程创建方式

**创建方式: Thread 、Runnable、Callable**

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/002.png)



#### 2.1 继承Thread类:

案例一：主线程和子线程交替执行

```java
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
```

案例二：练习Thread，实现多线程同步下载图片

```java
package cn.lsl.code.extendsThread;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;

// 练习Thread，实现多线程同步下载图片
public class TestThread2 extends Thread{
    private String url;
    private String fileName;
    public TestThread2(String url,String fileName){
        this.url = url;
        this.fileName = fileName;
    }
    @Override
    public void run(){
        WebDownloader webDownloader = new WebDownloader();
        webDownloader.downloader(url,fileName);
        System.out.println("下载了文件名为"+fileName);
    }

    public static void main(String[] args) {
        TestThread2 t1 = new TestThread2("https://blog.kuangstudy.com/usr/themes/handsome/usr/img/sj/1.jpg","1.jpg");
        TestThread2 t2 = new TestThread2("https://blog.kuangstudy.com/usr/themes/handsome/usr/img/sj/2.jpg","2.jpg");
        TestThread2 t3 = new TestThread2("https://blog.kuangstudy.com/usr/themes/handsome/usr/img/sj/3.jpg","3.jpg");
        // t1下载
        t1.start();
        // t2下载
        t2.start();
        // t3下载
        t3.start();
    }
}

//下载器
class WebDownloader{
      // 下载方法
    public void downloader(String url,String fileName){
        try {
       // 依赖于<!-- https://mvnrepository.com/artifact/commons-io/commons-io -->
            FileUtils.copyURLToFile(new URL(url),new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

运行main函数，控制台输出：

> 下载了文件名为2.jpg
>
> 下载了文件名为3.jpg
>
> 下载了文件名为1.jpg

方式一总结：

- 子类继承Thread类具备多线程能力

- 启动线程:子类对象. start()
- <font color=red>不建议使用:避免OOP单继承局限性</font>

#### 2.2 实现Runnable接口:

案例一:主线程和子线程交替执行

```java
package cn.lsl.code.implementsRunnable;
// 创建线程方式2︰实现runnable接口,重写run方法，执行线程需要丢入runnable接口实现类。调用start方法
public class TestRunnableThread1 implements Runnable{
    @Override
    public void run(){
        //run方法线程体
        for(int i=0;i<200;i++){
            System.out.println("我在看代码---"+i);
        }
    }
    //main线程->主线程
    public static void main(String[] args) {
        // 创建runnbale接口的实现类对象
        TestRunnableThread1 testRunnableThread1 = new TestRunnableThread1();
        // 创建线程对象,通过线程对象来开启我们的线程,代理
//        Thread thread = new Thread(testRunnableThread1);
//        thread.start();
        new Thread(testRunnableThread1).start();
        for (int i = 0; i < 200; i++) {
            System.out.println("我在学习多线程---"+i);
        }
    }
}
```

案例二：练习Runnable，实现多线程同步下载图片

```java
package cn.lsl.code.implementsRunnable;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TestRunnableThread2 implements Runnable{
    private String url;
    private String fileName;
    public TestRunnableThread2(String url,String fileName){
        this.url = url;
        this.fileName = fileName;
    }
    @Override
    public void run(){
        WebDownloader webDownloader = new WebDownloader();
        webDownloader.downloader(url,fileName);
        System.out.println("下载了文件名为"+fileName);
    }

    public static void main(String[] args) {
        TestRunnableThread2 t1 = new TestRunnableThread2("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/001.png","1.jpg");
        TestRunnableThread2 t2 = new TestRunnableThread2("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/002.png","2.jpg");
        TestRunnableThread2 t3 = new TestRunnableThread2("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/003.png","3.jpg");
        // t1下载
        new Thread(t1).start();
        // t2下载
        new Thread(t2).start();
        // t3下载
        new Thread(t3).start();
    }
}

//下载器
class WebDownloader{
    // 下载方法
    public void downloader(String url,String fileName){
        try {
            // 依赖于<!-- https://mvnrepository.com/artifact/commons-io/commons-io -->
            FileUtils.copyURLToFile(new URL(url),new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```



案例三：同一个对象被多个线程使用

```java
package cn.lsl.code.implementsRunnable;
// 多个线程同时操作一个对象->买火车票的例子
// 继承Runnable->方便同一个对象被多个线程使用
public class ticketRunnableThread implements Runnable{
    // 票数
    private int ticketNums = 10;

    @Override
    public void run() {
        while(true){
            if(ticketNums<=0){
                break;
            }
            System.out.println(Thread.currentThread().getName() + "拿到了第"+ ticketNums-- +"张票");
        }
    }
    public static void main(String[] args) {
        ticketRunnableThread ticket = new ticketRunnableThread();
        new Thread(ticket,"小明").start();
        new Thread(ticket,"老师").start();
        new Thread(ticket,"黄牛党").start();
    }
}
// 此程序存在的问题:多个线程操作同一个资源的情况下,线程不安全，数据可能紊乱(后续讲线程同步再优化)
```

输出：

> 老师拿到了第8张票
>
> 黄牛党拿到了第9张票
>
> 小明拿到了第10张票
>
> 黄牛党拿到了第6张票
>
> 老师拿到了第7张票
>
> 黄牛党拿到了第4张票
>
> 小明拿到了第5张票
>
> 黄牛党拿到了第2张票
>
> 老师拿到了第3张票
>
> 小明拿到了第1张票



案例四:龟兔赛跑-Race

1. 首先来个赛道距离，然后要离终点越来越近
2. 判断比赛是否结束
3. 打印出胜利者
4. 龟兔赛跑开始
5. 故事中是乌龟赢的，兔子需要睡觉，所以我们来模拟兔子睡觉
6. 终于，乌龟赢得比赛I

```java
package cn.lsl.code.implementsRunnable;
// 模拟龟兔赛跑
public class Race implements Runnable{
    // 胜利者
    private static String winner;
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {

            // 模拟兔子睡觉
            if("兔子".equals(Thread.currentThread().getName()) && i%20==0){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 判断比赛是否结束
            if(gameOver(i)){
               break;
            }
            System.out.println(Thread.currentThread().getName()+"-->跑了"+ i + "步");
        }
    }
    // 判断是否完成比赛
    private boolean gameOver(int steps){
        // 判断是否有胜利者
        if(winner!=null){
            return true;
        }else if (steps>=100){
            winner = Thread.currentThread().getName();
            System.out.println("winner is" + winner);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Race race = new Race();
        new Thread(race,"兔子").start();
        new Thread(race,"乌龟").start();
    }
}
```



方式二总结：

- 实现接口Runnable具有多线程能力
- 启动线程:传入目标对象+Thread对象.start()
- <font color=red>推荐使用:避免单继承局限性，灵活方便，方便同一个对象被多个线程使用</font>

#### 2.3 实现Callable接口

Callable创建线程一般步骤：

1. 实现Callable接口，需要返回值类型

2. 重写call方法，需要抛出异常

3. 创建目标对象

4. 创建执行服务:ExecutorService ser = Executors.newFixedThreadPool(1);

5. 提交执行:Future<Boolean> result1 = ser.submit(t1);

6. 获取结果: boolean r1 = result1.get()

7. 关闭服务:ser.shutdownNow();

   

案例演示:利用callable改造下载图片案例

```java
package cn.lsl.code.implementsCallable;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.*;

public class CallableThread implements Callable<Boolean> {
    private String url;
    private String fileName;
    public CallableThread(String url,String fileName){
        this.url = url;
        this.fileName = fileName;
    }

    @Override
    public Boolean call() throws Exception {
       WebDownloader webDownloader = new WebDownloader();
        webDownloader.downloader(url,fileName);
        System.out.println("下载了文件名为"+fileName);
        return true;
    }
    public static void main(String[] args) {
        // 创建目标对象
        CallableThread t1 = new CallableThread("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/001.png","1.jpg");
        CallableThread t2 = new CallableThread("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/002.png","2.jpg");
        CallableThread t3 = new CallableThread("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/003.png","3.jpg");

        // 创建执行服务
        ExecutorService ser = Executors.newFixedThreadPool(3);

        // 提交执行
        Future<Boolean> r1 = ser.submit(t1);
        Future<Boolean> r2 = ser.submit(t2);
        Future<Boolean> r3 = ser.submit(t3);

        // 获取结果
        try {
            boolean rs1 = r1.get();
            boolean rs2 = r2.get();
            boolean rs3 = r3.get();
            System.out.println(rs1);
            System.out.println(rs2);
            System.out.println(rs3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // 关闭服务
        ser.shutdownNow();
    }
}

//下载器
class WebDownloader{
    // 下载方法
    public void downloader(String url,String fileName){
        try {
            // 依赖于<!-- https://mvnrepository.com/artifact/commons-io/commons-io -->
            FileUtils.copyURLToFile(new URL(url),new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```



## 3.线程创建方式总结



```java
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
```
