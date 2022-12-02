package com.lsl.code.ThreadSafe;

import java.util.ArrayList;
import java.util.List;

// �̲߳���ȫ�ļ��ϣ���ArrayList
public class safeList {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            new Thread(()->{
                synchronized (list){
                    list.add(Thread.currentThread().getName());
                }
            }).start();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(list.size());//���10000 -> �̰߳�ȫ
    }
}