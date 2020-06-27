package com.lamp.commons.lang.email.gao.entity;

/**
 * 创建发送者的参数类
 * Created by gaoyang on 14-10-15.
 */
public class SenderParams {
    private String account;
    private String password;
    private String smtpHost;
    private int smtpPort;
    private Boolean isDebug;
    private Boolean isSSL;

    public Boolean isSSL() {
        if (isSSL == null) {
            this.isSSL = false;
        }
        return isSSL;
    }

    public void setSSL(Boolean isSSL) {
        this.isSSL = isSSL;
    }

    public String isDebug() {
        if (isDebug == null) {
            this.isDebug = false;
        }
        return isDebug ? "true" : "false";
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }
}
