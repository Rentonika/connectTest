package com.example.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter  {

	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// 헤더에서 JWT 받아오기
		String token = jwtTokenProvider.resolveToken((HttpServletRequest)request);

		// logger.info("유효 검증 : " + jwtTokenProvider.validateToken(token));		
		if(token != null && jwtTokenProvider.validateToken(token)) {
			// 토큰이 유효할 시, 토큰으로부터 유저 정보를 가져옴
			// 유저 정보를 가져와서 username토큰을 생성
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			// SecurityContext에 Authentication 객체를 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} 
		
		chain.doFilter(request, response);
	}
}
