package com.example.demo.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MainControllerImpl implements MainController{


	@Override
	@GetMapping("/main")
	public String showMain(Model model) {
		// TODO Auto-generated method stub
		return "main/main";
	}

}
