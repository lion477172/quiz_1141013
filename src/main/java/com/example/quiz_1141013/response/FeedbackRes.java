package com.example.quiz_1141013.response;

import java.util.List;

public class FeedbackRes  extends BasicRes{

	private List<Feedback> feedbackList;

	public FeedbackRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FeedbackRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public FeedbackRes(int code, String message, List<Feedback> feedbackList) {
		super(code, message);
		this.feedbackList = feedbackList;
	}

	public List<Feedback> getFeedbackList() {
		return feedbackList;
	}

	public void setFeedbackList(List<Feedback> feedbackList) {
		this.feedbackList = feedbackList;
	}
	
}
