package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Material;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.service.MaterialService;
import com.example.demo.vi.materialPreviewMapping;

@CrossOrigin
@RestController
@RequestMapping("/api/material")
public class MaterialController {

	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	MaterialService materialService;
	
	@GetMapping("/one")
	public Material getMaterial(@RequestParam("id") Long id) {
		return materialRepository.findById(id).orElseThrow();
	}
	
	@GetMapping
	public List<Material> getMaterials(@RequestParam("ids") String ids) {
		return materialService.getMaterials(ids);
	}
	
	@GetMapping("/byContent")
	public List<materialPreviewMapping> getMaterialsByContent(@RequestParam("title") String title) {
		return materialRepository.findByTitleLike("%"+title+"%");
	}
	
}
