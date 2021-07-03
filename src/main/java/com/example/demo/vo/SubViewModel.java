package com.example.demo.vo;

import java.util.List;
import java.util.Map;

import com.example.demo.entity.Issue;
import com.example.demo.entity.Opinion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubViewModel {
	
	Issue issue;
	List<Opinion> opinions;
	Map<Long, String> stand;
	
}
