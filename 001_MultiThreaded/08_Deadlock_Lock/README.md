## 1.死锁

- 多个线程各自占有一些共享资源﹐并且互相等待其他线程占有的资源才能运行,而导致两个或者多个线程都在等待对方释放资源,都停止执行的情形

- 某一个同步块同时拥有“两个以上对象的锁”时，就可能会发生“死锁”的问题

-  死锁:多个线程互相占有对方需要的资源,然后形成僵持

#### 1.1 模拟死锁

```java
package com.lsl.code;

// 口红
class Lipstick{
}
// 镜子
class Mirror{
}

class Makeup extends Thread{
    //需要的资源只有一份，用static来保证只有一份
    static Lipstick lipstick = new Lipstick();
    static Mirror mirror = new Mirror();
    // 选择
    int choice;// 选择
    String girlName;// 使用化妆品的人
    Makeup(int choice,String girlName){
        this.choice = choice;
        this.girlName = girlName;
    }
    @Override
    public void run(){
        // 化妆
        try {
            makeup();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // 化妆,互相持有对方的锁，就是需要拿到对方的资源
    private void makeup() throws InterruptedException {
        if( choice==0) {
            // 获得口红的锁
            synchronized(lipstick) {
                System.out.println (this.girlName+"获得口红的锁");
                Thread.sleep( 1000) ;
                synchronized (mirror){ // 一秒钟后想获得镜子
                    System.out.println (this.girlName+"获得镜子的锁");
                }
            }
        }else{
            synchronized (mirror){//获得镜子的锁
                System.out.println (this.girlName+"获得镜子的锁");
                Thread.sleep( 2000);
                synchronized (lipstick){ //一秒钟后想获得口红
                    System.out.println (this.girlName+"获得口红的锁");
                }
            }
        }
    }
}
// 死锁:多个线程互相占有对方需要的资源,然后形成僵持
public class Deadlock {
    public static void main(String[] args) {
        Makeup g1 = new Makeup(0,"灰姑娘");
        Makeup g2 = new Makeup(1,"白雪公主");
        g1.start();
        g2.start();
    }

}
/*出现死锁：
        白雪公主获得镜子的锁
        灰姑娘获得口红的锁*/
```



#### 1.2 解决死锁



```java
package com.lsl.code.SolveDeadLock;
// 口红
class Lipstick{
}
// 镜子
class Mirror{
}

class Makeup extends Thread{
    //需要的资源只有一份，用static来保证只有一份
    static Lipstick lipstick = new Lipstick();
    static Mirror mirror = new Mirror();
    // 选择
    int choice;// 选择
    String girlName;// 使用化妆品的人
    Makeup(int choice,String girlName){
        this.choice = choice;
        this.girlName = girlName;
    }
    @Override
    public void run(){
        // 化妆
        try {
            makeup();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // 化妆,互相持有对方的锁，就是需要拿到对方的资源
    private void makeup() throws InterruptedException {
        if(choice==0){
            // 获得口红的锁
            synchronized(lipstick) {
                System.out.println (this.girlName+"获得口红的锁");
                Thread.sleep( 1000) ;
            }
            synchronized (mirror){ // 一秒钟后想获得镜子
                System.out.println (this.girlName+"获得镜子的锁");
            }
        }else{
            synchronized (mirror){//获得镜子的锁
                System.out.println (this.girlName+"获得镜子的锁");
                Thread.sleep( 2000);
            }
            synchronized (lipstick){ //一秒钟后想获得口红
                System.out.println (this.girlName+"获得口红的锁");
            }
        }
    }
}
// 死锁:多个线程互相占有对方需要的资源,然后形成僵持
public class  SolveDeadLock  {
    public static void main(String[] args) {
        Makeup g1 = new Makeup(0,"灰姑娘");
        Makeup g2 = new Makeup(1,"白雪公主");
        g1.start();
        g2.start();
    }

}
/*没出现死锁:
                白雪公主获得镜子的锁
                灰姑娘获得口红的锁
                白雪公主获得口红的锁
                灰姑娘获得镜子的锁*/
```



#### 1.3 死锁避免方法

**产生死锁的四个必要条件:**

1. 互斥条件:一个资源每次只能被一个进程使用
2. 请求与保持条件:一个进程因请求资源而阻塞时，对已获得的资源保持不放
3. 不剥夺条件:进程已获得的资源，在未使用完之前，不能强行剥夺
4. 循环等待条件∶若干进程之间形成一种头尾相接的循环等待资源关系

上面列出了死锁的四个必要条件，只要想办法破其中的任意一个或多个条件就可以避免死锁发生



## 2.Lock(锁)

**Lock(锁)**

- 从JDK 5.0开始，Java提供了更强大的线程同步机制——通过显式定义同步锁对象来实现同步。同步锁使用Lock对象充当

- java.util.concurrent.locks.Lock接口是控制多个线程对共享资源进行访问的工具。锁提供了对共享资源的独占访问，每次只能有一个线程对Lock对象加锁，线程开始访问共享资源之前应先获得Lock对象
- ReentrantLock类实现了Lock，它拥有与synchronized相同的并发性和内存语义，在实现线程安全的控制中，比较常用的是ReentrantLock，可以显式加锁、释放锁。

**ReentrantLock使用**



![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/008.png)

```java
package com.lsl.code.Lock;

import java.util.concurrent.locks.ReentrantLock;

class TestLock2 implements Runnable{
    int ticketNum = 10;

    // 定义lock锁
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
          while(true){
              try{
                  lock.lock(); // 加锁
                  if(ticketNum>0){
                      try {
                          Thread.sleep(1000);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                      System.out.println(ticketNum--);
                  }else{
                      break;
                  }
              }finally {
                  // 解锁
                  lock.lock();
              }
          }
    }
}

// 测试Lock锁
public class TestLock {
    public static void main(String[] args) {
        TestLock2 testLock2 = new TestLock2();
        new Thread(testLock2).start();
        new Thread(testLock2).start();
        new Thread(testLock2).start();
    }
}
```



**synchronized 与Lock的对比**

- Lock是显式锁（手动开启和关闭锁，别忘记关闭锁) 。synchronized是隐式锁，出了作用域自动释放
- Lock只有代码块锁,synchronized有代码块锁和方法锁
- 使用Lock锁，JVM将花费较少的时间来调度线程，性能更好。并且具有更好的扩展性(提供更多的子类)
- 优先使用顺序:
  - Lock >同步代码块（已经进入了方法体，分配了相应资源)>同步方法（在方法体之外)