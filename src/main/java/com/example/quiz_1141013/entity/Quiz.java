package com.example.quiz_1141013.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "quiz")
public class Quiz {

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	/*
	 * 屬性變數名稱，即使資料型態是 boolean，也不能用 is 開頭，會影響到 getter 方法的命名 published
	 * 的資料型態是boolean，所以 getter 的方法名稱預設是 isPublished，若是變數名稱是 isPublished， 正確的 getter
	 * 方法名稱應該是 isIsPublished，但IDE 自動產生的 getter 方法名稱也是 isPublished，
	 * 影響結果會是資料庫中該欄位的值無法被放到變數這個容器中，所以永遠是預設值 false (0)。
	 */
	@Column(name = "published")
	private boolean published;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

}
