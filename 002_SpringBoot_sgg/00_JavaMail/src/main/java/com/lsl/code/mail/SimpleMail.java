package com.lsl.code.mail;

import com.lsl.code.auth.MailAuthenticator;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SimpleMail {
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
        // 设置邮件内容
        message.setText("JavaMail邮件测试");
        // 设置邮件发送方
        InternetAddress from = new InternetAddress("harveyli_lsl@163.com");
        message.setFrom(from);
        // 设置邮件接收方
        message.setRecipient(Message.RecipientType.TO,new InternetAddress("2241876453@qq.com"));
        // 送卫星上天
        Transport.send(message); //此方法会抛出MessagingException异常
    }
}
