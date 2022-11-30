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
