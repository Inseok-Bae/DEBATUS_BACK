package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.IssueTopicMap;

public interface IssueTopicMapRepository extends JpaRepository<IssueTopicMap, Long> {

	List<IssueTopicMap> findByTopicId(Long id);
	
	List<IssueTopicMap> findByIssueId(Long id);
	
}
