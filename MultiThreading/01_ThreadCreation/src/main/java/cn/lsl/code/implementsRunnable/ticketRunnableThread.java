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