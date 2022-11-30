## 1.线程同步机制

**并发**

- 同一个对象被多个线程同时操作

**线程同步**

- 现实生活中,我们会遇到”同一个资源，多个人都想使用”的问题，比如,食堂排队打饭,每个人都想吃饭,最天然的解决办法就是排队一个个来

- 处理多线程问题时,多个线程访问同一个对象，并且某些线程还想修改这个对象．这时候我们就需要线程同步

- 线程同步其实就是一种等待机制，多个需要同时访问此对象的线程进入这个对象的等待池形成队列,等待前面线程使用完毕，下一个线程再使用
- 线程同步发生场景   --> 多个线程操作同一个资源

**队列和锁**

- 每一个对象都有一个锁

- 队列+锁  保证线程同步的安全性

**线程同步的安全性**

- 由于同一进程的多个线程共享同一块存储空间，在带来方便的同时,也带来了访问冲突问题

- 为了保证数据在方法中被访问时的正确性,在<font color=red>访问时加入锁机制synchronized，当一个线程获得对象的排它锁,独占资源﹐其他线程必须等待，使用后释放锁即可</font>.存在以下问题
  - 一个线程持有锁会导致其他所有需要此锁的线程挂起
  - 在多线程竞争下，加锁，释放锁会导致比较多的上下文切换和调度延时,引起性能问题
  - 如果一个优先级高的线程等待一个优先级低的线程释放锁会导致优先级倒置，引起性能问题

## 2. 三大线程不安全案例

模拟买票：

```java
package com.lsl.code;

class BuyTicket implements Runnable{
    // 票
    private int ticketNums = 10;
    boolean flag = true; // 外部停止方式
    @Override
    public void run() {
        // 买票
      while(flag){
          buy();
      }
    }
    private void buy(){
        // 判断是否有票
        if(ticketNums<=0){
            flag = false;
            return;
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 买票
        System.out.println(Thread.currentThread().getName() + "拿到了" + ticketNums--);
    }
}

// 不安全的买票
public class UnsafeBuyTicket {
    public static void main(String[] args) {
        BuyTicket buyTicket = new BuyTicket();
        new Thread(buyTicket,"苦逼的小明").start();
        new Thread(buyTicket,"有钱的大佬").start();
        new Thread(buyTicket,"可恶的黄牛党").start();
    }
}
/*线程不安全->出现负数
            有钱的大佬拿到了9
            苦逼的小明拿到了10
            可恶的黄牛党拿到了8
            有钱的大佬拿到了7
            苦逼的小明拿到了6
            可恶的黄牛党拿到了5
            有钱的大佬拿到了4
            苦逼的小明拿到了3
            可恶的黄牛党拿到了3
            有钱的大佬拿到了2
            苦逼的小明拿到了1
            可恶的黄牛党拿到了0
            有钱的大佬拿到了-1*/
```



模拟银行取款：

```java
package com.lsl.code;

// 账户
class Account{
    int money;  // 余额
    String name;// 卡名
    public Account(int money,String name){
        this.money = money;
        this.name = name;
    }
}

// 银行模拟取钱
class Drawing extends Thread{
    // 账户
    Account account;
    // 取了多少钱
    int drawingMoney;
    // 现在手里有多少钱
    int nowMoney;
    public Drawing(Account account,int drawingMoney ,String name){
        super(name); // 继承父类 -> 线程名字
        this.account = account;
        this.drawingMoney = drawingMoney;
    }

    //取钱
    @Override
    public void run(){
        // 判断有没有钱
        if (account.money-drawingMoney<0){
            System.out.println(Thread.currentThread ().getName()+"钱不够,取不了");
            return;
        }
        // sleep可以放大问题的发生性
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 卡内余额=余额–你取的钱
        account.money = account.money - drawingMoney;//你手里的钱
        nowMoney = nowMoney + drawingMoney;
        System.out.println(account.name+"余额为:"+account.money) ;
        //Thread.currentThread().getNawe() 等价于 this.getName(
        System.out.println(this.getName()+"手里的钱: "+nowMoney) ;
        }
}

// 不安全的取钱
// 两个人去银行取钱，账户
public class UnsafeBank {
    public static void main(String[] args) {
        Account account = new Account(100,"结婚基金");
        Drawing you = new Drawing(account,50,"你");
        Drawing girlFriend = new Drawing(account,100,"女朋友");
        you.start();
        girlFriend.start();
    }
}

/*线程不安全:
        结婚基金余额为:-50
        结婚基金余额为:-50
        你手里的钱: 50
        女朋友手里的钱: 100*/
```



线程不安全的集合：如ArrayList

```java
package com.lsl.code;

import java.util.ArrayList;
import java.util.List;

// 线程不安全的集合：如ArrayList
public class UnsafeList {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            new Thread(()->{
                list.add(Thread.currentThread().getName());}
            ).start();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(list.size());//输出9987 -> 线程不安全
    }
}
```

## 3.同步方法

**同步方法**

- 由于我们可以通过private关键字来保证数据对象只能被方法访问﹐所以我们只需要针对方法提出一套机制，这套机制就是synchronized关键字，它包括两种用法:<font color=red>synchronized方法和synchronized块</font>>
  - 同步方法: public synchronized void method(int args)
