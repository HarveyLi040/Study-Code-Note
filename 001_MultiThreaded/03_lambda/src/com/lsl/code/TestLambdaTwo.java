package com.lsl.code;

interface Love{
    void love(int a);
}
class Ilove implements Love{
    @Override
    public void love(int a) {
        System.out.println("I love you-->" + a);
    }
}

public class TestLambdaTwo {
    public static void main(String[] args) {
        Love love1 = new Ilove();
        love1.love(1);

        Love love2 = new Ilove(){
            @Override
            public void love(int a){
                System.out.println("I love you-->" + a);
            }
        };
        love2.love(2);
        // 简化: 采用lambda表达式
        Love love3 = (int a)->{
                System.out.println("I love you-->" + a);
        };
        love3.love(3);

        // 简化: 去掉参数类型
        Love love4 = (a)->{
            System.out.println("I love you-->" + a);
        };
        love4.love(4);

        // 简化: 去掉参数的括号
        Love love5 = a->{
            System.out.println("I love you-->" + a);
        };
        love5.love(5);

        // 简化: 去掉函数体的花括号
        Love love6 = a-> System.out.println("I love you-->" + a);
        love6.love(6);
    }
}

//总结:
//    lambda表达式只能有一行代码的情况下才能简化成为一行
//    如果有多行，那么就用代码块包裹。前提是接口为函数式接口
//    多个参数也可以去掠参数类型，要去掠就都去控,必须加上括号