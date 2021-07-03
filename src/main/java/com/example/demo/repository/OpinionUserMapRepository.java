package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.OpinionUserMap;

public interface OpinionUserMapRepository extends JpaRepository<OpinionUserMap, Long>{
	
	List<OpinionUserMap> findAllByOpinionId(Long opinionId);
	List<OpinionUserMap> findAllByUserId(String userId);
	
}
