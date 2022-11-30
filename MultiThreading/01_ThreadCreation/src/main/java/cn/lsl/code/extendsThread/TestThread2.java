package cn.lsl.code.extendsThread;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;

// 练习Thread，实现多线程同步下载图片
public class TestThread2 extends Thread{
    private String url;
    private String fileName;
    public TestThread2(String url,String fileName){
        this.url = url;
        this.fileName = fileName;
    }
    @Override
    public void run(){
        WebDownloader webDownloader = new WebDownloader();
        webDownloader.downloader(url,fileName);
        System.out.println("下载了文件名为"+fileName);
    }

    public static void main(String[] args) {
        TestThread2 t1 = new TestThread2("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/001.png","1.jpg");
        TestThread2 t2 = new TestThread2("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/002.png","2.jpg");
        TestThread2 t3 = new TestThread2("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/003.png","3.jpg");
        // t1下载
        t1.start();
        // t2下载
        t2.start();
        // t3下载
        t3.start();
    }
}

//下载器
class WebDownloader{
      // 下载方法
    public void downloader(String url,String fileName){
        try {
       // 依赖于<!-- https://mvnrepository.com/artifact/commons-io/commons-io -->
            FileUtils.copyURLToFile(new URL(url),new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}