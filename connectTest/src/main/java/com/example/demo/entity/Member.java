package com.example.demo.entity;

import java.util.List;

// import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
// import lombok.Builder;
// import lombok.Getter;
import lombok.ToString;

// @Getter 
@Data
@ToString(exclude = "authorities") // toString 오류 때문에 예외로 생성하지 않음
@Entity
@Table(name = "MEMBER")
// @SuppressWarnings("serial")
public class Member{

	@Id
	@Column(name = "ID")
	private String id;
	private String pw;
	@Column(name = "ISENABLED") // jpa 칼럼 -> 칼.. 뭐시기 타입인 경우에는 기본적으로 _로 표현 // ex - isEnabled => IS_ENALBED
	private boolean isEnabled;
	 
	// 연관관계 매핑
	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
	private List<Authority> authorities;
	// private Collection <? extends GrantedAuthority> authorities;

/*	@Builder
	protected Member(String id, String pw, Boolean isEnabled, Collection <? extends GrantedAuthority> authorities) {
		this.id = id;
		this.pw = pw;
		this.isEnabled = isEnabled;
		this.authorities = authorities;
	}*/
	
	// 사용자의 권한을 콜렉션 형태로 반환
	// 단, 클래스 자료형은 GrantedAuthority을 구현해야함
/*	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		
		 * Set<GrantedAuthority> authList = new HashSet<GrantedAuthority>();
		 * authList.add(new SimpleGrantedAuthority(auth)); return authList;
		 
		return this.authorities;
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.pw;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.id;
	}
	
	// 계정 만료 여부 반환
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true; // -> 만료 X
	}
	
	// 계정 잠금 여부 반환
	@Override
	public boolean isAccountNonLocked() {
		
		// 계정이 잠금 되었는지 확인
		// TODO Auto-generated method stub
		return true; // true -> 잠금 X
	}
	
	// 패스워드 만료 여부 반환
	@Override
	public boolean isCredentialsNonExpired() {
		
		// 패스워드가 만료되었는지 확인
		// TODO Auto-generated method stub
		return true; // true -> 만료되지 않았음 
	}
	
	// 계정 사용 가능 여부 반환
	@Override
	public boolean isEnabled() {
		
		// 계정이 사용 가능한지 확인하는 로직
		// TODO Auto-generated method stub
		return this.isEnabled;
	}*/
}
