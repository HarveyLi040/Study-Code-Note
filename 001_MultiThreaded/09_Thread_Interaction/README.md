## 1.线程协作
#### 1.1 线程通信应用场景∶生产者和消费者问题

- 假设仓库中只能存放一件产品，生产者将生产出来的产品放入仓库。消费者将仓库中产品取走消费

- 如果仓库中没有产品，则生产者将产品放入仓库，否则停止生产并等待，直到仓库中的产品被消费者取走为止

- 如果仓库中放有产品，则消费者可以将产品取走消费，否则停止消费并等待，直到仓库中再次放入产品为止

**分析**

- 这是一个线程同步问题，生产者和消费者共享同一个资源，并且生产者和消费者之间相互依赖,互为条件
- 对于生产者，没有生产产品之前，要通知消费者等待，而生产了产品之后，又需要马上通知消费者消费
- 对于消费者，在消费之后，要通知生产者已经结束消费，需要生产新的产品以供消费
- 在生产者消费者问题中,仅有synchronized是不够的
  - synchronized可阻止并发更新同一个共享资源，实现了同步
  - synchronized不能用来实现不同线程之间的消息传递(通信)

**线程通信相关方法**

Java提供了几个方法解决线程之间的通信问题

| 方法名             | 作用                                                         |
| ------------------ | ------------------------------------------------------------ |
| wait()             | 表示线程一直等待，直到其他线程通知，与sleep不同，会释放锁    |
| wait(long timeout) | 指定等待的毫秒数                                             |
| notify()           | 唤醒一个处于等待状态的线程                                   |
| notifyAll()        | 唤醒同一个对象上所有调用wait()方法的线程﹐优先级别高的线程优先调度 |


<font color=red>注意:均是Object类的方法，都只能在同步方法或者同步代码块中使用,否则会抛出异常lIIegalMonitorStateException</font>>



####  1.2 生产者消费者解决方式一：管程法



- 并发协作模型“生产者/消费者模式”--->管程法
- 生产者:负责生产数据的模块(可能是方法,对象,线程,进程)
- 消费者:负责处理数据的模块(可能是方法,对象,线程,进程)
- 缓冲区:消费者不能直接使用生产者的数据,他们之间有个“缓冲区
- **生产者将生产好的数据放入缓冲区,消费者从缓冲区拿出数据**



![img](https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/009.png)





```java
package com.lsl.code;
// 测试: 生产者与消费者模型
// 解决方法: 管程法 ——> 利用缓冲区解决
// 角色： 生产者、消费者、产品、缓冲区
// 生产者
class Productor extends Thread{
      SynContainer container;
      public Productor(SynContainer container){
          this.container = container;
      }
    // 生产
    @Override
    public void run(){
        for (int i = 0; i < 100; i++) {
            System.out.println("生产了" + i + "只鸡");
            container.push(new Chicken(i));
        }
    }
}

// 消费者
class Consumer extends Thread{
    SynContainer container;
    public Consumer(SynContainer container){
        this.container = container;
    }
    @Override
    public void run(){
        for (int i = 0; i < 100; i++) {
            System.out.println("消费了-->" + container.pop().id + "只鸡");
        }
    }
}

// 产品
class Chicken{
    int id; // 产品编号
    public Chicken(int id){
        this.id = id;
    }
}

// 缓冲区
class SynContainer{
    // 需要一个容器大小
    Chicken[] chickens = new Chicken[10];
    // 容器计数器
    int count = 0;

    // 生产者放入产品
    public synchronized void push(Chicken chicken){
        // 如果容器满了，就需要等待消费者消费
        if(count == chickens.length){
            // 通知消费者消费、通知生产者等待
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 如果没有满，我们就需要丢入产品
        chickens[count] = chicken;
        count++;
        // 通知消费者消费
        this.notifyAll();
    }

    // 消费者消费产品
    public synchronized Chicken pop(){
        // 判断能否消费
        if(count==0){
            // 等待生产者生产，消费者等待
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 如果可以消费
        count--;
        Chicken chicken = chickens[count];

        // 吃完了，通知生产者生产
        this.notifyAll();
        return chicken;
    }
}
public class TestPCModel {
    public static void main(String[] args) {
        SynContainer container = new SynContainer();
        new Productor(container).start();
        new Consumer(container).start();
    }
}
```

