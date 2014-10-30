package com.studyroom.model;


public class Point {
	private int value;
	private User reciever;
	private User giver;
	private String classcode;
	
	public Point(int value, String classcode, User reciever, User giver){
		this.value = value;
		this.classcode = classcode;
		this.reciever = reciever;
		this.giver = giver;
	}
	
	//Called by Parser Only
	public Point(){
		this.value = 0;
		this.classcode = null;
		this.reciever = null;
		this.giver = null;
	}
}
