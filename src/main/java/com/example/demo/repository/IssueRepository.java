package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Issue;
import com.example.demo.vi.issuePreviewMapping;

public interface IssueRepository extends JpaRepository<Issue, Long>{

	List<Issue> findByTitleLike(String title);

	List<issuePreviewMapping> findAllBy(Pageable pageable);
	
	List<issuePreviewMapping> findByWriter(String writer);
	
	@Query(value="SELECT issue_id, title from issue i where i.issue_id IN (:issueIds)", nativeQuery = true)
	List<List<Object>> findAllByIssueIds(@Param("issueIds") List<Long> issueIds);
	
	Issue findByRelatedTopic(Long relatedTopic);
	
}
