package com.lsl.code.mail;

import com.lsl.code.auth.MailAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Slf4j
public class MailSender {
    /**
     *
     * @param from
     * @param mailTo
     * @param mailContent
     * @param files
     */
    public void sendEmail(String from,
                          String[] mailTo,
                          String mailSubject,
                          String mailContent,
                          String[] files) throws MessagingException, UnsupportedEncodingException {
        if(!checkEmailParams(from,mailTo,mailSubject,mailContent)){
            return;
        }

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
        message.setSubject(mailSubject);

        // 设置 html
        Multipart multipart = null;

        // 添加邮件内容
        BodyPart Content = new MimeBodyPart();
        Content.setContent(mailContent,"text/html;charset=utf-8");
        multipart = new MimeMultipart();
        multipart.addBodyPart(Content);

        // 添加附件
        if(files !=null && files.length!=0){
            for (String file : files) {
                BodyPart attachment = new MimeBodyPart();
                attachment.setDataHandler(new DataHandler(new FileDataSource(new File(file))));
                // encodeText方法抛出UnsupportedEncodingException
                attachment.setFileName(MimeUtility.encodeText(new File(file).getName()));
                multipart.addBodyPart(attachment);
            }
        }

        // 添加邮件信息
        message.setContent(multipart);

        // 设置邮件发送方
        message.setFrom(new InternetAddress(from));

        // 设置邮件接收方
        InternetAddress[] recipients = new InternetAddress[mailTo.length];
        for (int i = 0; i < recipients.length; i++) {
            recipients[i] = new InternetAddress(mailTo[i]);
        }

        // 注意：发给多个人是setRecipients方法
        message.setRecipients(Message.RecipientType.TO,recipients);

        // 送卫星上天
        Transport.send(message); //此方法会抛出MessagingException异常
    }

    public boolean checkEmailParams(String from,
                                   String[] mailTo,
                                String mailSubject,
                                 String mailContent){
        boolean flag = true;
        if(StringUtils.isBlank(from)){
            flag = false;
            throw new RuntimeException("请指定发送方邮箱！");
        }
        else if (mailTo == null || mailTo.length==0 ){
            flag = false;
            throw new RuntimeException("请指定接收方邮箱！");
        }
        else if (StringUtils.isBlank(mailContent)){
            flag = false;
            throw new RuntimeException("请指定邮件主题！");
        }
        else if (StringUtils.isBlank(mailSubject)){
            flag = false;
            throw new RuntimeException("请指定邮件内容！");
        }
        else{
            flag = true;
        }
        return flag;
    }
}
