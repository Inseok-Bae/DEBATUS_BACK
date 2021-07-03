package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Topic;
import com.example.demo.vi.topicPreviewMapping;

public interface TopicRepository extends JpaRepository<Topic, Long> {

	List<Topic> findByTitleLike(String title);
	List<Topic> findByTextLike(String text);

	List<topicPreviewMapping> findAllBy(Pageable pageable);
	List<topicPreviewMapping> findByWriter(String writer);
	
}
