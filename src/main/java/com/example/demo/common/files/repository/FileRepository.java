package com.example.demo.common.files.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.common.files.dto.FileDTO;

public interface FileRepository extends JpaRepository<FileDTO, Integer> {
	List<FileDTO> findByArticleNo(int articleNo);
	// select 문 필요없이 articleNo 로 파일을 가져옴

}
