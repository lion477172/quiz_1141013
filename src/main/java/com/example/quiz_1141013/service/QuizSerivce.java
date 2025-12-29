package com.example.quiz_1141013.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.quiz_1141013.constants.ResMessage;
import com.example.quiz_1141013.constants.Type;
import com.example.quiz_1141013.dao.QuestionDao;
import com.example.quiz_1141013.dao.QuizDao;
import com.example.quiz_1141013.entity.Question;
import com.example.quiz_1141013.request.QuizCreateReq;
import com.example.quiz_1141013.request.QuizUpdateReq;
import com.example.quiz_1141013.response.BasicRes;
import com.example.quiz_1141013.response.GetListRes;
import com.example.quiz_1141013.response.GetQuestionRes;
import com.example.quiz_1141013.vo.Options;
import com.example.quiz_1141013.vo.QuestionVo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuizSerivce {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	/* rollbackFor = Exception.class : 表示只要此方法發生的 Exception 寫一半的資料都會讓資料回滾 */
	@Transactional(rollbackFor = Exception.class)
	public BasicRes create(QuizCreateReq req) throws Exception {
		BasicRes checkRes = check(req);
		/* 方法 check 的結果只會有2種結果，null 和 非null (BasicRes的回傳) ，非 null 的結果 表示檢查有錯*/
		if(checkRes != null) {
			/* 把檢查有錯的結果直接 return 出去*/
			return checkRes;
		}
		/* 新增問卷 */
		quizDao.addQuiz(req.getTitle(), req.getDescription(), req.getStartDate(), req.getEndDate(), req.isPublished());
		/* 取得最新的 quiz_id 編號 */
		int quizId = quizDao.getMaxId();
		/* 將question資料寫進 DB */
		for (QuestionVo vo : req.getQuestionVoList()) {
			/* 要把 vo 中的 List<Options> 轉換成字串，不能用 to.String，寫得進去但轉不回來原本格式 */
			try {
				String optionsListStr = mapper.writeValueAsString(vo.getOptionsList());
				questionDao.addQuestion(quizId, vo.getQuestionId(), vo.getQuestion(), vo.getType(), vo.isRequired(),
						optionsListStr);
			} catch (Exception e) {
				/* throw 要拋給別人的 他會紅蚯蚓 在除錯就好 */
				throw e;
			}
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	private BasicRes check(QuizCreateReq req) {
		/* 排除開始時間比結束時間晚 或 開始時間比當天早 */
		if (req.getStartDate().isAfter(req.getEndDate()) || req.getStartDate().isBefore(LocalDate.now())) {
			return new BasicRes(ResMessage.DATE_ERROR.getCode(), ResMessage.DATE_ERROR.getMessage());
		}
		List<QuestionVo> voList = req.getQuestionVoList();
		for (QuestionVo vo : voList) {
			/* 排除非固定3種 type 的驗證 */
			// 前面有驚嘆號，等同於 Type.checkType(vo.getType());
			if (!Type.checkType(vo.getType())) {
				return new BasicRes(ResMessage.TYPE_ERROR.getCode(), ResMessage.TYPE_ERROR.getMessage());
			}
			/* type 是選擇題的時候，選項至少要有一個 */
			if (Type.isChosenType(vo.getType())) {
				if (vo.getOptionsList().size() < 1) {
					return new BasicRes(ResMessage.OPTIONS_SIZE_ERROR.getCode(),
							ResMessage.OPTIONS_SIZE_ERROR.getMessage());
				}
			} else { /* type 是簡答題的時候，不能有選項 */
				if (!vo.getOptionsList().isEmpty()) {
					return new BasicRes(ResMessage.OPTIONS_SIZE_ERROR.getCode(),
							ResMessage.OPTIONS_SIZE_ERROR.getMessage());
				}
			}
		}
		return null;
	}
	@Transactional(rollbackFor = Exception.class)
	public BasicRes update(QuizUpdateReq req) throws Exception {
		/* 方法 check 中的參數資料型態是 QuestionCreateReq，對 QuizUpdateReq 來說是父類別，
		 * 若把子類別 QuizUpdateReq 當參數 check 中，資料型態會自動轉型成父類別 QuestionCreateReq
		 * 即 check((QuizCreateReq) req), 這樣的結果差別只會是在於子類別中的屬性 quizId 都會是預設值 0，
		 * 但不影響方法 check 的檢查，因為沒用到 quizId*/
		BasicRes checkRes = check(req);
		/* 方法 check 的結果只會有2種結果，null 和 非null (BasicRes的回傳) ，非 null 的結果 表示檢查有錯*/
		if(checkRes != null) {
			/* 把檢查有錯的結果直接 return 出去*/
			return checkRes;
		}
		/* 檢查 quizId && QuestionVo 中的 quizId 是否一樣*/
		for(QuestionVo vo : req.getQuestionVoList()) {
			if(req.getQuizId() != vo.getQuizId()) {
				return new BasicRes(ResMessage.QUIZ_ID_MISMATCH.getCode(), ResMessage.QUIZ_ID_MISMATCH.getMessage());
			}
		}
		/* 更新*/
		int updateRes = quizDao.update(req.getQuizId(), req.getTitle(), req.getDescription(), req.getStartDate(), req.getEndDate(), req.isPublished());
		/* 有找到 quizId 並更新成功(即使要更新的資料與 DB 中的資料都一樣)，會回傳 1 ( where 條件帶的是 PK)*/
		if(updateRes != 1) {
			return new BasicRes(ResMessage.QUIZ_NOTFUND.getCode(), ResMessage.QUIZ_NOTFUND.getMessage());
		}
		/* 確定 quizID 有存在 --> 先刪除問題 --> 再新增問題*/
		questionDao.deleteByQuizId(req.getQuizId());
		for (QuestionVo vo : req.getQuestionVoList()) {
			/* 要把 vo 中的 List<Options> 轉換成字串，不能用 to.String，寫得進去但轉不回來原本格式 */
			try {
				String optionsListStr = mapper.writeValueAsString(vo.getOptionsList());
				/*寫進DB*/
				questionDao.addQuestion(vo.getQuizId(), vo.getQuestionId(), vo.getQuestion(), vo.getType(), vo.isRequired(),
						optionsListStr);
			} catch (Exception e) {
				/* throw 要拋給別人的 他會紅蚯蚓 在除錯就好 */
				throw e;
			}
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	/*全部問卷資料*/
	public GetListRes getAll() {
		return new GetListRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), //
				quizDao.getAll());
	}
	/*全部問卷裡用 keyword 找對應問卷*/
	public GetListRes getAll(String keyword, LocalDate startDate, LocalDate endDate) {
		/*
		 * 把 keyword 是 null (沒有輸入值) 或 空字串 或全空白字串， 轉成空字串 目的是後面再取資料時會使用 like %%,
		 * %%中間是空字串時，也會撈全部
		 */
		if (!StringUtils.hasText(keyword)) {
			keyword = "";
		}
		if (startDate == null) {
			startDate = LocalDate.of(1970, 1, 1);
		}
		if (endDate == null) {
			endDate = LocalDate.of(9999, 12, 31);
		}
		return new GetListRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), //
				quizDao.getAll(keyword, startDate, endDate));
	}

/*去取 list<QuestionVo> 的資料 要去Res 新增 list < QuestionVo > */
	public GetQuestionRes getQuestionByQuizId(int quizId) throws Exception {
		List<Question> list = questionDao.getQuestionById(quizId);
		List<QuestionVo> questionVoList = new ArrayList<>();
		/* 把 Question 中的每個字串 options 轉換成自定義的物件Options */
		for (Question item : list) {
			/* 轉換過程 */
			try {
				List<Options> opList = mapper.readValue(item.getOptions(), new TypeReference<>() {
				});
				/* 把 Question 中每個屬性值以及 oplist , set 到 QuestionVo 對應的屬性位置 */
				QuestionVo vo = new QuestionVo(quizId, item.getQuestionId(), item.getQuestion(), //
						item.getType(), item.isRequired(), opList);
				/* 把每個 vo 加到 questionVoList */
				questionVoList.add(vo);
			} catch (Exception e) {
				throw e;
			}
		}
		return new GetQuestionRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), questionVoList);
	}
	@Transactional(rollbackFor = Exception.class)
	public BasicRes getQuidIdDelQuestions1(int quizId)  {
		try {
	        // 刪除題目 (子表)
	        quizDao.getQuidIdDelQuestions1(quizId);
	        
	        // 刪除問卷 (主表)
	        int result = quizDao.getQuidIdDelQuestions2(quizId);
	        
	        if (result > 0) {
	            return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	        } else {
	            // 如果主表沒被刪除（可能是 ID 不存在），回傳找不到
	            return new BasicRes(ResMessage.NOT_FOUND.getCode(), "找不到該問卷 ID，刪除失敗");
	        }
	    } catch (Exception e) {
	       throw e; 
	    }
}
}
