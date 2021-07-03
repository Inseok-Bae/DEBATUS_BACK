package com.example.demo.requiredController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Issue;
import com.example.demo.repository.IssueRepository;
import com.example.demo.service.IssueService;
import com.example.demo.vi.issuePreviewMapping;

@CrossOrigin
@RestController
@RequestMapping("/api/required/issue")
public class IssueRequiredController {

	@Autowired
	IssueRepository issueRepository;
	
	@Autowired
	IssueService issueService;
	
	@PostMapping
	public Long postIssue(@RequestBody Issue issue, @RequestHeader("jwt-auth-token") String token) {
		return issueService.postIssue(issue, token);
	}
	
	@GetMapping("/byUserId")
	public List<issuePreviewMapping> getIssuesByUserId(@RequestHeader("jwt-auth-token") String token) {
		return issueService.findByWriter(token);
	}
	
}
