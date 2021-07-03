package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Issue;
import com.example.demo.entity.IssueUserMap;
import com.example.demo.entity.DebatusUser;
import com.example.demo.jwt.JwtService;
import com.example.demo.repository.IssueUserMapRepository;
import com.example.demo.repository.DebatusUserRepository;

@Service
public class UserService {
	
	@Autowired
	DebatusUserRepository userRepository;
	
	@Autowired
	IssueUserMapRepository issueUserMapRepository;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	IssueService issueService;
	
	@Value("${requiredRelatedOpinionCount}")
	private Long requiredRelatedOpinionCount;
	
	public Map<String, String> logIn(String id, String password) {
		Optional<DebatusUser> _userInfo = userRepository.findById(id);
		DebatusUser userInfo = _userInfo.orElse(new DebatusUser());
		
		Map<String, String> result = new HashMap<>();
		
		if (userInfo.getUserId() == null) {
			result.put("permit", "false");
			result.put("reason", "존재하지 않는 ID입니다.");
		} else if (!userInfo.getPassword().equals(password)) {
			result.put("permit", "false");
			result.put("reason", "비밀번호가 일치하지 않습니다.");
		} else {
			result.put("permit", "true");
			String jwtToken = jwtService.create(id, password);
			result.put("token", jwtToken);
		}
		return result;
	}
	
	public List<Issue> getPublishableIssues(String token) {
		
		String userId = (String) jwtService.get(token).get("userId");
		List<IssueUserMap> maps = issueUserMapRepository.findAllByUserId(userId);
		
		List<Long> issueIds = new ArrayList<>();
		for (IssueUserMap map : maps) {
			issueIds.add(map.getIssueId());
		};
		
		List<Issue> issues = issueService.findByIssueIds(issueIds);
		
		List<Issue> results = new ArrayList<>();
		
		for (Issue issue : issues) {
			if (issue.getConsCount() + issue.getProsCount() >= requiredRelatedOpinionCount) {
				results.add(issue);
			}
		}
		
		return results;
		
	}
}
