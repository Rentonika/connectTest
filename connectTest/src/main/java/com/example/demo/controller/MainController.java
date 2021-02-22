package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@GetMapping("/")
	public UsernamePasswordAuthenticationToken main(HttpSession session, UsernamePasswordAuthenticationToken token) {
		
		logger.info("[Main] 사용자 정보 : " + token);
		// session.setAttribute("loginId", token.getName());
		return token;
	}
}
