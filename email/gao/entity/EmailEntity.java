package com.lamp.commons.lang.email.gao.entity;

/**
 * 邮件的实体类
 * Created by gaoyang on 14-10-15.
 */
public class EmailEntity {
    private String title;
    private String body;
    private String[] receivers;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String[] getReceivers() {
        return receivers;
    }

    public void setReceivers(String[] receivers) {
        this.receivers = receivers;
    }
}
