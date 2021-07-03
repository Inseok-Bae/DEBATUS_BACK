package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Issue;
import com.example.demo.entity.Opinion;
import com.example.demo.entity.OpinionIssueMap;
import com.example.demo.repository.IssueRepository;
import com.example.demo.repository.OpinionIssueMapRepository;
import com.example.demo.repository.OpinionRepository;
import com.example.demo.vo.SubViewModel;

@Service
public class DataService {
	
	@Autowired
	IssueRepository issueRepository;
	
	@Autowired
	OpinionRepository opinionRepository;
	
	@Autowired
	OpinionIssueMapRepository opinionIssueMapRepository; 
	
	public SubViewModel getSubViewModels(Long id, String type) {
		
		SubViewModel result = new SubViewModel();
		
		List<OpinionIssueMap> maps = new ArrayList<>();
		
		if (type.equals("byIssue")) {
			Issue issue = issueRepository.findById(id).orElseThrow();
			result.setIssue(issue);
			
			maps = opinionIssueMapRepository.findByIssueId(id);
		} else {
			Issue issue = issueRepository.findByRelatedTopic(id);
			if (issue == null) {
				return null;
			}
			result.setIssue(issue);
			
			maps = opinionIssueMapRepository.findByIssueId(issue.getIssueId());
		}
		
		List<Long> opinionIds = new ArrayList<>();
		
		Map<Long, String> stand = new HashMap<>();
		
		for (OpinionIssueMap map:maps) {
			opinionIds.add(map.getOpinionId()); 
			stand.put(map.getOpinionId(), map.getStand());
		}
		result.setStand(stand);
		
		List<Opinion> opinions = opinionRepository.findAllById(opinionIds);
		result.setOpinions(opinions);
		
		return result;
		
	}
	
}
