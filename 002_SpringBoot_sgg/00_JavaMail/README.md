# JavaMail

##  1.JavaMail概述

JavaMail是由Sun定义的一套收发电子邮件的API，不同的厂商可以提供自己的实现类。但它并没有包含在JDK中，而是作为JavaEE的一部分

厂商所提供的JavaMail服务程序可以有选择地实现某些邮件协议，常见的邮件协议包括:

- SMTP:简单邮件传输协议，用于发送电子邮件的传输协议
- POP3:用于接收电子邮件的标准协议
- IMAP:互联网消息协议，是POP3的替代协议

这三种协议都有对应SSL加密传输的协议，分别是SMTPS，POP3S和IMAPS

除JavaMail服务提供程序之外，JavaMail还需要JAF（JavaBeans Activation Framework）来处理不是纯文本的邮件内容，这包括MIME(多用途互联网邮件扩展)、URL页面和文件附件等内容。另外，JavaMail依赖JAF(JavaBeans Activation Framework)，JAF在Java6之后已经合并到JDK中，而JDK5之前需要另外下载JAF的类库

##  2.邮件协议

### 2.1.邮件协议
在研究JavaMail API的细则之前，首先需要对于API用到的协议有个认识。对于java mail来说用到的协议有一下4种:

- SMTP
- POP
- IMAP
- MIME

###  2.2.SMTP

简单邮件传输协议(Simple Mail Transfer Protocol，SMTP)由RFC 821定义。它定义了发送电子邮件的机制。在JavaMail API环境中，您基于JavaMail的程序将和您的公司或因特网服务供应商的(Internet Service Provider's，ISP's)SMTP服务器通信。SMTP服务器会中转消息给接收方SMTP服务器以便最终让用户经由POP或IMAP获得

###  2.3.POP

POP代表邮局协议(Post Office Protocol)。目前用的是版本3，也称POP3，RFC1939定义了这个协议。POP是一种机制,因特网上大多数人用它得到邮件。它规定每个用户一个邮箱的支持。这就是它所能做的，而这也造成了许多混淆。使用POP3时,用户熟悉的许多性能并不是由POP协议支持的，如查看有几封新邮件消息这一性能。这些性能内建于如Eudora或Microsoft ,Outlook之类的程序中，它们能记住一些事，诸如最近一次收到的邮件，还能计算出有多少是新的。所以当使用JavaMail API时，如果您想要这类信息，您就必须自己算

###  2.4. IMAP

IMAP是更高级的用于接收消息的协议。在RFC2060中被定义，IMAP代表因特网消息访问协议(Internet Message AccessProtocol)，目前用的是版本4，也称IMAP4。在用到IMAP时，邮件服务器必需支持这个协议。不能仅仅把使用POP的程序用于IMAP，并指望它支持IMAP所有性能。假设邮件服务器支持IMAP，基于JavaMail的程序可以利用这种情况一用户在服务器上有多个文件夹(folder)，并且这些文件夹可以被多个用户共享

因为有这一更高级的性能，您也许会认为所有用户都会使用IMAP。事实并不是这样。要求服务器接收新消息，在用户请求时发送到用户手中，还要在每个用户的多个文件夹中维护消息。这样虽然能将消息集中备份，但随着用户长期的邮件夹越来越大到磁盘空间耗尽时，每个用户都会受到损失。使用POP，就能卸载邮件服务器上保存的消息了

###  2.5.MIME

MIME代表多用途因特网邮件扩展标准(Multipurpose Internet Mail Extensions)。它不是邮件传输协议。但对传输内容的消息、附件及其它的内容定义了格式。这里有很多不同的有效文档:RFC 822、RFC 2045、RFC2046和RFC2047。作为一个JavaMail API的用户，您通常不必对这些格式操心。无论如何，一定存在这些格式而且程序会用到它

# 3.JavaMail的关键对象

JavaMail对收发邮件进行了高级的抽象，形成了一些关键的的接口和类，它们构成了程序的基础，下面我们分别来了解一下这些最常见的对象

###  3.1.Properties:属性对象

由于JavaMail需要和邮件服务器进行通信，这就要求程序提供许多诸如服务器地址、端口、用户名、密码等信息，JavaMail通过Properties对象封装这些属性西信息。如下面的代码封装了两个属性信息:

```java
Properties props = new Properties();
props.put("mail.smtp.host", "smtp.sina.com.cn");
props.put("mail.smtp.auth" , "true");
```

针对不同的的邮件协议，JavaMail规定了服务提供者必须支持一系列属性，下表是针对SMTP协议的一些常见属性（属性值都以string类型进行设置，属性类型栏仅表示属性是如何被解析的):

