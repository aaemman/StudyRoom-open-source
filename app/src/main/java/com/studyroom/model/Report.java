package com.studyroom.model;

public class Report {
	private String reportableType;
	private int reportableId;
	private User reporter;
	private User reportee;
	private String description;
	
	public Report(String type, int id, User reporter, User reportee, String description){
		this.reportableType = type;
		this.reportableId = id;
		this.reporter = reporter;
		this.reportee = reportee;
		this.description = description;
	}

	//Called by Parser Only
	public Report(){
		this.reportableType = null;
		this.reportableId = 0;
		this.reporter = null;
		this.reportee = null;
		this.description = null;
	}
}
