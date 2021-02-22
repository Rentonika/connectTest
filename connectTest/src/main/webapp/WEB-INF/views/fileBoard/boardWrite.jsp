<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>File Upload</title>
<link rel="icon" href="data:;base64,="> <!-- favicon io 제거 -->
<script src="resources/js/jquery-3.4.1.min.js"></script>
<script src="resources/js/jquery-ui.min.js"></script>
<script type="text/javascript">

function sendFile() {
	var title = $('#title').val();
	if(title.trim().length < 1) {
		alert('제목을 입력해주세요.');
	}

	var upload = $('#upload').val();
	if(upload.length < 1) {
		alert('파일을 업로드해주세요.');
	}

	$('#boardForm').submit();
}

</script>
<c:if test="${not empty fileError}">
<script type="text/javascript">
$(function(){
	alert("${fileError}");
});
</script>
</c:if>
</head>
<body>
<h4>File Upload</h4>
<div id="wrapper">
      <h2>[ 게시글 쓰기 ]</h2>
      <!-- 화면 요청이 아니라 처리 요청 -->
      <!-- multipart/form-data : 반드시 요청방식이 post여야 함  -->
      <form action="fileBoard/upload" id="boardForm" method="post" enctype="multipart/form-data">
      <table border="1">
         <tr>
            <th>업로더</th>
            <td>${sessionScope.loginId}</td>
         </tr>
         <tr>
            <th>제목</th>
            <td>
               <input type="text" id="title" name="title">
            </td>
         </tr>
         <tr>
            <th>첨부파일</th>
            <th>
               <input type="file" id="upload" name="upload" value="파일첨부">
            </th>
         </tr>
         <tr>
            <th colspan="2">
               <input type="button" value="업로드" onclick="sendFile()">
               <input type="reset" value="취소">
            </th>
         
         </tr>
      
      </table>
      
      
      </form>
   
   </div>
</body>
</html>