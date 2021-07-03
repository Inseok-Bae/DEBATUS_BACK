package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Image;

public interface ImageRepository extends JpaRepository<Image, String>{
	
}
