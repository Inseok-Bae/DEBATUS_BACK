package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.OpinionIssueMap;

public interface OpinionIssueMapRepository extends JpaRepository<OpinionIssueMap, Long> {
	
	List<OpinionIssueMap> findByIssueId(Long issueId);
	List<OpinionIssueMap> findByOpinionId(Long id);
	
	@Query("SELECT oi from OpinionIssueMap oi where oi.opinionId IN (:opinionIds)")
	List<OpinionIssueMap> findByOpinionIds(@Param("opinionIds") List<Long> opinionIds);

}
