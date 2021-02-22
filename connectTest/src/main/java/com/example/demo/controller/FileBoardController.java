package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.FileService;
import com.example.demo.util.FileUtil;

import com.example.demo.entity.FileInfo;

@RestController
@RequestMapping("/fileBoard")
public class FileBoardController {

	private static final Logger logger = LoggerFactory.getLogger(FileBoardController.class);
	
	// C드라이브 파일 경로 
	private final String uploadPath = "C:/uploadPath";
	
	// 파일 서비스
	@Autowired
	private FileService fileService;
	
	// 보드 요청 
	@ResponseBody
	@GetMapping("")
	public List<FileInfo> fileBoard() {
		
		// model.addAttribute("fileList", fileService.getAllFiles());
		return fileService.getAllFiles();
	}
	
	/* 글 쓰러 가기
	 * @GetMapping("/boardWrite") public String boardWrite() {
	 * 
	 * return "fileBoard/boardWrite"; }
	 */
	
	/*
	 * 파일 업로드
	 */
	@ResponseBody
	@PostMapping("/upload")
	public String fileUpload(Principal principal, FileInfo file, MultipartFile upload) {
		
		// 설정값 저장
		file.setUploader(principal.getName());
		file.setOriginalFileName(upload.getOriginalFilename());
		file.setSize(upload.getSize());
		SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Calendar uploadDate = Calendar.getInstance();
		file.setUploadDate(dateFormat.format(uploadDate.getTime()));
		
		// 파일 저장 : 경로 & DB
		String savedFileName = FileUtil.saveFile(file, upload, uploadPath);
		file.setSavedFileName(savedFileName);
		return fileService.saveFileData(file);
	}
	
	@ResponseBody
	@GetMapping("/download")
	public String download(@RequestBody FileInfo info, HttpServletResponse response) throws Exception {
	
		logger.info("다운로드 로그 : " + info);
		FileInfo downloadFile = fileService.getOneFileByNo(info.getFileNo());
		
		try {
			   String savedFileName = downloadFile.getSavedFileName();
			   String filePath = uploadPath + "/" + savedFileName;
			   
			   try {
				   File file = new File(filePath);
				   FileInputStream in = new FileInputStream(filePath);
			    
				   response.setContentType(URLConnection.guessContentTypeFromStream(in));
				   response.setContentLength(Files.readAllBytes(file.toPath()).length);
				   // 다운 받을 때 파일 이름을 원본 이름으로 설정
				   response.setHeader("Content-Disposition","attachment;filename=" +URLEncoder.encode(downloadFile.getOriginalFileName(), "UTF-8"));
			    
				   // 파일 카피
				   FileCopyUtils.copy(in, response.getOutputStream());
				   in.close();
			    
			   		} catch (FileNotFoundException e) {
			   				// e.printStackTrace();
			   				return "there is no file";
			   		} catch (IOException e) {
			   				// e.printStackTrace();
			   				return "download error";
			   		}
				}
			   catch (Exception e) {
				   e.printStackTrace();
				   return "no file data. download fail";
			  }
		return "success";
	}
	
	@ResponseBody
	@GetMapping("/delete")
	public String deleteFile(@RequestBody FileInfo info) {
		
		String savedFileName = fileService.getOneFileByNo(info.getFileNo()).getSavedFileName();				
		String fullPath = uploadPath + "/" + savedFileName;
		boolean delResult = false;
		
		try {
			delResult = FileUtil.deleteFile(fullPath);
		} catch (Exception e) {
			// 경로 삭제 실패 시, fail 반환
			e.printStackTrace();
			return "path delete fail";
			// TODO: handle exception
		}
		
		// 삭제 성공 시, DB 기록 역시 삭제
		if(delResult) {
			fileService.deleteFileData(info);
			return "delete success";
		} 
		
		// DB에서 삭제 못한 경우도, fail 반환
		return "db delete fail";
	}
	
}
