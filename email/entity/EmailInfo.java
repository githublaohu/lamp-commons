package com.lamp.commons.lang.email.entity;

import java.util.List;

import com.lamp.commons.lang.util.Tuple.Pair;

public class EmailInfo {

	private Pair< String , String > sender;
	
	private List<Pair< String , String >> recipientList;
	
	private List<Pair< String , String >> peopleList;
	
	private String subject;
	
	private String content;
	
	private List<Pair< String , String >> attachment;
	
	private List<Pair<String,String>> contentImg;

	public Pair< String , String > getSender( ) {
		return sender;
	}

	public void setSender( Pair< String , String > sender ) {
		this.sender = sender;
	}

	public List< Pair< String , String > > getRecipientList( ) {
		return recipientList;
	}

	public void setRecipientList( List< Pair< String , String > > recipientList ) {
		this.recipientList = recipientList;
	}

	public List< Pair< String , String > > getPeopleList( ) {
		return peopleList;
	}

	public void setPeopleList( List< Pair< String , String > > peopleList ) {
		this.peopleList = peopleList;
	}

	public String getSubject( ) {
		return subject;
	}

	public void setSubject( String subject ) {
		this.subject = subject;
	}

	public String getContent( ) {
		return content;
	}

	public void setContent( String content ) {
		this.content = content;
	}

	public List< Pair< String , String > > getAttachment( ) {
		return attachment;
	}

	public void setAttachment( List< Pair< String , String > > attachment ) {
		this.attachment = attachment;
	}

	public List< Pair< String , String > > getContentImg( ) {
		return contentImg;
	}

	public void setContentImg( List< Pair< String , String > > contentImg ) {
		this.contentImg = contentImg;
	}
	
	
	
	
}
