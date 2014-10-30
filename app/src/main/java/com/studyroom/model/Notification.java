package com.studyroom.model;
import java.util.Date;


public class Notification {
	String description;
	private String notificationableId;
	private String notificationableType;
	private Date dateTime;	

	
	public Notification(String description, String id, String type){
		this.description = description;
		this.notificationableId = id;
		this.notificationableType = type;
		dateTime = new Date();
	}

	//Called by Parser Only
	public Notification(){
		this.description = null;
		this.notificationableId = null;
		this.notificationableType = null;
		dateTime = null;
	}
}
