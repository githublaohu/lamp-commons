package cn.vpclub.notification.cmpp.domain;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Submit消息结构应答定义
 * Created by Fred on 2016/8/10.
 */
public class MsgSubmitResp extends MsgResponse {
	
    private static Logger logger = Logger.getLogger(MsgSubmitResp.class);
    
    private long msgId;
    /**
     * 结果
     *   0：正确
     *   1：消息结构错
     *   2：命令字错 
     *   3：消息序号重复 
     *   4：消息长度错 
     *   5：资费代码错 
     *   6：超过最大信息长 
     *   7：业务代码错
     *   8：流量控制错 
     *   9：本网关不负责服务此计费号码 
     *   10：Src_Id错误 
     *   11：Msg_src错误
     *   12：Fee_terminal_Id错误 
     *   13：Dest_terminal_Id错误
     */
    private byte result;

    public MsgSubmitResp(){
    	
    }
    
    public MsgSubmitResp(MsgResponse response) {
        super(response);
        byte[] data = response.getBody();
        if (data.length == 8 + 1) {
            ByteArrayInputStream bins = new ByteArrayInputStream(data);
            DataInputStream dins = new DataInputStream(bins);
            try {
                this.msgId = dins.readLong();
                this.result = dins.readByte();
                dins.close();
                bins.close();
            } catch (IOException e) {
            }
        } else {
            logger.info("发送短信IMSP回复,解析数据包出错，包长度不一致。长度为:" + data.length);
        }
    }

    public void init(ByteBuffer buffer){
    	  this.msgId  = buffer.getLong( );
          setResult( buffer.get( ) );
    }
    
    
    
    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(byte result) {
    	if(result == 0){
    		setSuccess( true );
    	}else{
    		setSuccess( false );
    	}
        this.result = result;
    }
    
    StringBuffer  content( ) {
    	return super.content( ).append( "  stauts : " ).append( result );
    }
    
}
