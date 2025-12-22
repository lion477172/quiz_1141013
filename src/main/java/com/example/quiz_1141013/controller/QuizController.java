package com.example.quiz_1141013.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_1141013.request.FillinReq;
import com.example.quiz_1141013.request.QuizCreateReq;
import com.example.quiz_1141013.request.QuizUpdateReq;
import com.example.quiz_1141013.response.BasicRes;
import com.example.quiz_1141013.response.GetListRes;
import com.example.quiz_1141013.response.GetQuestionRes;
import com.example.quiz_1141013.service.QuizSerivce;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class QuizController {

	@Autowired
	private QuizSerivce quizSerivce;

	/* 新增問卷 */
	@PostMapping("quiz/create")
	public BasicRes create(@Valid @RequestBody QuizCreateReq req) throws Exception {
		return quizSerivce.create(req);
	}

	/* 更新問卷 */
	@PostMapping("quiz/update")
	public BasicRes update(@Valid @RequestBody QuizUpdateReq req) throws Exception {
		return quizSerivce.update(req);
	}

	/* 全部問卷 */
	@GetMapping("quiz/getAll")
	public BasicRes getAll() {
		return quizSerivce.getAll();
	}

	/* 搜尋問卷 */
	@GetMapping("quiz/get_fillter_data")
	public GetListRes getAll(@RequestParam("keyword") String keyword, //
			@RequestParam("startDate") LocalDate startDate, //
			@RequestParam("endDate") LocalDate endDate) {
		return quizSerivce.getAll(keyword, startDate, endDate);
	}

	/* quizId取問卷內容 */
	@GetMapping("quiz/getQuestionByQuizId")
	public GetQuestionRes getQuestionByQuizId( //
			@RequestParam(value = "quiz_id", required = true) int quizId) throws Exception {
		return quizSerivce.getQuestionByQuizId(quizId);
	}

}
