package com.example.demo.member.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.member.dto.MemberDTO;
import com.example.demo.member.service.MemberService;
import com.example.demo.member.service.PasswordResetService;
import com.example.demo.order.dto.OrderDTO;
import com.example.demo.reserve.dto.ReserveDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberControllerImpl implements MemberController{  
   @Autowired
   MemberService service;
   
   @Autowired
   private PasswordEncoder passwordEncoder;  // 암호화기
   
   @Override
   @GetMapping("/member/memberList")
   public String memberList(Model model) {
      // TODO Auto-generated method stub
      List<MemberDTO> memberList = service.findAll();
      model.addAttribute("memberList", memberList);
      return "member/memberList";
   }

   @Override
   @GetMapping("/member/{formName:.*Form}")
   public String joinMemberForm(String formName) {
      // TODO Auto-generated method stub
      return "member/" + formName;
   }

   @PostMapping("/member/joinMember")
   public String joinMember(@ModelAttribute MemberDTO dto, HttpServletRequest request, Model model) {
       HttpSession session = request.getSession();
       String authStatus = request.getParameter("phoneAuth");

       if (!"Y".equals(authStatus)) {
    	   System.out.println(authStatus);
           model.addAttribute("error", "휴대폰 인증이 필요합니다.");
           return "member/joinMemberForm";
       }

       try {
           // 비밀번호 암호화
           String encodedPwd = passwordEncoder.encode(dto.getPwd());
           dto.setPwd(encodedPwd);

           service.insertMember(dto); // DB 저장

           model.addAttribute("message", "회원가입에 성공했습니다.");
           model.addAttribute("redirectUrl", "/member/loginForm");
           return "common/alert";

       } catch (Exception e) {
           e.printStackTrace(); // 로그 확인
           model.addAttribute("error", "회원가입 실패: " + e.getMessage());
           return "member/joinMemberForm";
       }
   }
   
   @Override
   @GetMapping("/member/detailMember")
   public String detailMember(String id, Model model) {
      // TODO Auto-generated method stub
      MemberDTO dto = service.findById(id);
      model.addAttribute("member", dto);
      return "member/detailMember";
   }

	/*
	 * @Override
	 * 
	 * @PostMapping("/updateMember") public String updateMember(MemberDTO dto, Model
	 * model) { // TODO Auto-generated method stub int result = service.merge(dto);
	 * 
	 * if(result >= 1) { model.addAttribute("message", "회원 수정에 성공했습니다.");
	 * model.addAttribute("redirectUrl", "/member/memberList"); } else {
	 * model.addAttribute("message", "회원 수정에 실패했습니다. 다시 시도하세요.");
	 * model.addAttribute("redirectUrl", "/member/detailMember?id="+dto.getId()); }
	 * return "common/alert"; }
	 */

   @GetMapping("/member/deleteAccount")
   public String deleteAccount(HttpSession session, Model model) {
       String loginId = (String) session.getAttribute("loginId");
       int result = service.remove(loginId);
       session.invalidate();

       if (result > 0) {
           model.addAttribute("message", "회원 탈퇴가 완료되었습니다.");
           model.addAttribute("redirectUrl", "/main/main");
       } else {
           model.addAttribute("message", "탈퇴 실패. 다시 시도해주세요.");
           model.addAttribute("redirectUrl", "/member/myPage");
       }
       return "common/alert";
   }
// 로그인 폼

   @GetMapping("/member/login")
   public String login() {
       return "member/loginForm";
   }
   @GetMapping("/member/findId")
   public String findId() {
       return "member/findId";
   }
   @GetMapping("/member/findPassword")
   public String findPassword() {
       return "member/findPassword";
   }

   @PostMapping("/member/login")
   public String login(String id, String pwd, Model model, HttpSession session) {
       // 로그인 실패 횟수 읽기 (처음엔 0)
       Integer failCount = (Integer) session.getAttribute("failCount");
       if (failCount == null) {
           failCount = 0;
       }

       String inputPwd = (pwd != null) ? pwd.trim() : "";
       boolean result = service.login(id, inputPwd);

       if (result) {
           // 로그인 성공하면 실패 카운트 초기화
           session.removeAttribute("failCount");

           MemberDTO dto = service.findById(id); // 사용자 정보 조회

           // 권한 설정
           List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

           // Authentication 객체 생성
           UsernamePasswordAuthenticationToken authentication =
               new UsernamePasswordAuthenticationToken(id, null, authorities);

           // SecurityContext 생성 및 설정
           SecurityContext context = SecurityContextHolder.createEmptyContext();
           context.setAuthentication(authentication);
           SecurityContextHolder.setContext(context);

           // 세션에 SecurityContext 저장
           session.setAttribute("SPRING_SECURITY_CONTEXT", context);

           // 사용자 정보 세션 저장
           session.setAttribute("loginId", id);
           session.setAttribute("loginUser", dto);

           System.out.println("Authentication: " + authentication);
           System.out.println("SecurityContext Authentication: " + context.getAuthentication());
           System.out.println("Session SPRING_SECURITY_CONTEXT: " + session.getAttribute("SPRING_SECURITY_CONTEXT"));

           // 새 세션에 SecurityContext 저장 (중복 저장 문제 방지용)
           session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

           model.addAttribute("message", id + "님 환영합니다. 로그인에 성공하였습니다.");
           model.addAttribute("redirectUrl", "/main/main");
       } else {
           // 로그인 실패 시 실패 횟수 증가 및 저장
           failCount++;
           session.setAttribute("failCount", failCount);

           if (failCount >= 5) {
        	    model.addAttribute("message", "로그인 실패가 5회 이상입니다. 보안코드를 입력하세요.");
        	    model.addAttribute("redirectUrl", "/member/captcha");
        	    return "common/alert";  // alert 보여주고 캡차 페이지로 이동
        	}

           model.addAttribute("message", "아이디나 암호가 잘못 되었습니다. 다시 로그인하세요.");
           model.addAttribute("redirectUrl", "/member/loginForm");
       }

       return "common/alert";
   }


@Override
@GetMapping("/member/logout")
public String logout(Model model, HttpSession session) {
	// TODO Auto-generated method stub
	String loginId = (String) session.getAttribute("loginId");
	session.invalidate();
	model.addAttribute("message", loginId + " 님이 로그아웃 하셨습니다.");
	model.addAttribute("redirectUrl", "/member/loginForm");
	return "/common/alert";
}
   
@GetMapping("/mypage/myPage")
public String myPage(Model model, HttpSession session) {
    String loginId = (String) session.getAttribute("loginId");

    if (loginId == null) {
        model.addAttribute("message", "로그인이 필요합니다.");
        model.addAttribute("redirectUrl", "/member/loginForm");
        return "common/alert";
    }

    MemberDTO member = service.findById(loginId);
    List<OrderDTO> orderList = service.findOrdersByMemberId(loginId);
    List<ReserveDTO> reservationList = service.findReservationsByMemberId(loginId);
    
    if (member.getProfileImage() == null || member.getProfileImage().isEmpty()) {
        member.setProfileImage("/image/profile/default-profile.png"); // 기본 이미지 경로
    }
   
    model.addAttribute("member", member);
    model.addAttribute("orderList", orderList);
    model.addAttribute("reservationList", reservationList);

    return "mypage/myPage";
}

@GetMapping("/mypage/updateForm")
public String updateForm(HttpSession session, Model model) {
    String loginId = (String) session.getAttribute("loginId");
    if (loginId == null) {
        return "redirect:/member/loginForm";
    }
    MemberDTO member = service.findById(loginId);
    model.addAttribute("member", member);
    return "mypage/updateForm";
}
@PostMapping("/mypage/updateProfile")
public String update(@RequestParam("file") MultipartFile file,
                     @ModelAttribute MemberDTO member,
                     HttpServletRequest request) throws IOException {

    if (!file.isEmpty()) {
        // 실제 저장 경로를 절대경로로 직접 지정
        String uploadDir = "C:/springboot/workspace/GoldFlow/upload/profile/";

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // 고유 파일명 생성
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File saveFile = new File(dir, fileName);
        file.transferTo(saveFile);

        // DB에 저장할 상대 URL 경로 설정 (브라우저에서 보여줄 용도)
        member.setProfileImage("/upload/profile/" + fileName);
    }

    // DB 업데이트 처리
    service.updateMember(member);

    return "redirect:/mypage/myPage";
}



@Override
public String deleteMember(String id, Model model) {
	// TODO Auto-generated method stub
	return null;
}


//
//
//  uploadProfileImage는 사용 x 일단 주석은 하지 않았지만 확인후 사용여부 확인!!!
//
//

@PostMapping("/mypage/uploadProfileImage")
@ResponseBody
public String uploadProfileImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
    if (file.isEmpty()) {
        return "/images/default-profile.png";  // 기본 이미지 경로 또는 에러 처리
    }

    try {
        // 저장할 경로 (서버 파일 시스템 또는 외부 스토리지)
        String uploadDir = request.getServletContext().getRealPath("/upload/profile/");
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String originalFilename = file.getOriginalFilename();
        // 중복 방지용 이름 변경 (UUID 등)
        String savedFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        File destination = new File(dir, savedFilename);
        file.transferTo(destination);

        // 업로드된 이미지 접근 URL (웹에서 접근 가능한 경로)
        String imageUrl = "/upload/profile/" + savedFilename;
        return imageUrl;

    } catch (IOException e) {
        e.printStackTrace();
        return "/images/default-profile.png"; // 실패 시 기본 이미지
    }
}
@GetMapping("/mypage/manageMember")
public String manageMember(HttpSession session, Model model) {
    String loginId = (String) session.getAttribute("loginId");
    if (loginId == null) {
        model.addAttribute("message", "로그인이 필요합니다.");
        model.addAttribute("redirectUrl", "/member/loginForm");
        return "common/alert";
    }

    model.addAttribute("loginId", loginId);
    return "mypage/manageMember";
}


