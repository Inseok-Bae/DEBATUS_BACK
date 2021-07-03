package com.example.demo.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Material;
import com.example.demo.jwt.JwtService;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.vo.MaterialPostModel;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MaterialService {
	
	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	IssueService issueService;
	
	public List<Material> getMaterials(String ids) {
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String decodedIds = URLDecoder.decode(ids, "UTF-8");
			try {
				List<Long> meterialIds = Arrays.asList(mapper.readValue(decodedIds, Long[].class));
				return materialRepository.findAllById(meterialIds);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}
		
	}
	
	public Long postMaterial(MaterialPostModel model, String token) {
		String userId = (String) jwtService.get(token).get("userId");
		Material material = model.getMaterial();
		material.setWriter(userId);
		Material result = materialRepository.save(material);
		issueService.setIssueMaterials(model.getIssueId(), result.getMaterialId());
		return result.getMaterialId();
	}
	
}
