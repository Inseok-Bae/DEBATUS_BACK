package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TopicUserMap;

public interface TopicUserMapRepository extends JpaRepository<TopicUserMap, Long>{
	
	List<TopicUserMap> findAllByTopicId(Long topicId);
	List<TopicUserMap> findAllByUserId(String userId);
	
}
