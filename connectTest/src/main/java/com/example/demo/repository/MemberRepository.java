package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	
	public Member findById(String id);

	//  Member getMember(String id); // 계정 정보 검색 -> findById
	// public List<String> getAutorities(String id); // 계정이 가지고 있는 권한 가져오기 -> Auth 레퍼지토리에서 
	// public void insertMember(Member member); // 회원등록 -> save 로 할 수 있음
	// public void insertMemberAuthority(String id, String authority); // 권한 등록 -> Auth 레퍼지토리에서
	// public List<Member> getMemberList(); // db연결 테스트용으로 만든 것 -> 만들 필요 X
	// public List<Member> getAllMembers(); // 모든 멤버 출력 -> 만들 필요 X

}
