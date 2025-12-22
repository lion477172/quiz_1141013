package com.example.quiz_1141013.vo;

import java.util.List;

import com.example.quiz_1141013.constants.ValidMsg;

import jakarta.validation.constraints.Min;

public class Answers {

	@Min(value = 1, message = ValidMsg.QUESTION_ID_ERROR)
	private int questionId;

	/* 不檢查 : 可能是簡答且非必答 */
	private List<AnswerVo> answerVoList;

	public Answers() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Answers( int questionId, List<AnswerVo> answerVoList) {
		super();
		this.questionId = questionId;
		this.answerVoList = answerVoList;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public List<AnswerVo> getAnswerVoList() {
		return answerVoList;
	}

	public void setAnswerVoList(List<AnswerVo> answerVoList) {
		this.answerVoList = answerVoList;
	}

}
