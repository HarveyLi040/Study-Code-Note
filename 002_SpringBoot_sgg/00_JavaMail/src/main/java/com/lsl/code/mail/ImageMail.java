package com.lsl.code.mail;

import com.lsl.code.auth.MailAuthenticator;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.Properties;

/**
 * 发送内嵌图片的邮件
 */
public class ImageMail {
    private static Properties props;
    private static InternetAddress sendFrom;
    static {
        // 初始化邮件参数
        props = new Properties();
        props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.host","smtp.163.com");
        props.put("mail.smtp.port","25");
        props.put("mail.smtp.auth",true);
        try {
            sendFrom = new InternetAddress("harveyli_lsl@163.com");
        } catch (AddressException e) {
            throw new RuntimeException();
        }
    }
    public static void main(String[] args) throws MessagingException {
        // 1.创建一个邮件会话
        Session session = Session.getDefaultInstance(props,
                new MailAuthenticator("harveyli_lsl@163.com","LOAZHAAHXYHAGYCD"));

        // 2.创建邮件整体对象
        Message message = new MimeMessage(session);

        // 3.设置邮件参数
        // 邮件标题
        message.setSubject("发送内嵌图片资源邮件");
        // 邮件发送时间
        message.setSentDate(new Date());
        // 邮件发件人
        message.setFrom(sendFrom);
        // 邮件接收方
        message.setRecipient(Message.RecipientType.TO,new InternetAddress("harveyli_lsl@163.com"));

        // 4.1 构建多功能邮件块
        MimeMultipart related = new MimeMultipart("related");
        // 4.2 构建邮件内容（左侧文本 + 右侧图片资源）
        MimeBodyPart content = new MimeBodyPart();
        MimeBodyPart resource01 = new MimeBodyPart();
        MimeBodyPart resource02 = new MimeBodyPart();

        // 设置具体内容：图片资源
        String filePath = System.getProperty("user.dir") + "\\" + "ZMail.png";
        DataSource dataSource = new FileDataSource(new File(filePath));
        DataHandler handler = new DataHandler(dataSource);
        resource01.setDataHandler(handler);
        resource01.setContentID("aaa"); // 设置资源名称，给外键引用.可以随便命名，只要和cid对应

        // 设置具体内容：图片资源
        String file = System.getProperty("user.dir") + "\\" + "ZMIME.png";
        DataSource ds = new FileDataSource(new File(file));
        DataHandler hd = new DataHandler(ds);
        resource02.setDataHandler(hd);
        resource02.setContentID("bbb"); // 设置资源名称，给外键引用.可以随便命名，只要和cid对应

        // 4.3 把构建的复杂邮件块，添加到邮件中
        message.setContent(related);
        // 设置资源具体内容
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>发送内嵌图片资源邮件</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<hr/>\n" +
                "<a href='https://blog.csdn.net/IAMLSL'>欢迎查看我的CSDN博客</a><br/>\n" +
                "<hr/>\n" +
                "<img src='cid:aaa'/><br/>\n" +
                "<hr/>\n" +
                "<a href='https://www.cnblogs.com/ysocean/p/7666061.html'>一篇不错的javaMail博客</a><br/>\n" +
                "<hr/>\n" +
                "<img src='cid:bbb'/><br/>\n" +
                "<hr/>\n" +
                "</body>\n" +
                "</html>";
        content.setContent(html,"text/html;charset=utf-8");

        related.addBodyPart(content);
        related.addBodyPart(resource01);
        related.addBodyPart(resource02);
        // 保存邮件（可选）
        message.saveChanges();

        Transport.send(message); //此方法会抛出MessagingException异常
    }
}
