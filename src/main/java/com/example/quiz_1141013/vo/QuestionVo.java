package com.example.quiz_1141013.vo;

import java.util.ArrayList;
import java.util.List;

import com.example.quiz_1141013.constants.ValidMsg;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class QuestionVo {

	private int quizId;

	private int questionId;

	@NotBlank(message = ValidMsg.QUESTION_ERROR)
	private String question;

	@NotBlank(message = ValidMsg.TYPE_ERROR)
	private String type;

	private boolean required;

	/* 嵌套驗證 : AddInfoReq2 有註解 */
	@Valid
	/*
	 * 不限制 因為會有簡答題的存在他沒有選項 給定空的ArrayList 可預防這個屬性沒有 mapping 到時會有 null --> 及預設值會從 null
	 * 變成空的 List
	 */
	private List<Options> optionsList = new ArrayList<>();

	public QuestionVo() {
		super();
	}

	public QuestionVo(int quizId, int questionId, String question, String type, boolean required,
			@Valid List<Options> optionsList) {
		super();
		this.quizId = quizId;
		this.questionId = questionId;
		this.question = question;
		this.type = type;
		this.required = required;
		this.optionsList = optionsList;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public List<Options> getOptionsList() {
		return optionsList;
	}

	public void setOptionsList(List<Options> optionsList) {
		this.optionsList = optionsList;
	}

}
