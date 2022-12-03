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
