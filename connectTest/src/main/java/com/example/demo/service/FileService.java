package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.FileInfo;
import com.example.demo.repository.FileInfoRepository;

@Service
public class FileService {
	
	// @Autowired
	// FileDAO dao;
	
	@Autowired
	private FileInfoRepository repository;
	
	
	public String saveFileData(FileInfo file) {
		try {
			repository.save(file);
		} catch (Exception e) {
			return "fail";
			// TODO: handle exception
		}
		return "success";
	}
	
	public List<FileInfo> getAllFiles() {
		return repository.findAll();
	}
	
	public FileInfo getOneFileByNo(int fileNo) {
		return repository.findByFileNo(fileNo);
	}
	
	public String deleteFileData(FileInfo file) {
		try {
			repository.delete(file);
		} catch (Exception e) {
			return "fail";
			// TODO: handle exception
		}
		return "success";
	}
	
}
