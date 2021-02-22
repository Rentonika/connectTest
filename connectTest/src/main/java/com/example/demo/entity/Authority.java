package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


//import lombok.Builder;
import lombok.Data;
// import lombok.Getter;

// 조인 테이블 설정 -> Authority 테이블은 생략..
@Data
@Entity
@Table(name = "MEMBER_AUTHORITY")
@SequenceGenerator(
		name = "SEQ_AUTHORITY_GENERATOR"
		, sequenceName = "SEQ_AUTHORITY"
		, initialValue = 1, allocationSize = 1) // 시퀸스 전략 설정. 초기값 1. 증분값 1
public class Authority {
  
	@Id
	@GeneratedValue (strategy = GenerationType.SEQUENCE
	, generator = "SEQ_AUTHORITY_GENERATOR")
	@Column(name = "ID")
	private int id;
	@Column(name = "AUTHORITY_NAME")
	private String authority;

	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	private Member member;
	/*
	@Builder
	public Authority(String id, String authority) {
		this.id = id;
		this.authority = authority;
	}
	*/
}
