package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Member;
// import com.example.demo.repository.AuthorityRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.MemberService;

@RestController
@RequestMapping("/member")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService service;
	
	// @Autowired
	// MemberDAO dao;
	
	// @Autowired
	// private AuthorityRepository authRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	
	@ResponseBody // postman에서 json 확인 시, requestbody를 기입해야 함.
	@PostMapping("/idCheck")
	public String idCheck(@RequestBody Member member) {
		
		Member selectedMember = null; 
		selectedMember = service.getMemberById(member.getId());
		if(selectedMember == null) {
			return "success";
		}
		
		logger.info("가입자 존재 : " + selectedMember.getId());
		return "fail";
	}
	
	// 가입
	@ResponseBody
	@PostMapping("/signup")
	public Map<String, Object> signup(@RequestBody Member member) {
		
		logger.info("가입자 확인 : " + member);
		
		Map<String, Object> signupResult = new HashMap<String, Object>();

		Member savedMember = service.getMemberById(member.getId());
		if(savedMember != null) {
			signupResult.put("member", member);
			signupResult.put("result", "fail");
			signupResult.put("msg", "already exists");
			return signupResult;
		}
		
		savedMember = service.save(member, "ROLE_USER");
		if(savedMember == null) {
			signupResult.put("member", member);
			signupResult.put("result", "fail");
			signupResult.put("msg", "insert error");
			return signupResult;
		}
		
		signupResult.put("member", savedMember);
		signupResult.put("result", "success");
		return signupResult;
	}
	
	@RequestMapping(value = "/loginPage", method =  {RequestMethod.GET, RequestMethod.POST})
	public String loginPage() {
		
		logger.info("로그인 페이지 이동");
		return "LoginPage";
	}
	
	@ResponseBody
	@PostMapping("/login")
	public Map<String, String> login(@RequestBody Map<String, String> user) {
		
		Member member = service.getMemberById(user.get("id"));
		
		// id 검증
		if(member == null) {
			throw new UsernameNotFoundException("not existing id");
		} 
		
		// pw 검증
		if(!passwordEncoder.matches(user.get("pw"), member.getPw())) {
			throw new BadCredentialsException("password is not correct");
		}
		
		// jwtToken 생성
		String jwtToken = jwtTokenProvider.createToken(member.getId(), service.getAuthorities(member)); 
		
		// refreshToken 생성
		String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
		
		// tokenMap 생성
		Map<String, String> tokens = new HashMap<String, String>();
		tokens.put("accessToken", jwtToken);
		tokens.put("refreshToken", refreshToken);
		
		return tokens;
	}
	
	// 토큰 재발급
	@ResponseBody
	@PostMapping("/refresh")
	public Map<String, String> refreshToken(@RequestBody Map<String, String> tokens) {
		
		String accessToken = tokens.get("accessToken");
		String refreshToken = tokens.get("refreshToken");
		
		// accessToken 폐기 여부
		Boolean isAccess = jwtTokenProvider.validateToken(accessToken);
		// refreshToken 유효 여부
		Boolean isRefresh = jwtTokenProvider.validateToken(refreshToken);
				
		// access -> 유효
		if(isAccess) {
			// 굳이 새 토큰을 발급할 필요가 없는 상황 // 테스트용
			// 가끔 씹힘 -> 왜?
			logger.info("accessToken already exitsts");
			tokens.put("status", "access");
			tokens.put("result", "fail");
			tokens.put("msg", "accessToken already exists");
			
		} else if(!isAccess && isRefresh) {
			// Access 폐기가 된 상황 & Refresh는 건재
			// 새로운 access 토큰을 발급을 요청
			String id = jwtTokenProvider.getMemberPk(refreshToken);
			Member member = service.getMemberById(id);
			String newJwtToken = jwtTokenProvider.createToken(id, service.getAuthorities(member)); 

			// 반환값에 새 accessToken 삽입
			tokens.put("accessToken", newJwtToken);
			tokens.put("status", "refreshed");
			tokens.put("result", "success");
			tokens.put("msg", "new accessToken issued");
			
		} else if(!isAccess && !isRefresh) {
			// refresh 또한 폐기. 완전 새로운 로그인이 필요함.
			tokens.put("accessToken", null);
			tokens.put("refreshToken", null);
			tokens.put("status", "none");
			tokens.put("result", "fail");
			tokens.put("msg", "no refreshToken. need login");
		}
		
		// 토큰 반환
		return tokens;
	}
	
	// 로그아웃
	// 클라이언트에서 가지고 있는 해당 정보를 삭제하는 식으로
	// Redis 활용은 아직 모르겠음...
	@ResponseBody
	@PostMapping("/logout") 
	public Map<String, String> logout(@RequestBody Map<String, String> tokens) {
	 
		String accessToken = tokens.get("accessToken");
		// String refreshToken = tokens.get("refreshToken");
		
		// accessToken 폐기 여부
		Boolean isAccess = jwtTokenProvider.validateToken(accessToken);
		// refreshToken 유효 여부
		// Boolean isRefresh = jwtTokenProvider.validateToken(refreshToken);

		// Access token 이 활성화 되어 있을 경우 -> Access Token/Refresh Token 삭제 요청
		if(isAccess) { // refreshToken의 활성화 여부와 상관 없이 모두 삭제
			tokens.put("status", "access"); // status : access -> 클라이언트에서 access & refresh 둘 다 삭제
			tokens.put("result", "success");
			tokens.put("msg", "delete both token");
		} /*else if(!isAccess && isRefresh) { // 어차피 authorized 에서 걸러지니 딱히 필요 X
			// Access token 비활성화, refresh 활성화 -> refresh 필요
			tokens.put("status", "refresh");
			tokens.put("result", "fail");
			tokens.put("msg", "need refresh");
		} // 어차피 authorized 에서 걸러지니 딱히 필요 X
		else if(!isAccess && !isRefresh) {
			// 둘 다 비 활성화 -> 로그인 필요
			tokens.put("status", "none");
			tokens.put("result", "fail");
			tokens.put("msg", "need login");
		}*/
		
		return tokens;
	} 

	@GetMapping("/admin") 
	public String admin() {
		return "관리자 전용 페이지"; 
	}
		
	@GetMapping("/denialedPage") 
	public String denial() {
		return "접근 권한 부족"; 
	}
	
	// Test용 Admin 계정 부여
	@ResponseBody
	@GetMapping("/create")
	public Map<String, Object> create() {

		Member member = new Member();
		member.setId("admin");
		member.setPw("admin");
		
		Map<String, Object> adminResult = new HashMap<String, Object>();
		
		// 이미 값이 존재할 시
		Member admin = service.getMemberById(member.getId());
		if(admin != null) {
			adminResult.put("admin", member);
			adminResult.put("result", "fail");
			adminResult.put("msg", "already exists");
			return adminResult;
		}
		
		admin = service.save(member, "ROLE_ADMIN");
		// 삽입 오류 시
		if(admin == null) {
			adminResult.put("admin", member);
			adminResult.put("result", "fail");
			adminResult.put("msg", "insert error");
		}
		
		adminResult.put("admin", admin);
		adminResult.put("result", "success");
		
		return adminResult;
	}	

}
