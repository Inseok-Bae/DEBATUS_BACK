package com.example.demo.vo;

import java.util.List;

import com.example.demo.entity.Topic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicPostModel {
	
	Topic topic;
	List<RelatedIssueModel> relatedIssues;
	
}
