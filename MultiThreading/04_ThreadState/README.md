## 1.线程状态

**线程状态：**

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/005ThreadState.png)





**线程状态：**

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/006ThreadState.png)



## 2.线程状态切换

#### 2.1 线程状态切换方法汇总

| 线程方法                       | 说明                                       |
| ------------------------------ | ------------------------------------------ |
| setPriority(int newPriority)   | 更改线程的优先级                           |
| static void sleep(long millis) | 在指定的毫秒数内让当前正在执行的线程休眠   |
| void join()                    | 等待该线程终止                             |
| static void yield()            | 暂停当前正在执行的线程对象，并执行其他线程 |
| void interrupt()               | 中断线程，建议不要使用此方法               |
| boolean isAlive()              | 测试线程是否处于活动状态                   |

#### 2.2 停止线程



- 不推荐使用JDK提供的stop().destroy()方法【已废弃】

- 推荐线程自己停止下来

- 建议使用一个标志位进行终止变量当flag=false，则终止线程运行



```java
package com.lsl.code.stop;
//测试stop
//1.建议线程正常停止--->利用次数,不建议死循环
//2.建议使用标志位--->设置一个标志位
//3.不要使用stop或者destroy等过时或者JDK不建议使用的方法
public class ThreadStop implements Runnable{
    // 设置一个标志位
    private boolean flag = true;
    @Override
    public void run() {
        int i = 0;
        while(flag){
            System.out.println("ThreadStop_" + i++);
        }
    }

    // 设置一个公开的方法停止线程，转换标志位
    public void stop(){
        this.flag = false;
    }
    public static void main(String[] args) {
           ThreadStop threadStop = new ThreadStop();
           new Thread(threadStop).start();
           int i = 0;
           for (i = 0; i < 1000; i++) {
            System.out.println("main_" + i);
               if(i==900){
                   // 调用自己写的stop方法,切换标志位让线程停止
                   threadStop.stop();
                   System.out.println("线程该停止了...");
               }
           }
    }
}
```



#### 2.3 线程休眠

- sleep(时间)指定当前线程阻塞的毫秒数
- sleep存在异常InterruptedException
- **sleep时间到达后线程进入就绪状态**
- sleep可以模拟网络延时，倒计时等
- 每一个对象都有一个锁
- sleep不会释放锁



演示:模拟倒计时

```java
package com.lsl.code.sleep;

// 模拟时钟倒计时
public class ThreadSleep {
    public static void tenDown() throws InterruptedException {
        int num = 10 ;
        while(true){
            Thread.sleep(1000);
            System.out.println(num--);
            if(num<=0){
                break;
            }
        }
    }

    public static void main(String[] args) {
        try {
            tenDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

- 获取系统当前时间

```java
package com.lsl.code.sleep;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestSleep {
    public static void main(String[] args) {
        // 打印系统当前时间
        Date startTime = new Date(System.currentTimeMillis());// 获取系统当前时间
        while(true){
            try {
                Thread.sleep(1000);
                System.out.println(new SimpleDateFormat("HH:mm:ss").format(startTime));
                startTime = new Date(System.currentTimeMillis());// 获取系统当前时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```



#### 2.4 线程礼让

- 
  礼让线程，让当前正在执行的线程暂停，但不阻塞

- 将线程从运行状态转为就绪状态

- 让cpu重新调度，礼让不一定成功!看CPU心情



```java
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
```

#### 2.5 Jion

- Join合并线程，待此线程执行完成后，再执行其他线程，其他线程阻塞
- 可以想象成插队
  

```java
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
```

## 3.线程状态的观测

```
public static enum Thread.State extends Enum<Thread.State>
线程状态。线程可以处于以下状态之—:
        NEW -> 尚未启动的线程处于此状态
        RUNNABLE -> 在Java虚拟机中执行的线程处于此状态
        BLOCKED -> 被阻塞等待监视器锁定的线程处于此状态
        WAITING -> 正在等待另一个线程执行特定动作的线程处于此状态
        TIMED_WAITING -> 正在等待另一个线程执行动作达到指定等待时间的线程处于此状态
        TERMINATED -> 已退出的线程处于此状态
一个线程可以在给定时间点处于一个状态。这些状态是不反映任何操作系统线程状态的虚拟机状态
```



```java
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
```