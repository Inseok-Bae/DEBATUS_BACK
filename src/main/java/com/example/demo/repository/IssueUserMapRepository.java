package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.IssueUserMap;

public interface IssueUserMapRepository extends JpaRepository<IssueUserMap, Long>{
	
	List<IssueUserMap> findAllByIssueId(Long issueId);
	List<IssueUserMap> findAllByUserId(String userId);
	
}
