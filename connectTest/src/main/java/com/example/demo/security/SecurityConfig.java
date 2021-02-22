package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	// 비밀번호를 암호화 할 때 사용할 인코더를 미리 빈으로 등록
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// 접근 불가 페이지 핸들러 빈 등록
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new JwtAccessDeniedHandler();
	}
	
	// 로그인 검증을 수행 빈 등록
	/*
	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new MemberAuthenticationProvider();
	}*/
	
	
	/*
	 * // 로그인 실패시 핸들러 빈 등록
	 * 
	 * @Bean public MemberAuthFailureHandler authFailureHandler() { return new
	 * MemberAuthFailureHandler(); }
	 */
	
	// 인증 관련 오류 엔트리포인트
	@Bean
	public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint();
	}
	
	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	
		// 모든 페이지 잠금 풀림
		// web.ignoring().antMatchers("/**");
		
		// resource 영역만 해제
		web.ignoring().antMatchers("/resources/**");
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		// crof 설정 해제, js 사용을 위함
		http
			.csrf().disable();
		
		// 리소스 외의 페이지 인증/비인증/인증권한을 설정
		http
			.authorizeRequests()
				.antMatchers("/member/loginPage", "/member/signup", "/member/create", "/member/idCheck", "/member/login", "/member/refresh").permitAll() // 누구나 접근 가능
				.antMatchers("/").hasAnyRole("USER", "ADMIN") // USER, ADMIN만 접근 가능
				.antMatchers("/member/admin").hasRole("ADMIN") // ADMIN만 접근 가능
				.anyRequest().authenticated() // 나머지 요청은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
			.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증 -> 세션 사용 X
			.and()
				.exceptionHandling()
					.accessDeniedHandler(accessDeniedHandler()) // 접근 불가 홈페이지 접근 시
					.authenticationEntryPoint(jwtAuthenticationEntryPoint()) // 인증 오류 시, 401 발생 -> 클라이언트에서 결과 받고 refresh 하도록	
			.and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), 
					UsernamePasswordAuthenticationFilter.class); // JwtAuthenticationFilter 를 UsernamePasswordAuthenticationFilter 전에 넣음.
		
	}
	 
}
