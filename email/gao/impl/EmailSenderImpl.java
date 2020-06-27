package com.lamp.commons.lang.email.gao.impl;

import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.lamp.commons.lang.email.gao.EmailSender;
import com.lamp.commons.lang.email.gao.entity.EmailEntity;
import com.lamp.commons.lang.email.gao.entity.SenderParams;
import com.lamp.commons.lang.email.gao.utils.EmailSenderUtils;

/**
 * 发送邮件的外部接口实现类
 * Created by gaoyang on 14-10-15.
 */
public class EmailSenderImpl implements EmailSender {
    private SenderParams senderParams;
    private Session session;

    protected EmailSenderImpl(SenderParams senderParams) {
        Properties properties;
        if (senderParams.isSSL()) {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            properties = EmailSenderUtils.getPropertiesWithSSL(senderParams);
        } else {
            properties = EmailSenderUtils.getProperties(senderParams);
        }
        this.senderParams = senderParams;
        session = Session.getInstance(properties);
    }

    public void send(EmailEntity emailEntity) throws MessagingException {
        Message message = getMessage(emailEntity);
        Transport transport = getTransport();
        InternetAddress[] internetAddresses = getInternetAddresses(emailEntity);
        transport.sendMessage(message, internetAddresses);
        transport.close();
    }

    private Transport getTransport() throws MessagingException {
        Transport transport = session.getTransport();
        transport.connect(senderParams.getSmtpHost(), senderParams.getSmtpPort(), senderParams.getAccount(), senderParams.getPassword());
        return transport;
    }

    private InternetAddress[] getInternetAddresses(EmailEntity emailEntity) throws AddressException {
        InternetAddress[] internetAddresses = new InternetAddress[emailEntity.getReceivers().length];
        for (int i = 0; i < emailEntity.getReceivers().length; i++) {
            internetAddresses[i] = new InternetAddress(emailEntity.getReceivers()[i]);
        }
        return internetAddresses;
    }

    private Message getMessage(EmailEntity emailEntity) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderParams.getAccount()));
        message.setText(emailEntity.getBody());
        message.setSubject(emailEntity.getTitle());
        return message;
    }


}