####  1.3 生产者消费者解决方式一：信号灯法

并发协作模型“生产者/消费者模式”--->信号灯法



```java
package com.lsl.code;
// 测试生产者与消费者问题：信号灯法，标志位解决
// 生产者 -> 演员
class Player extends Thread{
    TV tv;
    public Player(TV tv){
        this.tv = tv;
    }
    @Override
    public void run(){
        for (int i = 1; i <= 20; i++) {
            if(i%2==0){
                this.tv.play("仙剑奇侠传"+ (i/2));
            }else {
                this.tv.play("雪中悍刀行"+ (i/2 + 1) );
            }
        }
    }
}
// 消费者 --> 观众
class Watcher extends Thread{
    TV tv;
    public Watcher(TV tv){
        this.tv = tv;
    }
    @Override
    public void run(){
        for (int i = 0; i < 20; i++) {
            tv.watch();
        }
    }
}
// 产品 -> 节目
class TV{
     // 演员表演，观众等待
     // 观众观看，演员等待
    String voice; // 表演的节目
    boolean flag = true;

    // 表演
    public synchronized void play(String voice){
        if(!flag){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("演员表演了：" + voice);
        // 通知观众观看
        this.notifyAll();
        this.voice = voice;
        this.flag = !this.flag;
    }
    // 观看
    public synchronized void watch(){
        if(flag){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("观众观看了："+voice);
        // 通知演员表演
        this.notifyAll();
        this.flag = !this.flag;
    }
}
// 测试生产者与消费者问题：信号灯法，标志位解决
public class TestPCSignal {
    public static void main(String[] args) {
        TV tv = new TV();
        new Player(tv).start();
        new Watcher(tv).start();
    }
}
/*输出：
            演员表演了：雪中悍刀行1
            观众观看了：雪中悍刀行1
            演员表演了：仙剑奇侠传1
            观众观看了：仙剑奇侠传1
            演员表演了：雪中悍刀行2
            观众观看了：雪中悍刀行2
            演员表演了：仙剑奇侠传2
            观众观看了：仙剑奇侠传2
            演员表演了：雪中悍刀行3
            观众观看了：雪中悍刀行3
            演员表演了：仙剑奇侠传3
            观众观看了：仙剑奇侠传3
            演员表演了：雪中悍刀行4
            观众观看了：雪中悍刀行4
            演员表演了：仙剑奇侠传4
            观众观看了：仙剑奇侠传4
            演员表演了：雪中悍刀行5
            观众观看了：雪中悍刀行5
            演员表演了：仙剑奇侠传5
            观众观看了：仙剑奇侠传5
            演员表演了：雪中悍刀行6
            观众观看了：雪中悍刀行6
            演员表演了：仙剑奇侠传6
            观众观看了：仙剑奇侠传6
            演员表演了：雪中悍刀行7
            观众观看了：雪中悍刀行7
            演员表演了：仙剑奇侠传7
            观众观看了：仙剑奇侠传7
            演员表演了：雪中悍刀行8
            观众观看了：雪中悍刀行8
            演员表演了：仙剑奇侠传8
            观众观看了：仙剑奇侠传8
            演员表演了：雪中悍刀行9
            观众观看了：雪中悍刀行9
            演员表演了：仙剑奇侠传9
            观众观看了：仙剑奇侠传9
            演员表演了：雪中悍刀行10
            观众观看了：雪中悍刀行10
            演员表演了：仙剑奇侠传10
            观众观看了：仙剑奇侠传10*/
```