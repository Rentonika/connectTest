package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long>{

	// public List<Authority> findById(String id); // 아이디로 권한 목록 검색 -> entity 설정 해줬기 때문에 필요 X
	// public void insertMemberAuthority(String id, String authority); // 권한 등록 -> save로 가능

}

