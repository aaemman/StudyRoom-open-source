package com.studyroom.model;

//import java.awt.image.BufferedImage;

public class User {
	private String name;
	// private BufferedImage picture;
	private String joinDate;
	private String totalPoints;
	private String correctAnswerCount;
	private String percentCorrectAnswer;
	private String answerCount;
	private String questionsCount;
	private String commentCount;

	// Called by Parser Only
	public User(String name, String createdAt, String totalPoints,
			String questionCount, String answerCount,
			String correctAnswerCount, String percentCorrectAnswerCount,
			String commentCount) {
		this.name = name;
		this.joinDate = createdAt;
		this.totalPoints = totalPoints;
		this.correctAnswerCount = correctAnswerCount;
		this.percentCorrectAnswer = percentCorrectAnswerCount;
		this.answerCount = answerCount;
		this.questionsCount = questionCount;
		this.commentCount = commentCount;
	}
	
	public User(String name, String points){
		this.name = name;
		this.totalPoints = points;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(String totalPoints) {
		this.totalPoints = totalPoints;
	}

	public String getCorrectAnswerCount() {
		return correctAnswerCount;
	}

	public void setCorrectAnswerCount(String correctAnswerCount) {
		this.correctAnswerCount = correctAnswerCount;
	}

	public String getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(String answerCount) {
		this.answerCount = answerCount;
	}

	public String getQuestionsCount() {
		return questionsCount;
	}

	public void setQuestionsCount(String questionsCount) {
		this.questionsCount = questionsCount;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
	
	public String getPercentCorrectAnswer() {
		return percentCorrectAnswer;
	}

	public void setPercentCorrectAnswer(String percentCorrectAnswer) {
		this.percentCorrectAnswer = percentCorrectAnswer;
	}
}
