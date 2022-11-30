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
