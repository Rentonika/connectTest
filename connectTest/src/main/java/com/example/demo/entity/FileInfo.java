package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

// import lombok.Builder;
import lombok.Data;
// import lombok.Getter;

//@Getter
@Data
@Entity
@Table(name = "FILEINFO")
@SequenceGenerator(
		name = "SEQ_FILEINFO_GENERATOR"
		, sequenceName = "SEQ_FILEINFO"
		, initialValue = 1, allocationSize = 1) // 시퀸스 전략 설정. 초기값 1. 증분값 1
public class FileInfo {

	@Id 
	@GeneratedValue (strategy = GenerationType.SEQUENCE
					, generator = "SEQ_FILEINFO_GENERATOR")
	@Column(name = "FILENO")
	private int fileNo;
	private String uploader;
	private String title;
	@Column(name = "UPLOADDATE")
	private String uploadDate;
	private Long size;
	@Column(name = "ORIGINALFILENAME")
	private String originalFileName;
	@Column(name = "SAVEDFILENAME")
	private String savedFileName;
	
/*	@Builder
	public FileInfo(int fileNo, String uploader, String title, 
						String uploadDate,
						Long size,
						String originalFileName,
						String savedFileName) {
		
		this.fileNo = fileNo;
		this.uploader = uploader;
		this.title = title;
		this.uploadDate = uploadDate;
		this.size = size;
		this.originalFileName = originalFileName;
		this.savedFileName = savedFileName;
	} */
}
