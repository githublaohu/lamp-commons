package com.lamp.commons.lang.email.gao;

import javax.mail.MessagingException;

import com.lamp.commons.lang.email.gao.entity.EmailEntity;

/**
 * 发送邮件的接口
 * Created by gaoyang on 14-10-15.
 */
public interface EmailSender {
    /**
     * 传入包含有Email信息的实体类，发送邮件
     *
     * @param emailEntity 包含有邮件内容的实体类
     * @throws MessagingException 发送邮件失败时的异常类
     */
    public void send(EmailEntity emailEntity) throws MessagingException;
}
