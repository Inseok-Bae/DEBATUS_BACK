package com.example.demo.vo;

import java.util.List;

import com.example.demo.entity.Opinion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpinionPostModel {
	
	Opinion opinion;
	List<Long> relatedOpinionIds;
	List<RelatedIssueModel> relatedIssues;
	
}
