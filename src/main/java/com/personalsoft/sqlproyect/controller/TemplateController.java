package com.personalsoft.sqlproyect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {
	
	@GetMapping("/index")
	public String getAll(Model model) {
		model.addAttribute("mensaje","¡Hola Mundo!");
		return "index";		
	}

}
