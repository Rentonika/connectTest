package com.example.demo.util;

import java.io.File;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.FileInfo;

public class FileUtil {
	
	/**
	 * 파일 저장
	 * @param file
	 * @param upload
	 * @param uploadPath
	 * @return
	 */
	public static String saveFile(FileInfo file, MultipartFile upload, String uploadPath) {
		
		File path = new File(uploadPath);
		if(!path.isDirectory()) {
			
			// 경로가 존재하지 않을 경우, 디렉토리를 만듬
			path.mkdirs();
		}
		
		// 파일 이름이 없거나 파일이 올라오지 않은 경우
		String originalFileName = upload.getOriginalFilename();
		if(originalFileName.trim().length() == 0 || upload.isEmpty()) {
			return "";
		}
		
		// 파일 이름명 변경
		String sdf = UUID.randomUUID().toString();
		
		// 원본 파일의 확장자와 파일명 분리
		String fileName;	// 확장자를 뺀 파일명
		String ext;			// 확장명
		String savedFileName;	// db 저장할 이름
		
		int lastIndex = originalFileName.lastIndexOf(".");
		
		// 확장자가 없는 경우
		if (lastIndex == -1) {
			ext = "";
			fileName = originalFileName;
		} else {
			ext = "." + originalFileName.substring(lastIndex + 1);
			fileName = originalFileName.substring(0, lastIndex);
		}
		
		// DB & 경로에 저장될 파일명
		savedFileName = fileName + "_" + sdf + ext;
		File serverFileName = null;
		serverFileName = new File(uploadPath + "/" + savedFileName);
		
		// 파일 저장
		try {
			// 서버 경로 저장
			upload.transferTo(serverFileName);
			
		} catch (Exception e) {
			e.printStackTrace();
			savedFileName = null;
			// TODO: handle exception
		}
		
		return savedFileName;
	}
	
	/**
	 * 전달된 경로에 파일이 있을 시 삭제, 없을 시 삭제X
	 * @param fullPath
	 * @return
	 */
	public static boolean deleteFile(String fullPath) {
		
		// 전달된 경로로 파일 객체 생성
		File delFile = new File(fullPath);
		
		if(delFile.isFile()) {
			delFile.delete();
			return true;
		} 
		
		return false;
		
	}

}
