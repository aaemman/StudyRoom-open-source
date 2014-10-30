package com.studyroom.model;
public class Leaderboard {
	private User[] users;
	private Point[] points;
	
	public Leaderboard(){
		users = new User[5];
		points = new Point[5];
	}
}
