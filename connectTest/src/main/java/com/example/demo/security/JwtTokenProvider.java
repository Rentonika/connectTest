package com.example.demo.security;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;
// import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.service.MemberService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	// private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
	
	private String secretKey = "hyejun";
	
	// 토큰 유효시간 30분  - 30*60*1000L
	// 토큰 유효시간 2주 - 9000000
	private long tokenValidTime = 3*60*1000L;	// 테스트용 3분
	private long refreshTokenValidTime = 5*60*1000L; // 테스트용 5분 
	
	@Autowired
	private MemberService service;
	
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	public void setTokenValidTime(long tokenValidTime) {
		this.tokenValidTime = tokenValidTime;
	}
	
	public void setRefreshTokenValidTime(long refreshTokenValidTime) {
		this.refreshTokenValidTime = refreshTokenValidTime;
	}
	
	// 객체 초기화. secretKey를 Base64로 인코딩.
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	// JWT 토큰 생성
	public String createToken(String id, Collection<GrantedAuthority> collection) {
		Claims claims = Jwts.claims().setSubject(id); // JWT payload에 저장되는 정보 단위
		claims.put("roles", collection);
		Date now = new Date();
		return Jwts.builder()
					.setClaims(claims) // 정보 저장
					.setIssuedAt(now) // 토큰 발생 시간 정보
					.setExpiration(new Date(now.getTime() + tokenValidTime)) // 토큰 유통기한 설정 
					.signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘 & signature에 들어갈 secret 값 
					.compact();
	}
	
	// 리프레시 토큰 생성
	// 권한은 딱히 필요 없어서 생략
	public String createRefreshToken(String id) {
		Claims claims = Jwts.claims().setSubject(id); 
		Date now = new Date();
		return Jwts.builder()
					.setClaims(claims)
					.setIssuedAt(now)
					.setExpiration(new Date(now.getTime() + refreshTokenValidTime))
					.signWith(SignatureAlgorithm.HS256, secretKey)
					.compact();
	}
	
	// JWT 토큰에서 인증 정보 조회
	public Authentication getAuthentication(String token) {
		// logger.info("토큰 정보 : " + token);
		UserDetails member = service.loadUserByUsername(this.getMemberPk(token));
		return new UsernamePasswordAuthenticationToken(member, "", member.getAuthorities());
	}
	
	// 토큰에서 회원 정보 추출 -> 이 경우에는 유저 id 추출 
	public String getMemberPk(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}
	
	// Request의 Header에서 token값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN 값"
	public String resolveToken(HttpServletRequest request) {
		return request.getHeader("X-AUTH-TOKEN");
	}
	
	/* 필터는 refreshToken을 검사하는 게 아니니 상관 없음.
	// Request의 Header에서 refreshToken 값 가져오기. 
	public String resolveRefreshToken(HttpServletRequest request) {
		return request.getHeader("RefreshToken");
	}*/
	
	// 토큰의 유효성, 만료일자 확인
	public boolean validateToken(String jwtToken) {
		
		// logger.info("유효한지 검증 : " + jwtToken);
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			// e.printStackTrace();
			// logger.info("유효하지 않음");
			return false;
		}
	}

}
