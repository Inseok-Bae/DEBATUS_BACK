package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Opinion;
import com.example.demo.vi.opinionPreviewMapping;

public interface OpinionRepository extends JpaRepository<Opinion, Long>{

	List<opinionPreviewMapping> findByTitleLike(String title);
	List<opinionPreviewMapping> findByTextLike(String text);
	List<opinionPreviewMapping> findAllBy(Pageable pageable);
	List<opinionPreviewMapping> findByWriter(String writer);
	
	
	@Query(value="SELECT opinion_id, title, recommend_count from opinion o where o.opinion_id IN (:opinionIds)", nativeQuery = true)
	List<List<String>> findAllByOpinionIds(@Param("opinionIds") List<Long> opinionIds);
	
	
}
