	package com.example.demo.reserve.service;
	
	import com.example.demo.reserve.dto.ReserveDTO;
	import com.example.demo.reserve.repository.ReserveRepository;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;
	
	import java.util.List;
	
	@Service
	public interface ReserveService {
	
		public List<ReserveDTO> getReservationsByLoginId(String loginId);
		public void cancelReservation(int reserveId, String loginId);
		public List<ReserveDTO> getConfirmedReservations(String loginId);
		public void cancelReservation(Long reserveId, String loginId);
		public void approveReservation(Long reserveId);
		public Object getAllReservations();
		public boolean cancelReservation(Long id);
		public List<ReserveDTO> getPaidReservationsByLoginId(String loginId);
		
		
	  
	}
