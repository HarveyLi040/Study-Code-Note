## 1.守护(daemon)线程
- 
  线程分为**用户线程**（如main）和**守护线程**（如gc）

- 虚拟机必须确保用户线程执行完毕

- 虚拟机不用等待守护线程执行完毕

- 如,后台记录操作日志,监控内存,垃圾回收等待..



```java
package com.lsl.code;

// 测试守护线程
class DaemonThread implements Runnable{
    @Override
    public void run() {
        while(true){
            System.out.println("守护线程守护你");
        }
    }
}

class UserThread implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 36500; i++) {
            System.out.println("用户线程running-->" + i);
        }
        System.out.println("====goodbye world!====");
    }
}

public class TestDaemon {
    public static void main(String[] args) {
        DaemonThread   daemonThread = new DaemonThread();
        UserThread userThread = new UserThread();

        Thread thread = new Thread(daemonThread);
        thread.setDaemon(true); // 默认是false表示是用户线程,正常的线程都是用户线程...

        thread.start(); // 启动守护线程

        new Thread(userThread).start();
    }
}
/*用户线程结束后守护线程还在运行：
                守护线程守护你
                ====goodbye world!====
                守护线程守护你
                守护线程守护你
                守护线程守护你
                守护线程守护你
                守护线程守护你*/
```