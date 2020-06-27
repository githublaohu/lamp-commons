把cmpp集成到 sms模块，实现cmpp的各种模式。
移动cmpp
https://wenku.baidu.com/view/6461d1f34693daef5ef73dce.html###
联通sgip协议
https://wenku.baidu.com/view/00ce4759ad02de80d4d84088.html
电信smgp协议
https://wenku.baidu.com/view/2adafcf8910ef12d2af9e75a.html

http://www.cnblogs.com/zhcncn/articles/3035561.html
群发功能处理

sms 细节
1, 协议，需要兼容这三种协议
   http
   cmpp（ 这里 老胡在做）
       阿里云sdk（目前不处理）
       
2，需要支持同步调用，异步调用，队列等多种方案
3，模板接口
     模板实现方案，有两个，一个v。一个是自定义实现方案。
  模板执行时机。异步，是异步线程执行，其他都在当前线程执行
      模板内容的添加。删除修改。
     
4，操作细节
	发送
	查询余额等



移动cmpp协议

英文缩写：CMPP (China Mobile Peer to Peer)

　　中文名称：中国移动通信互联网短信网关接口协议

　　说明：为中国移动通信集团公司企业规范。规范中描述了中国移动短信业务中各网元（包括ISMG、 GNS和SP）之间的相关消息的类型和定义。

　　范围：

　　规范中定义了以下三方面的内容：

　　（1）信息资源站实体与互联网短信网关之间的接口协议；

　　（2）互联网短信网关之间的接口协议；

　　（3）互联网短信网关与汇接网关之间的接口协议。

　　适用于各SP和ISMG的开发厂商。

联通sgip协议

SGIP是Short Message Gateway Interface Protocol的英文缩写，是中国联合通信公司短消息网关系统接口协议。

　　协议说明

　　本协议是SMG和SP之间、SMG和GNS之间、以及SMG和SMG之间的接口协议，简称SGIP。

　　通过应用SGIP协议，SP可以接入到SMG，实现SP应用的一点接入、全网服务；SMG可以通过SGIP协议，实现消息在不同SMG之间的路由和转发。同时，SMG通过该协议也可以和GNS通信，以实现各SMG和GNS之间路由表的同步功能。

　　适用范围

　　本协议适用于各SP厂商和SMG的开发厂商。

　　号码可随意扩展

　　支持全国联通上下行

　　支持300字长短信

　　可提供二次开发接口

电信smgp协议

SMGP协议简称SMGP(Short Message Gateway Protocol)

　　定义

　　SMGP 协议是SMGW 与其它网元设备（除SMC 外）进行短消息传输的接口协议。

　　非SMC 网元设备向SMGW 发送或从SMGW 接收短消息，这些非SMC 网元设备称为

　　ESME。

　　通信方式

　　SMGW 与ESME 之间共有两种连接方式：长连接和短连接。所谓长连接，指在一个

　　TCP 连接上可以连续发送多个数据包，在TCP 连接保持期间，如果没有数据包发送，需要

　　双方发链路检测包以维持此连接。短连接是指通信双方有数据交互时，就建立一个TCP 连

　　接，数据发送完成后，则断开此TCP 连接，即每次TCP 连接只完成一对SMGP 消息的发送。






5，下面是参数细节。基于xml配置
   厂商动态识别
https://yq.aliyun.com/articles/42239

协议
ip
端口
编码
账户 k
账户 v
密码 k
密码 v
机构
机构编码


消息id k
消息id v
内容k
内容 v
手机 k
手机 v
定时 k
定时 v

数据格式
格式标识符字段
数据字段



返回

messageID 	消息编号
receiver 	短信接收号码
state 	          发送状态（1成功，2失败）
err_code 	错误码
event 	          触发的事件类型



success:msgid 
提交成功，发送状态请见 4.1 
error:msgid 
提交失败 
error:Missing username 
用户名为空 
error:Missing password 
密码为空 
error:Missing apikey 
APIKEY 为空 
error:Missing recipient 
收件人手机号码为空 
error:Missing message content 
短信内容为空或编码不正确 
error:Account is blocked 
帐号被禁用 
error:Unrecognized encoding 
编码未能识别 
error:APIKEY or password error 
APIKEY 或密码错误 
error:Unauthorized IP address 
未授权 IP 地址 
error:Account balance is insufficient 
余额不足 
error:Throughput Rate Exceeded 
发送频率受限 
error:Invalid md5 password length 
MD5 密码长度非 32 位 