| 属性名                           | 属性类型 | 说明                                                         |
| -------------------------------- | -------- | ------------------------------------------------------------ |
| mail.smtp.host                   | String   | SMTP服务器地址，如smtp.sina.com.cn                           |
| mail.smtp.port                   | int      | SMTP服务器端口号，默认为25                                   |
| mail.smtp.auth                   | boolean  | SMTP服务器是否需要用户认证，默认为false                      |
| mail.smtp.user                   | String   | SMTP默认的登陆用户名                                         |
| mail.smtp.from                   | String   | 默认的邮件发送源地址                                         |
| mail.smtp.socketFactory.class    | String   | socket工厂类类名，通过设置该属性可以覆盖提供者默认的实现，必须实现javax.NET.SocketFactory接口 |
| mail.smtp.socketFactory.port     | int      | 指定socket工厂类所用的端口号，如果没有规定，则使用默认的端口 |
| mail.smtp.socketFactory.fallback | boolean  | 设置为true时，当使用指定的socket类创建socket失败后，将使用Java.net.Socket创建socket，默认为true |
| mail.smtp.timeout                | int      | I/o连接超时时间，单位为毫秒，默认为永不超时                  |

其他几个协议也有类似的一系列属性，如POP3的mail.pop3.host、mail.pop3.port以及IMAP的mail.imap.host、mail.imap.port等

### 3.2. Session:会话对象

Session是一个很容易被误解的类，这归咎于混淆视听的类名。千万不要以为这里的Session像HttpSession一样代表真实的交互会话，但创建Session对象时，并没有对应的物理连接，它只不过是一对配置信息的集合。Session的主要作用包括两个方面:

- 一、接收各种配置属性信息:通过Properties对象设置的属性信息
- 二、初始化lavaMail环境:根据JavaMail的配置文件，初始化]avaMail环境，以便通过Session对象创建其他重要类的实例

### 3.3.Transport和Store:传输和存储

邮件操作只有发送或接收两种处理方式，JavaMail将这两种不同操作描述为传输(javax.mail.Transport)和存储(javax.mail.Store)，传输对应邮件的发送，而存储对应邮件的接收

### 3.4.Message:消息对象

一旦获得Session对象，就可以继续创建要发送的消息。这由Message类来完成。因为Message是个抽象类，您必需用一个子类，多数情况下为javax.mail.internet.MimeMessage。MimeMessage是个能理解MIME类型和头的电子邮件消息，正如不同RFC中所定义的。虽然在某些头部域非ASCIl字符也能被译码，但Message头只能被限制为用US-ASCIl字符

### 3.5.Address:地址

一旦您创建了Session和Message，并将内容填入消息后，就可以用Address 确定信件地址了。和Message一样，Address 也是个抽象类。您用的是javax.mail.internet.InternetAddress类。若创建的地址只包含电子邮件地址，只要传递电子邮件地址到构造器就行了

### 3.6.Authenticator:认证者

与java类一样，JavaMail API也可以利用Authenticator通过用户名和密码访问受保护的资源。对于JavaMail API来说，这些资源就是邮件服务器。JavaMail Authenticator在javax.mail包中，而且它和java.net中同名的类Authenticator不同。两者并不共享同一个Authenticator，因为]avaMail API用于Java 1.1，它没有java.net类别。要使用Authenticator，先创建一个抽象类的子类，并从getPasswordAuthentication()方法中返回PasswordAuthentication 实例。创建完成后，您必需向session注册Authenticator。然后，在需要认证的时候，就会通知Authenticator。您可以弹出窗口，也可以从配置文件中（虽然没有加密是不安全的）读取用户名和密码，将它们作为PasswordAuthentication对象返回给调用程序



# 4.Java Mail 发送邮件

###  4.1 环境准备

进入qq邮箱，点击设置中的POP3/SMTP/IMAP,开通服务。开通服务后会获得一个授权码(本人开通授权码为LOAZHAAHXYHAGYCD)，授权码后续会用到



<table align="center">
    <tr>
        <th ><img src="https://img-blog.csdnimg.cn/6e97b1a2ca3d4ad4a33dc8e8fd5e14f4.png" > <b>点击设置中的POP3/SMTP/IMAP</b></th>
                <th ><img src="https://img-blog.csdnimg.cn/c33880edb78c4640b84edc6f03dfc25a.png" > <b>开通服务</b></th>
    </tr>
    </table>

### 4.2 发送简单邮件

**添加依赖**

```xml
<!--java mail坐标依赖-->
<dependency>
    <groupId>javax.mail</groupId>
    <artifactId>mail</artifactId>
    <version>1.4.7</version>
</dependency>
```

**权限认证类**

```java
package com.lsl.code.auth;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {
    private String userName;
    private String passWord;

    public MailAuthenticator(String userName, String passWord){
        this.userName = userName;
        this.passWord = passWord;
    }
    @Override
    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(userName,passWord);
    }

}
```

**简单邮件发送**

```java
package com.lsl.code.mail;

import com.lsl.code.auth.MailAuthenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mail {
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
```

### 4.3 发送html邮件



```java
package com.lsl.code.mail;

import com.lsl.code.auth.MailAuthenticator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class htmlMail {
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

        // 设置 HTML 邮件内容
        String html = "<a href='https://blog.csdn.net/IAMLSL'>欢迎查看我的CSDN博客</a>";
        Multipart multipart = null;
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(html,"text/html;charset=utf-8");
        multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);
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
```

###  4.4 发送带附件邮件

```java
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
```



###  4.5 封装邮件发送类

**封装邮件功能**

```java
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
```

**测试：调用MailSender发送邮件**

```java
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
```
