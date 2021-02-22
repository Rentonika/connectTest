<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sign up</title>
<script src="resources/js/jquery-3.4.1.min.js"></script>
<script src="resources/js/jquery-ui.min.js"></script>
<script type="text/javascript">

function idCheck() {

	var id = $('#id').val();
	var pw = $('#pw').val();

	// id 체크
	if(id.trim().length <3 || id.trim().length > 10) {
		alert('아이디를 3~10글자 내외로 입력해주세요.');
		return;
	}

	// pw 체크
	if(pw.trim().length < 3 || pw.trim().length > 7) {
		alert('비밀번호를 3~7글자 내외로 입력해주세요.');
		return;		
	}

	// 아이디 중복확인
	// controller에서 인식을 못함 -> 왜지?
	$.ajax({
		method : 'POST'
		, data : {"id" : id}
		, url : 'member/idCheck'
		, success : function(resp) {
			if(resp == 'fail') {
				alert('이미 가입된 아이디입니다.');
				$('#id').val('');
				$('#pw').val(''); 
				return;
			} else if(resp == 'success') {
				alert('가입을 축하드립니다! ' + id + '님!');
				// 정보 전달
				$('#signupForm').submit();
			}
		}
		, error : function(err) {
			alert('에러가 발생했습니다. 다시 시도해주세요.');
			return;
		}
	});

}

</script>
<link rel="icon" href="data:;base64,="> <!-- favicon io 제거 -->
</head>
<body>
<h3>회원가입</h3>
<form action="member/signup" id="signupForm" method="post">
<table border="1">
<tr>
<th>아이디 : </th>
<td><input type="text" id="id" name="id"></td>
</tr>
<tr>
<th>비밀번호 : </th>
<td><input type="password" id="pw" name="pw"></td>
</tr>
</table>
<input type="button" value="회원가입" onclick="idCheck()">
<input type="reset" value="리셋">
</form>
</body>
</html>