package com.example.demo.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Opinion;
import com.example.demo.entity.OpinionIssueMap;
import com.example.demo.entity.OpinionOpinionMap;
import com.example.demo.entity.OpinionUserMap;
import com.example.demo.jwt.JwtService;
import com.example.demo.repository.IssueRepository;
import com.example.demo.repository.OpinionIssueMapRepository;
import com.example.demo.repository.OpinionOpinionMapRepository;
import com.example.demo.repository.OpinionRepository;
import com.example.demo.repository.OpinionUserMapRepository;
import com.example.demo.vi.opinionPreviewMapping;
import com.example.demo.vo.RelatedIssueModel;
import com.example.demo.vo.OpinionPostModel;
import com.example.demo.vo.OpinionPreviews;
import com.example.demo.vo.RecommendCountPatchModel;

@Service
public class OpinionService {

	@Autowired
	OpinionRepository opinionRepository;
	
	@Autowired
	IssueRepository issueRepository;
	
	@Autowired
	IssueService issueService;
	
	@Autowired
	OpinionIssueMapRepository opinionIssueMapRepository;
	
	@Autowired
	OpinionOpinionMapRepository opinionOpinionMapRepository;
	
	@Autowired
	OpinionUserMapRepository opinionUserMapRepository;
	
	@Autowired
	JwtService jwtService;
	
	@Value("${requiredRecommendCount}")
	private Long requiredRecommendCount;
	
	public Map<String, Object> getPreviews(Pageable pageable) {
		
		Map<String, Object> result = new HashMap<>();
		
		int totalPages = (int) Math.ceil(opinionRepository.count() / pageable.getPageSize());
		result.put("totalPages", totalPages);
		
		// get opinion data
		List<opinionPreviewMapping> opinions = opinionRepository.findAllBy(pageable);
		 
		
		// get related issue data
		List<Long> opinionIds = new ArrayList<>();
		
		for (opinionPreviewMapping opinion : opinions) {
			opinionIds.add(opinion.getOpinionId()); 
		}
		
		List<OpinionIssueMap> maps = opinionIssueMapRepository.findByOpinionIds(opinionIds);
		
		List<Long> issueIds = new ArrayList<>();
		
		for (OpinionIssueMap map:maps) {
			boolean alreadyInFlag = false;
			
			for (Long issueId : issueIds) {
				if (map.getOpinionId() == issueId) {
					alreadyInFlag = true;
					break;
				}
			}
			
			if (!alreadyInFlag) {
				issueIds.add(map.getIssueId());
			}
		}
		
		List<List<Object>> issues = issueRepository.findAllByIssueIds(issueIds);
		
		Map<Long, List<Map<String, Object>>> relatedIssueMap = new HashMap<>(); 
		
		for (Long opinionId : opinionIds) {
			List<Map<String, Object>> relatedIssues = new ArrayList<Map<String,Object>>(); 
			List<Long> opinionRelatedIssueIds = new ArrayList<>();
			
			for (OpinionIssueMap map : maps) {
				if (map.getOpinionId() == opinionId) {
					opinionRelatedIssueIds.add(map.getIssueId());
				}
			}
			
			for (List<Object> issue : issues) {
				for (Long relatedIssueId : opinionRelatedIssueIds) {
					BigInteger targetIssueId = (BigInteger) issue.get(0);
					if (relatedIssueId == targetIssueId.longValue()) {
						Map<String, Object> relatedIssue = new HashMap<>();
						
						relatedIssue.put("issueId", targetIssueId.longValue());
						relatedIssue.put("title", (String) issue.get(1));
						
						relatedIssues.add(relatedIssue);
					}
				}
			}
			
			relatedIssueMap.put(opinionId, relatedIssues);
		}
		
		
		// make contents
		
		List<OpinionPreviews> contents = new ArrayList<OpinionPreviews>();
		
		for (opinionPreviewMapping opinion : opinions) {
			
			OpinionPreviews temp = new OpinionPreviews();
			
			temp.setOpinionId(opinion.getOpinionId());
			temp.setTitle(opinion.getTitle());
			temp.setText(opinion.getText());
			temp.setRelatedIssues(relatedIssueMap.get(opinion.getOpinionId()));
			
			contents.add(temp);
			
		}
		
		result.put("content", contents);
		
		return result;
	}
	
	public List<opinionPreviewMapping> findByWriter(String token) {
		
		String userId = (String) jwtService.get(token).get("userId");
		return opinionRepository.findByWriter(userId);
		
	}
	
	public List<Opinion> getOpinionGroupById(Long id) {
		
		List<OpinionOpinionMap> maps = opinionOpinionMapRepository.findByOpinionId1OrOpinionId2(id, id);
		
		List<Long> opinionIds = new ArrayList<>();
		
		for (OpinionOpinionMap map : maps) {
			if (!map.getOpinionId1().equals(id)) {
				opinionIds.add(map.getOpinionId1());
			}
			if (!map.getOpinionId2().equals(id)) {
				opinionIds.add(map.getOpinionId2());
			}
		} 
		
		return opinionRepository.findAllById(opinionIds);
		
	}
	
