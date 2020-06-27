package com.lamp.commons.lang.email.gao.utils;

import java.util.Properties;

import com.lamp.commons.lang.email.gao.entity.SenderParams;

/**
 * EmailSender的工具类
 * Created by gaoyang on 14-10-16.
 */
public class EmailSenderUtils {
    /**
     * 添加默认的属性
     * @param senderParams Sender的配置信息
     * @return 发送邮件的属性
     */
    public static Properties getProperties(SenderParams senderParams) {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.debug", senderParams.isDebug());
        return properties;
    }

    /**
     * 添加支持SSL链接的配置
     * @param senderParams Sender的配置信息
     * @return 支持SSL链接的发送邮件的配置属性
     */
    public static Properties getPropertiesWithSSL(SenderParams senderParams) {
        Properties props = getProperties(senderParams);
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        return props;
    }

}
