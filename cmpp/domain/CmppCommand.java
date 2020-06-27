package com.lamp.commons.lang.cmpp.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.vpclub.notification.cmpp.domain.MsgSubmitResp;

/**
 * Created by Fred on 2016/8/6.
 */
public enum CmppCommand implements Serializable {
    /**
     * 请求连接
     */
    CMPP_CONNECT("CMPP_CONNECT", 0x00000001, "0x00000001", "请求连接"),
    /**
     * 请求连接应答
     */
    CMPP_CONNECT_RESP("CMPP_CONNECT_RESP", 0x80000001, "0x80000001", "请求连接应答" ,MsgConnectResp.class),
    /**
     * 终止连接
     */
    CMPP_TERMINATE("CMPP_TERMINATE", 0x00000002, "0x00000002", "终止连接"),
    /**
     * 终止连接应答
     */
    CMPP_TERMINATE_RESP("CMPP_TERMINATE_RESP", 0x80000002, "0x80000002", "终止连接应答"),
    /**
     * 提交短信
     */
    CMPP_SUBMIT("CMPP_SUBMIT ", 0x00000004, "0x00000004", "提交短信"),
    /**
     * 提交短信应答
     */
    CMPP_SUBMIT_RESP("CMPP_SUBMIT_RESP", 0x80000004, "0x80000004", "提交短信应答" , MsgSubmitResp.class),
    /**
     * 短信下发
     */
    CMPP_DELIVER("CMPP_DELIVER", 0x00000005, "0x00000005", "短信下发" , MsgDeliver.class),
    /**
     * 下发短信应答
     */
    CMPP_DELIVER_RESP("CMPP_DELIVER_RESP", 0x80000005, "0x80000005", "下发短信应答"),
    /**
     * 发送短信状态查询
     */
    CMPP_QUERY("CMPP_QUERY", 0x00000006, "0x00000006", "发送短信状态查询"),
    /**
     * 发送短信状态查询应答
     */
    CMPP_QUERY_RESP("CMPP_QUERY_RESP", 0x80000006, "", "发送短信状态查询应答"),
    /**
     * 删除短信
     */
    CMPP_CANCEL("CMPP_CANCEL ", 0x00000007, "0x00000007", "删除短信"),
    /**
     * 删除短信应答
     */
    CMPP_CANCEL_RESP("CMPP_CANCEL_RESP", 0x80000007, "0x80000007", "删除短信应答"),
    /**
     * 激活测试
     */
    CMPP_ACTIVE_TEST("CMPP_ACTIVE_TEST", 0x00000008, "0x00000008", "激活测试"),
    /**
     * 激活测试应答
     */
    CMPP_ACTIVE_TEST_RESP("CMPP_ACTIVE_TEST_RESP", 0x80000008, "0x80000008", "激活测试应答" , MsgActiveTestResp.class),
    /**
     * 消息前转
     */
    CMPP_FWD("CMPP_FWD", 0x00000009, "0x00000009", "消息前转"),
    /**
     * 消息前转应答
     */
    CMPP_FWD_RESP("CMPP_FWD_RESP", 0x80000009, "0x80000009", "消息前转应答"),
    /**
     * MT路由请求
     */
    CMPP_MT_ROUTE("CMPP_MT_ROUTE", 0x00000010, "0x00000010", "MT路由请求"),
    /**
     * MT路由请求应答
     */
    CMPP_MT_ROUTE_RESP("CMPP_MT_ROUTE_RESP", 0x80000010, "0x80000010", "MT路由请求应答"),
    /**
     * MO路由请求
     */
    CMPP_MO_ROUTE("CMPP_MO_ROUTE", 0x00000011, "0x00000011", "MO路由请求"),
    /**
     * MO路由请求应答
     */
    CMPP_MO_ROUTE_RESP("CMPP_MO_ROUTE_RESP", 0x80000011, "0x80000011", "MO路由请求应答"),
    /**
     * 获取路由请求
     */
    CMPP_GET_ROUTE("CMPP_GET_ROUTE", 0x00000012, "0x00000012", "获取路由请求"),
    /**
     * 获取路由请求应答
     */
    CMPP_GET_ROUTE_RESP("CMPP_GET_ROUTE_RESP", 0x80000012, "0x80000012", "获取路由请求应答"),
    /**
     * MT路由更新
     */
    CMPP_MT_ROUTE_UPDATE("CMPP_MT_ROUTE_UPDATE", 0x00000013, "0x00000013", "MT路由更新"),
    /**
     * MT路由更新应答
     */
    CMPP_MT_ROUTE_UPDATE_RESP("CMPP_MT_ROUTE_UPDATE_RESP", 0x80000013, "0x80000013", "MT路由更新应答"),
    /**
     * MO路由更新
     */
    CMPP_MO_ROUTE_UPDATE("CMPP_MO_ROUTE_UPDATE", 0x00000014, "0x00000014", "MO路由更新"),
    /**
     * MO路由更新应答
     */
    CMPP_MO_ROUTE_UPDATE_RESP("CMPP_MO_ROUTE_UPDATE_RESP", 0x80000014, "0x80000014", "MO路由更新应答"),
    /**
     * MT路由更新
     */
    CMPP_PUSH_MT_ROUTE_UPDATE("CMPP_PUSH_MT_ROUTE_UPDATE", 0x00000015, "0x00000015", "MT路由更新"),
    /**
     * MT路由更新应答
     */
    CMPP_PUSH_MT_ROUTE_UPDATE_RESP("CMPP_PUSH_MT_ROUTE_UPDATE_RESP", 0x80000015, "0x80000015", "MT路由更新应答"),
    /**
     * MO路由更新
     */
    CMPP_PUSH_MO_ROUTE_UPDATE("CMPP_PUSH_MO_ROUTE_UPDATE", 0x00000016, "0x00000016", "MO路由更新"),
    /**
     * MO路由更新应答
     */
    CMPP_PUSH_MO_ROUTE_UPDATE_RESP("CMPP_PUSH_MO_ROUTE_UPDATE_RESP", 0x80000016, "0x80000016", "MO路由更新应答"),