	public Long postOpinion(OpinionPostModel opinionPostModel, String token) {
		
		String userId = (String) jwtService.get(token).get("userId");
		
		Opinion opinion = opinionPostModel.getOpinion();
		
		opinion.setWriter(userId);
		
		Opinion result =  opinionRepository.save(opinion);
		
		List<OpinionIssueMap> issueMaps = new ArrayList<OpinionIssueMap>();
		
		for (RelatedIssueModel relatedIssue : opinionPostModel.getRelatedIssues()) {
			OpinionIssueMap temp = new OpinionIssueMap();
			
			temp.setOpinionId(result.getOpinionId());
			temp.setIssueId(relatedIssue.getIssueId());
			temp.setStand(relatedIssue.getStand());
			issueMaps.add(temp);
			
			issueService.patchCount(relatedIssue.getIssueId(), 
									relatedIssue.getStand().equals("1") ? 1 : 0, 
											relatedIssue.getStand().equals("1") ? 0 : 1);
			
		}
			
		opinionIssueMapRepository.saveAll(issueMaps);
		
		
		
		List<OpinionOpinionMap> opinionMaps = new ArrayList<OpinionOpinionMap>();
		
		for (Long relatedOpinionId : opinionPostModel.getRelatedOpinionIds()) {
			OpinionOpinionMap temp = new OpinionOpinionMap();
			
			temp.setOpinionId1(result.getOpinionId());
			temp.setOpinionId2(relatedOpinionId);
			
			opinionMaps.add(temp);
		}
		
		opinionOpinionMapRepository.saveAll(opinionMaps);
		
		return result.getOpinionId();
	}
	
	public long patchOpinion(OpinionPostModel opinionPostModel, String token) {

		if (opinionPostModel.getOpinion().getOpinionId() == null) {
			throw new RuntimeException("Opinion ID is not exist.");
		};
		
		Opinion opinion = opinionRepository.findById(opinionPostModel.getOpinion().getOpinionId()).orElseThrow();
		String userId = (String) jwtService.get(token).get("userId");
		
		if (!opinion.getWriter().equals(userId)) {
			throw new RuntimeException("Writer is not same.");
		} 
			
		Opinion modifiedOpinion = opinionPostModel.getOpinion();
		modifiedOpinion.setWriter(userId);
		
		opinionRepository.save(modifiedOpinion);
		
		List<RelatedIssueModel> modifiedIssues = opinionPostModel.getRelatedIssues(); 
		List<OpinionIssueMap> originIssues = opinionIssueMapRepository.findByOpinionId(opinion.getOpinionId());
		
		List<OpinionIssueMap> saveList = new ArrayList<>();
		
		// patch 및 create 대상 검색
		for (RelatedIssueModel modifiedIssue : modifiedIssues) {
			
			boolean alreadyInFlag = false;
			OpinionIssueMap seletedOriginIssue = null;
			
			for (OpinionIssueMap originIssue : originIssues) {
				if (originIssue.getIssueId() == modifiedIssue.getIssueId()) {
					alreadyInFlag = true;
					seletedOriginIssue = originIssue;
					break;
				}
			};
			
			OpinionIssueMap temp = new OpinionIssueMap();
			
			if (alreadyInFlag) { // patch 대상
				
				temp.setId(seletedOriginIssue.getId());
				
				if (!modifiedIssue.getStand().equals(seletedOriginIssue.getStand())) {
					
					String stand = modifiedIssue.getStand(); 
					
					seletedOriginIssue.setStand(stand);
					
					issueService.patchCount(modifiedIssue.getIssueId(), stand.equals("1") ? 1 : -1, stand.equals("1") ? -1 : 1);
				} 
				
				temp = seletedOriginIssue;
				
			} else { // create 대상
				
				String stand = modifiedIssue.getStand(); 
				
				temp.setIssueId(modifiedIssue.getIssueId());
				temp.setOpinionId(opinion.getOpinionId());
				temp.setStand(stand);
				
				issueService.patchCount(modifiedIssue.getIssueId(), stand.equals("1") ? 1 : 0, stand.equals("1") ? 0 : 1);
			};
			
			saveList.add(temp);
			
		};
		
		opinionIssueMapRepository.saveAll(saveList);
		
		List<OpinionIssueMap> deleteList = new ArrayList<>();
		
		// delete 대상 검색
		for (OpinionIssueMap originIssue : originIssues) {
			
			boolean deleteFlag = true;
			
			for (RelatedIssueModel modifiedIssue : modifiedIssues) {
				if (originIssue.getIssueId() == modifiedIssue.getIssueId()) {
					deleteFlag = false;
					break;
				}
			}
			
			if (deleteFlag) {
				deleteList.add(originIssue);
				
				issueService.patchCount(originIssue.getIssueId(), originIssue.getStand().equals("1") ? -1 : 0, originIssue.getStand().equals("1") ? 0 : -1);
				
			}
		}
		
		opinionIssueMapRepository.deleteAll(deleteList);
		
		List<OpinionOpinionMap> originRelatedOpinions = opinionOpinionMapRepository.findByOpinionId1OrOpinionId2(opinion.getOpinionId(), opinion.getOpinionId());
		List<Long> modifiedRelatedOpinions =  opinionPostModel.getRelatedOpinionIds();
		
		// create & modify
		List<OpinionOpinionMap> _saveList = new ArrayList<>();
		
		for (Long modifiedRelatedOpinion : modifiedRelatedOpinions) {
			OpinionOpinionMap temp = new OpinionOpinionMap();
			temp.setOpinionId1(opinion.getOpinionId());
			temp.setOpinionId2(modifiedRelatedOpinion);
			_saveList.add(temp);
		}
		
		opinionOpinionMapRepository.saveAll(_saveList);
		
		// delete
		List<OpinionOpinionMap> _deleteList = new ArrayList<>();
		
		for (OpinionOpinionMap originRelatedOpinion : originRelatedOpinions) {
			
			boolean deleteFlag = true;
			
			for (Long modifiedRelatedOpinion : modifiedRelatedOpinions) {
				if (modifiedRelatedOpinion == originRelatedOpinion.getOpinionId1() || modifiedRelatedOpinion == originRelatedOpinion.getOpinionId2()) {
					deleteFlag = false;
					break;
				}
			}
			
			if (deleteFlag) {
				_deleteList.add(originRelatedOpinion);
			}
		}
		
		opinionOpinionMapRepository.deleteAll(_deleteList);
		
		return opinionPostModel.getOpinion().getOpinionId();
		

	}
	
