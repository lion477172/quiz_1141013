package com.example.quiz_1141013.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.quiz_1141013.constants.ResMessage;
import com.example.quiz_1141013.dao.FillinDao;
import com.example.quiz_1141013.dao.UserDao;
import com.example.quiz_1141013.entity.Fillin;
import com.example.quiz_1141013.entity.User;
import com.example.quiz_1141013.response.BasicRes;
import com.example.quiz_1141013.response.Feedback;
import com.example.quiz_1141013.response.FeedbackRes;
import com.example.quiz_1141013.response.StatisticsRes;
import com.example.quiz_1141013.vo.AnswerVo;
import com.example.quiz_1141013.vo.Answers;
import com.example.quiz_1141013.vo.OptionsCount;
import com.example.quiz_1141013.vo.Statistics;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.constraints.Email;

@Service
public class FeedbackService {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private FillinDao fillinDao;

	@Autowired
	private UserDao userDao;

	public FeedbackRes feedbackRes(int quizId) throws Exception {
		/* res 包含了多位使用者(email)的填答 */
		List<Fillin> res = fillinDao.getByQuizId(quizId);
		/* Map< email, List<Answers>> */
		Map<String, List<Answers>> map = new HashMap<>();
		List<Answers> ansList = new ArrayList<>();
		for (Fillin item : res) {
			try {
				/*
				 * 把 answer 轉換成物件 List<AnswrVo> 這邊一個 List<AnswerVo> 只包含了一個問題的 所有編號跟選項
				 */
				List<AnswerVo> voList = mapper.readValue(item.getAnswer(), new TypeReference<>() {
				});
				Answers ans = new Answers(item.getQuestionId(), voList);
				/* 把相同 email 對應的 List<Answers> 取出 */
				ansList = map.get(item.getEmail());
				if (CollectionUtils.isEmpty(ansList)) {
					/* 如果判斷式為真 --> 表示 map 中沒有該位使用者的 email */
					/* 清掉原本的 ansList */
					ansList = new ArrayList<>();
				}
				ansList.add(ans);
				map.put(item.getEmail(), ansList);
			} catch (Exception e) {
				throw e;
			}
		}
		List<Feedback> feedbackList = new ArrayList<>();
		for (String email : map.keySet()) {
			User user = userDao.getUser(email);
			feedbackList
					.add(new Feedback(user.getName(), user.getPhone(), email, user.getAge(), quizId, map.get(email)));
		}
		return new FeedbackRes(ResMessage.QUESTION_NOTFUND.getCode(), //
				ResMessage.QUESTION_NOTFUND.getMessage(), feedbackList);
	}

	/* 一次性撈取所有 email 對應的 User 資訊 --> 不管 email 有多少，就只會使用 userDao 一次 */
	private List<Feedback> getFeedbackList(List<Fillin> fillinList, int quizId, //
			Map<String, List<Answers>> map) {
		/* 蒐集同一張問卷下的所有 email */
		List<String> emailList = new ArrayList<>();
		fillinList.forEach(item -> {
			emailList.add(item.getEmail());
		});
		/* 一次性的撈取包含所有 email 的 User 資訊 */
		List<User> userList = userDao.getUsersIn(emailList);
		/* 生成所有 FeedbackRes */
		List<Feedback> feedbackList = new ArrayList<>();
		userList.forEach(item -> {
			feedbackList.add(new Feedback(item.getName(), item.getPhone(), item.getEmail(), //
					item.getAge(), quizId, map.get(item.getEmail())));
		});
		return feedbackList;
	}

