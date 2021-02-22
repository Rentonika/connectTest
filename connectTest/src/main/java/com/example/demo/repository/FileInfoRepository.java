package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.FileInfo;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long>{

	// public boolean saveFileData(FileInfo file); // save로 가능
	// public List<FileInfo> selectAllFiles(); // findAll로 가능
	public FileInfo findByFileNo(int fileNo); // findOne으로 가능
	// public FileInfo selectOneFile(int fileNo);
	// public boolean deleteFileByNo(int fileNo); // delete로 가능
}
