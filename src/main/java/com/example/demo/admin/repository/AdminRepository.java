package com.example.demo.admin.repository;

import com.example.demo.reserve.dto.ReserveDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<ReserveDTO, Long> {
    Optional<ReserveDTO> findById(Long id);  // 예약 취소 등에서 사용
}
