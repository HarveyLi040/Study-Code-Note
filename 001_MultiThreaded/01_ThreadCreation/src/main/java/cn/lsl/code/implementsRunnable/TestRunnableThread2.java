package cn.lsl.code.implementsRunnable;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TestRunnableThread2 implements Runnable{
    private String url;
    private String fileName;
    public TestRunnableThread2(String url,String fileName){
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
        TestRunnableThread2 t1 = new TestRunnableThread2("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/001.png","1.jpg");
        TestRunnableThread2 t2 = new TestRunnableThread2("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/002.png","2.jpg");
        TestRunnableThread2 t3 = new TestRunnableThread2("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/003.png","3.jpg");
        // t1下载
        new Thread(t1).start();
        // t2下载
        new Thread(t2).start();
        // t3下载
        new Thread(t3).start();
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