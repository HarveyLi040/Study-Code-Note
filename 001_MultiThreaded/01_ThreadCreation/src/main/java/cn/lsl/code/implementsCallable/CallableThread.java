package cn.lsl.code.implementsCallable;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.*;

public class CallableThread implements Callable<Boolean> {
    private String url;
    private String fileName;
    public CallableThread(String url,String fileName){
        this.url = url;
        this.fileName = fileName;
    }

    @Override
    public Boolean call() throws Exception {
       WebDownloader webDownloader = new WebDownloader();
        webDownloader.downloader(url,fileName);
        System.out.println("下载了文件名为"+fileName);
        return true;
    }
    public static void main(String[] args) {
        // 创建目标对象
        CallableThread t1 = new CallableThread("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/001.png","1.jpg");
        CallableThread t2 = new CallableThread("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/002.png","2.jpg");
        CallableThread t3 = new CallableThread("https://gitee.com/IAMLSL/repository-for-code-andnotes/raw/master/MultiThreading/img/003.png","3.jpg");

        // 创建执行服务
        ExecutorService ser = Executors.newFixedThreadPool(3);

        // 提交执行
        Future<Boolean> r1 = ser.submit(t1);
        Future<Boolean> r2 = ser.submit(t2);
        Future<Boolean> r3 = ser.submit(t3);

        // 获取结果
        try {
            boolean rs1 = r1.get();
            boolean rs2 = r2.get();
            boolean rs3 = r3.get();
            System.out.println(rs1);
            System.out.println(rs2);
            System.out.println(rs3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // 关闭服务
        ser.shutdownNow();
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