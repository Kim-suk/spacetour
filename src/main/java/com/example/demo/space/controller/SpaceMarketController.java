package com.example.demo.space.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.space.service.SpaceMarketService;

@Controller
@RequestMapping("/space")
public class SpaceMarketController {

    @Autowired
    private SpaceMarketService service;

    @GetMapping("/star")
    public String viewStar(Model model) {
        model.addAttribute("dataList", service.getMarketData("star"));
        return "space/star";
    }
    
    @GetMapping("/cesium")
    public String viewCesium(Model model) {
        model.addAttribute("dataList", service.getMarketData("star"));
        return "space/cesium";
    }


}
