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
public class Opinion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long opinionId;
	
	@Column(length = 100)
	String title;
	
	@Column(columnDefinition = "TEXT")
	String text;
	
	@Column(length = 20)
	String writer;
	
	Long recommendCount;
		
	@Column(columnDefinition = "TEXT")
	String materialIds;
	
	@Column(updatable = false)
	@CreationTimestamp
	Timestamp writtenDate;
	
	@UpdateTimestamp
	Timestamp modifiedDate;
}
