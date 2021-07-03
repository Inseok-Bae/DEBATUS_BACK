package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.DebatusUser;

public interface DebatusUserRepository extends JpaRepository<DebatusUser, String>{
	
}
