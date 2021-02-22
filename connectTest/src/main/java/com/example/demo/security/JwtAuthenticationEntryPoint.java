package com.example.demo.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class JwtAuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
		
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
	   	if(authException instanceof UsernameNotFoundException) {
    		// 오류 중 유저 아이디가 발견되지 않았을 때
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
			return;
     	} else if (authException instanceof BadCredentialsException) {  		
            // 오류 중 id가 만료된 계정 혹은 비밀번호가 틀렸을 때
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
			return;
     	} 
	   	
		// 인증 오류 시, 401 호출
		// 클라이언트가 401을 캐치해서 refreshToken을 보낼 수 있도록
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized. You need refresh your token or login");
	}
}
