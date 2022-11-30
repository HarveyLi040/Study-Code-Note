## 1.线程池

**线程池**

- 背景:经常创建和销毁、使用量特别大的资源，比如并发情况下的线程，对性能影响很大
- 思路:提前创建好多个线程，放入线程池中，使用时直接获取，使用完放回池中
- 好处:
  - 可以避免频繁创建销毁、实现重复利用。类似生活中的公共交通工具
  - 提高响应速度(减少了创建新线程的时间)
  - 降低资源消耗(重复利用线程池中线程，不需要每次都创建)
  - 便于线程管理

- 线程池参数:

  - corePoolSize: 核心池的大小

  - maximumPoolSize: 最大线程数

  - keepAliveTime: 线程没有任务时最多保持多长时间后会终止

**使用线程池**

- JDK 5.0起提供了线程池相关API: ExecutorService 和Executors
- ExecutorService:真正的线程池接口。常见子类ThreadPoolExecutor
- void execute(Runnable command) :执行任务/命令，没有返回值，一般用来执行Runnable
- <T> Future<T> submit(Callable<T> task):执行任务，有返回值，一般又来执行Callable
- void shutdown() :关闭连接池
- Executors:工具类、线程池的工厂类，用于创建并返回不同类型的线程池



 **ExecutorService练习**

```java
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
```