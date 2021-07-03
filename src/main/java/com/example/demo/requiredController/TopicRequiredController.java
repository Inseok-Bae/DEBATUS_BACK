package com.example.demo.requiredController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.TopicRepository;
import com.example.demo.service.TopicService;
import com.example.demo.vi.topicPreviewMapping;
import com.example.demo.vo.AnswerModel;
import com.example.demo.vo.TopicPostModel;
import com.example.demo.vo.VoteCountPatchModel;

@CrossOrigin
@RestController
@RequestMapping("/api/required/topic")
public class TopicRequiredController {

	@Autowired
	TopicRepository topicRepository;
	
	@Autowired
	TopicService topicService;
	
	@PatchMapping("/voteCount")
	public String patchVoteCount(@RequestBody VoteCountPatchModel model, @RequestHeader("jwt-auth-token") String token) {
		return topicService.patchVoteCount(model, token);
	}
	
	@GetMapping("/byUserId")
	public List<topicPreviewMapping> getTopicsByUserId(@RequestHeader("jwt-auth-token") String token) {
		return topicService.findByWriter(token);
	}
	
	@PostMapping
	public Long postTopic(@RequestBody TopicPostModel model, @RequestHeader("jwt-auth-token") String token) {
		return topicService.postTopic(model, token);
	}
	
	@PostMapping("/answer")
	public Long postAnswer(@RequestBody AnswerModel answerModel, @RequestHeader("jwt-auth-token") String token) {
		return topicService.postAnswer(answerModel, token);
	}
	
	@PatchMapping
	public Long patchTopic(@RequestBody TopicPostModel model, @RequestHeader("jwt-auth-token") String token) {
		return topicService.patchTopic(model, token);
	}
	
	@DeleteMapping
	public String deleteTopic(@RequestParam("id") Long id, @RequestHeader("jwt-auth-token") String token) {
		return topicService.deleteTopic(id, token);
	}
	
}