	public StatisticsRes statistics(int quizId) throws Exception {
		/* res 包含了多位使用者(email)的填答 */
		List<Fillin> res = fillinDao.getByQuizId(quizId);
		/* Map<questionId,Map<code-optionName, count>> */
		Map<Integer, Map<String, Integer>> map = new HashMap<>();
		for (Fillin item : res) {
			try {
				/*
				 * 把 answer 轉換成物件 List<AnswrVo> 這邊一個 List<AnswerVo> 只包含了一個問題的 所有編號跟選項
				 */
				List<AnswerVo> voList = mapper.readValue(item.getAnswer(), new TypeReference<>() {
				});
				/*
				 * 從 voList 蒐集 code (選項編號)對應的 check 遍歷完之後，一個 codeCountMap 會有四筆資料 --> 編號1,count =
				 * 0, 編號2, count = 1, ....
				 */
				Map<String, Integer> codeCountMap = CollectionUtils.isEmpty(map.get(item.getQuestionId()))
						? new HashMap<>()
						: map.get(item.getQuestionId());

				voList.forEach(vo -> {
					/*
					 * 第一筆資料時， codeCounyMap 使用 code 當key 取出對應的 value 肯定是 null，因為沒資料， 會 null 的原因是
					 * codeCountMap 的資料型態是 Integer
					 */
					String str = String.valueOf(vo.getCode() + "-" + vo.getOptionName());
					int count = codeCountMap.get(str) == null ? 0 : codeCountMap.get(str);
					if (vo.isCheck()) {
						count++;
					}
					codeCountMap.put(str, count);
				});
				map.put(item.getQuestionId(), codeCountMap);
			} catch (Exception e) {
				throw e;
			}
		}
		/* 把 map 轉成 List<Statistics> */
		List<Statistics> list = new ArrayList<>();
		map.forEach((k, v) -> {
			/* v 就是 M<code-optionName, count> */
			List<OptionsCount> opCountList = new ArrayList<>();
			v.forEach((k1, v1) -> {
				/* array = [code, optionName] */
				String[] array = k1.split("-");
				/* array[0] 是選項編號(code)，要把其資料型態轉回 int */
				OptionsCount opCcount = new OptionsCount(Integer.valueOf(array[0]), array[1], v1);
				opCountList.add(opCcount);
			});
			Statistics st = new Statistics(k, opCountList);
			list.add(st);
		});
		return new StatisticsRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), list);
	}

	public StatisticsRes statistics_test(int quizId) throws Exception {
	    /* res 包含了多位使用者(email)的填答 */
	    List<Fillin> res = fillinDao.getByQuizId(quizId);
	    
	    /* Map<questionId, List<OptionsCount>> */
	    Map<Integer, List<OptionsCount>> map = new HashMap<>();

	    for (Fillin item : res) {
	        try {
	            /* 把字串 answer 轉換成物件 List<AnswerVo> */
	            List<AnswerVo> voList = mapper.readValue(item.getAnswer(), new TypeReference<List<AnswerVo>>() {});

	            /* 取得目前該問題已統計的 List，若無則 new 一個新的 */
	            List<OptionsCount> opCountList = map.get(item.getQuestionId());
	            if (opCountList == null) {
	                opCountList = new ArrayList<>();
	            }

	            /* 遍歷使用者的回答 */
	            for (AnswerVo vo : voList) {
	                /* 有選才統計 */
	                if (vo.isCheck()) {
	                    boolean isExist = false; // 標記該選項是否已存在於統計清單中

	                    /* 遍歷目前的統計清單，找找看有沒有這個選項代碼 */
	                    for (OptionsCount op : opCountList) {
	                        if (op.getCode() == vo.getCode()) {
	                            // 【Bug A 修復】: 這裡是累加次數，不是修改編號，改成 setCount
	                            op.setCount(op.getCount() + 1);
	                            isExist = true;
	                            break; // 找到就不用繼續找了
	                        }
	                    }

	                    /* 【Bug B 修復】: 如果跑完一輪都沒找到 (isExist 為 false)，代表這是新選項，要加入 */
	                    if (!isExist) {
	                        // 假設第一次出現，次數為 1
	                        opCountList.add(new OptionsCount(vo.getCode(), vo.getOptionName(), 1));
	                    }
	                }
	            }

	            /* 更新 Map */
	            map.put(item.getQuestionId(), opCountList);

	        } catch (Exception e) {
	            throw e;
	        }
	    }

	    /* 把 map 轉成 List<Statistics> 回傳 */
	    List<Statistics> list = new ArrayList<>();
	    map.forEach((k, v) -> {
	        list.add(new Statistics(k, v));
	    });

	    return new StatisticsRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), list);
	}

}
