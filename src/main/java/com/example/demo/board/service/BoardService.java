package com.example.demo.board.service;

import java.util.List;

import com.example.demo.board.dto.BoardDTO;

public interface BoardService {
	/* List<BoardDTO> boardList(); */
	boolean insertBoard(BoardDTO dto);
	BoardDTO getBoard(int articleNo);
	void updateBoard(BoardDTO dto);
	int deleteBoard(int articleNo);
	List<BoardDTO> getBoardList(int page, int size);
	Long getTotalCount();
}