package com.lamp.commons.lang.email.gao.impl;

import com.lamp.commons.lang.email.gao.EmailSender;
import com.lamp.commons.lang.email.gao.entity.SenderParams;

/**
 * 邮件发送类的工厂类
 * Created by gaoyang on 14-10-15.
 */
public class EmailSenderFactory {
    private SenderParams senderParams;

    /**
     * 设置是否通过SSL进行链接
     *
     * @param SSL 是否通过SSL进行链接
     * @return Sender的工厂类
     */
    public EmailSenderFactory setSSL(boolean SSL) {
        senderParams.setSSL(SSL);
        return this;
    }

    private EmailSenderFactory() {
        this.senderParams = new SenderParams();
    }

    /**
     * 发送邮件类的构造工厂
     *
     * @return Sender的工厂类
     */
    public static EmailSenderFactory custom() {
        return new EmailSenderFactory();
    }

    public EmailSenderFactory setAccount(String account) {
        senderParams.setAccount(account);
        return this;
    }

    public EmailSenderFactory setPassword(String password) {
        senderParams.setPassword(password);
        return this;
    }

    public EmailSenderFactory setSmtpHost(String smtpHost) {
        senderParams.setSmtpHost(smtpHost);
        return this;
    }

    public EmailSenderFactory setSmtpPort(int smtpPort) {
        senderParams.setSmtpPort(smtpPort);
        return this;
    }

    public EmailSenderFactory setDebug(boolean isDebug) {
        senderParams.setDebug(isDebug);
        return this;
    }

    /**
     * 根据设置的参数生成Sender类
     *
     * @return 发送邮件的操作类
     */
    public EmailSender build() {
        return new EmailSenderImpl(this.senderParams);
    }
}
