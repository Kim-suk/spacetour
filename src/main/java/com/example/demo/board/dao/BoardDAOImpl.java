package com.example.demo.board.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.board.dto.BoardDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class BoardDAOImpl implements BoardDAO {

    @PersistenceContext
    private EntityManager em;

	/*
	 * @Override public List<BoardDTO> boardList() { return
	 * em.createQuery("SELECT b FROM BoardDTO b ORDER BY b.articleNo DESC",
	 * BoardDTO.class) .getResultList(); }
	 */

    @Override
    public void insertBoard(BoardDTO dto) {
        em.persist(dto);
    }

    @Override
    public BoardDTO getBoard(int articleNo) {
        return em.createQuery("SELECT b FROM Board b WHERE b.articleNo = :articleNo", BoardDTO.class)
                 .setParameter("articleNo", articleNo)
                 .getSingleResult();
    }

    @Override
    public void update(BoardDTO dto) {
        em.merge(dto);
    }

    @Override
    public void delete(BoardDTO dto) {
        em.remove(em.contains(dto) ? dto : em.merge(dto));
    }

    @Override
    public int deleteBoard(int articleNo) {
        return em.createQuery("DELETE FROM Board b WHERE b.articleNo = :articleNo")
                 .setParameter("articleNo", articleNo)
                 .executeUpdate();
    }

    @Override
    public List<BoardDTO> getBoardList(int page, int size) {
        return em.createQuery("SELECT b FROM Board b ORDER BY b.articleNo DESC", BoardDTO.class)
                 .setFirstResult((page - 1) * size)
                 .setMaxResults(size)
                 .getResultList();
    }


    // ✅ 전체 게시글 수
    @Override
    public Long getTotalCount() {
        return em.createQuery("SELECT COUNT(b) FROM Board b", Long.class)
                 .getSingleResult();
    }
}
