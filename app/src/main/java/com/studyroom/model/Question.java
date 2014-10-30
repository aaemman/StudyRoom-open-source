package com.studyroom.model;
import java.util.ArrayList;


public class Question {
	//Post
	private String description;
	private String author;
	private String dateTime;
	private boolean isReported;
	//Questions and Answers Only
	private String totalPoints;
	private boolean isVotable;
	private ArrayList<Object> comments;
	private String class_name;
	//Question
	private String title;
	private int id;
	private int authorId;
	public boolean hasCorrectAnswer = false;
	
	//Questions feed
	public Question(String title, String date, String description, String author, String className, String totalPoints, int id){
		//Post
		this.description = description;
		this.author = author;
		dateTime = date;
		isReported = false;
		this.class_name = className;
		
		//Questions and Answers Only
		this.totalPoints = totalPoints;
		isVotable = true;
		comments = new ArrayList<Object>();
		this.id=id;
		
		//Answer
		this.title = title;
	}
	

	
	//Called by Answer Page
	public Question(String title, String dateTime, String description, String author, String class_name, String totalPoints, int id, ArrayList<Object> comments, int authorId, boolean hasCorrectAnswer){
		//Post
		
		this.description = description;
		this.author = author;
		this.dateTime = dateTime;
		this.isReported = false;
		
		//Questions and Answers Only
		this.totalPoints = totalPoints;
		this.isVotable = false;
		this.comments = comments;
		this.id=id;
		
		//Answer
		this.title = title;
		this.class_name = class_name;
		
		this.authorId = authorId;
		this.hasCorrectAnswer = hasCorrectAnswer;
	}

	public ArrayList<Object> getComments() {
		return comments;
	}



	public void setComments(ArrayList<Object> comments) {
		this.comments = comments;
	}



	@Override
	public String toString() {
		return "Question [description=" + description + ", author=" + author
				+ ", dateTime=" + dateTime + ", isReported=" + isReported
				+ ", totalPoints=" + totalPoints + ", isVotable=" + isVotable
				+ ", comments=" + comments + ", class_name=" + class_name
				+ ", title=" + title + ", id=" + id + "]";
	}

	public CharSequence getTitle() {
		// TODO Auto-generated method stub
		return this.title;
	}

	public CharSequence getAuthor() {
		// TODO Auto-generated method stub
		return this.author;
	}
	public CharSequence getDescription() {
		// TODO Auto-generated method stub
		return this.description;
	}
	
	public CharSequence getClassName() {
		// TODO Auto-generated method stub
		return this.class_name;
	}
	public CharSequence getDateTime() {
		// TODO Auto-generated method stub
		return this.dateTime;
	}
	public CharSequence getTotalPoints() {
		// TODO Auto-generated method stub
		return this.totalPoints;
	}
	
	public int getID(){
		return id;
	}



	public int getAuthorID() {
		// TODO Auto-generated method stub
		return this.authorId;
	}
}