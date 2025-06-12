package com.example.demo.member.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream; // ✅ 이 줄 추가
import javax.imageio.ImageIO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // ✅ verifyCaptcha 메서드에서 필요
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.member.util.CaptchaUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
public class CaptchaController {

	 @GetMapping("/member/captcha")
	    public String showCaptchaPage() {
	        return "member/captcha";  // 캡차 입력 폼 JSP or HTML 뷰 이름
	    }

	    @GetMapping("/member/captchaImage")
	    public void getCaptchaImage(HttpServletResponse response, HttpSession session) throws IOException {
	        String captchaText = CaptchaUtil.generateCaptchaText(6);
	        BufferedImage captchaImage = CaptchaUtil.createCaptchaImage(captchaText);

	        session.setAttribute("captcha", captchaText);

	        response.setContentType("image/png");
	        OutputStream os = response.getOutputStream();
	        ImageIO.write(captchaImage, "png", os);
	        os.close();
	    }

	    @PostMapping("/member/verifyCaptcha")
	    public String verifyCaptcha(@RequestParam("captcha") String inputCaptcha, HttpSession session, Model model) {
	        String realCaptcha = (String) session.getAttribute("captcha");

	        if (realCaptcha != null && realCaptcha.equalsIgnoreCase(inputCaptcha)) {
	            // 캡차 통과 시 실패 카운트 초기화 후 로그인 폼으로 이동
	            session.removeAttribute("failCount");
	            session.removeAttribute("captcha");

	            model.addAttribute("message", "보안코드 인증에 성공했습니다. 다시 로그인 해주세요.");
	            model.addAttribute("redirectUrl", "/member/loginForm");
	            return "common/alert";
	        } else {
	            model.addAttribute("msg", "보안코드가 일치하지 않습니다.");
	            return "member/captcha"; // 다시 캡차 폼으로
	        }
	    }

}
