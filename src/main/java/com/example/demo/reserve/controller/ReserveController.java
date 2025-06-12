package com.example.demo.reserve.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.reserve.dto.ReserveDTO;
import com.example.demo.reserve.repository.ReserveRepository;
import com.example.demo.reserve.service.ReserveService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/reserve")
public class ReserveController {
   
   @Autowired
    private ReserveService reserveService;

    @Autowired
    private ReserveRepository reserveRepository;
    
    // 캘린더 페이지로 이동
    @GetMapping("/calendar")
    public String calendar() {
    
       
        return "reserve/calendar"; 
    }
    @GetMapping("/planet")
    public String planetGet(
        @RequestParam("reserveDate") String reserveDate,
        @RequestParam("reserveHour") int reserveHour,
        @RequestParam("reserveMinute") int reserveMinute,
        @RequestParam("personCount") int personCount,
        @RequestParam("planet") String planet,
        Model model, HttpSession session) {

        session.setAttribute("reserveDate", reserveDate);
        session.setAttribute("reserveHour", reserveHour);
        session.setAttribute("reserveMinute", reserveMinute);
        session.setAttribute("personCount", personCount);
        session.setAttribute("planet", planet);

        model.addAttribute("reserveDate", reserveDate);
        model.addAttribute("reserveHour", reserveHour);
        model.addAttribute("reserveMinute", reserveMinute);
        model.addAttribute("personCount", personCount);
        model.addAttribute("planet", planet);

        return "reserve/planet";
    }

    // 행성 선택 페이지로 이동 (예약 데이터 처리 후)
    @PostMapping("/planet")
    public String planet(
        @RequestParam("reserveDate") String reserveDate,
        @RequestParam("reserveHour") String reserveHourStr,
        @RequestParam("reserveMinute") String reserveMinuteStr,
        @RequestParam("reserveDayOfWeek") String reserveDayOfWeek,
        @RequestParam("personCount") int personCount,
        Model model,  HttpSession session) {

        // reserveHour, reserveMinute가 "12:30" 또는 ":" 포함해서 넘어올 경우 처리
       int reserveHour = parseTime(reserveHourStr, 0);
       int reserveMinute = parseTime(reserveMinuteStr, 1);

        System.out.println("예약 날짜: " + reserveDate);
        System.out.println("예약 시간: " + reserveHour + ":" + reserveMinute);
        System.out.println("요일 : " + reserveDayOfWeek);
        System.out.println("탑승자 : " + personCount + "명");
        
        session.setAttribute("reserveDate", reserveDate);
        session.setAttribute("reserveHour", reserveHour);
        session.setAttribute("reserveMinute", reserveMinute);
        session.setAttribute("personCount", personCount);

        model.addAttribute("reserveDate", reserveDate);
        model.addAttribute("reserveHour", reserveHour);
        model.addAttribute("reserveMinute", reserveMinute);
        model.addAttribute("reserveDayOfWeek", reserveDayOfWeek);
        model.addAttribute("personCount", personCount);

        return "reserve/planet"; 
    }
    
    private int parseTime(String timeStr, int index) {
        try {
            if (timeStr.contains(":")) {
                String[] parts = timeStr.split(":");
                return Integer.parseInt(parts[index]);
            } else {
                return Integer.parseInt(timeStr);
            }
        } catch (Exception e) {
            return 0;
        }
    }

   // 예약 처리 (날짜, 시간, 행성 선택을 받음)
    @PostMapping("/calendar")
    public String handleReservation(
        @RequestParam("reserveDate") String reserveDate,
        @RequestParam("reserveHour") int reserveHour,
        @RequestParam("reserveMinute") int reserveMinute,
        @RequestParam("reserveDayOfWeek") String reserveDayOfWeek,
        @RequestParam("personCount") int personCount,
        Model model, HttpSession session) {

        System.out.println("예약 날짜: " + reserveDate);
        System.out.println("예약 시간: " + reserveHour + ":" + reserveMinute);
        System.out.println("요일 : " + reserveDayOfWeek);
        System.out.println("탑승자 : " + personCount + "명");

        session.setAttribute("reserveDate", reserveDate);
        session.setAttribute("reserveHour", reserveHour);
        session.setAttribute("reserveMinute", reserveMinute);
        session.setAttribute("reserveDayOfWeek", reserveDayOfWeek);
        session.setAttribute("personCount", personCount);

        return "redirect:/reserve/planet";
    }


    // planet.jsp에서 POST로 넘어가는 대신 GET으로도 가능
    // seat.jsp에서는 session에서 데이터 꺼내서 사용
    @GetMapping("/seat")
    public String seatGet(@RequestParam(value = "planet", required = false) String planet, 
                          HttpSession session, Model model) {

        System.out.println("[seatGet] Query param planet: " + planet);
        if (planet == null || planet.isEmpty()) {
            planet = (String) session.getAttribute("planet");
            System.out.println("[seatGet] Session planet: " + planet);
        } else {
            session.setAttribute("planet", planet);
            System.out.println("[seatGet] planet set to session: " + planet);
        }
        model.addAttribute("planet", planet);

        String selectedSeats = (String) session.getAttribute("selectedSeats");
        if (selectedSeats != null) {
            model.addAttribute("selectedSeats", selectedSeats);
        }

        return "reserve/seat";
    }



