package com.lsl.code.mail;

import com.lsl.code.auth.MailAuthenticator;
import lombok.extern.slf4j.Slf4j;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Slf4j
public class AttachmentsMail {
    public static void main(String[] args) throws MessagingException {
        // 卫星
        Message message = null;
        /**
         * 补充燃料
         */
        Session session = null;
        Properties props = null;
        props = new Properties();
        props.put("mail.smtp.host","smtp.163.com");
        props.put("mail.smtp.port","25");
        props.put("mail.smtp.auth",true);
        session = Session.getDefaultInstance(props,new MailAuthenticator("harveyli_lsl@163.com","LOAZHAAHXYHAGYCD"));
        message = new MimeMessage(session);
        // 设置邮件主题
        message.setSubject("JavaMail邮件开发");

        // 设置 html
        String html = "<a href='https://blog.csdn.net/IAMLSL'>欢迎查看我的CSDN博客</a>";
        Multipart multipart = null;

        // 添加html
        BodyPart bodyPartHtml = new MimeBodyPart();
        bodyPartHtml.setContent(html,"text/html;charset=utf-8");
        multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPartHtml);

        // 添加附件
        BodyPart bodyPartAttachment01 = new MimeBodyPart();
        bodyPartAttachment01.setDataHandler(new DataHandler(new FileDataSource(new File("README.md"))));
        bodyPartAttachment01.setFileName("README.md");
        multipart.addBodyPart(bodyPartAttachment01);

        // 添加附件
        BodyPart bodyPartAttachment02 = new MimeBodyPart();
        bodyPartAttachment02.setDataHandler(new DataHandler(new FileDataSource(new File("邮件附件.txt"))));
        //bodyPartAttachment02.setFileName("邮件附件.txt");   此方式添加附件时，附件文件名经过传输后会乱码
        try {
            // 解决邮件附件名乱码
            bodyPartAttachment02.setFileName(MimeUtility.encodeText("邮件附件.txt"));
        } catch (UnsupportedEncodingException e) {
            log.error("邮件附件名编码错误");
        }
        multipart.addBodyPart(bodyPartAttachment02);

        // 设置邮件信息
        message.setContent(multipart);

        // 设置邮件发送方
        InternetAddress from = new InternetAddress("harveyli_lsl@163.com");
        message.setFrom(from);
        // 设置邮件接收方
        message.setRecipient(Message.RecipientType.TO,new InternetAddress("2241876453@qq.com"));
        // 送卫星上天
        Transport.send(message); //此方法会抛出MessagingException异常
    }
}
