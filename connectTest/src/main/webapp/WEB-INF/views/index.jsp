<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="resources/js/jquery-3.4.1.min.js"></script>
<script src="resources/js/jquery-ui.min.js"></script>
<title>Insert title here</title>
<link rel="icon" href="data:;base64,="> <!-- favicon io 제거 -->
</head>
<body>
<h5>test</h5>
${sessionScope.loginId} 님 환영합니다.

<ul>
<c:if test="${not empty sessionScope.loginId}">
<li><a href="member/logout">로그아웃</a></li>
</c:if>
<li><a href="member/goTest">db 테스트 페이지</a></li>
<li><a href="member/admin">관리자 페이지</a></li>
<li><a href="fileBoard">파일 업로드</a></li>
</ul>

</body>
</html>