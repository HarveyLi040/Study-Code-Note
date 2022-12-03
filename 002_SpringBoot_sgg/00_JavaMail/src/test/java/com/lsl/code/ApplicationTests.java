package com.lsl.code;

import com.lsl.code.mail.MailSender;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@SpringBootTest
@Slf4j
class MailSenderTest {

    @Test
    void MailSenderTest() {

        String from = "harveyli_lsl@163.com";
        String[] mailTo = new String[]{"2241876453@qq.com"};
        String mailSubject = "邮件发送类MailSender简单封装测试";
        String mailContent = "<a href='https://blog.csdn.net/IAMLSL'>欢迎查看我的CSDN博客</a>";
        String path = System.getProperty("user.dir") + "\\";
        System.out.println("路径：" + path);
        String[] files = new String[]{"README.md","邮件附件.txt"};
        try {
            new MailSender().sendEmail(from,mailTo,mailSubject,mailContent,files);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
