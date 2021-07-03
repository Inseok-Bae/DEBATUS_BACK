package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.OpinionOpinionMap;

public interface OpinionOpinionMapRepository extends JpaRepository<OpinionOpinionMap, Long> {

	List<OpinionOpinionMap> findByOpinionId1OrOpinionId2(Long id, Long id2);
	
}
