package com.example.demo.vo;

import com.example.demo.entity.Material;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialPostModel {
	
	Material material;
	Long issueId;
	
}
