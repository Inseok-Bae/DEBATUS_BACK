package com.example.demo.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.IssueTopicMap;
import com.example.demo.entity.Topic;
import com.example.demo.entity.TopicUserMap;
import com.example.demo.jwt.JwtService;
import com.example.demo.repository.IssueTopicMapRepository;
import com.example.demo.repository.TopicRepository;
import com.example.demo.repository.TopicUserMapRepository;
import com.example.demo.vi.topicPreviewMapping;
import com.example.demo.vo.AnswerModel;
import com.example.demo.vo.RelatedIssueModel;
import com.example.demo.vo.TopicPostModel;
import com.example.demo.vo.VoteCountPatchModel;

@Service
public class TopicService {

	@Autowired
	TopicRepository topicRepository;
	
	@Autowired
	IssueService issueService;
	
	@Autowired
	IssueTopicMapRepository issueTopicMapRepository;
	
	@Autowired
	TopicUserMapRepository topicUserMapRepository;
	
	@Autowired
	JwtService jwtService;
	
	public Map<String, Object> getPreviews(Pageable pageable) {
		
		Map<String, Object> result = new HashMap<>();
		
		List<topicPreviewMapping> content = topicRepository.findAllBy(pageable);
		int totalPages = (int) Math.ceil(topicRepository.count() / pageable.getPageSize()); 
		
		result.put("content", content);
		result.put("totalPages", totalPages);
		
		return result;
	} 
	
	public List<topicPreviewMapping> findByWriter(String token) {
		
		String userId = (String) jwtService.get(token).get("userId");
		return topicRepository.findByWriter(userId);
		
	}
	
	public List<Topic> getTopicsByIssueId(Long id) {
		
		List<IssueTopicMap> maps = issueTopicMapRepository.findByIssueId(id);
		
		List<Long> topicIds = new ArrayList<>();
		
		for (IssueTopicMap map : maps) {
			topicIds.add(map.getTopicId()); 
		}
		
		return topicRepository.findAllById(topicIds);
		
	}
	
	public String patchVoteCount(VoteCountPatchModel model, String token) {
		
		Topic topic = topicRepository.findById(model.getTopicId()).orElseThrow();
		String userId = (String) jwtService.get(token).get("userId");
		
		List<TopicUserMap> userMaps = topicUserMapRepository.findAllByTopicId(model.getTopicId());
		
		for (TopicUserMap map : userMaps) {
			if (map.getUserId().equals(userId)) {
				return "dup";
			}
		}
		
		TopicUserMap map = new TopicUserMap();
		map.setTopicId(model.getTopicId());
		map.setUserId(userId);
		
		topicUserMapRepository.save(map);
		
		int voteCount = topic.getVotingCount();
		voteCount += model.getVoteCount();
		topic.setVotingCount(voteCount);
		topicRepository.save(topic);
		
		return "OK";
	}
	
	public Long postTopic(TopicPostModel model, String token) {
		
		String userId = (String) jwtService.get(token).get("userId");
		Topic topic = model.getTopic();
		topic.setWriter(userId);
		topic.setAnsweredFlag(0);
		topic.setVotingCount(0);
		
		Topic result = topicRepository.save(topic);
		
		List<IssueTopicMap> maps = new ArrayList<>();
		
		for (RelatedIssueModel relatedIssue : model.getRelatedIssues()) {
			IssueTopicMap temp = new IssueTopicMap();
			temp.setIssueId(relatedIssue.getIssueId());
			temp.setTopicId(result.getTopicId());
			temp.setCenteredIssueFlag(relatedIssue.getCenteredFlag());
			maps.add(temp);
		}
		
		issueTopicMapRepository.saveAll(maps);
		
		return result.getTopicId();
		
	}
	
	public Long patchTopic(TopicPostModel model, String token) {
		
		String userId = (String) jwtService.get(token).get("userId");
		Topic topic = model.getTopic();
		
		Topic originTopic = topicRepository.findById(topic.getTopicId()).orElseThrow();
		
		if (!userId.equals(originTopic.getWriter())) {
			throw new RuntimeException("Writer is not same.");
		}
		
		originTopic.setTitle(topic.getTitle());
		originTopic.setText(topic.getText());
		
		Topic result = topicRepository.save(originTopic);
		
		List<IssueTopicMap> originMaps = issueTopicMapRepository.findByTopicId(topic.getTopicId());
		List<IssueTopicMap> addList = new ArrayList<>();
		
		for (RelatedIssueModel relatedIssue : model.getRelatedIssues()) {
			
			boolean alreadyInFlag = false;
			
			for (IssueTopicMap originMap : originMaps) {
				if (relatedIssue.getIssueId() == originMap.getIssueId()) {
					alreadyInFlag = true;
					break;
				}
			}
			
			if (!alreadyInFlag) {
				IssueTopicMap temp = new IssueTopicMap();
				temp.setIssueId(relatedIssue.getIssueId());
				temp.setTopicId(result.getTopicId());
				temp.setCenteredIssueFlag(relatedIssue.getCenteredFlag());
				addList.add(temp);
			}
			
		}
		
		issueTopicMapRepository.saveAll(addList);
		
		List<IssueTopicMap> deleteList = new ArrayList<>();
		
		for (IssueTopicMap originMap : originMaps) {
			boolean deleteFlag = true;
			
			for (RelatedIssueModel relatedIssue : model.getRelatedIssues()) {
				if (originMap.getIssueId() == relatedIssue.getIssueId()) {
					deleteFlag = false;
					break;
				}
			}
			
			if (deleteFlag) {
				deleteList.add(originMap);
			}
		}
		
		issueTopicMapRepository.deleteAll(deleteList);
		
		return result.getTopicId();
		
	}
	
	public String deleteTopic(Long id, String token) {
		String userId = (String) jwtService.get(token).get("userId");
		Topic topic = topicRepository.findById(id).orElseThrow();
		
		if (!userId.equals(topic.getWriter())) {
			throw new RuntimeException("Writer is not same.");
		}
		
		topicRepository.delete(topic);
		
		List<IssueTopicMap> issueTopicMaps = issueTopicMapRepository.findByTopicId(id);
		issueTopicMapRepository.deleteAll(issueTopicMaps);
		
		List<TopicUserMap> topicUserMaps = topicUserMapRepository.findAllByTopicId(id);
		topicUserMapRepository.deleteAll(topicUserMaps);
		
		issueService.deleteTopicRelatedIssue(id);
		
		return "OK";
		
	}
	
	public Long postAnswer(AnswerModel answerModel, String token) {
		String userId = (String) jwtService.get(token).get("userId");
		Topic topic = topicRepository.findById(answerModel.getTopicId()).orElseThrow();
		
		if (!userId.equals("admin")) {
			throw new RuntimeException("NOT ADMIN.");
		}
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		topic.setAnsweredDate(timestamp);
		topic.setAnsweredFlag(1);
		topic.setAnsweredTitle(answerModel.getTitle());
		topic.setAnsweredText(answerModel.getText());
		
		topicRepository.save(topic);
		issueService.makeTopicFeedbackIssue(topic);
		
		return topic.getTopicId();
	}
	
}