    // 좌석 선택 페이지로 이동 (POST)
    @PostMapping("/seat")
    public String seat(
        @RequestParam("planet") String planet,
        @RequestParam("price") String price,
        HttpSession session, Model model) {
       
       Integer personCount = (Integer) session.getAttribute("personCount");

        session.setAttribute("planet", planet);
        session.setAttribute("price", price);
        model.addAttribute("planet", planet);
        model.addAttribute("personCount", personCount);
        
        return "reserve/seat"; 
    }

    // 예약 확인 페이지로 이동
    @GetMapping("/check")
    public String check(
        @RequestParam("selectedSeats") String selectedSeats,  
        HttpSession session,
        Model model) {
       
    
        String loginId = (String) session.getAttribute("loginId");
        String reserveDate = (String) session.getAttribute("reserveDate");
        Integer reserveHour = (Integer) session.getAttribute("reserveHour");
        Integer reserveMinute = (Integer) session.getAttribute("reserveMinute");
        Integer personCount = (Integer) session.getAttribute("personCount");
        String planet = (String) session.getAttribute("planet");
        String price = (String) session.getAttribute("price");
        
   
        if (loginId == null) {
            return "redirect:/loginForm.do?result=notLoggedIn";
        }
        System.out.println("==== 예약 정보 ====");
        System.out.println("loginId: " + loginId);
        System.out.println("planet: " + planet);
        System.out.println("selectedSeats: " + selectedSeats);
        System.out.println("reserveDate: " + reserveDate);
        System.out.println("reserveHour: " + reserveHour);
        System.out.println("price : " + price);
        
        session.setAttribute("selectedSeats", selectedSeats);
        session.setAttribute("planet", planet);
       
        model.addAttribute("loginId", loginId);
        model.addAttribute("planet", planet);
        model.addAttribute("price",price);
        model.addAttribute("selectedSeats", selectedSeats);
        model.addAttribute("reserveDate", reserveDate);
        model.addAttribute("reserveHour", reserveHour);
        model.addAttribute("reserveMinute", reserveMinute);
        model.addAttribute("personCount", personCount);

        return "reserve/check";
    }
    // 예약 완료 처리
    @PostMapping("/confirm")
    public String confirmReservation(HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");
        String reserveDate = (String) session.getAttribute("reserveDate");
        Integer reserveHour = (Integer) session.getAttribute("reserveHour");
        Integer reserveMinute = (Integer) session.getAttribute("reserveMinute");
        Integer personCount = (Integer) session.getAttribute("personCount");
        String planet = (String) session.getAttribute("planet");
        String selectedSeats = (String) session.getAttribute("selectedSeats");
        int price = (int) session.getAttribute("price");

        if (loginId == null || reserveDate == null || reserveHour == null || reserveMinute == null || planet == null || selectedSeats == null) {
            return "redirect:/reserve/calendar?error=missingData";
        }

        // DTO 구성
        ReserveDTO dto = new ReserveDTO();
        dto.setLoginId(loginId);
        dto.setReserveDate(reserveDate);
        dto.setReserveHour(reserveHour);
        dto.setReserveMinute(reserveMinute);
        dto.setPersonCount(personCount);
        dto.setPlanet(planet);
        dto.setSelectedSeats(selectedSeats);
        dto.setPrice(price);

        try {
            // DB 저장 처리
            reserveRepository.save(dto);
            model.addAttribute("message", "예약이 완료되었습니다!");
            return "reserve/success";  // 예약 완료 페이지
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/reserve/check?error=saveFailed";
        }
    }
 // 예약 내역 보기
    @GetMapping("/list")
    public String reservationList(HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");

        if (loginId == null) {
            return "redirect:/loginForm.do?result=notLoggedIn";
        }

        try {
            // 로그인된 사용자의 예약 내역 조회
            List<ReserveDTO> reserveList = reserveService.getReservationsByLoginId(loginId);
            model.addAttribute("reserveList", reserveList);
            return "reserve/list";  // 예약 내역을 보여주는 JSP 페이지
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/?result=reservationLoadFailed";
        }
    }

    @PostMapping("/cancel")
    public String cancelReservation(@RequestParam("id") Long reserveId, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        if (loginId == null) return "redirect:/loginForm.do?result=notLoggedIn";

        try {
            reserveService.cancelReservation(reserveId, loginId);
            return "redirect:/reserve/myReservations";
        } catch (Exception e) {
            return "redirect:/reserve/myReservations?error=cancelFailed";
        }
    }

    // 관리자 예약 승인
    @PostMapping("/approve")
    public String approveReservation(@RequestParam("id") Long reserveId, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");

        // 관리자 권한 확인 (예: "admin"으로 설정된 계정만 승인 가능하도록)
        if (loginId == null || !loginId.equals("admin")) {
            return "redirect:/loginForm.do?result=notAuthorized";
        }

        try {
            reserveService.approveReservation(reserveId);
            return "redirect:/admin/adminList";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/adminList?error=approveFailed";
        }
    }
    
    @GetMapping("/api/admin/reservations")
    @ResponseBody
    public List<ReserveDTO> getAllReservationsJson() {
        return (List<ReserveDTO>) reserveService.getAllReservations();
    }
}
