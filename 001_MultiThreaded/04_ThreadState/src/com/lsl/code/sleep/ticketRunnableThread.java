package com.lsl.code.sleep;

// 模拟网络延时->放大问题的发生性
public class ticketRunnableThread implements Runnable{
        // 票数
        private int ticketNums = 10;

        @Override
        public void run() {
            while(true){
                if(ticketNums<=0){
                    break;
                }
                // 模拟延时
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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

