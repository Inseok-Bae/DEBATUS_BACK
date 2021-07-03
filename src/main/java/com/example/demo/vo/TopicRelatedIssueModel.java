package com.example.demo.vo;

import java.util.List;

import com.example.demo.entity.Issue;
import com.example.demo.entity.Opinion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicRelatedIssueModel {

	Issue issue;
	List<Opinion> hotOpinions;
	Integer centeredFlag;
	
}
