package com.lamp.commons.lang.rocketmq.producer;

import java.util.List;

public class ProducerEntity {

	private String name;
	
	private String clazzName;
	
	private List<String> clazzNameList;
	
	private String group;
	
	private String nameServer;
	
	private String topic;
	
	private String keys;
	
	private String tags;

	private String entityClazzName;
	
	public String getClazzName( ) {
		return clazzName;
	}

	public void setClazzName( String clazzName ) {
		this.clazzName = clazzName;
	}

	public String getGroup( ) {
		return group;
	}

	public void setGroup( String group ) {
		this.group = group;
	}

	public String getNameServer( ) {
		return nameServer;
	}

	public void setNameServer( String nameServer ) {
		this.nameServer = nameServer;
	}

	public String getTopic( ) {
		return topic;
	}

	public void setTopic( String topic ) {
		this.topic = topic;
	}

	public String getKeys( ) {
		return keys;
	}

	public void setKeys( String keys ) {
		this.keys = keys;
	}

	public String getTags( ) {
		return tags;
	}

	public void setTags( String tags ) {
		this.tags = tags;
	}

	public String getEntityClazzName( ) {
		return entityClazzName;
	}

	public void setEntityClazzName( String entityClazzName ) {
		this.entityClazzName = entityClazzName;
	}

	public String getName( ) {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public List< String > getClazzNameList( ) {
		return clazzNameList;
	}

	public void setClazzNameList( List< String > clazzNameList ) {
		this.clazzNameList = clazzNameList;
	}
	
	
}