    /**
     *
     */
    CMPP_UKNOWN_RESP("CMPP_UKNOWN_RESP", 0, "0", "未知结果");

    /**
     * 业务编号
     */
    private String name;

    /**
     * 业务值
     */
    private Integer value;

    /**
     * 业务名称
     */
    private String description;

    private String code;


    private Class<?> clazz;
    
    private CmppCommand(String name, Integer value, String code, String description) {
        this.name = code;
        this.value = value;
        this.code = name;
        this.description = description;
    }

    private CmppCommand(String name, Integer value, String code, String description , Class<?> clazz) {
    	this( name , value , code , description );
    	this.clazz = clazz;
    }
    
    
    public final String getName() {
        return name;
    }

    public final int getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public final String getDescription() {
        return description;
    }

    @SuppressWarnings ( "unchecked" )
	public <T>T getMsgResponse() throws Exception{
    	MsgResponse mr = null;
    	if(clazz !=null){
    		try {
				mr = (MsgResponse)clazz.newInstance( );;
				mr.setSuccess( true );
				return ( T ) mr;
			} catch ( Exception e ){
				throw e;
			}
    		
    	}else{
    		mr = new MsgResponse();
    		mr.setSuccess( true );
    		return ( T ) mr;
    	}
    }
    
    
    @Override
    public String toString() {
        return this.name + "[" + this.value + "]" + "=" + this.description;
    }

    private static List<CmppCommand> list = new ArrayList<>();

    static {
        for (CmppCommand legEnum : CmppCommand.values()) {
            list.add(legEnum);
        }
    }

    /**
     * 根据业务值获取支付方式枚举
     *
     * @param value 业务值
     * @return PayMethodEnum
     */
    public static CmppCommand getEnum(Integer value) {
        return list.stream().filter(c -> value.equals(c.getValue())).findFirst().orElse(CMPP_UKNOWN_RESP);
    }

    /**
     * 根据业务编号获取支付方式枚举
     *
     * @param name 业务名称
     * @return PayMethodEnum
     */
    public static CmppCommand getEnum(String name) {
        return list.stream().filter(c -> name.equals(c.getName())).findFirst().orElse(CMPP_UKNOWN_RESP);
    }
}
