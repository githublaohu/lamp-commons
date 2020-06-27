package com.lamp.commons.lang.email.content;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.lamp.commons.lang.email.entity.EmailInfo;
import com.lamp.commons.lang.util.Tuple.Pair;

public class ContentCreate {

	
	static{
		 if (props.get("mail.smtp.auth").equals("true")) {
             Transport transport = session.getTransport("smtp"); 
             transport.connect((String)props.get("mail.smtp.host"), (String)props.get("username"), (String)props.get("password")); 
             transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
             transport.close(); 
         } else {
             Transport.send(mimeMsg);
         }
	}
	
	public static MimeMessage createMimeMessage(EmailInfo emailInfo , Session session) throws UnsupportedEncodingException, MessagingException{
		MimeMessage message = new MimeMessage( session );
		
		Pair< String , String > sender = emailInfo.getSender( );
		String email = sender.getUnit( );
		String alias = sender.getPair( );
		if(alias != null){
			message.setFrom(new InternetAddress(email, MimeUtility.encodeText( alias , "utf-8" , "B" )));
		}else{
			message.setFrom(new InternetAddress(email));
		}
		setAddress(message,  RecipientType.TO , emailInfo.getRecipientList( ) );
		setAddress(message,  RecipientType.CC , emailInfo.getPeopleList( ) );
		
		message.setSubject( emailInfo.getSubject( ) , "utf-8" );
		
		
	    MimeMultipart mimeMultipart = new MimeMultipart(); 
		BodyPart bodyPart = new MimeBodyPart(); 
		bodyPart.setContent(emailInfo.getContent( ), "text/html;charset=utf-8");
		mimeMultipart.addBodyPart(bodyPart);
		
		if (fileList != null && fileList.length > 0) {
            for (int i = 0; i < fileList.length; i++) {
            	bodyPart = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(fileList[i]); 
                bodyPart.setDataHandler(new DataHandler(fds)); 
                bodyPart.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                mimeMultipart.addBodyPart(bodyPart); 
            }
        }
		
		MimeBodyPart imgPart = new MimeBodyPart();
		DataSource imgds = new FileDataSource(new File("C:/Users/H__D/Desktop/logo.png"));
		DataHandler imgdh = new DataHandler(imgds );
		imgPart.setDataHandler(imgdh);
		//说明html中的img标签的src，引用的是此图片
		imgPart.setHeader("Content-Location", "http://sunteam.cc/logo.jsg");
		mimeMultipart.addBodyPart(imgPart); 
		
		message.setContent(mimeMultipart); 
		message.saveChanges(); 
		
		return message;
	}
	
	
	private static void setAddress(MimeMessage message ,RecipientType recipientType  , List<Pair< String , String >> list ) throws UnsupportedEncodingException, MessagingException{
		
		Address[] address = new Address[list.size( )];
		Pair< String , String > recipient;
		for(int i=0 ; i<list.size( ) ; i++ ){
			recipient = list.get( i );
			if( recipient.getPair( ) == null){
				address[ i ] = new InternetAddress(recipient.getUnit( ), MimeUtility.encodeText( recipient.getPair( ) , "utf-8" , "B" ));
			}else{
				address[ i ] = new InternetAddress(recipient.getUnit( ));
			}
		}
		message.setRecipients( recipientType , address );
	}
	
}