	public String deleteOpinion(Long id, String token) {
		
		Opinion opinion = opinionRepository.findById(id).orElseThrow();
		String userId = (String) jwtService.get(token).get("userId");
		
		if (!opinion.getWriter().equals(userId)) {
			throw new RuntimeException("Writer is not same.");
		} else {
			opinionRepository.deleteById(id);
			
			List<OpinionIssueMap> opinionIssueMaps = opinionIssueMapRepository.findByOpinionId(id);
			opinionIssueMapRepository.deleteAll(opinionIssueMaps);
			
			for (OpinionIssueMap maps: opinionIssueMaps) {
				issueService.patchCount(maps.getIssueId(), maps.getStand().equals("1") ? -1 : 0, maps.getStand().equals("1") ? 0 : -1);
			}
			
			List<OpinionOpinionMap> opinionOpinionMaps = opinionOpinionMapRepository.findByOpinionId1OrOpinionId2(id, id);
			opinionOpinionMapRepository.deleteAll(opinionOpinionMaps);
			
			List<OpinionUserMap> opinionUserMaps = opinionUserMapRepository.findAllByOpinionId(id);
			opinionUserMapRepository.deleteAll(opinionUserMaps); 
			
			return "OK";
		}
		
	}
	
	public String patchRecommendCount(RecommendCountPatchModel model, String token) {
		
		Opinion opinion = opinionRepository.findById(model.getOpinionId()).orElseThrow();
		String userId = (String) jwtService.get(token).get("userId");
		
		List<OpinionUserMap> userMaps = opinionUserMapRepository.findAllByOpinionId(model.getOpinionId());
		
		for (OpinionUserMap map : userMaps) {
			if (map.getUserId().equals(userId)) {
				return "dup";
			}
		};
		
		OpinionUserMap map = new OpinionUserMap();
		map.setOpinionId(model.getOpinionId());
		map.setUserId(userId);
		
		opinionUserMapRepository.save(map);
		
		Long recommendCount = opinion.getRecommendCount();
		recommendCount += model.getRecommendCount();
		
		if (recommendCount >= requiredRecommendCount) {
			issueService.hotOpinionRegister(opinion.getOpinionId(), opinion.getWriter());
		};
		
		opinion.setRecommendCount(recommendCount);
		opinionRepository.save(opinion);
		
		return "OK";
	}
	
	public List<opinionPreviewMapping> getOpinionsByContent(String title, String text) {
		List<opinionPreviewMapping> results = new ArrayList<>();
		results.addAll(opinionRepository.findByTitleLike("%"+title+"%"));
		
		List<opinionPreviewMapping> textLikeLists = opinionRepository.findByTextLike("%"+text+"%");
		
		for (opinionPreviewMapping textLikeOpinion : textLikeLists) {
			boolean alreadyInFlag = false;
			for (opinionPreviewMapping result : results) {
				if (textLikeOpinion.getOpinionId() == result.getOpinionId()) {
					alreadyInFlag = true;
					break;
				}
			}
			if (!alreadyInFlag) {
				results.add(textLikeOpinion);
			}
		}
		return results;
	}
	
	public List<Opinion> getHotOpinions(List<Long> hotOpinionIds) {
		List<Opinion> result = new ArrayList<>();
		for (Opinion opinion : opinionRepository.findAllById(hotOpinionIds)) {
			if (opinion.getRecommendCount() >= requiredRecommendCount) {
				result.add(opinion);
			}
		}
		return result;
	}
	
}
