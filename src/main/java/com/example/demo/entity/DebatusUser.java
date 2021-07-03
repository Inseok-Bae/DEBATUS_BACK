package com.example.demo.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class DebatusUser {
	
	@Id
	@Column(length = 20)
	String userId;
	
	@Column(length = 20)
	String password;
	
	@Column(length = 20)
	String name;
	
	@Column(length = 20)
	String major;
	
	@Column(length = 12)
	String studentId;
	
	@Column(length = 30)
	String email;
	
	@Column(updatable = false)
	@CreationTimestamp
	Timestamp registerDate;
	
	@UpdateTimestamp
	Timestamp modifiedDate;
}
