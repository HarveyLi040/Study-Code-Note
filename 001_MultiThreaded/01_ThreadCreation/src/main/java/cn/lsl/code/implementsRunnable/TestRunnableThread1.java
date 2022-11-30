package cn.lsl.code.implementsRunnable;
// 创建线程方式2︰实现runnable接口,重写run方法，执行线程需要丢入runnable接口实现类。调用start方法
public class TestRunnableThread1 implements Runnable{
    @Override
    public void run(){
        //run方法线程体
        for(int i=0;i<200;i++){
            System.out.println("我在看代码---"+i);
        }
    }
    //main线程->主线程
    public static void main(String[] args) {
        // 创建runnbale接口的实现类对象
        TestRunnableThread1 testRunnableThread1 = new TestRunnableThread1();
        // 创建线程对象,通过线程对象来开启我们的线程,代理
//        Thread thread = new Thread(testRunnableThread1);
//        thread.start();
        new Thread(testRunnableThread1).start();
        for (int i = 0; i < 200; i++) {
            System.out.println("我在学习多线程---"+i);
        }
    }
}
