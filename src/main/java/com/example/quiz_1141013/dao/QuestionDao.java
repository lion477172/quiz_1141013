package com.example.quiz_1141013.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz_1141013.entity.Question;
import com.example.quiz_1141013.entity.QuestionId;

@Repository
public interface QuestionDao extends JpaRepository<Question, QuestionId> {

	@Modifying
	@Transactional
	@Query(value = "insert into question(quiz_id, question_id,  question, type, required, options) values(?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
	public void addQuestion(int quizId, int questionId, String question, String type, boolean required, String options);

	@Query(value = "select * from question where quiz_id = ?1", nativeQuery = true)
	public List<Question> getQuestionById(int quizId);
	
	
	/**  刪除相同 quizId 的所有問題*/
	@Modifying
	@Transactional
	@Query(value = "delete from question where quiz_id = ?", nativeQuery = true)
	public void deleteByQuizId(int quizId);
}
