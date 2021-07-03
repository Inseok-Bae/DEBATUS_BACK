package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Opinion;
import com.example.demo.repository.OpinionRepository;
import com.example.demo.service.OpinionService;
import com.example.demo.vi.opinionPreviewMapping;

@CrossOrigin
@RestController
@RequestMapping("/api/opinion")
public class OpinionController {

	@Autowired
	OpinionRepository opinionRepository;
	
	@Autowired
	OpinionService opinionService;
	
	@GetMapping
	public List<Opinion> getAllOpinions() {
		return opinionRepository.findAll();
	}
	
	@GetMapping("/byPage")
	public Page<Opinion> getAllOpinionsByPage(Pageable pageable) {
		return opinionRepository.findAll(pageable);
	}
	
	@GetMapping("/byPagePreview")
	public Map<String, Object> getAllOpinionPreviewsByPage(Pageable pageable) {
		return opinionService.getPreviews(pageable);
	}
	
	@GetMapping("/byContent")
	public List<opinionPreviewMapping> getOpinionsByContent(@RequestParam("title") String title, @RequestParam("text") String text) {
		return opinionService.getOpinionsByContent(title, text);
	}
	
	@GetMapping("/byId")
	public Opinion getOpinionById(@RequestParam("id") Long id) {
		return opinionRepository.findById(id).orElseThrow();
	}
	
	@GetMapping("/groupById")
	public List<Opinion> getOpinionGroupById(@RequestParam("id") Long id) {
		return opinionService.getOpinionGroupById(id);
	}
}
