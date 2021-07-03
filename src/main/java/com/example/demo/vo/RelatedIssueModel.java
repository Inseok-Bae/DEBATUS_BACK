package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatedIssueModel {
	
	Long issueId;
	String stand;
	Integer centeredFlag;
	
}
