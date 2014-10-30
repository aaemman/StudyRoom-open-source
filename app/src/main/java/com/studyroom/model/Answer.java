package com.studyroom.model;
import java.util.ArrayList;


public class Answer {
	//Post
	private String description;
	private String dateTime;
	//Questions and Answers Only
	private int totalPoints;
	//Answer
	private boolean isCorrect;
	public int id;
	public int question_id;
	public int user_id;
	String author;
	ArrayList<Object> comments;
	
	public Answer(String date, String description, String author,String totalPoints, int id, int question_id, String isCorrect, ArrayList< Object> comments){
		//Post
		this.description = description;
		dateTime = date;
		
		//Questions and Answers Only
		this.totalPoints = Integer.parseInt(totalPoints);
		this.author=author;
		this.id=id;
		
		if(isCorrect.equals("true"))
			this.isCorrect=true;
		else
			this.isCorrect=false;
		
		this.question_id = question_id;
		
		setComments(comments);
		
	}
	
	public ArrayList<Object> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Object> comments) {
		this.comments = comments;
	}

	//Called by Parser Only
	public Answer(){
		this.description = null;
		dateTime = null;
		
		
		
		//Answer
		isCorrect = false;
	}
	

	public String getDateTime() {
		// TODO Auto-generated method stub
		return this.dateTime;
	}
	public int getTotalPoints() {
		// TODO Auto-generated method stub
		return this.totalPoints;
	}
	
	public int getID(){
		return id;
	}
	public int getQuestionID(){
		return question_id;
	}
	public String getDescription(){
		return this.description;
	}
	public boolean getIsCorrect(){
		return this.isCorrect;
	}
	public String getAuthor(){
		return this.author;
	}
	
	
	
}