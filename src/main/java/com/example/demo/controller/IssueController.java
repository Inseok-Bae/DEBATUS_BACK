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

import com.example.demo.entity.Issue;
import com.example.demo.repository.IssueRepository;
import com.example.demo.service.IssueService;
import com.example.demo.vo.TopicRelatedIssueModel;

@CrossOrigin
@RestController
@RequestMapping("/api/issue")
public class IssueController {

	@Autowired
	IssueRepository issueRepository;
	
	@Autowired
	IssueService issueService;
	
	@GetMapping
	public List<Issue> getAllIssues() {
		return issueRepository.findAll();
	}
	
	@GetMapping("/byPage")
	public Page<Issue> getAllIssuesByPage(Pageable pageable) {
		return issueRepository.findAll(pageable);
	}
	
	@GetMapping("/byPagePreview")
	public Map<String, Object> getAllIssuePreviewsByPage(Pageable pageable) {
		return issueService.getPreviews(pageable);
	}
	
	@GetMapping("/byContent")
	public List<Issue> getIssuesByContent(@RequestParam("title") String title) {
		return issueRepository.findByTitleLike("%"+title+"%");
	}
	
	@GetMapping("/byId")
	public Issue getIssueById(@RequestParam("id") Long id) {
		return issueRepository.findById(id).orElseThrow();
	}
	
	@GetMapping("/preview/byTopicId")
	public List<Object> getRelatedIssuePreviewsByTopic(@RequestParam("id") Long id) {
		return issueService.getRelatedIssuePreviewsByTopic(id);
	}
	
	@GetMapping("/byTopicId")
	public List<TopicRelatedIssueModel> getRelatedIssuesByTopic(@RequestParam("id") Long id) {
		return issueService.getIssueByTopicId(id);
	}
	
	@GetMapping("/byOpinionId")
	public List<Object> getRelatedIssuesByOpinion(@RequestParam("id") Long id) {
		return issueService.getIssueByOpinionId(id);
	}
	
	@GetMapping("/byOpinionId/detail")
	public List<Object> getRelatedDetailIssuesByOpinion(@RequestParam("id") Long id) {
		return issueService.getDetailIssueByOpinionId(id);
	}
	
	@GetMapping("/byOpinionId/all")
	public List<Issue> getAllIssuesByOpinion(@RequestParam("id") Long id) {
		return issueService.getAllIssuesByOpinionId(id);
	}
	
	@GetMapping("/getTopicFeedBackIssue")
	public Issue getTopicFeedBackIssue(@RequestParam("topicId") Long topicId) {
		return issueService.getTopicFeedbackIssue(topicId);
	}

}
