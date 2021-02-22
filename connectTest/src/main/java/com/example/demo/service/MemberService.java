package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Authority;
import com.example.demo.entity.Member;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.MemberRepository;

@Service
public class MemberService implements UserDetailsService {

	// @Autowired
	// MemberDAO dao;
	
	@Autowired
	private MemberRepository repository;
	
	@Autowired
	private AuthorityRepository authRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	private static final  Logger logger = LoggerFactory.getLogger(MemberService.class);
	
	@Override
	public User loadUserByUsername(String id) throws UsernameNotFoundException {
		
		Member member = repository.findById(id);
		if(member == null) {
			logger.info("존재하지 않는 아이디 ID : " + id);
			throw new UsernameNotFoundException(id + "는 존재하지 않는 아이디입니다. 회원가입을 진행해주세요.");
		}
		logger.info("Found ID : " + member);
		// member.setAuthorities(getAuthorities(id));
		
		User authorizedMember = new User(member.getId(), member.getPw(), getAuthorities(member));
		return authorizedMember;
	}
	
	public Collection<GrantedAuthority> getAuthorities(Member member) { 
		List<Authority> string_authorities = member.getAuthorities();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
		for (Authority authority : string_authorities) { 
			authorities.add(new SimpleGrantedAuthority(authority.getAuthority())); 
		} 
		return authorities; 
	}
	
	public Member save(Member member, String role) {
		// TODO Auto-generated method stub
		// 비밀번호 암호화 해서 셋팅
		member.setPw(passwordEncoder.encode(member.getPw()));
		member.setEnabled(true);

		// auth 설정
		Authority auth = new Authority();
		// auth.setId(member.getId());
		auth.setAuthority(role);
		auth.setMember(member);
		
		Member newMember = null; 
		
		// 오류 일 시
		try {
			// 사용자, 권한 동시 저장
			repository.save(member);
			newMember = authRepository.save(auth).getMember();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("member insert error : " + member);
			return newMember;
			// throw new RuntimeException("This ID already exists");
		}
		
		return newMember;
	}
	
	public Member getMemberById(String id) {
		
		return repository.findById(id);
	}

}
