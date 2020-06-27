package com.lamp.commons.lang.sms;
/**
 * 短信业务接口
 * @author yuki
 *
 */
public interface SmsBusiness {

	/**
	 * 收到数据
	 */
	void receiveData();
	
	/**
	 * 发送一条数据
	 * @return
	 */
	boolean sendData();
	
	/**
	 * 发送多条数据
	 * @return
	 */
	boolean sendDataList();
	
	/**
	 * 发送成功
	 * @return
	 */
	boolean sendDataSuccess();
	
	/**
	 * 发送失败
	 * @return
	 */
	boolean sendDataFailure();
	
	/**
	 * 发送失败次数达到限制
	 * @return
	 */
	boolean sendDataFailureLimit();
	
	/**
	 * 服务器批量响应成功
	 * @return
	 */
	boolean serverResponseDataListSuccess();
	
	/**
	 * 服务器批量响应失败
	 * @return
	 */
	boolean serverResponseDataListFailure();
	
	/**
	 * 服务器相关成功
	 * @return
	 */
	boolean serverRespnseDataSuccess();
	
	/**
	 * 服务器响应失败
	 * @return
	 */
	boolean serverResponseDataFailure();
	
	
	
	
}
