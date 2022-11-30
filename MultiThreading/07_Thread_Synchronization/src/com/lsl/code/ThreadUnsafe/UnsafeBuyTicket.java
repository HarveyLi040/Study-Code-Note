package com.lsl.code.ThreadUnsafe;

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
