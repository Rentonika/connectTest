<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="resources/js/jquery-3.4.1.min.js"></script>
<script src="resources/js/jquery-ui.min.js"></script>
<script type="text/javascript">

function loginCheck() {

	var id = $('#id').val();
	var pw = $('#pw').val();

	if(id.trim().length < 1) {
		alert('아이디를 입력해주세요.');
		return;
	}

	if(pw.trim().length < 1) {
		alert('비밀번호를 입력해주세요.');
		return;
	}	

	// 로그인 요청
	$.ajax({
		method : 'POST'
		, url : '/member/login'
		, data : {"id" : id, "password" : pw}
		, function() {

		}


	});
	
}

</script>
<link rel="icon" href="data:;base64,="> <!-- favicon io 제거 -->
</head>
<body>
<h3>Login Page</h3>
<form action="member/login" id="loginForm" method="post">
<table>
<tr>
<th>아이디</th>
<td><input type="text" id="id"></td>
</tr>
<tr>
<th>비밀번호</th>
<td><input type="password" id="pw"></td>
</tr>
</table>
<input type="button" value="로그인" onclick="loginCheck()">
<input type="reset" value="리셋">
</form>
<h5>아이디가 없으신가요? <a href="signup">회원가입</a></h5>
<c:if test="${not empty requestScope.failureMessage}">
<h5>${requestScope.failureMessage}</h5>
</c:if>
</body>
</html>