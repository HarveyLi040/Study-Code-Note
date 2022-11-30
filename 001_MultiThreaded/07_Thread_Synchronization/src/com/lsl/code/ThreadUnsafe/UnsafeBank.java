package com.lsl.code.ThreadUnsafe;

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
