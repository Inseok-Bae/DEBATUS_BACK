package com.example.demo.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long topicId;
	
	@Column(length = 100)
	String title;
	
	@Column(columnDefinition = "TEXT")
	String text;
	
	@Column(length = 20)
	String writer;
	
	Integer votingCount;
	Integer answeredFlag;
	Timestamp answeredDate;
	
	@Column(length = 50)
	String answeredTitle;
	
	@Column(columnDefinition = "TEXT")
	String answeredText;
	
	@Column(updatable = false)
	@CreationTimestamp
	Timestamp writtenDate;
	
	@UpdateTimestamp
	Timestamp modifiedDate;
}
