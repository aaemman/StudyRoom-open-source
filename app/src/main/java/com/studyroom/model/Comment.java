package com.studyroom.model;

public class Comment {
	//Post
	private String description;
	private String author;
	private String dateTime;

	
	
	//Called by Parser Only
	public Comment(String comment_description, String comment_author, String datetime){
		//Post
		this.description = comment_description;
		this.author = comment_author;
		dateTime = datetime;
		
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	
	
	
	
	
	
	
}