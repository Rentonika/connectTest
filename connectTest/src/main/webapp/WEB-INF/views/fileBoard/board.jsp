<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>File Upload Board</title>
<link type="text/css" href="resources/css/fileBoardCSS.css" rel="stylesheet"/>
<link rel="icon" href="data:;base64,="> <!-- favicon io 제거 -->
<script src="resources/js/jquery-3.4.1.min.js"></script>
<script src="resources/js/jquery-ui.min.js"></script>
<c:if test="${not empty downloadMsg}">
<script type="text/javascript">
$(function(){
	alert("${downloadMsg}");
});
</script>
</c:if>
<script type="text/javascript">
function fileCheck(fileNo) {

	var YesOrNo = confirm('정말로 삭제하시겠습니까?');
	if(YesOrNo) {
		// Get 방식으로 요청 전송
		location.href = "deleteFile?fileNo=" + fileNo;
	} 	
}
</script>

</head>
<body>
<h3>File Upload Page</h3>

<form action="fileBoard/boardWrite" method="get">
<input type="submit" value="업로드">
</form>
<div id="boardWrapper">
   <table class="table" border="1">
      <thead class="thead-dark">
      <tr>
      	 <th>파일 넘버</th>
         <th>업로더</th>
         <th>제목</th>
         <th>파일 이름</th>
         <th>크기</th>
         <th>날짜</th>
         <th>삭제</th>
      </tr>
      </thead>
      <tbody>
      <!-- varStatus : 현재 상태를 알아낼 수 있음  -->
      <c:forEach var="file" items="${fileList}" varStatus="stat">
      <tr>
      	 <td>${file.fileNo}</td>
         <td>${file.uploader}</td>
         <td>${file.title}</td>
         <td><a href="download?fileNo=${file.fileNo}">${file.originalFileName}</a></td>
         <td>${file.size} byte</td>
         <td>${file.uploadDate}</td>
         <td>
         <c:if test="${file.uploader == sessionScope.loginId}">
         <img alt="deleteBtn" src="resources/img/Xbutton.png" id="deleteBtn" style="width: 20px; height: 14px; padding: 5px; cursor: pointer;"  onclick="fileCheck(${file.fileNo})"> 
         </c:if>
         </td>
      </tr>
      </c:forEach>
      </tbody>
   </table>

</div>
</body>
</html>