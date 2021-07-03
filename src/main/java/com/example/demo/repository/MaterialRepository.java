package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Material;
import com.example.demo.vi.materialPreviewMapping;

public interface MaterialRepository extends JpaRepository<Material, Long>{
	
	List<materialPreviewMapping> findByTitleLike(String title);
	
}