@Override
@PostMapping("/mypage/manageMember")
public String manageMember(Model model, HttpSession session) {
	// TODO Auto-generated method stub
	return "mypage/manageMember";
}

@PostMapping("/member/sendCode")
@ResponseBody
public String sendPhoneCode(@RequestBody MemberDTO dto) {
    // 로직
    return "SUCCESS";
}

@Override
public String joinMember(MemberDTO dto, Model model) {
	// TODO Auto-generated method stub
	return null;
}

@GetMapping("/member/checkId")
@ResponseBody
public String checkId(@RequestParam("id") String id) {
    boolean exists = service.existsById(id);
    return exists ? "DUPLICATE" : "OK";
}

@Override
@GetMapping("/mypage/changePassword")
public String showChangePassword() {
	// TODO Auto-generated method stub
	return "mypage/changePassword";
}

@Override
@PostMapping("/mypage/changePassword")
public String changePassword(String currentPassword, String newPassword, String confirmPassword, HttpSession session,
		Model model) {
	// TODO Auto-generated method stub
	
	String loginId = (String) session.getAttribute("loginId");
	
	if(!newPassword.equals(confirmPassword)) {
		 model.addAttribute("message", "새 비밀번호가 일치하지 않습니다.");
	     model.addAttribute("redirectUrl", "/mypage/changePassword");
	     return "common/alert";
	}
	
	boolean result = service.changePassword(loginId, currentPassword, newPassword);
	if(!result) {
		 model.addAttribute("message", "현재 비밀번호가 일치하지 않거나 오류가 발생했습니다.");
	     model.addAttribute("redirectUrl", "/mypage/changePassword");
	     return "common/alert";
	}
	
	 model.addAttribute("message", "비밀번호가 변경되었습니다.");
     model.addAttribute("redirectUrl", "/mypage/myPage");
     return "common/alert";
}
@GetMapping("/member/find-Id")
public String findIdForm() {
    return "member/findId"; // JSP 폼 뷰
}

@PostMapping("/member/findId")
public String findIdResult(@RequestParam("name") String name,
                           @RequestParam("email") String email,
                           Model model) {
    String userId = service.findUserIdByEmailAndName(email, name);
    if (userId != null) {
        model.addAttribute("result", "사용자 ID는: " + userId);
    } else {
        model.addAttribute("result", "해당 이메일로 등록된 사용자가 없습니다.");
    }
    return "member/checkId"; // 결과를 보여줄 JSP
}



}







