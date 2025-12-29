package com.example.quiz_1141013.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_1141013.request.QuizCreateReq;
import com.example.quiz_1141013.request.QuizUpdateReq;
import com.example.quiz_1141013.response.BasicRes;
import com.example.quiz_1141013.response.GetListRes;
import com.example.quiz_1141013.response.GetQuestionRes;
import com.example.quiz_1141013.response.StatisticsRes;
import com.example.quiz_1141013.service.FeedbackService;
import com.example.quiz_1141013.service.FillinService;
import com.example.quiz_1141013.service.QuizSerivce;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class QuizController {

	@Autowired
	private QuizSerivce quizSerivce;
	
	@Autowired
	private FeedbackService feedbackService;
	
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

	/* 刪除問卷 */
	@DeleteMapping("quiz/delete")
	public BasicRes getQuidIdDelQuestions1(@RequestParam(value = "quiz_id", required = true) int quizId) {
		return quizSerivce.getQuidIdDelQuestions1(quizId);
	}

	/* 顯示結果*/
	@GetMapping("quiz/statistics")
	public StatisticsRes statistics_test(@RequestParam(value = "quiz_id", required = true) int quizId) throws Exception {
		return feedbackService.statistics_test(quizId);
	}
	@GetMapping("quiz/statisticsQ")
	public StatisticsRes statistics(@RequestParam(value = "quiz_id", required = true) int quizId) throws Exception {
		return feedbackService.statistics(quizId);
	}

}
