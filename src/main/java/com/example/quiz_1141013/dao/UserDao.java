package com.example.quiz_1141013.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.quiz_1141013.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, String> {

	@Query(value ="select * from user where eamil = ?", nativeQuery = true)
	public User getUser(String email);
	
	@Query(value ="select * from user where eamil in(?)", nativeQuery = true)
	public List<User> getUsersIn(List<String> emailList);
}
