package com.example.demo.vo;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpinionPreviews {
	
	Long opinionId;
	String title;
	String text;
	List<Map<String, Object>> relatedIssues;

}
