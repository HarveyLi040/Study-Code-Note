package com.lsl.code;

// 定义一个函数式接口
interface Like{
    void lambda();
}

// 实现类
class ILike implements Like{
    @Override
    public void lambda() {
        System.out.println("I like lambda1");
    }
}

// 推导lamda表达式
public class TestLambda {

    // 静态内部类
    static class ILike2 implements Like{
        @Override
        public void lambda() {
            System.out.println("I like lambda2");
        }
    }

    public static void main(String[] args) {
        // 1.new一个接口指向实现类
        Like like1 = new ILike();
        like1.lambda();

       // 2.静态内部类
        Like like2 = new ILike2();
        like2.lambda();

        // 3.局部内部类
        class ILike3 implements Like{
            @Override
            public void lambda() {
                System.out.println("I like lambda3");
            }
        }
        Like like3 = new ILike3();
        like3.lambda();

        // 4.匿名内部类，没有类的名称,必须惜助接口或者父类
        Like like4 = new Like() {
             @Override
             public void  lambda(){
                 System.out.println("I like lambda4");
             }
        };
        like4.lambda();

        // 5.利用lambda表达式简化
        Like like5 = ()->{
            System.out.println("I like lambda5");
        };
        like5.lambda();
    }
}
/** 输出:
 * I like lambda1
 * I like lambda2
 * I like lambda3
 * I like lambda4
 * I like lambda5
 */