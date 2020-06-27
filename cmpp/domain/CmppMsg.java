package com.lamp.commons.lang.cmpp.domain;

import java.util.Date;

/**
 * CMPP短信模型
 * Created by Fred on 2016/8/10.
 */
public class CmppMsg {
    /**
     * 短信Id
     */
    private String msgId;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 短信内容
     */
    private String message;

    /**
     *优先级
     */
    private int priority;
    /**
     * 提交时间
     */
    private Date submitTime;
    /**
     * 序列id
     */
    private int sequenceId;

    /**
     * 当前短信data索引
     */
    private int curIndex;

    /**
     * 处理总数
     */
    private int dealCount;

    private boolean success;

    /**
     * 短信数据分组byte流
     */
    private byte[][] dataArray;
    /**
     * 提交次数
     */
    private int connectTime;

    private int sendNumber;    

    public CmppMsg(String msgId) {
        this();
        this.msgId = msgId;
    }

    public CmppMsg() {
        this.success=true;
    }

    /**
     * 获取短信Id
     *
     * @return
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * 设置短信Id
     *
     * @param msgId
     */
    /*public void setMsgId(String msgId) {
        this.msgId = msgId;
    }*/

    /**
     * 获取电话号码
     *
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置电话号码
     *
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取短信内容
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置短信内容
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public int getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(int connectTime) {
        this.connectTime = connectTime;
    }

    /**
     * 获取序列id
     *
     * @return
     */
    public int getSequenceId() {
        return sequenceId;
    }

    /**
     * 设置序列id
     *
     * @param sequenceId
     */
    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    /**
     * 获取提交时间
     *
     * @return
     */
    public Date getSubmitTime() {
        return submitTime;
    }

    /**
     * 设置提交时间
     *
     * @param submitTime
     */
    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    /**
     * 获取短信条数
     *
     * @return
     */
    public int getMsgCount() {
        return dataArray.length;
    }

    /**
     * 获取短信数据
     *
     * @return
     */
    public byte[][] getDataArray() {
        return dataArray;
    }

    /**
     * 获取短信条数
     *
     * @param dataArray
     */
    public void setDataArray(byte[][] dataArray) {
        curIndex = 0;
        this.dataArray = dataArray;
    }

    /**
     * 逐条推送短信byte流
     *
     * @return
     */
    public byte[] getData() {
        if (curIndex < dataArray.length) {
            return dataArray[curIndex];
        }
        return null;
    }

    /**
     * 是否已经推送完毕
     *
     * @return
     */
    public boolean notEmpty() {
        return curIndex < dataArray.length;
    }
    public void moveNext(){
        if (curIndex < dataArray.length) {
            curIndex++;
        }
    }
    public boolean addResult(boolean success)
    {
        this.success&=success;
        dealCount++;
        return dealCount>=dataArray.length;
    }

    public boolean isSuccess() {
        return success;
    }

    /**
     * 获取优先级
     * @return
     */
    public int getPriority() {
        return priority;
    }

    /**
     * 设置优先级
     * @param priority
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

	public int getSendNumber( ) {
		return sendNumber;
	}

	public void setSendNumber( int sendNumber ) {
		this.sendNumber = sendNumber;
	}
}
