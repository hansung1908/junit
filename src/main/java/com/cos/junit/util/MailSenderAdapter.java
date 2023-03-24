package com.cos.junit.util;

public class MailSenderAdapter implements MailSender{

//    private Mail mail;
//
//    public MailSenderAdapter(Mail mail) {
//        this.mail = mail;
//    }

    @Override
    public boolean send() {
        // return mail.sendMail();
        return true;
    }
}
