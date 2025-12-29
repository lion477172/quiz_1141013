package com.example.quiz_1141013.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz_1141013.entity.Fillin;
import com.example.quiz_1141013.entity.FillinId;

@Repository
public interface FillinDao extends JpaRepository<Fillin, FillinId> {

	@Modifying
	@Transactional
	@Query(value= "insert into fillin (quiz_id, question_id, email, answer) values (?1, ?2, ?3, ?4)", nativeQuery = true)
	public void insert(int quizId, int questionId, String email, String answer);

	@Query(value= "select * from fillin where quiz_id = ?", nativeQuery = true)
	public List<Fillin> getByQuizId(int quizId);
	
//	從 fillin 資料表中篩選出對應quiz_id表單中，所有『非簡答題』類型的題目資料
@Query(value = "select * from fillin where quiz_id = ?1 and question_id "
			+ " in (select question_id from question where quiz_id = ?1 and type != 'short-answer')", // 根據自己的 constants裡面 Type.java 的簡答題 type 去修改，照理說會是text
			nativeQuery = true)
	public List<Fillin> getByQuizIdWithoutText(int quizId);
}