- synchronized方法控制对“对象”的访问，每个对象对应一把锁，每个synchronized方法都必须获得调用该方法的对象的锁才能执行，否则线程会阻塞，方法一旦执行，就独占该锁，直到该方法返回才释放锁，后面被阻塞的线程才能获得这个锁,继续执行
  - 缺陷:若将一个大的方法声明为synchronized将会影响效率

**同步方法弊端**

![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/007.png)

**同步方法案例**

线程安全的买票案例：

```java
package com.lsl.code.ThreadSafe;

class BuyTicket implements Runnable{
    // 票
    private int ticketNums = 10;
    boolean flag = true; // 外部停止方式
    @Override
    public void run() {
        // 买票
      while(flag){
          buy();
      }
    }
    // synchronized 同步方法， 锁的是this（默认锁this）
    private synchronized void buy(){
        // 判断是否有票
        if(ticketNums<=0){
            flag = false;
            return;
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 买票
        System.out.println(Thread.currentThread().getName() + "拿到了" + ticketNums--);
    }
}

// 安全的买票
public class safeBuyTicket {
    public static void main(String[] args) {
        BuyTicket buyTicket = new BuyTicket();
        new Thread(buyTicket,"苦逼的小明").start();
        new Thread(buyTicket,"有钱的大佬").start();
        new Thread(buyTicket,"可恶的黄牛党").start();
    }
}
/*线程安全：
            有钱的大佬拿到了10
            有钱的大佬拿到了9
            可恶的黄牛党拿到了8
            可恶的黄牛党拿到了7
            可恶的黄牛党拿到了6
            可恶的黄牛党拿到了5
            可恶的黄牛党拿到了4
            可恶的黄牛党拿到了3
            可恶的黄牛党拿到了2
            可恶的黄牛党拿到了1
            */
```



## 4.同步代码块



- 同步块: synchronized (Obj ){ }

- Obj称之为**同步监视器**
  - Obj可以是任何对象，但是推荐使用共享资源作为同步监视器
  - 同步方法中无需指定同步监视器，因为同步方法的同步监视器就是this ,就是这个对象本身，或者是class [反射中讲解]
- 同步监视器的执行过程
  1. 第一个线程访问，锁定同步监视器，执行其中代码
  2. 第二个线程访问，发现同步监视器被锁定，无法访问
  3. 第一个线程访问完毕，解锁同步监视器
  4. 第二个线程访问，发现同步监视器没有锁，然后锁定并访问

- 同步代码块解决问题关键：<font color=red>锁的对象就是变化的量（需要增删改的对象）</font>

**同步代码块案例**

线程安全的银行取款：

```java
package com.lsl.code.ThreadSafe;

// 账户
class Account{
    int money;  // 余额
    String name;// 卡名
    public Account(int money,String name){
        this.money = money;
        this.name = name;
    }
}

// 银行模拟取钱
class Drawing extends Thread{
    // 账户
    Account account;
    // 取了多少钱
    int drawingMoney;
    // 现在手里有多少钱
    int nowMoney;
    public Drawing(Account account,int drawingMoney ,String name){
        super(name); // 继承父类 -> 线程名字
        this.account = account;
        this.drawingMoney = drawingMoney;
    }
    //取钱
    @Override
    public void run(){
        // 锁的对象就是变化的量,需要增删改的对象
        synchronized (account){
            // 判断有没有钱
            if (account.money-drawingMoney<0){
                System.out.println(Thread.currentThread ().getName()+"钱不够,取不了");
                return;
            }
            // sleep可以放大问题的发生性
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 卡内余额=余额–你取的钱
            account.money = account.money - drawingMoney;//你手里的钱
            nowMoney = nowMoney + drawingMoney;
            System.out.println(account.name+"余额为:"+account.money) ;
            //Thread.currentThread().getNawe() 等价于 this.getName(
            System.out.println(this.getName()+"手里的钱: "+nowMoney) ;
        }
    }
}

// 不安全的取钱
// 两个人去银行取钱，账户
public class safeBank {
    public static void main(String[] args) {
        Account account = new Account(100,"结婚基金");
        Drawing you = new Drawing(account,50,"你");
        Drawing girlFriend = new Drawing(account,100,"女朋友");
        you.start();
        girlFriend.start();
    }
}

/*线程安全:
        结婚基金余额为:0
        女朋友手里的钱: 100
        你钱不够,取不了*/
```

线程安全集合：

```java
package com.lsl.code.ThreadSafe;

import java.util.ArrayList;
import java.util.List;

// 线程不安全的集合：如ArrayList
public class safeList {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            new Thread(()->{
                synchronized (list){
                    list.add(Thread.currentThread().getName());
                }
            }).start();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(list.size());//输出10000 -> 线程安全
    }
}
```

## 5.JUC 

测试JUC安全类型的集合

```java
package com.lsl.code.JUCList;


import java.util.concurrent.CopyOnWriteArrayList;

// 测试JUC安全类型的集合
public class TestJUC {
    public static void main(String[] args) {
        CopyOnWriteArrayList<String>list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 10000; i++) {
            new Thread(()->{
                list.add(Thread.currentThread().getName());
            }).start();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(list.size()); // 10000
    }
}
```