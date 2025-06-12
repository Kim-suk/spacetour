package com.example.demo.reserve.controller;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.reserve.dto.PaymentHistory;
import com.example.demo.reserve.dto.ReserveDTO;
import com.example.demo.reserve.repository.PaymentHistoryRepository;
import com.example.demo.reserve.service.PaymentHistoryService;
import com.example.demo.reserve.service.ReserveService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class PaymentController {

	@Autowired
	private PaymentHistoryRepository paymentHistoryRepository;	
	@Autowired
	private PaymentHistoryService paymentHistoryService;
	@Autowired
	private ReserveService reserveService;
	
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    // 결제 페이지
    @GetMapping("/payment/payment")
    public String paymentPage(HttpSession session, Model model) {
        if (session.getAttribute("orderId") == null) {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            int randomNum = new Random().nextInt(900000) + 100000;
            String orderId = "ORD" + date + "-" + randomNum;
            session.setAttribute("orderId", orderId);
        }

        Object priceObj = session.getAttribute("price");
        Object loginIdObj = session.getAttribute("loginId");
        Object planetObj = session.getAttribute("planet");
        Object orderIdObj = session.getAttribute("orderId");

        Integer price = 0;
        if (priceObj instanceof Integer) {
            price = (Integer) priceObj;
        } else if (priceObj instanceof String) {
            try {
                price = Integer.parseInt((String) priceObj);
            } catch (NumberFormatException e) {
                price = 0;
            }
        }

        String loginId = (loginIdObj != null) ? loginIdObj.toString() : "익명";
        String planet = (planetObj != null) ? planetObj.toString() : "미상 상품";
        String orderId = (orderIdObj != null) ? orderIdObj.toString() : "주문번호 없음";

        model.addAttribute("loginId", loginId);
        model.addAttribute("planet", planet);
        model.addAttribute("price", price);
        model.addAttribute("orderId", orderId);

        return "payment/payment";
    }

    // 결제 성공 페이지
    @GetMapping("/payment/success")
    public String showSuccessPage(HttpSession session, Model model) {
        Object priceObj = session.getAttribute("price");
        Object loginIdObj = session.getAttribute("loginId");
        Object planetObj = session.getAttribute("planet");
        Object orderIdObj = session.getAttribute("orderId");

        Integer amount = 0;
        if (priceObj instanceof Integer) {
            amount = (Integer) priceObj;
        } else if (priceObj instanceof String) {
            try {
                amount = Integer.parseInt((String) priceObj);
            } catch (NumberFormatException e) {
                amount = 0;
            }
        }

        String loginId = (loginIdObj != null) ? loginIdObj.toString() : "익명";
        String planet = (planetObj != null) ? planetObj.toString() : "미상 상품";
        String orderId = (orderIdObj != null) ? orderIdObj.toString() : "주문번호 없음";

        model.addAttribute("orderId", orderId);
        model.addAttribute("amount", amount);
        model.addAttribute("planet", planet);
        model.addAttribute("customerName", loginId);

        return "payment/success";
    }

    // 세션 기반 사용자 정보
    @GetMapping("/user/getUserInfo")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        if (loginId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Object> user = new HashMap<>();
        user.put("name", loginId);
        user.put("email", loginId + "@example.com");

        return ResponseEntity.ok(user);
    }

    // 인증 정보 기반 사용자 정보
    @GetMapping("/api/user/info")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserInfo(Authentication authentication, HttpSession session) {
        String loginId = (authentication != null && authentication.isAuthenticated()) 
            ? ((User) authentication.getPrincipal()).getUsername() 
            : (String) session.getAttribute("loginId");

        if (loginId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Object> user = new HashMap<>();
        user.put("name", loginId);
        user.put("email", loginId + "@example.com");

        return ResponseEntity.ok(user);
    }
    
    // 결제 성공 시 DB 저장 
    @PostMapping("/payment/confirm-payment")
    @ResponseBody
    public ResponseEntity<String> confirmPayment(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        String orderId = (String) session.getAttribute("orderId");
        Integer amount = (Integer) session.getAttribute("price");

        if (loginId == null || orderId == null || amount == null) {
            return ResponseEntity.badRequest().body("세션 정보 누락");
        }

        PaymentHistory payment = new PaymentHistory();
        payment.setLoginId(loginId);
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());

        paymentHistoryService.savePaymentHistory(payment);

        return ResponseEntity.ok("결제 이력 저장 성공");
    }

    // Stripe 결제 Intent 생성
    @PostMapping("payment/create-payment-intent")
    @ResponseBody
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> payload) {
        try {
            int amount = (int) payload.get("amount");
            String currency = (String) payload.get("currency");

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) amount)
                    .setCurrency(currency)
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", intent.getClientSecret());

            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 생성 중 오류 발생"));
        }
    }
    @GetMapping("/payment/history")
    public String getPaymentHistory(HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");

        if (loginId == null) {
            return "redirect:/login";  // 로그인 페이지 URL 확인 필요
        }

        String orderId = (String) session.getAttribute("orderId");
        List<PaymentHistory> histories = paymentHistoryService.getHistoriesByLoginId(loginId);

        model.addAttribute("histories", histories);
        model.addAttribute("orderId", orderId);
        model.addAttribute("customerName", loginId);

        return "payment/history";  // 뷰 파일이 존재하는지 반드시 확인하세요
    }





}
