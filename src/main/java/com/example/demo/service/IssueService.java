package com.example.demo.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Issue;
import com.example.demo.entity.IssueTopicMap;
import com.example.demo.entity.IssueUserMap;
import com.example.demo.entity.Opinion;
import com.example.demo.entity.OpinionIssueMap;
import com.example.demo.entity.Topic;
import com.example.demo.jwt.JwtService;
import com.example.demo.repository.IssueRepository;
import com.example.demo.repository.IssueTopicMapRepository;
import com.example.demo.repository.IssueUserMapRepository;
import com.example.demo.repository.OpinionIssueMapRepository;
import com.example.demo.vi.issuePreviewMapping;
import com.example.demo.vo.TopicRelatedIssueModel;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class IssueService {

	@Autowired
	IssueRepository issueRepository;
	
	@Autowired
	OpinionService opinionService;
	
	@Autowired
	IssueTopicMapRepository issueTopicMapRepository;
	
	@Autowired
	OpinionIssueMapRepository opinionIssueMapRepository;
	
	@Autowired
	IssueUserMapRepository issueUserMapRepository;
	
	@Autowired
	JwtService jwtService;
	
	public List<Issue> findByIssueIds(List<Long> issueIds) {
		return issueRepository.findAllById(issueIds);
	}
	
	public Map<String, Object> getPreviews(Pageable pageable) {
		
		Map<String, Object> result = new HashMap<>();
		
		List<issuePreviewMapping> content = issueRepository.findAllBy(pageable);
		int totalPages = (int) Math.ceil(issueRepository.count() / pageable.getPageSize()); 
		
		result.put("content", content);
		result.put("totalPages", totalPages);
		
		return result;
	}
	
	public List<Object> getRelatedIssuePreviewsByTopic(Long id) {
		
		List<IssueTopicMap> maps = issueTopicMapRepository.findByTopicId(id);
		
		List<Long> issueIds = new ArrayList<>();
		
		for (IssueTopicMap map : maps) {
			issueIds.add(map.getIssueId());
		}
		
		List<List<Object>> issues = issueRepository.findAllByIssueIds(issueIds);
		List<Object> result = new ArrayList<Object>();
		
		for (List<Object> issue : issues) {
			result.add(issue.get(0));
		}
		
		return result;
	}
	
	public List<issuePreviewMapping> findByWriter(String token) {
		
		String userId = (String) jwtService.get(token).get("userId");
		return issueRepository.findByWriter(userId);
		
	}
	
	public List<TopicRelatedIssueModel> getIssueByTopicId(Long id) {
		
		List<IssueTopicMap> maps = issueTopicMapRepository.findByTopicId(id);
		
		List<Long> issueIds = new ArrayList<>();
		Map<Long, Integer> issueCenteredFlagMap = new HashMap<>(); 
		
		for (IssueTopicMap map : maps) { 
			issueIds.add(map.getIssueId());
			issueCenteredFlagMap.put(map.getIssueId(), map.getCenteredIssueFlag());
		}
		
		List<Issue> issues = issueRepository.findAllById(issueIds);
		
		List<TopicRelatedIssueModel> result = new ArrayList<>();
		
		for (Issue issue : issues) {
			TopicRelatedIssueModel temp = new TopicRelatedIssueModel();
			
			List<Long> hotOpinionIds = new ArrayList<>(); 
			List<OpinionIssueMap> hotOpinionMaps = opinionIssueMapRepository.findByIssueId(issue.getIssueId());
			
			for (OpinionIssueMap hotOpinionMap : hotOpinionMaps) {
				hotOpinionIds.add(hotOpinionMap.getOpinionId());
			}
			
			List<Opinion> hotOpinions = opinionService.getHotOpinions(hotOpinionIds);
			
			temp.setIssue(issue);
			temp.setHotOpinions(hotOpinions);
			temp.setCenteredFlag(issueCenteredFlagMap.get(issue.getIssueId()));
			result.add(temp);
		};
		
		return result;
		
	}
	
	public List<Object> getIssueByOpinionId(Long id) {
		
		List<OpinionIssueMap> maps = opinionIssueMapRepository.findByOpinionId(id);
		
		List<Long> issueIds = new ArrayList<>();
		
		for (OpinionIssueMap map : maps) {
			issueIds.add(map.getIssueId());
		}
		
		List<List<Object>> issues = issueRepository.findAllByIssueIds(issueIds);
		List<Object> result = new ArrayList<Object>();
		
		for (List<Object> issue : issues) {
			result.add(issue.get(0));
		}
		
		return result;
		
	}
	
	public List<Object> getDetailIssueByOpinionId(Long id) {
		
		List<OpinionIssueMap> maps = opinionIssueMapRepository.findByOpinionId(id);
		
		List<Long> issueIds = new ArrayList<>();
		
		for (OpinionIssueMap map : maps) {
			issueIds.add(map.getIssueId());
		}
		
		List<List<Object>> issues = issueRepository.findAllByIssueIds(issueIds);
		List<Object> result = new ArrayList<Object>();
		
		for (List<Object> issue : issues) {
			Map<String, Object> temp = new HashMap<>();
			temp.put("id", issue.get(0));
			temp.put("title", issue.get(1));
			
			for (OpinionIssueMap map : maps) {
				BigInteger tempId = (BigInteger) temp.get("id"); 
				if (tempId.longValue() == map.getIssueId()) {
					temp.put("stand", map.getStand());
					break;
				}
			}
			
			result.add(temp);
		}
		
		return result;
		
	}
	
	public List<Issue> getAllIssuesByOpinionId(Long id) {
		List<OpinionIssueMap> maps = opinionIssueMapRepository.findByOpinionId(id);
		
		List<Long> issueIds = new ArrayList<>();
		
		for (OpinionIssueMap map : maps) {
			issueIds.add(map.getIssueId());
		}
		
		return issueRepository.findAllById(issueIds);
	}
	
	public List<Object> getIssuePreviewByIssueIds(List<Long> issueIds) {
		List<List<Object>> issues = issueRepository.findAllByIssueIds(issueIds);
		List<Object> result = new ArrayList<Object>();
		
		for (List<Object> issue : issues) {
			Map<String, Object> temp = new HashMap<>();
			temp.put("id", issue.get(0));
			temp.put("title", issue.get(1));
			
			result.add(temp);
		}
		
		return result;
	}
	
	
	public Long postIssue(Issue issue, String token) {
		
		String userId = (String) jwtService.get(token).get("userId");
		issue.setWriter(userId);
		issue.setProsCount((long) 0);
		issue.setConsCount((long) 0);
		Issue result = issueRepository.save(issue);
		
		return result.getIssueId(); 
		
	}
	
	public void patchCount(Long issueId, int prosParam, int consParam) {
		Issue issue = issueRepository.findById(issueId).orElseThrow();
		
		Long prosCount = issue.getProsCount();
		Long consCount = issue.getConsCount();
		
		prosCount += prosParam;
		consCount += consParam;
		
		issue.setProsCount(prosCount);
		issue.setConsCount(consCount);
		
		if (issue.getConsCount() + issue.getProsCount() <= 0) {
			this.deleteIssue(issueId);
			return;
		}
		issueRepository.save(issue);
	}
	
	public void deleteIssue(Long id) {
		issueRepository.deleteById(id);
		List<IssueUserMap> issueUserMaps = issueUserMapRepository.findAllByIssueId(id);
		issueUserMapRepository.deleteAll(issueUserMaps);
	}
	
	public void deleteTopicRelatedIssue(Long topicId) {
		Issue issue = issueRepository.findByRelatedTopic(topicId);
		
		if (issue != null) {
			issueRepository.deleteById(issue.getIssueId());
			List<IssueUserMap> issueUserMaps = issueUserMapRepository.findAllByIssueId(issue.getIssueId());
			issueUserMapRepository.deleteAll(issueUserMaps);
		}
		
	}
	
	public void hotOpinionRegister(Long opinionId, String userId) {
		List<OpinionIssueMap> maps = opinionIssueMapRepository.findByOpinionId(opinionId);
		
		List<IssueUserMap> originIssueUserMaps = issueUserMapRepository.findAllByUserId(userId);
		List<IssueUserMap> issueUserMaps = new ArrayList<>();
		
		for (OpinionIssueMap map : maps) {
			
			boolean alreadyInFlag = false;
			
			for (IssueUserMap originMap : originIssueUserMaps) {
				if (map.getIssueId() == originMap.getIssueId()) {
					alreadyInFlag = true;
					break;
				}
			}
			
			if (!alreadyInFlag) {
				IssueUserMap temp = new IssueUserMap();
				temp.setIssueId(map.getIssueId());
				temp.setUserId(userId);
				issueUserMaps.add(temp);
			}
			
		}
		
		issueUserMapRepository.saveAll(issueUserMaps);
		
	}
	
	public Issue makeTopicFeedbackIssue(Topic topic) {
		
		Issue issue = new Issue();
		
		issue.setTitle(topic.getTitle() + "에 대한 피드백");
		issue.setRelatedTopic(topic.getTopicId());
		issue.setProsCount((long) 0);
		issue.setConsCount((long) 0);
		
		Issue result = issueRepository.save(issue);
		
		return result;
		
	}
	
	public Issue getTopicFeedbackIssue(Long topicId) {
		return issueRepository.findByRelatedTopic(topicId);
	}
	
	public void setIssueMaterials(Long issueId, Long materialId) {
		
		ObjectMapper mapper = new ObjectMapper();
		
		Issue issue = issueRepository.findById(issueId).orElseThrow();
		String originMaterialIds = issue.getMaterialIds();
		
		try {
			List<Long> meterialIdList = new ArrayList<Long>();
			
			if (originMaterialIds != null) {
				meterialIdList.addAll(Arrays.asList(mapper.readValue(originMaterialIds, Long[].class)));
			};
			
			meterialIdList.add(materialId);
			String modifiedMaterialIds = mapper.writeValueAsString(meterialIdList);
			issue.setMaterialIds(modifiedMaterialIds);
			issueRepository.save(issue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	
}
