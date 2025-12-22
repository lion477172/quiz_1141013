package com.example.quiz_1141013.request;

import java.util.List;

import com.example.quiz_1141013.constants.ValidMsg;
import com.example.quiz_1141013.vo.Answers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class FillinReq {

	@NotBlank(message = ValidMsg.USER_NAME_IS_EMPTY)
	private String name;

	private String phone;

	@NotBlank(message = ValidMsg.USER_EMAIL_IS_EMPTY)
	private String email;

	@Min(value = 18, message = ValidMsg.USER_AGE_ERROR)
	private int age;

	@Min(value = 1, message = ValidMsg.QUIZID_ERROR)
	private int quizId;

	@Valid
	public List<Answers> answersList;

	public FillinReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FillinReq(String name, String phone, String email, int age, int quizId, List<Answers> answersList) {
		super();
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.quizId = quizId;
		this.answersList = answersList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public List<Answers> getAnswersList() {
		return answersList;
	}

	public void setAnswersList(List<Answers> answersList) {
		this.answersList = answersList;
	}

}
