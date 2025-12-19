package com.example.quiz_1141013.request;

import com.example.quiz_1141013.constants.ValidMsg;

import jakarta.validation.constraints.Min;

public class QuizUpdateReq extends QuizCreateReq {
	
	/*因為更新是更新已存在的問卷， quiz_id 問卷*/
	@Min(value = 1, message = ValidMsg.QUIZID_ERROR)
	private int quizId;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}
	
}
