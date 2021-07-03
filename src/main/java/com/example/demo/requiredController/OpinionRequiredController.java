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

import com.example.demo.repository.OpinionRepository;
import com.example.demo.service.OpinionService;
import com.example.demo.vi.opinionPreviewMapping;
import com.example.demo.vo.OpinionPostModel;
import com.example.demo.vo.RecommendCountPatchModel;

@CrossOrigin
@RestController
@RequestMapping("/api/required/opinion")
public class OpinionRequiredController {

	@Autowired
	OpinionRepository opinionRepository;
	
	@Autowired
	OpinionService opinionService;
	
	@PostMapping
	public Long postOpinion(@RequestBody OpinionPostModel opinionPostModel, @RequestHeader("jwt-auth-token") String token) {
		return opinionService.postOpinion(opinionPostModel, token);
	}
	
	@PatchMapping
	public Long patchOpinion(@RequestBody OpinionPostModel opinionPostModel, @RequestHeader("jwt-auth-token") String token) {
		return opinionService.patchOpinion(opinionPostModel, token);
	}
	
	@DeleteMapping
	public String deleteOpinion(@RequestParam Long id, @RequestHeader("jwt-auth-token") String token) {
		return opinionService.deleteOpinion(id, token);
	}
	
	@GetMapping("/byUserId")
	public List<opinionPreviewMapping> getOpinionsByUserId(@RequestHeader("jwt-auth-token") String token) {
		return opinionService.findByWriter(token);
	}
	
	@PatchMapping("/recommendCount")
	public String patchVoteCount(@RequestBody RecommendCountPatchModel model, @RequestHeader("jwt-auth-token") String token) {
		return opinionService.patchRecommendCount(model, token);
	}
	
}
