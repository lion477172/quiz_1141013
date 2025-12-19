package com.example.quiz_1141013.constants;

public class ValidMsg {

	// 加上 final (常數不可被修改) 是因為使用在 @Validation 中的 message 限制
	// 加上 static 是為了方便直接透過ValidMsg ，來呼叫此常數變數
	public static final String TITLE_ERROR = " 問卷名稱錯誤 ";

	public static final String DESRIPTION_ERROR = " 問卷描述錯誤 ";

	public static final String START_DATE_ERROR = " 開始時間錯誤 ";

	public static final String END_DATE_ERROR = " 結束時間錯誤 ";

	public static final String QUESTION_ERROR = " 選項錯誤 ";
	
	public static final String TYPE_ERROR = " Type錯誤 ";
	
	public static final String QUIZID_ERROR = " QuizID錯誤 ";
}
